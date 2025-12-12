package com.xuyang.ttpage.ui.components.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.xuyang.ttpage.model.data.CommentWithReplies

/**
 * 评论项组件
 * @brief 显示单条评论及其回复
 * @author xuyang
 * @date 2025-12-10
 */
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

