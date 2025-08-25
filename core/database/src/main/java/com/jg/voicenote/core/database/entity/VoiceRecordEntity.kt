package com.jg.voicenote.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * 음성 녹음 Room 엔티티
 */
@Entity(tableName = "voice_records")
data class VoiceRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val filePath: String,
    val duration: Long, // 밀리초 단위
    val fileSize: Long, // 바이트 단위
    val createdAt: LocalDateTime
)
