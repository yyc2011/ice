from __future__ import annotations

from typing import List, Optional, Tuple

from app.providers.base import AuditProvider
from app.schemas.audit import AuditDimensions


class MockProvider(AuditProvider):
    code = "mock"

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
        if "__reject__" in body:
            return AuditDimensions(compliance=30, quality=35, spam=20, abuse=10), "mock-rules"
        if "__manual__" in body:
            return AuditDimensions(compliance=70, quality=45, spam=45, abuse=35), "mock-rules"
        return AuditDimensions(compliance=95, quality=78, spam=8, abuse=5), "mock-rules"
