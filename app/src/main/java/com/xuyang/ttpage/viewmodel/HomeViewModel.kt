package com.xuyang.ttpage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xuyang.ttpage.model.data.Content
import com.xuyang.ttpage.model.repository.ContentRepository
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
 */
class HomeViewModel : ViewModel() {
    
    private val repository = ContentRepository()
    
    // UI状态：内容列表
    private val _contents = MutableStateFlow<List<Content>>(emptyList())
    val contents: StateFlow<List<Content>> = _contents.asStateFlow()
    
    // UI状态：加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // UI状态：错误信息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
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
    fun loadContents() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                _contents.value = repository.getRecommendedContents()
            } catch (e: Exception) {
                _errorMessage.value = "加载失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 清除错误信息
     */
    fun clearError() {
        _errorMessage.value = null
    }
}

