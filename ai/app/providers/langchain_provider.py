from __future__ import annotations

from typing import List, Optional, Tuple

from app.config import settings
from app.providers.base import AuditProvider
from app.schemas.audit import AuditDimensions


class LangChainProvider(AuditProvider):
    """
    LangChain orchestration layer — routes to configured underlying model.
    Implementation stub; enable with AI_PROVIDER=langchain and LANGCHAIN_ENABLED=true.
    """

    code = "langchain"

    async def audit(
        self,
        *,
        content_type: str,
        title: str,
        body: str,
        tags: List[str],
        topic_title: Optional[str],
        appeal_text: Optional[str],
    ) -> Tuple[AuditDimensions, str]:
        if not settings.langchain_enabled:
            raise NotImplementedError("LANGCHAIN_ENABLED is false")
        # TODO: LangChain chain -> delegate to deepseek/doubao/glm via settings.langchain_model_provider
        raise NotImplementedError("LangChain provider not implemented yet")
