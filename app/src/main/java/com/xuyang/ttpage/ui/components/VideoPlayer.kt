package com.xuyang.ttpage.ui.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
 * 视频播放器组件
 * 
 * 功能：
 * 1. 视频播放和暂停
 * 2. 进度条控制
 * 3. 显示播放状态
 * 4. 倍速播放（0.5x, 1x, 1.5x, 2x）
 * 5. 音量控制
 */
@Composable
fun VideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    onPlayerReady: (ExoPlayer) -> Unit = {}
) {
    val context = LocalContext.current
    
    // 将资源名称转换为URI（如果是本地资源）
    val videoUri = remember(videoUrl) {
        if (videoUrl.startsWith("http://") || videoUrl.startsWith("https://") || videoUrl.startsWith("android.resource://")) {
            // 已经是完整的URI
            videoUrl
        } else {
            // 本地资源名称，转换为android.resource://格式
            ResourceHelper.getRawResourceUri(context, videoUrl)
        }
    }
    
    // 创建ExoPlayer实例
    val exoPlayer = remember(videoUri) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = false // 默认不自动播放
            repeatMode = Player.REPEAT_MODE_OFF
        }
    }
    
    // 播放状态
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(0L) }
    var playbackSpeed by remember { mutableStateOf(1f) }
    var showSpeedMenu by remember { mutableStateOf(false) }
    var showVolumeControl by remember { mutableStateOf(false) }
    
    // 音量控制
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    var currentVolume by remember { mutableIntStateOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)) }
    val maxVolume = remember { audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) }
    
    // 监听播放状态
    LaunchedEffect(exoPlayer, videoUri) {
        onPlayerReady(exoPlayer)
        
        // 更新播放状态
        while (true) {
            isPlaying = exoPlayer.isPlaying
            currentPosition = exoPlayer.currentPosition
            duration = if (exoPlayer.duration > 0) exoPlayer.duration else 0L
            playbackSpeed = exoPlayer.playbackParameters.speed
            delay(100) // 每100ms更新一次
        }
    }
    
    // 清理资源
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    
    Box(modifier = modifier) {
        // ExoPlayer视图
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    useController = false // 使用自定义控制器
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // 自定义控制栏
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 进度条和控制按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 播放/暂停按钮
                IconButton(
                    onClick = {
                        if (exoPlayer.isPlaying) {
                            exoPlayer.pause()
                        } else {
                            exoPlayer.play()
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "暂停" else "播放"
                    )
                }
                
                // 时间显示
                Text(
                    text = formatTime(currentPosition),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(50.dp)
                )
                
                // 进度条
                Slider(
                    value = if (duration > 0) currentPosition.toFloat() / duration else 0f,
                    onValueChange = { progress ->
                        val newPosition = (progress * duration).toLong()
                        exoPlayer.seekTo(newPosition)
                    },
                    modifier = Modifier.weight(1f)
                )
                
                // 总时长
                Text(
                    text = formatTime(duration),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(50.dp)
                )
            }
            
            // 倍速和音量控制行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 倍速播放按钮
                Box {
                    IconButton(
                        onClick = { showSpeedMenu = !showSpeedMenu }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "倍速"
                        )
                    }
                    Text(
                        text = "${playbackSpeed}x",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    
                    // 倍速菜单
                    DropdownMenu(
                        expanded = showSpeedMenu,
                        onDismissRequest = { showSpeedMenu = false }
                    ) {
                        listOf(0.5f, 1f, 1.5f, 2f).forEach { speed ->
                            DropdownMenuItem(
                                text = { Text("${speed}x") },
                                onClick = {
                                    exoPlayer.playbackParameters = exoPlayer.playbackParameters.withSpeed(speed)
                                    playbackSpeed = speed
                                    showSpeedMenu = false
                                }
                            )
                        }
                    }
                }
                
                // 音量控制按钮
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { showVolumeControl = !showVolumeControl }
                    ) {
                        Icon(
                            imageVector = if (currentVolume > 0) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = "音量"
                        )
                    }
                    
                    // 音量滑块（当显示时）
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

/**
 * 格式化时间（毫秒转 mm:ss）
 */
private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

