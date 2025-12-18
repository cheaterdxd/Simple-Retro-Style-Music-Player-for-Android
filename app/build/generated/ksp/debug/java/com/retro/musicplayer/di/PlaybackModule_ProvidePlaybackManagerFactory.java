package com.retro.musicplayer.di;

import android.content.Context;
import com.retro.musicplayer.service.playback.PlaybackManager;
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
public final class PlaybackModule_ProvidePlaybackManagerFactory implements Factory<PlaybackManager> {
  private final Provider<Context> contextProvider;

  public PlaybackModule_ProvidePlaybackManagerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PlaybackManager get() {
    return providePlaybackManager(contextProvider.get());
  }

  public static PlaybackModule_ProvidePlaybackManagerFactory create(
      Provider<Context> contextProvider) {
    return new PlaybackModule_ProvidePlaybackManagerFactory(contextProvider);
  }

  public static PlaybackManager providePlaybackManager(Context context) {
    return Preconditions.checkNotNullFromProvides(PlaybackModule.INSTANCE.providePlaybackManager(context));
  }
}
