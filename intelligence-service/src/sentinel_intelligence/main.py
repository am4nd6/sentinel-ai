from datetime import datetime, timezone
from fastapi import FastAPI
from pydantic import BaseModel, Field
from .risk import RiskSignals, score, p95, drift

app = FastAPI(title="SENTINEL AI Intelligence Service", version="0.1.0")


class InvestigationRequest(BaseModel):
    incident_id: str = Field(pattern=r"^inc_demo_[0-9]+$")
    evidence_ids: list[str] = Field(default_factory=list, max_length=50)

class RiskRequest(BaseModel):
    tenant_id: str = Field(pattern=r"^tenant_[a-z0-9_-]+$")
    signals: RiskSignals

class MetricsRequest(BaseModel):
    reference: list[float] = Field(min_length=1, max_length=1000)
    current: list[float] = Field(min_length=1, max_length=1000)


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok", "service": "sentinel-intelligence"}


@app.post("/v1/investigations", status_code=202)
def create_investigation(request: InvestigationRequest) -> dict[str, object]:
    return {
        "jobId": f"job_{request.incident_id}",
        "status": "ACCEPTED",
        "receivedAt": datetime.now(timezone.utc).isoformat(),
        "mode": "read-only",
    }

@app.post("/v1/risk/evaluate")
def evaluate_risk(request: RiskRequest) -> dict[str, object]:
    return {"tenantId": request.tenant_id, **score(request.signals)}

@app.post("/v1/risk/metrics")
def evaluate_metrics(request: MetricsRequest) -> dict[str, object]:
    return {"p95": p95(request.current), **drift(request.reference, request.current)}
