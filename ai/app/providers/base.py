from __future__ import annotations

from abc import ABC, abstractmethod
from typing import List, Optional, Tuple

from app.schemas.audit import AuditDimensions


class AuditProvider(ABC):
    """Base class for AI audit providers. Implementations may be stubs until API keys are configured."""

    code: str = "base"

    @abstractmethod
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
        """Return (dimensions, model_identifier)."""
