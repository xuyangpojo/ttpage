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
        
        val commentId = viewModel.comments.value.firstOrNull()?.id ?: "c1"
        
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
        
        val commentId = viewModel.comments.value.firstOrNull()?.id ?: "c1"
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
        assertEquals(initialLikeCount + 1, updatedComment!!.likeCount)
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
}

