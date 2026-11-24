package com.sentinelai.ingestion.domain;
import jakarta.persistence.Entity; import jakarta.persistence.Id; import jakarta.persistence.Table; import java.util.UUID;
@Entity @Table(name="device") public class Device { @Id private UUID id; private String tenantId; private String deviceRef; protected Device() {} }
