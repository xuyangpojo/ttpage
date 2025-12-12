package com.xuyang.ttpage.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val TikTokDarkColorScheme = darkColorScheme(
    primary = TikTokPrimary,
    onPrimary = Color.Black,
    primaryContainer = TikTokSecondary,
    onPrimaryContainer = TikTokOnBackground,
    
    secondary = TikTokSecondary,
    onSecondary = Color.Black,
    secondaryContainer = TikTokSurfaceVariant,
    onSecondaryContainer = TikTokOnSurface,
    
    tertiary = TikTokTertiary,
    onTertiary = Color.White,
    tertiaryContainer = TikTokSurfaceVariant,
    onTertiaryContainer = TikTokOnSurface,
    
    background = TikTokBackground,
    onBackground = TikTokOnBackground,
    
    surface = TikTokSurface,
    onSurface = TikTokOnSurface,
    
    surfaceVariant = TikTokSurfaceVariant,
    onSurfaceVariant = TikTokOnSurfaceVariant,
    
    error = TikTokTertiary,
    onError = Color.White,
    errorContainer = TikTokSurfaceVariant,
    onErrorContainer = TikTokOnSurface,
    
    outline = TikTokOnSurfaceVariant,
    outlineVariant = TikTokSurfaceVariant,
    
    scrim = Color.Black.copy(alpha = 0.5f),
    inverseSurface = TikTokOnBackground,
    inverseOnSurface = TikTokBackground,
    inversePrimary = TikTokPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = TikTokPrimary,
    secondary = TikTokSecondary,
    tertiary = TikTokTertiary
)

@Composable
fun TTPageTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = TikTokDarkColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}