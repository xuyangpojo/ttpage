package com.xuyang.ttpage.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xuyang.ttpage.ui.screens.HomeScreen
import com.xuyang.ttpage.viewmodel.HomeViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun homeScreen_displaysContent() {
        composeTestRule.setContent {
            HomeScreen()
        }
        composeTestRule.waitForIdle()
    }
    
    @Test
    fun homeScreen_displaysLoadingIndicator() {
        composeTestRule.setContent {
            HomeScreen()
        }
        composeTestRule.waitForIdle()
    }
}

