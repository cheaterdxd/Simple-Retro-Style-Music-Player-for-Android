package com.retro.musicplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retro.musicplayer.data.model.Song
import com.retro.musicplayer.data.repository.MusicRepository
import com.retro.musicplayer.service.playback.PlaybackManager
import com.retro.musicplayer.service.playback.PlaybackState
import com.retro.musicplayer.service.scanner.MusicScanner
import com.retro.musicplayer.service.scanner.ScanState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel cho Home screen - quản lý music library.
 * 
 * Responsibilities:
 * - Scan và load songs
 * - Search songs
 * - Filter theo artist/album
 * - Trigger playback
 */
@HiltViewModel
class MusicViewModel @Inject constructor(
    private val repository: MusicRepository,
    private val musicScanner: MusicScanner,
    private val playbackManager: PlaybackManager
) : ViewModel() {
    
    // ==================== STATE ====================
    
    /** Trạng thái scan */
    private val _scanState = MutableStateFlow<ScanState>(ScanState.Idle)
    val scanState: StateFlow<ScanState> = _scanState.asStateFlow()
    
    /** Search query */
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    /** Tab hiện tại: songs, albums, artists */
    private val _currentTab = MutableStateFlow(LibraryTab.SONGS)
    val currentTab: StateFlow<LibraryTab> = _currentTab.asStateFlow()
    
    /** Danh sách songs */
    val songs: StateFlow<List<Song>> = repository.getAllSongs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    /** Kết quả search */
    val searchResults: StateFlow<List<Song>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getAllSongs()
            } else {
                repository.searchSongs(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    /** Danh sách artists */
    val artists: StateFlow<List<String>> = repository.getAllArtists()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    /** Danh sách albums */
    val albums: StateFlow<List<String>> = repository.getAllAlbums()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    /** Số lượng songs */
    val songCount: StateFlow<Int> = repository.getSongCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
    
    /** Playback state từ PlaybackManager */
    val playbackState: StateFlow<PlaybackState> = playbackManager.playbackState
    
    // ==================== ACTIONS ====================
    
    /**
     * Scan all music từ device.
     */
    fun scanMusic() {
        viewModelScope.launch {
            _scanState.value = ScanState.Scanning(0f, "Scanning device...")
            
            try {
                val songs = musicScanner.scanAllMusic()
                
                // Clear old data và insert mới
                repository.deleteAllSongs()
                repository.insertSongs(songs)
                
                _scanState.value = ScanState.Success(songs.size)
            } catch (e: Exception) {
                _scanState.value = ScanState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    /**
     * Reset scan state.
     */
    fun resetScanState() {
        _scanState.value = ScanState.Idle
    }
    
    /**
     * Update search query.
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    /**
     * Switch tab.
     */
    fun setCurrentTab(tab: LibraryTab) {
        _currentTab.value = tab
    }
    
    /**
     * Phát một bài hát (và queue toàn bộ list).
     */
    fun playSong(song: Song, allSongs: List<Song> = songs.value) {
        val index = allSongs.indexOf(song).takeIf { it >= 0 } ?: 0
        playbackManager.playQueue(allSongs, index)
    }
    
    /**
     * Phát tất cả songs.
     */
    fun playAll() {
        val allSongs = songs.value
        if (allSongs.isNotEmpty()) {
            playbackManager.playQueue(allSongs, 0)
        }
    }
    
    /**
     * Shuffle play all songs.
     */
    fun shuffleAll() {
        val allSongs = songs.value.shuffled()
        if (allSongs.isNotEmpty()) {
            playbackManager.playQueue(allSongs, 0)
        }
    }
    
    /**
     * Add song to queue.
     */
    fun addToQueue(song: Song) {
        playbackManager.addToQueue(song)
    }
    
    /**
     * Toggle play/pause.
     */
    fun togglePlayPause() {
        playbackManager.togglePlayPause()
    }
    
    /**
     * Skip to next song.
     */
    fun skipToNext() {
        playbackManager.skipToNext()
    }
    
    /**
     * Skip to previous song.
     */
    fun skipToPrevious() {
        playbackManager.skipToPrevious()
    }
    
    /**
     * Stop playback and clear queue.
     */
    fun stopPlayback() {
        playbackManager.stop()
    }
}

/**
 * Library tabs.
 */
enum class LibraryTab {
    SONGS,
    ALBUMS,
    ARTISTS
}
