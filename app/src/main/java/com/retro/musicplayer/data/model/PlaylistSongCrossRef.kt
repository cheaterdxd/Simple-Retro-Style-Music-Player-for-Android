package com.retro.musicplayer.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Cross-reference table cho quan hệ many-to-many giữa Playlist và Song.
 * 
 * Cho phép một bài hát xuất hiện trong nhiều playlists,
 * và một playlist chứa nhiều bài hát.
 */
@Entity(
    tableName = "playlist_songs",
    primaryKeys = ["playlistId", "songId"],
    foreignKeys = [
        ForeignKey(
            entity = Playlist::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Song::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("playlistId"),
        Index("songId")
    ]
)
data class PlaylistSongCrossRef(
    val playlistId: Long,
    val songId: Long,
    
    /** Thứ tự bài hát trong playlist (để sắp xếp) */
    val position: Int = 0,
    
    /** Thời điểm thêm vào playlist */
    val addedAt: Long = System.currentTimeMillis()
)
