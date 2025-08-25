package com.jg.voicenote.feature.voice.recorder;

import android.content.Context;
import com.jg.voicenote.core.datastore.VoiceNotePreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AudioRecorder_Factory implements Factory<AudioRecorder> {
  private final Provider<Context> contextProvider;

  private final Provider<VoiceNotePreferences> preferencesProvider;

  public AudioRecorder_Factory(Provider<Context> contextProvider,
      Provider<VoiceNotePreferences> preferencesProvider) {
    this.contextProvider = contextProvider;
    this.preferencesProvider = preferencesProvider;
  }

  @Override
  public AudioRecorder get() {
    return newInstance(contextProvider.get(), preferencesProvider.get());
  }

  public static AudioRecorder_Factory create(Provider<Context> contextProvider,
      Provider<VoiceNotePreferences> preferencesProvider) {
    return new AudioRecorder_Factory(contextProvider, preferencesProvider);
  }

  public static AudioRecorder newInstance(Context context, VoiceNotePreferences preferences) {
    return new AudioRecorder(context, preferences);
  }
}
