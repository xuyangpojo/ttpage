package com.xuyang.ttpage.ui.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackParameters
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.xuyang.ttpage.util.ResourceHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

/**
 * VideoPlayer 视频播放器组件
 * @author xuyang
 * @date 2025-12-10
 * 1. 视频播放和暂停
 * 2. 进度条控制
 * 3. 显示播放状态
 * 4. 倍速播放 (0.5x, 1x, 1.5x, 2x)
 * 5. 音量控制
 */
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    onPlayerReady: (ExoPlayer) -> Unit = {}
) {
    val context = LocalContext.current
    
    val videoUri = remember(videoUrl) {
        try {
            if (videoUrl.startsWith("http://") || videoUrl.startsWith("https://") || videoUrl.startsWith("android.resource://")) {
                videoUrl
            } else {
                ResourceHelper.getRawResourceUri(context, videoUrl)
            }
        } catch (e: Exception) {
            null
        }
    }
    
    var playerError by remember { mutableStateOf<String?>(null) }
    
    // 验证资源是否存在
    val isValidResource = remember(videoUri) {
        videoUri != null && videoUri.isNotEmpty() && (
            videoUri.startsWith("http://") || 
            videoUri.startsWith("https://") || 
            videoUri.startsWith("android.resource://")
        )
    }
    
    // 使用 DisposableEffect 来管理 ExoPlayer 的生命周期
    val exoPlayer = remember {
        mutableStateOf<ExoPlayer?>(null)
    }
    
    // 初始化ExoPlayer
    LaunchedEffect(videoUri) {
        // 先释放旧的播放器
        exoPlayer.value?.release()
        exoPlayer.value = null
        playerError = null
        
        if (videoUri == null || videoUri.isEmpty()) {
            playerError = "视频资源不存在: $videoUrl"
            return@LaunchedEffect
        }
        
        if (!isValidResource) {
            playerError = "视频资源格式无效: $videoUri"
            return@LaunchedEffect
        }
        
        try {
            // 先验证 URI 是否有效
            val uri = android.net.Uri.parse(videoUri)
            if (uri == null) {
                throw IllegalArgumentException("无效的URI: $videoUri")
            }
            
            val player = ExoPlayer.Builder(context).build()
            
            try {
                val mediaItem = MediaItem.fromUri(uri)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.playWhenReady = true  // 自动播放
                player.repeatMode = Player.REPEAT_MODE_OFF
                
                // 添加错误监听
                player.addListener(object : Player.Listener {
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        playerError = "播放错误: ${error.message}"
                    }
                })
                
                exoPlayer.value = player
                onPlayerReady(player)
            } catch (e: Exception) {
                try {
                    player.release()
                } catch (releaseError: Exception) {
                    // 忽略释放错误
                }
                throw e
            }
        } catch (e: Exception) {
            playerError = "视频加载失败: ${e.message}"
        }
    }
    
    // 确保在组件销毁时释放播放器
    DisposableEffect(Unit) {
        onDispose {
            try {
                exoPlayer.value?.release()
                exoPlayer.value = null
            } catch (e: Exception) {
                // 忽略释放错误
            }
        }
    }
    
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(0L) }
    var playbackSpeed by remember { mutableStateOf(1f) }
    var showSpeedMenu by remember { mutableStateOf(false) }
    
    // 更新播放状态
    LaunchedEffect(exoPlayer.value) {
        val player = exoPlayer.value ?: return@LaunchedEffect
        while (player == exoPlayer.value) {
            try {
                isPlaying = player.isPlaying
                currentPosition = player.currentPosition
                duration = if (player.duration > 0) player.duration else 0L
                playbackSpeed = player.playbackParameters.speed
            } catch (e: Exception) {
                playerError = "播放器错误: ${e.message}"
                break
            }
            delay(100)
        }
    }
    
    // 如果资源无效或加载失败，显示错误信息
    if (!isValidResource || playerError != null || exoPlayer.value == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = playerError ?: "视频资源不存在",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }
    
    val currentPlayer = exoPlayer.value ?: return
    
    var showPlayButton by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = currentPlayer
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    useController = false
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    // 点击屏幕切换播放/暂停
                    if (currentPlayer.isPlaying) {
                        currentPlayer.pause()
                        showPlayButton = true  // 暂停时显示按钮
                    } else {
                        currentPlayer.play()
                        // 播放时显示按钮，然后2秒后自动隐藏
                        showPlayButton = true
                        coroutineScope.launch {
                            delay(2000)
                            if (currentPlayer.isPlaying) {
                                showPlayButton = false
                            }
                        }
                    }
                }
        )
        
        // 屏幕中央的播放/暂停按钮（暂停时始终显示，播放时点击后显示2秒）
        if (showPlayButton || !isPlaying) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        if (currentPlayer.isPlaying) {
                            currentPlayer.pause()
                        } else {
                            currentPlayer.play()
                        }
                    },
                    modifier = Modifier.size(80.dp)
                ) {
                    if (isPlaying) {
                        // 暂停图标：两个竖条
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp, 32.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        shape = MaterialTheme.shapes.small
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp, 32.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        shape = MaterialTheme.shapes.small
                                    )
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "播放",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }
        
        // 底部进度条和控制栏 - 现代化样式（更薄）
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
                .padding(horizontal = 12.dp, vertical = 2.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 进度条
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = formatTime(currentPosition),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(45.dp)
                )
                
                Slider(
                    value = if (duration > 0) currentPosition.toFloat() / duration else 0f,
                    onValueChange = { progress ->
                        val newPosition = (progress * duration).toLong()
                        currentPlayer.seekTo(newPosition)
                    },
                    modifier = Modifier
                        .weight(0.3f)
                        .height(6.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                )
                
                Text(
                    text = formatTime(duration),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(45.dp)
                )
                
                // 倍速控制 - 放在右边
                Box {
                    TextButton(
                        onClick = { showSpeedMenu = !showSpeedMenu },
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Text(
                            text = "${playbackSpeed}x",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    DropdownMenu(
                        expanded = showSpeedMenu,
                        onDismissRequest = { showSpeedMenu = false }
                    ) {
                        listOf(0.5f, 1f, 1.5f, 2f).forEach { speed ->
                            DropdownMenuItem(
                                text = { Text("${speed}x") },
                                onClick = {
                                    currentPlayer.playbackParameters = currentPlayer.playbackParameters.withSpeed(speed)
                                    playbackSpeed = speed
                                    showSpeedMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

