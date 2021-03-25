package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CustomerContactDTO extends BaseDTO {
    private Long contactId;
    private Long customerId;
    private Short contactType;
    private String contact;
    private Short isDirectLine;
    private Short status;
    private Date createDate;
    private Date updateDate;
    private String createBy;
    private String updateBy;
    private Date startDate;
    private Date endDate;
    private String language;
    private Long siteId;
}
