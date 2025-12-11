package com.xuyang.ttpage.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.xuyang.ttpage.navigation.NavGraph
import com.xuyang.ttpage.navigation.Screen
import com.xuyang.ttpage.ui.components.BottomNavigationBar
import com.xuyang.ttpage.viewmodel.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * 主屏幕容器
 * 包含底部导航栏和主要内容区域
 */
@Composable
fun MainScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val homeViewModel: HomeViewModel = viewModel()
    
    var scrollToTopTrigger by remember { mutableIntStateOf(0) }
    
    // 获取当前路由（通过监听导航变化）
    var currentRoute by remember { mutableStateOf(Screen.Home.route) }
    
    LaunchedEffect(navController) {
        // 初始化当前路由
        currentRoute = navController.currentBackStackEntry?.destination?.route ?: Screen.Home.route
        
        // 监听导航变化
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentRoute = destination.route ?: Screen.Home.route
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // 使用深色背景
        bottomBar = {
            // 只在首页和我的页面显示底部导航栏
            if (currentRoute == Screen.Home.route || currentRoute == Screen.Profile.route) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onHomeClick = {
                        if (currentRoute == Screen.Home.route) {
                            // 如果已在首页，返回顶部并刷新
                            scrollToTopTrigger++
                            homeViewModel.loadVideos()
                        } else {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    },
                    onProfileClick = {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(paddingValues),
            scrollToTopTrigger = scrollToTopTrigger
        )
    }
}

