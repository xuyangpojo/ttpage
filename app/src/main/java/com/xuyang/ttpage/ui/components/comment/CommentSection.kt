package com.xuyang.ttpage.ui.components.comment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

