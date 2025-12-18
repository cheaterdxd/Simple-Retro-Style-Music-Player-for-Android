package com.retro.musicplayer.di

import android.content.Context
import androidx.room.Room
import com.retro.musicplayer.data.database.MusicDatabase
import com.retro.musicplayer.data.database.PlaylistDao
import com.retro.musicplayer.data.database.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module cung cấp database dependencies.
 * 
 * Tạo singleton instances của MusicDatabase và các DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * Cung cấp MusicDatabase singleton.
     */
    @Provides
    @Singleton
    fun provideMusicDatabase(
        @ApplicationContext context: Context
    ): MusicDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MusicDatabase::class.java,
            MusicDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    /**
     * Cung cấp SongDao.
     */
    @Provides
    @Singleton
    fun provideSongDao(database: MusicDatabase): SongDao {
        return database.songDao()
    }
    
    /**
     * Cung cấp PlaylistDao.
     */
    @Provides
    @Singleton
    fun providePlaylistDao(database: MusicDatabase): PlaylistDao {
        return database.playlistDao()
    }
}
