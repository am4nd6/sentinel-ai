package com.sentinelai.ingestion.infrastructure;
import com.sentinelai.ingestion.domain.SecurityEvent;
import java.util.Optional; import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SecurityEventRepository extends JpaRepository<SecurityEvent, UUID> { Optional<SecurityEvent> findByTenantIdAndDedupeKey(String tenantId, String dedupeKey); }
