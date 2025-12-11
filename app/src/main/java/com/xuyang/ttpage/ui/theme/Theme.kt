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

// 抖音/TikTok风格的深色配色方案
private val TikTokDarkColorScheme = darkColorScheme(
    primary = TikTokPrimary, // 亮青色
    onPrimary = Color.Black, // 主色上的文字用黑色
    primaryContainer = TikTokSecondary, // 主色容器
    onPrimaryContainer = TikTokOnBackground, // 主色容器上的文字
    
    secondary = TikTokSecondary, // 次要色
    onSecondary = Color.Black,
    secondaryContainer = TikTokSurfaceVariant,
    onSecondaryContainer = TikTokOnSurface,
    
    tertiary = TikTokTertiary, // 粉红色强调
    onTertiary = Color.White,
    tertiaryContainer = TikTokSurfaceVariant,
    onTertiaryContainer = TikTokOnSurface,
    
    background = TikTokBackground, // 纯黑背景
    onBackground = TikTokOnBackground, // 白色文字
    
    surface = TikTokSurface, // 深灰表面
    onSurface = TikTokOnSurface, // 浅灰文字
    
    surfaceVariant = TikTokSurfaceVariant, // 表面变体
    onSurfaceVariant = TikTokOnSurfaceVariant, // 次要文字
    
    error = TikTokTertiary, // 错误用粉红色
    onError = Color.White,
    errorContainer = TikTokSurfaceVariant,
    onErrorContainer = TikTokOnSurface,
    
    outline = TikTokOnSurfaceVariant, // 边框颜色
    outlineVariant = TikTokSurfaceVariant,
    
    scrim = Color.Black.copy(alpha = 0.5f), // 遮罩层
    inverseSurface = TikTokOnBackground,
    inverseOnSurface = TikTokBackground,
    inversePrimary = TikTokPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun TTPageTheme(
    darkTheme: Boolean = true, // 强制使用深色主题
    // Dynamic color 禁用，使用自定义配色
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // 始终使用深色主题，忽略系统设置
    val colorScheme = TikTokDarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}