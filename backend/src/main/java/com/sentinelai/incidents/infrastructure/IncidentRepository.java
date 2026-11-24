package com.sentinelai.incidents.infrastructure;
import com.sentinelai.incidents.domain.Incident; import java.util.*; import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.JpaRepository;
 public interface IncidentRepository extends JpaRepository<Incident,UUID>{ Page<Incident> findByTenantIdAndTitleContainingIgnoreCaseAndStatus(String tenant,String title,String status,Pageable page); Page<Incident> findByTenantIdAndTitleContainingIgnoreCase(String tenant,String title,Pageable page); long countByTenantIdAndStatus(String tenant,String status); }
