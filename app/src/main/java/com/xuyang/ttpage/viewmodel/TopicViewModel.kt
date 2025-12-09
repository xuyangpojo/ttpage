package com.xuyang.ttpage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xuyang.ttpage.model.data.Topic
import com.xuyang.ttpage.model.repository.TopicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel层：话题视图模型
 * 
 * 职责：
 * 1. 管理话题列表状态
 * 2. 管理当前选中的话题
 * 3. 与Repository通信获取话题数据
 */
class TopicViewModel : ViewModel() {
    
    private val repository = TopicRepository()
    
    // UI状态：话题列表
    private val _topics = MutableStateFlow<List<Topic>>(emptyList())
    val topics: StateFlow<List<Topic>> = _topics.asStateFlow()
    
    // UI状态：当前选中的话题ID
    private val _selectedTopicId = MutableStateFlow<String>("all")
    val selectedTopicId: StateFlow<String> = _selectedTopicId.asStateFlow()
    
    // UI状态：加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        // 初始化时加载话题列表
        loadTopics()
    }
    
    /**
     * 加载话题列表
     */
    fun loadTopics() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _topics.value = repository.getTopics()
            } catch (e: Exception) {
                // 错误处理
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 选择话题
     */
    fun selectTopic(topicId: String) {
        _selectedTopicId.value = topicId
    }
    
    /**
     * 获取当前选中的话题
     */
    fun getSelectedTopic(): Topic? {
        return _topics.value.find { it.id == _selectedTopicId.value }
    }
}

