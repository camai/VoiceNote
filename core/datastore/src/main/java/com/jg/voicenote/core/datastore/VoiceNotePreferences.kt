package com.jg.voicenote.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 음성 녹음 앱 설정 관리
 */
@Singleton
class VoiceNotePreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    
    // 오디오 품질 설정 (비트레이트)
    val audioBitRate: Flow<Int> = dataStore.data.map { preferences ->
        preferences[AUDIO_BIT_RATE_KEY] ?: DEFAULT_AUDIO_BIT_RATE
    }
    
    // 샘플링 레이트 설정
    val sampleRate: Flow<Int> = dataStore.data.map { preferences ->
        preferences[SAMPLE_RATE_KEY] ?: DEFAULT_SAMPLE_RATE
    }
    
    // 자동 저장 여부
    val isAutoSaveEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUTO_SAVE_KEY] ?: true
    }
    
    // 백그라운드 녹음 허용 여부
    val isBackgroundRecordingEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[BACKGROUND_RECORDING_KEY] ?: true
    }
    
    // 최대 녹음 시간 (분 단위)
    val maxRecordingDuration: Flow<Int> = dataStore.data.map { preferences ->
        preferences[MAX_RECORDING_DURATION_KEY] ?: DEFAULT_MAX_RECORDING_DURATION
    }
    
    suspend fun setAudioBitRate(bitRate: Int) {
        dataStore.edit { preferences ->
            preferences[AUDIO_BIT_RATE_KEY] = bitRate
        }
    }
    
    suspend fun setSampleRate(sampleRate: Int) {
        dataStore.edit { preferences ->
            preferences[SAMPLE_RATE_KEY] = sampleRate
        }
    }
    
    suspend fun setAutoSaveEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_SAVE_KEY] = enabled
        }
    }
    
    suspend fun setBackgroundRecordingEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[BACKGROUND_RECORDING_KEY] = enabled
        }
    }
    
    suspend fun setMaxRecordingDuration(duration: Int) {
        dataStore.edit { preferences ->
            preferences[MAX_RECORDING_DURATION_KEY] = duration
        }
    }
    
    companion object {
        private val AUDIO_BIT_RATE_KEY = intPreferencesKey("audio_bit_rate")
        private val SAMPLE_RATE_KEY = intPreferencesKey("sample_rate")
        private val AUTO_SAVE_KEY = booleanPreferencesKey("auto_save")
        private val BACKGROUND_RECORDING_KEY = booleanPreferencesKey("background_recording")
        private val MAX_RECORDING_DURATION_KEY = intPreferencesKey("max_recording_duration")
        
        private const val DEFAULT_AUDIO_BIT_RATE = 128000 // 128 kbps
        private const val DEFAULT_SAMPLE_RATE = 44100 // 44.1 kHz
        private const val DEFAULT_MAX_RECORDING_DURATION = 60 // 60분
    }
}
