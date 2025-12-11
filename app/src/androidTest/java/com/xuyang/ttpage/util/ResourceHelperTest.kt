package com.xuyang.ttpage.util

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test

/**
 * ResourceHelper集成测试（需要Android Context）
 */
class ResourceHelperTest {
    
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    
    @Test
    fun `getRawResourceUri should return valid URI format`() {
        // Given
        val resourceName = "video1"
        
        // When
        val uri = ResourceHelper.getRawResourceUri(context, resourceName)
        
        // Then
        assertNotNull(uri)
        assertTrue(uri.startsWith("android.resource://"))
        assertTrue(uri.contains(context.packageName))
    }
    
    @Test
    fun `getDrawableResourceUri should return valid URI format`() {
        // Given
        val resourceName = "ic_launcher_foreground"
        
        // When
        val uri = ResourceHelper.getDrawableResourceUri(context, resourceName)
        
        // Then
        assertNotNull(uri)
        assertTrue(uri.startsWith("android.resource://"))
        assertTrue(uri.contains(context.packageName))
    }
    
    @Test
    fun `getRawResourceUri should handle non-existent resource gracefully`() {
        // Given
        val nonExistentResource = "non_existent_resource_12345"
        
        // When
        val uri = ResourceHelper.getRawResourceUri(context, nonExistentResource)
        
        // Then - 资源不存在时应该返回null
        assertNull(uri)
    }
}

