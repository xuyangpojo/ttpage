package com.xuyang.ttpage.model.data

/**
 * Model层：用户数据模型
 */
data class User(
    val id: String,
    val username: String,
    val email: String,
    val avatar: String? = null,
    val bio: String? = null
)

