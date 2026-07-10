# 系统架构设计与技术选型

> 来源：需求文档-v0-总体设计.md 第十三章、第十四章；**以 ice 仓库 `server/`、`ai/`、`README.md` 为准**  
> 状态：已落地初版（2026-07-08 迁入 ice；2026-07-08 增补 AI 服务）

---

## 一、整体架构（三端）

```
miniprogram（微信小程序）
        │  OpenAPI 生成客户端
        ▼
server/（Kotlin Spring Boot）──────► MySQL + Redis
        │  HTTP 内网
        ▼
ai/（Python FastAPI）────────────► Provider Registry
                                        │
                    ┌───────────────────┼───────────────────┐
                    ▼                   ▼                   ▼
               deepseek              doubao                glm
                    │                   │                   │
                    └──────── langchain（可选编排层）────────┘
```

| 目录 | 职责 | 对外暴露 |
|---|---|---|
| `miniprogram/` | 用户界面、交互 | 否（微信客户端） |
| `server/` | 业务编排、鉴权、数据持久化 | 是（REST API） |
| `ai/` | 大模型调用、内容审核评分 | 否（仅 server 内网调用） |

详细审核流程见 [`12-track-审核模块-2026-07-08.md`](12-track-审核模块-2026-07-08.md)；API 清单见 [`11-track-API设计-2026-07-08.md`](11-track-API设计-2026-07-08.md)。

---

## 二、前端（微信小程序）

| 技术 | 选择 |
| --- | --- |
| 开发语言 | TypeScript |
| UI 框架 | Vant Weapp |
| 接口客户端 | openapi-generator 自动生成（基于 **server** OpenAPI 规范） |

---

## 三、前后端接口联动方案

- 后端使用 **SpringDoc** 自动生成 `openapi.json`
- 使用 **openapi-generator-cli** 生成 TypeScript fetch 客户端
- 小程序引用生成的客户端代码调用接口
- 接口变动时重新生成，编译期报错，避免前端调用废弃接口
- **AI 服务接口不纳入** OpenAPI 生成链路

---

## 四、主后端（server/）

| 技术 | 选择 | 说明 |
| --- | --- | --- |
| 语言 | **Kotlin** | 空安全、Data Class、协程 |
| 框架 | Spring Boot **4.0.4** | |
| ORM | **JOOQ** | 类型安全的 SQL DSL，代码生成操作数据库 |
| 数据库 | MySQL **8.4.5** | Flyway 管理迁移脚本 |
| 缓存 | Redis **7** | 热点数据、计数、发文限流 |
| 依赖管理 | Gradle（Kotlin DSL） | |
| 本地开发 | Docker + Docker Compose | MySQL + Redis + AI 服务 |

---

## 五、AI 服务（ai/）

| 技术 | 选择 | 说明 |
| --- | --- | --- |
| 语言 | **Python 3.11+** | 大模型生态成熟 |
| 框架 | **FastAPI** | 异步、Pydantic 校验 |
| 运行 | Uvicorn | 本地 / Docker |
| 认证 | `X-Internal-Token` | 与 server 共享环境变量 |
| MVP 接口 | `GET /health`、`POST /internal/audit`、`GET /internal/providers` | Mock 默认；多 Provider 可插拔 |
| Provider | `mock` / `deepseek` / `doubao` / `glm` / `langchain` | 环境变量 `AI_PROVIDER` 切换；Key 未配置回退 mock |

**调用链**：用户发布文章 → `server` 写库 → 同步调用 `ai/internal/audit`（15s 超时）→ Provider 返回评分 → 更新状态 / 进人工队列。

本地配置（`ai/.env`，节选）：

```env
AI_PROVIDER=mock
DEEPSEEK_API_KEY=
DOUBAO_API_KEY=
GLM_API_KEY=
LANGCHAIN_ENABLED=false
```

server 规划项（`application.yml`）：

```yaml
ice:
  ai:
    base-url: http://localhost:8000
    internal-token: ${INTERNAL_API_TOKEN}
    timeout-ms: 15000
```

---

## 六、云服务（腾讯云）

微信小程序项目首选腾讯云（与微信生态原生集成）：

| 服务 | 用途 |
| --- | --- |
| COS 对象存储 | 图片、音频文件存储，支持小程序直传 |
| CDN | 静态资源加速 |
| 短信服务 | 手机号注册验证码 |
| 微信支付 | 书币充值（契约已设计，底层 Stub；`feature_config.recharge_enabled` 控制入口） |

---

## 六点五、客户端功能开关

| 配置源 | 接口 | 说明 |
|---|---|---|
| `feature_config` 表 | `GET /api/v1/config/client` | 小程序拉取；`recharge_enabled=false` 时隐藏全部充值入口 |

详见 [`11-track-API设计`](11-track-API设计-2026-07-08.md) §2.11、附录 D。

---

## 七、AI 内容审核

- **服务位置**：`ai/` 微服务 + Provider Registry；`server/` 编排业务状态
- **Provider**：`mock`（默认）、`deepseek`、`doubao`、`glm`、`langchain`（编排层可选）
- **首选模型**：DeepSeek API（成本低，开放 API）
- **评分维度**：合规性、内容质量、广告灌水、人身攻击（见 12-track 第三节）
- **通过阈值**：综合分 ≥ 60 自动通过；40–59 拒绝可申诉；< 40 严重违规
- **降级**：AI 超时或 5xx → 保持「审核中」，自动进人工队列

---

## 八、数据规模规划

| 指标 | 目标值 | 说明 |
| --- | --- | --- |
| 用户规模 | 1000 万 | 长期目标 |
| 文章总量 | 10 亿 | 长期目标 |
| 文章长度分布 | 50% ≤800字 / 40% ≤1500字 / 5% ≤3000字 / 5% ≤5000字 | 受用户等级控制 |

**架构预留**：初期 MySQL 单库可支撑，当数据量达到 **1 亿+** 时需评估分库分表或迁移至 TiDB。表设计阶段应提前预留分片键（如 `user_id`）。
