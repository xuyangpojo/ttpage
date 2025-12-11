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
            assertNotNull(comment.authorName)
            assertNotNull(comment.content)
            assertNotNull(comment.publishTime)
            assertTrue(comment.likeCount >= 0)
            assertTrue(comment.replyCount >= 0)
        }
    }
    
    @Test
    fun `getComments should return flat list with parent-child relationships`() = runTest {
        // When
        val comments = repository.getComments("video1")
        
        // Then
        // 应该包含顶级评论和回复
        val topLevelComments = comments.filter { it.parentCommentId == null }
        val replies = comments.filter { it.parentCommentId != null }
        assertTrue("应该至少有一个顶级评论", topLevelComments.isNotEmpty())
        assertTrue("应该至少有一个回复", replies.isNotEmpty())
        
        // 验证回复的parentCommentId指向存在的顶级评论
        replies.forEach { reply ->
            assertNotNull(reply.parentCommentId)
            val parentExists = comments.any { it.id == reply.parentCommentId }
            assertTrue("回复的父评论应该存在", parentExists)
        }
    }
    
    @Test
    fun `addComment should return new comment`() = runTest {
        // When
        val newComment = repository.addComment("video1", "New comment")
        
        // Then
        assertNotNull(newComment)
        assertNotNull(newComment.videoId)
        assertEquals("New comment", newComment.content)
        assertNull(newComment.parentCommentId)
    }
    
    @Test
    fun `addComment with parentCommentId should return reply`() = runTest {
        // When
        val reply = repository.addComment("video1", "Reply comment", "c001")
        
        // Then
        assertNotNull(reply)
        assertNotNull(reply.videoId)
        assertEquals("Reply comment", reply.content)
        assertEquals("c001", reply.parentCommentId)
    }
    
    @Test
    fun `likeComment should return true`() = runTest {
        // When
        val result = repository.likeComment("c001")
        
        // Then
        assertTrue(result)
    }
    
    @Test
    fun `unlikeComment should return true`() = runTest {
        // When
        val result = repository.unlikeComment("c001")
        
        // Then
        assertTrue(result)
    }
    
    @Test
    fun `getComments should return comments with correct videoId`() = runTest {
        // When
        val comments = repository.getComments("video1")
        
        // Then
        comments.forEach { comment ->
            assertEquals("video1", comment.videoId)
        }
    }
    
    @Test
    fun `getComments should return comments with unique ids`() = runTest {
        // When
        val comments = repository.getComments("video1")
        
        // Then
        val ids = comments.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }
}

