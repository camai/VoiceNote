package com.jg.voicenote.feature.voice.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jg.voicenote.core.domain.usecase.DeleteVoiceRecordUseCase
import com.jg.voicenote.core.domain.usecase.GetAllVoiceRecordsUseCase
import com.jg.voicenote.core.domain.usecase.GetAudioSettingsUseCase
import com.jg.voicenote.core.domain.usecase.UpdateAudioSettingsUseCase
import com.jg.voicenote.core.model.AudioQuality
import com.jg.voicenote.core.model.BitRate
import com.jg.voicenote.core.model.SampleRate
import com.jg.voicenote.feature.voice.R
import com.jg.voicenote.feature.voice.recorder.AudioPlayer
import com.jg.voicenote.feature.voice.recorder.AudioRecorder
import com.jg.voicenote.feature.voice.service.VoiceRecordingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class VoiceViewModel @Inject constructor(
    private val getAllVoiceRecordsUseCase: GetAllVoiceRecordsUseCase,
    private val deleteVoiceRecordUseCase: DeleteVoiceRecordUseCase,
    private val recordingManager: VoiceRecordingManager,
    private val audioPlayer: AudioPlayer,
    private val getAudioSettingsUseCase: GetAudioSettingsUseCase,
    private val updateAudioSettingsUseCase: UpdateAudioSettingsUseCase,
    private val audioRecorder: AudioRecorder  // AudioRecorder 주입
) : ViewModel() {

    private var timerJob: Job? = null
    private var recordingStartTime: Long = 0
    private var pausedDuration: Long = 0

    private val _uiState = MutableStateFlow(VoiceUiState())
    val uiState: StateFlow<VoiceUiState> = _uiState.asStateFlow()

    init {
        loadRecords()
        loadAudioSettings()
        // 초기 권한 상태 설정
        _uiState.update { it.copy(hasRecordPermission = true) }

        // AudioPlayer 상태 관찰
        observeAudioPlayerState()
    }

    private fun loadAudioSettings() {
        viewModelScope.launch {
            getAudioSettingsUseCase().collect { settings ->
                val sampleRateEnum = SampleRate.fromValue(settings.sampleRate)
                val bitRateEnum = BitRate.fromValue(settings.bitRate)
                val quality = getQualityFromSettings(settings.sampleRate, settings.bitRate)

                _uiState.update { currentState ->
                    currentState.copy(
                        sampleRate = sampleRateEnum.displayName,
                        bitRate = bitRateEnum.displayName,
                        quality = quality
                    )
                }
            }
        }
    }

    private fun getQualityFromSettings(sampleRate: Int, bitRate: Int): String {
        return AudioQuality.fromSettings(sampleRate, bitRate).displayName
    }

    private fun observeAudioPlayerState() {
        viewModelScope.launch {
            audioPlayer.playbackState.collect { playbackState ->
                _uiState.update { currentState ->
                    currentState.copy(playbackState = playbackState)
                }
            }
        }

        viewModelScope.launch {
            audioPlayer.currentPosition.collect { position ->
                _uiState.update { currentState ->
                    currentState.copy(playbackPosition = position)
                }
            }
        }

        viewModelScope.launch {
            audioPlayer.duration.collect { duration ->
                _uiState.update { currentState ->
                    currentState.copy(playbackDuration = duration)
                }
            }
        }

        // 재생 위치 업데이트 타이머
        viewModelScope.launch {
            while (true) {
                audioPlayer.updateCurrentPosition()
                delay(500) // 0.5초마다 업데이트
            }
        }
    }

    fun handleIntent(intent: VoiceIntent) {
        when (intent) {
            is VoiceIntent.StartRecording -> startRecording()
            is VoiceIntent.StopRecording -> stopRecording()
            is VoiceIntent.PauseRecording -> pauseRecording()
            is VoiceIntent.ResumeRecording -> resumeRecording()

            is VoiceIntent.PlayRecord -> playRecord(intent.recordId, intent.filePath)
            is VoiceIntent.PausePlayback -> pausePlayback()
            is VoiceIntent.StopPlayback -> stopPlayback()
            is VoiceIntent.SeekTo -> seekTo(intent.position)

            is VoiceIntent.LoadRecords -> loadRecords()
            is VoiceIntent.DeleteRecord -> deleteRecord(intent.recordId)
            is VoiceIntent.SaveRecord -> { /* RecordingService에서 처리 */
            }

            is VoiceIntent.RequestPermissions -> requestPermissions()
            is VoiceIntent.PermissionResult -> handlePermissionResult(intent.granted)

            is VoiceIntent.UpdateSampleRate -> updateSampleRate(intent.sampleRateHz)
            is VoiceIntent.UpdateBitRate -> updateBitRate(intent.bitRateKbps)
            is VoiceIntent.UpdateQuality -> updateQuality(intent.quality)
        }
    }

    private fun startRecording() {
        if (!_uiState.value.canStartRecording) return

        // 실제 녹음 서비스 시작
        recordingManager.startRecording()

        // 새 녹음 시작
        recordingStartTime = System.currentTimeMillis()
        pausedDuration = 0L

        _uiState.update { currentState ->
            currentState.copy(
                recordingState = com.jg.voicenote.core.model.RecordingState.RECORDING,
                recordingDuration = 0L
            )
        }

        // 타이머 시작
        startTimer()
    }

    private fun stopRecording() {
        if (!_uiState.value.canStopRecording) return

        // 실제 녹음 서비스 중지
        recordingManager.stopRecording()

        // 타이머 중지
        stopTimer()

        // 녹음 관련 변수 초기화
        recordingStartTime = 0
        pausedDuration = 0L

        _uiState.update { currentState ->
            currentState.copy(
                recordingState = com.jg.voicenote.core.model.RecordingState.IDLE,
                recordingDuration = 0L
            )
        }
        
        // 녹음 저장 완료 이벤트 발생
        _uiState.update { currentState ->
            currentState.copy(showRecordingSavedToast = true)
        }
    }

    private fun pauseRecording() {
        if (!_uiState.value.canPauseRecording) return

        // 실제 녹음 일시정지
        recordingManager.pauseRecording()

        // 현재까지의 녹음 시간을 누적 (현재 표시된 시간을 그대로 저장)
        pausedDuration = _uiState.value.recordingDuration

        // 타이머 일시정지
        stopTimer()

        _uiState.update { currentState ->
            currentState.copy(
                recordingState = com.jg.voicenote.core.model.RecordingState.PAUSED
            )
        }
    }

    private fun resumeRecording() {
        if (!_uiState.value.canResumeRecording) return

        // 실제 녹음 재개
        recordingManager.resumeRecording()

        // 재개 시점을 새로운 시작 시간으로 설정
        recordingStartTime = System.currentTimeMillis()

        // 상태를 먼저 RECORDING으로 변경
        _uiState.update { currentState ->
            currentState.copy(
                recordingState = com.jg.voicenote.core.model.RecordingState.RECORDING
            )
        }

        // 그 다음 타이머 시작
        startTimer()
    }

    private fun playRecord(recordId: Long, filePath: String) {
        viewModelScope.launch {
            // 현재 목록에서 해당 레코드 찾기
            val record = _uiState.value.voiceRecords.find { it.id == recordId }
            if (record != null) {
                // 실제 오디오 재생 시작
                val success = audioPlayer.play(filePath)
                if (success) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            currentPlayingRecord = record
                        )
                    }
                } else {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = "파일을 재생할 수 없습니다: ${record.title}"
                        )
                    }
                }
            }
        }
    }

    private fun pausePlayback() {
        audioPlayer.pause()
    }

    private fun stopPlayback() {
        audioPlayer.stopPlayback()
        _uiState.update { currentState ->
            currentState.copy(
                currentPlayingRecord = null
            )
        }
    }

    private fun seekTo(position: Int) {
        audioPlayer.seekTo(position)
    }

    private fun loadRecords() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                getAllVoiceRecordsUseCase()
                    .collect { records ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                voiceRecords = records,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
            } catch (exception: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = exception.message
                    )
                }
            }
        }
    }

    private fun deleteRecord(recordId: Long) {
        viewModelScope.launch {
            try {
                // 현재 재생 중인 파일이 삭제할 파일과 같으면 재생 중지
                val currentPlayingRecord = _uiState.value.currentPlayingRecord
                if (currentPlayingRecord != null && currentPlayingRecord.id == recordId) {
                    stopPlayback()
                }

                deleteVoiceRecordUseCase(recordId)
            } catch (exception: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = exception.message)
                }
            }
        }
    }

    private fun requestPermissions() {
        _uiState.update { currentState ->
            currentState.copy(showPermissionDialog = true)
        }
    }

    private fun handlePermissionResult(granted: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                hasRecordPermission = granted,
                showPermissionDialog = false
            )
        }
    }

    fun clearError() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
    
    fun clearRecordingSavedToast() {
        _uiState.update { currentState ->
            currentState.copy(showRecordingSavedToast = false)
        }
    }

    private fun updateSampleRate(sampleRateHz: String) {
        viewModelScope.launch {
            val sampleRate = SampleRate.fromDisplayName(sampleRateHz)
            updateAudioSettingsUseCase.updateSampleRate(sampleRate.value)
            // Flow 기반 자동 업데이트로 수동 호출 불필요
        }
    }

    private fun updateBitRate(bitRateKbps: String) {
        viewModelScope.launch {
            val bitRate = BitRate.fromDisplayName(bitRateKbps)
            updateAudioSettingsUseCase.updateBitRate(bitRate.value)
            // Flow 기반 자동 업데이트로 수동 호출 불필요
        }
    }

    private fun updateQuality(quality: String) {
        viewModelScope.launch {
            val audioQuality = AudioQuality.fromDisplayName(quality)
            updateAudioSettingsUseCase.updateSampleRate(audioQuality.sampleRate.value)
            updateAudioSettingsUseCase.updateBitRate(audioQuality.bitRate.value)
            // Flow 기반 자동 업데이트로 수동 호출 불필요

            // UI 상태도 즉시 업데이트
            _uiState.update { currentState ->
                currentState.copy(
                    sampleRate = audioQuality.sampleRate.displayName,
                    bitRate = audioQuality.bitRate.displayName,
                    quality = audioQuality.displayName
                )
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                val currentState = _uiState.value.recordingState
                if (currentState != com.jg.voicenote.core.model.RecordingState.RECORDING) {
                    break
                }

                val currentTime = System.currentTimeMillis()
                val currentSessionDuration = currentTime - recordingStartTime
                val totalDuration = pausedDuration + currentSessionDuration

                _uiState.update { it.copy(recordingDuration = totalDuration) }
                delay(100) // 100ms마다 업데이트
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        audioPlayer.release()
    }
}
