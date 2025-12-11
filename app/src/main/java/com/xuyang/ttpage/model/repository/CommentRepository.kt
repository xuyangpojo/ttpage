package com.xuyang.ttpage.model.repository

import com.xuyang.ttpage.model.data.Comment
import kotlinx.coroutines.delay

/**
 * CommentRepository 评论数据仓库
 * @brief .
 * @author xuyang
 * @date 2025-12-10
 */
class CommentRepository {
    
    suspend fun getComments(videoId: String): List<Comment> {
        return listOf(
            // 顶级评论1
            Comment(
                id = "c001",
                videoId = videoId,
                authorId = "u001",
                authorName = "用户A",
                content = "这个内容很不错，学到了很多！",
                publishTime = "1小时前",
                likeCount = 15u,
                replyCount = 2u,
                parentCommentId = null
            ),
            // 评论1的回复1
            Comment(
                id = "c002",
                videoId = videoId,
                authorId = "u002",
                authorName = "用户B",
                content = "同感！",
                publishTime = "30分钟前",
                likeCount = 3u,
                replyCount = 0u,
                parentCommentId = "c001"
            ),
            // 评论1的回复2
            Comment(
                id = "c003",
                videoId = videoId,
                authorId = "u003",
                authorName = "用户C",
                content = "确实很有帮助",
                publishTime = "20分钟前",
                likeCount = 1u,
                replyCount = 0u,
                parentCommentId = "c001"
            ),
            // 顶级评论2
            Comment(
                id = "c004",
                videoId = videoId,
                authorId = "u004",
                authorName = "用户D",
                content = "希望能有更多这样的内容",
                publishTime = "2小时前",
                likeCount = 8u,
                replyCount = 1u,
                parentCommentId = null
            ),
            // 评论2的回复1
            Comment(
                id = "c005",
                videoId = videoId,
                authorId = "u005",
                authorName = "用户E",
                content = "支持！",
                publishTime = "1小时前",
                likeCount = 2u,
                replyCount = 0u,
                parentCommentId = "c004"
            ),
            // 顶级评论3
            Comment(
                id = "c006",
                videoId = videoId,
                authorId = "u006",
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
            id = System.currentTimeMillis().toString(),
            videoId = videoId,
            authorId = "u001",
            authorName = "当前用户",
            content = content,
            publishTime = "刚刚",
            likeCount = 0u,
            replyCount = 0u,
            parentCommentId = parentCommentId
        )
    }

    suspend fun likeComment(commentId: String): Boolean {
        delay(100)
        return true
    }
    
    suspend fun unlikeComment(commentId: String): Boolean {
        delay(100)
        return true
    }
}

