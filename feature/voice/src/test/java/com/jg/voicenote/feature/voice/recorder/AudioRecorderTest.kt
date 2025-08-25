package com.jg.voicenote.feature.voice.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import com.jg.voicenote.core.datastore.VoiceNotePreferences
import com.jg.voicenote.core.model.RecordingState
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.N]) // Android 7.0 이상에서 테스트
class AudioRecorderTest {

    @MockK
    private lateinit var mockContext: Context

    @MockK
    private lateinit var mockPreferences: VoiceNotePreferences

    @MockK
    private lateinit var mockMediaRecorder: MediaRecorder

    private lateinit var audioRecorder: AudioRecorder
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        
        // 기본 설정값 모킹
        coEvery { mockPreferences.sampleRate } returns flowOf(44100)
        coEvery { mockPreferences.audioBitRate } returns flowOf(192000)
        
        // MediaRecorder 모킹
        mockkConstructor(MediaRecorder::class)
        every { anyConstructed<MediaRecorder>().setAudioSource(any()) } just Runs
        every { anyConstructed<MediaRecorder>().setOutputFormat(any()) } just Runs
        every { anyConstructed<MediaRecorder>().setAudioEncoder(any()) } just Runs
        every { anyConstructed<MediaRecorder>().setAudioSamplingRate(any()) } just Runs
        every { anyConstructed<MediaRecorder>().setAudioEncodingBitRate(any()) } just Runs
        every { anyConstructed<MediaRecorder>().setOutputFile(any()) } just Runs
        every { anyConstructed<MediaRecorder>().prepare() } just Runs
        every { anyConstructed<MediaRecorder>().start() } just Runs
        every { anyConstructed<MediaRecorder>().stop() } just Runs
        every { anyConstructed<MediaRecorder>().release() } just Runs
        every { anyConstructed<MediaRecorder>().pause() } just Runs
        every { anyConstructed<MediaRecorder>().resume() } just Runs

        Dispatchers.setMain(testDispatcher)
        audioRecorder = AudioRecorder(mockContext, mockPreferences)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `일시정지 상태에서 중지하면 파일 경로를 반환해야 한다`() = runTest {
        // Given: 녹음 시작
        val testFile = File("/test/recording.m4a")
        val result = audioRecorder.startRecording(testFile)
        assertTrue(result)
        assertEquals(RecordingState.RECORDING, audioRecorder.recordingState.value)

        // When: 일시정지
        val pauseResult = audioRecorder.pauseRecording()
        assertTrue(pauseResult)
        assertEquals(RecordingState.PAUSED, audioRecorder.recordingState.value)

        // Then: 중지 시 파일 경로 반환
        val filePath = audioRecorder.stopRecording()
        assertNotNull(filePath)
        assertEquals(testFile.absolutePath, filePath)
        assertEquals(RecordingState.STOPPED, audioRecorder.recordingState.value)
    }

    @Test
    fun `일시정지 상태에서 중지하면 MediaRecorder가 정상적으로 중지되어야 한다`() = runTest {
        // Given: 녹음 시작 후 일시정지
        val testFile = File("/test/recording.m4a")
        audioRecorder.startRecording(testFile)
        audioRecorder.pauseRecording()

        // When: 중지
        audioRecorder.stopRecording()

        // Then: MediaRecorder의 stop과 release가 호출됨
        verify(exactly = 1) { anyConstructed<MediaRecorder>().stop() }
        verify(exactly = 1) { anyConstructed<MediaRecorder>().release() }
    }

    @Test
    fun `IDLE 상태에서 중지하면 null을 반환해야 한다`() = runTest {
        // Given: IDLE 상태 (초기 상태)
        assertEquals(RecordingState.IDLE, audioRecorder.recordingState.value)

        // When: 중지
        val filePath = audioRecorder.stopRecording()

        // Then: null 반환
        assertNull(filePath)
        assertEquals(RecordingState.IDLE, audioRecorder.recordingState.value)
    }

    @Test
    fun `STOPPED 상태에서 중지하면 null을 반환해야 한다`() = runTest {
        // Given: 녹음 시작 후 중지
        val testFile = File("/test/recording.m4a")
        audioRecorder.startRecording(testFile)
        audioRecorder.stopRecording()
        assertEquals(RecordingState.STOPPED, audioRecorder.recordingState.value)

        // When: 다시 중지
        val filePath = audioRecorder.stopRecording()

        // Then: null 반환
        assertNull(filePath)
        assertEquals(RecordingState.STOPPED, audioRecorder.recordingState.value)
    }

    @Test
    fun `일시정지 후 재개 후 중지하면 정상적으로 파일 경로를 반환해야 한다`() = runTest {
        // Given: 녹음 시작 → 일시정지 → 재개
        val testFile = File("/test/recording.m4a")
        audioRecorder.startRecording(testFile)
        audioRecorder.pauseRecording()
        audioRecorder.resumeRecording()
        assertEquals(RecordingState.RECORDING, audioRecorder.recordingState.value)

        // When: 중지
        val filePath = audioRecorder.stopRecording()

        // Then: 파일 경로 반환
        assertNotNull(filePath)
        assertEquals(testFile.absolutePath, filePath)
        assertEquals(RecordingState.STOPPED, audioRecorder.recordingState.value)
    }
}
