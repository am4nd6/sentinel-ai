package com.sentinelai.ingestion.domain;
import jakarta.persistence.Entity; import jakarta.persistence.Id; import jakarta.persistence.Table; import java.util.UUID; import java.time.Instant;
@Entity @Table(name="security_session") public class SecuritySession { @Id private UUID id; private String tenantId; private String sessionRef; private Instant startedAt; protected SecuritySession() {} }
