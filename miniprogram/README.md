# 微信小程序前端说明

## 项目架构

```
miniprogram/
├── app.ts                  # 全局入口（App 生命周期）
├── app.json                # 全局配置（页面注册、窗口样式）
├── app.less                # 全局样式
├── pages/                  # 页面目录（每个页面一个子目录）
│   ├── index/              # 首页
│   │   ├── index.ts        # 页面逻辑和数据
│   │   ├── index.wxml      # 页面结构（类似 HTML）
│   │   ├── index.less      # 页面样式
│   │   └── index.json      # 页面局部配置
│   └── logs/               # 日志页（模板示例，可删除）
├── components/             # 公共组件
│   └── navigation-bar/     # 导航栏组件
├── api/
│   ├── generated/          # openapi-generator 生成的接口客户端（勿手写）
│   ├── instances.ts        # *ControllerApi 单例（页面 import 入口）
│   ├── api-config.ts       # Configuration + wxFetch 注入
│   ├── wx-fetch.ts         # wx.request → fetch 适配器
│   ├── auth.ts             # 登录编排（token 存取 + ensureLogin）
│   └── token.ts            # token 读写
└── utils/                  # 工具函数
```

---

## 一个页面由四个文件组成

微信小程序每个页面固定由四个同名文件构成：

| 文件 | 对应 Web | 作用 |
|------|---------|------|
| `.wxml` | HTML | 页面结构和模板 |
| `.less` | CSS | 页面样式 |
| `.ts` | JavaScript | 页面逻辑和数据 |
| `.json` | 无对应 | 页面局部配置（覆盖全局配置） |

---

## 配置文件说明

### `app.json` — 全局运行配置（最核心）

```json
{
  "pages": [
    "pages/index/index",   ← 第一项即为启动页（默认展示页面）
    "pages/logs/logs"
  ],
  "window": {
    "navigationStyle": "custom"  ← 自定义导航栏，隐藏默认标题栏
  }
}
```

**所有新页面都必须在 `pages` 数组中注册，否则无法访问。**

**修改默认启动页**：将目标页面路径移到 `pages` 数组第一位即可。

---

### `app.ts` — 全局逻辑入口

小程序启动时第一个执行的文件，包含：

- `onLaunch` — 小程序初始化时执行，**整个生命周期只执行一次**
- `globalData` — 全局共享数据，所有页面均可读写

适合在这里处理：登录态初始化、全局配置加载等。

---

### `app.less` — 全局样式

对所有页面生效的样式，适合定义：
- CSS 变量（主题色、字体大小等）
- 全局 reset 样式
- 通用工具类

---

### 页面级 `*.json` — 局部配置

每个页面自己的 `.json` 可覆盖 `app.json` 中的全局配置，仅对当前页面生效。常用于：

```json
{
  "usingComponents": {
    "navigation-bar": "/components/navigation-bar/navigation-bar"
  },
  "navigationBarTitleText": "页面标题",
  "backgroundColor": "#f5f5f5"
}
```

---

## 为什么预览时默认展示 `pages/index/index`

微信小程序规定：**`app.json` 中 `pages` 数组的第一项就是启动页**。

预览或真机调试时，框架会自动打开第一个注册的页面。要修改默认启动页，只需调整 `pages` 数组中的顺序。

---

## 新建页面的步骤

1. 在 `pages/` 下创建新目录，例如 `pages/home/`
2. 创建四个文件：`home.ts`、`home.wxml`、`home.less`、`home.json`
3. 在 `app.json` 的 `pages` 数组中注册：`"pages/home/home"`

---

## 接口调用规范

不允许手写 `wx.request` 调用后端接口。

正确流程：
1. 后端启动后执行 `./gradlew generateOpenApi` 导出 `openapi/openapi.json`
2. 在项目根目录执行 `npm run generate:api` 生成 TypeScript 客户端到 `api/generated/`
3. 页面中 `import { xxxApi } from '../../api/instances'` 调用生成的方法；类型从 `api/generated/models/XxxDto` 等**具体文件**导入

> **注意**：微信小程序不支持 `from './generated'` 或 `from './generated/models'` 这类目录导入，必须写到具体文件，如 `./generated/runtime`、`./generated/apis/ArticleControllerApi`、`./generated/models/TopicItemDto`。

接口变更后重新生成，编译期即可发现调用错误。
