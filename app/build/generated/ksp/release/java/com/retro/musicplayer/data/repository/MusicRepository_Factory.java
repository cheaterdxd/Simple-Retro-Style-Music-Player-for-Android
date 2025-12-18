package com.retro.musicplayer.data.repository;

import com.retro.musicplayer.data.database.PlaylistDao;
import com.retro.musicplayer.data.database.SongDao;
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
public final class MusicRepository_Factory implements Factory<MusicRepository> {
  private final Provider<SongDao> songDaoProvider;

  private final Provider<PlaylistDao> playlistDaoProvider;

  public MusicRepository_Factory(Provider<SongDao> songDaoProvider,
      Provider<PlaylistDao> playlistDaoProvider) {
    this.songDaoProvider = songDaoProvider;
    this.playlistDaoProvider = playlistDaoProvider;
  }

  @Override
  public MusicRepository get() {
    return newInstance(songDaoProvider.get(), playlistDaoProvider.get());
  }

  public static MusicRepository_Factory create(Provider<SongDao> songDaoProvider,
      Provider<PlaylistDao> playlistDaoProvider) {
    return new MusicRepository_Factory(songDaoProvider, playlistDaoProvider);
  }

  public static MusicRepository newInstance(SongDao songDao, PlaylistDao playlistDao) {
    return new MusicRepository(songDao, playlistDao);
  }
}
