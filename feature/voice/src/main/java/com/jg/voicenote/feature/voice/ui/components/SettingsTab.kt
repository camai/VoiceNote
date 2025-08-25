package com.jg.voicenote.feature.voice.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jg.voicenote.feature.voice.ui.VoiceIntent
import com.jg.voicenote.feature.voice.ui.VoiceUiState
import com.jg.voicenote.core.model.SampleRate
import com.jg.voicenote.core.model.BitRate
import com.jg.voicenote.core.model.AudioQuality
import com.jg.voicenote.feature.voice.R

/**
 * 설정 탭 컴포넌트
 */
@Composable
fun SettingsTab(
    uiState: com.jg.voicenote.feature.voice.ui.VoiceUiState? = null,
    onIntent: ((com.jg.voicenote.feature.voice.ui.VoiceIntent) -> Unit)? = null
) {
    var showSampleRateDialog by remember { mutableStateOf(false) }
    var showBitRateDialog by remember { mutableStateOf(false) }
    var showQualityDialog by remember { mutableStateOf(false) }
    
    // UI 상태에서 현재 설정 값들 가져오기
    val selectedSampleRate = uiState?.sampleRate ?: "44100Hz"
    val selectedBitRate = uiState?.bitRate ?: "192kbps"
    val selectedQuality = uiState?.quality ?: "정상 음질"
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "설정",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // 설정 항목들
        SettingItem(
            title = stringResource(R.string.setting_sample_rate_with_value, selectedSampleRate), 
            subtitle = stringResource(R.string.setting_quality_description),
            onClick = { showSampleRateDialog = true }
        )
        
        SettingItem(
            title = stringResource(R.string.setting_bit_rate_with_value, selectedBitRate), 
            subtitle = stringResource(R.string.setting_quality_description),
            onClick = { showBitRateDialog = true }
        )
        
        SettingItem(
            title = stringResource(R.string.setting_quality_with_value, selectedQuality), 
            subtitle = stringResource(R.string.setting_quality_description),
            onClick = { showQualityDialog = true }
        )
    }
    
    // 샘플레이트 선택 다이얼로그
    if (showSampleRateDialog) {
        SampleRateDialog(
            currentSelection = selectedSampleRate,
            onSelectionChanged = { newValue ->
                onIntent?.invoke(com.jg.voicenote.feature.voice.ui.VoiceIntent.UpdateSampleRate(newValue))
            },
            onDismiss = { showSampleRateDialog = false }
        )
    }
    
    // 비트레이트 선택 다이얼로그
    if (showBitRateDialog) {
        BitRateDialog(
            currentSelection = selectedBitRate,
            onSelectionChanged = { newValue ->
                onIntent?.invoke(com.jg.voicenote.feature.voice.ui.VoiceIntent.UpdateBitRate(newValue))
            },
            onDismiss = { showBitRateDialog = false }
        )
    }
    
    // 품질 선택 다이얼로그
    if (showQualityDialog) {
        QualityDialog(
            currentSelection = selectedQuality,
            onSelectionChanged = { newValue ->
                onIntent?.invoke(com.jg.voicenote.feature.voice.ui.VoiceIntent.UpdateQuality(newValue))
            },
            onDismiss = { showQualityDialog = false }
        )
    }
}

@Composable
private fun SettingItem(
    title: String,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/**
 * 샘플레이트 선택 다이얼로그
 */
@Composable
private fun SampleRateDialog(
    currentSelection: String,
    onSelectionChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sampleRates = SampleRate.entries.map { it.displayName }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.sample_rate_selection)) },
        text = {
            Column {
                sampleRates.forEach { sampleRate ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                onSelectionChanged(sampleRate)
                                onDismiss()
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSelection == sampleRate,
                            onClick = {
                                onSelectionChanged(sampleRate)
                                onDismiss()
                            }
                        )
                        Text(
                            text = sampleRate,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("확인")
            }
        }
    )
}

/**
 * 비트레이트 선택 다이얼로그
 */
@Composable
private fun BitRateDialog(
    currentSelection: String,
    onSelectionChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val bitRates = BitRate.entries.map { it.displayName }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.bit_rate_selection)) },
        text = {
            Column {
                bitRates.forEach { bitRate ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                onSelectionChanged(bitRate)
                                onDismiss()
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSelection == bitRate,
                            onClick = {
                                onSelectionChanged(bitRate)
                                onDismiss()
                            }
                        )
                        Text(
                            text = bitRate,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("확인")
            }
        }
    )
}

/**
 * 품질 선택 다이얼로그
 */
@Composable
private fun QualityDialog(
    currentSelection: String,
    onSelectionChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val qualities = AudioQuality.entries.map { it.displayName }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.quality_selection)) },
        text = {
            Column {
                qualities.forEach { quality ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                onSelectionChanged(quality)
                                onDismiss()
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSelection == quality,
                            onClick = {
                                onSelectionChanged(quality)
                                onDismiss()
                            }
                        )
                        Text(
                            text = quality,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("확인")
            }
        }
    )
}