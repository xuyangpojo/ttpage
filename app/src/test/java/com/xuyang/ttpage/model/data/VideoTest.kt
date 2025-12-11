package com.xuyang.ttpage.model.data

import org.junit.Assert.*
import org.junit.Test

/**
 * Video数据模型单元测试
 */
class VideoTest {
    
    @Test
    fun `hasVideo should return true when videoUrl is not null or blank`() {
        // Given
        val videoWithVideo = Video(
            id = "1",
            title = "Test",
            authorId = "u001",
            authorName = "Author",
            publishTime = "1小时前",
            likeCount = 10u,
            commentCount = 5u,
            isHot = false,
            videoUrl = "video1",
            topics = listOf("tech")
        )
        
        // When & Then
        assertTrue(videoWithVideo.hasVideo)
    }
    
    @Test
    fun `hasVideo should return false when videoUrl is null`() {
        // Given
        val videoWithoutVideo = Video(
            id = "1",
            title = "Test",
            authorId = "u001",
            authorName = "Author",
            publishTime = "1小时前",
            likeCount = 10u,
            commentCount = 5u,
            isHot = false,
            videoUrl = null,
            topics = listOf("tech")
        )
        
        // When & Then
        assertFalse(videoWithoutVideo.hasVideo)
    }
    
    @Test
    fun `hasVideo should return false when videoUrl is blank`() {
        // Given
        val videoWithBlankVideo = Video(
            id = "1",
            title = "Test",
            authorId = "u001",
            authorName = "Author",
            publishTime = "1小时前",
            likeCount = 10u,
            commentCount = 5u,
            isHot = false,
            videoUrl = "",
            topics = listOf("tech")
        )
        
        // When & Then
        assertFalse(videoWithBlankVideo.hasVideo)
    }
    
    @Test
    fun `video should have all required fields`() {
        // Given & When
        val video = Video(
            id = "1",
            title = "Test Title",
            authorId = "u001",
            authorName = "Test Author",
            publishTime = "2小时前",
            likeCount = 100u,
            commentCount = 50u,
            isHot = true,
            videoCover = "cover1",
            videoUrl = "video1",
            topics = listOf("tech", "hot")
        )
        
        // Then
        assertEquals("1", video.id)
        assertEquals("Test Title", video.title)
        assertEquals("u001", video.authorId)
        assertEquals("Test Author", video.authorName)
        assertEquals("2小时前", video.publishTime)
        assertEquals(100u, video.likeCount)
        assertEquals(50u, video.commentCount)
        assertTrue(video.isHot)
        assertEquals("cover1", video.videoCover)
        assertEquals("video1", video.videoUrl)
        assertEquals(listOf("tech", "hot"), video.topics)
    }
    
    @Test
    fun `video equals should work correctly`() {
        // Given
        val video1 = Video(
            id = "1",
            title = "Test",
            authorId = "u001",
            authorName = "Author",
            publishTime = "1小时前",
            likeCount = 10u,
            commentCount = 5u,
            isHot = false,
            topics = listOf("tech")
        )
        
        val video2 = Video(
            id = "1",
            title = "Test",
            authorId = "u001",
            authorName = "Author",
            publishTime = "1小时前",
            likeCount = 10u,
            commentCount = 5u,
            isHot = false,
            topics = listOf("tech")
        )
        
        // When & Then
        assertEquals(video1, video2)
    }
}

