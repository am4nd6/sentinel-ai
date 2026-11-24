from sentinel_intelligence.risk import RiskSignals, baseline, detect, evaluation, score, p95, drift

def test_detects_synthetic_patterns():
    assert detect(RiskSignals(login_attempts=12, distinct_ips=4)) == ["CREDENTIAL_STUFFING"]
    assert "ACCOUNT_TAKEOVER" in detect(RiskSignals(account_velocity=6, device_changes=2))
    assert "COUPON_ABUSE" in detect(RiskSignals(coupon_claims=9, distinct_accounts=5))

def test_score_has_deterministic_explanation():
    result = score(RiskSignals(login_attempts=12, distinct_ips=4))
    assert result["decision"] == "REVIEW" and result["policyVersion"] == "risk-policy-v1"
    assert result["explanations"] == ["threshold matched: CREDENTIAL_STUFFING"]

def test_metrics_and_baseline_are_bounded():
    assert baseline([1, 2, 3])["mean"] == 2
    metrics = evaluation([True, False, True, False], [True, False, False, True])
    assert metrics["recall"] == 0.5 and metrics["precision"] == 0.5 and metrics["fpr"] == 0.5

def test_p95_and_drift():
    assert p95([1, 2, 3, 4, 5, 100]) == 100
    assert drift([10, 10], [13, 13])["drift"] is True
