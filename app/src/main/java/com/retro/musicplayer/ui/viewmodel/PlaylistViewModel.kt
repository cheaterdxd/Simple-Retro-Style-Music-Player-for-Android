package com.retro.musicplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retro.musicplayer.data.model.Playlist
import com.retro.musicplayer.data.model.PlaylistWithSongs
import com.retro.musicplayer.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel cho Playlist management.
 * 
 * Handles:
 * - Create/Update/Delete playlists
 * - Add/Remove songs from playlists
 * - Load playlist details
 */
@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val repository: MusicRepository
) : ViewModel() {
    
    // ==================== STATE ====================
    
    /** Tất cả playlists */
    val playlists: StateFlow<List<Playlist>> = repository.getAllPlaylists()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    /** Tất cả playlists với songs */
    val playlistsWithSongs: StateFlow<List<PlaylistWithSongs>> = 
        repository.getAllPlaylistsWithSongs()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    /** Playlist đang được xem */
    private val _selectedPlaylistId = MutableStateFlow<Long?>(null)
    val selectedPlaylistId: StateFlow<Long?> = _selectedPlaylistId.asStateFlow()
    
    /** Chi tiết playlist đang xem */
    val selectedPlaylist: StateFlow<PlaylistWithSongs?> = _selectedPlaylistId
        .filterNotNull()
        .flatMapLatest { id ->
            repository.getPlaylistWithSongs(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    // ==================== ACTIONS ====================
    
    /**
     * Tạo playlist mới.
     */
    fun createPlaylist(name: String, description: String? = null) {
        viewModelScope.launch {
            repository.createPlaylist(name, description)
        }
    }
    
    /**
     * Xóa playlist.
     */
    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            repository.deletePlaylist(playlistId)
        }
    }
    
    /**
     * Đổi tên playlist.
     */
    fun renamePlaylist(playlist: Playlist, newName: String) {
        viewModelScope.launch {
            repository.updatePlaylist(playlist.copy(
                name = newName,
                updatedAt = System.currentTimeMillis()
            ))
        }
    }
    
    /**
     * Thêm bài hát vào playlist.
     */
    fun addSongToPlaylist(playlistId: Long, songId: Long) {
        viewModelScope.launch {
            repository.addSongToPlaylist(playlistId, songId)
        }
    }
    
    /**
     * Thêm nhiều bài hát vào playlist.
     */
    fun addSongsToPlaylist(playlistId: Long, songIds: List<Long>) {
        viewModelScope.launch {
            repository.addSongsToPlaylist(playlistId, songIds)
        }
    }
    
    /**
     * Xóa bài hát khỏi playlist.
     */
    fun removeSongFromPlaylist(playlistId: Long, songId: Long) {
        viewModelScope.launch {
            repository.removeSongFromPlaylist(playlistId, songId)
        }
    }
    
    /**
     * Chọn playlist để xem chi tiết.
     */
    fun selectPlaylist(playlistId: Long) {
        _selectedPlaylistId.value = playlistId
    }
    
    /**
     * Clear selection.
     */
    fun clearSelection() {
        _selectedPlaylistId.value = null
    }
}
