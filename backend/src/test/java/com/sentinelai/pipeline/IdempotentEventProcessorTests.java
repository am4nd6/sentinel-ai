package com.sentinelai.pipeline;

import com.sentinelai.pipeline.application.IdempotentEventProcessor;
import com.sentinelai.pipeline.application.PipelineMetrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.atomic.AtomicInteger;

class IdempotentEventProcessorTests {
    @Test void replayIsIgnored() {
        var metrics = new PipelineMetrics(); var processor = new IdempotentEventProcessor(metrics); var calls = new AtomicInteger();
        assertTrue(processor.process("event-1", calls::incrementAndGet));
        assertFalse(processor.process("event-1", calls::incrementAndGet));
        assertEquals(1, calls.get()); assertEquals(1, metrics.duplicateCount());
    }
    @Test void failedProcessingCanBeRetried() {
        var metrics = new PipelineMetrics(); var processor = new IdempotentEventProcessor(metrics); var calls = new AtomicInteger();
        assertThrows(IllegalStateException.class, () -> processor.process("event-2", () -> { calls.incrementAndGet(); throw new IllegalStateException(); }));
        assertTrue(processor.process("event-2", calls::incrementAndGet));
        assertEquals(2, calls.get()); assertEquals(1, metrics.failureCount());
    }
}
