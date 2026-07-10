from __future__ import annotations

from typing import Dict, List

from app.config import settings
from app.providers.base import AuditProvider
from app.providers.deepseek import DeepSeekProvider
from app.providers.doubao import DoubaoProvider
from app.providers.glm import GlmProvider
from app.providers.langchain_provider import LangChainProvider
from app.providers.mock_provider import MockProvider

_PROVIDERS: Dict[str, AuditProvider] = {
    "mock": MockProvider(),
    "deepseek": DeepSeekProvider(),
    "doubao": DoubaoProvider(),
    "glm": GlmProvider(),
    "langchain": LangChainProvider(),
}


def list_providers() -> List[dict]:
    active = settings.ai_provider
    return [
        {
            "code": code,
            "active": code == active,
            "configured": _is_configured(code),
        }
        for code in _PROVIDERS
    ]


def _is_configured(code: str) -> bool:
    if code == "mock":
        return True
    if code == "deepseek":
        return bool(settings.deepseek_api_key)
    if code == "doubao":
        return bool(settings.doubao_api_key)
    if code == "glm":
        return bool(settings.glm_api_key)
    if code == "langchain":
        return settings.langchain_enabled
    return False


def get_provider() -> AuditProvider:
    code = settings.ai_provider
    provider = _PROVIDERS.get(code)
    if provider is None:
        return _PROVIDERS["mock"]
    if not _is_configured(code):
        return _PROVIDERS["mock"]
    return provider
