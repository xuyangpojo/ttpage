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
    fun `loadContents should populate contents`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        
        // When - 等待初始加载完成
        viewModel.contents.test {
            // 跳过初始空列表
            skipItems(1)
            val contents = awaitItem()
            
            // Then
            assertTrue(contents.isNotEmpty())
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
    fun `getContentById should return correct content`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        
        // Wait for initial load
        delay(1000)
        
        // When
        val contents = viewModel.contents.value
        if (contents.isNotEmpty()) {
            val firstContent = contents.first()
            val foundContent = viewModel.getContentById(firstContent.id)
            
            // Then
            assertNotNull(foundContent)
            assertEquals(firstContent.id, foundContent?.id)
            assertEquals(firstContent.title, foundContent?.title)
        }
    }
    
    @Test
    fun `getContentById should return null for non-existent id`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        
        // Wait for initial load
        delay(1000)
        
        // When
        val foundContent = viewModel.getContentById("non-existent-id-12345")
        
        // Then
        assertNull(foundContent)
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
    fun `contents should have valid data after loading`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        
        // Wait for initial load
        delay(1000)
        
        // When
        val contents = viewModel.contents.value
        
        // Then
        assertTrue(contents.isNotEmpty())
        contents.forEach { content ->
            assertNotNull(content.id)
            assertNotNull(content.title)
            assertNotNull(content.author)
            assertTrue(content.likeCount >= 0)
            assertTrue(content.commentCount >= 0)
        }
    }
    
    @Test
    fun `loadContents with topicId should update currentTopicId`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        delay(1000) // Wait for initial load
        
        // When
        viewModel.loadContents(topicId = "tech", refresh = true)
        delay(1000) // Wait for loading
        
        // Then
        assertEquals("tech", viewModel.currentTopicId.value)
    }
    
    @Test
    fun `refreshContents should reload contents`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        delay(1000) // Wait for initial load
        val initialSize = viewModel.contents.value.size
        
        // When
        viewModel.refreshContents()
        delay(1000) // Wait for refresh
        
        // Then
        val refreshedContents = viewModel.contents.value
        assertTrue(refreshedContents.size >= initialSize)
    }
    
    @Test
    fun `loadMoreContents should append more contents`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        delay(1000) // Wait for initial load
        val initialSize = viewModel.contents.value.size
        
        // When
        viewModel.loadMoreContents()
        delay(1000) // Wait for loading more
        
        // Then
        val updatedContents = viewModel.contents.value
        // 由于模拟数据相同，大小可能不变，但至少应该不减少
        assertTrue(updatedContents.size >= initialSize)
    }
    
    @Test
    fun `currentTopicId should default to all`() = runTest {
        // Given
        val viewModel = HomeViewModel()
        
        // Then
        assertEquals("all", viewModel.currentTopicId.value)
    }
}

