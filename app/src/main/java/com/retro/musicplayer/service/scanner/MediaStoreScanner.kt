package com.retro.musicplayer.service.scanner

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.retro.musicplayer.data.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation của MusicScanner sử dụng Android MediaStore API.
 * 
 * Quét audio files từ MediaStore content provider.
 * Đây là cách chính thức và hiệu quả nhất để scan nhạc trên Android.
 */
class MediaStoreScanner @Inject constructor(
    private val context: Context
) : MusicScanner {
    
    companion object {
        // URI cho album art
        private val ALBUM_ART_URI = Uri.parse("content://media/external/audio/albumart")
        
        // Minimum duration để filter (30 seconds)
        private const val MIN_DURATION_MS = 30_000L
        
        // Projection cho query
        private val SONG_PROJECTION = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.YEAR
        )
    }
    
    /**
     * Quét tất cả audio files từ MediaStore.
     */
    override suspend fun scanAllMusic(): List<Song> = withContext(Dispatchers.IO) {
        val songs = mutableListOf<Song>()
        
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        
        // Selection: chỉ lấy music files có duration >= 30s
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} = 1 AND ${MediaStore.Audio.Media.DURATION} >= ?"
        val selectionArgs = arrayOf(MIN_DURATION_MS.toString())
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        
        context.contentResolver.query(
            collection,
            SONG_PROJECTION,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
            val trackColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
            val yearColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)
            
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val albumId = cursor.getLong(albumIdColumn)
                
                val song = Song(
                    id = id,
                    title = cursor.getString(titleColumn) ?: "Unknown",
                    artist = cursor.getString(artistColumn) ?: "Unknown Artist",
                    album = cursor.getString(albumColumn) ?: "Unknown Album",
                    duration = cursor.getLong(durationColumn),
                    path = cursor.getString(dataColumn) ?: "",
                    albumArtUri = getAlbumArtUri(albumId),
                    dateAdded = cursor.getLong(dateAddedColumn) * 1000, // Convert to millis
                    trackNumber = cursor.getInt(trackColumn),
                    year = cursor.getInt(yearColumn)
                )
                songs.add(song)
            }
        }
        
        songs
    }
    
    /**
     * Quét audio files từ thư mục cụ thể.
     */
    override suspend fun scanFolder(folderPath: String): List<Song> = withContext(Dispatchers.IO) {
        val songs = mutableListOf<Song>()
        
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        
        // Selection: music files trong folder cụ thể
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} = 1 AND " +
                "${MediaStore.Audio.Media.DURATION} >= ? AND " +
                "${MediaStore.Audio.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf(MIN_DURATION_MS.toString(), "$folderPath%")
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        
        context.contentResolver.query(
            collection,
            SONG_PROJECTION,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
            val trackColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
            val yearColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)
            
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val albumId = cursor.getLong(albumIdColumn)
                
                val song = Song(
                    id = id,
                    title = cursor.getString(titleColumn) ?: "Unknown",
                    artist = cursor.getString(artistColumn) ?: "Unknown Artist",
                    album = cursor.getString(albumColumn) ?: "Unknown Album",
                    duration = cursor.getLong(durationColumn),
                    path = cursor.getString(dataColumn) ?: "",
                    albumArtUri = getAlbumArtUri(albumId),
                    dateAdded = cursor.getLong(dateAddedColumn) * 1000,
                    trackNumber = cursor.getInt(trackColumn),
                    year = cursor.getInt(yearColumn)
                )
                songs.add(song)
            }
        }
        
        songs
    }
    
    /**
     * Lấy danh sách các thư mục chứa nhạc.
     */
    override suspend fun getMusicFolders(): List<String> = withContext(Dispatchers.IO) {
        val folders = mutableSetOf<String>()
        
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} = 1"
        
        context.contentResolver.query(
            collection,
            projection,
            selection,
            null,
            null
        )?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            
            while (cursor.moveToNext()) {
                val path = cursor.getString(dataColumn)
                if (path != null) {
                    // Extract folder path
                    val folderPath = path.substringBeforeLast('/')
                    folders.add(folderPath)
                }
            }
        }
        
        folders.toList().sorted()
    }
    
    /**
     * Tạo URI cho album art.
     */
    private fun getAlbumArtUri(albumId: Long): String {
        return ContentUris.withAppendedId(ALBUM_ART_URI, albumId).toString()
    }
}
