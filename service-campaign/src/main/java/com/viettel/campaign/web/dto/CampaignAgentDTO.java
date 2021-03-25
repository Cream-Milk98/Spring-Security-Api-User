package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignAgentDTO extends BaseDTO {
    Long campaignAgentId;
    Long campaignId;
    Long agentId;
    Short filterType;
    Integer status;
    Long companySiteId;
    Long reSchedule;
}
