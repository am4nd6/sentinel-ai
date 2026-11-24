"""Generate deterministic synthetic events without real personal data."""
import json
import random
from datetime import datetime, timedelta, timezone
from pathlib import Path

SEED = 20261124


def generate(count: int = 10) -> list[dict]:
    rng = random.Random(SEED)
    start = datetime(2026, 11, 24, 12, 0, tzinfo=timezone.utc)
    events = []
    for index in range(count):
        events.append({
            "eventId": f"evt_generated_{index:04d}",
            "tenantId": "tenant_demo_a",
            "eventType": "auth.login.failed" if index % 3 == 0 else "auth.login.success",
            "schemaVersion": "1.0",
            "occurredAt": (start + timedelta(minutes=index)).isoformat().replace("+00:00", "Z"),
            "accountRef": f"acct_demo_{rng.randint(1, 99):03d}",
            "deviceRef": f"dev_demo_{rng.randint(1, 20):03d}",
            "attributes": {"attemptCountWindow": 1 if index % 3 else 6},
        })
    return events


if __name__ == "__main__":
    output = Path("datasets/synthetic/generated-events.jsonl")
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text("\n".join(json.dumps(event) for event in generate()) + "\n", encoding="utf-8")
    print(f"generated {len(generate())} synthetic events at {output}")
