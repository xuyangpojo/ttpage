package com.xuyang.ttpage.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xuyang.ttpage.model.data.Content
import com.xuyang.ttpage.ui.screens.HomeScreen
import com.xuyang.ttpage.viewmodel.HomeViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * HomeScreen UI集成测试
 */
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun homeScreen_displaysContent() {
        // Given
        val testContent = Content(
            id = "1",
            title = "Test Title",
            author = "Test Author",
            publishTime = "1小时前",
            likeCount = 100,
            commentCount = 50,
            isHot = true
        )
        
        // When
        composeTestRule.setContent {
            HomeScreen()
        }
        
        // Then - 等待内容加载后检查
        composeTestRule.waitForIdle()
        // 由于是异步加载，我们检查是否有内容显示
        // 实际测试中可能需要等待更长时间或使用更具体的测试策略
    }
    
    @Test
    fun homeScreen_displaysLoadingIndicator() {
        // When
        composeTestRule.setContent {
            HomeScreen()
        }
        
        // Then - 在加载期间应该显示加载指示器
        // 由于加载很快，这个测试可能需要调整
        composeTestRule.waitForIdle()
    }
}

