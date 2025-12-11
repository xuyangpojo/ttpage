package com.xuyang.ttpage.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import com.xuyang.ttpage.model.data.Comment
import com.xuyang.ttpage.model.data.CommentWithReplies
import com.xuyang.ttpage.viewmodel.CommentViewModel

/**
 * CommentSection 评论区组件
 * @brief .
 * @author xuyang
 * @date 2025-12-10
 */
@Composable
fun CommentSection(
    videoId: String,
    viewModel: CommentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    modifier: Modifier = Modifier
) {
    val commentsWithReplies by viewModel.commentsWithReplies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val likedCommentIds by viewModel.likedCommentIds.collectAsState()
    var replyingToCommentId by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(videoId) {
        viewModel.loadComments(videoId)
    }
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "评论区 (${commentsWithReplies.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        if (isLoading && commentsWithReplies.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            // 注意：不能使用 LazyColumn，因为 CommentSection 在 VerticalPager 中
            // 使用普通的 Column 来显示评论列表
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                commentsWithReplies.forEach { commentWithReplies ->
                    val commentId = commentWithReplies.comment.id.toString()
                    CommentItem(
                        commentWithReplies = commentWithReplies,
                        isLiked = likedCommentIds.contains(commentId),
                        likedReplyIds = likedCommentIds,
                        isReplying = replyingToCommentId == commentId,
                        onLikeClick = {
                            if (likedCommentIds.contains(commentId)) {
                                viewModel.unlikeComment(commentId)
                            } else {
                                viewModel.likeComment(commentId)
                            }
                        },
                        onReplyLikeClick = { replyId ->
                            if (likedCommentIds.contains(replyId)) {
                                viewModel.unlikeComment(replyId)
                            } else {
                                viewModel.likeComment(replyId)
                            }
                        },
                        onReplyClick = { parentCommentId ->
                            replyingToCommentId = if (replyingToCommentId == parentCommentId) null else parentCommentId
                        },
                        onAddReply = { parentCommentId, replyContent ->
                            viewModel.addComment(commentWithReplies.comment.videoId.toString(), replyContent, parentCommentId)
                            replyingToCommentId = null
                        },
                        onCancelReply = {
                            replyingToCommentId = null
                        }
                    )
                }
            }
        }
        
        CommentInput(
            onSendClick = { content ->
                if (content.isNotBlank()) {
                    viewModel.addComment(videoId, content)
                }
            }
        )
    }
}

@Composable
fun CommentItem(
    commentWithReplies: CommentWithReplies,
    isLiked: Boolean,
    likedReplyIds: Set<String>,
    isReplying: Boolean,
    onLikeClick: () -> Unit,
    onReplyLikeClick: (String) -> Unit,
    onReplyClick: (String) -> Unit,
    onAddReply: (String, String) -> Unit,
    onCancelReply: () -> Unit,
    modifier: Modifier = Modifier
) {
    val comment = commentWithReplies.comment
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 用户头像
                    UserAvatar(
                        authorId = comment.authorId,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = comment.authorName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = comment.publishTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { onLikeClick() }
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "点赞",
                        modifier = Modifier.size(18.dp),
                        tint = if (isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${comment.likeCount}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { onReplyClick(comment.id.toString()) }
                ) {
                    Text(
                        text = if (comment.replyCount > 0u) "${comment.replyCount.toInt()}条回复" else "回复",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            if (commentWithReplies.replies.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                commentWithReplies.replies.forEach { replyWithReplies ->
                    ReplyItem(
                        reply = replyWithReplies.comment,
                        isLiked = likedReplyIds.contains(replyWithReplies.comment.id.toString()),
                        onLikeClick = { onReplyLikeClick(replyWithReplies.comment.id.toString()) },
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            
            if (isReplying) {
                Spacer(modifier = Modifier.height(8.dp))
                ReplyInput(
                    onSendClick = { replyContent ->
                        if (replyContent.isNotBlank()) {
                            onAddReply(comment.id.toString(), replyContent)
                        }
                    },
                    onCancelClick = onCancelReply
                )
            }
        }
    }
}

@Composable
fun ReplyItem(
    reply: Comment,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 用户头像
                    UserAvatar(
                        authorId = reply.authorId,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = reply.authorName,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = reply.publishTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = reply.content,
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.clickable { onLikeClick() }
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "点赞",
                    modifier = Modifier.size(16.dp),
                    tint = if (isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${reply.likeCount.toInt()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun CommentInput(
    onSendClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var commentText by remember { mutableStateOf("") }
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = commentText,
            onValueChange = { commentText = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("写下你的评论...") },
            singleLine = false,
            maxLines = 3
        )
        IconButton(
            onClick = {
                if (commentText.isNotBlank()) {
                    onSendClick(commentText)
                    commentText = ""
                }
            },
            enabled = commentText.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "发送"
            )
        }
    }
}

@Composable
fun ReplyInput(
    onSendClick: (String) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var replyText by remember { mutableStateOf("") }
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = replyText,
            onValueChange = { replyText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("写下你的回复...") },
            singleLine = false,
            maxLines = 3
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancelClick) {
                Text("取消")
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (replyText.isNotBlank()) {
                        onSendClick(replyText)
                        replyText = ""
                    }
                },
                enabled = replyText.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "发送"
                )
            }
        }
    }
}

/**
 * 用户头像组件
 * 
 * 头像文件放置位置：
 * - 路径：app/src/main/res/drawable/
 * - 命名规则：avatar_{authorId}.png
 *   例如：authorId为"u101"的用户，头像文件名为"avatar_u101.png"
 * 
 * 使用说明：
 * 1. 将用户头像PNG文件放在 app/src/main/res/drawable/ 目录下
 * 2. 文件名必须遵循命名规则：avatar_{authorId}.png
 * 3. 文件名必须是小写字母、数字和下划线，不能包含空格和特殊字符
 * 4. 如果找不到对应的头像文件，将显示默认头像（Person图标）
 * 
 * 示例：
 * - 用户ID: u101 -> 头像文件: avatar_u101.png
 * - 用户ID: u102 -> 头像文件: avatar_u102.png
 */
@Composable
fun UserAvatar(
    authorId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // 尝试根据authorId查找头像资源
    val avatarResourceId = remember(authorId) {
        try {
            val resourceName = "avatar_$authorId"
            context.resources.getIdentifier(
                resourceName,
                "drawable",
                context.packageName
            )
        } catch (e: Exception) {
            0
        }
    }
    
    // 如果找到头像资源，显示头像图片；否则显示默认头像
    if (avatarResourceId != 0) {
        Image(
            painter = painterResource(id = avatarResourceId),
            contentDescription = "用户头像",
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop
        )
    } else {
        // 默认头像：使用Person图标
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "默认头像",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

