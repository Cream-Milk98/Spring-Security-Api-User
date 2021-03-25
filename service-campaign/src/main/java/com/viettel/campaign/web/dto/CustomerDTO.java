package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CustomerDTO extends BaseDTO {

    private Long customerId;
    private String code;
    private String name;
    private String description;
    private String companyName;
    private String customerImg;
    private Date createDate;
    private Date updateDate;
    private String status;
    private String createBy;
    private String updateBy;
    private Long siteId;
    private Short gender;
    private String currentAddress;
    private String placeOfBirth;
    private Date dateOfBirth;
    private String mobileNumber;
    private String email;
    private String userName;
    private String areaCode;
    private Long customerType;
    private String callAllowed;
    private Long emailAllowed;
    private Long smsAllowed;
    private String ipccStatus;
    private String customerDnc;
}
