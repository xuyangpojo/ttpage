package com.xuyang.ttpage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xuyang.ttpage.model.data.Video
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
    
    // UI状态：收藏的视频列表
    private val _favorites = MutableStateFlow<List<Video>>(emptyList())
    val favorites: StateFlow<List<Video>> = _favorites.asStateFlow()
    
    // UI状态：浏览历史ID列表
    private val _historyIds = MutableStateFlow<List<String>>(emptyList())
    val historyIds: StateFlow<List<String>> = _historyIds.asStateFlow()
    
    // UI状态：浏览历史视频列表
    private val _history = MutableStateFlow<List<Video>>(emptyList())
    val history: StateFlow<List<Video>> = _history.asStateFlow()
    
    // UI状态：加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        // 初始化时加载收藏和历史记录
        loadFavorites()
        loadHistory()
    }
    
    /**
     * 检查视频是否已收藏
     */
    fun isFavorite(videoId: String): Boolean {
        return _favoriteIds.value.contains(videoId)
    }
    
    /**
     * 切换收藏状态
     */
    fun toggleFavorite(videoId: String) {
        viewModelScope.launch {
            try {
                val isCurrentlyFavorite = _favoriteIds.value.contains(videoId)
                val success = if (isCurrentlyFavorite) {
                    repository.removeFavorite(videoId)
                } else {
                    repository.addFavorite(videoId)
                }
                
                if (success) {
                    // 更新本地状态
                    val newFavoriteIds = if (isCurrentlyFavorite) {
                        _favoriteIds.value - videoId
                    } else {
                        _favoriteIds.value + videoId
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
    fun addHistory(videoId: String) {
        viewModelScope.launch {
            try {
                repository.addHistory(videoId)
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
     * 更新收藏视频列表（从视频列表填充）
     */
    fun updateFavoritesFromVideos(videos: List<Video>) {
        val favoriteVideos = videos.filter { _favoriteIds.value.contains(it.id) }
        _favorites.value = favoriteVideos
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
     * 更新历史视频列表（从视频列表填充）
     */
    fun updateHistoryFromVideos(videos: List<Video>) {
        val historyVideos = _historyIds.value.mapNotNull { id ->
            videos.find { it.id == id }
        }
        _history.value = historyVideos
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
    fun removeHistory(videoId: String) {
        viewModelScope.launch {
            try {
                repository.removeHistory(videoId)
                loadHistory()
            } catch (e: Exception) {
                // 错误处理
            }
        }
    }
}

