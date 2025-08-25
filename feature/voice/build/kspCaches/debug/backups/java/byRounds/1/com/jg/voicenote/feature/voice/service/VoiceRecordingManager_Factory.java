package com.jg.voicenote.feature.voice.service;

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
public final class VoiceRecordingManager_Factory implements Factory<VoiceRecordingManager> {
  private final Provider<Context> contextProvider;

  public VoiceRecordingManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public VoiceRecordingManager get() {
    return newInstance(contextProvider.get());
  }

  public static VoiceRecordingManager_Factory create(Provider<Context> contextProvider) {
    return new VoiceRecordingManager_Factory(contextProvider);
  }

  public static VoiceRecordingManager newInstance(Context context) {
    return new VoiceRecordingManager(context);
  }
}
