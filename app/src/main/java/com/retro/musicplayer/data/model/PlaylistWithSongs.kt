package com.retro.musicplayer.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Data class kết hợp Playlist với danh sách Songs của nó.
 * 
 * Sử dụng Room @Relation để tự động query songs từ cross-reference table.
 * Dùng trong các trường hợp cần hiển thị playlist cùng với songs.
 */
data class PlaylistWithSongs(
    @Embedded
    val playlist: Playlist,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PlaylistSongCrossRef::class,
            parentColumn = "playlistId",
            entityColumn = "songId"
        )
    )
    val songs: List<Song>
)
