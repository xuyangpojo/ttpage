package com.xuyang.ttpage.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xuyang.ttpage.model.data.Comment
import com.xuyang.ttpage.viewmodel.CommentViewModel

/**
 * 评论区组件
 */
@Composable
fun CommentSection(
    contentId: String,
    viewModel: CommentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    modifier: Modifier = Modifier
) {
    val comments by viewModel.comments.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val likedCommentIds by viewModel.likedCommentIds.collectAsState()
    
    LaunchedEffect(contentId) {
        viewModel.loadComments(contentId)
    }
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 评论区标题
        Text(
            text = "评论区 (${comments.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        if (isLoading && comments.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            // 评论列表
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(comments) { comment ->
                    CommentItem(
                        comment = comment,
                        isLiked = likedCommentIds.contains(comment.id),
                        likedReplyIds = likedCommentIds,
                        onLikeClick = {
                            if (likedCommentIds.contains(comment.id)) {
                                viewModel.unlikeComment(comment.id)
                            } else {
                                viewModel.likeComment(comment.id)
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
                            // TODO: 显示回复输入框
                        },
                        onAddReply = { parentCommentId, replyContent ->
                            viewModel.addComment(comment.contentId, replyContent, parentCommentId)
                        }
                    )
                }
            }
        }
        
        // 添加评论输入框
        CommentInput(
            onSendClick = { content ->
                if (content.isNotBlank()) {
                    viewModel.addComment(contentId, content)
                }
            }
        )
    }
}

/**
 * 评论项组件
 */
@Composable
fun CommentItem(
    comment: Comment,
    isLiked: Boolean,
    likedReplyIds: Set<String>,
    onLikeClick: () -> Unit,
    onReplyLikeClick: (String) -> Unit,
    onReplyClick: (String) -> Unit,
    onAddReply: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 作者和时间
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.author,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = comment.publishTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // 评论内容
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium
            )
            
            // 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 点赞按钮
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
                
                // 回复按钮
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable { onReplyClick(comment.id) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Reply,
                        contentDescription = "回复",
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (comment.replyCount > 0) "${comment.replyCount}" else "回复",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            // 回复列表
            if (comment.replies.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                comment.replies.forEach { reply ->
                    ReplyItem(
                        reply = reply,
                        isLiked = likedReplyIds.contains(reply.id),
                        onLikeClick = { onReplyLikeClick(reply.id) },
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

/**
 * 回复项组件
 */
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = reply.author,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
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
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "点赞",
                    modifier = Modifier.size(16.dp),
                    tint = if (isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${reply.likeCount}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 评论输入框组件
 */
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

