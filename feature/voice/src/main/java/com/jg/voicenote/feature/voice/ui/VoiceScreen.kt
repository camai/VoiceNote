package com.jg.voicenote.feature.voice.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.jg.voicenote.feature.voice.R
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jg.voicenote.feature.voice.ui.components.PlaybackTab
import com.jg.voicenote.feature.voice.ui.components.RecordingTab
import com.jg.voicenote.feature.voice.ui.components.SettingsTab

/**
 * 음성 녹음 메인 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceScreen(
    modifier: Modifier = Modifier,
    viewModel: VoiceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = selectedTab
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(stringResource(R.string.tab_recording)) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(stringResource(R.string.tab_playback)) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text(stringResource(R.string.tab_settings)) }
            )
        }

        // 탭 컨텐츠
        when (selectedTab) {
            0 -> RecordingTab(
                uiState = uiState,
                onIntent = viewModel::handleIntent
            )
            1 -> PlaybackTab(
                uiState = uiState,
                onIntent = viewModel::handleIntent
            )
            2 -> SettingsTab(
                uiState = uiState,
                onIntent = viewModel::handleIntent
            )
        }
    }

    // 에러 메시지 표시
    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            viewModel.clearError()
        }
    }
    
    // 녹음 저장 완료 토스트 표시
    LaunchedEffect(uiState.showRecordingSavedToast) {
        if (uiState.showRecordingSavedToast) {
            Toast.makeText(context, context.getString(R.string.recording_saved), Toast.LENGTH_SHORT).show()
            viewModel.clearRecordingSavedToast()
        }
    }
}
