package com.xuyang.ttpage.navigation

import com.xuyang.ttpage.model.data.Content

/**
 * 导航路由定义
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    data class Detail(val content: Content) : Screen("detail/{contentId}") {
        fun createRoute(contentId: String) = "detail/$contentId"
    }
    
    companion object {
        const val CONTENT_ID_ARG = "contentId"
    }
}

