package com.xuyang.ttpage.model.data

import org.junit.Assert.*
import org.junit.Test

class CommentTest {
    
    @Test
    fun `comment should have all required fields`() {
        val comment = Comment(
            id = "c001",
            videoId = "v001",
            authorId = "u001",
            authorName = "Test Author",
            content = "Test comment",
            publishTime = "1小时前",
            likeCount = 10u,
            replyCount = 2u
        )
        assertEquals("c001", comment.id)
        assertEquals("v001", comment.videoId)
        assertEquals("u001", comment.authorId)
        assertEquals("Test Author", comment.authorName)
        assertEquals("Test comment", comment.content)
        assertEquals("1小时前", comment.publishTime)
        assertEquals(10u, comment.likeCount)
        assertEquals(2u, comment.replyCount)
        assertNull(comment.parentCommentId)
    }
    
    @Test
    fun `comment with parent should have parentCommentId`() {
        val reply = Comment(
            id = "c002",
            videoId = "v001",
            authorId = "u002",
            authorName = "Reply Author",
            content = "Reply content",
            publishTime = "30分钟前",
            likeCount = 5u,
            replyCount = 0u,
            parentCommentId = "c001"
        )
        assertEquals("c001", reply.parentCommentId)
    }
    
    @Test
    fun `CommentWithReplies should build tree structure correctly`() {
        val parentComment = Comment(
            id = "c001",
            videoId = "v001",
            authorId = "u001",
            authorName = "Parent Author",
            content = "Parent comment",
            publishTime = "1小时前",
            likeCount = 10u,
            replyCount = 2u,
            parentCommentId = null
        )
        
        val reply1 = Comment(
            id = "c002",
            videoId = "v001",
            authorId = "u002",
            authorName = "Reply1",
            content = "Reply 1",
            publishTime = "30分钟前",
            likeCount = 3u,
            replyCount = 0u,
            parentCommentId = "c001"
        )
        
        val reply2 = Comment(
            id = "c003",
            videoId = "v001",
            authorId = "u003",
            authorName = "Reply2",
            content = "Reply 2",
            publishTime = "20分钟前",
            likeCount = 1u,
            replyCount = 0u,
            parentCommentId = "c001"
        )

        val commentWithReplies = CommentWithReplies(
            comment = parentComment,
            replies = listOf(
                CommentWithReplies(comment = reply1),
                CommentWithReplies(comment = reply2)
            )
        )

        assertEquals(parentComment.id, commentWithReplies.comment.id)
        assertEquals(2, commentWithReplies.replies.size)
        assertEquals("c002", commentWithReplies.replies[0].comment.id)
        assertEquals("c003", commentWithReplies.replies[1].comment.id)
    }
}

