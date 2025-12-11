package com.xuyang.ttpage.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
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
import com.xuyang.ttpage.ui.components.CommentSection
import com.xuyang.ttpage.ui.components.VideoPlayer
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
    val context = LocalContext.current
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
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "视频详情 (${pagerState.currentPage + 1}/${videos.size})"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
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
    
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
            // 标题
            Text(
                text = "标题",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = video.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            // 视频播放器（如果有视频）
            if (video.hasVideo && !video.videoUrl.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                VideoPlayer(
                    videoUrl = video.videoUrl!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Divider()
            }
            
            Divider()
            
            // ID
            Text(
                text = "ID",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = video.id,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Divider()
            
            // 作者
            Text(
                text = "作者",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = video.authorName,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Divider()
            
            // 发布时间
            Text(
                text = "发布时间",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = video.publishTime,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Divider()
            
            // 操作按钮（点赞、转发）
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 点赞按钮
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { toggleLike() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "点赞",
                        tint = if (isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$likeCount",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                // 转发按钮（复制链接）
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { copyLinkToClipboard() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "转发"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "转发",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            Divider()
            
            // 点赞数和评论数（显示）
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "点赞数: $likeCount",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "评论数: ${video.commentCount}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Divider()
            
            // 是否热门
            Text(
                text = "是否热门",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Surface(
                color = if (video.isHot) 
                    MaterialTheme.colorScheme.errorContainer 
                else 
                    MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = if (video.isHot) "是" else "否",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (video.isHot) 
                        MaterialTheme.colorScheme.onErrorContainer 
                    else 
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Divider()
            
            // 完整实体信息（字符串形式）
            Text(
                text = "完整实体信息",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = """
                        Video(
                            id = "${video.id}",
                            title = "${video.title}",
                            authorId = "${video.authorId}",
                            authorName = "${video.authorName}",
                            publishTime = "${video.publishTime}",
                            likeCount = ${video.likeCount},
                            commentCount = ${video.commentCount},
                            isHot = ${video.isHot},
                            videoCover = ${video.videoCover?.let { "\"$it\"" } ?: "null"},
                            videoUrl = ${video.videoUrl?.let { "\"$it\"" } ?: "null"},
                            topics = ${video.topics}
                        )
                    """.trimIndent(),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 20.sp
                )
            }
            
            Divider()
            
            // 评论区
            CommentSection(videoId = video.id)
        }
    }

