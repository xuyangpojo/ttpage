package com.xuyang.ttpage.navigation

import com.xuyang.ttpage.model.data.Video

/**
 * Screen 导航路由定义
 * @brief .
 * @author xuyang
 * @date 2025-12-10
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register : Screen("register")
    data class Detail(val video: Video) : Screen("detail/{videoId}") {
        fun createRoute(videoId: String) = "detail/$videoId"
    }
    companion object {
        const val VIDEO_ID_ARG = "videoId"
    }
}
