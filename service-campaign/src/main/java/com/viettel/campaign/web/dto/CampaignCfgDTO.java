package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CampaignCfgDTO extends BaseDTO {

    private Long campaignCompleteCodeId;
    private Long campaignId;
    private String completeValue;
    private String completeName;
    private String description;
    private Short status;
    private Short completeType;
    private Short isRecall;
    private String updateBy;
    private Date updateAt;
    private String createBy;
    private Date createAt;
    private String campaignType;
    private Short isFinish;
    private Long companySiteId;
    private Short isLock;
    private Long durationLock;
    private Long chanel;
    private Boolean checkRecall;
}
