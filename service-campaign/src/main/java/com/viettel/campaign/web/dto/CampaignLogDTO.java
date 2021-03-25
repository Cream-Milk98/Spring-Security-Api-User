package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author anhvd_itsol
 */

@Getter
@Setter
public class CampaignLogDTO {
    private Long campaignLogId;

    private Long companySiteId;

    private Date createTime;

    private Long agentId;

    private String tableName;

    private String columnName;

    private String preValue;

    private String posValue;

    private String description;

    private Long campaignId;

    private Long customerId;
}
