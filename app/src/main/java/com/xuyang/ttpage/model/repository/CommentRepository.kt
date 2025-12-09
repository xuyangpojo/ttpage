package com.xuyang.ttpage.model.repository

import com.xuyang.ttpage.model.data.Comment
import kotlinx.coroutines.delay

/**
 * Model层：评论数据仓库
 */
class CommentRepository {
    
    /**
     * 获取视频的评论列表
     */
    suspend fun getComments(videoId: String): List<Comment> {
        delay(500)
        
        return listOf(
            Comment(
                id = "c1",
                videoId = videoId,
                authorId = "userA",
                author = "用户A",
                content = "这个内容很不错，学到了很多！",
                publishTime = "1小时前",
                likeCount = 15,
                replyCount = 2,
                replies = listOf(
                    Comment(
                        id = "c1-1",
                        videoId = videoId,
                        authorId = "userB",
                        author = "用户B",
                        content = "同感！",
                        publishTime = "30分钟前",
                        likeCount = 3,
                        replyCount = 0,
                        parentCommentId = "c1"
                    ),
                    Comment(
                        id = "c1-2",
                        videoId = videoId,
                        authorId = "userC",
                        author = "用户C",
                        content = "确实很有帮助",
                        publishTime = "20分钟前",
                        likeCount = 1,
                        replyCount = 0,
                        parentCommentId = "c1"
                    )
                )
            ),
            Comment(
                id = "c2",
                videoId = videoId,
                authorId = "userD",
                author = "用户D",
                content = "希望能有更多这样的内容",
                publishTime = "2小时前",
                likeCount = 8,
                replyCount = 1,
                replies = listOf(
                    Comment(
                        id = "c2-1",
                        videoId = videoId,
                        authorId = "userE",
                        author = "用户E",
                        content = "支持！",
                        publishTime = "1小时前",
                        likeCount = 2,
                        replyCount = 0,
                        parentCommentId = "c2"
                    )
                )
            ),
            Comment(
                id = "c3",
                videoId = videoId,
                authorId = "userF",
                author = "用户F",
                content = "感谢分享",
                publishTime = "3小时前",
                likeCount = 5,
                replyCount = 0
            )
        )
    }
    
    /**
     * 添加评论
     */
    suspend fun addComment(videoId: String, content: String, parentCommentId: String? = null): Comment {
        delay(300)
        return Comment(
            id = "c${System.currentTimeMillis()}",
            videoId = videoId,
            authorId = "currentUser",
            author = "当前用户",
            content = content,
            publishTime = "刚刚",
            likeCount = 0,
            replyCount = 0,
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

