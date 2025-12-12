package com.xuyang.ttpage.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class CommentViewModelTest {
    
    @Test
    fun `initial state should have empty comments`() = runTest {
        val viewModel = CommentViewModel()
        viewModel.comments.test {
            val comments = awaitItem()
            assertTrue(comments.isEmpty())
        }
    }
    
    @Test
    fun `loadComments should populate comments`() = runTest {
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        val comments = viewModel.comments.value
        assertTrue(comments.isNotEmpty())
    }
    
    @Test
    fun `addComment should add new comment to list`() = runTest {
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        val initialCount = viewModel.comments.value.size
        viewModel.addComment(videoId, "New comment")
        delay(500)
        val comments = viewModel.comments.value
        assertEquals(initialCount + 1, comments.size)
        assertTrue(comments.any { it.content == "New comment" })
    }
    
    @Test
    fun `likeComment should add comment to liked set`() = runTest {
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        val commentId = viewModel.comments.value.firstOrNull()?.id ?: "c001"
        viewModel.likeComment(commentId)
        delay(500)
        assertTrue(viewModel.likedCommentIds.value.contains(commentId))
    }
    
    @Test
    fun `unlikeComment should remove comment from liked set`() = runTest {
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        val commentId = viewModel.comments.value.firstOrNull()?.id ?: "c001"
        viewModel.likeComment(commentId)
        delay(500)
        assertTrue(viewModel.likedCommentIds.value.contains(commentId))
        viewModel.unlikeComment(commentId)
        delay(500)
        assertFalse(viewModel.likedCommentIds.value.contains(commentId))
    }
    
    @Test
    fun `likeComment should increase like count`() = runTest {
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        
        val comment = viewModel.comments.value.firstOrNull()
        assertNotNull(comment)
        val initialLikeCount = comment!!.likeCount

        viewModel.likeComment(comment.id)
        delay(500)
        
        val updatedComment = viewModel.comments.value.find { it.id == comment.id }
        assertNotNull(updatedComment)
        assertEquals(initialLikeCount + 1u, updatedComment!!.likeCount)
    }
    
    @Test
    fun `clearError should clear error message`() = runTest {
        val viewModel = CommentViewModel()
        viewModel.clearError()
        assertNull(viewModel.errorMessage.value)
    }
    
    @Test
    fun `commentsWithReplies should build tree structure correctly`() = runTest {
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        
        val commentsWithReplies = viewModel.commentsWithReplies.value
        
        assertTrue(commentsWithReplies.isNotEmpty())
        val topLevelComments = viewModel.comments.value.filter { it.parentCommentId == null }
        assertEquals(topLevelComments.size, commentsWithReplies.size)
        commentsWithReplies.forEach { commentWithReplies ->
            val comment = commentWithReplies.comment
            assertNull("顶级评论的parentCommentId应该为null", comment.parentCommentId)
            val expectedReplies = viewModel.comments.value.filter { it.parentCommentId == comment.id }
            assertEquals(expectedReplies.size, commentWithReplies.replies.size)
        }
    }
    
    @Test
    fun `unlikeComment should decrease like count`() = runTest {
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        
        val comment = viewModel.comments.value.firstOrNull()
        assertNotNull(comment)
        val initialLikeCount = comment!!.likeCount
        
        viewModel.likeComment(comment.id)
        delay(500)
        
        val likedComment = viewModel.comments.value.find { it.id == comment.id }
        assertEquals(initialLikeCount + 1u, likedComment!!.likeCount)
        
        viewModel.unlikeComment(comment.id)
        delay(500)
        
        val unlikedComment = viewModel.comments.value.find { it.id == comment.id }
        assertNotNull(unlikedComment)
        assertEquals(initialLikeCount, unlikedComment!!.likeCount)
    }
    
    @Test
    fun `addComment with parentCommentId should add reply`() = runTest {
        val viewModel = CommentViewModel()
        val videoId = "video1"
        viewModel.loadComments(videoId)
        delay(1000)
        
        val parentCommentId = viewModel.comments.value.firstOrNull()?.id ?: "c001"
        val initialCount = viewModel.comments.value.size
        
        viewModel.addComment(videoId, "Reply comment", parentCommentId)
        delay(500)
        
        val comments = viewModel.comments.value
        assertEquals(initialCount + 1, comments.size)
        val newComment = comments.last()
        assertEquals(parentCommentId, newComment.parentCommentId)
        assertEquals("Reply comment", newComment.content)
    }
}

