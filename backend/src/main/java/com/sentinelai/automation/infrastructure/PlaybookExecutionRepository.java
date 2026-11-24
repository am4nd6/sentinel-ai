package com.sentinelai.automation.infrastructure;
import com.sentinelai.automation.domain.PlaybookExecution; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface PlaybookExecutionRepository extends JpaRepository<PlaybookExecution,UUID>{ Optional<PlaybookExecution> findByTenantIdAndIdempotencyKey(String tenant,String key); long countByIdAndTenantId(UUID id,String tenant); }
