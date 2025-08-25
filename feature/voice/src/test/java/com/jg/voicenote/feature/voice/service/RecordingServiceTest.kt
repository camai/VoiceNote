package com.jg.voicenote.feature.voice.service

import android.content.Context
import android.content.Intent
import com.jg.voicenote.core.domain.usecase.SaveVoiceRecordUseCase
import com.jg.voicenote.core.model.RecordingState
import com.jg.voicenote.feature.voice.recorder.AudioRecorder
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.flow.MutableStateFlow

@RunWith(MockitoJUnitRunner::class)
class RecordingServiceTest {

    @Mock
    private lateinit var mockAudioRecorder: AudioRecorder

    @Mock
    private lateinit var mockSaveVoiceRecordUseCase: SaveVoiceRecordUseCase

    private lateinit var recordingService: RecordingService
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        
        // AudioRecorder 모킹
        val recordingStateFlow = MutableStateFlow(RecordingState.IDLE)
        `when`(mockAudioRecorder.recordingState).thenReturn(recordingStateFlow)
        `when`(mockAudioRecorder.startRecording(any())).thenReturn(true)
        `when`(mockAudioRecorder.stopRecording()).thenReturn("/test/recording.m4a")
        `when`(mockAudioRecorder.pauseRecording()).thenReturn(true)
        `when`(mockAudioRecorder.resumeRecording()).thenReturn(true)
        
        // Service 생성
        recordingService = RecordingService()
        
        // 의존성 주입
        recordingService.audioRecorder = mockAudioRecorder
        recordingService.saveVoiceRecordUseCase = mockSaveVoiceRecordUseCase
    }

    @Test
    fun `일시정지 후 중지 시 데이터베이스에 저장되어야 한다`() {
        // Given: 녹음 시작
        val startIntent = Intent(context, RecordingService::class.java).apply {
            action = RecordingService.ACTION_START_RECORDING
        }
        recordingService.onStartCommand(startIntent, 0, 1)

        // When: 일시정지
        val pauseIntent = Intent(context, RecordingService::class.java).apply {
            action = RecordingService.ACTION_PAUSE_RECORDING
        }
        recordingService.onStartCommand(pauseIntent, 0, 2)

        // Then: 중지 시 데이터베이스 저장
        val stopIntent = Intent(context, RecordingService::class.java).apply {
            action = RecordingService.ACTION_STOP_RECORDING
        }
        recordingService.onStartCommand(stopIntent, 0, 3)

        // SaveVoiceRecordUseCase가 호출되었는지 확인
        verify(mockSaveVoiceRecordUseCase, times(1)).invoke(any())
    }

    @Test
    fun `AudioRecorder가 null을 반환하면 데이터베이스에 저장되지 않아야 한다`() {
        // Given: AudioRecorder가 null 반환하도록 설정
        `when`(mockAudioRecorder.stopRecording()).thenReturn(null)

        val startIntent = Intent(context, RecordingService::class.java).apply {
            action = RecordingService.ACTION_START_RECORDING
        }
        recordingService.onStartCommand(startIntent, 0, 1)

        val pauseIntent = Intent(context, RecordingService::class.java).apply {
            action = RecordingService.ACTION_PAUSE_RECORDING
        }
        recordingService.onStartCommand(pauseIntent, 0, 2)

        // When: 중지
        val stopIntent = Intent(context, RecordingService::class.java).apply {
            action = RecordingService.ACTION_STOP_RECORDING
        }
        recordingService.onStartCommand(stopIntent, 0, 3)

        // Then: 데이터베이스 저장되지 않음
        verify(mockSaveVoiceRecordUseCase, never()).invoke(any())
    }
}
