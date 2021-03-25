package com.viettel.campaign.service.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.UserActionLog;
import com.viettel.campaign.repository.ccms_full.UserActionLogRepository;
import com.viettel.campaign.service.UserActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author anhvd_itsol
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class UserActionLogServiceImpl implements UserActionLogService {

    @Autowired
    UserActionLogRepository userActionLogRepository;

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public void save(UserActionLog log) {
        userActionLogRepository.save(log);
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public UserActionLog getByAgentId(Long agentId) {
        return userActionLogRepository.findByAgentId(agentId);
    }
}
