package com.jg.voicenote.core.domain.usecase

import com.jg.voicenote.core.datastore.VoiceNotePreferences
import javax.inject.Inject

/**
 * 오디오 설정을 업데이트
 */
interface UpdateAudioSettingsUseCase {
    suspend fun updateSampleRate(sampleRate: Int)
    suspend fun updateBitRate(bitRate: Int)
    suspend fun updateAutoSave(enabled: Boolean)
    suspend fun updateBackgroundRecording(enabled: Boolean)
    suspend fun updateMaxRecordingDuration(duration: Int)
}

class UpdateAudioSettingsUseCaseImpl @Inject constructor(
    private val preferences: VoiceNotePreferences
) : UpdateAudioSettingsUseCase {
    
    override suspend fun updateSampleRate(sampleRate: Int) {
        preferences.setSampleRate(sampleRate)
    }
    
    override suspend fun updateBitRate(bitRate: Int) {
        preferences.setAudioBitRate(bitRate)
    }
    
    override suspend fun updateAutoSave(enabled: Boolean) {
        preferences.setAutoSaveEnabled(enabled)
    }
    
    override suspend fun updateBackgroundRecording(enabled: Boolean) {
        preferences.setBackgroundRecordingEnabled(enabled)
    }
    
    override suspend fun updateMaxRecordingDuration(duration: Int) {
        preferences.setMaxRecordingDuration(duration)
    }
}
