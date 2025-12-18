package com.retro.musicplayer.service.playback

import android.app.PendingIntent
import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.retro.musicplayer.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * MediaSessionService cho background music playback.
 * 
 * Service này:
 * - Cho phép nhạc phát tiếp khi app minimize
 * - Hiển thị notification với controls
 * - Tích hợp với hệ thống media controls (lockscreen, Bluetooth, etc.)
 * - Xử lý audio focus
 */
@AndroidEntryPoint
class RetroMusicService : MediaSessionService() {
    
    @Inject
    lateinit var playbackManager: PlaybackManager
    
    private var mediaSession: MediaSession? = null
    
    /**
     * Khởi tạo service và MediaSession.
     */
    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        
        // Intent để mở app khi tap notification
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Tạo MediaSession với player từ PlaybackManager
        mediaSession = MediaSession.Builder(this, playbackManager.player)
            .setSessionActivity(pendingIntent)
            .setCallback(MediaSessionCallback())
            .build()
    }
    
    /**
     * Trả về MediaSession cho clients kết nối.
     */
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
    
    /**
     * Xử lý khi service bị destroy.
     */
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
        }
        mediaSession = null
        super.onDestroy()
    }
    
    /**
     * Callback để xử lý các requests từ MediaSession.
     */
    private inner class MediaSessionCallback : MediaSession.Callback {
        // Có thể override các phương thức để customize behavior
        // Ví dụ: onConnect, onDisconnect, onMediaButtonEvent, etc.
    }
}
