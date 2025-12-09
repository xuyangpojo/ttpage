package com.xuyang.ttpage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.xuyang.ttpage.ui.MainScreen
import com.xuyang.ttpage.ui.theme.TTPageTheme

/**
 * MainActivity 入口文件
 * @brief 进行初始加载
 * @author xuyang
 * @date 2025-12-9
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TTPageTheme {
                val navController = rememberNavController()
                MainScreen(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
