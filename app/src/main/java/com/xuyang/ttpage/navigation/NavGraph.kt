package com.xuyang.ttpage.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xuyang.ttpage.model.data.Video
import com.xuyang.ttpage.ui.screens.*
import com.xuyang.ttpage.viewmodel.FavoriteViewModel
import com.xuyang.ttpage.viewmodel.HomeViewModel
import com.xuyang.ttpage.viewmodel.TopicViewModel

/**
 * 导航图配置
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Home.route,
    scrollToTopTrigger: Int = 0
) {
    // 共享ViewModel实例
    val homeViewModel: HomeViewModel = viewModel()
    val topicViewModel: TopicViewModel = viewModel()
    val favoriteViewModel: FavoriteViewModel = viewModel()
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 首页
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                topicViewModel = topicViewModel,
                onVideoClick = { video ->
                    // 导航到详情页，传递Video ID
                    navController.navigate("detail/${video.id}") {
                        // 保存状态，避免重复创建
                        launchSingleTop = true
                    }
                },
                onRefreshRequest = {
                    homeViewModel.refreshVideos()
                },
                scrollToTopTrigger = scrollToTopTrigger
            )
        }
        
        // 我的页面
        composable(Screen.Profile.route) {
            ProfileScreen(
                homeViewModel = homeViewModel,
                favoriteViewModel = favoriteViewModel,
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
        
        // 登录页面
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
        
        // 注册页面
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
        
        // 详情页
        composable(
            route = "detail/{${Screen.VIDEO_ID_ARG}}"
        ) { backStackEntry ->
            // 从参数中获取videoId
            val videoId = backStackEntry.arguments?.getString(Screen.VIDEO_ID_ARG) ?: ""
            
            // 从HomeViewModel获取对应的Video对象
            val video = homeViewModel.getVideoById(videoId) ?: Video(
                id = videoId,
                title = "视频不存在",
                author = "未知",
                publishTime = "未知",
                likeCount = 0,
                commentCount = 0,
                isHot = false
            )
            
            DetailScreen(
                video = video,
                onBackClick = {
                    navController.popBackStack()
                },
                homeViewModel = homeViewModel,
                favoriteViewModel = favoriteViewModel
            )
        }
    }
}

