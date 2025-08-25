package com.jg.voicenote.feature.voice.recorder

import android.content.Context
import android.media.MediaPlayer
import com.jg.voicenote.core.model.PlaybackState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 오디오 재생
 */
@Singleton
class AudioPlayer {
    
    private var mediaPlayer: MediaPlayer? = null
    private var currentFilePath: String? = null
    
    private val _playbackState = MutableStateFlow(PlaybackState.IDLE)
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()
    
    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition.asStateFlow()
    
    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration.asStateFlow()
    
    /**
     * 오디오 파일을 재생
     */
    fun play(filePath: String): Boolean {
        return try {
            if (currentFilePath == filePath && _playbackState.value == PlaybackState.PAUSED) {
                // 같은 파일이고 일시정지 상태면 재개
                resumePlayback()
            } else {
                // 새 파일 재생 또는 처음 재생
                stopPlayback()
                startPlayback(filePath)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * 재생
     */
    private fun startPlayback(filePath: String) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(filePath)
            prepare()
            
            setOnCompletionListener {
                _playbackState.value = PlaybackState.COMPLETED
                _currentPosition.value = 0
            }
            
            setOnPreparedListener {
                _duration.value = duration
                start()
                _playbackState.value = PlaybackState.PLAYING
                currentFilePath = filePath
            }
        }
    }
    
    /**
     * 재개
     */
    private fun resumePlayback() {
        mediaPlayer?.let { player ->
            if (!player.isPlaying) {
                player.start()
                _playbackState.value = PlaybackState.PLAYING
            }
        }
    }
    
    /**
     * 일시정지
     */
    fun pause() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                _playbackState.value = PlaybackState.PAUSED
            }
        }
    }
    
    /**
     * 중지
     */
    fun stopPlayback() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.stop()
            }
            player.release()
        }
        mediaPlayer = null
        _playbackState.value = PlaybackState.STOPPED
        _currentPosition.value = 0
        currentFilePath = null
    }
    
    /**
     * 특정 위치로 이동
     */
    fun seekTo(position: Int) {
        mediaPlayer?.let { player ->
            if (position >= 0 && position <= player.duration) {
                player.seekTo(position)
                _currentPosition.value = position
            }
        }
    }
    
    /**
     * 현재 재생 위치를 업데이트
     */
    fun updateCurrentPosition() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                _currentPosition.value = player.currentPosition
            }
        }
    }
    
    /**
     * 리소스를 정리합니다
     */
    fun release() {
        stopPlayback()
    }
}
