package com.jg.voicenote.feature.voice.recorder;

import android.content.Context;
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
public final class AudioPlayer_Factory implements Factory<AudioPlayer> {
  private final Provider<Context> contextProvider;

  public AudioPlayer_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AudioPlayer get() {
    return newInstance(contextProvider.get());
  }

  public static AudioPlayer_Factory create(Provider<Context> contextProvider) {
    return new AudioPlayer_Factory(contextProvider);
  }

  public static AudioPlayer newInstance(Context context) {
    return new AudioPlayer(context);
  }
}
