package com.sentinelai.incidents.domain;
import jakarta.persistence.*; import java.time.Instant; import java.util.UUID;
@Entity @Table(name="incident_comment") public class IncidentComment { @Id private UUID id; private UUID incidentId; private String tenantId; private String author; private String body; private Instant createdAt; protected IncidentComment(){} public IncidentComment(UUID id,UUID incidentId,String tenantId,String author,String body){this.id=id;this.incidentId=incidentId;this.tenantId=tenantId;this.author=author;this.body=body;this.createdAt=Instant.now();} }
