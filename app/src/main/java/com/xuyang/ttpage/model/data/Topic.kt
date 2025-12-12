package com.xuyang.ttpage.model.data

/**
 * Topic 顶部话题(视频分类)
 * @brief 每个视频可以属于一个或多个话题
 * @author xuyang
 * @date 2025-12-9
 */
data class Topic(
    val id: String,
    val name: String,
    // 预留，暂时不使用话题图标
    val icon: String? = null
)