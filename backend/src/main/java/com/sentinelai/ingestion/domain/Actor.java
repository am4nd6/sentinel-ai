package com.sentinelai.ingestion.domain;
import jakarta.persistence.Entity; import jakarta.persistence.Id; import jakarta.persistence.Table; import java.util.UUID;
@Entity @Table(name="actor") public class Actor { @Id private UUID id; private String tenantId; private String subjectId; protected Actor() {} }
