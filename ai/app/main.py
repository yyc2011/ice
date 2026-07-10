from fastapi import FastAPI

from app.routers import audit

app = FastAPI(
    title="Ice AI Service",
    description="Internal AI capabilities for Ice — content audit and LLM",
    version="0.1.0",
)

app.include_router(audit.router)


@app.get("/health")
async def health() -> dict[str, str]:
    return {"status": "ok"}
