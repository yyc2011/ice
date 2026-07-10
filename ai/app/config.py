from __future__ import annotations

from typing import Optional

from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    model_config = SettingsConfigDict(env_file=".env", env_file_encoding="utf-8", extra="ignore")

    internal_api_token: str = "dev-internal-token"

    # Active provider: mock | deepseek | doubao | glm | langchain
    ai_provider: str = "mock"

    deepseek_api_key: Optional[str] = None
    deepseek_base_url: str = "https://api.deepseek.com"
    deepseek_model: str = "deepseek-chat"

    doubao_api_key: Optional[str] = None
    doubao_model: str = "doubao-pro"

    glm_api_key: Optional[str] = None
    glm_model: str = "glm-4-flash"

    langchain_enabled: bool = False


settings = Settings()
