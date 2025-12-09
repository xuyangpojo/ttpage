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
 * - 视频封面（可选）
 * - 视频文件URL（可选）
 */
data class Content(
    val id: String,
    val title: String,
    val author: String,
    val publishTime: String,
    val likeCount: Int,
    val commentCount: Int,
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

