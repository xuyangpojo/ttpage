package com.xuyang.ttpage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xuyang.ttpage.model.data.Content
import com.xuyang.ttpage.model.repository.ContentRepository
import com.xuyang.ttpage.model.repository.TopicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel层：首页视图模型
 * 
 * 职责：
 * 1. 管理内容列表状态
 * 2. 处理数据加载
 * 3. 与Repository通信获取数据
 * 4. 支持按话题加载内容
 */
class HomeViewModel : ViewModel() {
    
    private val contentRepository = ContentRepository()
    private val topicRepository = TopicRepository()
    
    // UI状态：内容列表
    private val _contents = MutableStateFlow<List<Content>>(emptyList())
    val contents: StateFlow<List<Content>> = _contents.asStateFlow()
    
    // UI状态：加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // UI状态：错误信息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // UI状态：当前话题ID
    private val _currentTopicId = MutableStateFlow<String>("all")
    val currentTopicId: StateFlow<String> = _currentTopicId.asStateFlow()
    
    // UI状态：是否有更多内容
    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore.asStateFlow()
    
    init {
        // 初始化时加载数据
        loadContents()
    }
    
    /**
     * 根据ID获取内容
     */
    fun getContentById(id: String): Content? {
        return _contents.value.find { it.id == id }
    }
    
    /**
     * 加载内容列表
     */
    fun loadContents(topicId: String = "all", refresh: Boolean = false) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                _currentTopicId.value = topicId
                
                val newContents = if (topicId == "all") {
                    contentRepository.getRecommendedContents()
                } else {
                    topicRepository.getContentsByTopic(topicId)
                }
                
                if (refresh) {
                    _contents.value = newContents
                } else {
                    // 加载更多时追加
                    val existingIds = _contents.value.map { it.id }.toSet()
                    val filteredNew = newContents.filter { it.id !in existingIds }
                    _contents.value = _contents.value + filteredNew
                }
                
                _hasMore.value = newContents.isNotEmpty()
            } catch (e: Exception) {
                _errorMessage.value = "加载失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 刷新内容
     */
    fun refreshContents() {
        loadContents(_currentTopicId.value, refresh = true)
    }
    
    /**
     * 加载更多内容
     */
    fun loadMoreContents() {
        if (!_isLoading.value && _hasMore.value) {
            loadContents(_currentTopicId.value, refresh = false)
        }
    }
    
    /**
     * 清除错误信息
     */
    fun clearError() {
        _errorMessage.value = null
    }
}

