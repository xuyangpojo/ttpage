package com.xuyang.ttpage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.xuyang.ttpage.ui.screens.HomeScreen
import com.xuyang.ttpage.ui.theme.TTPageTheme

/**
 * MainActivity - View层入口
 * 
 * MVVM架构中，Activity只负责：
 * 1. 设置UI主题
 * 2. 加载主界面（Screen）
 * 3. 不包含业务逻辑
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TTPageTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 使用MVVM架构的HomeScreen
                    // ViewModel会在HomeScreen内部自动创建
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
