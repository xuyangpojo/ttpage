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
 * CommentViewModel
 * @brief .
 * @author xuyang
 * @date 2025-12-11
 */
class CommentViewModel : ViewModel() {
    
    private val repository = CommentRepository()
    
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()
    
    private val _commentsWithReplies = MutableStateFlow<List<CommentWithReplies>>(emptyList())
    val commentsWithReplies: StateFlow<List<CommentWithReplies>> = _commentsWithReplies.asStateFlow()
    
    init {
        viewModelScope.launch {
            _comments.collect { flatComments ->
                _commentsWithReplies.value = buildCommentTree(flatComments)
            }
        }
    }
    
    // UI状态: 加载
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // UI状态: 错误
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // UI状态: 已点赞的评论
    private val _likedCommentIds = MutableStateFlow<Set<String>>(emptySet())
    val likedCommentIds: StateFlow<Set<String>> = _likedCommentIds.asStateFlow()
    
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
    
    fun addComment(videoId: String, content: String, parentCommentId: String? = null) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val newComment = repository.addComment(videoId, content, parentCommentId)
                if (parentCommentId == null) {
                    _comments.value = listOf(newComment) + _comments.value
                } else {
                    _comments.value = _comments.value + newComment
                }
            } catch (e: Exception) {
                _errorMessage.value = "评论失败: ${e.message}"
            }
        }
    }
    
    fun likeComment(commentId: String) {
        viewModelScope.launch {
            try {
                repository.likeComment(commentId)
                _likedCommentIds.value = _likedCommentIds.value + commentId
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
    
    fun unlikeComment(commentId: String) {
        viewModelScope.launch {
            try {
                repository.unlikeComment(commentId)
                _likedCommentIds.value = _likedCommentIds.value - commentId
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
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    private fun buildCommentTree(flatComments: List<Comment>): List<CommentWithReplies> {
        val commentMap = flatComments.associateBy { it.id }
        val topLevelComments = flatComments.filter { it.parentCommentId == null }
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

