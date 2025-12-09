package com.xuyang.ttpage.model.data

import org.junit.Assert.*
import org.junit.Test

/**
 * Content数据模型单元测试
 */
class ContentTest {
    
    @Test
    fun `hasVideo should return true when videoUrl is not null or blank`() {
        // Given
        val contentWithVideo = Content(
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
        assertTrue(contentWithVideo.hasVideo)
    }
    
    @Test
    fun `hasVideo should return false when videoUrl is null`() {
        // Given
        val contentWithoutVideo = Content(
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
        assertFalse(contentWithoutVideo.hasVideo)
    }
    
    @Test
    fun `hasVideo should return false when videoUrl is blank`() {
        // Given
        val contentWithBlankVideo = Content(
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
        assertFalse(contentWithBlankVideo.hasVideo)
    }
    
    @Test
    fun `content should have all required fields`() {
        // Given & When
        val content = Content(
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
        assertEquals("1", content.id)
        assertEquals("Test Title", content.title)
        assertEquals("Test Author", content.author)
        assertEquals("2小时前", content.publishTime)
        assertEquals(100, content.likeCount)
        assertEquals(50, content.commentCount)
        assertTrue(content.isHot)
        assertEquals("cover1", content.videoCover)
        assertEquals("video1", content.videoUrl)
    }
    
    @Test
    fun `content equals should work correctly`() {
        // Given
        val content1 = Content(
            id = "1",
            title = "Test",
            author = "Author",
            publishTime = "1小时前",
            likeCount = 10,
            commentCount = 5,
            isHot = false
        )
        
        val content2 = Content(
            id = "1",
            title = "Test",
            author = "Author",
            publishTime = "1小时前",
            likeCount = 10,
            commentCount = 5,
            isHot = false
        )
        
        // When & Then
        assertEquals(content1, content2)
    }
}

