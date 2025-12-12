package com.xuyang.ttpage.util

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test


class ResourceHelperTest {
    
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    
    @Test
    fun testGetRawResourceUri_shouldReturnValidUriFormat() {
        val resourceName = "video1"
        val uri = ResourceHelper.getRawResourceUri(context, resourceName)
        assertNotNull(uri)
        uri?.let {
            assertTrue(it.startsWith("android.resource://"))
            assertTrue(it.contains(context.packageName))
        }
    }
    
    @Test
    fun testGetDrawableResourceUri_shouldReturnValidUriFormat() {
        val resourceName = "ic_launcher_foreground"
        val uri = ResourceHelper.getDrawableResourceUri(context, resourceName)
        assertNotNull(uri)
        uri?.let {
            assertTrue(it.startsWith("android.resource://"))
            assertTrue(it.contains(context.packageName))
        }
    }
    
    @Test
    fun testGetRawResourceUri_shouldHandleNonExistentResourceGracefully() {
        val nonExistentResource = "non_existent_resource_12345"
        val uri = ResourceHelper.getRawResourceUri(context, nonExistentResource)
        assertNull(uri)
    }
}
