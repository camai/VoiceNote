package com.jg.voicenote.feature.voice.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jg.voicenote.core.model.RecordingState
import com.jg.voicenote.feature.voice.R
import com.jg.voicenote.feature.voice.ui.VoiceIntent
import com.jg.voicenote.feature.voice.ui.VoiceUiState

/**
 * 녹음 탭 컴포넌트
 */
@Composable
fun RecordingTab(
    uiState: VoiceUiState,
    onIntent: (VoiceIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        // 녹음 시간 표시 (크게)
        Text(
            text = uiState.getFormattedRecordingDuration(),
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = if (uiState.isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        
        // 녹음 상태 표시
        Text(
            text = when (uiState.recordingState) {
                RecordingState.IDLE -> ""
                RecordingState.RECORDING -> "녹음 중..."
                RecordingState.PAUSED -> "일시정지"
                RecordingState.STOPPED -> "녹음 완료"
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // 파형 표시 영역
        WaveformDisplay(isRecording = uiState.isRecording)
        
        Spacer(modifier = Modifier.weight(1f))
        
        // 녹음 컨트롤 버튼들
        RecordingControls(
            uiState = uiState,
            onIntent = onIntent
        )
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

/**
 * 파형 표시 컴포넌트
 */
@Composable
private fun WaveformDisplay(
    isRecording: Boolean
) {
    var animationState by remember { mutableStateOf(0) }
    
    // 녹음 중일 때 애니메이션
    LaunchedEffect(isRecording) {
        if (isRecording) {
            while (isRecording) {
                animationState = (animationState + 1) % 100
                delay(100)
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 32.dp)
    ) {
        if (isRecording) {
            // 애니메이션 파형 표시
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(20) { index ->
                    val height = remember(animationState, index) {
                        (20 + (kotlin.math.sin((animationState + index * 5) * 0.1) * 30).toInt()).coerceIn(10, 80)
                    }
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(height.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        } else {
            // 기본 라인
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.outline)
                    .align(Alignment.Center)
            )
        }
    }
}

/**
 * 녹음 컨트롤 버튼들
 */
@Composable
private fun RecordingControls(
    uiState: VoiceUiState,
    onIntent: (VoiceIntent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(54.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 왼쪽 버튼 (재생 또는 일시정지)
        FloatingActionButton(
            onClick = {
                when {
                    uiState.canStartRecording -> onIntent(VoiceIntent.StartRecording)
                    // 녹음 중이면 일시정지
                    uiState.canPauseRecording -> onIntent(VoiceIntent.PauseRecording)
                    // 일시정지 중이면 재개
                    uiState.canResumeRecording -> onIntent(VoiceIntent.ResumeRecording)
                }
            },
            modifier = Modifier.size(54.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            if (uiState.isRecording) {
                Icon(
                    painter = painterResource(R.drawable.ic_pause),
                    contentDescription = "일시정지"
                )
            } else {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = when {
                        uiState.canResumeRecording -> "재개"
                        else -> "재생"
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = {
                if (uiState.isPlaying) {
                    onIntent(VoiceIntent.StopPlayback)
                } else if (uiState.canStopRecording) {
                    onIntent(VoiceIntent.StopRecording)
                }
            },
            modifier = Modifier.size(54.dp),
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_stop),
                contentDescription = "정지"
            )
        }
    }
}
