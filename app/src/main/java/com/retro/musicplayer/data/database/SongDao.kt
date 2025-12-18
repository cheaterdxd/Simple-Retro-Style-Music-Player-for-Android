package com.retro.musicplayer.data.database

import androidx.room.*
import com.retro.musicplayer.data.model.Song
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object cho Song entity.
 * 
 * Cung cấp các methods CRUD và query cho bảng songs.
 * Sử dụng Flow để reactive updates khi data thay đổi.
 */
@Dao
interface SongDao {
    
    // ==================== INSERT ====================
    
    /**
     * Insert một bài hát mới hoặc replace nếu đã tồn tại.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song)
    
    /**
     * Insert nhiều bài hát cùng lúc.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<Song>)
    
    // ==================== UPDATE ====================
    
    /**
     * Cập nhật thông tin bài hát.
     */
    @Update
    suspend fun updateSong(song: Song)
    
    // ==================== DELETE ====================
    
    /**
     * Xóa một bài hát.
     */
    @Delete
    suspend fun deleteSong(song: Song)
    
    /**
     * Xóa bài hát theo ID.
     */
    @Query("DELETE FROM songs WHERE id = :songId")
    suspend fun deleteSongById(songId: Long)
    
    /**
     * Xóa tất cả bài hát (dùng khi refresh library).
     */
    @Query("DELETE FROM songs")
    suspend fun deleteAllSongs()
    
    // ==================== QUERY ====================
    
    /**
     * Lấy tất cả bài hát, sắp xếp theo title.
     */
    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getAllSongs(): Flow<List<Song>>
    
    /**
     * Lấy tất cả bài hát (non-flow, dùng cho one-time queries).
     */
    @Query("SELECT * FROM songs ORDER BY title ASC")
    suspend fun getAllSongsOnce(): List<Song>
    
    /**
     * Lấy bài hát theo ID.
     */
    @Query("SELECT * FROM songs WHERE id = :songId")
    suspend fun getSongById(songId: Long): Song?
    
    /**
     * Lấy bài hát theo ID (Flow).
     */
    @Query("SELECT * FROM songs WHERE id = :songId")
    fun getSongByIdFlow(songId: Long): Flow<Song?>
    
    /**
     * Tìm kiếm bài hát theo title, artist, hoặc album.
     */
    @Query("""
        SELECT * FROM songs 
        WHERE title LIKE '%' || :query || '%' 
           OR artist LIKE '%' || :query || '%' 
           OR album LIKE '%' || :query || '%'
        ORDER BY title ASC
    """)
    fun searchSongs(query: String): Flow<List<Song>>
    
    /**
     * Lấy bài hát theo artist.
     */
    @Query("SELECT * FROM songs WHERE artist = :artist ORDER BY album, trackNumber")
    fun getSongsByArtist(artist: String): Flow<List<Song>>
    
    /**
     * Lấy bài hát theo album.
     */
    @Query("SELECT * FROM songs WHERE album = :album ORDER BY trackNumber")
    fun getSongsByAlbum(album: String): Flow<List<Song>>
    
    /**
     * Lấy danh sách tất cả artists (distinct).
     */
    @Query("SELECT DISTINCT artist FROM songs ORDER BY artist ASC")
    fun getAllArtists(): Flow<List<String>>
    
    /**
     * Lấy danh sách tất cả albums (distinct).
     */
    @Query("SELECT DISTINCT album FROM songs ORDER BY album ASC")
    fun getAllAlbums(): Flow<List<String>>
    
    /**
     * Đếm tổng số bài hát.
     */
    @Query("SELECT COUNT(*) FROM songs")
    fun getSongCount(): Flow<Int>
    
    /**
     * Lấy bài hát gần đây thêm vào.
     */
    @Query("SELECT * FROM songs ORDER BY dateAdded DESC LIMIT :limit")
    fun getRecentlyAdded(limit: Int = 20): Flow<List<Song>>
    
    /**
     * Kiểm tra bài hát có tồn tại không.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM songs WHERE id = :songId)")
    suspend fun songExists(songId: Long): Boolean
}
