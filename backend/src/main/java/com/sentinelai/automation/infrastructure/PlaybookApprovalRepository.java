package com.sentinelai.automation.infrastructure;
import com.sentinelai.automation.domain.PlaybookApproval; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface PlaybookApprovalRepository extends JpaRepository<PlaybookApproval,UUID>{ List<PlaybookApproval> findByExecutionIdAndTenantId(UUID execution,String tenant); }
