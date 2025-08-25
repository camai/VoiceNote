package com.jg.voicenote.core.database.di

import android.content.Context
import androidx.room.Room
import com.jg.voicenote.core.database.VoiceNoteDatabase
import com.jg.voicenote.core.database.dao.VoiceRecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideVoiceNoteDatabase(
        @ApplicationContext context: Context
    ): VoiceNoteDatabase {
        return Room.databaseBuilder(
            context,
            VoiceNoteDatabase::class.java,
            VoiceNoteDatabase.DATABASE_NAME
        ).build()
    }
    
    @Provides
    fun provideVoiceRecordDao(database: VoiceNoteDatabase): VoiceRecordDao {
        return database.voiceRecordDao()
    }
}
