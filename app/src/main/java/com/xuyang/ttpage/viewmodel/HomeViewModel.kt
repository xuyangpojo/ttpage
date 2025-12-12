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
    
    // 为每个topic维护独立的视频列表缓存
    private val _videosByTopic = MutableStateFlow<Map<String, List<Video>>>(emptyMap())
    val videosByTopic: StateFlow<Map<String, List<Video>>> = _videosByTopic.asStateFlow()
    
    // 为每个topic维护独立的加载状态
    private val _loadingByTopic = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val loadingByTopic: StateFlow<Map<String, Boolean>> = _loadingByTopic.asStateFlow()
    
    // 为每个topic维护独立的hasMore状态
    private val _hasMoreByTopic = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val hasMoreByTopic: StateFlow<Map<String, Boolean>> = _hasMoreByTopic.asStateFlow()
    
    // UI状态：错误信息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // UI状态：当前话题ID
    private val _currentTopicId = MutableStateFlow<String>("all")
    val currentTopicId: StateFlow<String> = _currentTopicId.asStateFlow()
    
    /**
     * 设置当前话题ID（不加载数据）
     */
    fun setCurrentTopicId(topicId: String) {
        _currentTopicId.value = topicId
    }
    
    init {
        // 初始化时加载数据
        loadVideos()
    }
    
    /**
     * 根据ID获取视频（从所有topic的视频中查找）
     */
    fun getVideoById(id: String): Video? {
        return _videosByTopic.value.values.flatten().find { it.id == id }
    }
    
    /**
     * 获取指定topic的视频列表
     */
    fun getVideosByTopic(topicId: String): List<Video> {
        return _videosByTopic.value[topicId] ?: emptyList()
    }
    
    /**
     * 获取指定topic的加载状态
     */
    fun isLoadingTopic(topicId: String): Boolean {
        return _loadingByTopic.value[topicId] ?: false
    }
    
    /**
     * 获取指定topic的hasMore状态
     */
    fun hasMoreForTopic(topicId: String): Boolean {
        return _hasMoreByTopic.value[topicId] ?: true
    }
    
    /**
     * 加载视频列表
     */
    fun loadVideos(topicId: String = "all", refresh: Boolean = false, updateCurrentTopic: Boolean = true) {
        viewModelScope.launch {
            try {
                // 更新当前topic的加载状态
                _loadingByTopic.value = _loadingByTopic.value.toMutableMap().apply {
                    put(topicId, true)
                }
                _errorMessage.value = null
                
                // 如果要求更新currentTopic，则更新
                if (updateCurrentTopic) {
                    _currentTopicId.value = topicId
                }
                
                val newVideos = if (topicId == "all") {
                    videoRepository.getRecommendedVideos()
                } else {
                    topicRepository.getVideosByTopic(topicId)
                }
                
                val currentVideos = _videosByTopic.value[topicId] ?: emptyList()
                val updatedVideos = if (refresh) {
                    // 刷新时随机打乱视频顺序
                    newVideos.shuffled()
                } else {
                    // 加载更多时追加
                    val existingIds = currentVideos.map { it.id }.toSet()
                    val filteredNew = newVideos.filter { it.id !in existingIds }
                    currentVideos + filteredNew
                }
                
                // 更新该topic的视频列表缓存
                _videosByTopic.value = _videosByTopic.value.toMutableMap().apply {
                    put(topicId, updatedVideos)
                }
                
                // 更新hasMore状态
                val hasMoreValue = newVideos.isNotEmpty()
                _hasMoreByTopic.value = _hasMoreByTopic.value.toMutableMap().apply {
                    put(topicId, hasMoreValue)
                }
            } catch (e: Exception) {
                _errorMessage.value = "加载失败: ${e.message}"
            } finally {
                // 更新加载状态
                _loadingByTopic.value = _loadingByTopic.value.toMutableMap().apply {
                    put(topicId, false)
                }
            }
        }
    }
    
    /**
     * 刷新视频
     */
    fun refreshVideos(topicId: String? = null) {
        val targetTopicId = topicId ?: _currentTopicId.value
        // 刷新时，如果指定了topicId且与currentTopicId不同，则更新currentTopicId
        val updateCurrent = topicId == null || topicId == _currentTopicId.value
        loadVideos(targetTopicId, refresh = true, updateCurrentTopic = updateCurrent)
    }
    
    /**
     * 加载更多视频
     */
    fun loadMoreVideos(topicId: String? = null) {
        val targetTopicId = topicId ?: _currentTopicId.value
        val isLoading = _loadingByTopic.value[targetTopicId] ?: false
        val hasMore = _hasMoreByTopic.value[targetTopicId] ?: true
        if (!isLoading && hasMore) {
            // 加载更多时，如果指定了topicId且与currentTopicId不同，则更新currentTopicId
            val updateCurrent = topicId == null || topicId == _currentTopicId.value
            loadVideos(targetTopicId, refresh = false, updateCurrentTopic = updateCurrent)
        }
    }
    
    /**
     * 清除错误信息
     */
    fun clearError() {
        _errorMessage.value = null
    }
}

