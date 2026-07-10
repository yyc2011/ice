from __future__ import annotations

from typing import List, Optional, Tuple

from app.config import settings
from app.providers.base import AuditProvider
from app.schemas.audit import AuditDimensions


class DoubaoProvider(AuditProvider):
    code = "doubao"

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
        if not settings.doubao_api_key:
            raise NotImplementedError("DOUBAO_API_KEY not configured")
        # TODO: call Volcengine / Doubao API
        raise NotImplementedError("Doubao provider not implemented yet")
