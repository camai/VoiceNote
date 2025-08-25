package com.jg.voicenote.core.database.dao

import androidx.room.*
import com.jg.voicenote.core.database.entity.VoiceRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VoiceRecordDao {
    
    @Query("SELECT * FROM voice_records ORDER BY createdAt DESC")
    fun getAllVoiceRecords(): Flow<List<VoiceRecordEntity>>
    
    @Query("SELECT * FROM voice_records WHERE id = :id")
    suspend fun getVoiceRecordById(id: Long): VoiceRecordEntity?
    
    @Insert
    suspend fun insertVoiceRecord(voiceRecord: VoiceRecordEntity): Long
    
    @Update
    suspend fun updateVoiceRecord(voiceRecord: VoiceRecordEntity)
    
    @Delete
    suspend fun deleteVoiceRecord(voiceRecord: VoiceRecordEntity)
    
    @Query("DELETE FROM voice_records WHERE id = :id")
    suspend fun deleteVoiceRecordById(id: Long)
    
    @Query("SELECT COUNT(*) FROM voice_records")
    suspend fun getVoiceRecordCount(): Int
}
