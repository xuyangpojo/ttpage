package com.xuyang.ttpage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.xuyang.ttpage.navigation.NavGraph
import com.xuyang.ttpage.ui.theme.TTPageTheme

/**
 * MainActivity - View层入口
 * 
 * MVVM架构中，Activity只负责：
 * 1. 设置UI主题
 * 2. 初始化Navigation
 * 3. 加载导航图
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TTPageTheme {
                // 创建NavController
                val navController = rememberNavController()
                
                // 设置导航图
                NavGraph(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
