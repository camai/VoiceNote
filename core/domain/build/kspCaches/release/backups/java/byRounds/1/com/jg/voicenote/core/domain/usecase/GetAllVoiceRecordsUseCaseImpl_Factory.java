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
public final class GetAllVoiceRecordsUseCaseImpl_Factory implements Factory<GetAllVoiceRecordsUseCaseImpl> {
  private final Provider<VoiceRecordRepository> voiceRecordRepositoryProvider;

  public GetAllVoiceRecordsUseCaseImpl_Factory(
      Provider<VoiceRecordRepository> voiceRecordRepositoryProvider) {
    this.voiceRecordRepositoryProvider = voiceRecordRepositoryProvider;
  }

  @Override
  public GetAllVoiceRecordsUseCaseImpl get() {
    return newInstance(voiceRecordRepositoryProvider.get());
  }

  public static GetAllVoiceRecordsUseCaseImpl_Factory create(
      Provider<VoiceRecordRepository> voiceRecordRepositoryProvider) {
    return new GetAllVoiceRecordsUseCaseImpl_Factory(voiceRecordRepositoryProvider);
  }

  public static GetAllVoiceRecordsUseCaseImpl newInstance(
      VoiceRecordRepository voiceRecordRepository) {
    return new GetAllVoiceRecordsUseCaseImpl(voiceRecordRepository);
  }
}
