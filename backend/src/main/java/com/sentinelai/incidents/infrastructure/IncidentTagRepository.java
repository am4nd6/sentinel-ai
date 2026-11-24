package com.sentinelai.incidents.infrastructure;
import com.sentinelai.incidents.domain.IncidentTag; import org.springframework.data.jpa.repository.JpaRepository;
public interface IncidentTagRepository extends JpaRepository<IncidentTag,IncidentTag.Key>{}
