package com.jg.voicenote.feature.voice.service;

import com.jg.voicenote.core.domain.usecase.SaveVoiceRecordUseCase;
import com.jg.voicenote.feature.voice.recorder.AudioRecorder;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class RecordingService_MembersInjector implements MembersInjector<RecordingService> {
  private final Provider<AudioRecorder> audioRecorderProvider;

  private final Provider<SaveVoiceRecordUseCase> saveVoiceRecordUseCaseProvider;

  public RecordingService_MembersInjector(Provider<AudioRecorder> audioRecorderProvider,
      Provider<SaveVoiceRecordUseCase> saveVoiceRecordUseCaseProvider) {
    this.audioRecorderProvider = audioRecorderProvider;
    this.saveVoiceRecordUseCaseProvider = saveVoiceRecordUseCaseProvider;
  }

  public static MembersInjector<RecordingService> create(
      Provider<AudioRecorder> audioRecorderProvider,
      Provider<SaveVoiceRecordUseCase> saveVoiceRecordUseCaseProvider) {
    return new RecordingService_MembersInjector(audioRecorderProvider, saveVoiceRecordUseCaseProvider);
  }

  @Override
  public void injectMembers(RecordingService instance) {
    injectAudioRecorder(instance, audioRecorderProvider.get());
    injectSaveVoiceRecordUseCase(instance, saveVoiceRecordUseCaseProvider.get());
  }

  @InjectedFieldSignature("com.jg.voicenote.feature.voice.service.RecordingService.audioRecorder")
  public static void injectAudioRecorder(RecordingService instance, AudioRecorder audioRecorder) {
    instance.audioRecorder = audioRecorder;
  }

  @InjectedFieldSignature("com.jg.voicenote.feature.voice.service.RecordingService.saveVoiceRecordUseCase")
  public static void injectSaveVoiceRecordUseCase(RecordingService instance,
      SaveVoiceRecordUseCase saveVoiceRecordUseCase) {
    instance.saveVoiceRecordUseCase = saveVoiceRecordUseCase;
  }
}
