package com.retro.musicplayer

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.compose.rememberNavController
import com.google.common.util.concurrent.MoreExecutors
import com.retro.musicplayer.service.playback.RetroMusicService
import com.retro.musicplayer.ui.navigation.NavGraph
import com.retro.musicplayer.ui.theme.RetroMusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity - Entry point của ứng dụng.
 * 
 * Single Activity architecture với Compose Navigation.
 * Kết nối với MediaSessionService để control playback.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private var mediaController: MediaController? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Start music service
        startMusicService()
        
        setContent {
            RetroMusicPlayerTheme(darkTheme = false) {
                val navController = rememberNavController()
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        navController = navController,
                        onExitApp = { exitApp() }
                    )
                }
            }
        }
    }
    
    /**
     * Start và connect to MediaSessionService.
     */
    private fun startMusicService() {
        // Start service
        val intent = Intent(this, RetroMusicService::class.java)
        startService(intent)
        
        // Connect to MediaSession
        val sessionToken = SessionToken(this, ComponentName(this, RetroMusicService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        
        controllerFuture.addListener({
            mediaController = controllerFuture.get()
        }, MoreExecutors.directExecutor())
    }
    
    /**
     * Exit app completely - stop service and finish activity.
     */
    private fun exitApp() {
        // Stop the music service
        val intent = Intent(this, RetroMusicService::class.java)
        stopService(intent)
        
        // Release media controller
        mediaController?.release()
        mediaController = null
        
        // Finish activity and exit process
        finishAffinity()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        mediaController?.release()
    }
}
