import time
from typing import Optional

from app.providers import get_provider
from app.schemas.audit import (
    AuditDimensions,
    AuditRequest,
    AuditResponse,
    AuditResult,
)

PASS_THRESHOLD = 60
SEVERE_REJECT_THRESHOLD = 40


def _compute_overall_score(dimensions: AuditDimensions) -> int:
    raw = int(
        dimensions.quality * 0.4
        + (100 - dimensions.spam) * 0.3
        + (100 - dimensions.abuse) * 0.3
    )
    if dimensions.compliance < 40:
        return min(raw, 39)
    return max(0, min(100, raw))


def _resolve_result(overall_score: int) -> AuditResult:
    if overall_score >= PASS_THRESHOLD:
        return AuditResult.PASS
    if overall_score < SEVERE_REJECT_THRESHOLD:
        return AuditResult.REJECT
    return AuditResult.MANUAL


def _reject_reason(result: AuditResult, dimensions: AuditDimensions) -> Optional[str]:
    if result == AuditResult.PASS:
        return None
    if dimensions.compliance < 40:
        return "含有违反社区规范的内容"
    if dimensions.spam > 60:
        return "含有垃圾广告或引流信息"
    if dimensions.abuse > 60:
        return "含有人身攻击或辱骂内容"
    if dimensions.quality < 40:
        return "内容质量不达标"
    return "未通过社区内容审核"


async def run_audit(request: AuditRequest) -> AuditResponse:
    started = time.perf_counter()
    provider = get_provider()

    try:
        dimensions, model = await provider.audit(
            content_type=request.content_type.value,
            title=request.title,
            body=request.body,
            tags=request.tags,
            topic_title=request.topic_title,
            appeal_text=request.appeal_text,
        )
    except NotImplementedError:
        # Fallback to mock when provider stub is not implemented
        from app.providers.mock_provider import MockProvider

        dimensions, model = await MockProvider().audit(
            content_type=request.content_type.value,
            title=request.title,
            body=request.body,
            tags=request.tags,
            topic_title=request.topic_title,
            appeal_text=request.appeal_text,
        )
        model = f"{provider.code}->mock-fallback"

    overall_score = _compute_overall_score(dimensions)
    result = _resolve_result(overall_score)
    latency_ms = int((time.perf_counter() - started) * 1000)

    return AuditResponse(
        request_id=request.request_id,
        result=result,
        overall_score=overall_score,
        dimensions=dimensions,
        reject_reason=_reject_reason(result, dimensions),
        model=model,
        latency_ms=latency_ms,
    )
