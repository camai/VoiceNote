package com.jg.voicenote.core.domain.usecase

import com.jg.voicenote.core.domain.repository.VoiceRecordRepository
import com.jg.voicenote.core.model.VoiceRecord
import javax.inject.Inject

/**
 * 음성 녹음을 저장
 */
interface SaveVoiceRecordUseCase {
    suspend operator fun invoke(voiceRecord: VoiceRecord): Long
}

class SaveVoiceRecordUseCaseImpl @Inject constructor(
    private val voiceRecordRepository: VoiceRecordRepository
) : SaveVoiceRecordUseCase {
    override suspend fun invoke(voiceRecord: VoiceRecord): Long {
        return voiceRecordRepository.insertVoiceRecord(voiceRecord)
    }
}



