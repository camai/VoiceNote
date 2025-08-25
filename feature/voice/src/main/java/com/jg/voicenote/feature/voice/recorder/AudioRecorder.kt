package com.jg.voicenote.feature.voice.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.jg.voicenote.core.datastore.VoiceNotePreferences
import com.jg.voicenote.core.model.RecordingState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AudioRecorder @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferences: VoiceNotePreferences
) {
    
    private var mediaRecorder: MediaRecorder? = null
    private var currentFilePath: String? = null
    private var startTime: Long = 0
    
    // 캐시된 설정값들
    private var cachedSampleRate: Int = 44100 // 기본값
    private var cachedBitRate: Int = 192000   // 기본값
    
    private val _recordingState = MutableStateFlow(RecordingState.IDLE)
    val recordingState: StateFlow<RecordingState> = _recordingState.asStateFlow()
    
    private val _recordingDuration = MutableStateFlow(0L)
    val recordingDuration: StateFlow<Long> = _recordingDuration.asStateFlow()
    
    init {
        // 초기화 시점에 설정값들을 비동기로 로드하고 Flow로 실시간 업데이트
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 초기값 로드
                cachedSampleRate = preferences.sampleRate.first()
                cachedBitRate = preferences.audioBitRate.first()
                
                // Flow로 실시간 업데이트 구독
                launch {
                    preferences.sampleRate.collect { newSampleRate ->
                        cachedSampleRate = newSampleRate
                    }
                }
                
                launch {
                    preferences.audioBitRate.collect { newBitRate ->
                        cachedBitRate = newBitRate
                    }
                }
            } catch (e: Exception) {
                // 에러 발생 시 기본값 사용
                e.printStackTrace()
            }
        }
    }
    
    /**
     * 녹음 시작
     */
    suspend fun startRecording(outputFile: File): Boolean {
        return try {
            if (_recordingState.value != RecordingState.IDLE) {
                return false
            }
            
            // 캐시된 설정값 사용 (필요시 최신값으로 업데이트)
            val sampleRate = cachedSampleRate
            val bitRate = cachedBitRate
            
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Android 12+
                MediaRecorder(context)
            } else {
                // Android 11 이하
                @Suppress("DEPRECATION")
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioSamplingRate(sampleRate)
                setAudioEncodingBitRate(bitRate)
                setOutputFile(outputFile.absolutePath)
                
                prepare()
                start()
            }
            
            currentFilePath = outputFile.absolutePath
            startTime = System.currentTimeMillis()
            _recordingState.value = RecordingState.RECORDING
            _recordingDuration.value = 0L
            
            true
        } catch (e: IOException) {
            e.printStackTrace()
            releaseRecorder()
            false
        }
    }
    
    /**
     * 중지
     */
    fun stopRecording(): String? {
        return try {
            // 일시정지 상태에서도 중지 가능하도록 수정
            if (_recordingState.value != RecordingState.RECORDING && 
                _recordingState.value != RecordingState.PAUSED) {
                return null
            }
            
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            
            _recordingState.value = RecordingState.STOPPED
            val filePath = currentFilePath
            currentFilePath = null
            
            filePath
        } catch (e: RuntimeException) {
            e.printStackTrace()
            releaseRecorder()
            null
        }
    }
    
    /**
     * 일시정지 (Android 7.0 이상)
     */
    fun pauseRecording(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && 
                _recordingState.value == RecordingState.RECORDING) {
                mediaRecorder?.pause()
                _recordingState.value = RecordingState.PAUSED
                true
            } else {
                false
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * 일시정지된 녹음을 재개 (Android 7.0 이상)
     */
    fun resumeRecording(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && 
                _recordingState.value == RecordingState.PAUSED) {
                mediaRecorder?.resume()
                _recordingState.value = RecordingState.RECORDING
                true
            } else {
                false
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * 현재 녹음 시간을 업데이트
     */
    fun updateRecordingDuration() {
        if (_recordingState.value == RecordingState.RECORDING) {
            _recordingDuration.value = System.currentTimeMillis() - startTime
        }
    }
    
    /**
     * 녹음 리소스 해제
     */
    private fun releaseRecorder() {
        mediaRecorder?.apply {
            try {
                stop()
            } catch (e: RuntimeException) {
                // 이미 중지된 경우 무시
            }
            release()
        }
        mediaRecorder = null
        _recordingState.value = RecordingState.IDLE
        currentFilePath = null
    }
    
    /**
     * 리소스를 정리
     */
    fun release() {
        releaseRecorder()
    }
}
