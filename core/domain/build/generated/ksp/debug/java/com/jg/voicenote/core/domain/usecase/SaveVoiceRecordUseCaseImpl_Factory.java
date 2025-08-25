package com.jg.voicenote.core.domain.usecase;

import com.jg.voicenote.core.domain.repository.VoiceRecordRepository;
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
public final class SaveVoiceRecordUseCaseImpl_Factory implements Factory<SaveVoiceRecordUseCaseImpl> {
  private final Provider<VoiceRecordRepository> voiceRecordRepositoryProvider;

  public SaveVoiceRecordUseCaseImpl_Factory(
      Provider<VoiceRecordRepository> voiceRecordRepositoryProvider) {
    this.voiceRecordRepositoryProvider = voiceRecordRepositoryProvider;
  }

  @Override
  public SaveVoiceRecordUseCaseImpl get() {
    return newInstance(voiceRecordRepositoryProvider.get());
  }

  public static SaveVoiceRecordUseCaseImpl_Factory create(
      Provider<VoiceRecordRepository> voiceRecordRepositoryProvider) {
    return new SaveVoiceRecordUseCaseImpl_Factory(voiceRecordRepositoryProvider);
  }

  public static SaveVoiceRecordUseCaseImpl newInstance(
      VoiceRecordRepository voiceRecordRepository) {
    return new SaveVoiceRecordUseCaseImpl(voiceRecordRepository);
  }
}
