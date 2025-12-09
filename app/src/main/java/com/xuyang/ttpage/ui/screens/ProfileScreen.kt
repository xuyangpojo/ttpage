package com.xuyang.ttpage.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.xuyang.ttpage.model.data.Video
import com.xuyang.ttpage.ui.screens.HomeScreen.VideoCard
import com.xuyang.ttpage.viewmodel.FavoriteViewModel
import com.xuyang.ttpage.viewmodel.HomeViewModel
import com.xuyang.ttpage.viewmodel.UserViewModel

/**
 * View层：我的页面
 * 
 * 功能：
 * 1. 显示用户信息
 * 2. 登录/登出功能
 * 3. 用户设置
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    favoriteViewModel: FavoriteViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onVideoClick: (Video) -> Unit = {}
) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val isLoggedIn by userViewModel.isLoggedIn.collectAsState()
    
    val videos = homeViewModel.videos.collectAsState().value
    val favorites by favoriteViewModel.favorites.collectAsState()
    val history by favoriteViewModel.history.collectAsState()
    
    // 更新收藏和历史记录列表
    LaunchedEffect(videos) {
        favoriteViewModel.updateFavoritesFromVideos(videos)
        favoriteViewModel.updateHistoryFromVideos(videos)
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 用户信息区域
        if (isLoggedIn && currentUser != null) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 头像占位
                        Surface(
                            modifier = Modifier.size(64.dp),
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "头像",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        
                        Column {
                            Text(
                                text = currentUser!!.username,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentUser!!.email,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    if (!currentUser!!.bio.isNullOrBlank()) {
                        Text(
                            text = currentUser!!.bio!!,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            // 登出按钮
            Button(
                onClick = { userViewModel.logout() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("登出")
            }
        } else {
            // 未登录状态
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "未登录",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "请登录以查看个人信息",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onLoginClick,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("登录")
                        }
                        OutlinedButton(
                            onClick = onRegisterClick,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("注册")
                        }
                    }
                }
            }
        }
        
        // 收藏和历史记录
        if (isLoggedIn) {
            // 我的收藏
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ListItem(
                        headlineContent = { 
                            Text(
                                "我的收藏 (${favorites.size})",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                    Divider()
                    if (favorites.isEmpty()) {
                        ListItem(
                            headlineContent = { 
                                Text(
                                    "暂无收藏",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.height((favorites.size * 120).coerceAtMost(400).dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(favorites) { video ->
                                VideoCard(
                                    video = video,
                                    onClick = { onVideoClick(video) }
                                )
                            }
                        }
                    }
                }
            }
            
            // 浏览历史
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ListItem(
                            headlineContent = { 
                                Text(
                                    "浏览历史 (${history.size})",
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        if (history.isNotEmpty()) {
                            TextButton(
                                onClick = { favoriteViewModel.clearHistory() }
                            ) {
                                Text("清空", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                    Divider()
                    if (history.isEmpty()) {
                        ListItem(
                            headlineContent = { 
                                Text(
                                    "暂无浏览历史",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.height((history.size * 120).coerceAtMost(400).dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(history) { video ->
                                VideoCard(
                                    video = video,
                                    onClick = { onVideoClick(video) }
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // 设置选项
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                ListItem(
                    headlineContent = { Text("设置") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null
                        )
                    }
                )
                Divider()
                ListItem(
                    headlineContent = { Text("关于") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}

