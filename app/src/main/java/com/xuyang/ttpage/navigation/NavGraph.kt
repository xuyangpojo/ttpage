package com.xuyang.ttpage.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xuyang.ttpage.model.data.Content
import com.xuyang.ttpage.ui.screens.DetailScreen
import com.xuyang.ttpage.ui.screens.HomeScreen
import com.xuyang.ttpage.viewmodel.HomeViewModel

/**
 * 导航图配置
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Home.route
) {
    // 共享ViewModel实例
    val homeViewModel: HomeViewModel = viewModel()
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 首页
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onContentClick = { content ->
                    // 导航到详情页，传递Content ID
                    navController.navigate("detail/${content.id}") {
                        // 保存状态，避免重复创建
                        launchSingleTop = true
                    }
                }
            )
        }
        
        // 详情页
        composable(
            route = "detail/{${Screen.CONTENT_ID_ARG}}"
        ) { backStackEntry ->
            // 从参数中获取contentId
            val contentId = backStackEntry.arguments?.getString(Screen.CONTENT_ID_ARG) ?: ""
            
            // 从HomeViewModel获取对应的Content对象
            val content = homeViewModel.getContentById(contentId) ?: Content(
                id = contentId,
                title = "内容不存在",
                author = "未知",
                publishTime = "未知",
                likeCount = 0,
                commentCount = 0,
                isHot = false
            )
            
            DetailScreen(
                content = content,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

