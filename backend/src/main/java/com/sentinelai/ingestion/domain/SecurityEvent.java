package com.sentinelai.ingestion.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "security_event")
public class SecurityEvent {
    @Id private UUID id;
    private String tenantId;
    private String eventRef;
    private String eventType;
    private String schemaVersion;
    private Instant occurredAt;
    private Instant receivedAt;
    private String accountRef;
    private String deviceRef;
    private String sourceRef;
    @Column(columnDefinition = "jsonb") private String payload;
    private String dedupeKey;
    private String classification;
    protected SecurityEvent() { }
    public SecurityEvent(UUID id, String tenantId, String eventRef, String eventType, String schemaVersion, Instant occurredAt, String accountRef, String deviceRef, String sourceRef, String payload, String dedupeKey) {
        this.id=id; this.tenantId=tenantId; this.eventRef=eventRef; this.eventType=eventType; this.schemaVersion=schemaVersion; this.occurredAt=occurredAt; this.receivedAt=Instant.now(); this.accountRef=accountRef; this.deviceRef=deviceRef; this.sourceRef=sourceRef; this.payload=payload; this.dedupeKey=dedupeKey; this.classification="synthetic";
    }
    public UUID getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getDedupeKey() { return dedupeKey; }
}
