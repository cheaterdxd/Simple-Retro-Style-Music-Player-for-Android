package com.retro.musicplayer.di

import android.content.Context
import com.retro.musicplayer.service.playback.PlaybackManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module cung cấp playback dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object PlaybackModule {
    
    /**
     * Cung cấp PlaybackManager singleton.
     */
    @Provides
    @Singleton
    fun providePlaybackManager(
        @ApplicationContext context: Context
    ): PlaybackManager {
        return PlaybackManager(context)
    }
}
