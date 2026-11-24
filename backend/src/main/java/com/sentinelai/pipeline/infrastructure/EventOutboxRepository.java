package com.sentinelai.pipeline.infrastructure;
import com.sentinelai.pipeline.domain.EventOutbox; import java.time.Instant; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface EventOutboxRepository extends JpaRepository<EventOutbox, UUID> {
    List<EventOutbox> findTop20ByStatusAndAvailableAtLessThanEqualOrderByCreatedAtAsc(String status, Instant availableAt);
}
