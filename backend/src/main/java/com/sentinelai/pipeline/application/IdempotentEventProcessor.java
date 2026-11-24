package com.sentinelai.pipeline.application;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class IdempotentEventProcessor {
    private final Set<String> receipts = ConcurrentHashMap.newKeySet();
    private final PipelineMetrics metrics;
    public IdempotentEventProcessor(PipelineMetrics metrics) { this.metrics = metrics; }
    public boolean process(String eventKey, Runnable handler) {
        if (!receipts.add(eventKey)) { metrics.duplicate(); return false; }
        try { handler.run(); metrics.processed(); return true; }
        catch (RuntimeException ex) { receipts.remove(eventKey); metrics.failure(); throw ex; }
    }
}
