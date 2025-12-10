package com.xuyang.ttpage.model.repository

import com.xuyang.ttpage.model.data.Comment
import kotlinx.coroutines.delay

/**
 * Model层：评论数据仓库
 */
class CommentRepository {
    
    /**
     * 获取视频的评论列表（扁平化结构，包含顶级评论和回复）
     */
    suspend fun getComments(videoId: String): List<Comment> {
        delay(500)
        
        // 返回扁平化的评论列表，通过 parentCommentId 来标识父子关系
        return listOf(
            // 顶级评论1
            Comment(
                id = 1L,
                videoId = videoId.toLongOrNull() ?: 1L,
                authorId = 1L,
                authorName = "用户A",
                content = "这个内容很不错，学到了很多！",
                publishTime = "1小时前",
                likeCount = 15u,
                replyCount = 2u,
                parentCommentId = null
            ),
            // 评论1的回复1
            Comment(
                id = 2L,
                videoId = videoId.toLongOrNull() ?: 1L,
                authorId = 2L,
                authorName = "用户B",
                content = "同感！",
                publishTime = "30分钟前",
                likeCount = 3u,
                replyCount = 0u,
                parentCommentId = "1"
            ),
            // 评论1的回复2
            Comment(
                id = 3L,
                videoId = videoId.toLongOrNull() ?: 1L,
                authorId = 3L,
                authorName = "用户C",
                content = "确实很有帮助",
                publishTime = "20分钟前",
                likeCount = 1u,
                replyCount = 0u,
                parentCommentId = "1"
            ),
            // 顶级评论2
            Comment(
                id = 4L,
                videoId = videoId.toLongOrNull() ?: 1L,
                authorId = 4L,
                authorName = "用户D",
                content = "希望能有更多这样的内容",
                publishTime = "2小时前",
                likeCount = 8u,
                replyCount = 1u,
                parentCommentId = null
            ),
            // 评论2的回复1
            Comment(
                id = 5L,
                videoId = videoId.toLongOrNull() ?: 1L,
                authorId = 5L,
                authorName = "用户E",
                content = "支持！",
                publishTime = "1小时前",
                likeCount = 2u,
                replyCount = 0u,
                parentCommentId = "4"
            ),
            // 顶级评论3
            Comment(
                id = 6L,
                videoId = videoId.toLongOrNull() ?: 1L,
                authorId = 6L,
                authorName = "用户F",
                content = "感谢分享",
                publishTime = "3小时前",
                likeCount = 5u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 添加评论
     */
    suspend fun addComment(videoId: String, content: String, parentCommentId: String? = null): Comment {
        delay(300)
        return Comment(
            id = System.currentTimeMillis(),
            videoId = videoId.toLongOrNull() ?: 1L,
            authorId = 999L, // 当前用户ID
            authorName = "当前用户",
            content = content,
            publishTime = "刚刚",
            likeCount = 0u,
            replyCount = 0u,
            parentCommentId = parentCommentId
        )
    }
    
    /**
     * 点赞评论
     */
    suspend fun likeComment(commentId: String): Boolean {
        delay(200)
        return true
    }
    
    /**
     * 取消点赞评论
     */
    suspend fun unlikeComment(commentId: String): Boolean {
        delay(200)
        return true
    }
}

