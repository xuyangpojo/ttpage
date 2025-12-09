package com.xuyang.ttpage.model.data

import org.junit.Assert.*
import org.junit.Test

/**
 * Comment数据模型单元测试
 */
class CommentTest {
    
    @Test
    fun `comment should have all required fields`() {
        // Given & When
        val comment = Comment(
            id = "c1",
            videoId = "video1",
            authorId = "author1",
            author = "Test Author",
            content = "Test comment",
            publishTime = "1小时前",
            likeCount = 10,
            replyCount = 2
        )
        
        // Then
        assertEquals("c1", comment.id)
        assertEquals("video1", comment.videoId)
        assertEquals("Test Author", comment.author)
        assertEquals("Test comment", comment.content)
        assertEquals("1小时前", comment.publishTime)
        assertEquals(10, comment.likeCount)
        assertEquals(2, comment.replyCount)
        assertNull(comment.parentCommentId)
        assertTrue(comment.replies.isEmpty())
    }
    
    @Test
    fun `comment with parent should have parentCommentId`() {
        // Given & When
        val reply = Comment(
            id = "c1-1",
            videoId = "video1",
            author = "Reply Author",
            content = "Reply content",
            publishTime = "30分钟前",
            likeCount = 5,
            replyCount = 0,
            parentCommentId = "c1"
        )
        
        // Then
        assertEquals("c1", reply.parentCommentId)
    }
    
    @Test
    fun `comment with replies should have replies list`() {
        // Given
        val reply1 = Comment(
            id = "c1-1",
            videoId = "video1",
            author = "Reply1",
            content = "Reply 1",
            publishTime = "30分钟前",
            likeCount = 3,
            replyCount = 0,
            parentCommentId = "c1"
        )
        
        val reply2 = Comment(
            id = "c1-2",
            videoId = "video1",
            author = "Reply2",
            content = "Reply 2",
            publishTime = "20分钟前",
            likeCount = 1,
            replyCount = 0,
            parentCommentId = "c1"
        )
        
        // When
        val comment = Comment(
            id = "c1",
            videoId = "video1",
            authorId = "author1",
            author = "Author",
            content = "Comment",
            publishTime = "1小时前",
            likeCount = 10,
            replyCount = 2,
            replies = listOf(reply1, reply2)
        )
        
        // Then
        assertEquals(2, comment.replies.size)
        assertEquals("c1-1", comment.replies[0].id)
        assertEquals("c1-2", comment.replies[1].id)
    }
}

