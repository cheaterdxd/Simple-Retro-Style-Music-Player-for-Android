package com.retro.musicplayer.ui.viewmodel;

import com.retro.musicplayer.data.repository.MusicRepository;
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
public final class PlaylistViewModel_Factory implements Factory<PlaylistViewModel> {
  private final Provider<MusicRepository> repositoryProvider;

  public PlaylistViewModel_Factory(Provider<MusicRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public PlaylistViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static PlaylistViewModel_Factory create(Provider<MusicRepository> repositoryProvider) {
    return new PlaylistViewModel_Factory(repositoryProvider);
  }

  public static PlaylistViewModel newInstance(MusicRepository repository) {
    return new PlaylistViewModel(repository);
  }
}
