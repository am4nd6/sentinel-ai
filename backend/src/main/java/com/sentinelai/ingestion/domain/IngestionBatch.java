package com.sentinelai.ingestion.domain;
import jakarta.persistence.Entity; import jakarta.persistence.Id; import jakarta.persistence.Table; import java.util.UUID; import java.time.Instant;
@Entity @Table(name="ingestion_batch") public class IngestionBatch { @Id private UUID id; private String tenantId; private Instant receivedAt; private int eventCount; protected IngestionBatch() {} }
