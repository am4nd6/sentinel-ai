from fastapi.testclient import TestClient
from sentinel_intelligence.main import app

client = TestClient(app)


def test_health() -> None:
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "ok"


def test_investigation_is_read_only_job() -> None:
    response = client.post("/v1/investigations", json={"incident_id": "inc_demo_001"})
    assert response.status_code == 202
    assert response.json()["mode"] == "read-only"
