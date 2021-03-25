package com.viettel.campaign.web.dto.request_dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CampaignAgentRequestDTO {
    Long companySiteId;
    Long campaignId;
    List<Long> agentId;
}
