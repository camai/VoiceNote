package com.jg.voicenote.core.database.di;

import com.jg.voicenote.core.database.VoiceNoteDatabase;
import com.jg.voicenote.core.database.dao.VoiceRecordDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideVoiceRecordDaoFactory implements Factory<VoiceRecordDao> {
  private final Provider<VoiceNoteDatabase> databaseProvider;

  public DatabaseModule_ProvideVoiceRecordDaoFactory(Provider<VoiceNoteDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public VoiceRecordDao get() {
    return provideVoiceRecordDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideVoiceRecordDaoFactory create(
      Provider<VoiceNoteDatabase> databaseProvider) {
    return new DatabaseModule_ProvideVoiceRecordDaoFactory(databaseProvider);
  }

  public static VoiceRecordDao provideVoiceRecordDao(VoiceNoteDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideVoiceRecordDao(database));
  }
}
