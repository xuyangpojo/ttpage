package com.xuyang.ttpage.model.repository

import com.xuyang.ttpage.model.data.Comment
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/**
 * CommentRepository单元测试
 */
class CommentRepositoryTest {
    
    private val repository = CommentRepository()
    
    @Test
    fun `getComments should return non-empty list`() = runTest {
        // When
        val comments = repository.getComments("video1")
        
        // Then
        assertNotNull(comments)
        assertTrue(comments.isNotEmpty())
    }
    
    @Test
    fun `getComments should return comments with valid data`() = runTest {
        // When
        val comments = repository.getComments("video1")
        
        // Then
        comments.forEach { comment ->
            assertNotNull(comment.id)
            assertNotNull(comment.videoId)
            assertNotNull(comment.author)
            assertNotNull(comment.content)
            assertNotNull(comment.publishTime)
            assertTrue(comment.likeCount >= 0)
            assertTrue(comment.replyCount >= 0)
        }
    }
    
    @Test
    fun `getComments should return some comments with replies`() = runTest {
        // When
        val comments = repository.getComments("video1")
        
        // Then
        val commentsWithReplies = comments.filter { it.replies.isNotEmpty() }
        assertTrue("应该至少有一个带回复的评论", commentsWithReplies.isNotEmpty())
    }
    
    @Test
    fun `addComment should return new comment`() = runTest {
        // When
        val newComment = repository.addComment("video1", "New comment")
        
        // Then
        assertNotNull(newComment)
        assertEquals("video1", newComment.videoId)
        assertEquals("New comment", newComment.content)
        assertNull(newComment.parentCommentId)
    }
    
    @Test
    fun `addComment with parentCommentId should return reply`() = runTest {
        // When
        val reply = repository.addComment("video1", "Reply comment", "c1")
        
        // Then
        assertNotNull(reply)
        assertEquals("video1", reply.videoId)
        assertEquals("Reply comment", reply.content)
        assertEquals("c1", reply.parentCommentId)
    }
    
    @Test
    fun `likeComment should return true`() = runTest {
        // When
        val result = repository.likeComment("c1")
        
        // Then
        assertTrue(result)
    }
    
    @Test
    fun `unlikeComment should return true`() = runTest {
        // When
        val result = repository.unlikeComment("c1")
        
        // Then
        assertTrue(result)
    }
}

