package com.xuyang.ttpage.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class UserViewModelTest {
    
    @Test
    fun `initial state should have no user`() = runTest {
        val viewModel = UserViewModel()
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
        val viewModel = UserViewModel()
        val username = "testuser"
        val password = "password123"
        viewModel.login(username, password)
        delay(1500)
        val user = viewModel.currentUser.value
        assertNotNull(user)
        assertEquals(username, user?.username)
        assertTrue(viewModel.isLoggedIn.value)
    }
    
    @Test
    fun `login should set error when credentials are empty`() = runTest {
        val viewModel = UserViewModel()
        viewModel.login("", "")
        delay(1500)
        val error = viewModel.errorMessage.value
        assertNotNull(error)
        assertTrue(error!!.contains("不能为空"))
        assertNull(viewModel.currentUser.value)
        assertFalse(viewModel.isLoggedIn.value)
    }
    
    @Test
    fun `register should set current user when information is valid`() = runTest {
        val viewModel = UserViewModel()
        val username = "newuser"
        val email = "newuser@example.com"
        val password = "password123"
        viewModel.register(username, email, password)
        delay(1500)
        val user = viewModel.currentUser.value
        assertNotNull(user)
        assertEquals(username, user?.username)
        assertEquals(email, user?.email)
        assertTrue(viewModel.isLoggedIn.value)
    }
    
    @Test
    fun `register should set error when information is incomplete`() = runTest {
        val viewModel = UserViewModel()
        viewModel.register("", "", "")
        delay(1500)
        val error = viewModel.errorMessage.value
        assertNotNull(error)
        assertTrue(error!!.contains("请填写完整信息"))
        assertNull(viewModel.currentUser.value)
        assertFalse(viewModel.isLoggedIn.value)
    }
    
    @Test
    fun `logout should clear current user`() = runTest {
        val viewModel = UserViewModel()
        viewModel.login("testuser", "password")
        delay(1500)
        assertNotNull(viewModel.currentUser.value)
        assertTrue(viewModel.isLoggedIn.value)
        viewModel.logout()
        assertNull(viewModel.currentUser.value)
        assertFalse(viewModel.isLoggedIn.value)
    }
    
    @Test
    fun `clearError should clear error message`() = runTest {
        val viewModel = UserViewModel()
        viewModel.login("", "")
        delay(1500)
        assertNotNull(viewModel.errorMessage.value)
        viewModel.clearError()
        assertNull(viewModel.errorMessage.value)
    }
    
    @Test
    fun `isLoading should be true during login`() = runTest {
        val viewModel = UserViewModel()
        viewModel.login("testuser", "password")
        delay(100)
        delay(1500)
        assertFalse(viewModel.isLoading.value)
    }
    
    @Test
    fun `isLoading should be true during register`() = runTest {
        val viewModel = UserViewModel()
        viewModel.register("testuser", "test@example.com", "password")
        delay(100)
        delay(1500)
        assertFalse(viewModel.isLoading.value)
    }
    
    @Test
    fun `isLoggedIn should be false initially`() = runTest {
        val viewModel = UserViewModel()
        assertFalse(viewModel.isLoggedIn.value)
        assertNull(viewModel.currentUser.value)
    }
    
    @Test
    fun `isLoggedIn should be true after successful login`() = runTest {
        val viewModel = UserViewModel()
        viewModel.login("testuser", "password")
        delay(1500)
        assertTrue(viewModel.isLoggedIn.value)
        assertNotNull(viewModel.currentUser.value)
    }
    
    @Test
    fun `isLoggedIn should be false after logout`() = runTest {
        val viewModel = UserViewModel()
        viewModel.login("testuser", "password")
        delay(1500)
        assertTrue(viewModel.isLoggedIn.value)
        viewModel.logout()
        assertFalse(viewModel.isLoggedIn.value)
        assertNull(viewModel.currentUser.value)
    }
    
    @Test
    fun `register should set email correctly`() = runTest {
        val viewModel = UserViewModel()
        val email = "test@example.com"
        viewModel.register("testuser", email, "password")
        delay(1500)
        val user = viewModel.currentUser.value
        assertNotNull(user)
        assertEquals(email, user?.email)
    }
}
