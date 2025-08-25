package com.jg.voicenote.feature.voice.ui

/**
 * MVI 패턴의 Intent (사용자 액션)
 */
sealed interface VoiceIntent {
    
    // 녹음 관련
    data object StartRecording : VoiceIntent
    data object StopRecording : VoiceIntent
    data object PauseRecording : VoiceIntent
    data object ResumeRecording : VoiceIntent
    
    // 재생 관련
    data class PlayRecord(val recordId: Long, val filePath: String) : VoiceIntent
    data object PausePlayback : VoiceIntent
    data object StopPlayback : VoiceIntent
    data class SeekTo(val position: Int) : VoiceIntent
    
    // 데이터 관리
    data object LoadRecords : VoiceIntent
    data class DeleteRecord(val recordId: Long) : VoiceIntent
    data class SaveRecord(val title: String, val filePath: String, val duration: Long, val fileSize: Long) : VoiceIntent
    
    // 권한 관련
    data object RequestPermissions : VoiceIntent
    data class PermissionResult(val granted: Boolean) : VoiceIntent
    
    // 설정 관련
    data class UpdateSampleRate(val sampleRateHz: String) : VoiceIntent
    data class UpdateBitRate(val bitRateKbps: String) : VoiceIntent
    data class UpdateQuality(val quality: String) : VoiceIntent
}
