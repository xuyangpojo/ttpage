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
    // 视频封面资源
    val videoCover: String? = null,
    // 视频文件资源
    val videoUrl: String? = null,
    // 视频所属的话题ID列表, 可为0到多个
    // foreign key: Topic.kt
    val topics: List<String> = emptyList()
) {
    val hasVideo: Boolean
        get() = !videoUrl.isNullOrBlank()
}

