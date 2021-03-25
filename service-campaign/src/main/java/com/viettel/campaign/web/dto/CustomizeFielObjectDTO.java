package com.viettel.campaign.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomizeFielObjectDTO implements Serializable {
    private Long customerId;
    private String name;
    private String companyName;
    private String customerImg;
    private String status;
    private Long siteId;
    private Short gender;
    private String currentAddress;
    private String placeOfBirth;
    private Date dateOfBirth;
    private String mobileNumber;
    private String email;
    private String userName;
    private Long customerType;
    private Long customizeFieldObjectId;
    private Long objectId;
    private Long customizeFieldId;
    private String valueText;
    private Long valueNumber;
    private Date valueDate;
    private Long valueCheckbox;
    private Date createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
    private Long fieldOptionValueId;
    private String title;
    private String functionCode;
    private  String active;

}
