package com.xuyang.ttpage.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 底部导航栏
 * 
 * 功能：
 * 1. 首页和我的页面切换
 * 2. 点击首页时返回顶部并刷新（如果已在首页）
 */
@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "首页"
                )
            },
            label = { Text("首页") },
            selected = currentRoute == "home",
            onClick = onHomeClick
        )
        
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "我的"
                )
            },
            label = { Text("我的") },
            selected = currentRoute == "profile",
            onClick = onProfileClick
        )
    }
}

