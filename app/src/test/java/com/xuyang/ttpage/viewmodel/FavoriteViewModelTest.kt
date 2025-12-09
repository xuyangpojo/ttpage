package com.xuyang.ttpage.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/**
 * FavoriteViewModel单元测试
 */
class FavoriteViewModelTest {
    
    @Test
    fun `initial state should have empty favorites and history`() = runTest {
        // Given
        val viewModel = FavoriteViewModel()
        
        // When & Then
        viewModel.favoriteIds.test {
            val favoriteIds = awaitItem()
            assertTrue(favoriteIds.isEmpty())
        }
        
        viewModel.historyIds.test {
            val historyIds = awaitItem()
            assertTrue(historyIds.isEmpty())
        }
    }
    
    @Test
    fun `toggleFavorite should add favorite when not favorite`() = runTest {
        // Given
        val viewModel = FavoriteViewModel()
        delay(500) // Wait for initialization
        val contentId = "content1"
        
        // When
        viewModel.toggleFavorite(contentId)
        delay(500) // Wait for operation
        
        // Then
        assertTrue(viewModel.isFavorite(contentId))
    }
    
    @Test
    fun `toggleFavorite should remove favorite when already favorite`() = runTest {
        // Given
        val viewModel = FavoriteViewModel()
        delay(500) // Wait for initialization
        val contentId = "content1"
        
        // When - Add favorite first
        viewModel.toggleFavorite(contentId)
        delay(500)
        assertTrue(viewModel.isFavorite(contentId))
        
        // Then - Remove favorite
        viewModel.toggleFavorite(contentId)
        delay(500)
        assertFalse(viewModel.isFavorite(contentId))
    }
    
    @Test
    fun `addHistory should add content to history`() = runTest {
        // Given
        val viewModel = FavoriteViewModel()
        delay(500) // Wait for initialization
        val contentId = "content1"
        
        // When
        viewModel.addHistory(contentId)
        delay(500) // Wait for operation
        
        // Then
        viewModel.historyIds.test {
            val historyIds = awaitItem()
            assertTrue(historyIds.contains(contentId))
            assertEquals(0, historyIds.indexOf(contentId)) // Should be at the beginning
        }
    }
    
    @Test
    fun `addHistory should move existing content to top`() = runTest {
        // Given
        val viewModel = FavoriteViewModel()
        delay(500) // Wait for initialization
        val contentId1 = "content1"
        val contentId2 = "content2"
        
        // When - Add two contents
        viewModel.addHistory(contentId1)
        delay(200)
        viewModel.addHistory(contentId2)
        delay(200)
        viewModel.addHistory(contentId1) // Add content1 again
        
        delay(500) // Wait for operations
        
        // Then - content1 should be at the top
        val historyIds = viewModel.historyIds.value
        assertEquals(contentId1, historyIds.first())
        assertTrue(historyIds.contains(contentId2))
    }
    
    @Test
    fun `clearHistory should remove all history`() = runTest {
        // Given
        val viewModel = FavoriteViewModel()
        delay(500) // Wait for initialization
        viewModel.addHistory("content1")
        viewModel.addHistory("content2")
        delay(500)
        
        // When
        viewModel.clearHistory()
        delay(500)
        
        // Then
        val historyIds = viewModel.historyIds.value
        assertTrue(historyIds.isEmpty())
    }
    
    @Test
    fun `isFavorite should return false for non-favorite content`() = runTest {
        // Given
        val viewModel = FavoriteViewModel()
        delay(500) // Wait for initialization
        
        // When & Then
        assertFalse(viewModel.isFavorite("non-existent"))
    }
}

