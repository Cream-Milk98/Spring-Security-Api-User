package com.viettel.campaign.web.dto.request_dto;

import com.viettel.campaign.web.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentRequestDTO extends BaseDTO {
    private int companySiteId;
    private String campaignId;
    private String userName;
    private String fullName;
}
