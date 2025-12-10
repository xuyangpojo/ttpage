package com.xuyang.ttpage.model.data

/**
 * User 平台用户
 * @brief 包括个人账户的基本信息
 * @author xuyang
 * @date 2025-12-10
 */
data class User(
    val id: String,
    val username: String,
    val email: String,
    val avatar: String? = null,
    val bio: String? = null
)
