package com.xuyang.ttpage.model.data

/**
 * Comment 评论
 * @brief 视频评论区的评论Entity
 * @author xuyang
 * @date 2025-12-9
 */
data class Comment(
    val id: String,
    // foreign key: Video.kt
    val videoId: String,
    // foreign key: User.kt
    val authorId: String,
    val authorName: String,
    val content: String,
    val publishTime: String,
    val likeCount: UInt,
    val replyCount: UInt,
    // foreign key: Comment.kt 父评论ID 
    // 为null时代表顶级评论, 非null非空时代表二级评论
    val parentCommentId: String? = null
)

/**
 * CommentWithReplies 
 * @brief 带回复的评论 (用于UI展示结构)
 * @author xuyang
 * @date 2025-12-9
 */
data class CommentWithReplies(
    val comment: Comment,
    val replies: List<CommentWithReplies> = emptyList()
)
