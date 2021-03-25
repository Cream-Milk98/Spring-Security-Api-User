package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ReceiveCustLogDTO implements Serializable {
    private Long receiveCustLogId;
    private Long companySiteId;
    private Long customerId;
    private Date startTime;
    private Long agentId;
    private Long campaignId;
    private Date endTime;
    private boolean isUpdateEndTime;
}
