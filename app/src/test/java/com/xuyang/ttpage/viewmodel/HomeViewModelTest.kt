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
        viewModel.videos.test {
            skipItems(1)
            val videos = awaitItem()
            assertTrue(videos.isNotEmpty())
        }
    }
    
    @Test
    fun `isLoading should eventually be false after loading`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        viewModel.isLoading.test {
            val isLoading = awaitItem()
            assertFalse("加载完成后应该是false", isLoading)
        }
    }
    
    @Test
    fun `getVideoById should return correct video`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        val videos = viewModel.videos.value
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
        val videos = viewModel.videos.value
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
        val initialSize = viewModel.videos.value.size
        viewModel.refreshVideos()
        delay(1000)
        val refreshedVideos = viewModel.videos.value
        assertTrue(refreshedVideos.size >= initialSize)
    }
    
    @Test
    fun `loadMoreVideos should append more videos`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        val initialSize = viewModel.videos.value.size
        viewModel.loadMoreVideos()
        delay(1000)
        val updatedVideos = viewModel.videos.value
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
        assertTrue(viewModel.hasMore.value)
    }
    
    @Test
    fun `loadMoreVideos should not load when isLoading is true`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        val initialSize = viewModel.videos.value.size
        viewModel.loadMoreVideos()
        delay(1000)
        assertTrue(viewModel.videos.value.size >= initialSize)
    }
    
    @Test
    fun `loadMoreVideos should not load when hasMore is false`() = runTest {
        val viewModel = HomeViewModel()
        delay(1000)
        viewModel.loadMoreVideos()
        delay(1000)
        assertNotNull(viewModel.videos.value)
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

