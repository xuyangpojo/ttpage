package com.xuyang.ttpage.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Comment
import androidx.compose.ui.graphics.Color
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.xuyang.ttpage.model.data.Video
import com.xuyang.ttpage.model.data.Topic
import com.xuyang.ttpage.util.ResourceHelper
import com.xuyang.ttpage.viewmodel.HomeViewModel
import com.xuyang.ttpage.viewmodel.TopicViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * View层：首页双列推荐页面
 * 
 * 功能：
 * 1. 屏幕分成左右两部分
 * 2. 从上到下显示内容卡片
 * 3. 显示内容的所有信息（作者、发布时间、点赞数、评论数、是否热门、标题）
 * 4. 点击卡片跳转到详情页
 * 5. 顶部话题切换（左右滑动）
 * 6. 下拉刷新和上拉加载更多
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    topicViewModel: TopicViewModel = viewModel(),
    onVideoClick: (Video) -> Unit = {},
    onRefreshRequest: () -> Unit = { viewModel.refreshVideos() },
    scrollToTopTrigger: Int = 0
) {
    // 观察ViewModel的状态
    val videosByTopic by viewModel.videosByTopic.collectAsState()
    val loadingByTopic by viewModel.loadingByTopic.collectAsState()
    val hasMoreByTopic by viewModel.hasMoreByTopic.collectAsState()
    val currentTopicId by viewModel.currentTopicId.collectAsState()
    
    val topics by topicViewModel.topics.collectAsState()
    val selectedTopicId by topicViewModel.selectedTopicId.collectAsState()
    
    // 找到当前选中topic的索引
    val selectedTopicIndex = remember(selectedTopicId, topics) {
        topics.indexOfFirst { it.id == selectedTopicId }.coerceAtLeast(0)
    }
    
    // 创建HorizontalPager状态，用于左右滑动切换topic
    val pagerState = rememberPagerState(
        initialPage = selectedTopicIndex,
        pageCount = { topics.size.coerceAtLeast(1) }
    )
    
    // 当HorizontalPager页面变化时，切换topic
    LaunchedEffect(pagerState.currentPage) {
        val currentPage = pagerState.currentPage
        if (currentPage < topics.size) {
            val topicId = topics[currentPage].id
            if (topicId != selectedTopicId) {
                topicViewModel.selectTopic(topicId)
            }
        }
    }
    
    // 当选中topic变化时，同步HorizontalPager页面
    LaunchedEffect(selectedTopicId) {
        val index = topics.indexOfFirst { it.id == selectedTopicId }
        if (index >= 0 && index != pagerState.currentPage) {
            pagerState.animateScrollToPage(index)
        }
    }
    
    // 话题切换时，如果缓存中没有数据则加载
    LaunchedEffect(selectedTopicId) {
        val cachedVideos = videosByTopic[selectedTopicId]
        if (cachedVideos == null || cachedVideos.isEmpty()) {
            // 如果缓存中没有数据，加载并更新currentTopicId
            viewModel.loadVideos(selectedTopicId, refresh = true, updateCurrentTopic = true)
        } else {
            // 如果缓存中有数据，只更新currentTopicId，不重新加载
            if (selectedTopicId != currentTopicId) {
                viewModel.setCurrentTopicId(selectedTopicId)
            }
        }
    }
    
    // 返回顶部功能
    LaunchedEffect(scrollToTopTrigger) {
        if (scrollToTopTrigger > 0) {
            onRefreshRequest()
        }
    }
    
    Column(modifier = modifier.fillMaxSize()) {
        // 话题切换栏
        TopicTabs(
            topics = topics,
            selectedTopicId = selectedTopicId,
            onTopicSelected = { topicId ->
                topicViewModel.selectTopic(topicId)
            }
        )
        
        // 使用HorizontalPager实现左右滑动切换topic
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            key = { index -> topics.getOrNull(index)?.id ?: index }
        ) { page ->
            val currentTopic = topics.getOrNull(page)
            if (currentTopic != null) {
                // 为每个topic加载对应的视频（如果缓存中没有）
                // 注意：不更新currentTopicId，让每个topic独立加载
                LaunchedEffect(currentTopic.id) {
                    val cachedVideos = videosByTopic[currentTopic.id]
                    if (cachedVideos == null || cachedVideos.isEmpty()) {
                        viewModel.loadVideos(currentTopic.id, refresh = true, updateCurrentTopic = false)
                    }
                }
                
                // 获取当前topic的视频列表（从缓存中）
                val topicVideos = videosByTopic[currentTopic.id] ?: emptyList()
                val topicIsLoading = loadingByTopic[currentTopic.id] ?: false
                val topicHasMore = hasMoreByTopic[currentTopic.id] ?: true
                val isCurrentTopic = currentTopic.id == currentTopicId
                
                // 刷新状态：只有在当前topic且正在加载且有视频时才显示刷新
                val isRefreshing = topicIsLoading && topicVideos.isNotEmpty() && isCurrentTopic
                
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = {
                        viewModel.refreshVideos(currentTopic.id)
                    }
                ) {
                    TopicVideoContent(
                        topicId = currentTopic.id,
                        videos = topicVideos,
                        isLoading = topicIsLoading,
                        hasMore = topicHasMore,
                        onVideoClick = onVideoClick,
                        onLoadMore = {
                            viewModel.loadMoreVideos(currentTopic.id)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Topic视频内容组件（双列布局）
 */
@Composable
fun TopicVideoContent(
    topicId: String,
    videos: List<Video>,
    isLoading: Boolean,
    hasMore: Boolean,
    onVideoClick: (Video) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 为每个topic创建独立的滚动状态
    val leftListState = rememberLazyListState()
    val rightListState = rememberLazyListState()
    
    // 同步滚动标志，防止循环同步
    var isSyncingScroll by remember { mutableStateOf(false) }
    
    // 使用 snapshotFlow 实时监听左列滚动变化，同步到右列
    LaunchedEffect(leftListState) {
        snapshotFlow {
            leftListState.firstVisibleItemIndex to leftListState.firstVisibleItemScrollOffset
        }
        .collect { (index, offset) ->
            // 只有在不是同步过程中才执行，避免循环同步
            if (!isSyncingScroll && rightListState.layoutInfo.totalItemsCount > 0) {
                isSyncingScroll = true
                
                val rightIndex = index.coerceIn(
                    0,
                    rightListState.layoutInfo.totalItemsCount - 1
                )
                
                // 使用 scrollToItem 实现实时同步
                try {
                    rightListState.scrollToItem(
                        index = rightIndex,
                        scrollOffset = offset
                    )
                } catch (e: Exception) {
                    // 忽略滚动错误（可能因为索引超出范围）
                }
                
                // 短暂延迟后重置标志，允许下一次同步
                kotlinx.coroutines.delay(1)
                isSyncingScroll = false
            }
        }
    }
    
    // 使用 snapshotFlow 实时监听右列滚动变化，同步到左列
    LaunchedEffect(rightListState) {
        snapshotFlow {
            rightListState.firstVisibleItemIndex to rightListState.firstVisibleItemScrollOffset
        }
        .collect { (index, offset) ->
            // 只有在不是同步过程中才执行，避免循环同步
            if (!isSyncingScroll && leftListState.layoutInfo.totalItemsCount > 0) {
                isSyncingScroll = true
                
                val leftIndex = index.coerceIn(
                    0,
                    leftListState.layoutInfo.totalItemsCount - 1
                )
                
                // 使用 scrollToItem 实现实时同步
                try {
                    leftListState.scrollToItem(
                        index = leftIndex,
                        scrollOffset = offset
                    )
                } catch (e: Exception) {
                    // 忽略滚动错误（可能因为索引超出范围）
                }
                
                // 短暂延迟后重置标志，允许下一次同步
                kotlinx.coroutines.delay(1)
                isSyncingScroll = false
            }
        }
    }
    
    // 检测滚动到底部，加载更多
    LaunchedEffect(leftListState, rightListState, hasMore, isLoading) {
        snapshotFlow {
            val leftLastVisible = leftListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val rightLastVisible = rightListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val leftTotal = leftListState.layoutInfo.totalItemsCount
            val rightTotal = rightListState.layoutInfo.totalItemsCount
            Pair(Pair(leftLastVisible, rightLastVisible), Pair(leftTotal, rightTotal))
        }
        .collect { (indices, totals) ->
            val (leftLastVisible, rightLastVisible) = indices
            val (leftTotal, rightTotal) = totals
            if ((leftLastVisible >= leftTotal - 2 || rightLastVisible >= rightTotal - 2) && hasMore && !isLoading) {
                onLoadMore()
            }
        }
    }
    
    if (isLoading && videos.isEmpty()) {
        // 加载中显示
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // 双列布局（使用两个LazyColumn，通过同步滚动实现整体滚动效果）
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                // 左列
                LazyColumn(
                    state = leftListState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    userScrollEnabled = true
                ) {
                    itemsIndexed(videos.filterIndexed { index, _ -> index % 2 == 0 }) { _, video ->
                        VideoCard(
                            video = video,
                            onClick = { onVideoClick(video) }
                        )
                    }
                    
                    // 加载更多指示器
                    if (isLoading && videos.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
                
                // 右列（可以滚动，通过同步实现与左列一起滚动）
                LazyColumn(
                    state = rightListState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    userScrollEnabled = true
                ) {
                    itemsIndexed(videos.filterIndexed { index, _ -> index % 2 == 1 }) { _, video ->
                        VideoCard(
                            video = video,
                            onClick = { onVideoClick(video) }
                        )
                    }
                    
                    // 加载更多指示器
                    if (isLoading && videos.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 话题切换标签栏
 */
@Composable
fun TopicTabs(
    topics: List<Topic>,
    selectedTopicId: String,
    onTopicSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = topics.indexOfFirst { it.id == selectedTopicId }.takeIf { it >= 0 } ?: 0
    val topicsListState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
    
    // 当选中话题变化时，滚动到对应位置
    LaunchedEffect(selectedTopicId) {
        val index = topics.indexOfFirst { it.id == selectedTopicId }
        if (index >= 0) {
            topicsListState.animateScrollToItem(index)
        }
    }
    
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        LazyRow(
            state = topicsListState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            itemsIndexed(topics) { _, topic ->
                TopicTabItem(
                    topic = topic,
                    isSelected = topic.id == selectedTopicId,
                    onClick = { onTopicSelected(topic.id) }
                )
            }
        }
    }
}

/**
 * 话题标签项
 */
@Composable
fun TopicTabItem(
    topic: Topic,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    ) {
        Text(
            text = topic.name,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

/**
 * 视频卡片组件
 */
@Composable
fun VideoCard(
    video: Video,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // 视频封面（如果有）- 自适应布局
            if (video.hasVideo && !video.videoCover.isNullOrBlank()) {
                val context = LocalContext.current
                val coverResourceId = try {
                    context.resources.getIdentifier(
                        video.videoCover,
                        "drawable",
                        context.packageName
                    )
                } catch (e: Exception) {
                    0
                }
                
                if (coverResourceId != 0) {
                    // 自适应封面布局：根据图片比例计算高度
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val maxWidth = maxWidth
                        
                        // 获取图片尺寸并计算高度
                        val imageHeight = remember(coverResourceId, maxWidth) {
                            try {
                                val drawable = context.resources.getDrawable(coverResourceId, null)
                                val width = drawable.intrinsicWidth
                                val height = drawable.intrinsicHeight
                                if (width > 0 && height > 0) {
                                    // 计算宽高比
                                    val aspectRatio = height.toFloat() / width.toFloat()
                                    // 根据最大宽度和宽高比计算高度
                                    val calculatedHeight = maxWidth * aspectRatio
                                    // 限制高度范围：最小100dp，最大300dp
                                    calculatedHeight.coerceIn(100.dp, 300.dp)
                                } else {
                                    // 默认比例 16:9
                                    maxWidth * 9f / 16f
                                }
                            } catch (e: Exception) {
                                // 默认比例 16:9
                                maxWidth * 9f / 16f
                            }
                        }
                        
                        Image(
                            painter = painterResource(id = coverResourceId),
                            contentDescription = "视频封面",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(1.dp))
                }
            }
            
            // 标题
            Text(
                text = video.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                lineHeight = 16.sp
            )
            
            Spacer(modifier = Modifier.height(1.dp))
            
            // 作者和发布时间
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = video.authorName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = video.publishTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // 热门标签和互动数据
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 热门标签
                if (video.isHot) {
                    Surface(
                        color = Color(0xFFFF6B35), // 橘红色背景
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "热门",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFFFFF) // 白色文字
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }
                
                // 点赞数和评论数
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "点赞",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${video.likeCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Comment,
                            contentDescription = "评论",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${video.commentCount}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

