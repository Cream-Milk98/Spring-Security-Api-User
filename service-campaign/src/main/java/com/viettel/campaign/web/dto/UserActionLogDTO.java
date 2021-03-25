package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author anhvd_itsol
 */

@Getter
@Setter
public class UserActionLogDTO {
    private Long agentId; //userId
    private Long companySiteId;
    private String sessionId;
    private Date startTime;
    private Date endTime;
    private Short actionType;
    private String description;
    private Long objectId; //campaignId
}
