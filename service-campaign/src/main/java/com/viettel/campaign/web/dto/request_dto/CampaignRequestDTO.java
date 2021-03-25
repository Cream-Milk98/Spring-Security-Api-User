package com.viettel.campaign.web.dto.request_dto;

import com.viettel.campaign.web.dto.BaseDTO;
import com.viettel.campaign.web.dto.CustomerCustomDTO;
import com.viettel.campaign.web.dto.CustomerListDTO;
import com.viettel.campaign.web.dto.FieldsToShowDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author anhvd_itsol
 */

@Getter
@Setter
public class CampaignRequestDTO extends BaseDTO {
    String campaignCode;
    String campaignName;
    Short status;
    String fromDateFr;
    String fromDateTo;
    String toDateTo;
    String toDateFr;
    Long numOfCusFr;
    Long numOfCusTo;
    Short type;
    Short chanel;
    String companySiteId;
    String agentId;
    String types;
    String phoneNumber;
    String contactStatus;
    String campaignStatus;
    String customerId;
    String customerName;
    String callTimeTo;
    String callTimeFrom;
    String recordStatus;
    String connectStatus;
    String toDate;
    String fromDate;
    String campaignType;
    String agentName;
    String campaignId;
    String surveyStatus;
    String roleUser;
    String contactCustId;
    List<CustomerCustomDTO> customerCustomDTOList;
    String custListCode;
    String custListName;
    String createTimeTo;
    String createTimeFr;
    String lstCustomerListId;
    List<FieldsToShowDTO> lstFiedCustomer;
    List<CustomerListDTO> lstCustomerCampaign;
    String customerListId;
    List<CustomerListDTO> listCustomerListId;
    String statuses;
    String channels;
    Integer timezoneOffset;
    Short agentStatus;
    String language;
}
