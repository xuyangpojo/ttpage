package com.xuyang.ttpage.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xuyang.ttpage.ui.screens.RegisterScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * RegisterScreen UI集成测试
 */
@RunWith(AndroidJUnit4::class)
class RegisterScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun registerScreen_displaysRegisterForm() {
        // When
        composeTestRule.setContent {
            RegisterScreen()
        }
        
        // Then
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("注册").assertIsDisplayed()
        composeTestRule.onNodeWithText("创建账户").assertIsDisplayed()
    }
    
    @Test
    fun registerScreen_displaysAllInputFields() {
        // When
        composeTestRule.setContent {
            RegisterScreen()
        }
        
        // Then
        composeTestRule.waitForIdle()
        // 检查输入框标签
        composeTestRule.onNodeWithText("用户名").assertIsDisplayed()
        composeTestRule.onNodeWithText("邮箱").assertIsDisplayed()
        composeTestRule.onNodeWithText("密码").assertIsDisplayed()
    }
}

