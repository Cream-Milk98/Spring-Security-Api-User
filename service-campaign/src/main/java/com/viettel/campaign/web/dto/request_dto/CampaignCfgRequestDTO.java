package com.viettel.campaign.web.dto.request_dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CampaignCfgRequestDTO {
    List<Long> listId;
    Long companySiteId;
    Long campaignCompleteCodeID;

}
