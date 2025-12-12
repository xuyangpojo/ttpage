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
 * TopicViewModel
 * @brief .
 * @author xuyang
 * @date 2025-12-11
 */
class TopicViewModel : ViewModel() {
    
    private val repository = TopicRepository()
    
    private val _topics = MutableStateFlow<List<Topic>>(emptyList())
    val topics: StateFlow<List<Topic>> = _topics.asStateFlow()
    
    private val _selectedTopicId = MutableStateFlow<String>("all")
    val selectedTopicId: StateFlow<String> = _selectedTopicId.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadTopics()
    }
    
    fun loadTopics() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _topics.value = repository.getTopics()
            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun selectTopic(topicId: String) {
        _selectedTopicId.value = topicId
    }
    
    fun getSelectedTopic(): Topic? {
        return _topics.value.find { it.id == _selectedTopicId.value }
    }
}

