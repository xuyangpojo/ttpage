package com.xuyang.ttpage.model.repository

import com.xuyang.ttpage.model.data.Content
import kotlinx.coroutines.delay

/**
 * Model层：内容数据仓库
 * 负责提供内容数据（模拟数据源）
 */
class ContentRepository {
    
    /**
     * 获取推荐内容列表
     * 返回模拟数据
     */
    suspend fun getRecommendedContents(): List<Content> {
        // 模拟网络请求延迟
        delay(500)
        
        return listOf(
            Content(
                id = "1",
                title = "Android Jetpack Compose 入门教程：从零开始学习现代UI开发",
                author = "开发者小王",
                publishTime = "2小时前",
                likeCount = 128,
                commentCount = 45,
                isHot = true,
                videoCover = "cover1",  // 对应 res/drawable/cover1.png
                videoUrl = "video1"     // 对应 res/raw/video1.mp4
            ),
            Content(
                id = "2",
                title = "Kotlin协程深入理解：掌握异步编程的核心概念",
                author = "技术大牛",
                publishTime = "5小时前",
                likeCount = 256,
                commentCount = 89,
                isHot = true,
                videoCover = "cover2",  // 对应 res/drawable/cover2.png
                videoUrl = "video2"      // 对应 res/raw/video2.mp4
            ),
            Content(
                id = "3",
                title = "MVVM架构模式在Android开发中的最佳实践",
                author = "架构师老李",
                publishTime = "1天前",
                likeCount = 89,
                commentCount = 23,
                isHot = false
            ),
            Content(
                id = "4",
                title = "Material Design 3 设计规范详解",
                author = "UI设计师",
                publishTime = "2天前",
                likeCount = 156,
                commentCount = 34,
                isHot = false
            ),
            Content(
                id = "5",
                title = "Android性能优化：内存泄漏检测与修复",
                author = "性能专家",
                publishTime = "3小时前",
                likeCount = 312,
                commentCount = 67,
                isHot = true,
                videoCover = "cover5",  // 对应 res/drawable/cover5.png
                videoUrl = "video5"      // 对应 res/raw/video5.mp4
            ),
            Content(
                id = "6",
                title = "Room数据库使用指南：本地数据持久化方案",
                author = "数据工程师",
                publishTime = "6小时前",
                likeCount = 78,
                commentCount = 19,
                isHot = false
            ),
            Content(
                id = "7",
                title = "Retrofit网络请求框架实战：构建高效的API客户端",
                author = "后端开发者",
                publishTime = "4小时前",
                likeCount = 201,
                commentCount = 56,
                isHot = true,
                videoCover = "cover7",  // 对应 res/drawable/cover7.png
                videoUrl = "video7"      // 对应 res/raw/video7.mp4
            ),
            Content(
                id = "8",
                title = "Compose状态管理：StateFlow vs LiveData",
                author = "技术博主",
                publishTime = "1天前",
                likeCount = 134,
                commentCount = 42,
                isHot = false
            ),
            Content(
                id = "9",
                title = "Android动画效果实现：从基础到高级",
                author = "动画设计师",
                publishTime = "8小时前",
                likeCount = 167,
                commentCount = 38,
                isHot = false
            ),
            Content(
                id = "10",
                title = "Jetpack Navigation组件：实现流畅的页面导航",
                author = "导航专家",
                publishTime = "12小时前",
                likeCount = 98,
                commentCount = 25,
                isHot = false
            ),
            Content(
                id = "11",
                title = "Android安全开发：数据加密与权限管理",
                author = "安全专家",
                publishTime = "1天前",
                likeCount = 245,
                commentCount = 71,
                isHot = true
            ),
            Content(
                id = "12",
                title = "Kotlin DSL构建Gradle脚本：提升构建效率",
                author = "构建工程师",
                publishTime = "2天前",
                likeCount = 112,
                commentCount = 29,
                isHot = false
            )
        )
    }
}

