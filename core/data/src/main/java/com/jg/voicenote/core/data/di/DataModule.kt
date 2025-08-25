package com.jg.voicenote.core.data.di

import com.jg.voicenote.core.domain.repository.VoiceRecordRepository
import com.jg.voicenote.core.data.repository.VoiceRecordRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    
    @Binds
    abstract fun bindVoiceRecordRepository(
        voiceRecordRepositoryImpl: VoiceRecordRepositoryImpl
    ): VoiceRecordRepository
}
