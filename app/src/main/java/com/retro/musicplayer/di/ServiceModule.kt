package com.retro.musicplayer.di

import android.content.Context
import com.retro.musicplayer.service.scanner.MediaStoreScanner
import com.retro.musicplayer.service.scanner.MusicScanner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module cung cấp các service dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    
    /**
     * Cung cấp MusicScanner implementation (MediaStoreScanner).
     */
    @Provides
    @Singleton
    fun provideMusicScanner(
        @ApplicationContext context: Context
    ): MusicScanner {
        return MediaStoreScanner(context)
    }
}
