package com.viettel.campaign.service;

import com.viettel.campaign.model.ccms_full.UserActionLog;

/**
 * @author hanv_itsol
 * @project campaign
 */
public interface UserActionLogService {

    void save(UserActionLog log);

    UserActionLog getByAgentId(Long agentId);
}
