package com.xuyang.ttpage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xuyang.ttpage.model.data.Video
import com.xuyang.ttpage.model.repository.TopicRepository
import com.xuyang.ttpage.model.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel层：首页视图模型
 * 
 * 职责：
 * 1. 管理视频列表状态
 * 2. 处理数据加载
 * 3. 与Repository通信获取数据
 * 4. 支持按话题加载视频
 */
class HomeViewModel : ViewModel() {
    
    private val videoRepository = VideoRepository()
    private val topicRepository = TopicRepository()
    
    // UI状态：视频列表
    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos: StateFlow<List<Video>> = _videos.asStateFlow()
    
    // UI状态：加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // UI状态：错误信息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // UI状态：当前话题ID
    private val _currentTopicId = MutableStateFlow<String>("all")
    val currentTopicId: StateFlow<String> = _currentTopicId.asStateFlow()
    
    // UI状态：是否有更多视频
    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore.asStateFlow()
    
    init {
        // 初始化时加载数据
        loadVideos()
    }
    
    /**
     * 根据ID获取视频
     */
    fun getVideoById(id: String): Video? {
        return _videos.value.find { it.id == id }
    }
    
    /**
     * 加载视频列表
     */
    fun loadVideos(topicId: String = "all", refresh: Boolean = false) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                _currentTopicId.value = topicId
                
                val newVideos = if (topicId == "all") {
                    videoRepository.getRecommendedVideos()
                } else {
                    topicRepository.getVideosByTopic(topicId)
                }
                
                if (refresh) {
                    _videos.value = newVideos
                } else {
                    // 加载更多时追加
                    val existingIds = _videos.value.map { it.id }.toSet()
                    val filteredNew = newVideos.filter { it.id !in existingIds }
                    _videos.value = _videos.value + filteredNew
                }
                
                _hasMore.value = newVideos.isNotEmpty()
            } catch (e: Exception) {
                _errorMessage.value = "加载失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 刷新视频
     */
    fun refreshVideos() {
        loadVideos(_currentTopicId.value, refresh = true)
    }
    
    /**
     * 加载更多视频
     */
    fun loadMoreVideos() {
        if (!_isLoading.value && _hasMore.value) {
            loadVideos(_currentTopicId.value, refresh = false)
        }
    }
    
    /**
     * 清除错误信息
     */
    fun clearError() {
        _errorMessage.value = null
    }
}

