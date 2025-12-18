package com.retro.musicplayer.data.repository

import com.retro.musicplayer.data.database.PlaylistDao
import com.retro.musicplayer.data.database.SongDao
import com.retro.musicplayer.data.model.Playlist
import com.retro.musicplayer.data.model.PlaylistSongCrossRef
import com.retro.musicplayer.data.model.PlaylistWithSongs
import com.retro.musicplayer.data.model.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository là single source of truth cho dữ liệu nhạc.
 * 
 * Kết hợp data từ Room database và MediaStore scanner.
 * Tất cả data access đều thông qua Repository.
 */
@Singleton
class MusicRepository @Inject constructor(
    private val songDao: SongDao,
    private val playlistDao: PlaylistDao
) {
    // ==================== SONGS ====================
    
    /**
     * Lấy tất cả bài hát.
     */
    fun getAllSongs(): Flow<List<Song>> = songDao.getAllSongs()
    
    /**
     * Lấy bài hát theo ID.
     */
    suspend fun getSongById(id: Long): Song? = songDao.getSongById(id)
    
    /**
     * Lấy bài hát theo ID (Flow).
     */
    fun getSongByIdFlow(id: Long): Flow<Song?> = songDao.getSongByIdFlow(id)
    
    /**
     * Tìm kiếm bài hát.
     */
    fun searchSongs(query: String): Flow<List<Song>> = songDao.searchSongs(query)
    
    /**
     * Lấy bài hát theo artist.
     */
    fun getSongsByArtist(artist: String): Flow<List<Song>> = songDao.getSongsByArtist(artist)
    
    /**
     * Lấy bài hát theo album.
     */
    fun getSongsByAlbum(album: String): Flow<List<Song>> = songDao.getSongsByAlbum(album)
    
    /**
     * Lấy tất cả artists.
     */
    fun getAllArtists(): Flow<List<String>> = songDao.getAllArtists()
    
    /**
     * Lấy tất cả albums.
     */
    fun getAllAlbums(): Flow<List<String>> = songDao.getAllAlbums()
    
    /**
     * Lấy bài hát mới thêm.
     */
    fun getRecentlyAdded(limit: Int = 20): Flow<List<Song>> = songDao.getRecentlyAdded(limit)
    
    /**
     * Lấy số lượng bài hát.
     */
    fun getSongCount(): Flow<Int> = songDao.getSongCount()
    
    /**
     * Thêm bài hát vào database.
     */
    suspend fun insertSong(song: Song) = songDao.insertSong(song)
    
    /**
     * Thêm nhiều bài hát.
     */
    suspend fun insertSongs(songs: List<Song>) = songDao.insertSongs(songs)
    
    /**
     * Xóa tất cả bài hát (để refresh).
     */
    suspend fun deleteAllSongs() = songDao.deleteAllSongs()
    
    // ==================== PLAYLISTS ====================
    
    /**
     * Lấy tất cả playlists.
     */
    fun getAllPlaylists(): Flow<List<Playlist>> = playlistDao.getAllPlaylists()
    
    /**
     * Lấy playlist với songs.
     */
    fun getPlaylistWithSongs(playlistId: Long): Flow<PlaylistWithSongs?> = 
        playlistDao.getPlaylistWithSongs(playlistId)
    
    /**
     * Lấy tất cả playlists với songs.
     */
    fun getAllPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>> = 
        playlistDao.getAllPlaylistsWithSongs()
    
    /**
     * Tạo playlist mới.
     */
    suspend fun createPlaylist(name: String, description: String? = null): Long {
        val playlist = Playlist.create(name, description)
        return playlistDao.insertPlaylist(playlist)
    }
    
    /**
     * Cập nhật playlist.
     */
    suspend fun updatePlaylist(playlist: Playlist) = playlistDao.updatePlaylist(playlist)
    
    /**
     * Xóa playlist.
     */
    suspend fun deletePlaylist(playlistId: Long) = playlistDao.deletePlaylistById(playlistId)
    
    /**
     * Thêm bài hát vào playlist.
     */
    suspend fun addSongToPlaylist(playlistId: Long, songId: Long) {
        val position = playlistDao.getPlaylistSongCount(playlistId)
        val crossRef = PlaylistSongCrossRef(
            playlistId = playlistId,
            songId = songId,
            position = position
        )
        playlistDao.addSongToPlaylist(crossRef)
        playlistDao.updatePlaylistSongCount(playlistId)
    }
    
    /**
     * Thêm nhiều bài hát vào playlist.
     */
    suspend fun addSongsToPlaylist(playlistId: Long, songIds: List<Long>) {
        val startPosition = playlistDao.getPlaylistSongCount(playlistId)
        val crossRefs = songIds.mapIndexed { index, songId ->
            PlaylistSongCrossRef(
                playlistId = playlistId,
                songId = songId,
                position = startPosition + index
            )
        }
        playlistDao.addSongsToPlaylist(crossRefs)
        playlistDao.updatePlaylistSongCount(playlistId)
    }
    
    /**
     * Xóa bài hát khỏi playlist.
     */
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long) {
        playlistDao.removeSongFromPlaylist(playlistId, songId)
        playlistDao.updatePlaylistSongCount(playlistId)
    }
    
    /**
     * Kiểm tra bài hát có trong playlist không.
     */
    suspend fun isSongInPlaylist(playlistId: Long, songId: Long): Boolean =
        playlistDao.isSongInPlaylist(playlistId, songId)
}
