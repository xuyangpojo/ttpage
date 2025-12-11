package com.xuyang.ttpage.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import com.xuyang.ttpage.model.data.Video
import com.xuyang.ttpage.ui.screens.*
import com.xuyang.ttpage.viewmodel.HomeViewModel
import com.xuyang.ttpage.viewmodel.TopicViewModel

/**
 * NavGraph 导航图
 * @brief .
 * @author xuyang
 * @date 2025-12-10
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Home.route,
    scrollToTopTrigger: Int = 0
) {
    val homeViewModel: HomeViewModel = viewModel()
    val topicViewModel: TopicViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 菜单栏1: 首页
        composable(
            route = Screen.Home.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            HomeScreen(
                viewModel = homeViewModel,
                topicViewModel = topicViewModel,
                onVideoClick = { video ->
                    navController.navigate("detail/${video.id}") {
                        launchSingleTop = true
                    }
                },
                onRefreshRequest = {
                    homeViewModel.refreshVideos()
                },
                scrollToTopTrigger = scrollToTopTrigger
            )
        }
        // 菜单栏2: 我的
        composable(Screen.Profile.route) {
            ProfileScreen(
                homeViewModel = homeViewModel,
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                onVideoClick = { video ->
                    navController.navigate("detail/${video.id}") {
                        launchSingleTop = true
                    }
                }
            )
        }
        // 我的 - 登录
        composable(Screen.Login.route) {
            LoginScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLoginSuccess = {
                    navController.popBackStack()
                }
            )
        }
        // 我的 - 注册
        composable(Screen.Register.route) {
            RegisterScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.popBackStack()
                }
            )
        }
        // 视频 - 详情
        composable(
            route = "detail/{videoId}",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString(Screen.VIDEO_ID_ARG) ?: ""
            
            val video = homeViewModel.getVideoById(videoId) ?: Video(
                id = videoId,
                title = "视频不存在",
                authorId = "unknown",
                authorName = "未知",
                publishTime = "未知",
                likeCount = 0u,
                commentCount = 0u,
                isHot = false,
                topics = emptyList()
            )
            
            DetailScreen(
                video = video,
                onBackClick = {
                    navController.popBackStack()
                },
                homeViewModel = homeViewModel
            )
        }
    }
}

