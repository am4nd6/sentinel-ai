from dataclasses import dataclass
from math import sqrt
from statistics import mean, pstdev
from typing import Iterable

@dataclass(frozen=True)
class RiskSignals:
    login_attempts: int = 0
    distinct_ips: int = 0
    account_velocity: float = 0.0
    coupon_claims: int = 0
    distinct_accounts: int = 0
    device_changes: int = 0

def features(signals: RiskSignals) -> dict[str, float]:
    return {"login_attempts": float(signals.login_attempts), "distinct_ips": float(signals.distinct_ips), "account_velocity": signals.account_velocity, "coupon_claims": float(signals.coupon_claims), "distinct_accounts": float(signals.distinct_accounts), "device_changes": float(signals.device_changes)}

def detect(signals: RiskSignals) -> list[str]:
    found: list[str] = []
    if signals.login_attempts >= 10 and signals.distinct_ips >= 3: found.append("CREDENTIAL_STUFFING")
    if signals.account_velocity >= 5 and signals.device_changes >= 2: found.append("ACCOUNT_TAKEOVER")
    if signals.coupon_claims >= 8 and signals.distinct_accounts >= 5: found.append("COUPON_ABUSE")
    return found

def score(signals: RiskSignals) -> dict[str, object]:
    detections = detect(signals)
    weights = {"CREDENTIAL_STUFFING": 0.8, "ACCOUNT_TAKEOVER": 0.9, "COUPON_ABUSE": 0.7}
    value = min(1.0, max((weights[d] for d in detections), default=0.0))
    return {"score": value, "decision": "REVIEW" if value >= 0.7 else "ALLOW", "detections": detections, "explanations": [f"threshold matched: {d}" for d in detections], "policyVersion": "risk-policy-v1"}

def baseline(values: Iterable[float]) -> dict[str, float]:
    samples = list(values)
    if not samples: raise ValueError("at least one baseline sample is required")
    return {"mean": mean(samples), "stddev": pstdev(samples), "samples": float(len(samples))}

def evaluation(predicted: list[bool], actual: list[bool]) -> dict[str, float]:
    if len(predicted) != len(actual) or not actual: raise ValueError("prediction and label sizes must match")
    tp = sum(p and a for p, a in zip(predicted, actual)); tn = sum(not p and not a for p, a in zip(predicted, actual)); fp = sum(p and not a for p, a in zip(predicted, actual)); fn = sum(not p and a for p, a in zip(predicted, actual))
    return {"recall": tp / (tp + fn) if tp + fn else 0.0, "precision": tp / (tp + fp) if tp + fp else 0.0, "fpr": fp / (fp + tn) if fp + tn else 0.0, "samples": float(len(actual))}

def p95(values: Iterable[float]) -> float:
    samples = sorted(values)
    if not samples: raise ValueError("at least one latency sample is required")
    rank = max(0, int(0.95 * len(samples) + 0.999999) - 1)
    return samples[rank]

def drift(reference: Iterable[float], current: Iterable[float]) -> dict[str, float | bool]:
    ref, cur = list(reference), list(current)
    if not ref or not cur: raise ValueError("both periods require samples")
    ref_mean, cur_mean = mean(ref), mean(cur)
    delta = abs(cur_mean - ref_mean) / max(abs(ref_mean), 1e-9)
    return {"referenceMean": ref_mean, "currentMean": cur_mean, "relativeChange": delta, "drift": delta > 0.2}
