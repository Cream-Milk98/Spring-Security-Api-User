package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerCustomDTO {
    Long customerListMappingId;
    Long companySiteId;
    Long customerListId;
    Long customerId;
    String name;
    String description;
    String companyName;
    String customerType;
    String currentAddress;
    Short callAllowed;
    Short emailAllowed;
    Short smsAllowed;
    String ipccStatus;
    String mobileNumber;
    String email;
    String connectStatus;
    String connectTime;
    String customerName;
    Long campaignCustomerId;
    Integer totalRow;
}
