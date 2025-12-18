package com.retro.musicplayer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.retro.musicplayer.data.model.Song
import com.retro.musicplayer.service.playback.PlaybackState
import com.retro.musicplayer.ui.theme.*

/**
 * Mini player bar hiển thị ở bottom của Home screen.
 * 
 * Shows: album art, song info, play/pause, và next buttons.
 */
@Composable
fun MiniPlayer(
    playbackState: PlaybackState,
    onPlayerClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val song = playbackState.currentSong ?: return
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        color = CassetteBlack,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onPlayerClick)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album art
            AlbumArtSmall(
                albumArtUri = song.albumArtUri,
                modifier = Modifier.size(56.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Song info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = WarmCream,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Progress bar
                LinearProgressIndicator(
                    progress = { playbackState.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(2.dp)
                        .clip(RoundedCornerShape(1.dp)),
                    color = RetroOrange,
                    trackColor = VintageBrownDark.copy(alpha = 0.3f),
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Play/Pause button
            PlayPauseButton(
                isPlaying = playbackState.isPlaying,
                onClick = onPlayPauseClick,
                size = ButtonSize.SMALL
            )
            
            Spacer(modifier = Modifier.width(4.dp))
            
            // Next button
            IconButton(onClick = onNextClick) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next",
                    tint = WarmCream
                )
            }
        }
    }
}

/**
 * Progress slider với style retro.
 */
@Composable
fun RetroSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        modifier = modifier,
        colors = SliderDefaults.colors(
            thumbColor = RetroOrange,
            activeTrackColor = RetroOrange,
            inactiveTrackColor = VintageBrownDark.copy(alpha = 0.3f)
        )
    )
}

/**
 * Time display với LED style.
 */
@Composable
fun LedTimeDisplay(
    currentTime: String,
    totalTime: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = currentTime,
            style = MaterialTheme.typography.displaySmall,
            color = PixelGreen
        )
        Text(
            text = totalTime,
            style = MaterialTheme.typography.displaySmall,
            color = PixelGreen.copy(alpha = 0.5f)
        )
    }
}
