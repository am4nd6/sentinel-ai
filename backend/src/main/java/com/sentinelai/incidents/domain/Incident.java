package com.sentinelai.incidents.domain;
import jakarta.persistence.*; import java.time.Instant; import java.util.Set; import java.util.UUID;
@Entity @Table(name="incident") public class Incident {
 @Id private UUID id; private String tenantId; private String title; private String severity; private String status; private String assignee; private Instant createdAt; private Instant updatedAt;
 protected Incident() {}
 public Incident(UUID id,String tenantId,String title,String severity,String assignee){this.id=id;this.tenantId=tenantId;this.title=title;this.severity=severity;this.status="OPEN";this.assignee=assignee;this.createdAt=Instant.now();this.updatedAt=createdAt;}
 public UUID getId(){return id;} public String getTenantId(){return tenantId;} public String getTitle(){return title;} public String getSeverity(){return severity;} public String getStatus(){return status;} public String getAssignee(){return assignee;} public Instant getUpdatedAt(){return updatedAt;}
 public void changeStatus(String status){if(!Set.of("OPEN","INVESTIGATING","RESOLVED","CLOSED").contains(status)) throw new IllegalArgumentException("Invalid incident status");this.status=status;this.updatedAt=Instant.now();}
}

