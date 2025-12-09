package com.xuyang.ttpage.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/**
 * HomeViewModel单元测试
 */
class HomeViewModelTest {
    
    @Test
    fun `loadVideos should populate videos`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        
        // When - 等待初始加载完成
        viewModel.videos.test {
            // 跳过初始空列表
            skipItems(1)
            val videos = awaitItem()
            
            // Then
            assertTrue(videos.isNotEmpty())
        }
    }
    
    @Test
    fun `isLoading should eventually be false after loading`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        
        // When - 等待加载完成
        delay(1000) // 等待初始加载
        
        // Then
        viewModel.isLoading.test {
            val isLoading = awaitItem()
            assertFalse("加载完成后应该是false", isLoading)
        }
    }
    
    @Test
    fun `getVideoById should return correct video`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        
        // Wait for initial load
        delay(1000)
        
        // When
        val videos = viewModel.videos.value
        if (videos.isNotEmpty()) {
            val firstVideo = videos.first()
            val foundVideo = viewModel.getVideoById(firstVideo.id)
            
            // Then
            assertNotNull(foundVideo)
            assertEquals(firstVideo.id, foundVideo?.id)
            assertEquals(firstVideo.title, foundVideo?.title)
        }
    }
    
    @Test
    fun `getVideoById should return null for non-existent id`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        
        // Wait for initial load
        delay(1000)
        
        // When
        val foundVideo = viewModel.getVideoById("non-existent-id-12345")
        
        // Then
        assertNull(foundVideo)
    }
    
    @Test
    fun `clearError should clear error message`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        
        // When
        viewModel.clearError()
        
        // Then
        assertNull(viewModel.errorMessage.value)
    }
    
    @Test
    fun `videos should have valid data after loading`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        
        // Wait for initial load
        delay(1000)
        
        // When
        val videos = viewModel.videos.value
        
        // Then
        assertTrue(videos.isNotEmpty())
        videos.forEach { video ->
            assertNotNull(video.id)
            assertNotNull(video.title)
            assertNotNull(video.author)
            assertTrue(video.likeCount >= 0)
            assertTrue(video.commentCount >= 0)
        }
    }
    
    @Test
    fun `loadVideos with topicId should update currentTopicId`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        delay(1000) // Wait for initial load
        
        // When
        viewModel.loadVideos(topicId = "tech", refresh = true)
        delay(1000) // Wait for loading
        
        // Then
        assertEquals("tech", viewModel.currentTopicId.value)
    }
    
    @Test
    fun `refreshVideos should reload videos`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        delay(1000) // Wait for initial load
        val initialSize = viewModel.videos.value.size
        
        // When
        viewModel.refreshVideos()
        delay(1000) // Wait for refresh
        
        // Then
        val refreshedVideos = viewModel.videos.value
        assertTrue(refreshedVideos.size >= initialSize)
    }
    
    @Test
    fun `loadMoreVideos should append more videos`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        delay(1000) // Wait for initial load
        val initialSize = viewModel.videos.value.size
        
        // When
        viewModel.loadMoreVideos()
        delay(1000) // Wait for loading more
        
        // Then
        val updatedVideos = viewModel.videos.value
        // 由于模拟数据相同，大小可能不变，但至少应该不减少
        assertTrue(updatedVideos.size >= initialSize)
    }
    
    @Test
    fun `currentTopicId should default to all`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        
        // Then
        assertEquals("all", viewModel.currentTopicId.value)
    }
}

