package com.jg.voicenote.core.domain.usecase

import com.jg.voicenote.core.domain.repository.VoiceRecordRepository
import java.io.File
import javax.inject.Inject

/**
 * 음성 녹음을 삭제
 */
interface DeleteVoiceRecordUseCase {
    suspend operator fun invoke(recordId: Long)
}

class DeleteVoiceRecordUseCaseImpl @Inject constructor(
    private val voiceRecordRepository: VoiceRecordRepository
) : DeleteVoiceRecordUseCase {
    override suspend fun invoke(recordId: Long) {
        // 1. 데이터베이스에서 레코드 정보 가져오기
        val record = voiceRecordRepository.getVoiceRecordById(recordId)
        
        // 2. 파일 삭제
        record?.let {
            try {
                val file = File(it.filePath)
                if (file.exists()) {
                    file.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        // 3. 데이터베이스에서 삭제
        voiceRecordRepository.deleteVoiceRecordById(recordId)
    }
}
