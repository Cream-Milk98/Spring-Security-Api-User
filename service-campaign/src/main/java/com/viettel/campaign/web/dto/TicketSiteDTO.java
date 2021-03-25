package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TicketSiteDTO extends BaseDTO {

    private Long siteId;
    private String siteCode;
    private String siteName;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;
    private Long status;
    private Long parentId;
    private String accountKazooId;
    private Long servicePlanId;
}
