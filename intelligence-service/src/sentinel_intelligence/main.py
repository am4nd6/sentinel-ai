from datetime import datetime, timezone
from fastapi import FastAPI, Query, HTTPException
from pydantic import BaseModel, Field, field_validator
import re
from .risk import RiskSignals, score, p95, drift

app = FastAPI(title="SENTINEL AI Intelligence Service", version="0.1.0")


class Evidence(BaseModel):
    id: str = Field(pattern=r"^ev_[a-zA-Z0-9_-]+$")
    text: str = Field(min_length=1, max_length=2000)
    source: str = Field(min_length=1, max_length=160)


class Citation(BaseModel):
    evidence_id: str
    source: str
    excerpt: str


class Hypothesis(BaseModel):
    text: str
    confidence: str = Field(pattern=r"^(LOW|MEDIUM|HIGH)$")


class InvestigationRequest(BaseModel):
    tenant_id: str = Field(pattern=r"^tenant_[a-z0-9_-]+$")
    incident_id: str = Field(pattern=r"^inc_demo_[0-9]+$")
    evidence: list[Evidence] = Field(default_factory=list, max_length=50)
    roles: list[str] = Field(default_factory=list, max_length=10)
    question: str = Field(default="Summarize the authorized evidence.", min_length=1, max_length=500)

    @field_validator("question")
    @classmethod
    def reject_instructions_in_data(cls, value: str) -> str:
        if re.search(r"(ignore|disregard).{0,40}(previous|system|instructions)|dump|exfiltrat|secret|token", value, re.I):
            raise ValueError("investigation question contains an unsafe instruction")
        return value


class InvestigationResponse(BaseModel):
    job_id: str
    status: str
    tenant_id: str
    mode: str = "read-only"
    summary: str
    hypotheses: list[Hypothesis]
    citations: list[Citation]
    limitations: list[str]
    prompt_version: str = "investigator-v1"
    provider_payload: dict[str, object]

class RiskRequest(BaseModel):
    tenant_id: str = Field(pattern=r"^tenant_[a-z0-9_-]+$")
    signals: RiskSignals

class MetricsRequest(BaseModel):
    reference: list[float] = Field(min_length=1, max_length=1000)
    current: list[float] = Field(min_length=1, max_length=1000)


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok", "service": "sentinel-intelligence"}


def redact(text: str) -> str:
    text = re.sub(r"[\w.+-]+@[\w.-]+\.[A-Za-z]{2,}", "[REDACTED_EMAIL]", text)
    text = re.sub(r"\b(?:\+?\d[\d ()-]{7,}\d)\b", "[REDACTED_PHONE]", text)
    return text


@app.post("/v1/investigations", status_code=202, response_model=InvestigationResponse)
def create_investigation(request: InvestigationRequest) -> InvestigationResponse:
    if not ({"ANALYST", "AUDITOR", "ADMIN"} & set(request.roles)):
        raise HTTPException(status_code=403, detail="role is not authorized for investigation")
    citations = [Citation(evidence_id=e.id, source=e.source, excerpt=redact(e.text[:240])) for e in request.evidence]
    if not citations:
        summary = "Inconclusive: no authorized evidence was supplied."
        hypotheses = []
        limitations = ["No evidence available", "Read-only deterministic fallback; no external provider was called"]
    else:
        summary = f"Review of {len(citations)} authorized evidence item(s); findings require analyst validation."
        hypotheses = [Hypothesis(text="The supplied evidence may indicate an anomalous activity pattern.", confidence="LOW")]
        limitations = ["Logs and retrieved text are untrusted data, not instructions", "Hypotheses are not decisions or actions"]
    return InvestigationResponse(
        job_id=f"job_{request.incident_id}", status="COMPLETED", tenant_id=request.tenant_id,
        summary=summary, hypotheses=hypotheses, citations=citations, limitations=limitations,
        provider_payload={"redacted": True, "external_provider_called": False, "tenant_scope": request.tenant_id},
    )


@app.get("/v1/knowledge/search")
def search_approved_knowledge(tenant_id: str = Query(pattern=r"^tenant_[a-z0-9_-]+$"), query: str = "") -> dict[str, object]:
    """Read-only approved knowledge index. Documents carry provenance and tenant ACL."""
    if not query or re.search(r"(ignore|system prompt|secret|token)", query, re.I):
        return {"items": [], "inconclusive": True, "reason": "unsafe or empty query"}
    return {"items": [{"chunkId": "kb_synthetic_001", "source": "docs/security-auth.md", "version": "1", "tenantId": tenant_id, "text": "Investigations require authorized analyst, auditor, or admin access."}], "inconclusive": False}

@app.post("/v1/risk/evaluate")
def evaluate_risk(request: RiskRequest) -> dict[str, object]:
    return {"tenantId": request.tenant_id, **score(request.signals)}

@app.post("/v1/risk/metrics")
def evaluate_metrics(request: MetricsRequest) -> dict[str, object]:
    return {"p95": p95(request.current), **drift(request.reference, request.current)}
