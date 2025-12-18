package com.retro.musicplayer.service.scanner

/**
 * Trạng thái của quá trình scan nhạc.
 */
sealed class ScanState {
    /**
     * Đang ở trạng thái chờ (chưa bắt đầu scan).
     */
    object Idle : ScanState()
    
    /**
     * Đang scan nhạc.
     * 
     * @param progress Tiến độ từ 0.0 đến 1.0
     * @param message Thông báo hiện tại
     */
    data class Scanning(
        val progress: Float = 0f,
        val message: String = "Scanning..."
    ) : ScanState()
    
    /**
     * Scan hoàn tất thành công.
     * 
     * @param songsFound Số bài hát tìm thấy
     */
    data class Success(
        val songsFound: Int
    ) : ScanState()
    
    /**
     * Scan thất bại.
     * 
     * @param error Thông báo lỗi
     */
    data class Error(
        val error: String
    ) : ScanState()
}
