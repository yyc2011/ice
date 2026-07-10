# Ice AI Service

Python + FastAPI 内网服务，负责大模型相关能力（MVP：内容审核）。

- 仅 `server/` 调用，不对小程序暴露
- 契约见 [`docs/12-track-审核模块-2026-07-08.md`](../docs/12-track-审核模块-2026-07-08.md) 附录 A

## 本地启动

```bash
cd ai
cp .env.example .env
pip install -r requirements.txt
PYTHONPATH=. uvicorn app.main:app --reload --port 8000
```

健康检查：http://localhost:8000/health

## Mock 审核规则

`AI_PROVIDER=mock`（默认）时使用规则 Mock（见 12-track 附录 A.4）：

| 正文包含 | 结果 |
|---|---|
| `__reject__` | reject，score≈30 |
| `__manual__` | manual，score≈55 |
| 默认 | pass，score≈75 |

## 多模型 Provider

| `AI_PROVIDER` | 说明 |
|---|---|
| `mock` | 默认，无需 API Key |
| `deepseek` | DeepSeek（需 `DEEPSEEK_API_KEY`） |
| `doubao` | 豆包 / 火山方舟（需 `DOUBAO_API_KEY`） |
| `glm` | 智谱 GLM（需 `GLM_API_KEY`） |
| `langchain` | LangChain 编排层（需 `LANGCHAIN_ENABLED=true`） |

查看已注册 Provider：`GET /internal/providers`

未配置 Key 或实现未完成时自动回退 mock。

## 调用示例

```bash
curl -s -X POST http://localhost:8000/internal/audit \
  -H "Content-Type: application/json" \
  -H "X-Internal-Token: dev-internal-token" \
  -d '{
    "request_id": "test-1",
    "content_type": "article",
    "review_type": "initial",
    "title": "测试",
    "body": "正常正文"
  }' | python3 -m json.tool
```

## Docker

与 `server/docker-compose.yml` 中的 `ai` 服务一并启动：

```bash
cd server
docker compose up -d
```
