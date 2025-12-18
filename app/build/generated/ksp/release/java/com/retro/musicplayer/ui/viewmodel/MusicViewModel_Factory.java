package com.retro.musicplayer.ui.viewmodel;

import com.retro.musicplayer.data.repository.MusicRepository;
import com.retro.musicplayer.service.playback.PlaybackManager;
import com.retro.musicplayer.service.scanner.MusicScanner;
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
public final class MusicViewModel_Factory implements Factory<MusicViewModel> {
  private final Provider<MusicRepository> repositoryProvider;

  private final Provider<MusicScanner> musicScannerProvider;

  private final Provider<PlaybackManager> playbackManagerProvider;

  public MusicViewModel_Factory(Provider<MusicRepository> repositoryProvider,
      Provider<MusicScanner> musicScannerProvider,
      Provider<PlaybackManager> playbackManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.musicScannerProvider = musicScannerProvider;
    this.playbackManagerProvider = playbackManagerProvider;
  }

  @Override
  public MusicViewModel get() {
    return newInstance(repositoryProvider.get(), musicScannerProvider.get(), playbackManagerProvider.get());
  }

  public static MusicViewModel_Factory create(Provider<MusicRepository> repositoryProvider,
      Provider<MusicScanner> musicScannerProvider,
      Provider<PlaybackManager> playbackManagerProvider) {
    return new MusicViewModel_Factory(repositoryProvider, musicScannerProvider, playbackManagerProvider);
  }

  public static MusicViewModel newInstance(MusicRepository repository, MusicScanner musicScanner,
      PlaybackManager playbackManager) {
    return new MusicViewModel(repository, musicScanner, playbackManager);
  }
}
