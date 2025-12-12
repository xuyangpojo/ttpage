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
                title = "【钢琴练习vlog】卡农",
                authorId = "u001",
                authorName = "钢琴爱好者小风",
                publishTime = "2小时前",
                likeCount = 128u,
                commentCount = 45u,
                isHot = true,
                // app/src/main/res/drawable/f001.png
                videoCover = "f001",
                // app/src/main/res/raw/v001.mp4
                videoUrl = "v001",
                topics = listOf("vlog")
            ),
            Video(
                id = "v002",
                title = "快舟一号甲火箭升空",
                authorId = "u002",
                authorName = "科技日报",
                publishTime = "5小时前",
                likeCount = 256u,
                commentCount = 89u,
                isHot = false,
                videoCover = "f002",
                videoUrl = "v002",
                topics = listOf("news")
            ),
            Video(
                id = "v003",
                title = "【天气】河南成为强降雪中心",
                authorId = "u003",
                authorName = "澎湃新闻",
                publishTime = "1天前",
                likeCount = 89u,
                commentCount = 23u,
                isHot = false,
                videoCover = "f003",
                videoUrl = "v003",
                topics = listOf("news")
            ),
            Video(
                id = "v004",
                title = "电吉他练习vlog",
                authorId = "u004",
                authorName = "摇滚史密斯",
                publishTime = "2天前",
                likeCount = 156u,
                commentCount = 34u,
                isHot = false,
                videoCover = "f004",
                videoUrl = "v004",
                topics = listOf("vlog")
            ),
            Video(
                id = "v005",
                title = "【练习】We Go aespa",
                authorId = "u005",
                authorName = "吉他手小张",
                publishTime = "3小时前",
                likeCount = 312u,
                commentCount = 67u,
                isHot = true,
                videoCover = "f005",
                videoUrl = "v005",
                topics = listOf("vlog")
            ),
            Video(
                id = "v006",
                title = "【Vlog】乐器合奏",
                authorId = "u006",
                authorName = "音乐工作室",
                publishTime = "6小时前",
                likeCount = 78u,
                commentCount = 19u,
                isHot = false,
                videoCover = "f006",
                videoUrl = "v006",
                topics = listOf("vlog")
            ),
            Video(
                id = "v007",
                title = "【世界旅行】澳大利亚",
                authorId = "u007",
                authorName = "旅行日记",
                publishTime = "4小时前",
                likeCount = 201u,
                commentCount = 56u,
                isHot = true,
                videoCover = "f007",
                videoUrl = "v007",
                topics = listOf("vlog")
            ),
            Video(
                id = "v008",
                title = "汽车销量创历史新高",
                authorId = "u008",
                authorName = "汽车之家",
                publishTime = "1天前",
                likeCount = 134u,
                commentCount = 42u,
                isHot = false,
                videoCover = "f008",
                videoUrl = "v008",
                topics = listOf("news")
            ),
            Video(
                id = "v009",
                title = "九天无人机首飞成功",
                authorId = "u009",
                authorName = "航空科技",
                publishTime = "8小时前",
                likeCount = 167u,
                commentCount = 38u,
                isHot = false,
                videoCover = "f009",
                videoUrl = "v009",
                topics = listOf("news")
            ),
            Video(
                id = "v010",
                title = "钢琴演奏",
                authorId = "u010",
                authorName = "钢琴家",
                publishTime = "12小时前",
                likeCount = 98u,
                commentCount = 25u,
                isHot = false,
                videoCover = "f010",
                videoUrl = "v010",
                topics = listOf("vlog")
            ),
            Video(
                id = "v011",
                title = "钢琴演奏练习",
                authorId = "u011",
                authorName = "音乐家",
                publishTime = "6天前",
                likeCount = 245u,
                commentCount = 71u,
                isHot = true,
                videoCover = "f011",
                videoUrl = "v011",
                topics = listOf("vlog")
            ),
            Video(
                id = "v012",
                title = "【旅行Vlog】重庆",
                authorId = "u012",
                authorName = "旅行达人",
                publishTime = "1月前",
                likeCount = 245u,
                commentCount = 71u,
                isHot = true,
                videoCover = "f012",
                videoUrl = "v012",
                topics = listOf("vlog")
            ),
            Video(
                id = "v013",
                title = "电影票房最新资讯",
                authorId = "u013",
                authorName = "娱乐资讯",
                publishTime = "2天前",
                likeCount = 245u,
                commentCount = 71u,
                isHot = true,
                videoCover = "f013",
                videoUrl = "v013",
                topics = listOf("news")
            ),
            Video(
                id = "v014",
                title = "国庆出游含“8”量很高",
                authorId = "u014",
                authorName = "旅行日记",
                publishTime = "1天前",
                likeCount = 245u,
                commentCount = 71u,
                isHot = false,
                videoCover = "f014",
                videoUrl = "v014",
                topics = listOf("news")
            ),
            Video(
                id = "v015",
                title = "诺贝尔物理学奖",
                authorId = "u015",
                authorName = "科学时报",
                publishTime = "3天前",
                likeCount = 245u,
                commentCount = 71u,
                isHot = true,
                videoCover = "f015",
                videoUrl = "v015",
                topics = listOf("news")
            ),
            Video(
                id = "v016",
                title = "紫色莲花",
                authorId = "u016",
                authorName = "自然探索",
                publishTime = "7天前",
                likeCount = 245u,
                commentCount = 71u,
                isHot = true,
                videoCover = "f016",
                videoUrl = "v016",
                topics = listOf("news")
            ),
            Video(
                id = "v017",
                title = "【新闻】日本地震",
                authorId = "u017",
                authorName = "央视新闻",
                publishTime = "1天前",
                likeCount = 245u,
                commentCount = 71u,
                isHot = false,
                videoCover = "f017",
                videoUrl = "v017",
                topics = listOf("news")
            ),
            Video(
                id = "v018",
                title = "体育赛事",
                authorId = "u018",
                authorName = "体育频道",
                publishTime = "2天前",
                likeCount = 245u,
                commentCount = 71u,
                isHot = true,
                videoCover = "f018",
                videoUrl = "v018",
                topics = listOf("news")
            )
        )
    }
}
