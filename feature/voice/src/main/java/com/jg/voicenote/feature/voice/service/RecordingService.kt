package com.jg.voicenote.feature.voice.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

import com.jg.voicenote.core.domain.usecase.SaveVoiceRecordUseCase
import com.jg.voicenote.core.model.RecordingState
import com.jg.voicenote.core.model.VoiceRecord
import com.jg.voicenote.feature.voice.recorder.AudioRecorder
import com.jg.voicenote.feature.voice.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

/**
 * 백그라운드 녹음을 위한 포그라운드 서비스
 */
@AndroidEntryPoint
class RecordingService : Service() {
    
    @Inject
    lateinit var audioRecorder: AudioRecorder
    
    @Inject
    lateinit var saveVoiceRecordUseCase: SaveVoiceRecordUseCase
    
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var timerJob: Job? = null
    private var monitoringJob: Job? = null
    private var currentOutputFile: File? = null
    private var recordingStartTime: Long = 0
    private var maxRecordingDurationMinutes: Int = 60 // 기본값 60분
    private val maxFileSizeBytes: Long = 2L * 1024 * 1024 * 1024 // 2GB
    
    companion object {
        const val ACTION_START_RECORDING = "START_RECORDING"
        const val ACTION_STOP_RECORDING = "STOP_RECORDING"
        const val ACTION_PAUSE_RECORDING = "PAUSE_RECORDING"
        const val ACTION_RESUME_RECORDING = "RESUME_RECORDING"
        
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "recording_channel"
        
        fun startRecording(context: Context) {
            val intent = Intent(context, RecordingService::class.java).apply {
                action = ACTION_START_RECORDING
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
        
        fun stopRecording(context: Context) {
            val intent = Intent(context, RecordingService::class.java).apply {
                action = ACTION_STOP_RECORDING
            }
            context.startService(intent)
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        observeRecordingState()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_RECORDING -> startRecording()
            ACTION_STOP_RECORDING -> stopRecording()
            ACTION_PAUSE_RECORDING -> pauseRecording()
            ACTION_RESUME_RECORDING -> resumeRecording()
        }
        return START_NOT_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun startRecording() {
        val outputFile = createOutputFile()
        currentOutputFile = outputFile
        recordingStartTime = System.currentTimeMillis()
        
        serviceScope.launch {
            if (audioRecorder.startRecording(outputFile)) {
                startForeground(NOTIFICATION_ID, createNotification(getString(R.string.notification_recording)))
                startTimer()
                startMonitoring() // 장시간 녹음 모니터링 시작
            } else {
                stopSelf()
            }
        }
    }
    
    private fun stopRecording() {
        val filePath = audioRecorder.stopRecording()
        stopTimer()
        stopMonitoring() // 모니터링 중지
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        
        // 녹음 완료 시 데이터베이스에 저장
        filePath?.let { path ->
            currentOutputFile?.let { file ->
                saveRecordingToDatabase(file, path)
            }
        }
        
        stopSelf()
    }
    
    private fun saveRecordingToDatabase(file: File, filePath: String) {
        serviceScope.launch {
            try {
                val duration = System.currentTimeMillis() - recordingStartTime
                val fileSize = file.length()
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                
                val voiceRecord = VoiceRecord(
                    title = getString(R.string.recording_title_prefix) + "_$timestamp",
                    filePath = filePath,
                    duration = duration,
                    fileSize = fileSize,
                    createdAt = LocalDateTime.now()
                )
                
                saveVoiceRecordUseCase(voiceRecord)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun pauseRecording() {
        audioRecorder.pauseRecording()
        stopTimer()
        stopMonitoring() // 일시정지 시 모니터링도 중지
        updateNotification(getString(R.string.notification_paused))
    }
    
    private fun resumeRecording() {
        audioRecorder.resumeRecording()
        startTimer()
        startMonitoring() // 재개 시 모니터링도 재시작
        updateNotification(getString(R.string.notification_recording))
    }
    
    /**
     * 장시간 녹음 모니터링 시작
     */
    private fun startMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = serviceScope.launch {
            while (isActive) {
                checkMaxRecordingDuration()
                checkFileSizeLimit()
                checkStorageSpace()
                delay(10000) // 10초마다 체크
            }
        }
    }
    
    /**
     * 모니터링 중지
     */
    private fun stopMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = null
    }
    
    /**
     * 최대 녹음 시간 체크
     */
    private fun checkMaxRecordingDuration() {
        val currentDuration = System.currentTimeMillis() - recordingStartTime
        val maxDurationMs = maxRecordingDurationMinutes * 60 * 1000L // 분을 밀리초로 변환
        
        if (currentDuration >= maxDurationMs) {
            // 최대 녹음 시간 도달 시 자동 중지
            serviceScope.launch {
                stopRecording()
            }
        }
    }
    
    /**
     * 파일 크기 제한 체크
     */
    private fun checkFileSizeLimit() {
        currentOutputFile?.let { file ->
            if (file.exists() && file.length() >= maxFileSizeBytes) {
                // 파일 크기 제한 도달 시 자동 중지
                serviceScope.launch {
                    stopRecording()
                }
            }
        }
    }
    
    /**
     * 저장 공간 체크
     */
    private fun checkStorageSpace() {
        val recordingsDir = File(getExternalFilesDir(null), "recordings")
        val freeSpace = recordingsDir.freeSpace
        val requiredSpace = 100L * 1024 * 1024 // 100MB 예상 필요 공간
        
        if (freeSpace < requiredSpace) {
            // 저장 공간 부족 시 경고 알림 업데이트
            updateNotification(getString(R.string.notification_storage_warning))
        }
    }
    
    private fun createOutputFile(): File {
        val recordingsDir = File(getExternalFilesDir(null), "recordings")
        if (!recordingsDir.exists()) {
            recordingsDir.mkdirs()
        }
        
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File(recordingsDir, "recording_$timestamp.m4a")
    }
    
    private fun observeRecordingState() {
        audioRecorder.recordingState
            .onEach { state ->
                            when (state) {
                RecordingState.RECORDING -> updateNotification(getString(R.string.notification_recording))
                RecordingState.PAUSED -> updateNotification(getString(R.string.notification_paused))
                RecordingState.STOPPED -> {
                    stopTimer()
                    stopMonitoring()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        stopForeground(STOP_FOREGROUND_REMOVE)
                    } else {
                        @Suppress("DEPRECATION")
                        stopForeground(true)
                    }
                    stopSelf()
                }
                else -> {}
            }
            }
            .launchIn(serviceScope)
    }
    
    private fun startTimer() {
        timerJob?.cancel()
        timerJob = serviceScope.launch {
            while (isActive) {
                audioRecorder.updateRecordingDuration()
                delay(1000)
            }
        }
    }
    
    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.notification_channel_description)
                setSound(null, null)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(contentText: String): Notification {
        // MainActivity를 직접 참조하지 않고 패키지명으로 Intent 생성
        val intent = Intent().apply {
            setClassName(this@RecordingService, "com.jg.voicenote.MainActivity")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_media_play) // 기본 안드로이드 아이콘 사용
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }
    
    @Suppress("MissingPermission")
    private fun updateNotification(contentText: String) {
        val notification = createNotification(contentText)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        audioRecorder.release()
        serviceScope.cancel()
    }
}
