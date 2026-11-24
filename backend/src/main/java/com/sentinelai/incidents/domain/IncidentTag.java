package com.sentinelai.incidents.domain;
import jakarta.persistence.*; import java.io.Serializable; import java.util.UUID;
@Entity @Table(name="incident_tag") @IdClass(IncidentTag.Key.class) public class IncidentTag { @Id private UUID incidentId; @Id private String tag; private String tenantId; protected IncidentTag(){} public IncidentTag(UUID incidentId,String tenantId,String tag){this.incidentId=incidentId;this.tenantId=tenantId;this.tag=tag;} public record Key(UUID incidentId,String tag) implements Serializable{} }
