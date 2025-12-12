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
 * MainScreen
 * @brief .
 * @author xuyang
 * @date 2025-12-11
 */
@Composable
fun MainScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val homeViewModel: HomeViewModel = viewModel()
    var scrollToTopTrigger by remember { mutableIntStateOf(0) }
    var currentRoute by remember { mutableStateOf(Screen.Home.route) }
    
    LaunchedEffect(navController) {
        currentRoute = navController.currentBackStackEntry?.destination?.route ?: Screen.Home.route
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentRoute = destination.route ?: Screen.Home.route
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (currentRoute == Screen.Home.route || currentRoute == Screen.Profile.route) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    // 再次点击首页可触发刷新
                    onHomeClick = {
                        if (currentRoute == Screen.Home.route) {
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
