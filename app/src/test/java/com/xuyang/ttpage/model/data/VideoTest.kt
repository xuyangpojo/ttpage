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
            author = "Author",
            publishTime = "1小时前",
            likeCount = 10,
            commentCount = 5,
            isHot = false,
            videoUrl = "video1"
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
            author = "Author",
            publishTime = "1小时前",
            likeCount = 10,
            commentCount = 5,
            isHot = false,
            videoUrl = null
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
            author = "Author",
            publishTime = "1小时前",
            likeCount = 10,
            commentCount = 5,
            isHot = false,
            videoUrl = ""
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
            author = "Test Author",
            publishTime = "2小时前",
            likeCount = 100,
            commentCount = 50,
            isHot = true,
            videoCover = "cover1",
            videoUrl = "video1"
        )
        
        // Then
        assertEquals("1", video.id)
        assertEquals("Test Title", video.title)
        assertEquals("Test Author", video.author)
        assertEquals("2小时前", video.publishTime)
        assertEquals(100, video.likeCount)
        assertEquals(50, video.commentCount)
        assertTrue(video.isHot)
        assertEquals("cover1", video.videoCover)
        assertEquals("video1", video.videoUrl)
    }
    
    @Test
    fun `video equals should work correctly`() {
        // Given
        val video1 = Video(
            id = "1",
            title = "Test",
            author = "Author",
            publishTime = "1小时前",
            likeCount = 10,
            commentCount = 5,
            isHot = false
        )
        
        val video2 = Video(
            id = "1",
            title = "Test",
            author = "Author",
            publishTime = "1小时前",
            likeCount = 10,
            commentCount = 5,
            isHot = false
        )
        
        // When & Then
        assertEquals(video1, video2)
    }
}

