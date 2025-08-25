package com.jg.voicenote.core.domain.di

import com.jg.voicenote.core.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Domain Layer 관련 Hilt 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    
    @Binds
    abstract fun bindGetAllVoiceRecordsUseCase(
        getAllVoiceRecordsUseCaseImpl: GetAllVoiceRecordsUseCaseImpl
    ): GetAllVoiceRecordsUseCase
    
    @Binds
    abstract fun bindDeleteVoiceRecordUseCase(
        deleteVoiceRecordUseCaseImpl: DeleteVoiceRecordUseCaseImpl
    ): DeleteVoiceRecordUseCase
    
    @Binds
    abstract fun bindGetAudioSettingsUseCase(
        getAudioSettingsUseCaseImpl: GetAudioSettingsUseCaseImpl
    ): GetAudioSettingsUseCase
    
    @Binds
    abstract fun bindUpdateAudioSettingsUseCase(
        updateAudioSettingsUseCaseImpl: UpdateAudioSettingsUseCaseImpl
    ): UpdateAudioSettingsUseCase
    
    @Binds
    abstract fun bindSaveVoiceRecordUseCase(
        saveVoiceRecordUseCaseImpl: SaveVoiceRecordUseCaseImpl
    ): SaveVoiceRecordUseCase
}
