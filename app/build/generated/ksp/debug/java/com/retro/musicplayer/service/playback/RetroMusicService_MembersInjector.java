package com.retro.musicplayer.service.playback;

import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class RetroMusicService_MembersInjector implements MembersInjector<RetroMusicService> {
  private final Provider<PlaybackManager> playbackManagerProvider;

  public RetroMusicService_MembersInjector(Provider<PlaybackManager> playbackManagerProvider) {
    this.playbackManagerProvider = playbackManagerProvider;
  }

  public static MembersInjector<RetroMusicService> create(
      Provider<PlaybackManager> playbackManagerProvider) {
    return new RetroMusicService_MembersInjector(playbackManagerProvider);
  }

  @Override
  public void injectMembers(RetroMusicService instance) {
    injectPlaybackManager(instance, playbackManagerProvider.get());
  }

  @InjectedFieldSignature("com.retro.musicplayer.service.playback.RetroMusicService.playbackManager")
  public static void injectPlaybackManager(RetroMusicService instance,
      PlaybackManager playbackManager) {
    instance.playbackManager = playbackManager;
  }
}
