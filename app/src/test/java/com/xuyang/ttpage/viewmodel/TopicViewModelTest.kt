package com.xuyang.ttpage.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class TopicViewModelTest {
    
    @Test
    fun `initial state should have empty topics`() = runTest {
        val viewModel = TopicViewModel()
        viewModel.topics.test {
            val topics = awaitItem()
            assertTrue(topics.isEmpty())
        }
    }
    
    @Test
    fun `loadTopics should populate topics`() = runTest {
        val viewModel = TopicViewModel() 
        delay(500)
        val topics = viewModel.topics.value
        assertTrue(topics.isNotEmpty())
        assertEquals("all", topics.first().id)
        assertEquals("全部", topics.first().name)
    }
    
    @Test
    fun `selectTopic should update selectedTopicId`() = runTest {
        val viewModel = TopicViewModel()
        delay(500)
        viewModel.selectTopic("tech")
        viewModel.selectedTopicId.test {
            val selectedId = awaitItem()
            assertEquals("tech", selectedId)
        }
    }
    
    @Test
    fun `getSelectedTopic should return correct topic`() = runTest {
        val viewModel = TopicViewModel()
        delay(500)
        viewModel.selectTopic("tech")
        val selectedTopic = viewModel.getSelectedTopic()
        assertNotNull(selectedTopic)
        assertEquals("tech", selectedTopic?.id)
        assertEquals("科技", selectedTopic?.name)
    }
    
    @Test
    fun `selectTopic with non-existent id should still update selectedTopicId`() = runTest {
        val viewModel = TopicViewModel()
        delay(500)
        viewModel.selectTopic("non-existent")
        viewModel.selectedTopicId.test {
            val selectedId = awaitItem()
            assertEquals("non-existent", selectedId)
        }
    }
    
    @Test
    fun `getSelectedTopic with non-existent id should return null`() = runTest {
        val viewModel = TopicViewModel()
        delay(500)
        viewModel.selectTopic("non-existent")
        val selectedTopic = viewModel.getSelectedTopic()
        assertNull(selectedTopic)
    }
    
    @Test
    fun `isLoading should be false after topics are loaded`() = runTest {
        val viewModel = TopicViewModel()
        delay(500)
        assertFalse(viewModel.isLoading.value)
    }
    
    @Test
    fun `topics should contain all topic`() = runTest {
        val viewModel = TopicViewModel()
        delay(500)
        val topics = viewModel.topics.value
        assertTrue(topics.isNotEmpty())
        val allTopic = topics.find { it.id == "all" }
        assertNotNull(allTopic)
        assertEquals("全部", allTopic?.name)
    }
    
    @Test
    fun `selectTopic should update selectedTopicId immediately`() = runTest {
        val viewModel = TopicViewModel()
        delay(500)
        viewModel.selectTopic("tech")
        assertEquals("tech", viewModel.selectedTopicId.value)
    }
}

