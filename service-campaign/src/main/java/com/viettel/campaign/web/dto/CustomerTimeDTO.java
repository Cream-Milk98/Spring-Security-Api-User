package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CustomerTimeDTO extends BaseDTO {
    private Long customerTimeId;
    private Long companySiteId;
    private Long customerId;
    private Date startTime;
    private Date endTime;
    private Short status;
    private Date createTime;
    private Date updateTime;
    private Long createBy;
    private Long updateBy;
    private Long contactCustResultId;

    private String completeValue;
}
