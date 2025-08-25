package com.jg.voicenote.core.data.repository

import com.jg.voicenote.core.data.mapper.toVoiceRecord
import com.jg.voicenote.core.data.mapper.toVoiceRecordEntity
import com.jg.voicenote.core.database.dao.VoiceRecordDao
import com.jg.voicenote.core.domain.repository.VoiceRecordRepository
import com.jg.voicenote.core.model.VoiceRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 음성 녹음 Repository 구현체
 */
@Singleton
class VoiceRecordRepositoryImpl @Inject constructor(
    private val voiceRecordDao: VoiceRecordDao
) : VoiceRecordRepository {
    
    override fun getAllVoiceRecords(): Flow<List<VoiceRecord>> {
        return voiceRecordDao.getAllVoiceRecords().map { entities ->
            entities.map { it.toVoiceRecord() }
        }
    }
    
    override suspend fun getVoiceRecordById(id: Long): VoiceRecord? {
        return voiceRecordDao.getVoiceRecordById(id)?.toVoiceRecord()
    }
    
    override suspend fun insertVoiceRecord(voiceRecord: VoiceRecord): Long {
        return voiceRecordDao.insertVoiceRecord(voiceRecord.toVoiceRecordEntity())
    }
    
    override suspend fun updateVoiceRecord(voiceRecord: VoiceRecord) {
        voiceRecordDao.updateVoiceRecord(voiceRecord.toVoiceRecordEntity())
    }
    
    override suspend fun deleteVoiceRecord(voiceRecord: VoiceRecord) {
        voiceRecordDao.deleteVoiceRecord(voiceRecord.toVoiceRecordEntity())
    }
    
    override suspend fun deleteVoiceRecordById(id: Long) {
        voiceRecordDao.deleteVoiceRecordById(id)
    }
    
    override suspend fun getVoiceRecordCount(): Int {
        return voiceRecordDao.getVoiceRecordCount()
    }
}
