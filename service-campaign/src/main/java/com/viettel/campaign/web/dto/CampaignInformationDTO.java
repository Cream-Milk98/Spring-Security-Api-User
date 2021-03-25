package com.viettel.campaign.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignInformationDTO {
    private Long campaignId;
    private Long totalIndividual;
    private Long totalNotInteractive;
    private Long totalNotCall;
    private Long campaignCustomer;
    private Long customerListId;
    private Long totalCusInList;
}
