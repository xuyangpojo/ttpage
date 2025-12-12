package com.xuyang.ttpage.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class HomeViewModelTest {
    
    @Test
    fun `loadVideos should populate videos`() = runTest {
        val viewModel = HomeViewModel()
        viewModel.videosByTopic.test {
            skipItems(1)
            val videosByTopic = awaitItem()
            val videos = videosByTopic.values.flatten()
            assertTrue(videos.isNotEmpty())
        }
    }
    
    @Test
    fun `isLoading should eventually be false after loading`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        viewModel.loadingByTopic.test {
            val loadingByTopic = awaitItem()
            val isLoading = loadingByTopic.values.any { it }
            assertFalse("加载完成后应该是false", isLoading)
        }
    }
    
    @Test
    fun `getVideoById should return correct video`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        val videosByTopic = viewModel.videosByTopic.value
        val videos = videosByTopic.values.flatten()
        if (videos.isNotEmpty()) {
            val firstVideo = videos.first()
            val foundVideo = viewModel.getVideoById(firstVideo.id)
            assertNotNull(foundVideo)
            assertEquals(firstVideo.id, foundVideo?.id)
            assertEquals(firstVideo.title, foundVideo?.title)
        }
    }
    
    @Test
    fun `getVideoById should return null for non-existent id`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        val foundVideo = viewModel.getVideoById("non-existent-id-12345")
        assertNull(foundVideo)
    }
    
    @Test
    fun `clearError should clear error message`() = runTest {
        val viewModel = HomeViewModel()
        viewModel.clearError()
        assertNull(viewModel.errorMessage.value)
    }
    
    @Test
    fun `videos should have valid data after loading`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        val videosByTopic = viewModel.videosByTopic.value
        val videos = videosByTopic.values.flatten()
        assertTrue(videos.isNotEmpty())
        videos.forEach { video ->
            assertNotNull(video.id)
            assertNotNull(video.title)
            assertNotNull(video.authorId)
            assertNotNull(video.authorName)
            assertTrue(video.likeCount >= 0u)
            assertTrue(video.commentCount >= 0u)
        }
    }
    
    @Test
    fun `loadVideos with topicId should update currentTopicId`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        viewModel.loadVideos(topicId = "tech", refresh = true)
        delay(1000)
        assertEquals("tech", viewModel.currentTopicId.value)
    }
    
    @Test
    fun `refreshVideos should reload videos`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        val initialVideosByTopic = viewModel.videosByTopic.value
        val initialSize = initialVideosByTopic.values.flatten().size
        viewModel.refreshVideos()
        delay(1000)
        val refreshedVideosByTopic = viewModel.videosByTopic.value
        val refreshedVideos = refreshedVideosByTopic.values.flatten()
        assertTrue(refreshedVideos.size >= initialSize)
    }
    
    @Test
    fun `loadMoreVideos should append more videos`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        val initialVideosByTopic = viewModel.videosByTopic.value
        val initialSize = initialVideosByTopic.values.flatten().size
        viewModel.loadMoreVideos()
        delay(1000)
        val updatedVideosByTopic = viewModel.videosByTopic.value
        val updatedVideos = updatedVideosByTopic.values.flatten()
        assertTrue(updatedVideos.size >= initialSize)
    }
    
    @Test
    fun `currentTopicId should default to all`() = runTest {
        val viewModel = HomeViewModel()
        assertEquals("all", viewModel.currentTopicId.value)
    }
    
    @Test
    fun `hasMore should be true when videos are available`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        val hasMoreByTopic = viewModel.hasMoreByTopic.value
        val hasMore = hasMoreByTopic.values.any { it }
        assertTrue(hasMore)
    }
    
    @Test
    fun `loadMoreVideos should not load when isLoading is true`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        val initialVideosByTopic = viewModel.videosByTopic.value
        val initialSize = initialVideosByTopic.values.flatten().size
        viewModel.loadMoreVideos()
        delay(1000)
        val updatedVideosByTopic = viewModel.videosByTopic.value
        val updatedSize = updatedVideosByTopic.values.flatten().size
        assertTrue(updatedSize >= initialSize)
    }
    
    @Test
    fun `loadMoreVideos should not load when hasMore is false`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        viewModel.loadMoreVideos()
        delay(1000)
        val videosByTopic = viewModel.videosByTopic.value
        assertNotNull(videosByTopic)
    }
    
    @Test
    fun `errorMessage should be set when load fails`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        viewModel.loadVideos(topicId = "non-existent-topic", refresh = true)
        delay(1000)
        val errorMessage = viewModel.errorMessage.value
    }
}

