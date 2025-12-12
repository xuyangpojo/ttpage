package com.xuyang.ttpage.ui.components.comment

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 评论输入组件
 * @brief 用于输入新评论
 * @author xuyang
 * @date 2025-12-10
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

/**
 * 回复输入组件
 * @brief 用于输入回复
 * @author xuyang
 * @date 2025-12-10
 */
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

