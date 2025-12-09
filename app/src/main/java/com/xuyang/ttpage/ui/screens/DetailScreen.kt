package com.xuyang.ttpage.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xuyang.ttpage.model.data.Content

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
    modifier: Modifier = Modifier
) {
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
            
            // 点赞数
            Text(
                text = "点赞数",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${content.likeCount}",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Divider()
            
            // 评论数
            Text(
                text = "评论数",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${content.commentCount}",
                style = MaterialTheme.typography.bodyLarge
            )
            
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
                            isHot = ${content.isHot}
                        )
                    """.trimIndent(),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

