package com.jg.voicenote.core.data.mapper

import com.jg.voicenote.core.database.entity.VoiceRecordEntity
import com.jg.voicenote.core.model.VoiceRecord

/**
 * VoiceRecord와 VoiceRecordEntity 간 변환 매퍼
 */
fun VoiceRecordEntity.toVoiceRecord(): VoiceRecord {
    return VoiceRecord(
        id = id,
        title = title,
        filePath = filePath,
        duration = duration,
        fileSize = fileSize,
        createdAt = createdAt
    )
}

fun VoiceRecord.toVoiceRecordEntity(): VoiceRecordEntity {
    return VoiceRecordEntity(
        id = id,
        title = title,
        filePath = filePath,
        duration = duration,
        fileSize = fileSize,
        createdAt = createdAt
    )
}
