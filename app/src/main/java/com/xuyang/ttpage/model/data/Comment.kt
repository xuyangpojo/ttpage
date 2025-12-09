package com.xuyang.ttpage.model.data

/**
 * Model层：评论数据模型
 */
data class Comment(
    val id: String,
    val contentId: String,  // 所属内容的ID
    val author: String,
    val content: String,
    val publishTime: String,
    val likeCount: Int,
    val replyCount: Int,
    val parentCommentId: String? = null,  // 父评论ID，null表示顶级评论
    val replies: List<Comment> = emptyList()  // 回复列表
)

