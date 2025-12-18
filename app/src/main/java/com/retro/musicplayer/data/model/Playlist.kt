package com.retro.musicplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity đại diện cho một playlist.
 * 
 * Playlist chứa danh sách các bài hát do người dùng tạo.
 * Quan hệ với Song thông qua PlaylistSongCrossRef.
 */
@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    /** Tên playlist */
    val name: String,
    
    /** Mô tả playlist (optional) */
    val description: String? = null,
    
    /** Thời điểm tạo playlist */
    val createdAt: Long = System.currentTimeMillis(),
    
    /** Thời điểm cập nhật cuối */
    val updatedAt: Long = System.currentTimeMillis(),
    
    /** Số lượng bài hát (denormalized để query nhanh) */
    val songCount: Int = 0
) {
    companion object {
        /**
         * Tạo playlist mới với tên cho trước.
         */
        fun create(name: String, description: String? = null): Playlist {
            val now = System.currentTimeMillis()
            return Playlist(
                name = name,
                description = description,
                createdAt = now,
                updatedAt = now
            )
        }
    }
}
