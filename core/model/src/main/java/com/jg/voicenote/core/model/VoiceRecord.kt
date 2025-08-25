package com.jg.voicenote.core.model

import java.time.LocalDateTime

/**
 * 음성 녹음 데이터 모델
 */
data class VoiceRecord(
    val id: Long = 0,
    val title: String,
    val filePath: String,
    val duration: Long, // 밀리초 단위
    val fileSize: Long, // 바이트 단위
    val createdAt: LocalDateTime,
    val isPlaying: Boolean = false
)

/**
 * 녹음 상태
 */
enum class RecordingState {
    IDLE,      // 대기 상태
    RECORDING, // 녹음 중
    PAUSED,    // 일시정지
    STOPPED    // 정지
}

/**
 * 재생 상태
 */
enum class PlaybackState {
    IDLE,      // 대기 상태
    PLAYING,   // 재생 중
    PAUSED,    // 일시정지
    STOPPED,   // 정지
    COMPLETED  // 재생 완료
}
