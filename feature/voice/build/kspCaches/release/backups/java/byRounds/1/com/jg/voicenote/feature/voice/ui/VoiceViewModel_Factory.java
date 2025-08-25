package com.jg.voicenote.feature.voice.ui;

import com.jg.voicenote.core.domain.usecase.DeleteVoiceRecordUseCase;
import com.jg.voicenote.core.domain.usecase.GetAllVoiceRecordsUseCase;
import com.jg.voicenote.core.domain.usecase.GetAudioSettingsUseCase;
import com.jg.voicenote.core.domain.usecase.UpdateAudioSettingsUseCase;
import com.jg.voicenote.feature.voice.recorder.AudioPlayer;
import com.jg.voicenote.feature.voice.recorder.AudioRecorder;
import com.jg.voicenote.feature.voice.service.VoiceRecordingManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class VoiceViewModel_Factory implements Factory<VoiceViewModel> {
  private final Provider<GetAllVoiceRecordsUseCase> getAllVoiceRecordsUseCaseProvider;

  private final Provider<DeleteVoiceRecordUseCase> deleteVoiceRecordUseCaseProvider;

  private final Provider<VoiceRecordingManager> recordingManagerProvider;

  private final Provider<AudioPlayer> audioPlayerProvider;

  private final Provider<GetAudioSettingsUseCase> getAudioSettingsUseCaseProvider;

  private final Provider<UpdateAudioSettingsUseCase> updateAudioSettingsUseCaseProvider;

  private final Provider<AudioRecorder> audioRecorderProvider;

  public VoiceViewModel_Factory(
      Provider<GetAllVoiceRecordsUseCase> getAllVoiceRecordsUseCaseProvider,
      Provider<DeleteVoiceRecordUseCase> deleteVoiceRecordUseCaseProvider,
      Provider<VoiceRecordingManager> recordingManagerProvider,
      Provider<AudioPlayer> audioPlayerProvider,
      Provider<GetAudioSettingsUseCase> getAudioSettingsUseCaseProvider,
      Provider<UpdateAudioSettingsUseCase> updateAudioSettingsUseCaseProvider,
      Provider<AudioRecorder> audioRecorderProvider) {
    this.getAllVoiceRecordsUseCaseProvider = getAllVoiceRecordsUseCaseProvider;
    this.deleteVoiceRecordUseCaseProvider = deleteVoiceRecordUseCaseProvider;
    this.recordingManagerProvider = recordingManagerProvider;
    this.audioPlayerProvider = audioPlayerProvider;
    this.getAudioSettingsUseCaseProvider = getAudioSettingsUseCaseProvider;
    this.updateAudioSettingsUseCaseProvider = updateAudioSettingsUseCaseProvider;
    this.audioRecorderProvider = audioRecorderProvider;
  }

  @Override
  public VoiceViewModel get() {
    return newInstance(getAllVoiceRecordsUseCaseProvider.get(), deleteVoiceRecordUseCaseProvider.get(), recordingManagerProvider.get(), audioPlayerProvider.get(), getAudioSettingsUseCaseProvider.get(), updateAudioSettingsUseCaseProvider.get(), audioRecorderProvider.get());
  }

  public static VoiceViewModel_Factory create(
      Provider<GetAllVoiceRecordsUseCase> getAllVoiceRecordsUseCaseProvider,
      Provider<DeleteVoiceRecordUseCase> deleteVoiceRecordUseCaseProvider,
      Provider<VoiceRecordingManager> recordingManagerProvider,
      Provider<AudioPlayer> audioPlayerProvider,
      Provider<GetAudioSettingsUseCase> getAudioSettingsUseCaseProvider,
      Provider<UpdateAudioSettingsUseCase> updateAudioSettingsUseCaseProvider,
      Provider<AudioRecorder> audioRecorderProvider) {
    return new VoiceViewModel_Factory(getAllVoiceRecordsUseCaseProvider, deleteVoiceRecordUseCaseProvider, recordingManagerProvider, audioPlayerProvider, getAudioSettingsUseCaseProvider, updateAudioSettingsUseCaseProvider, audioRecorderProvider);
  }

  public static VoiceViewModel newInstance(GetAllVoiceRecordsUseCase getAllVoiceRecordsUseCase,
      DeleteVoiceRecordUseCase deleteVoiceRecordUseCase, VoiceRecordingManager recordingManager,
      AudioPlayer audioPlayer, GetAudioSettingsUseCase getAudioSettingsUseCase,
      UpdateAudioSettingsUseCase updateAudioSettingsUseCase, AudioRecorder audioRecorder) {
    return new VoiceViewModel(getAllVoiceRecordsUseCase, deleteVoiceRecordUseCase, recordingManager, audioPlayer, getAudioSettingsUseCase, updateAudioSettingsUseCase, audioRecorder);
  }
}
