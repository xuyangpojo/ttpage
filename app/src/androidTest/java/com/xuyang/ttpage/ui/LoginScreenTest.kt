package com.xuyang.ttpage.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xuyang.ttpage.ui.screens.LoginScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun loginScreen_displaysLoginForm() {
        composeTestRule.setContent {
            LoginScreen()
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("登录").assertIsDisplayed()
        composeTestRule.onNodeWithText("欢迎回来").assertIsDisplayed()
    }
    
    @Test
    fun loginScreen_displaysUsernameAndPasswordFields() {
        composeTestRule.setContent {
            LoginScreen()
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("用户名").assertIsDisplayed()
        composeTestRule.onNodeWithText("密码").assertIsDisplayed()
    }
}

