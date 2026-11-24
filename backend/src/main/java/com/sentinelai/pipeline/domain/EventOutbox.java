package com.sentinelai.pipeline.domain;

import jakarta.persistence.Entity; import jakarta.persistence.Id; import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode; import org.hibernate.type.SqlTypes;
import java.time.Instant; import java.util.UUID;

@Entity @Table(name="event_outbox")
public class EventOutbox {
    @Id private UUID id; private String tenantId; private UUID aggregateId; private String topic; private String schemaVersion;
    @JdbcTypeCode(SqlTypes.JSON) private String payload;
    private String status; private int attempts; private Instant availableAt; private String lastError; private Instant createdAt;
    protected EventOutbox() {}
    public EventOutbox(UUID id, String tenantId, UUID aggregateId, String topic, String schemaVersion, String payload) { this.id=id; this.tenantId=tenantId; this.aggregateId=aggregateId; this.topic=topic; this.schemaVersion=schemaVersion; this.payload=payload; this.status="PENDING"; this.availableAt=Instant.now(); this.createdAt=Instant.now(); }
    public String getTopic() { return topic; }
    public UUID getAggregateId() { return aggregateId; }
    public String getPayload() { return payload; }
    public void markPublishing() { status = "PUBLISHING"; attempts++; }
    public void markPublished() { status = "PUBLISHED"; }
    public void markRetry(String error) { status = attempts >= 5 ? "DLQ" : "PENDING"; lastError = error == null ? "publish failure" : error.substring(0, Math.min(500, error.length())); availableAt = Instant.now().plusSeconds(Math.min(60, attempts * 2L)); }
}
