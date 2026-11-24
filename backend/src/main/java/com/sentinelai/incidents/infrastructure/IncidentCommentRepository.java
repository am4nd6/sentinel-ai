package com.sentinelai.incidents.infrastructure;
import com.sentinelai.incidents.domain.IncidentComment; import java.util.UUID; import org.springframework.data.jpa.repository.JpaRepository;
public interface IncidentCommentRepository extends JpaRepository<IncidentComment,UUID>{}
