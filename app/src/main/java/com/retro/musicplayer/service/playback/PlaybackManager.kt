package com.retro.musicplayer.service.playback

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.retro.musicplayer.data.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Quản lý playback logic, tách biệt khỏi MediaSessionService.
 * 
 * Responsibilities:
 * - Khởi tạo và quản lý ExoPlayer
 * - Quản lý queue và playback state
 * - Xử lý shuffle/repeat modes
 * - Expose state qua StateFlow
 */
@Singleton
class PlaybackManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    // ExoPlayer instance
    private var _player: ExoPlayer? = null
    val player: ExoPlayer
        get() = _player ?: createPlayer().also { _player = it }
    
    // Current queue
    private val _queue = MutableStateFlow<List<Song>>(emptyList())
    val queue: StateFlow<List<Song>> = _queue.asStateFlow()
    
    // Playback state
    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()
    
    // Original queue (before shuffle)
    private var originalQueue: List<Song> = emptyList()
    
    /**
     * Tạo và cấu hình ExoPlayer.
     */
    private fun createPlayer(): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setHandleAudioBecomingNoisy(true) // Pause khi headphone disconnect
            .build()
            .apply {
                addListener(playerListener)
                // Start position update loop
                startPositionUpdateLoop()
            }
    }
    
    /**
     * Player event listener.
     */
    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            updatePlaybackState()
        }
        
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updatePlaybackState()
        }
        
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updatePlaybackState()
        }
        
        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            _playbackState.update { it.copy(
                shuffleMode = if (shuffleModeEnabled) ShuffleMode.ON else ShuffleMode.OFF
            )}
        }
        
        override fun onRepeatModeChanged(repeatMode: Int) {
            val mode = when (repeatMode) {
                Player.REPEAT_MODE_ONE -> RepeatMode.ONE
                Player.REPEAT_MODE_ALL -> RepeatMode.ALL
                else -> RepeatMode.OFF
            }
            _playbackState.update { it.copy(repeatMode = mode) }
        }
    }
    
    /**
     * Cập nhật position liên tục khi đang phát.
     */
    private fun startPositionUpdateLoop() {
        scope.launch {
            while (isActive) {
                if (_player?.isPlaying == true) {
                    _playbackState.update { state ->
                        state.copy(
                            currentPosition = _player?.currentPosition ?: 0L,
                            duration = _player?.duration?.takeIf { it > 0 } ?: state.duration
                        )
                    }
                }
                delay(500) // Update every 500ms
            }
        }
    }
    
    /**
     * Cập nhật playback state từ player.
     */
    private fun updatePlaybackState() {
        val p = _player ?: return
        val currentIndex = p.currentMediaItemIndex
        val currentSong = _queue.value.getOrNull(currentIndex)
        
        _playbackState.update { state ->
            state.copy(
                currentSong = currentSong,
                isPlaying = p.isPlaying,
                currentPosition = p.currentPosition,
                duration = p.duration.takeIf { it > 0 } ?: 0L,
                currentIndex = currentIndex,
                queueSize = _queue.value.size,
                isReady = p.playbackState == Player.STATE_READY,
                isBuffering = p.playbackState == Player.STATE_BUFFERING
            )
        }
    }
    
    // ==================== PLAYBACK CONTROLS ====================
    
    /**
     * Phát danh sách bài hát, bắt đầu từ index cho trước.
     */
    fun playQueue(songs: List<Song>, startIndex: Int = 0) {
        if (songs.isEmpty()) return
        
        originalQueue = songs
        _queue.value = songs
        
        val mediaItems = songs.map { song ->
            MediaItem.Builder()
                .setUri(song.path.toUri())
                .setMediaId(song.id.toString())
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .setAlbumTitle(song.album)
                        .setArtworkUri(song.albumArtUri?.let { Uri.parse(it) })
                        .build()
                )
                .build()
        }
        
        player.setMediaItems(mediaItems, startIndex, 0)
        player.prepare()
        player.play()
    }
    
    /**
     * Phát một bài hát đơn lẻ.
     */
    fun playSong(song: Song) {
        playQueue(listOf(song), 0)
    }
    
    /**
     * Thêm bài hát vào cuối queue.
     */
    fun addToQueue(song: Song) {
        val mediaItem = MediaItem.Builder()
            .setUri(song.path.toUri())
            .setMediaId(song.id.toString())
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(song.title)
                    .setArtist(song.artist)
                    .setAlbumTitle(song.album)
                    .build()
            )
            .build()
        
        player.addMediaItem(mediaItem)
        _queue.update { it + song }
        originalQueue = originalQueue + song
    }
    
    /**
     * Play/Pause toggle.
     */
    fun togglePlayPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }
    
    /**
     * Play.
     */
    fun play() {
        player.play()
    }
    
    /**
     * Pause.
     */
    fun pause() {
        player.pause()
    }
    
    /**
     * Skip to next.
     */
    fun skipToNext() {
        if (player.hasNextMediaItem()) {
            player.seekToNextMediaItem()
        }
    }
    
    /**
     * Skip to previous.
     */
    fun skipToPrevious() {
        // Nếu đang phát > 3 giây thì seek về đầu, không thì skip prev
        if (player.currentPosition > 3000) {
            player.seekTo(0)
        } else if (player.hasPreviousMediaItem()) {
            player.seekToPreviousMediaItem()
        } else {
            player.seekTo(0)
        }
    }
    
    /**
     * Seek to position (ms).
     */
    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }
    
    /**
     * Seek to position (0.0 - 1.0).
     */
    fun seekToFraction(fraction: Float) {
        val position = (player.duration * fraction).toLong()
        player.seekTo(position)
    }
    
    /**
     * Skip to specific index in queue.
     */
    fun skipToIndex(index: Int) {
        if (index in 0 until player.mediaItemCount) {
            player.seekTo(index, 0)
        }
    }
    
    // ==================== SHUFFLE & REPEAT ====================
    
    /**
     * Toggle shuffle mode.
     */
    fun toggleShuffle() {
        player.shuffleModeEnabled = !player.shuffleModeEnabled
    }
    
    /**
     * Set shuffle mode.
     */
    fun setShuffleMode(mode: ShuffleMode) {
        player.shuffleModeEnabled = mode == ShuffleMode.ON
    }
    
    /**
     * Cycle repeat mode: OFF -> ALL -> ONE -> OFF.
     */
    fun cycleRepeatMode() {
        val nextMode = when (player.repeatMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
            else -> Player.REPEAT_MODE_OFF
        }
        player.repeatMode = nextMode
    }
    
    /**
     * Set repeat mode.
     */
    fun setRepeatMode(mode: RepeatMode) {
        player.repeatMode = when (mode) {
            RepeatMode.OFF -> Player.REPEAT_MODE_OFF
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
        }
    }
    
    // ==================== CLEANUP ====================
    
    /**
     * Clear queue.
     */
    fun clearQueue() {
        player.clearMediaItems()
        _queue.value = emptyList()
        originalQueue = emptyList()
        _playbackState.value = PlaybackState()
    }
    
    /**
     * Stop playback and clear everything.
     */
    fun stop() {
        player.stop()
        player.clearMediaItems()
        _queue.value = emptyList()
        originalQueue = emptyList()
        _playbackState.value = PlaybackState()
    }
    
    /**
     * Release player resources.
     */
    fun release() {
        scope.cancel()
        _player?.removeListener(playerListener)
        _player?.release()
        _player = null
    }
}
