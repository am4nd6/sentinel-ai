package com.sentinelai.ingestion.domain;
import jakarta.persistence.Entity; import jakarta.persistence.Id; import jakarta.persistence.Table;
@Entity @Table(name="tenant") public class Tenant { @Id private String id; private String name; private String status; protected Tenant() {} }
