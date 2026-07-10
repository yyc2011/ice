from __future__ import annotations

from typing import List, Optional, Tuple

from app.config import settings
from app.providers.base import AuditProvider
from app.schemas.audit import AuditDimensions


class GlmProvider(AuditProvider):
    code = "glm"

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
        if not settings.glm_api_key:
            raise NotImplementedError("GLM_API_KEY not configured")
        # TODO: call Zhipu GLM API
        raise NotImplementedError("GLM provider not implemented yet")
