from __future__ import annotations

from enum import Enum
from typing import List, Optional

from pydantic import BaseModel, Field


class ContentType(str, Enum):
    ARTICLE = "article"
    TOPIC = "topic"


class ReviewType(str, Enum):
    INITIAL = "initial"
    AI_APPEAL = "ai_appeal"


class AuditResult(str, Enum):
    PASS = "pass"
    REJECT = "reject"
    MANUAL = "manual"


class AuditDimensions(BaseModel):
    compliance: int = Field(ge=0, le=100)
    quality: int = Field(ge=0, le=100)
    spam: int = Field(ge=0, le=100, description="spam score; lower is better")
    abuse: int = Field(ge=0, le=100, description="abuse score; lower is better")


class AuditRequest(BaseModel):
    request_id: str
    content_type: ContentType
    review_type: ReviewType
    title: str
    body: str
    tags: List[str] = Field(default_factory=list)
    topic_title: Optional[str] = None
    appeal_text: Optional[str] = None


class AuditResponse(BaseModel):
    request_id: str
    result: AuditResult
    overall_score: int = Field(ge=0, le=100)
    dimensions: AuditDimensions
    reject_reason: Optional[str] = None
    model: str
    latency_ms: int
