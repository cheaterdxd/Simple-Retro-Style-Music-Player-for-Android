package com.retro.musicplayer.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.retro.musicplayer.data.model.Song
import com.retro.musicplayer.service.playback.PlaybackState
import com.retro.musicplayer.service.scanner.ScanState
import com.retro.musicplayer.ui.components.*
import com.retro.musicplayer.ui.theme.*
import com.retro.musicplayer.ui.viewmodel.LibraryTab
import com.retro.musicplayer.ui.viewmodel.MusicViewModel
import com.retro.musicplayer.ui.viewmodel.PlaylistViewModel

/**
 * Home Screen - Màn hình chính hiển thị music library.
 * 
 * Features:
 * - Tab navigation: Songs, Albums, Artists
 * - Search functionality
 * - Scan music button
 * - Song list với mini player
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSongClick: (Song) -> Unit,
    onPlayerClick: () -> Unit,
    onExitClick: () -> Unit,
    onPlaylistClick: (Long) -> Unit,
    viewModel: MusicViewModel = hiltViewModel(),
    playlistViewModel: PlaylistViewModel = hiltViewModel()
) {
    // State
    val songs by viewModel.songs.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val scanState by viewModel.scanState.collectAsState()
    val playbackState by viewModel.playbackState.collectAsState()
    val songCount by viewModel.songCount.collectAsState()
    
    // Playlist state
    val playlists by playlistViewModel.playlistsWithSongs.collectAsState()
    var songToAddToPlaylist by remember { mutableStateOf<Song?>(null) }
    
    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.scanMusic()
        }
    }
    
    // Request permission and scan
    fun requestPermissionAndScan() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        permissionLauncher.launch(permission)
    }
    
    // Display songs based on search
    val displaySongs = if (searchQuery.isNotBlank()) searchResults else songs
    
    Scaffold(
        topBar = {
            HomeTopBar(
                searchQuery = searchQuery,
                onSearchQueryChange = viewModel::setSearchQuery,
                onScanClick = { requestPermissionAndScan() },
                onExitClick = {
                    viewModel.stopPlayback()
                    onExitClick()
                }
            )
        },
        bottomBar = {
            if (playbackState.currentSong != null) {
                MiniPlayer(
                    playbackState = playbackState,
                    onPlayerClick = onPlayerClick,
                    onPlayPauseClick = { viewModel.togglePlayPause() },
                    onNextClick = { viewModel.skipToNext() }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = currentTab.ordinal,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = RetroOrange
            ) {
                LibraryTab.entries.forEach { tab ->
                    Tab(
                        selected = currentTab == tab,
                        onClick = { viewModel.setCurrentTab(tab) },
                        text = {
                            Text(
                                text = tab.name,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    )
                }
            }
            
            // Content based on scan state
            when (scanState) {
                is ScanState.Scanning -> {
                    ScanningContent()
                }
                is ScanState.Error -> {
                    ErrorContent(
                        message = (scanState as ScanState.Error).error,
                        onRetry = { requestPermissionAndScan() }
                    )
                }
                else -> {
                    if (displaySongs.isEmpty()) {
                        EmptyContent(onScanClick = { requestPermissionAndScan() })
                    } else {
                        // Header with song count and shuffle
                        SongListHeader(
                            songCount = displaySongs.size,
                            onShuffleClick = { viewModel.shuffleAll() }
                        )
                        
                        // Song list
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(
                                items = displaySongs,
                                key = { it.id }
                            ) { song ->
                                SongItem(
                                    song = song,
                                    onClick = { 
                                        viewModel.playSong(song, displaySongs)
                                        onSongClick(song)
                                    },
                                    isPlaying = playbackState.currentSong?.id == song.id,
                                    onMoreClick = {
                                        songToAddToPlaylist = song
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Add to playlist dialog
    songToAddToPlaylist?.let { song ->
        AddToPlaylistDialog(
            song = song,
            onDismiss = { songToAddToPlaylist = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onScanClick: () -> Unit,
    onExitClick: () -> Unit
) {
    var showSearch by remember { mutableStateOf(false) }
    
    TopAppBar(
        title = {
            if (showSearch) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search songs...") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RetroOrange,
                        cursorColor = RetroOrange
                    )
                )
            } else {
                Text(
                    text = "📼 Retro Music",
                    style = MaterialTheme.typography.headlineSmall,
                    color = RetroOrange
                )
            }
        },
        actions = {
            IconButton(onClick = { showSearch = !showSearch }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onScanClick) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Scan music",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onExitClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Exit app",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
private fun SongListHeader(
    songCount: Int,
    onShuffleClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$songCount songs",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        TextButton(onClick = onShuffleClick) {
            Icon(
                imageVector = Icons.Default.Shuffle,
                contentDescription = null,
                tint = RetroOrange,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "SHUFFLE ALL",
                style = MaterialTheme.typography.labelMedium,
                color = RetroOrange
            )
        }
    }
}

@Composable
private fun EmptyContent(onScanClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📼",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No music found",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap to scan your music library",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        RetroButton(
            text = "Scan Music",
            onClick = onScanClick
        )
    }
}

@Composable
private fun ScanningContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = RetroOrange,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Scanning music...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⚠️",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Error scanning music",
            style = MaterialTheme.typography.titleLarge,
            color = ErrorRed
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        RetroButton(
            text = "Retry",
            onClick = onRetry
        )
    }
}
