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
 * HomeViewModel
 * @brief .
 * @author xuyang
 * @date 2025-12-11
 */
class HomeViewModel : ViewModel() {
    
    private val videoRepository = VideoRepository()
    private val topicRepository = TopicRepository()
    
    private val _videosByTopic = MutableStateFlow<Map<String, List<Video>>>(emptyMap())
    val videosByTopic: StateFlow<Map<String, List<Video>>> = _videosByTopic.asStateFlow()
    
    private val _loadingByTopic = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val loadingByTopic: StateFlow<Map<String, Boolean>> = _loadingByTopic.asStateFlow()
    
    private val _hasMoreByTopic = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val hasMoreByTopic: StateFlow<Map<String, Boolean>> = _hasMoreByTopic.asStateFlow()
    
    // UI状态: 错误
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // UI状态: 当前话题ID
    private val _currentTopicId = MutableStateFlow<String>("all")
    val currentTopicId: StateFlow<String> = _currentTopicId.asStateFlow()
    
    fun setCurrentTopicId(topicId: String) {
        _currentTopicId.value = topicId
    }
    
    init {
        loadVideos()
    }
    
    fun getVideoById(id: String): Video? {
        return _videosByTopic.value.values.flatten().find { it.id == id }
    }
    
    fun getVideosByTopic(topicId: String): List<Video> {
        return _videosByTopic.value[topicId] ?: emptyList()
    }
    
    fun isLoadingTopic(topicId: String): Boolean {
        return _loadingByTopic.value[topicId] ?: false
    }
    
    fun hasMoreForTopic(topicId: String): Boolean {
        return _hasMoreByTopic.value[topicId] ?: true
    }
    
    fun loadVideos(topicId: String = "all", refresh: Boolean = false, updateCurrentTopic: Boolean = true) {
        viewModelScope.launch {
            try {
                _loadingByTopic.value = _loadingByTopic.value.toMutableMap().apply {
                    put(topicId, true)
                }
                _errorMessage.value = null
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
                    newVideos.shuffled()
                } else {
                    val existingIds = currentVideos.map { it.id }.toSet()
                    val filteredNew = newVideos.filter { it.id !in existingIds }
                    currentVideos + filteredNew
                }
                
                _videosByTopic.value = _videosByTopic.value.toMutableMap().apply {
                    put(topicId, updatedVideos)
                }
                
                val hasMoreValue = newVideos.isNotEmpty()
                _hasMoreByTopic.value = _hasMoreByTopic.value.toMutableMap().apply {
                    put(topicId, hasMoreValue)
                }
            } catch (e: Exception) {
                _errorMessage.value = "加载失败: ${e.message}"
            } finally {
                _loadingByTopic.value = _loadingByTopic.value.toMutableMap().apply {
                    put(topicId, false)
                }
            }
        }
    }
    
    fun refreshVideos(topicId: String? = null) {
        val targetTopicId = topicId ?: _currentTopicId.value
        val updateCurrent = topicId == null || topicId == _currentTopicId.value
        loadVideos(targetTopicId, refresh = true, updateCurrentTopic = updateCurrent)
    }
    
    fun loadMoreVideos(topicId: String? = null) {
        val targetTopicId = topicId ?: _currentTopicId.value
        val isLoading = _loadingByTopic.value[targetTopicId] ?: false
        val hasMore = _hasMoreByTopic.value[targetTopicId] ?: true
        if (!isLoading && hasMore) {
            val updateCurrent = topicId == null || topicId == _currentTopicId.value
            loadVideos(targetTopicId, refresh = false, updateCurrentTopic = updateCurrent)
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}

