package com.retro.musicplayer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.retro.musicplayer.ui.theme.*

/**
 * Retro-styled button với hiệu ứng cassette.
 * 
 * Có 2 variants: Primary (orange) và Secondary (brown).
 */
@Composable
fun RetroButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isPrimary: Boolean = true
) {
    val backgroundColor = if (isPrimary) RetroOrange else VintageBrown
    val contentColor = WarmCream
    val disabledColor = VintageBrownDark.copy(alpha = 0.5f)
    
    val shape = RoundedCornerShape(8.dp)
    
    Box(
        modifier = modifier
            .shadow(
                elevation = if (enabled) 4.dp else 0.dp,
                shape = shape,
                ambientColor = backgroundColor.copy(alpha = 0.3f)
            )
            .clip(shape)
            .background(if (enabled) backgroundColor else disabledColor)
            .border(
                width = 2.dp,
                color = if (enabled) backgroundColor.copy(alpha = 0.8f) else disabledColor,
                shape = shape
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = if (enabled) contentColor else contentColor.copy(alpha = 0.5f)
        )
    }
}

/**
 * Icon button với style retro.
 */
@Composable
fun RetroIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isActive: Boolean = false,
    content: @Composable () -> Unit
) {
    val backgroundColor = when {
        isActive -> RetroOrange
        else -> Color.Transparent
    }
    
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * Player control button (Play/Pause).
 */
@Composable
fun PlayPauseButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.LARGE
) {
    val buttonSize = when (size) {
        ButtonSize.SMALL -> 48.dp
        ButtonSize.MEDIUM -> 64.dp
        ButtonSize.LARGE -> 80.dp
    }
    
    Box(
        modifier = modifier
            .size(buttonSize)
            .shadow(8.dp, RoundedCornerShape(buttonSize / 2))
            .clip(RoundedCornerShape(buttonSize / 2))
            .background(RetroOrange)
            .border(
                width = 3.dp,
                color = SoftAmber,
                shape = RoundedCornerShape(buttonSize / 2)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isPlaying) "❚❚" else "▶",
            style = when (size) {
                ButtonSize.SMALL -> MaterialTheme.typography.titleMedium
                ButtonSize.MEDIUM -> MaterialTheme.typography.titleLarge
                ButtonSize.LARGE -> MaterialTheme.typography.headlineMedium
            },
            color = CassetteBlack
        )
    }
}

enum class ButtonSize {
    SMALL, MEDIUM, LARGE
}
