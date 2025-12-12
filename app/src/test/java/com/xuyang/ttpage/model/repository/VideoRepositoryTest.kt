package com.xuyang.ttpage.model.repository

import com.xuyang.ttpage.model.data.Video
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class VideoRepositoryTest {
    
    private val repository = VideoRepository()
    
    @Test
    fun `getRecommendedVideos should return non-empty list`() = runTest {
        val videos = repository.getRecommendedVideos()
        assertNotNull(videos)
        assertTrue(videos.isNotEmpty())
    }
    
    @Test
    fun `getRecommendedVideos should return list with valid video`() = runTest {
        val videos = repository.getRecommendedVideos()
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
        val videos = repository.getRecommendedVideos()
        val ids = videos.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }
    
    @Test
    fun `getRecommendedVideos should return some videos with video`() = runTest {
        val videos = repository.getRecommendedVideos()
        val videosWithVideo = videos.filter { it.hasVideo }
        assertTrue("应该至少有一个带视频的视频", videosWithVideo.isNotEmpty())
    }
    
    @Test
    fun `getRecommendedVideos should return videos with video cover when has video`() = runTest {
        val videos = repository.getRecommendedVideos()
        
        videos.filter { it.hasVideo }.forEach { video ->
            assertNotNull("有视频的视频应该有封面", video.videoCover)
            assertFalse("封面URL不应该为空", video.videoCover.isNullOrBlank())
        }
    }
}

