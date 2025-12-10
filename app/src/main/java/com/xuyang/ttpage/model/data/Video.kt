package com.xuyang.ttpage.model.data

/**
 * Video 视频
 * @brief 视频内容Entity
 * @author xuyang
 * @date 2025-12-10
 */
data class Video(
    val id: String,
    val title: String,
    // foreign key: User.kt
    val authorId: String,
    val authorName: String,
    val publishTime: String,
    val likeCount: UInt,
    val commentCount: UInt,
    val isHot: Boolean,
    val videoCover: String? = null,  // 视频封面URL
    val videoUrl: String? = null     // 视频文件URL
) {
    /**
     * 判断是否有视频
     */
    val hasVideo: Boolean
        get() = !videoUrl.isNullOrBlank()
}

