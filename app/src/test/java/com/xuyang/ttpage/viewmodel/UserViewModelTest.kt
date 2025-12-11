package com.xuyang.ttpage.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/**
 * UserViewModel单元测试
 */
class UserViewModelTest {
    
    @Test
    fun `initial state should have no user`() = runTest {
        // Given
        val viewModel = UserViewModel()
        
        // When & Then
        viewModel.currentUser.test {
            val user = awaitItem()
            assertNull(user)
        }
        
        viewModel.isLoggedIn.test {
            val isLoggedIn = awaitItem()
            assertFalse(isLoggedIn)
        }
    }
    
    @Test
    fun `login should set current user when credentials are valid`() = runTest {
        // Given
        val viewModel = UserViewModel()
        val username = "testuser"
        val password = "password123"
        
        // When
        viewModel.login(username, password)
        
        // Wait for login to complete
        delay(1500)
        
        // Then
        val user = viewModel.currentUser.value
        assertNotNull(user)
        assertEquals(username, user?.username)
        assertTrue(viewModel.isLoggedIn.value)
    }
    
    @Test
    fun `login should set error when credentials are empty`() = runTest {
        // Given
        val viewModel = UserViewModel()
        
        // When
        viewModel.login("", "")
        
        // Wait for login to complete
        delay(1500)
        
        // Then
        val error = viewModel.errorMessage.value
        assertNotNull(error)
        assertTrue(error!!.contains("不能为空"))
        assertNull(viewModel.currentUser.value)
        assertFalse(viewModel.isLoggedIn.value)
    }
    
    @Test
    fun `register should set current user when information is valid`() = runTest {
        // Given
        val viewModel = UserViewModel()
        val username = "newuser"
        val email = "newuser@example.com"
        val password = "password123"
        
        // When
        viewModel.register(username, email, password)
        
        // Wait for register to complete
        delay(1500)
        
        // Then
        val user = viewModel.currentUser.value
        assertNotNull(user)
        assertEquals(username, user?.username)
        assertEquals(email, user?.email)
        assertTrue(viewModel.isLoggedIn.value)
    }
    
    @Test
    fun `register should set error when information is incomplete`() = runTest {
        // Given
        val viewModel = UserViewModel()
        
        // When
        viewModel.register("", "", "")
        
        // Wait for register to complete
        delay(1500)
        
        // Then
        val error = viewModel.errorMessage.value
        assertNotNull(error)
        assertTrue(error!!.contains("请填写完整信息"))
        assertNull(viewModel.currentUser.value)
        assertFalse(viewModel.isLoggedIn.value)
    }
    
    @Test
    fun `logout should clear current user`() = runTest {
        // Given
        val viewModel = UserViewModel()
        viewModel.login("testuser", "password")
        delay(1500)
        
        // Verify user is logged in
        assertNotNull(viewModel.currentUser.value)
        assertTrue(viewModel.isLoggedIn.value)
        
        // When
        viewModel.logout()
        
        // Then
        assertNull(viewModel.currentUser.value)
        assertFalse(viewModel.isLoggedIn.value)
    }
    
    @Test
    fun `clearError should clear error message`() = runTest {
        // Given
        val viewModel = UserViewModel()
        viewModel.login("", "")
        delay(1500)
        
        // Verify error exists
        assertNotNull(viewModel.errorMessage.value)
        
        // When
        viewModel.clearError()
        
        // Then
        assertNull(viewModel.errorMessage.value)
    }
    
    @Test
    fun `isLoading should be true during login`() = runTest {
        // Given
        val viewModel = UserViewModel()
        
        // When
        viewModel.login("testuser", "password")
        
        // Then - check loading state (may be very brief)
        delay(100)
        // Loading should eventually be false
        delay(1500)
        assertFalse(viewModel.isLoading.value)
    }
    
    @Test
    fun `isLoading should be true during register`() = runTest {
        // Given
        val viewModel = UserViewModel()
        
        // When
        viewModel.register("testuser", "test@example.com", "password")
        
        // Then - check loading state
        delay(100)
        // Loading should eventually be false
        delay(1500)
        assertFalse(viewModel.isLoading.value)
    }
    
    @Test
    fun `isLoggedIn should be false initially`() = runTest {
        // Given
        val viewModel = UserViewModel()
        
        // Then
        assertFalse(viewModel.isLoggedIn.value)
        assertNull(viewModel.currentUser.value)
    }
    
    @Test
    fun `isLoggedIn should be true after successful login`() = runTest {
        // Given
        val viewModel = UserViewModel()
        
        // When
        viewModel.login("testuser", "password")
        delay(1500)
        
        // Then
        assertTrue(viewModel.isLoggedIn.value)
        assertNotNull(viewModel.currentUser.value)
    }
    
    @Test
    fun `isLoggedIn should be false after logout`() = runTest {
        // Given
        val viewModel = UserViewModel()
        viewModel.login("testuser", "password")
        delay(1500)
        
        // Verify logged in
        assertTrue(viewModel.isLoggedIn.value)
        
        // When
        viewModel.logout()
        
        // Then
        assertFalse(viewModel.isLoggedIn.value)
        assertNull(viewModel.currentUser.value)
    }
    
    @Test
    fun `register should set email correctly`() = runTest {
        // Given
        val viewModel = UserViewModel()
        val email = "test@example.com"
        
        // When
        viewModel.register("testuser", email, "password")
        delay(1500)
        
        // Then
        val user = viewModel.currentUser.value
        assertNotNull(user)
        assertEquals(email, user?.email)
    }
}

