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
            id = 1L,
            videoId = 1L,
            authorId = 1L,
            authorName = "Test Author",
            content = "Test comment",
            publishTime = "1小时前",
            likeCount = 10u,
            replyCount = 2u
        )
        
        // Then
        assertEquals(1L, comment.id)
        assertEquals(1L, comment.videoId)
        assertEquals(1L, comment.authorId)
        assertEquals("Test Author", comment.authorName)
        assertEquals("Test comment", comment.content)
        assertEquals("1小时前", comment.publishTime)
        assertEquals(10u, comment.likeCount)
        assertEquals(2u, comment.replyCount)
        assertNull(comment.parentCommentId)
    }
    
    @Test
    fun `comment with parent should have parentCommentId`() {
        // Given & When
        val reply = Comment(
            id = 2L,
            videoId = 1L,
            authorId = 2L,
            authorName = "Reply Author",
            content = "Reply content",
            publishTime = "30分钟前",
            likeCount = 5u,
            replyCount = 0u,
            parentCommentId = "1"
        )
        
        // Then
        assertEquals("1", reply.parentCommentId)
    }
    
    @Test
    fun `CommentWithReplies should build tree structure correctly`() {
        // Given
        val parentComment = Comment(
            id = 1L,
            videoId = 1L,
            authorId = 1L,
            authorName = "Parent Author",
            content = "Parent comment",
            publishTime = "1小时前",
            likeCount = 10u,
            replyCount = 2u,
            parentCommentId = null
        )
        
        val reply1 = Comment(
            id = 2L,
            videoId = 1L,
            authorId = 2L,
            authorName = "Reply1",
            content = "Reply 1",
            publishTime = "30分钟前",
            likeCount = 3u,
            replyCount = 0u,
            parentCommentId = "1"
        )
        
        val reply2 = Comment(
            id = 3L,
            videoId = 1L,
            authorId = 3L,
            authorName = "Reply2",
            content = "Reply 2",
            publishTime = "20分钟前",
            likeCount = 1u,
            replyCount = 0u,
            parentCommentId = "1"
        )
        
        // When
        val commentWithReplies = CommentWithReplies(
            comment = parentComment,
            replies = listOf(
                CommentWithReplies(comment = reply1),
                CommentWithReplies(comment = reply2)
            )
        )
        
        // Then
        assertEquals(parentComment.id, commentWithReplies.comment.id)
        assertEquals(2, commentWithReplies.replies.size)
        assertEquals(2L, commentWithReplies.replies[0].comment.id)
        assertEquals(3L, commentWithReplies.replies[1].comment.id)
    }
}

