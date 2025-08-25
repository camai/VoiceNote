package com.jg.voicenote.feature.voice.ui

import com.jg.voicenote.core.model.PlaybackState
import com.jg.voicenote.core.model.RecordingState
import com.jg.voicenote.core.model.VoiceRecord

data class VoiceUiState(
    // 녹음 상태
    val recordingState: RecordingState = RecordingState.IDLE,
    val recordingDuration: Long = 0L,
    val currentRecordingFile: String? = null,
    
    // 재생 상태
    val playbackState: PlaybackState = PlaybackState.IDLE,
    val currentPlayingRecord: VoiceRecord? = null,
    val playbackPosition: Int = 0,
    val playbackDuration: Int = 0,
    
    // 데이터
    val voiceRecords: List<VoiceRecord> = emptyList(),
    val isLoading: Boolean = false,
    
    // 권한
    val hasRecordPermission: Boolean = false,
    val showPermissionDialog: Boolean = false,
    
    // 에러
    val errorMessage: String? = null,
    
    // 토스트 메시지
    val showRecordingSavedToast: Boolean = false,
    
    // 설정
    val sampleRate: String = "44100Hz",
    val bitRate: String = "192kbps",
    val quality: String = "정상 음질"
) {
    
    val isRecording: Boolean
        get() = recordingState == RecordingState.RECORDING

    val isRecordingPaused: Boolean
        get() = recordingState == RecordingState.PAUSED

    val canStartRecording: Boolean
        get() = recordingState == RecordingState.IDLE && hasRecordPermission
    
    val canStopRecording: Boolean
        get() = recordingState == RecordingState.RECORDING || recordingState == RecordingState.PAUSED
    
    val canPauseRecording: Boolean
        get() = recordingState == RecordingState.RECORDING
    
    val canResumeRecording: Boolean
        get() = recordingState == RecordingState.PAUSED
    
    val isPlaying: Boolean
        get() = playbackState == PlaybackState.PLAYING
    

    fun getFormattedRecordingDuration(): String {
        val seconds = (recordingDuration / 1000) % 60
        val minutes = (recordingDuration / (1000 * 60)) % 60
        val hours = (recordingDuration / (1000 * 60 * 60))
        
        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}
