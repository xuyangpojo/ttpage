package com.xuyang.ttpage.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/**
 * CommentViewModel单元测试
 */
class CommentViewModelTest {
    
    @Test
    fun `initial state should have empty comments`() = runTest {
        // Given
        val viewModel = CommentViewModel()
        
        // When & Then
        viewModel.comments.test {
            val comments = awaitItem()
            assertTrue(comments.isEmpty())
        }
    }
    
    @Test
    fun `loadComments should populate comments`() = runTest {
        // Given
        val viewModel = CommentViewModel()
        val videoId = "video1"
        
        // When
        viewModel.loadComments(videoId)
        
        // Wait for loading
        delay(1000)
        
        // Then
        val comments = viewModel.comments.value
        assertTrue(comments.isNotEmpty())
    }
    
    @Test
    fun `addComment should add new comment to list`() = runTest {
        // Given
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        
        val initialCount = viewModel.comments.value.size
        
        // When
        viewModel.addComment(videoId, "New comment")
        delay(500)
        
        // Then
        val comments = viewModel.comments.value
        assertEquals(initialCount + 1, comments.size)
        assertTrue(comments.any { it.content == "New comment" })
    }
    
    @Test
    fun `likeComment should add comment to liked set`() = runTest {
        // Given
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        
        val commentId = viewModel.comments.value.firstOrNull()?.id ?: "c001"
        
        // When
        viewModel.likeComment(commentId)
        delay(500)
        
        // Then
        assertTrue(viewModel.likedCommentIds.value.contains(commentId))
    }
    
    @Test
    fun `unlikeComment should remove comment from liked set`() = runTest {
        // Given
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        
        val commentId = viewModel.comments.value.firstOrNull()?.id ?: "c001"
        viewModel.likeComment(commentId)
        delay(500)
        
        // Verify liked
        assertTrue(viewModel.likedCommentIds.value.contains(commentId))
        
        // When
        viewModel.unlikeComment(commentId)
        delay(500)
        
        // Then
        assertFalse(viewModel.likedCommentIds.value.contains(commentId))
    }
    
    @Test
    fun `likeComment should increase like count`() = runTest {
        // Given
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        
        val comment = viewModel.comments.value.firstOrNull()
        assertNotNull(comment)
        val initialLikeCount = comment!!.likeCount
        
        // When
        viewModel.likeComment(comment.id)
        delay(500)
        
        // Then
        val updatedComment = viewModel.comments.value.find { it.id == comment.id }
        assertNotNull(updatedComment)
        assertEquals(initialLikeCount + 1u, updatedComment!!.likeCount)
    }
    
    @Test
    fun `clearError should clear error message`() = runTest {
        // Given
        val viewModel = CommentViewModel()
        
        // When
        viewModel.clearError()
        
        // Then
        assertNull(viewModel.errorMessage.value)
    }
    
    @Test
    fun `commentsWithReplies should build tree structure correctly`() = runTest {
        // Given
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        
        // When
        val commentsWithReplies = viewModel.commentsWithReplies.value
        
        // Then
        assertTrue(commentsWithReplies.isNotEmpty())
        // 应该只有顶级评论（parentCommentId为null的评论）
        val topLevelComments = viewModel.comments.value.filter { it.parentCommentId == null }
        assertEquals(topLevelComments.size, commentsWithReplies.size)
        
        // 验证树形结构：每个顶级评论应该包含其回复
        commentsWithReplies.forEach { commentWithReplies ->
            val comment = commentWithReplies.comment
            assertNull("顶级评论的parentCommentId应该为null", comment.parentCommentId)
            
            // 验证回复数量
            val expectedReplies = viewModel.comments.value.filter { it.parentCommentId == comment.id }
            assertEquals(expectedReplies.size, commentWithReplies.replies.size)
        }
    }
    
    @Test
    fun `unlikeComment should decrease like count`() = runTest {
        // Given
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        
        val comment = viewModel.comments.value.firstOrNull()
        assertNotNull(comment)
        val initialLikeCount = comment!!.likeCount
        
        // 先点赞
        viewModel.likeComment(comment.id)
        delay(500)
        
        // 验证点赞数增加
        val likedComment = viewModel.comments.value.find { it.id == comment.id }
        assertEquals(initialLikeCount + 1u, likedComment!!.likeCount)
        
        // When - 取消点赞
        viewModel.unlikeComment(comment.id)
        delay(500)
        
        // Then
        val unlikedComment = viewModel.comments.value.find { it.id == comment.id }
        assertNotNull(unlikedComment)
        assertEquals(initialLikeCount, unlikedComment!!.likeCount)
    }
    
    @Test
    fun `addComment with parentCommentId should add reply`() = runTest {
        // Given
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        
        val parentCommentId = viewModel.comments.value.firstOrNull()?.id ?: "c001"
        val initialCount = viewModel.comments.value.size
        
        // When
        viewModel.addComment(videoId, "Reply comment", parentCommentId)
        delay(500)
        
        // Then
        val comments = viewModel.comments.value
        assertEquals(initialCount + 1, comments.size)
        val newComment = comments.last()
        assertEquals(parentCommentId, newComment.parentCommentId)
        assertEquals("Reply comment", newComment.content)
    }
}

