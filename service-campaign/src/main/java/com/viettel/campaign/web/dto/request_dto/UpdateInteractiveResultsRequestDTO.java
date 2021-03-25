package com.viettel.campaign.web.dto.request_dto;

import com.viettel.campaign.web.dto.CampaignCustomerDTO;
import com.viettel.campaign.web.dto.ContactQuestResultDTO;
import com.viettel.campaign.web.dto.CustomerTimeDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author anhvd_itsol
 */

@Getter
@Setter
public class UpdateInteractiveResultsRequestDTO {
    Long campaignId;
    Long companySiteId;
    Long customerId;
    Long contactCusResultId;
    List<ContactQuestResultDTO> lstContactQuestResult;
    CustomerTimeDTO customerTime;
    Long userId;
    CampaignCustomerDTO customer;
}
