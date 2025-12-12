# TTPage - 双列推荐系统

一个基于 Jetpack Compose 开发的 Android 视频推荐应用，采用 MVVM 架构，支持双列布局、视频播放、话题切换、评论互动等功能。

## 📱 项目介绍

TTPage 是一个现代化的 Android 应用，展示了如何使用 Jetpack Compose 构建流畅的用户界面。应用采用双列瀑布流布局展示视频内容，支持多话题切换、视频播放、评论互动等核心功能。


ai使用：
### 核心特性


`./gradlew test` 运行单元测试 
`./gradlew connectedAndroidTest` 运行集成测试
- 🎬 **双列推荐布局** - 左右两列瀑布流展示，支持同步滚动
- 🎯 **多话题切换** - 支持横向滑动切换不同话题，每个话题独立缓存
- 📹 **视频播放** - 集成 ExoPlayer，支持播放/暂停、进度控制
- 💬 **评论系统** - 支持评论、回复、点赞功能
- 👤 **用户系统** - 登录、注册、用户信息管理
- 🔄 **下拉刷新** - 支持下拉刷新和上拉加载更多
- 🎨 **Material 3 设计** - 现代化的 UI 设计

## ✨ 功能特性表格

| 功能模块 | 功能点 | 状态 |
|---------|--------|------|
| **基础功能** | 首页双列推荐页面（左右两列布局） | ✅ |
| | 内容实体（标题、作者、时间、点赞、评论、热门标签） | ✅ |
| | 点击内容跳转到详情页 | ✅ |
| | 详情页滑动返回主页 | ✅ |
| **视频功能** | 实体增加视频封面和视频文件属性 | ✅ |
| | 视频播放器功能（播放、暂停、进度条） | ✅ |
| **话题功能** | 话题切换（横向滑动） | ✅ |
| | 每个话题独立缓存 | ✅ |
| | 话题预加载 | ✅ |
| **评论功能** | 评论列表展示 | ✅ |
| | 添加评论 | ✅ |
| | 回复评论 | ✅ |
| | 点赞/取消点赞 | ✅ |
| **用户功能** | 底部导航栏（首页+我的） | ✅ |
| | 我的页面（显示用户信息、登录/登出） | ✅ |
| | 登录页面（用户名密码登录） | ✅ |
| | 注册页面（用户名、邮箱、密码注册） | ✅ |
| | 首页点击返回顶部并刷新功能 | ✅ |
| **交互功能** | 下拉刷新 | ✅ |
| | 上拉加载更多 | ✅ |
| | 双列同步滚动 | ✅ |
| **测试** | 单元测试（Content、Repository、ViewModel） | ✅ |
| | 集成测试（ResourceHelper、UI测试） | ✅ |

## 🛠️ 技术栈

### 核心技术
- **语言**: Kotlin
- **UI框架**: Jetpack Compose（声明式UI）
- **架构模式**: MVVM（Model-View-ViewModel）
- **最低SDK**: Android 7.0 (API 24)
- **目标SDK**: Android 14 (API 36)
- **构建工具**: Gradle (Kotlin DSL)

### 主要依赖库
- `androidx.compose.material3` - Material 3 设计组件
- `androidx.compose.foundation` - Compose Foundation 库（HorizontalPager、VerticalPager）
- `androidx.lifecycle.viewmodel.compose` - ViewModel 支持
- `androidx.navigation.compose` - Navigation 导航组件
- `androidx.media3.exoplayer` - ExoPlayer 视频播放器
- `androidx.media3.ui` - ExoPlayer UI 组件
- `coil-compose` - 图片加载库
- `accompanist.swiperefresh` - 下拉刷新组件

## 📁 项目结构

```
app/src/main/java/com/xuyang/ttpage/
├── MainActivity.kt              # 主Activity（应用入口）
│
├── model/                        # Model层：数据模型和仓库
│   ├── data/
│   │   ├── Video.kt             # 视频数据模型
│   │   ├── Topic.kt            # 话题数据模型
│   │   ├── Comment.kt          # 评论数据模型
│   │   └── User.kt             # 用户数据模型
│   └── repository/
│       ├── VideoRepository.kt  # 视频数据仓库
│       ├── TopicRepository.kt  # 话题数据仓库
│       └── CommentRepository.kt # 评论数据仓库
│
├── viewmodel/                    # ViewModel层：状态管理
│   ├── HomeViewModel.kt         # 首页视图模型（支持多话题缓存）
│   ├── TopicViewModel.kt       # 话题视图模型
│   ├── CommentViewModel.kt      # 评论视图模型
│   └── UserViewModel.kt         # 用户视图模型
│
├── ui/                           # View层：UI界面
│   ├── MainScreen.kt            # 主屏幕容器（包含底部导航栏）
│   ├── theme/                   # 主题配置
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   ├── screens/                 # 页面
│   │   ├── HomeScreen.kt       # 首页（双列推荐，支持话题切换）
│   │   ├── DetailScreen.kt     # 详情页（显示内容和视频）
│   │   ├── ProfileScreen.kt    # 我的页面
│   │   ├── LoginScreen.kt      # 登录页面
│   │   └── RegisterScreen.kt   # 注册页面
│   └── components/             # 可复用组件
│       ├── VideoPlayer.kt      # 视频播放器组件
│       ├── CommentSection.kt   # 评论区组件
│       └── BottomNavigationBar.kt # 底部导航栏组件
│
├── navigation/                   # 导航相关
│   ├── NavGraph.kt             # 导航图配置
│   └── Screen.kt               # 路由定义
│
└── util/                         # 工具类
    └── ResourceHelper.kt        # 资源工具类（本地资源URI）
```

## 🚀 快速开始

### 环境要求
- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 11 或更高版本
- Android SDK 24 或更高版本

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd TTPage
   ```

2. **打开项目**
   - 使用 Android Studio 打开项目
   - 等待 Gradle 同步完成

3. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击运行按钮或使用快捷键 `Shift+F10`
   - 或使用命令行：`./gradlew installDebug`

### 资源文件配置

#### 视频资源
- 视频文件放在 `app/src/main/res/raw/` 目录（如 `video1.mp4`）
- 在代码中使用资源名称（不含扩展名），如 `"video1"`

#### 图片资源
- 图片文件放在 `app/src/main/res/drawable/` 目录（如 `cover1.png`）
- 在代码中使用资源名称（不含扩展名），如 `"cover1"`

#### 用户头像
- 头像文件放在 `app/src/main/res/drawable/` 目录
- 命名规则：`avatar_{authorId}.png`（如 `avatar_u101.png`）

详细说明请查看 `docs/` 目录下的相关文档。

## 📖 开发规范

### 代码风格
- 遵循 Kotlin 官方编码规范
- 使用 4 个空格缩进
- 类名使用 PascalCase
- 函数和变量名使用 camelCase
- 常量使用 UPPER_SNAKE_CASE

### 架构规范
- **MVVM 架构**：严格分离 Model、View、ViewModel
- **单一职责**：每个类/函数只负责一个功能
- **依赖注入**：使用 ViewModel 的 `viewModel()` 函数进行依赖注入
- **状态管理**：使用 StateFlow 管理 UI 状态

### 命名规范
- **Screen**：以 `Screen` 结尾（如 `HomeScreen.kt`）
- **Component**：使用描述性名称（如 `VideoPlayer.kt`）
- **ViewModel**：以 `ViewModel` 结尾（如 `HomeViewModel.kt`）
- **Repository**：以 `Repository` 结尾（如 `VideoRepository.kt`）

### Git 提交规范
- 使用有意义的提交信息
- 格式：`<type>: <description>`
- 类型：`feat`（新功能）、`fix`（修复）、`docs`（文档）、`refactor`（重构）

### 测试规范
- 每个 ViewModel 都应该有对应的单元测试
- 关键业务逻辑需要测试覆盖
- 使用 `Given-When-Then` 模式编写测试

详细测试说明请查看 `docs/测试说明.md`。

## 📚 文档

项目文档位于 `docs/` 目录：

- `项目结构说明.md` - 详细的项目结构说明
- `测试说明.md` - 测试相关文档
- `本地资源使用说明.md` - 资源文件使用指南
- `用户头像使用说明.md` - 用户头像配置说明
- `应用图标添加说明.md` - 应用图标配置说明

## 🧪 测试

### 运行单元测试
```bash
./gradlew test
```

### 运行集成测试
```bash
./gradlew connectedAndroidTest
```

### 测试覆盖率
- ✅ Model层：数据模型和Repository
- ✅ ViewModel层：所有ViewModel
- ✅ Util层：工具类
- ✅ View层：主要Screen组件

详细测试说明请查看 `docs/测试说明.md`。

## 💡 常见问题

**Q: 如何添加新的页面？**
A: 
1. 在 `ui/screens/` 目录下创建新的 Screen Composable函数
2. 在 `navigation/Screen.kt` 中添加路由定义
3. 在 `navigation/NavGraph.kt` 中添加 `composable` 路由配置
4. 如果需要显示在底部导航栏，在 `ui/components/BottomNavigationBar.kt` 中添加导航项

**Q: 如何实现话题切换功能？**
A: 
- 使用 `TopicViewModel` 管理话题状态
- 使用 `HomeViewModel` 的 `videosByTopic` 为每个话题维护独立的视频列表
- 在 `HomeScreen` 中使用 `HorizontalPager` 实现横向滑动切换

**Q: 如何添加网络请求？**
A: 在 `build.gradle.kts` 中添加 Retrofit 或 Ktor 等网络库依赖，然后在 `model/repository/` 中实现数据获取逻辑。

**Q: 视频播放器支持哪些格式？**
A: ExoPlayer 支持 MP4、WebM、MKV 等常见视频格式。建议使用 MP4 格式以获得最佳兼容性。

更多问题请查看 `docs/` 目录下的相关文档。

## 🔄 更新日志

### v1.0 (当前版本)
- ✅ 实现双列推荐布局
- ✅ 支持多话题切换和独立缓存
- ✅ 集成视频播放功能
- ✅ 实现评论系统
- ✅ 实现用户登录/注册功能
- ✅ 支持下拉刷新和上拉加载更多
- ✅ 完成单元测试和集成测试

## 📄 开源协议

本项目采用 MIT 协议开源。详情请查看 LICENSE 文件。

## 👥 贡献

欢迎提交 Issue 和 Pull Request！

## 📧 联系方式

如有问题或建议，请通过 Issue 联系。