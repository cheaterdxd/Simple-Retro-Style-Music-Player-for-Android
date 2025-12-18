package com.retro.musicplayer.service.playback

import com.retro.musicplayer.data.model.Song

/**
 * Trạng thái hiện tại của player.
 * 
 * Immutable data class để share state giữa Service và UI.
 */
data class PlaybackState(
    /** Bài hát đang phát */
    val currentSong: Song? = null,
    
    /** Đang phát hay không */
    val isPlaying: Boolean = false,
    
    /** Vị trí hiện tại trong bài hát (ms) */
    val currentPosition: Long = 0L,
    
    /** Thời lượng bài hát (ms) */
    val duration: Long = 0L,
    
    /** Index của bài hát trong queue */
    val currentIndex: Int = 0,
    
    /** Tổng số bài trong queue */
    val queueSize: Int = 0,
    
    /** Shuffle mode */
    val shuffleMode: ShuffleMode = ShuffleMode.OFF,
    
    /** Repeat mode */
    val repeatMode: RepeatMode = RepeatMode.OFF,
    
    /** Player đã sẵn sàng chưa */
    val isReady: Boolean = false,
    
    /** Đang buffering */
    val isBuffering: Boolean = false,
    
    /** Error message nếu có */
    val error: String? = null
) {
    /**
     * Progress từ 0.0 đến 1.0
     */
    val progress: Float
        get() = if (duration > 0) currentPosition.toFloat() / duration else 0f
    
    /**
     * Format thời gian hiện tại.
     */
    fun getFormattedPosition(): String = formatTime(currentPosition)
    
    /**
     * Format thời lượng bài hát.
     */
    fun getFormattedDuration(): String = formatTime(duration)
    
    private fun formatTime(timeMs: Long): String {
        val minutes = (timeMs / 1000) / 60
        val seconds = (timeMs / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}

/**
 * Shuffle mode options.
 */
enum class ShuffleMode {
    OFF,    // Phát theo thứ tự
    ON      // Phát ngẫu nhiên
}

/**
 * Repeat mode options.
 */
enum class RepeatMode {
    OFF,    // Không lặp
    ONE,    // Lặp 1 bài
    ALL     // Lặp toàn bộ queue
}
