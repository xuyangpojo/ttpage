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
        return when (videoId) {
            "v001" -> getVideo1Comments()
            "v002" -> getVideo2Comments()
            "v003" -> getVideo3Comments()
            "v004" -> getVideo4Comments()
            "v005" -> getVideo5Comments()
            "v006" -> getVideo6Comments()
            "v007" -> getVideo7Comments()
            "v008" -> getVideo8Comments()
            "v009" -> getVideo9Comments()
            "v010" -> getVideo10Comments()
            "v011" -> getVideo11Comments()
            "v012" -> getVideo12Comments()
            "v013" -> getVideo13Comments()
            "v014" -> getVideo14Comments()
            "v015" -> getVideo15Comments()
            "v016" -> getVideo16Comments()
            "v017" -> getVideo17Comments()
            "v018" -> getVideo18Comments()
            else -> emptyList()
        }
    }
    
    /**
     * 视频1的评论：10多条评论，前3个评论有回复结构
     */
    private fun getVideo1Comments(): List<Comment> {
        return listOf(
            // 顶级评论1 - 有回复
            Comment(
                id = "v001_c001",
                videoId = "v001",
                authorId = "u101",
                authorName = "音乐爱好者小王",
                content = "卡农这首曲子太经典了！弹得真好，音色很纯净",
                publishTime = "1小时前",
                likeCount = 45u,
                replyCount = 3u,
                parentCommentId = null
            ),
            // 评论1的回复1
            Comment(
                id = "v001_c002",
                videoId = "v001",
                authorId = "u102",
                authorName = "钢琴学习者",
                content = "同感！我也在练习这首曲子，难度不小",
                publishTime = "50分钟前",
                likeCount = 12u,
                replyCount = 0u,
                parentCommentId = "v001_c001"
            ),
            // 评论1的回复2
            Comment(
                id = "v001_c003",
                videoId = "v001",
                authorId = "u103",
                authorName = "古典音乐迷",
                content = "卡农的复调结构真的很美，每次听都有新感受",
                publishTime = "40分钟前",
                likeCount = 8u,
                replyCount = 0u,
                parentCommentId = "v001_c001"
            ),
            // 评论1的回复3
            Comment(
                id = "v001_c004",
                videoId = "v001",
                authorId = "u104",
                authorName = "音乐老师",
                content = "节奏把握得很好，继续加油！",
                publishTime = "30分钟前",
                likeCount = 5u,
                replyCount = 0u,
                parentCommentId = "v001_c001"
            ),
            // 顶级评论2 - 有回复
            Comment(
                id = "v001_c005",
                videoId = "v001",
                authorId = "u105",
                authorName = "练琴日记",
                content = "每天坚持练习真的很不容易，向你学习！",
                publishTime = "2小时前",
                likeCount = 32u,
                replyCount = 2u,
                parentCommentId = null
            ),
            // 评论2的回复1
            Comment(
                id = "v001_c006",
                videoId = "v001",
                authorId = "u106",
                authorName = "坚持就是胜利",
                content = "一起加油！每天进步一点点",
                publishTime = "1小时30分钟前",
                likeCount = 10u,
                replyCount = 0u,
                parentCommentId = "v001_c005"
            ),
            // 评论2的回复2
            Comment(
                id = "v001_c007",
                videoId = "v001",
                authorId = "u107",
                authorName = "钢琴新手",
                content = "我也要开始每天练习了！",
                publishTime = "1小时前",
                likeCount = 6u,
                replyCount = 0u,
                parentCommentId = "v001_c005"
            ),
            // 顶级评论3 - 有回复
            Comment(
                id = "v001_c008",
                videoId = "v001",
                authorId = "u108",
                authorName = "艺术欣赏家",
                content = "钢琴的音色太美了，这种vlog形式很棒，能看到真实的练习过程",
                publishTime = "3小时前",
                likeCount = 28u,
                replyCount = 2u,
                parentCommentId = null
            ),
            // 评论3的回复1
            Comment(
                id = "v001_c009",
                videoId = "v001",
                authorId = "u109",
                authorName = "视频创作者",
                content = "确实，vlog能记录成长过程，很有意义",
                publishTime = "2小时30分钟前",
                likeCount = 9u,
                replyCount = 0u,
                parentCommentId = "v001_c008"
            ),
            // 评论3的回复2
            Comment(
                id = "v001_c010",
                videoId = "v001",
                authorId = "u110",
                authorName = "音乐分享者",
                content = "希望看到更多这样的内容！",
                publishTime = "2小时前",
                likeCount = 7u,
                replyCount = 0u,
                parentCommentId = "v001_c008"
            ),
            // 顶级评论4
            Comment(
                id = "v001_c011",
                videoId = "v001",
                authorId = "u111",
                authorName = "钢琴爱好者",
                content = "请问这是什么型号的钢琴？音色很好听",
                publishTime = "4小时前",
                likeCount = 18u,
                replyCount = 0u,
                parentCommentId = null
            ),
            // 顶级评论5
            Comment(
                id = "v001_c012",
                videoId = "v001",
                authorId = "u112",
                authorName = "练习者",
                content = "卡农是我最喜欢的曲子之一，每次听都很感动",
                publishTime = "5小时前",
                likeCount = 24u,
                replyCount = 0u,
                parentCommentId = null
            ),
            // 顶级评论6
            Comment(
                id = "v001_c013",
                videoId = "v001",
                authorId = "u113",
                authorName = "音乐学习者",
                content = "弹得真不错！我也在学钢琴，希望能达到这个水平",
                publishTime = "6小时前",
                likeCount = 15u,
                replyCount = 0u,
                parentCommentId = null
            ),
            // 顶级评论7
            Comment(
                id = "v001_c014",
                videoId = "v001",
                authorId = "u114",
                authorName = "古典音乐爱好者",
                content = "卡农的旋律太经典了，百听不厌",
                publishTime = "7小时前",
                likeCount = 20u,
                replyCount = 0u,
                parentCommentId = null
            ),
            // 顶级评论8
            Comment(
                id = "v001_c015",
                videoId = "v001",
                authorId = "u115",
                authorName = "钢琴练习者",
                content = "每天坚持练习真的很不容易，为你点赞！",
                publishTime = "8小时前",
                likeCount = 16u,
                replyCount = 0u,
                parentCommentId = null
            ),
            // 顶级评论9
            Comment(
                id = "v001_c016",
                videoId = "v001",
                authorId = "u116",
                authorName = "音乐分享",
                content = "这种练习vlog很有意义，能看到真实的进步过程",
                publishTime = "9小时前",
                likeCount = 12u,
                replyCount = 0u,
                parentCommentId = null
            ),
            // 顶级评论10
            Comment(
                id = "v001_c017",
                videoId = "v001",
                authorId = "u117",
                authorName = "艺术欣赏",
                content = "音色处理得很好，细节把握到位",
                publishTime = "10小时前",
                likeCount = 14u,
                replyCount = 0u,
                parentCommentId = null
            ),
            // 顶级评论11
            Comment(
                id = "v001_c018",
                videoId = "v001",
                authorId = "u118",
                authorName = "钢琴学习者",
                content = "希望看到更多这样的练习视频，很有启发",
                publishTime = "11小时前",
                likeCount = 11u,
                replyCount = 0u,
                parentCommentId = null
            ),
            // 顶级评论12
            Comment(
                id = "v001_c019",
                videoId = "v001",
                authorId = "u119",
                authorName = "音乐爱好者",
                content = "卡农这首曲子难度不小，能弹成这样已经很棒了",
                publishTime = "12小时前",
                likeCount = 19u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频2的评论：2-4条
     */
    private fun getVideo2Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v002_c001",
                videoId = "v002",
                authorId = "u201",
                authorName = "科技爱好者",
                content = "火箭升空太震撼了！科技进步真快",
                publishTime = "4小时前",
                likeCount = 28u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v002_c002",
                videoId = "v002",
                authorId = "u202",
                authorName = "航天迷",
                content = "每次看到火箭发射都很激动，为祖国骄傲！",
                publishTime = "3小时前",
                likeCount = 35u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v002_c003",
                videoId = "v002",
                authorId = "u203",
                authorName = "科学探索",
                content = "希望未来能看到更多这样的科技突破",
                publishTime = "2小时前",
                likeCount = 15u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频3的评论：2-4条
     */
    private fun getVideo3Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v003_c001",
                videoId = "v003",
                authorId = "u301",
                authorName = "天气关注者",
                content = "最近的天气变化确实很大，要注意保暖",
                publishTime = "20小时前",
                likeCount = 12u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v003_c002",
                videoId = "v003",
                authorId = "u302",
                authorName = "生活资讯",
                content = "天气预报很及时，感谢分享",
                publishTime = "19小时前",
                likeCount = 8u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频4的评论：2-4条
     */
    private fun getVideo4Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v004_c001",
                videoId = "v004",
                authorId = "u401",
                authorName = "摇滚乐迷",
                content = "电吉他音色太棒了！弹得很有感觉",
                publishTime = "2天前",
                likeCount = 22u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v004_c002",
                videoId = "v004",
                authorId = "u402",
                authorName = "音乐爱好者",
                content = "这种vlog形式很棒，能看到真实的练习过程",
                publishTime = "1天23小时前",
                likeCount = 18u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v004_c003",
                videoId = "v004",
                authorId = "u403",
                authorName = "吉他手",
                content = "希望能看到更多电吉他的演奏视频",
                publishTime = "1天22小时前",
                likeCount = 14u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频5的评论：2-4条
     */
    private fun getVideo5Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v005_c001",
                videoId = "v005",
                authorId = "u501",
                authorName = "吉他学习者",
                content = "电吉他练习很不容易，向你学习！",
                publishTime = "2小时前",
                likeCount = 45u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v005_c002",
                videoId = "v005",
                authorId = "u502",
                authorName = "音乐分享",
                content = "弹得真好，技巧很娴熟",
                publishTime = "1小时30分钟前",
                likeCount = 32u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v005_c003",
                videoId = "v005",
                authorId = "u503",
                authorName = "摇滚音乐",
                content = "电吉他的音色处理得很好",
                publishTime = "1小时前",
                likeCount = 28u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v005_c004",
                videoId = "v005",
                authorId = "u504",
                authorName = "乐器爱好者",
                content = "希望看到更多练习视频，很有启发",
                publishTime = "30分钟前",
                likeCount = 19u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频6的评论：2-4条
     */
    private fun getVideo6Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v006_c001",
                videoId = "v006",
                authorId = "u601",
                authorName = "音乐工作室",
                content = "乐器vlog很有意思，能看到不同乐器的魅力",
                publishTime = "5小时前",
                likeCount = 15u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v006_c002",
                videoId = "v006",
                authorId = "u602",
                authorName = "音乐爱好者",
                content = "这种形式很棒，希望能看到更多",
                publishTime = "4小时前",
                likeCount = 12u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频7的评论：2-4条
     */
    private fun getVideo7Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v007_c001",
                videoId = "v007",
                authorId = "u701",
                authorName = "旅行爱好者",
                content = "世界旅行太棒了！风景真美",
                publishTime = "3小时前",
                likeCount = 38u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v007_c002",
                videoId = "v007",
                authorId = "u702",
                authorName = "旅游达人",
                content = "这些地方我也想去，感谢分享",
                publishTime = "2小时30分钟前",
                likeCount = 25u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v007_c003",
                videoId = "v007",
                authorId = "u703",
                authorName = "探索世界",
                content = "旅行vlog拍得真好，很有代入感",
                publishTime = "2小时前",
                likeCount = 20u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频8的评论：2-4条
     */
    private fun getVideo8Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v008_c001",
                videoId = "v008",
                authorId = "u801",
                authorName = "汽车爱好者",
                content = "汽车销量数据很有参考价值",
                publishTime = "23小时前",
                likeCount = 18u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v008_c002",
                videoId = "v008",
                authorId = "u802",
                authorName = "行业观察",
                content = "新能源汽车增长很快，趋势很明显",
                publishTime = "22小时前",
                likeCount = 22u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v008_c003",
                videoId = "v008",
                authorId = "u803",
                authorName = "汽车资讯",
                content = "希望看到更多行业分析",
                publishTime = "21小时前",
                likeCount = 14u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频9的评论：2-4条
     */
    private fun getVideo9Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v009_c001",
                videoId = "v009",
                authorId = "u901",
                authorName = "航空爱好者",
                content = "飞机制造技术太先进了！",
                publishTime = "7小时前",
                likeCount = 24u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v009_c002",
                videoId = "v009",
                authorId = "u902",
                authorName = "科技探索",
                content = "航空科技的发展令人惊叹",
                publishTime = "6小时前",
                likeCount = 19u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频10的评论：2-4条
     */
    private fun getVideo10Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v010_c001",
                videoId = "v010",
                authorId = "u1001",
                authorName = "钢琴爱好者",
                content = "钢琴演奏太美了，音色很纯净",
                publishTime = "11小时前",
                likeCount = 16u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v010_c002",
                videoId = "v010",
                authorId = "u1002",
                authorName = "音乐欣赏",
                content = "演奏技巧很娴熟，很有感染力",
                publishTime = "10小时前",
                likeCount = 13u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v010_c003",
                videoId = "v010",
                authorId = "u1003",
                authorName = "古典音乐",
                content = "希望能看到更多这样的演奏",
                publishTime = "9小时前",
                likeCount = 11u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频11的评论：2-4条
     */
    private fun getVideo11Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v011_c001",
                videoId = "v011",
                authorId = "u1101",
                authorName = "音乐家",
                content = "钢琴演奏很有感情，很打动人心",
                publishTime = "5天前",
                likeCount = 42u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v011_c002",
                videoId = "v011",
                authorId = "u1102",
                authorName = "艺术欣赏",
                content = "音色处理得很好，细节把握到位",
                publishTime = "4天23小时前",
                likeCount = 35u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v011_c003",
                videoId = "v011",
                authorId = "u1103",
                authorName = "音乐爱好者",
                content = "这种演奏水平很高，学习了",
                publishTime = "4天22小时前",
                likeCount = 28u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频12的评论：2-4条
     */
    private fun getVideo12Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v012_c001",
                videoId = "v012",
                authorId = "u1201",
                authorName = "旅行者",
                content = "国内旅行的地方都很美，值得一去",
                publishTime = "29天前",
                likeCount = 38u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v012_c002",
                videoId = "v012",
                authorId = "u1202",
                authorName = "旅游攻略",
                content = "这些地方我也去过，确实很美",
                publishTime = "28天23小时前",
                likeCount = 25u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v012_c003",
                videoId = "v012",
                authorId = "u1203",
                authorName = "探索中国",
                content = "希望看到更多国内旅行vlog",
                publishTime = "28天22小时前",
                likeCount = 20u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v012_c004",
                videoId = "v012",
                authorId = "u1204",
                authorName = "旅行日记",
                content = "风景拍得真好，很有代入感",
                publishTime = "28天21小时前",
                likeCount = 18u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频13的评论：2-4条
     */
    private fun getVideo13Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v013_c001",
                videoId = "v013",
                authorId = "u1301",
                authorName = "娱乐资讯",
                content = "电影票房数据很有意思，反映了市场趋势",
                publishTime = "1天23小时前",
                likeCount = 32u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v013_c002",
                videoId = "v013",
                authorId = "u1302",
                authorName = "电影爱好者",
                content = "这些电影我都看过，确实不错",
                publishTime = "1天22小时前",
                likeCount = 24u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v013_c003",
                videoId = "v013",
                authorId = "u1303",
                authorName = "影视观察",
                content = "希望看到更多票房分析",
                publishTime = "1天21小时前",
                likeCount = 19u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频14的评论：2-4条
     */
    private fun getVideo14Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v014_c001",
                videoId = "v014",
                authorId = "u1401",
                authorName = "旅行分享",
                content = "国庆出游的人真多，但风景很美",
                publishTime = "23小时前",
                likeCount = 28u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v014_c002",
                videoId = "v014",
                authorId = "u1402",
                authorName = "假期旅行",
                content = "我也在国庆期间出去玩了，人确实很多",
                publishTime = "22小时前",
                likeCount = 22u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频15的评论：2-4条
     */
    private fun getVideo15Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v015_c001",
                videoId = "v015",
                authorId = "u1501",
                authorName = "科学爱好者",
                content = "诺贝尔物理学奖的成果太重要了！",
                publishTime = "2天23小时前",
                likeCount = 45u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v015_c002",
                videoId = "v015",
                authorId = "u1502",
                authorName = "科研关注",
                content = "这些科学发现对人类发展意义重大",
                publishTime = "2天22小时前",
                likeCount = 38u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v015_c003",
                videoId = "v015",
                authorId = "u1503",
                authorName = "科学探索",
                content = "希望看到更多科学新闻的解读",
                publishTime = "2天21小时前",
                likeCount = 25u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频16的评论：2-4条
     */
    private fun getVideo16Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v016_c001",
                videoId = "v016",
                authorId = "u1601",
                authorName = "自然爱好者",
                content = "紫色莲花太美了！大自然的魅力无穷",
                publishTime = "6天23小时前",
                likeCount = 35u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v016_c002",
                videoId = "v016",
                authorId = "u1602",
                authorName = "植物观察",
                content = "这种颜色的莲花很少见，很珍贵",
                publishTime = "6天22小时前",
                likeCount = 28u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v016_c003",
                videoId = "v016",
                authorId = "u1603",
                authorName = "自然探索",
                content = "希望能看到更多自然奇观的视频",
                publishTime = "6天21小时前",
                likeCount = 20u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频17的评论：2-4条
     */
    private fun getVideo17Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v017_c001",
                videoId = "v017",
                authorId = "u1701",
                authorName = "新闻关注",
                content = "日本地震的消息很及时，希望灾区人民平安",
                publishTime = "23小时前",
                likeCount = 42u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v017_c002",
                videoId = "v017",
                authorId = "u1702",
                authorName = "国际新闻",
                content = "自然灾害很可怕，要做好防范",
                publishTime = "22小时前",
                likeCount = 35u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v017_c003",
                videoId = "v017",
                authorId = "u1703",
                authorName = "时事观察",
                content = "希望看到更多及时的新闻资讯",
                publishTime = "21小时前",
                likeCount = 28u,
                replyCount = 0u,
                parentCommentId = null
            )
        )
    }
    
    /**
     * 视频18的评论：2-4条
     */
    private fun getVideo18Comments(): List<Comment> {
        return listOf(
            Comment(
                id = "v018_c001",
                videoId = "v018",
                authorId = "u1801",
                authorName = "体育迷",
                content = "体育赛事太精彩了！比赛很激烈",
                publishTime = "1天23小时前",
                likeCount = 48u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v018_c002",
                videoId = "v018",
                authorId = "u1802",
                authorName = "运动爱好者",
                content = "这些运动员的表现太出色了",
                publishTime = "1天22小时前",
                likeCount = 38u,
                replyCount = 0u,
                parentCommentId = null
            ),
            Comment(
                id = "v018_c003",
                videoId = "v018",
                authorId = "u1803",
                authorName = "体育资讯",
                content = "希望看到更多精彩的体育赛事",
                publishTime = "1天21小时前",
                likeCount = 32u,
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

