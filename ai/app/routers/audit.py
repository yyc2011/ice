from fastapi import APIRouter, Depends, Header, HTTPException, status

from app.config import settings
from app.providers import list_providers
from app.schemas.audit import AuditRequest, AuditResponse
from app.services.audit import run_audit

router = APIRouter(prefix="/internal", tags=["internal"])


def verify_internal_token(x_internal_token: str = Header(..., alias="X-Internal-Token")) -> None:
    if x_internal_token != settings.internal_api_token:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid internal token")


@router.get("/providers")
async def get_providers(_: None = Depends(verify_internal_token)) -> dict:
    return {"active": settings.ai_provider, "providers": list_providers()}


@router.post("/audit", response_model=AuditResponse)
async def audit_content(
    request: AuditRequest,
    _: None = Depends(verify_internal_token),
) -> AuditResponse:
    return await run_audit(request)
