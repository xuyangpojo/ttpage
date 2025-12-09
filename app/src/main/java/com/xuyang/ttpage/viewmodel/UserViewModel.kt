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
 * ViewModel层：用户视图模型
 * 管理用户登录状态和用户信息
 */
class UserViewModel : ViewModel() {
    
    // UI状态：当前登录用户
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    // UI状态：是否已登录
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    init {
        // 监听用户状态变化
        viewModelScope.launch {
            _currentUser.collect { user ->
                _isLoggedIn.value = user != null
            }
        }
    }
    
    // UI状态：登录状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // UI状态：错误信息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    /**
     * 登录
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                
                // 模拟登录请求
                delay(1000)
                
                // 简单的模拟验证（实际应该调用API）
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
    
    /**
     * 注册
     */
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                
                // 模拟注册请求
                delay(1000)
                
                // 简单的模拟验证
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
    
    /**
     * 登出
     */
    fun logout() {
        _currentUser.value = null
    }
    
    /**
     * 清除错误信息
     */
    fun clearError() {
        _errorMessage.value = null
    }
}


