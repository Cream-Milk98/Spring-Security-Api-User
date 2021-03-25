package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CampaignDTO extends BaseDTO {

    private Long campaignId;
    private Long companySiteId;
    private String campaignCode;
    private String campaignName;
    private Short chanel;
    private String content;
    private Long customerNumber;
    private String target;
    private Short status;
    private Date startTime;
    private Date endTime;
    private Integer maxRecall;
    private Integer recallType;
    private Integer recallDuration;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String campaignType;
    private String product;
    private Integer processStatus;
    private Long dialMode;
    private String deptCode;
    private String timeRange;
    private String dayOfWeek;
    private Long currentTimeMode;
    private Long wrapupTimeConnect;
    private Long wrapupTimeDisconnect;
    private Long previewTime;
    private Long rateDial;
    private Long rateMiss;
    private Long avgTimeProcess;
    private Long isApplyCustLock;
    private Long targetType;
    private Long isTarget;
    private Long campaignIvrCalledId;
    private Long concurrentCall;
    private String callOutTimeInDay;
    private String musicList;
    private Integer timePlayMusic;
    private Date campaignStart;
    private Date campaignEnd;
    private Integer timeWaitAgent;
    private Long questIndex;
    private Long numOfJoinedCus;
    private Long numOfLockCus;
    private String campaignTypeName;
    private Date timeRangeStartTime;
    private String timeZoneHour;
    private String timeZoneMinute;
    private List<TimeRangeDialModeDTO> lstTimeRange;
    private List<TimeZoneDialModeDTO> lstTimeZone;
    private Short agentStatus;
    private String sessionId;
    private Long numOfInteractedCus;
    private Long numOfNotInteractedCus;
}
