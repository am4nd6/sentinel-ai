package com.sentinelai.incidents.infrastructure; import com.sentinelai.incidents.domain.IncidentTimeline; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface IncidentTimelineRepository extends JpaRepository<IncidentTimeline,UUID>{ List<IncidentTimeline> findByIncidentIdAndTenantIdOrderByCreatedAtAsc(UUID id,String tenant); }
