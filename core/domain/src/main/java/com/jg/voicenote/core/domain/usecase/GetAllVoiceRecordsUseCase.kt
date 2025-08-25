package com.jg.voicenote.core.domain.usecase

import com.jg.voicenote.core.domain.repository.VoiceRecordRepository
import com.jg.voicenote.core.model.VoiceRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 모든 음성 녹음을 가져오기
 */
interface GetAllVoiceRecordsUseCase {
    operator fun invoke(): Flow<List<VoiceRecord>>
}

class GetAllVoiceRecordsUseCaseImpl @Inject constructor(
    private val voiceRecordRepository: VoiceRecordRepository
) : GetAllVoiceRecordsUseCase {
    override fun invoke(): Flow<List<VoiceRecord>> {
        return voiceRecordRepository.getAllVoiceRecords()
    }
}
