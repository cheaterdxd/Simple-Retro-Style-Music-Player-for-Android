package com.retro.musicplayer.data.database

import androidx.room.*
import com.retro.musicplayer.data.model.Playlist
import com.retro.musicplayer.data.model.PlaylistSongCrossRef
import com.retro.musicplayer.data.model.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object cho Playlist entity.
 * 
 * Quản lý playlists và quan hệ với songs thông qua cross-reference table.
 */
@Dao
interface PlaylistDao {
    
    // ==================== PLAYLIST CRUD ====================
    
    /**
     * Tạo playlist mới, trả về ID của playlist vừa tạo.
     */
    @Insert
    suspend fun insertPlaylist(playlist: Playlist): Long
    
    /**
     * Cập nhật thông tin playlist.
     */
    @Update
    suspend fun updatePlaylist(playlist: Playlist)
    
    /**
     * Xóa playlist (songs trong playlist sẽ bị xóa khỏi cross-ref do CASCADE).
     */
    @Delete
    suspend fun deletePlaylist(playlist: Playlist)
    
    /**
     * Xóa playlist theo ID.
     */
    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: Long)
    
    // ==================== PLAYLIST QUERIES ====================
    
    /**
     * Lấy tất cả playlists.
     */
    @Query("SELECT * FROM playlists ORDER BY updatedAt DESC")
    fun getAllPlaylists(): Flow<List<Playlist>>
    
    /**
     * Lấy playlist theo ID.
     */
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): Playlist?
    
    /**
     * Lấy playlist với danh sách songs.
     */
    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistWithSongs(playlistId: Long): Flow<PlaylistWithSongs?>
    
    /**
     * Lấy tất cả playlists với songs.
     */
    @Transaction
    @Query("SELECT * FROM playlists ORDER BY updatedAt DESC")
    fun getAllPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>>
    
    /**
     * Tìm kiếm playlist theo tên.
     */
    @Query("SELECT * FROM playlists WHERE name LIKE '%' || :query || '%'")
    fun searchPlaylists(query: String): Flow<List<Playlist>>
    
    // ==================== PLAYLIST-SONG OPERATIONS ====================
    
    /**
     * Thêm bài hát vào playlist.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSongToPlaylist(crossRef: PlaylistSongCrossRef)
    
    /**
     * Thêm nhiều bài hát vào playlist.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSongsToPlaylist(crossRefs: List<PlaylistSongCrossRef>)
    
    /**
     * Xóa bài hát khỏi playlist.
     */
    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long)
    
    /**
     * Xóa tất cả bài hát khỏi playlist.
     */
    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId")
    suspend fun clearPlaylist(playlistId: Long)
    
    /**
     * Cập nhật vị trí bài hát trong playlist (để reorder).
     */
    @Query("UPDATE playlist_songs SET position = :newPosition WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun updateSongPosition(playlistId: Long, songId: Long, newPosition: Int)
    
    /**
     * Lấy số lượng bài hát trong playlist.
     */
    @Query("SELECT COUNT(*) FROM playlist_songs WHERE playlistId = :playlistId")
    suspend fun getPlaylistSongCount(playlistId: Long): Int
    
    /**
     * Kiểm tra bài hát có trong playlist không.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM playlist_songs WHERE playlistId = :playlistId AND songId = :songId)")
    suspend fun isSongInPlaylist(playlistId: Long, songId: Long): Boolean
    
    /**
     * Cập nhật song count cho playlist (denormalized field).
     */
    @Query("UPDATE playlists SET songCount = (SELECT COUNT(*) FROM playlist_songs WHERE playlistId = :playlistId), updatedAt = :updatedAt WHERE id = :playlistId")
    suspend fun updatePlaylistSongCount(playlistId: Long, updatedAt: Long = System.currentTimeMillis())
}
