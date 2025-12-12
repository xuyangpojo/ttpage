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
 * @brief 视频播放的基本功能
 * @author xuyang
 * @date 2025-12-10
 */
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    onPlayerReady: (ExoPlayer) -> Unit = {},
    onPlaybackEnded: () -> Unit = {}
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
    val isValidResource = remember(videoUri) {
        videoUri != null && videoUri.isNotEmpty() && (
            videoUri.startsWith("http://") || 
            videoUri.startsWith("https://") || 
            videoUri.startsWith("android.resource://")
        )
    }
    val exoPlayer = remember {
        mutableStateOf<ExoPlayer?>(null)
    }
    LaunchedEffect(videoUri) {
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
            val uri = android.net.Uri.parse(videoUri)
            if (uri == null) {
                throw IllegalArgumentException("无效的URI: $videoUri")
            }
            
            val player = ExoPlayer.Builder(context).build()
            
            try {
                val mediaItem = MediaItem.fromUri(uri)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.playWhenReady = isVisible
                player.repeatMode = Player.REPEAT_MODE_OFF
                player.addListener(object : Player.Listener {
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        playerError = "播放错误: ${error.message}"
                    }
                    
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            onPlaybackEnded()
                        }
                    }
                })
                
                exoPlayer.value = player
                onPlayerReady(player)
            } catch (e: Exception) {
                try {
                    player.release()
                } catch (releaseError: Exception) {
                }
                throw e
            }
        } catch (e: Exception) {
            playerError = "视频加载失败: ${e.message}"
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            try {
                exoPlayer.value?.release()
                exoPlayer.value = null
            } catch (e: Exception) {
            }
        }
    }
    
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(0L) }
    var playbackSpeed by remember { mutableStateOf(1f) }
    var showSpeedMenu by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible, exoPlayer.value) {
        val player = exoPlayer.value ?: return@LaunchedEffect
        if (isVisible) {
            if (!player.isPlaying) {
                player.play()
            }
        } else {
            if (player.isPlaying) {
                player.pause()
            }
        }
    }
    
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
                    if (currentPlayer.isPlaying) {
                        currentPlayer.pause()
                        showPlayButton = true
                    } else {
                        currentPlayer.play()
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

