package com.jg.voicenote.core.domain.usecase

import com.jg.voicenote.core.datastore.VoiceNotePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * 오디오 설정을 가져오기
 */
interface GetAudioSettingsUseCase {
    data class AudioSettings(
        val sampleRate: Int,
        val bitRate: Int,
        val isAutoSaveEnabled: Boolean,
        val isBackgroundRecordingEnabled: Boolean,
        val maxRecordingDuration: Int
    )
    
    operator fun invoke(): Flow<AudioSettings>
}

class GetAudioSettingsUseCaseImpl @Inject constructor(
    private val preferences: VoiceNotePreferences
) : GetAudioSettingsUseCase {
    override fun invoke(): Flow<GetAudioSettingsUseCase.AudioSettings> {
        return combine(
            preferences.sampleRate,
            preferences.audioBitRate,
            preferences.isAutoSaveEnabled,
            preferences.isBackgroundRecordingEnabled,
            preferences.maxRecordingDuration
        ) { sampleRate, bitRate, autoSave, backgroundRecording, maxDuration ->
            GetAudioSettingsUseCase.AudioSettings(
                sampleRate = sampleRate,
                bitRate = bitRate,
                isAutoSaveEnabled = autoSave,
                isBackgroundRecordingEnabled = backgroundRecording,
                maxRecordingDuration = maxDuration
            )
        }
    }
}
