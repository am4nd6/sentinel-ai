package com.sentinelai.pipeline;

import com.sentinelai.pipeline.application.CorrelationService;
import java.time.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CorrelationServiceTests {
    @Test void correlatesSignalsWithinWindowAndKeepsTenantIsolation() {
        var service = new CorrelationService(); var now = Instant.parse("2026-01-01T00:00:00Z");
        service.correlate("tenant-a", "account-1", "device-1", "ip-1", now, Duration.ofMinutes(5));
        var related = service.correlate("tenant-a", "account-1", "device-2", "ip-2", now.plusSeconds(30), Duration.ofMinutes(5));
        assertTrue(related.contains("device:device-1")); assertTrue(related.contains("source:ip-1"));
        var isolated = service.correlate("tenant-b", "account-1", "device-x", "ip-x", now, Duration.ofMinutes(5));
        assertFalse(isolated.contains("device:device-1"));
    }
    @Test void expiresOldSignals() {
        var service = new CorrelationService(); var now = Instant.parse("2026-01-01T00:00:00Z");
        service.correlate("t", "a", "old", "ip-old", now, Duration.ofMinutes(1));
        var current = service.correlate("t", "a", "new", "ip-new", now.plusSeconds(61), Duration.ofMinutes(1));
        assertFalse(current.contains("device:old"));
    }
}
