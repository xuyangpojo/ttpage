package com.xuyang.ttpage.model.repository

import com.xuyang.ttpage.model.data.Video
import kotlinx.coroutines.delay

/**
 * VideoRepository 视频数据仓库
 * @brief .
 * @author xuyang
 * @date 2025-12-10
 */
class VideoRepository {
    
    suspend fun getRecommendedVideos(): List<Video> {
        return listOf(
            Video(
                id = "v001",
                title = "【视频1】视频1",
                authorId = "u001",
                authorName = "作者1",
                publishTime = "2小时前",
                likeCount = 128u,
                commentCount = 45u,
                isHot = true,
                videoCover = "cover1",  // 对应 res/drawable/cover1.png
                videoUrl = "video1",     // 对应 res/raw/video1.mp4
                topics = emptyList()
            ),
            Video(
                id = "v002",
                title = "Kotlin协程深入理解：掌握异步编程的核心概念",
                authorId = "u002",
                authorName = "技术大牛",
                publishTime = "5小时前",
                likeCount = 256u,
                commentCount = 89u,
                isHot = true,
                videoCover = "cover2",
                videoUrl = "video2",
                topics = listOf("tech", "hot")
            ),
            Video(
                id = "v003",
                title = "MVVM架构模式在Android开发中的最佳实践",
                authorId = "u003",
                authorName = "架构师老李",
                publishTime = "1天前",
                likeCount = 89u,
                commentCount = 23u,
                isHot = false,
                topics = listOf("tech")
            ),
            Video(
                id = "v004",
                title = "Material Design 3 设计规范详解",
                authorId = "u004",
                authorName = "UI设计师",
                publishTime = "2天前",
                likeCount = 156u,
                commentCount = 34u,
                isHot = false,
                topics = listOf("tech")
            ),
            Video(
                id = "v005",
                title = "Android性能优化：内存泄漏检测与修复",
                authorId = "u005",
                authorName = "性能专家",
                publishTime = "3小时前",
                likeCount = 312u,
                commentCount = 67u,
                isHot = true,
                videoCover = "cover5",
                videoUrl = "video5",
                topics = listOf("tech", "hot")
            ),
            Video(
                id = "v006",
                title = "Room数据库使用指南：本地数据持久化方案",
                authorId = "u006",
                authorName = "数据工程师",
                publishTime = "6小时前",
                likeCount = 78u,
                commentCount = 19u,
                isHot = false,
                topics = listOf("tech")
            ),
            Video(
                id = "v007",
                title = "Retrofit网络请求框架实战：构建高效的API客户端",
                authorId = "u007",
                authorName = "后端开发者",
                publishTime = "4小时前",
                likeCount = 201u,
                commentCount = 56u,
                isHot = true,
                videoCover = "cover7",
                videoUrl = "video7",
                topics = listOf("tech", "hot")
            ),
            Video(
                id = "v008",
                title = "Compose状态管理：StateFlow vs LiveData",
                authorId = "u008",
                authorName = "技术博主",
                publishTime = "1天前",
                likeCount = 134u,
                commentCount = 42u,
                isHot = false,
                topics = listOf("tech")
            ),
            Video(
                id = "v009",
                title = "Android动画效果实现：从基础到高级",
                authorId = "u009",
                authorName = "动画设计师",
                publishTime = "8小时前",
                likeCount = 167u,
                commentCount = 38u,
                isHot = false,
                topics = listOf("tech")
            ),
            Video(
                id = "v010",
                title = "Jetpack Navigation组件：实现流畅的页面导航",
                authorId = "u010",
                authorName = "导航专家",
                publishTime = "12小时前",
                likeCount = 98u,
                commentCount = 25u,
                isHot = false,
                topics = listOf("tech")
            ),
            Video(
                id = "v011",
                title = "Android安全开发：数据加密与权限管理",
                authorId = "u011",
                authorName = "安全专家",
                publishTime = "1天前",
                likeCount = 245u,
                commentCount = 71u,
                isHot = true,
                topics = listOf("tech", "hot")
            ),
            Video(
                id = "v012",
                title = "Kotlin DSL构建Gradle脚本：提升构建效率",
                authorId = "u012",
                authorName = "构建工程师",
                publishTime = "2天前",
                likeCount = 112u,
                commentCount = 29u,
                isHot = false,
                topics = listOf("tech")
            )
        )
    }
}
