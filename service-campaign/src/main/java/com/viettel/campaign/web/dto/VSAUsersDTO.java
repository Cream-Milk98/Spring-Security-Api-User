package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class VSAUsersDTO {
    Long userId;
    String userName;
    Short status;
    String fullName;
//    Long userTypeId;
//    Date createDate;
//    String description;
//    String staffCode;
//    Long managerId;
//    Long locationId;
//    Long deptId;
//    String deptLevel;
//    Long posId;
//    String deptName;
//    Long groupId;
//    Long siteId;
    Long companySiteId;
//    Short agentType;
//    String mobileNumber;
//    String facebookId;
//    Short loginType;
//    String googleId;
//    String email;
//    Long availableTicket;
//    String userKazooId;
    Short filterType;
    Long campaignAgentId;
    String roleCode;
}
