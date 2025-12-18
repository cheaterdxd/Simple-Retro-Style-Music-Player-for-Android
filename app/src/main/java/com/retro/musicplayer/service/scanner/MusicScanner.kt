package com.retro.musicplayer.service.scanner

import com.retro.musicplayer.data.model.Song

/**
 * Interface cho music scanning operations.
 * 
 * Cho phép switch giữa các implementation khác nhau
 * (MediaStore, custom folder scan, etc.)
 */
interface MusicScanner {
    
    /**
     * Quét tất cả bài hát từ thiết bị.
     * 
     * @return Danh sách songs tìm được
     */
    suspend fun scanAllMusic(): List<Song>
    
    /**
     * Quét bài hát từ một thư mục cụ thể.
     * 
     * @param folderPath Đường dẫn thư mục
     * @return Danh sách songs trong thư mục
     */
    suspend fun scanFolder(folderPath: String): List<Song>
    
    /**
     * Lấy danh sách các thư mục chứa nhạc.
     * 
     * @return Danh sách đường dẫn thư mục
     */
    suspend fun getMusicFolders(): List<String>
}
