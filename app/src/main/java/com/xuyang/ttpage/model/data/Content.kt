package com.xuyang.ttpage.model.data

/**
 * Model层：内容数据模型
 * 
 * 包含字段：
 * - 标题
 * - 作者
 * - 发布时间
 * - 点赞数
 * - 评论数
 * - 是否是热门内容
 */
data class Content(
    val id: String,
    val title: String,
    val author: String,
    val publishTime: String,
    val likeCount: Int,
    val commentCount: Int,
    val isHot: Boolean
)

