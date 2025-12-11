package com.xuyang.ttpage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xuyang.ttpage.model.data.Comment
import com.xuyang.ttpage.model.data.CommentWithReplies
import com.xuyang.ttpage.model.repository.CommentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel层：评论视图模型
 */
class CommentViewModel : ViewModel() {
    
    private val repository = CommentRepository()
    
    // UI状态：评论列表（扁平化）
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()
    
    // UI状态：树形结构的评论列表（用于UI展示）
    val commentsWithReplies: StateFlow<List<CommentWithReplies>> = _comments.map { flatComments ->
        buildCommentTree(flatComments)
    }.asStateFlow()
    
    // UI状态：加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // UI状态：错误信息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // UI状态：已点赞的评论ID集合
    private val _likedCommentIds = MutableStateFlow<Set<String>>(emptySet())
    val likedCommentIds: StateFlow<Set<String>> = _likedCommentIds.asStateFlow()
    
    /**
     * 加载评论列表
     */
    fun loadComments(videoId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                _comments.value = repository.getComments(videoId)
            } catch (e: Exception) {
                _errorMessage.value = "加载评论失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * 添加评论
     */
    fun addComment(videoId: String, content: String, parentCommentId: String? = null) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val newComment = repository.addComment(videoId, content, parentCommentId)
                _comments.value = _comments.value + newComment
            } catch (e: Exception) {
                _errorMessage.value = "评论失败: ${e.message}"
            }
        }
    }
    
    /**
     * 点赞评论
     */
    fun likeComment(commentId: String) {
        viewModelScope.launch {
            try {
                repository.likeComment(commentId)
                _likedCommentIds.value = _likedCommentIds.value + commentId
                // 更新评论的点赞数
                _comments.value = _comments.value.map { comment ->
                    if (comment.id == commentId) {
                        comment.copy(likeCount = comment.likeCount + 1u)
                    } else {
                        comment
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "点赞失败: ${e.message}"
            }
        }
    }
    
    /**
     * 取消点赞评论
     */
    fun unlikeComment(commentId: String) {
        viewModelScope.launch {
            try {
                repository.unlikeComment(commentId)
                _likedCommentIds.value = _likedCommentIds.value - commentId
                // 更新评论的点赞数
                _comments.value = _comments.value.map { comment ->
                    if (comment.id == commentId) {
                        comment.copy(likeCount = (comment.likeCount - 1u).coerceAtLeast(0u))
                    } else {
                        comment
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "取消点赞失败: ${e.message}"
            }
        }
    }
    
    /**
     * 清除错误信息
     */
    fun clearError() {
        _errorMessage.value = null
    }
    
    /**
     * 将扁平化的评论列表构建成树形结构
     * @param flatComments 扁平化的评论列表
     * @return 树形结构的评论列表（只包含顶级评论，回复嵌套在replies中）
     */
    private fun buildCommentTree(flatComments: List<Comment>): List<CommentWithReplies> {
        // 将评论按ID建立索引
        val commentMap = flatComments.associateBy { it.id }
        
        // 找出所有顶级评论（parentCommentId为null）
        val topLevelComments = flatComments.filter { it.parentCommentId == null }
        
        // 递归构建树形结构
        fun buildReplies(parentId: String): List<CommentWithReplies> {
            return flatComments
                .filter { it.parentCommentId == parentId }
                .map { comment ->
                    CommentWithReplies(
                        comment = comment,
                        replies = buildReplies(comment.id)
                    )
                }
        }
        
        return topLevelComments.map { comment ->
            CommentWithReplies(
                comment = comment,
                replies = buildReplies(comment.id)
            )
        }
    }
}

