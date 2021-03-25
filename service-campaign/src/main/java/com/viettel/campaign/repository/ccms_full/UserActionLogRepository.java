package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.UserActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hanv_itsol
 * @project campaign
 */
@Repository
public interface UserActionLogRepository extends JpaRepository<UserActionLog, Long> {
    //
    UserActionLog findByAgentId(Long agentId);
}
