package com.xuyang.ttpage.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xuyang.ttpage.ui.screens.ProfileScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * ProfileScreen UI集成测试
 */
@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun profileScreen_displaysLoginPromptWhenNotLoggedIn() {
        // When
        composeTestRule.setContent {
            ProfileScreen()
        }
        
        // Then
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("未登录").assertIsDisplayed()
        composeTestRule.onNodeWithText("请登录以查看个人信息").assertIsDisplayed()
        composeTestRule.onNodeWithText("登录").assertIsDisplayed()
        composeTestRule.onNodeWithText("注册").assertIsDisplayed()
    }
    
    @Test
    fun profileScreen_displaysSettings() {
        // When
        composeTestRule.setContent {
            ProfileScreen()
        }
        
        // Then
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("设置").assertIsDisplayed()
        composeTestRule.onNodeWithText("关于").assertIsDisplayed()
    }
}

