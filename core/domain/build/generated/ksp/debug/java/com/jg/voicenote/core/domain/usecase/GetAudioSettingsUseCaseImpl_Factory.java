package com.jg.voicenote.core.domain.usecase;

import com.jg.voicenote.core.datastore.VoiceNotePreferences;
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
public final class GetAudioSettingsUseCaseImpl_Factory implements Factory<GetAudioSettingsUseCaseImpl> {
  private final Provider<VoiceNotePreferences> preferencesProvider;

  public GetAudioSettingsUseCaseImpl_Factory(Provider<VoiceNotePreferences> preferencesProvider) {
    this.preferencesProvider = preferencesProvider;
  }

  @Override
  public GetAudioSettingsUseCaseImpl get() {
    return newInstance(preferencesProvider.get());
  }

  public static GetAudioSettingsUseCaseImpl_Factory create(
      Provider<VoiceNotePreferences> preferencesProvider) {
    return new GetAudioSettingsUseCaseImpl_Factory(preferencesProvider);
  }

  public static GetAudioSettingsUseCaseImpl newInstance(VoiceNotePreferences preferences) {
    return new GetAudioSettingsUseCaseImpl(preferences);
  }
}
