package com.jg.voicenote.core.datastore;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class VoiceNotePreferences_Factory implements Factory<VoiceNotePreferences> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  public VoiceNotePreferences_Factory(Provider<DataStore<Preferences>> dataStoreProvider) {
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public VoiceNotePreferences get() {
    return newInstance(dataStoreProvider.get());
  }

  public static VoiceNotePreferences_Factory create(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new VoiceNotePreferences_Factory(dataStoreProvider);
  }

  public static VoiceNotePreferences newInstance(DataStore<Preferences> dataStore) {
    return new VoiceNotePreferences(dataStore);
  }
}
