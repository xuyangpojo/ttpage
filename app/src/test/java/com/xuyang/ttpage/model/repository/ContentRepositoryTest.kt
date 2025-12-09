package com.xuyang.ttpage.model.repository

import com.xuyang.ttpage.model.data.Content
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/**
 * ContentRepository单元测试
 */
class ContentRepositoryTest {
    
    private val repository = ContentRepository()
    
    @Test
    fun `getRecommendedContents should return non-empty list`() = runTest {
        // When
        val contents = repository.getRecommendedContents()
        
        // Then
        assertNotNull(contents)
        assertTrue(contents.isNotEmpty())
    }
    
    @Test
    fun `getRecommendedContents should return list with valid content`() = runTest {
        // When
        val contents = repository.getRecommendedContents()
        
        // Then
        contents.forEach { content ->
            assertNotNull(content.id)
            assertNotNull(content.title)
            assertNotNull(content.author)
            assertNotNull(content.publishTime)
            assertTrue(content.likeCount >= 0)
            assertTrue(content.commentCount >= 0)
        }
    }
    
    @Test
    fun `getRecommendedContents should return contents with unique ids`() = runTest {
        // When
        val contents = repository.getRecommendedContents()
        
        // Then
        val ids = contents.map { it.id }
        assertEquals(ids.size, ids.distinct().size) // 所有ID应该是唯一的
    }
    
    @Test
    fun `getRecommendedContents should return some contents with video`() = runTest {
        // When
        val contents = repository.getRecommendedContents()
        
        // Then
        val contentsWithVideo = contents.filter { it.hasVideo }
        assertTrue("应该至少有一个带视频的内容", contentsWithVideo.isNotEmpty())
    }
    
    @Test
    fun `getRecommendedContents should return contents with video cover when has video`() = runTest {
        // When
        val contents = repository.getRecommendedContents()
        
        // Then
        contents.filter { it.hasVideo }.forEach { content ->
            assertNotNull("有视频的内容应该有封面", content.videoCover)
            assertFalse("封面URL不应该为空", content.videoCover.isNullOrBlank())
        }
    }
}

