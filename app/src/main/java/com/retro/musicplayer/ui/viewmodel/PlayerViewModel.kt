package com.retro.musicplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.retro.musicplayer.data.model.Song
import com.retro.musicplayer.service.playback.PlaybackManager
import com.retro.musicplayer.service.playback.PlaybackState
import com.retro.musicplayer.service.playback.RepeatMode
import com.retro.musicplayer.service.playback.ShuffleMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * ViewModel cho Player screen.
 * 
 * Provides playback controls và state cho full player UI.
 * Delegates all operations to PlaybackManager.
 */
@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playbackManager: PlaybackManager
) : ViewModel() {
    
    // ==================== STATE ====================
    
    /** Playback state */
    val playbackState: StateFlow<PlaybackState> = playbackManager.playbackState
    
    /** Current queue */
    val queue: StateFlow<List<Song>> = playbackManager.queue
    
    // ==================== PLAYBACK CONTROLS ====================
    
    /**
     * Toggle play/pause.
     */
    fun togglePlayPause() {
        playbackManager.togglePlayPause()
    }
    
    /**
     * Play.
     */
    fun play() {
        playbackManager.play()
    }
    
    /**
     * Pause.
     */
    fun pause() {
        playbackManager.pause()
    }
    
    /**
     * Skip to next.
     */
    fun skipToNext() {
        playbackManager.skipToNext()
    }
    
    /**
     * Skip to previous.
     */
    fun skipToPrevious() {
        playbackManager.skipToPrevious()
    }
    
    /**
     * Seek to position (ms).
     */
    fun seekTo(positionMs: Long) {
        playbackManager.seekTo(positionMs)
    }
    
    /**
     * Seek to fraction (0.0 - 1.0).
     */
    fun seekToFraction(fraction: Float) {
        playbackManager.seekToFraction(fraction)
    }
    
    /**
     * Skip to specific index in queue.
     */
    fun skipToIndex(index: Int) {
        playbackManager.skipToIndex(index)
    }
    
    // ==================== SHUFFLE & REPEAT ====================
    
    /**
     * Toggle shuffle mode.
     */
    fun toggleShuffle() {
        playbackManager.toggleShuffle()
    }
    
    /**
     * Set shuffle mode.
     */
    fun setShuffleMode(mode: ShuffleMode) {
        playbackManager.setShuffleMode(mode)
    }
    
    /**
     * Cycle repeat mode: OFF -> ALL -> ONE -> OFF.
     */
    fun cycleRepeatMode() {
        playbackManager.cycleRepeatMode()
    }
    
    /**
     * Set repeat mode.
     */
    fun setRepeatMode(mode: RepeatMode) {
        playbackManager.setRepeatMode(mode)
    }
    
    // ==================== QUEUE ====================
    
    /**
     * Clear queue.
     */
    fun clearQueue() {
        playbackManager.clearQueue()
    }
}
