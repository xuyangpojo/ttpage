package com.xuyang.ttpage.model.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/**
 * FavoriteRepository单元测试
 */
class FavoriteRepositoryTest {
    
    private val repository = FavoriteRepository()
    
    @Test
    fun `addFavorite should add content to favorites`() = runTest {
        // Given
        val contentId = "content1"
        
        // When
        val result = repository.addFavorite(contentId)
        
        // Then
        assertTrue(result)
        assertTrue(repository.isFavorite(contentId))
    }
    
    @Test
    fun `addFavorite should return false if already favorite`() = runTest {
        // Given
        val contentId = "content1"
        repository.addFavorite(contentId)
        delay(100)
        
        // When
        val result = repository.addFavorite(contentId)
        
        // Then
        assertFalse(result) // Already exists
    }
    
    @Test
    fun `removeFavorite should remove content from favorites`() = runTest {
        // Given
        val contentId = "content1"
        repository.addFavorite(contentId)
        delay(100)
        assertTrue(repository.isFavorite(contentId))
        
        // When
        val result = repository.removeFavorite(contentId)
        
        // Then
        assertTrue(result)
        assertFalse(repository.isFavorite(contentId))
    }
    
    @Test
    fun `removeFavorite should return false if not favorite`() = runTest {
        // Given
        val contentId = "content1"
        
        // When
        val result = repository.removeFavorite(contentId)
        
        // Then
        assertFalse(result)
    }
    
    @Test
    fun `getFavoriteIds should return all favorite ids`() = runTest {
        // Given
        repository.addFavorite("content1")
        repository.addFavorite("content2")
        repository.addFavorite("content3")
        delay(200)
        
        // When
        val favoriteIds = repository.getFavoriteIds()
        
        // Then
        assertEquals(3, favoriteIds.size)
        assertTrue(favoriteIds.contains("content1"))
        assertTrue(favoriteIds.contains("content2"))
        assertTrue(favoriteIds.contains("content3"))
    }
    
    @Test
    fun `addHistory should add content to history`() = runTest {
        // Given
        val contentId = "content1"
        
        // When
        repository.addHistory(contentId)
        delay(100)
        
        // Then
        val historyIds = repository.getHistoryIds()
        assertTrue(historyIds.contains(contentId))
        assertEquals(contentId, historyIds.first()) // Should be at the top
    }
    
    @Test
    fun `addHistory should move existing content to top`() = runTest {
        // Given
        repository.addHistory("content1")
        repository.addHistory("content2")
        delay(200)
        
        // When - Add content1 again
        repository.addHistory("content1")
        delay(100)
        
        // Then
        val historyIds = repository.getHistoryIds()
        assertEquals("content1", historyIds.first())
        assertEquals(2, historyIds.size) // Should not duplicate
    }
    
    @Test
    fun `addHistory should limit history to 100 items`() = runTest {
        // Given & When - Add 101 items
        repeat(101) {
            repository.addHistory("content$it")
        }
        delay(200)
        
        // Then
        val historyIds = repository.getHistoryIds()
        assertEquals(100, historyIds.size)
    }
    
    @Test
    fun `clearHistory should remove all history`() = runTest {
        // Given
        repository.addHistory("content1")
        repository.addHistory("content2")
        delay(200)
        
        // When
        repository.clearHistory()
        delay(100)
        
        // Then
        val historyIds = repository.getHistoryIds()
        assertTrue(historyIds.isEmpty())
    }
    
    @Test
    fun `removeHistory should remove specific history item`() = runTest {
        // Given
        repository.addHistory("content1")
        repository.addHistory("content2")
        delay(200)
        
        // When
        repository.removeHistory("content1")
        delay(100)
        
        // Then
        val historyIds = repository.getHistoryIds()
        assertFalse(historyIds.contains("content1"))
        assertTrue(historyIds.contains("content2"))
    }
}

