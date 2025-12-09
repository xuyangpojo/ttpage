package com.xuyang.ttpage.model.data

/**
 * Comment 评论
 * @brief 视频评论区的评论Entity
 * @author xuyang
 * @date 2025-12-9
 */
data class Comment(
    val id: String,
    // foreign key
    val videoId: String,
    // foreign key
    val authorId: String,
    val author: String,  // 作者名称（显示用）
    val content: String,
    val publishTime: String,
    val likeCount: Int,
    val replyCount: Int,
    val parentCommentId: String? = null,  // 父评论ID，null表示顶级评论
    val replies: List<Comment> = emptyList()  // 回复列表
)

