package com.xuyang.ttpage.util

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * ResourceHelper 资源工具类
 * @brief .
 * @author xuyang
 * @date 2025-12-10
 */
object ResourceHelper {
    fun getRawResourceUri(context: Context, resourceName: String): String? {
        try {
            val resourceId = context.resources.getIdentifier(
                resourceName,
                "raw",
                context.packageName
            )
            return if (resourceId != 0) {
                "android.resource://${context.packageName}/$resourceId"
            } else {
                null
            }
        } catch (e: Exception) {
            return null
        }
    }
    
    fun hasRawResource(context: Context, resourceName: String): Boolean {
        val resourceId = context.resources.getIdentifier(
            resourceName,
            "raw",
            context.packageName
        )
        return resourceId != 0
    }

    fun getDrawableResourceUri(context: Context, resourceName: String): String? {
        val resourceId = context.resources.getIdentifier(
            resourceName,
            "drawable",
            context.packageName
        )
        return if (resourceId != 0) {
            "android.resource://${context.packageName}/$resourceId"
        } else {
            null
        }
    }
}

@Composable
fun getRawResourceUri(resourceName: String): String? {
    val context = LocalContext.current
    return ResourceHelper.getRawResourceUri(context, resourceName)
}

@Composable
fun getDrawableResourceUri(resourceName: String): String? {
    val context = LocalContext.current
    return ResourceHelper.getDrawableResourceUri(context, resourceName)
}
