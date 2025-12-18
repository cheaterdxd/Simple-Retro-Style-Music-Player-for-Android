package com.retro.musicplayer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class khởi tạo Hilt dependency injection.
 * 
 * Đây là entry point của ứng dụng, nơi Hilt tạo ra dependency graph
 * cho toàn bộ app.
 */
@HiltAndroidApp
class RetroMusicApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Có thể thêm initialization logic ở đây nếu cần
        // Ví dụ: Timber logging, Crash reporting, etc.
    }
}
