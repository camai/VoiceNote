package com.jg.voicenote.core.domain.repository

import com.jg.voicenote.core.model.VoiceRecord
import kotlinx.coroutines.flow.Flow

interface VoiceRecordRepository {
    
    /**
     * 모든 음성 녹음 목록을 가져오기
     */
    fun getAllVoiceRecords(): Flow<List<VoiceRecord>>
    
    /**
     * 특정 ID의 음성 녹음을 가져오기
     */
    suspend fun getVoiceRecordById(id: Long): VoiceRecord?
    
    /**
     * 새로운 음성 녹음을 저장
     */
    suspend fun insertVoiceRecord(voiceRecord: VoiceRecord): Long
    
    /**
     * 음성 녹음 정보를 업데이트
     */
    suspend fun updateVoiceRecord(voiceRecord: VoiceRecord)
    
    /**
     * 음성 녹음을 삭제
     */
    suspend fun deleteVoiceRecord(voiceRecord: VoiceRecord)
    
    /**
     * 특정 ID의 음성 녹음을 삭제
     */
    suspend fun deleteVoiceRecordById(id: Long)
    
    /**
     * 전체 음성 녹음 개수를 가져오기
     */
    suspend fun getVoiceRecordCount(): Int
}



