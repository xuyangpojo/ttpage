package com.xuyang.ttpage.model.repository

import com.xuyang.ttpage.model.data.Video
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/**
 * VideoRepository单元测试
 */
class VideoRepositoryTest {
    
    private val repository = VideoRepository()
    
    @Test
    fun `getRecommendedVideos should return non-empty list`() = runTest {
        // When
        val videos = repository.getRecommendedVideos()
        
        // Then
        assertNotNull(videos)
        assertTrue(videos.isNotEmpty())
    }
    
    @Test
    fun `getRecommendedVideos should return list with valid video`() = runTest {
        // When
        val videos = repository.getRecommendedVideos()
        
        // Then
        videos.forEach { video ->
            assertNotNull(video.id)
            assertNotNull(video.title)
            assertNotNull(video.authorId)
            assertNotNull(video.authorName)
            assertNotNull(video.publishTime)
            assertTrue(video.likeCount >= 0u)
            assertTrue(video.commentCount >= 0u)
        }
    }
    
    @Test
    fun `getRecommendedVideos should return videos with unique ids`() = runTest {
        // When
        val videos = repository.getRecommendedVideos()
        
        // Then
        val ids = videos.map { it.id }
        assertEquals(ids.size, ids.distinct().size) // 所有ID应该是唯一的
    }
    
    @Test
    fun `getRecommendedVideos should return some videos with video`() = runTest {
        // When
        val videos = repository.getRecommendedVideos()
        
        // Then
        val videosWithVideo = videos.filter { it.hasVideo }
        assertTrue("应该至少有一个带视频的视频", videosWithVideo.isNotEmpty())
    }
    
    @Test
    fun `getRecommendedVideos should return videos with video cover when has video`() = runTest {
        // When
        val videos = repository.getRecommendedVideos()
        
        // Then
        videos.filter { it.hasVideo }.forEach { video ->
            assertNotNull("有视频的视频应该有封面", video.videoCover)
            assertFalse("封面URL不应该为空", video.videoCover.isNullOrBlank())
        }
    }
}

