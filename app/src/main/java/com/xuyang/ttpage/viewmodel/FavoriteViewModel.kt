package com.xuyang.ttpage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xuyang.ttpage.model.data.Content
import com.xuyang.ttpage.model.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel层：收藏和历史记录视图模型
 * 
 * 职责：
 * 1. 管理收藏状态
 * 2. 管理浏览历史
 * 3. 与Repository通信
 */
class FavoriteViewModel : ViewModel() {
    
    private val repository = FavoriteRepository()
    
    // UI状态：收藏的内容ID集合
    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()
    
    // UI状态：收藏的内容列表
    private val _favorites = MutableStateFlow<List<Content>>(emptyList())
    val favorites: StateFlow<List<Content>> = _favorites.asStateFlow()
    
    // UI状态：浏览历史ID列表
    private val _historyIds = MutableStateFlow<List<String>>(emptyList())
    val historyIds: StateFlow<List<String>> = _historyIds.asStateFlow()
    
    // UI状态：浏览历史内容列表
    private val _history = MutableStateFlow<List<Content>>(emptyList())
    val history: StateFlow<List<Content>> = _history.asStateFlow()
    
    // UI状态：加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        // 初始化时加载收藏和历史记录
        loadFavorites()
        loadHistory()
    }
    
    /**
     * 检查内容是否已收藏
     */
    fun isFavorite(contentId: String): Boolean {
        return _favoriteIds.value.contains(contentId)
    }
    
    /**
     * 切换收藏状态
     */
    fun toggleFavorite(contentId: String) {
        viewModelScope.launch {
            try {
                val isCurrentlyFavorite = _favoriteIds.value.contains(contentId)
                val success = if (isCurrentlyFavorite) {
                    repository.removeFavorite(contentId)
                } else {
                    repository.addFavorite(contentId)
                }
                
                if (success) {
                    // 更新本地状态
                    val newFavoriteIds = if (isCurrentlyFavorite) {
                        _favoriteIds.value - contentId
                    } else {
                        _favoriteIds.value + contentId
                    }
                    _favoriteIds.value = newFavoriteIds
                    
                    // 更新收藏列表
                    loadFavorites()
                }
            } catch (e: Exception) {
                // 错误处理
            }
        }
    }
    
    /**
     * 添加浏览历史
     */
    fun addHistory(contentId: String) {
        viewModelScope.launch {
            try {
                repository.addHistory(contentId)
                // 更新历史记录列表
                loadHistory()
            } catch (e: Exception) {
                // 错误处理
            }
        }
    }
    
    /**
     * 加载收藏列表
     */
    fun loadFavorites() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val ids = repository.getFavoriteIds()
                _favoriteIds.value = ids.toSet()
                
                // 从HomeViewModel获取完整内容信息
                // 这里暂时只保存ID，实际使用时需要从内容列表获取
            } catch (e: Exception) {
                // 错误处理
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 更新收藏内容列表（从内容列表填充）
     */
    fun updateFavoritesFromContents(contents: List<Content>) {
        val favoriteContents = contents.filter { _favoriteIds.value.contains(it.id) }
        _favorites.value = favoriteContents
    }
    
    /**
     * 加载浏览历史
     */
    fun loadHistory() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val ids = repository.getHistoryIds()
                _historyIds.value = ids
            } catch (e: Exception) {
                // 错误处理
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 更新历史内容列表（从内容列表填充）
     */
    fun updateHistoryFromContents(contents: List<Content>) {
        val historyContents = _historyIds.value.mapNotNull { id ->
            contents.find { it.id == id }
        }
        _history.value = historyContents
    }
    
    /**
     * 清除浏览历史
     */
    fun clearHistory() {
        viewModelScope.launch {
            try {
                repository.clearHistory()
                _historyIds.value = emptyList()
                _history.value = emptyList()
            } catch (e: Exception) {
                // 错误处理
            }
        }
    }
    
    /**
     * 删除单条历史记录
     */
    fun removeHistory(contentId: String) {
        viewModelScope.launch {
            try {
                repository.removeHistory(contentId)
                loadHistory()
            } catch (e: Exception) {
                // 错误处理
            }
        }
    }
}

