package com.jg.voicenote.feature.voice.service

import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceRecordingManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    fun startRecording() {
        RecordingService.startRecording(context)
    }
    
    fun stopRecording() {
        RecordingService.stopRecording(context)
    }
    
    fun pauseRecording() {
        val intent = Intent(context, RecordingService::class.java).apply {
            action = RecordingService.ACTION_PAUSE_RECORDING
        }
        context.startService(intent)
    }
    
    fun resumeRecording() {
        val intent = Intent(context, RecordingService::class.java).apply {
            action = RecordingService.ACTION_RESUME_RECORDING
        }
        context.startService(intent)
    }
}
