package com.xuyang.ttpage

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
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
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 设置全局异常处理器 - 必须在最开始设置
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            Log.e(TAG, "========== 未捕获的异常 ==========")
            Log.e(TAG, "线程: ${thread.name}")
            Log.e(TAG, "异常类型: ${exception.javaClass.name}")
            Log.e(TAG, "异常消息: ${exception.message}")
            Log.e(TAG, "堆栈跟踪:", exception)
            exception.printStackTrace()
            // 调用系统默认处理器
            defaultHandler?.uncaughtException(thread, exception)
        }
        
        try {
            enableEdgeToEdge()
            
            // 设置系统栏为深色主题
            WindowCompat.setDecorFitsSystemWindows(window, false)
            
            // 使用 WindowInsetsControllerCompat 设置状态栏和导航栏
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.isAppearanceLightStatusBars = false // 深色状态栏，使用浅色图标
            windowInsetsController.isAppearanceLightNavigationBars = false // 深色导航栏，使用浅色图标
            
            // 设置系统栏颜色为透明（让内容延伸到系统栏下方）
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = android.graphics.Color.TRANSPARENT
                window.navigationBarColor = android.graphics.Color.TRANSPARENT
            }
            
            setContent {
                TTPageTheme {
                    val navController = rememberNavController()
                    MainScreen(
                        navController = navController,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "========== onCreate 发生异常 ==========")
            Log.e(TAG, "异常类型: ${e.javaClass.name}")
            Log.e(TAG, "异常消息: ${e.message}")
            Log.e(TAG, "堆栈跟踪:", e)
            e.printStackTrace()
            throw e
        }
    }
}
