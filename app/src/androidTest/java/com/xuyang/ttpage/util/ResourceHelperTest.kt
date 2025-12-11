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
    fun testGetRawResourceUri_shouldReturnValidUriFormat() {
        // Given
        val resourceName = "video1"
        
        // When
        val uri = ResourceHelper.getRawResourceUri(context, resourceName)
        
        // Then
        assertNotNull(uri)
        uri?.let {
            assertTrue(it.startsWith("android.resource://"))
            assertTrue(it.contains(context.packageName))
        }
    }
    
    @Test
    fun testGetDrawableResourceUri_shouldReturnValidUriFormat() {
        // Given
        val resourceName = "ic_launcher_foreground"
        
        // When
        val uri = ResourceHelper.getDrawableResourceUri(context, resourceName)
        
        // Then
        assertNotNull(uri)
        uri?.let {
            assertTrue(it.startsWith("android.resource://"))
            assertTrue(it.contains(context.packageName))
        }
    }
    
    @Test
    fun testGetRawResourceUri_shouldHandleNonExistentResourceGracefully() {
        // Given
        val nonExistentResource = "non_existent_resource_12345"
        
        // When
        val uri = ResourceHelper.getRawResourceUri(context, nonExistentResource)
        
        // Then - 资源不存在时应该返回null
        assertNull(uri)
    }
}

