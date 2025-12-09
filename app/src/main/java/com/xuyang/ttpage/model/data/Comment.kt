package com.xuyang.ttpage.model.data

/**
 * Comment 评论
 * @brief 视频评论区的评论Entity
 * @author xuyang
 * @date 2025-12-9
 */
data class Comment(
    val id: Long,
    // foreign key: Video.kt
    val videoId: Long,
    // foreign key: User.kt
    val authorId: Long,
    val authorName: String,
    val content: String,
    val publishTime: String,
    val likeCount: Int,
    val replyCount: Int,
    // foreign key: Comment.kt
    // 父评论ID，为null时代表顶级评论（不是回复）
    val parentCommentId: String? = null
)

/**
 * 带回复的评论（用于UI展示的树形结构）
 */
data class CommentWithReplies(
    val comment: Comment,
    val replies: List<CommentWithReplies> = emptyList()
)
