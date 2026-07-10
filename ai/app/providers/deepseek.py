from __future__ import annotations

from typing import List, Optional, Tuple

from app.config import settings
from app.providers.base import AuditProvider
from app.schemas.audit import AuditDimensions


class DeepSeekProvider(AuditProvider):
    code = "deepseek"

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
        if not settings.deepseek_api_key:
            raise NotImplementedError("DEEPSEEK_API_KEY not configured")
        # TODO: call DeepSeek chat completions with audit prompt
        raise NotImplementedError("DeepSeek provider not implemented yet")
