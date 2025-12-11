package com.xuyang.ttpage.ui.components

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import android.media.AudioManager
import android.content.Context

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
            Log.d("VideoPlayer", "处理视频URL: $videoUrl")
            if (videoUrl.startsWith("http://") || videoUrl.startsWith("https://") || videoUrl.startsWith("android.resource://")) {
                Log.d("VideoPlayer", "使用原始URL: $videoUrl")
                videoUrl
            } else {
                val uri = ResourceHelper.getRawResourceUri(context, videoUrl)
                if (uri != null) {
                    Log.d("VideoPlayer", "找到资源URI: $uri")
                } else {
                    Log.w("VideoPlayer", "未找到资源: $videoUrl")
                }
                uri
            }
        } catch (e: Exception) {
            Log.e("VideoPlayer", "处理视频URL时出错", e)
            null
        }
    }
    
    var playerError by remember { mutableStateOf<String?>(null) }
    var exoPlayer: ExoPlayer? by remember(videoUri) {
        mutableStateOf(null)
    }
    
    // 验证资源是否存在
    val isValidResource = remember(videoUri) {
        val isValid = videoUri != null && videoUri.isNotEmpty() && (
            videoUri.startsWith("http://") || 
            videoUri.startsWith("https://") || 
            videoUri.startsWith("android.resource://")
        )
        Log.d("VideoPlayer", "资源验证结果: isValid=$isValid, videoUri=$videoUri")
        isValid
    }
    
    // 初始化ExoPlayer
    LaunchedEffect(videoUri) {
        if (videoUri == null || videoUri.isEmpty()) {
            val errorMsg = "视频资源不存在: $videoUrl"
            Log.w("VideoPlayer", errorMsg)
            playerError = errorMsg
            return@LaunchedEffect
        }
        
        if (!isValidResource) {
            val errorMsg = "视频资源格式无效: $videoUri"
            Log.w("VideoPlayer", errorMsg)
            playerError = errorMsg
            return@LaunchedEffect
        }
        
        try {
            Log.d("VideoPlayer", "开始初始化ExoPlayer，URI: $videoUri")
            val player = ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.fromUri(videoUri)
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = false
                repeatMode = Player.REPEAT_MODE_OFF
                
                // 添加错误监听
                addListener(object : Player.Listener {
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        Log.e("VideoPlayer", "播放器错误", error)
                        playerError = "播放错误: ${error.message}"
                    }
                })
            }
            exoPlayer = player
            Log.d("VideoPlayer", "ExoPlayer初始化成功")
            onPlayerReady(player)
        } catch (e: Exception) {
            val errorMsg = "视频加载失败: ${e.message}"
            Log.e("VideoPlayer", errorMsg, e)
            playerError = errorMsg
            e.printStackTrace()
        }
    }
    
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(0L) }
    var playbackSpeed by remember { mutableStateOf(1f) }
    var showSpeedMenu by remember { mutableStateOf(false) }
    var showVolumeControl by remember { mutableStateOf(false) }
    
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    var currentVolume by remember { mutableIntStateOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) }
    val maxVolume = remember { audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) }
    
    // 更新播放状态
    LaunchedEffect(exoPlayer) {
        val player = exoPlayer ?: return@LaunchedEffect
        while (player == exoPlayer) {
            try {
                isPlaying = player.isPlaying
                currentPosition = player.currentPosition
                duration = if (player.duration > 0) player.duration else 0L
                playbackSpeed = player.playbackParameters.speed
            } catch (e: Exception) {
                Log.e("VideoPlayer", "更新播放状态时出错", e)
                playerError = "播放器错误: ${e.message}"
                break
            }
            delay(100)
        }
    }
    
    DisposableEffect(exoPlayer) {
        onDispose {
            try {
                Log.d("VideoPlayer", "释放ExoPlayer")
                exoPlayer?.release()
            } catch (e: Exception) {
                Log.e("VideoPlayer", "释放ExoPlayer时出错", e)
            }
        }
    }
    
    // 如果资源无效或加载失败，显示错误信息
    if (!isValidResource || playerError != null || exoPlayer == null) {
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
    
    val currentPlayer = exoPlayer ?: return
    
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
            modifier = Modifier.fillMaxSize()
        )
        
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        if (currentPlayer.isPlaying) {
                            currentPlayer.pause()
                        } else {
                            currentPlayer.play()
                        }
                    }
                ) {
                    Text(
                        text = if (isPlaying) "暂停" else "播放",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Text(
                    text = formatTime(currentPosition),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(50.dp)
                )
                
                Slider(
                    value = if (duration > 0) currentPosition.toFloat() / duration else 0f,
                    onValueChange = { progress ->
                        val newPosition = (progress * duration).toLong()
                        currentPlayer.seekTo(newPosition)
                    },
                    modifier = Modifier.weight(1f)
                )
                
                Text(
                    text = formatTime(duration),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(50.dp)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    IconButton(
                        onClick = { showSpeedMenu = !showSpeedMenu }
                    ) {
                    Text(
                        text = "${playbackSpeed}x",
                        style = MaterialTheme.typography.labelSmall
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { showVolumeControl = !showVolumeControl }
                    ) {
                        Text(
                            text = if (currentVolume > 0) "音量" else "静音",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    if (showVolumeControl) {
                        Slider(
                            value = currentVolume.toFloat() / maxVolume,
                            onValueChange = { volume ->
                                val newVolume = (volume * maxVolume).toInt()
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
                                currentVolume = newVolume
                            },
                            modifier = Modifier.width(100.dp)
                        )
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

