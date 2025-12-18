package com.retro.musicplayer.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.retro.musicplayer.data.model.Playlist
import com.retro.musicplayer.data.model.PlaylistSongCrossRef
import com.retro.musicplayer.data.model.Song

/**
 * Room Database cho ứng dụng Retro Music Player.
 * 
 * Quản lý các tables: songs, playlists, playlist_songs.
 * Sử dụng singleton pattern thông qua Hilt DI.
 */
@Database(
    entities = [
        Song::class,
        Playlist::class,
        PlaylistSongCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MusicDatabase : RoomDatabase() {
    
    /**
     * DAO cho songs table.
     */
    abstract fun songDao(): SongDao
    
    /**
     * DAO cho playlists table.
     */
    abstract fun playlistDao(): PlaylistDao
    
    companion object {
        const val DATABASE_NAME = "retro_music_database"
        
        /**
         * Tạo database instance (dùng cho manual creation nếu không dùng Hilt).
         */
        fun create(context: Context): MusicDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MusicDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
