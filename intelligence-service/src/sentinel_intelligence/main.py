from datetime import datetime, timezone
from fastapi import FastAPI
from pydantic import BaseModel, Field

app = FastAPI(title="SENTINEL AI Intelligence Service", version="0.1.0")


class InvestigationRequest(BaseModel):
    incident_id: str = Field(pattern=r"^inc_demo_[0-9]+$")
    evidence_ids: list[str] = Field(default_factory=list, max_length=50)


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
