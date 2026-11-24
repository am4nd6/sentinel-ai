package com.sentinelai.pipeline.api;
import java.util.Map; import org.springframework.web.bind.annotation.GetMapping; import org.springframework.web.bind.annotation.RequestMapping; import org.springframework.web.bind.annotation.RestController;
@RestController @RequestMapping("/api/v1/pipeline") public class PipelineStatusController {
    private final com.sentinelai.pipeline.application.PipelineMetrics metrics;
    public PipelineStatusController(com.sentinelai.pipeline.application.PipelineMetrics metrics) { this.metrics = metrics; }
    @GetMapping("/status") public Map<String,Object> status() { return Map.of("topic", "sentinel.events.v1", "status", "READY", "retryPolicy", "bounded-exponential", "deadLetterTopic", "sentinel.events.v1.dlq", "backpressure", "concurrency=1", "processed", metrics.processedCount(), "duplicates", metrics.duplicateCount(), "failures", metrics.failureCount(), "deadLetters", metrics.deadLetterCount()); }
}
