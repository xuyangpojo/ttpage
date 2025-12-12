package com.xuyang.ttpage.model.repository

import com.xuyang.ttpage.model.data.Topic
import com.xuyang.ttpage.model.data.Video
import kotlinx.coroutines.delay

/**
 * TopicRepository 话题数据仓库
 * @brief .
 * @author xuyang
 * @date 2025-12-10
 */
class TopicRepository {
    
    suspend fun getTopics(): List<Topic> {
        return listOf(
            Topic(id = "all", name = "全部"),
            Topic(id = "hot", name = "热点"),
            Topic(id = "news", name = "新闻"),
            Topic(id = "vlog", name = "Vlog")
        )
    }
    
    suspend fun getVideosByTopic(topicId: String): List<Video> {
        val allVideos = VideoRepository().getRecommendedVideos()
        if (topicId == "all") {
            return allVideos
        }
        if (topicId == "hot") {
            return allVideos.filter { it.isHot }
        }
        return allVideos.filter { video ->
            video.topics.isNotEmpty() && topicId in video.topics
        }
    }

}
