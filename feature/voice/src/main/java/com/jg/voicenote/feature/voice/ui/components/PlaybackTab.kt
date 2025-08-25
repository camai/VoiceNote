package com.jg.voicenote.feature.voice.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jg.voicenote.feature.voice.R
import com.jg.voicenote.feature.voice.ui.VoiceIntent
import com.jg.voicenote.feature.voice.ui.VoiceUiState

/**
 * 재생 탭 컴포넌트
 */
@Composable
fun PlaybackTab(
    uiState: VoiceUiState,
    onIntent: (VoiceIntent) -> Unit
) {
    if (uiState.voiceRecords.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.no_recorded_files),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.record_in_recording_tab),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.voiceRecords) { record ->
                VoiceRecordItem(
                    record = record,
                    isCurrentlyPlaying = uiState.currentPlayingRecord?.id == record.id && uiState.isPlaying,
                    onPlayClick = { onIntent(VoiceIntent.PlayRecord(record.id, record.filePath)) },
                    onStopClick = { onIntent(VoiceIntent.StopPlayback) },
                    onDeleteClick = { onIntent(VoiceIntent.DeleteRecord(record.id)) }
                )
            }
        }
    }
}
