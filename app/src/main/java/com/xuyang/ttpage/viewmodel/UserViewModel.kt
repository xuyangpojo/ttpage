package com.xuyang.ttpage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xuyang.ttpage.model.data.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UserViewModel
 * @brief .
 * @author xuyang
 * @date 2025-12-11
 */
class UserViewModel : ViewModel() {
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    init {
        viewModelScope.launch {
            _currentUser.collect { user ->
                _isLoggedIn.value = user != null
            }
        }
    }
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                delay(100)
                
                if (username.isNotBlank() && password.isNotBlank()) {
                    _currentUser.value = User(
                        id = "1",
                        username = username,
                        email = "$username@example.com",
                        avatar = null,
                        bio = "这是用户简介"
                    )
                } else {
                    _errorMessage.value = "用户名和密码不能为空"
                }
            } catch (e: Exception) {
                _errorMessage.value = "登录失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                delay(100)
                if (username.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                    _currentUser.value = User(
                        id = System.currentTimeMillis().toString(),
                        username = username,
                        email = email,
                        avatar = null,
                        bio = "新用户"
                    )
                } else {
                    _errorMessage.value = "请填写完整信息"
                }
            } catch (e: Exception) {
                _errorMessage.value = "注册失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun logout() {
        _currentUser.value = null
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}


