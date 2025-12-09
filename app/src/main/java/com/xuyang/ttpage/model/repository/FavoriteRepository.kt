package com.xuyang.ttpage.model.repository

import com.xuyang.ttpage.model.data.Content
import kotlinx.coroutines.delay

/**
 * Model层：收藏和历史记录数据仓库
 * 负责管理用户的收藏内容和浏览历史
 */
class FavoriteRepository {
    
    // 内存存储（实际应用中应使用数据库）
    private val favoriteContents = mutableSetOf<String>() // 收藏的内容ID集合
    private val historyContents = mutableListOf<Pair<String, Long>>() // 历史记录：(内容ID, 时间戳)
    
    /**
     * 添加收藏
     */
    suspend fun addFavorite(contentId: String): Boolean {
        delay(100) // 模拟网络请求
        return favoriteContents.add(contentId)
    }
    
    /**
     * 取消收藏
     */
    suspend fun removeFavorite(contentId: String): Boolean {
        delay(100) // 模拟网络请求
        return favoriteContents.remove(contentId)
    }
    
    /**
     * 检查是否已收藏
     */
    suspend fun isFavorite(contentId: String): Boolean {
        delay(50) // 模拟查询
        return favoriteContents.contains(contentId)
    }
    
    /**
     * 获取所有收藏的内容ID列表
     */
    suspend fun getFavoriteIds(): List<String> {
        delay(100) // 模拟查询
        return favoriteContents.toList()
    }
    
    /**
     * 添加浏览历史
     */
    suspend fun addHistory(contentId: String) {
        delay(50) // 模拟保存
        // 移除已存在的记录（避免重复）
        historyContents.removeAll { it.first == contentId }
        // 添加到列表开头
        historyContents.add(0, Pair(contentId, System.currentTimeMillis()))
        // 限制历史记录数量（最多保存100条）
        if (historyContents.size > 100) {
            historyContents.removeAt(historyContents.size - 1)
        }
    }
    
    /**
     * 获取浏览历史ID列表（按时间倒序）
     */
    suspend fun getHistoryIds(): List<String> {
        delay(100) // 模拟查询
        return historyContents.map { it.first }
    }
    
    /**
     * 清除浏览历史
     */
    suspend fun clearHistory() {
        delay(100) // 模拟删除
        historyContents.clear()
    }
    
    /**
     * 删除单条历史记录
     */
    suspend fun removeHistory(contentId: String) {
        delay(50) // 模拟删除
        historyContents.removeAll { it.first == contentId }
    }
}

