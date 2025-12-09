package com.xuyang.ttpage.model.repository

import com.xuyang.ttpage.model.data.Topic
import com.xuyang.ttpage.model.data.Video
import kotlinx.coroutines.delay

/**
 * Model层：话题数据仓库
 */
class TopicRepository {
    
    /**
     * 获取话题列表
     */
    suspend fun getTopics(): List<Topic> {
        delay(300)
        
        return listOf(
            Topic(id = "all", name = "全部"),
            Topic(id = "tech", name = "科技"),
            Topic(id = "entertainment", name = "娱乐"),
            Topic(id = "sports", name = "体育"),
            Topic(id = "news", name = "新闻"),
            Topic(id = "education", name = "教育"),
            Topic(id = "lifestyle", name = "生活")
        )
    }
    
    /**
     * 根据话题ID获取视频列表
     */
    suspend fun getVideosByTopic(topicId: String): List<Video> {
        delay(500)
        // 暂时返回相同的视频，实际应该根据话题筛选
        return VideoRepository().getRecommendedVideos()
    }
}

