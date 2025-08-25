package com.jg.voicenote.core.database.di;

import android.content.Context;
import com.jg.voicenote.core.database.VoiceNoteDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideVoiceNoteDatabaseFactory implements Factory<VoiceNoteDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideVoiceNoteDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public VoiceNoteDatabase get() {
    return provideVoiceNoteDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideVoiceNoteDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideVoiceNoteDatabaseFactory(contextProvider);
  }

  public static VoiceNoteDatabase provideVoiceNoteDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideVoiceNoteDatabase(context));
  }
}
