package com.retro.musicplayer.ui.viewmodel;

import com.retro.musicplayer.service.playback.PlaybackManager;
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
public final class PlayerViewModel_Factory implements Factory<PlayerViewModel> {
  private final Provider<PlaybackManager> playbackManagerProvider;

  public PlayerViewModel_Factory(Provider<PlaybackManager> playbackManagerProvider) {
    this.playbackManagerProvider = playbackManagerProvider;
  }

  @Override
  public PlayerViewModel get() {
    return newInstance(playbackManagerProvider.get());
  }

  public static PlayerViewModel_Factory create(Provider<PlaybackManager> playbackManagerProvider) {
    return new PlayerViewModel_Factory(playbackManagerProvider);
  }

  public static PlayerViewModel newInstance(PlaybackManager playbackManager) {
    return new PlayerViewModel(playbackManager);
  }
}
