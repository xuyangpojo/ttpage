package com.xuyang.ttpage.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/**
 * TopicViewModel单元测试
 */
class TopicViewModelTest {
    
    @Test
    fun `initial state should have empty topics`() = runTest {
        // Given
        val viewModel = TopicViewModel()
        
        // When & Then
        viewModel.topics.test {
            val topics = awaitItem()
            assertTrue(topics.isEmpty())
        }
    }
    
    @Test
    fun `loadTopics should populate topics`() = runTest {
        // Given
        val viewModel = TopicViewModel()
        
        // Wait for loading
        delay(500)
        
        // Then
        val topics = viewModel.topics.value
        assertTrue(topics.isNotEmpty())
        assertEquals("all", topics.first().id)
        assertEquals("全部", topics.first().name)
    }
    
    @Test
    fun `selectTopic should update selectedTopicId`() = runTest {
        // Given
        val viewModel = TopicViewModel()
        delay(500) // Wait for topics to load
        
        // When
        viewModel.selectTopic("tech")
        
        // Then
        viewModel.selectedTopicId.test {
            val selectedId = awaitItem()
            assertEquals("tech", selectedId)
        }
    }
    
    @Test
    fun `getSelectedTopic should return correct topic`() = runTest {
        // Given
        val viewModel = TopicViewModel()
        delay(500) // Wait for topics to load
        
        // When
        viewModel.selectTopic("tech")
        
        // Then
        val selectedTopic = viewModel.getSelectedTopic()
        assertNotNull(selectedTopic)
        assertEquals("tech", selectedTopic?.id)
        assertEquals("科技", selectedTopic?.name)
    }
    
    @Test
    fun `selectTopic with non-existent id should still update selectedTopicId`() = runTest {
        // Given
        val viewModel = TopicViewModel()
        delay(500) // Wait for topics to load
        
        // When
        viewModel.selectTopic("non-existent")
        
        // Then
        viewModel.selectedTopicId.test {
            val selectedId = awaitItem()
            assertEquals("non-existent", selectedId)
        }
    }
    
    @Test
    fun `getSelectedTopic with non-existent id should return null`() = runTest {
        // Given
        val viewModel = TopicViewModel()
        delay(500) // Wait for topics to load
        
        // When
        viewModel.selectTopic("non-existent")
        
        // Then
        val selectedTopic = viewModel.getSelectedTopic()
        assertNull(selectedTopic)
    }
}

