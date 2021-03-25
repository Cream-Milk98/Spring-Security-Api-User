package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.ReceiveCustLog;
import com.viettel.campaign.web.dto.ReceiveCustLogDTO;

import java.util.Date;
import java.util.List;

public interface ReceiveCustLogRepositoryCustom {
    Boolean checkCampaignStartAtLeastOnce(Long campaignId, Date startTime, Date endTime);

    List<ReceiveCustLog> findTop100EndTimeIsNullAndCondition(String condition);
}
