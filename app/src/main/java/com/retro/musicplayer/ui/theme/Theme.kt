package com.retro.musicplayer.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Light color scheme với warm, modern palette.
 * Inspired by coral, cream, and dusty blue tones.
 */
private val LightColorScheme = lightColorScheme(
    // Primary - Coral/Salmon
    primary = CoralPrimary,
    onPrimary = Color.White,
    primaryContainer = SoftPink,
    onPrimaryContainer = NavyBlue,
    
    // Secondary - Dusty Blue
    secondary = DustyBlue,
    onSecondary = Color.White,
    secondaryContainer = DustyBlue.copy(alpha = 0.2f),
    onSecondaryContainer = NavyBlue,
    
    // Tertiary - Warm Yellow
    tertiary = WarmYellow,
    onTertiary = NavyBlue,
    tertiaryContainer = WarmYellow.copy(alpha = 0.3f),
    onTertiaryContainer = NavyBlue,
    
    // Background - Warm Cream
    background = WarmCream,
    onBackground = TextPrimaryLight,
    
    // Surface
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    
    // Error
    error = ErrorRed,
    onError = Color.White,
    
    // Outline
    outline = DustyBlue.copy(alpha = 0.5f),
    outlineVariant = SoftBeige
)

/**
 * Dark color scheme - giữ lại cho option.
 */
private val DarkColorScheme = darkColorScheme(
    // Primary
    primary = CoralPrimary,
    onPrimary = Color.White,
    primaryContainer = CoralDark,
    onPrimaryContainer = WarmCream,
    
    // Secondary
    secondary = DustyBlue,
    onSecondary = CassetteBlack,
    secondaryContainer = DustyBlueDark,
    onSecondaryContainer = WarmCream,
    
    // Tertiary
    tertiary = WarmYellow,
    onTertiary = CassetteBlack,
    tertiaryContainer = WarmYellowDark,
    onTertiaryContainer = CassetteBlack,
    
    // Background
    background = CassetteDark,
    onBackground = TextPrimaryDark,
    
    // Surface
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    
    // Error
    error = ErrorRed,
    onError = WarmCream,
    
    // Outline
    outline = DustyBlue,
    outlineVariant = CassetteBlack
)

/**
 * Retro Music Player Theme.
 * 
 * Áp dụng modern warm color scheme.
 * Mặc định sử dụng light theme với coral/cream tones.
 */
@Composable
fun RetroMusicPlayerTheme(
    darkTheme: Boolean = false, // Mặc định light theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) DarkColorScheme else LightColorScheme
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set status bar to match theme
            val statusBarColor = if (darkTheme) CassetteBlack else WarmCream
            window.statusBarColor = statusBarColor.toArgb()
            window.navigationBarColor = statusBarColor.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = RetroTypography,
        content = content
    )
}
