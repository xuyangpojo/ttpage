package com.xuyang.ttpage.util

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * 资源工具类
 * 用于获取本地资源的URI
 */
object ResourceHelper {
    /**
     * 获取raw资源文件的URI
     * @param context 上下文
     * @param resourceName 资源文件名（不含扩展名），例如：video1
     * @return URI字符串，格式：android.resource://包名/raw/video1
     */
    fun getRawResourceUri(context: Context, resourceName: String): String {
        val resourceId = context.resources.getIdentifier(
            resourceName,
            "raw",
            context.packageName
        )
        return "android.resource://${context.packageName}/$resourceId"
    }
    
    /**
     * 获取drawable资源文件的URI
     * @param context 上下文
     * @param resourceName 资源文件名（不含扩展名），例如：cover1
     * @return URI字符串，格式：android.resource://包名/drawable/cover1
     */
    fun getDrawableResourceUri(context: Context, resourceName: String): String {
        val resourceId = context.resources.getIdentifier(
            resourceName,
            "drawable",
            context.packageName
        )
        return "android.resource://${context.packageName}/$resourceId"
    }
}

/**
 * Compose扩展函数：获取raw资源URI
 */
@Composable
fun getRawResourceUri(resourceName: String): String {
    val context = LocalContext.current
    return ResourceHelper.getRawResourceUri(context, resourceName)
}

/**
 * Compose扩展函数：获取drawable资源URI
 */
@Composable
fun getDrawableResourceUri(resourceName: String): String {
    val context = LocalContext.current
    return ResourceHelper.getDrawableResourceUri(context, resourceName)
}

