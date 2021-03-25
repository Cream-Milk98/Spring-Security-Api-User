package com.viettel.campaign.web.dto.request_dto;


import com.viettel.campaign.web.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CustomizeRequestDTo extends BaseDTO {
    String operatorLogic;
    String name;
    String filterCustomer;
    String compare;
    String valueCustomer;
    String customerId;
    String companyName;
    String status;
    String siteId;
    Short gender;
    String currentAddress;
    String placeOfBirth;
    Date dateOfBirth;
    String mobileNumber;
    String email;
    String userName;
    Long customerType;


}
