package com.xuyang.ttpage.model.repository

import com.xuyang.ttpage.model.data.Comment
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class CommentRepositoryTest {
    
    private val repository = CommentRepository()
    
    @Test
    fun `getComments should return non-empty list`() = runTest {
        val comments = repository.getComments("video1")
        assertNotNull(comments)
        assertTrue(comments.isNotEmpty())
    }
    
    @Test
    fun `getComments should return comments with valid data`() = runTest {
        val comments = repository.getComments("video1")
        comments.forEach { comment ->
            assertNotNull(comment.id)
            assertNotNull(comment.videoId)
            assertNotNull(comment.authorName)
            assertNotNull(comment.content)
            assertNotNull(comment.publishTime)
            assertTrue(comment.likeCount >= 0u)
            assertTrue(comment.replyCount >= 0u)
        }
    }
    
    @Test
    fun `getComments should return flat list with parent-child relationships`() = runTest {
        val comments = repository.getComments("video1")
        val topLevelComments = comments.filter { it.parentCommentId == null }
        val replies = comments.filter { it.parentCommentId != null }
        assertTrue("应该至少有一个顶级评论", topLevelComments.isNotEmpty())
        assertTrue("应该至少有一个回复", replies.isNotEmpty())
        replies.forEach { reply ->
            assertNotNull(reply.parentCommentId)
            val parentExists = comments.any { it.id == reply.parentCommentId }
            assertTrue("回复的父评论应该存在", parentExists)
        }
    }
    
    @Test
    fun `addComment should return new comment`() = runTest {
        val newComment = repository.addComment("video1", "New comment")
        assertNotNull(newComment)
        assertNotNull(newComment.videoId)
        assertEquals("New comment", newComment.content)
        assertNull(newComment.parentCommentId)
    }
    
    @Test
    fun `addComment with parentCommentId should return reply`() = runTest {
        val reply = repository.addComment("video1", "Reply comment", "c001")
        assertNotNull(reply)
        assertNotNull(reply.videoId)
        assertEquals("Reply comment", reply.content)
        assertEquals("c001", reply.parentCommentId)
    }
    
    @Test
    fun `likeComment should return true`() = runTest {
        val result = repository.likeComment("c001")
        assertTrue(result)
    }
    
    @Test
    fun `unlikeComment should return true`() = runTest {
        val result = repository.unlikeComment("c001")
        assertTrue(result)
    }
    
    @Test
    fun `getComments should return comments with correct videoId`() = runTest {
        val comments = repository.getComments("video1")
        comments.forEach { comment ->
            assertEquals("video1", comment.videoId)
        }
    }
    
    @Test
    fun `getComments should return comments with unique ids`() = runTest {
        val comments = repository.getComments("video1")
        val ids = comments.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }
}

