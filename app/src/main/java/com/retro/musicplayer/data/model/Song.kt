package com.retro.musicplayer.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity đại diện cho một bài hát trong thư viện nhạc.
 * 
 * Được lưu trữ trong Room database và đồng bộ với MediaStore.
 * ID sử dụng từ MediaStore để đảm bảo tính nhất quán.
 */
@Entity(tableName = "songs")
data class Song(
    @PrimaryKey
    val id: Long,
    
    /** Tên bài hát */
    val title: String,
    
    /** Tên nghệ sĩ */
    val artist: String,
    
    /** Tên album */
    val album: String,
    
    /** Thời lượng bài hát (milliseconds) */
    val duration: Long,
    
    /** Đường dẫn file nhạc */
    val path: String,
    
    /** URI của album art (có thể null nếu không có) */
    val albumArtUri: String? = null,
    
    /** Thời điểm thêm vào thư viện (Unix timestamp) */
    val dateAdded: Long = System.currentTimeMillis(),
    
    /** Track number trong album */
    val trackNumber: Int = 0,
    
    /** Năm phát hành */
    val year: Int = 0
) {
    /**
     * Lấy URI của file nhạc để phát.
     */
    fun getContentUri(): Uri = Uri.parse(path)
    
    /**
     * Lấy URI của album art để hiển thị.
     */
    fun albumArtAsUri(): Uri? = albumArtUri?.let { Uri.parse(it) }
    
    /**
     * Format thời lượng thành dạng mm:ss
     */
    fun getFormattedDuration(): String {
        val minutes = (duration / 1000) / 60
        val seconds = (duration / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
    
    companion object {
        /**
         * Tạo Song object trống cho trường hợp không có nhạc đang phát.
         */
        val EMPTY = Song(
            id = -1,
            title = "No song playing",
            artist = "Unknown",
            album = "Unknown",
            duration = 0,
            path = ""
        )
    }
}
