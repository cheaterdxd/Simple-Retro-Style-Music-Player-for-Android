package com.retro.musicplayer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.retro.musicplayer.service.playback.RepeatMode
import com.retro.musicplayer.service.playback.ShuffleMode
import com.retro.musicplayer.ui.components.*
import com.retro.musicplayer.ui.theme.*
import com.retro.musicplayer.ui.viewmodel.PlayerViewModel

/**
 * Player Screen - Màn hình phát nhạc toàn màn hình.
 * 
 * Features:
 * - Album art lớn với cassette style
 * - Controls: Play/Pause, Next, Previous
 * - Progress slider
 * - Shuffle & Repeat buttons  
 * - LED time display
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    onBackClick: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val playbackState by viewModel.playbackState.collectAsState()
    val song = playbackState.currentSong
    
    // Slider state
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isSliding by remember { mutableStateOf(false) }
    
    // Update slider position when not sliding
    LaunchedEffect(playbackState.progress) {
        if (!isSliding) {
            sliderPosition = playbackState.progress
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "NOW PLAYING",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Queue */ }) {
                        Icon(
                            imageVector = Icons.Default.QueueMusic,
                            contentDescription = "Queue",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Album Art with cassette frame
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                AlbumArtLarge(
                    albumArtUri = song?.albumArtUri,
                    modifier = Modifier.fillMaxSize()
                )
                
                // CRT scan lines overlay effect
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ScanlineOverlay)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Song info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = song?.title ?: "No song playing",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = song?.artist ?: "Unknown artist",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Progress slider
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                RetroSlider(
                    value = sliderPosition,
                    onValueChange = { 
                        sliderPosition = it
                        isSliding = true
                    },
                    onValueChangeFinished = {
                        viewModel.seekToFraction(sliderPosition)
                        isSliding = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Time display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = playbackState.getFormattedPosition(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = playbackState.getFormattedDuration(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Main controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shuffle button
                IconButton(
                    onClick = { viewModel.toggleShuffle() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (playbackState.shuffleMode == ShuffleMode.ON) 
                            RetroOrange else TextSecondaryDark,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                // Previous button
                IconButton(
                    onClick = { viewModel.skipToPrevious() },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(36.dp)
                    )
                }
                
                // Play/Pause button
                PlayPauseButton(
                    isPlaying = playbackState.isPlaying,
                    onClick = { viewModel.togglePlayPause() },
                    size = ButtonSize.LARGE
                )
                
                // Next button
                IconButton(
                    onClick = { viewModel.skipToNext() },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(36.dp)
                    )
                }
                
                // Repeat button
                IconButton(
                    onClick = { viewModel.cycleRepeatMode() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = when (playbackState.repeatMode) {
                            RepeatMode.ONE -> Icons.Default.RepeatOne
                            else -> Icons.Default.Repeat
                        },
                        contentDescription = "Repeat",
                        tint = if (playbackState.repeatMode != RepeatMode.OFF) 
                            RetroOrange else TextSecondaryDark,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Queue info
            Text(
                text = "${playbackState.currentIndex + 1} / ${playbackState.queueSize}",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondaryDark
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
