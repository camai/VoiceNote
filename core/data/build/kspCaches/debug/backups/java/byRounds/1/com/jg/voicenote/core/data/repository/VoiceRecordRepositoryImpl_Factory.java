package com.jg.voicenote.core.data.repository;

import com.jg.voicenote.core.database.dao.VoiceRecordDao;
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
public final class VoiceRecordRepositoryImpl_Factory implements Factory<VoiceRecordRepositoryImpl> {
  private final Provider<VoiceRecordDao> voiceRecordDaoProvider;

  public VoiceRecordRepositoryImpl_Factory(Provider<VoiceRecordDao> voiceRecordDaoProvider) {
    this.voiceRecordDaoProvider = voiceRecordDaoProvider;
  }

  @Override
  public VoiceRecordRepositoryImpl get() {
    return newInstance(voiceRecordDaoProvider.get());
  }

  public static VoiceRecordRepositoryImpl_Factory create(
      Provider<VoiceRecordDao> voiceRecordDaoProvider) {
    return new VoiceRecordRepositoryImpl_Factory(voiceRecordDaoProvider);
  }

  public static VoiceRecordRepositoryImpl newInstance(VoiceRecordDao voiceRecordDao) {
    return new VoiceRecordRepositoryImpl(voiceRecordDao);
  }
}
