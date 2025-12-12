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
 * HomeScreen
 * @brief .
 * @author xuyang
 * @date 2025-12-11
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
    val videosByTopic by viewModel.videosByTopic.collectAsState()
    val loadingByTopic by viewModel.loadingByTopic.collectAsState()
    val hasMoreByTopic by viewModel.hasMoreByTopic.collectAsState()
    val currentTopicId by viewModel.currentTopicId.collectAsState()
    
    val topics by topicViewModel.topics.collectAsState()
    val selectedTopicId by topicViewModel.selectedTopicId.collectAsState()
    
    val selectedTopicIndex = remember(selectedTopicId, topics) {
        topics.indexOfFirst { it.id == selectedTopicId }.coerceAtLeast(0)
    }
    
    val pagerState = rememberPagerState(
        initialPage = selectedTopicIndex,
        pageCount = { topics.size.coerceAtLeast(1) }
    )
    
    LaunchedEffect(pagerState.currentPage) {
        val currentPage = pagerState.currentPage
        if (currentPage < topics.size) {
            val topicId = topics[currentPage].id
            if (topicId != selectedTopicId) {
                topicViewModel.selectTopic(topicId)
            }
        }
    }
    
    LaunchedEffect(selectedTopicId) {
        val index = topics.indexOfFirst { it.id == selectedTopicId }
        if (index >= 0 && index != pagerState.currentPage) {
            pagerState.animateScrollToPage(index)
        }
    }
    
    LaunchedEffect(selectedTopicId) {
        val cachedVideos = videosByTopic[selectedTopicId]
        if (cachedVideos == null || cachedVideos.isEmpty()) {
            viewModel.loadVideos(selectedTopicId, refresh = true, updateCurrentTopic = true)
        } else {
            if (selectedTopicId != currentTopicId) {
                viewModel.setCurrentTopicId(selectedTopicId)
            }
        }
    }
    
    LaunchedEffect(scrollToTopTrigger) {
        if (scrollToTopTrigger > 0) {
            onRefreshRequest()
        }
    }
    
    Column(modifier = modifier.fillMaxSize()) {
        TopicTabs(
            topics = topics,
            selectedTopicId = selectedTopicId,
            onTopicSelected = { topicId ->
                topicViewModel.selectTopic(topicId)
            }
        )
        
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            key = { index -> topics.getOrNull(index)?.id ?: index }
        ) { page ->
            val currentTopic = topics.getOrNull(page)
            if (currentTopic != null) {
                LaunchedEffect(currentTopic.id) {
                    val cachedVideos = videosByTopic[currentTopic.id]
                    if (cachedVideos == null || cachedVideos.isEmpty()) {
                        viewModel.loadVideos(currentTopic.id, refresh = true, updateCurrentTopic = false)
                    }
                }
                
                val topicVideos = videosByTopic[currentTopic.id] ?: emptyList()
                val topicIsLoading = loadingByTopic[currentTopic.id] ?: false
                val topicHasMore = hasMoreByTopic[currentTopic.id] ?: true
                val isCurrentTopic = currentTopic.id == currentTopicId
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
    val leftListState = rememberLazyListState()
    val rightListState = rememberLazyListState()
    
    var isSyncingScroll by remember { mutableStateOf(false) }
    
    LaunchedEffect(leftListState) {
        snapshotFlow {
            leftListState.firstVisibleItemIndex to leftListState.firstVisibleItemScrollOffset
        }
        .collect { (index, offset) ->
            if (!isSyncingScroll && rightListState.layoutInfo.totalItemsCount > 0) {
                isSyncingScroll = true
                
                val rightIndex = index.coerceIn(
                    0,
                    rightListState.layoutInfo.totalItemsCount - 1
                )
                
                try {
                    rightListState.scrollToItem(
                        index = rightIndex,
                        scrollOffset = offset
                    )
                } catch (e: Exception) {
                }
                
                kotlinx.coroutines.delay(1)
                isSyncingScroll = false
            }
        }
    }
    
    LaunchedEffect(rightListState) {
        snapshotFlow {
            rightListState.firstVisibleItemIndex to rightListState.firstVisibleItemScrollOffset
        }
        .collect { (index, offset) ->
            if (!isSyncingScroll && leftListState.layoutInfo.totalItemsCount > 0) {
                isSyncingScroll = true
                
                val leftIndex = index.coerceIn(
                    0,
                    leftListState.layoutInfo.totalItemsCount - 1
                )
                
                try {
                    leftListState.scrollToItem(
                        index = leftIndex,
                        scrollOffset = offset
                    )
                } catch (e: Exception) {
                }
                
                kotlinx.coroutines.delay(1)
                isSyncingScroll = false
            }
        }
    }
    
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
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
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

@Composable
fun TopicTabs(
    topics: List<Topic>,
    selectedTopicId: String,
    onTopicSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = topics.indexOfFirst { it.id == selectedTopicId }.takeIf { it >= 0 } ?: 0
    val topicsListState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
    
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
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val maxWidth = maxWidth
                        val imageHeight = remember(coverResourceId, maxWidth) {
                            try {
                                val drawable = context.resources.getDrawable(coverResourceId, null)
                                val width = drawable.intrinsicWidth
                                val height = drawable.intrinsicHeight
                                if (width > 0 && height > 0) {
                                    val aspectRatio = height.toFloat() / width.toFloat()
                                    val calculatedHeight = maxWidth * aspectRatio
                                    calculatedHeight.coerceIn(100.dp, 300.dp)
                                } else {
                                    maxWidth * 9f / 16f
                                }
                            } catch (e: Exception) {
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
            
            Text(
                text = video.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                lineHeight = 16.sp
            )
            
            Spacer(modifier = Modifier.height(1.dp))
            
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
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (video.isHot) {
                    Surface(
                        color = Color(0xFFFF6B35),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "热门",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFFFFF)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }
                
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

