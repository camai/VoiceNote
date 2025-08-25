package com.jg.voicenote.core.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.jg.voicenote.core.database.converter.DateTimeConverter
import com.jg.voicenote.core.database.dao.VoiceRecordDao
import com.jg.voicenote.core.database.entity.VoiceRecordEntity


@Database(
    entities = [VoiceRecordEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateTimeConverter::class)
abstract class VoiceNoteDatabase : RoomDatabase() {
    
    abstract fun voiceRecordDao(): VoiceRecordDao
    
    companion object {
        const val DATABASE_NAME = "voice_note_database"
    }
}
