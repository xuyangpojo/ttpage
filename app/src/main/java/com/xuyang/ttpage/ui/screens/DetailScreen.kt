package com.xuyang.ttpage.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xuyang.ttpage.model.data.Content
import com.xuyang.ttpage.ui.components.CommentSection
import com.xuyang.ttpage.ui.components.VideoPlayer
import com.xuyang.ttpage.viewmodel.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * View层：内容详情页
 * 
 * 功能：
 * 1. 显示内容的完整信息（实体字符串信息）
 * 2. 支持滑动返回（通过Navigation的返回功能）
 */
@Composable
fun DetailScreen(
    content: Content,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableIntStateOf(content.likeCount) }
    
    // 复制链接到剪贴板
    fun copyLinkToClipboard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val link = "https://ttpage.example.com/content/${content.id}"
        val clip = ClipData.newPlainText("内容链接", link)
        clipboard.setPrimaryClip(clip)
    }
    
    // 点赞内容
    fun toggleLike() {
        isLiked = !isLiked
        likeCount = if (isLiked) likeCount + 1 else maxOf(0, likeCount - 1)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("内容详情") },
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                text = content.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            // 视频播放器（如果有视频）
            if (content.hasVideo && !content.videoUrl.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                VideoPlayer(
                    videoUrl = content.videoUrl!!,
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
                text = content.id,
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
                text = content.author,
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
                text = content.publishTime,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Divider()
            
            // 操作按钮（点赞、转发）
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                        style = MaterialTheme.typography.bodyLarge
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
                        style = MaterialTheme.typography.bodyLarge
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
                    text = "评论数: ${content.commentCount}",
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
                color = if (content.isHot) 
                    MaterialTheme.colorScheme.errorContainer 
                else 
                    MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = if (content.isHot) "是" else "否",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (content.isHot) 
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
                        Content(
                            id = "${content.id}",
                            title = "${content.title}",
                            author = "${content.author}",
                            publishTime = "${content.publishTime}",
                            likeCount = ${content.likeCount},
                            commentCount = ${content.commentCount},
                            isHot = ${content.isHot},
                            videoCover = ${content.videoCover?.let { "\"$it\"" } ?: "null"},
                            videoUrl = ${content.videoUrl?.let { "\"$it\"" } ?: "null"}
                        )
                    """.trimIndent(),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 20.sp
                )
            }
            
            Divider()
            
            // 评论区
            CommentSection(contentId = content.id)
        }
    }
}

