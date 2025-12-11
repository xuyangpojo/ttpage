package com.xuyang.ttpage.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * BottomNavigationBar 底部导航栏
 * @brief 在[首页,我的]之间切换; 再次点击[首页]时返回顶部并刷新
 * @author xuyang
 * @date 2025-12-10
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

