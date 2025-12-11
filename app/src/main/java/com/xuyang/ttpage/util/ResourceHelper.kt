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
    fun getRawResourceUri(context: Context, resourceName: String): String {
        val resourceId = context.resources.getIdentifier(
            resourceName,
            "raw",
            context.packageName
        )
        return "android.resource://${context.packageName}/$resourceId"
    }
    fun getDrawableResourceUri(context: Context, resourceName: String): String {
        val resourceId = context.resources.getIdentifier(
            resourceName,
            "drawable",
            context.packageName
        )
        return "android.resource://${context.packageName}/$resourceId"
    }
}
@Composable
fun getRawResourceUri(resourceName: String): String {
    val context = LocalContext.current
    return ResourceHelper.getRawResourceUri(context, resourceName)
}
@Composable
fun getDrawableResourceUri(resourceName: String): String {
    val context = LocalContext.current
    return ResourceHelper.getDrawableResourceUri(context, resourceName)
}
