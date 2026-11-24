package com.sentinelai.incidents.infrastructure;
import com.sentinelai.incidents.domain.IncidentEvidence; import java.util.UUID; import org.springframework.data.jpa.repository.JpaRepository;
public interface IncidentEvidenceRepository extends JpaRepository<IncidentEvidence,UUID>{}
