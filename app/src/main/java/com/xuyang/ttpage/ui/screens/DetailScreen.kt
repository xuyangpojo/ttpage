package com.xuyang.ttpage.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xuyang.ttpage.model.data.Video
import com.xuyang.ttpage.ui.components.VideoPlayer
import com.xuyang.ttpage.ui.components.CommentSection
import com.xuyang.ttpage.viewmodel.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.abs

/**
 * View层：内容详情页
 * 
 * 功能：
 * 1. 显示内容的完整信息（实体字符串信息）
 * 2. 支持滑动返回（通过Navigation的返回功能）
 * 3. 支持上下滑动切换不同内容
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    video: Video,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    if (errorMessage != null) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.errorContainer
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "加载视频详情失败",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "错误: $errorMessage",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBackClick) {
                    Text("返回")
                }
            }
        }
        return
    }
    
    val videos = homeViewModel.videos.collectAsState().value
    
    // 找到当前视频在列表中的索引
    val currentIndex = remember(video.id) {
        videos.indexOfFirst { it.id == video.id }.coerceAtLeast(0)
    }
    
    // 创建垂直Pager状态
    val pagerState = rememberPagerState(
        initialPage = currentIndex,
        pageCount = { videos.size.coerceAtLeast(1) }
    )
    
    // 当视频列表变化时，更新页面数量
    LaunchedEffect(videos.size) {
        val pageCount = videos.size.coerceAtLeast(1)
        if (pagerState.pageCount != pageCount) {
            // 如果当前页面超出范围，跳转到最后一页
            val targetPage = currentIndex.coerceIn(0, videos.size - 1)
            if (targetPage != pagerState.currentPage) {
                pagerState.animateScrollToPage(targetPage)
            }
        }
    }
    
    Scaffold(
    ) { paddingValues ->
        // 使用VerticalPager实现上下切换
        VerticalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            key = { index -> videos.getOrNull(index)?.id ?: index }
        ) { page ->
            val currentVideo = videos.getOrNull(page) ?: video
            
            DetailVideoPage(
                video = currentVideo,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * 详情页视频页面
 */
@Composable
fun DetailVideoPage(
    video: Video,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf(video.likeCount) }
    var showComments by remember { mutableStateOf(false) }
    
    // 复制链接到剪贴板
    fun copyLinkToClipboard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val link = "https://ttpage.example.com/video/${video.id}"
        val clip = ClipData.newPlainText("视频链接", link)
        clipboard.setPrimaryClip(clip)
    }
    
    // 点赞内容
    fun toggleLike() {
        isLiked = !isLiked
        likeCount = if (isLiked) likeCount + 1u else (likeCount - 1u).coerceAtLeast(0u)
    }
    
    // 抖音/TikTok风格：视频全屏，右侧半透明信息栏
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 视频播放器 - 全屏显示
        if (video.hasVideo && !video.videoUrl.isNullOrBlank()) {
            VideoPlayer(
                videoUrl = video.videoUrl!!,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // 没有视频时显示占位符
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "暂无视频",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // 右侧半透明信息栏
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 8.dp, bottom = 76.dp)
                .width(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 点赞按钮和数量
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = { toggleLike() },
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            shape = CircleShape
                        )
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "点赞",
                        tint = if (isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = formatCount(likeCount),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            
            // 评论按钮和数量
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = { showComments = !showComments },
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            shape = CircleShape
                        )
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Comment,
                        contentDescription = "评论",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = formatCount(video.commentCount),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            
            // 转发按钮
            IconButton(
                onClick = { copyLinkToClipboard() },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "转发",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        // 底部作者信息
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 60.dp)
                .fillMaxWidth(0.7f)
        ) {
            Text(
                text = "@${video.authorName}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = video.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        
        // 评论区弹窗
        if (showComments) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                    .clickable { showComments = false }
            ) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .clickable(enabled = false) { },
                    color = MaterialTheme.colorScheme.surface,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // 评论区标题栏
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "评论区",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { showComments = false }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "关闭"
                                )
                            }
                        }
                        
                        // 评论区内容 - 可滚动
                        val scrollState = rememberScrollState()
                        CommentSection(
                            videoId = video.id,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 16.dp)
                                .verticalScroll(scrollState)
                        )
                    }
                }
            }
        }
    }
}

// 格式化数字显示（如：1000 -> 1K, 1000000 -> 1M）
private fun formatCount(count: UInt): String {
    return when {
        count >= 1_000_000u -> "${count / 1_000_000u}M"
        count >= 1_000u -> "${count / 1_000u}K"
        else -> count.toString()
    }
}

