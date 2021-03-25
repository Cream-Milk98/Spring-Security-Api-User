package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CAMPAIGN")
//@Getter
//@Setter
public class Campaign implements Serializable {

    @Id
    @NotNull
    @GeneratedValue(generator = "campaign_seq")
    @SequenceGenerator(name = "campaign_seq", sequenceName = "campaign_seq", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;
    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;
    @Column(name = "CAMPAIGN_CODE")
    private String campaignCode;
    @Column(name = "CAMPAIGN_NAME")
    private String campaignName;
    @Column(name = "CHANEL")
    private Long chanel;
    @Column(name = "CONTENT")
    private String content;
    @Column(name = "CUSTOMER_NUMBER")
    private Long customerNumber;
    @Column(name = "TARGET")
    private String target;
    @Column(name = "STATUS")
    private Long status;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME")
    private Date startTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME")
    private Date endTime;
    @Column(name = "MAX_RECALL")
    private Integer maxRecall;
    @Column(name = "RECALL_TYPE")
    private Integer recallType;
    @Column(name = "RECALL_DURATION")
    private Integer recallDuration;
    @Column(name = "CREATE_BY")
    private String createBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createTime;
    @Column(name = "UPDATE_BY")
    private String updateBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    @Column(name = "CAMPAIGN_TYPE")
    private String campaignType;
    @Column(name = "PRODUCT")
    private String product;
    @Column(name = "PROCESS_STATUS")
    private Integer processStatus;
    @Column(name = "DIAL_MODE")
    private Long dialMode;
    @Column(name = "DEPT_CODE")
    private String deptCode;
    @Column(name = "TIME_RANGE")
    private String timeRange;
    @Column(name = "DAY_OF_WEEK")
    private String dayOfWeek;
    @Column(name = "CURRENT_TIME_MODE")
    private Long currentTimeMode;
    @Column(name = "WRAPUP_TIME_CONNECT")
    private Long wrapupTimeConnect;
    @Column(name = "WRAPUP_TIME_DISCONNECT")
    private Long wrapupTimeDisconnect;
    @Column(name = "PREVIEW_TIME")
    private Long previewTime;
    @Column(name = "RATE_DIAL")
    private Long rateDial;
    @Column(name = "RATE_MISS")
    private Long rateMiss;
    @Column(name = "AVG_TIME_PROCESS")
    private Long avgTimeProcess;
    @Column(name = "IS_APPLY_CUST_LOCK")
    private Long isApplyCustLock;
    @Column(name = "TARGET_TYPE")
    private Long targetType;
    @Column(name = "IS_TARGET")
    private Long isTarget;
    @Column(name = "CAMPAIGN_IVR_CALLED_ID")
    private Long campaignIvrCalledId;
    @Column(name = "CONCURRENT_CALL")
    private Long concurrentCall;
    @Column(name = "CALL_OUT_TIME_IN_DAY")
    private String callOutTimeInDay;
    @Column(name = "MUSIC_LIST")
    private String musicList;
    @Column(name = "TIME_PLAY_MUSIC")
    private Integer timePlayMusic;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CAMPAIGN_START")
    private Date campaignStart;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CAMPAIGN_END")
    private Date campaignEnd;
    @Column(name = "TIME_WAIT_AGENT")
    private Integer timeWaitAgent;
    @Column(name = "QUEST_INDEX")
    private Long questIndex;


    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getCompanySiteId() {
        return companySiteId;
    }

    public void setCompanySiteId(Long companySiteId) {
        this.companySiteId = companySiteId;
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Long getChanel() {
        return chanel;
    }

    public void setChanel(Long chanel) {
        this.chanel = chanel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Long customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getMaxRecall() {
        return maxRecall;
    }

    public void setMaxRecall(Integer maxRecall) {
        this.maxRecall = maxRecall;
    }

    public Integer getRecallType() {
        return recallType;
    }

    public void setRecallType(Integer recallType) {
        this.recallType = recallType;
    }

    public Integer getRecallDuration() {
        return recallDuration;
    }

    public void setRecallDuration(Integer recallDuration) {
        this.recallDuration = recallDuration;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public Long getDialMode() {
        return dialMode;
    }

    public void setDialMode(Long dialMode) {
        this.dialMode = dialMode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Long getCurrentTimeMode() {
        return currentTimeMode;
    }

    public void setCurrentTimeMode(Long currentTimeMode) {
        this.currentTimeMode = currentTimeMode;
    }

    public Long getWrapupTimeConnect() {
        return wrapupTimeConnect;
    }

    public void setWrapupTimeConnect(Long wrapupTimeConnect) {
        this.wrapupTimeConnect = wrapupTimeConnect;
    }

    public Long getWrapupTimeDisconnect() {
        return wrapupTimeDisconnect;
    }

    public void setWrapupTimeDisconnect(Long wrapupTimeDisconnect) {
        this.wrapupTimeDisconnect = wrapupTimeDisconnect;
    }

    public Long getPreviewTime() {
        return previewTime;
    }

    public void setPreviewTime(Long previewTime) {
        this.previewTime = previewTime;
    }

    public Long getRateDial() {
        return rateDial;
    }

    public void setRateDial(Long rateDial) {
        this.rateDial = rateDial;
    }

    public Long getRateMiss() {
        return rateMiss;
    }

    public void setRateMiss(Long rateMiss) {
        this.rateMiss = rateMiss;
    }

    public Long getAvgTimeProcess() {
        return avgTimeProcess;
    }

    public void setAvgTimeProcess(Long avgTimeProcess) {
        this.avgTimeProcess = avgTimeProcess;
    }

    public Long getIsApplyCustLock() {
        return isApplyCustLock;
    }

    public void setIsApplyCustLock(Long isApplyCustLock) {
        this.isApplyCustLock = isApplyCustLock;
    }

    public Long getTargetType() {
        return targetType;
    }

    public void setTargetType(Long targetType) {
        this.targetType = targetType;
    }

    public Long getIsTarget() {
        return isTarget;
    }

    public void setIsTarget(Long isTarget) {
        this.isTarget = isTarget;
    }

    public Long getCampaignIvrCalledId() {
        return campaignIvrCalledId;
    }

    public void setCampaignIvrCalledId(Long campaignIvrCalledId) {
        this.campaignIvrCalledId = campaignIvrCalledId;
    }

    public Long getConcurrentCall() {
        return concurrentCall;
    }

    public void setConcurrentCall(Long concurrentCall) {
        this.concurrentCall = concurrentCall;
    }

    public String getCallOutTimeInDay() {
        return callOutTimeInDay;
    }

    public void setCallOutTimeInDay(String callOutTimeInDay) {
        this.callOutTimeInDay = callOutTimeInDay;
    }

    public String getMusicList() {
        return musicList;
    }

    public void setMusicList(String musicList) {
        this.musicList = musicList;
    }

    public Integer getTimePlayMusic() {
        return timePlayMusic;
    }

    public void setTimePlayMusic(Integer timePlayMusic) {
        this.timePlayMusic = timePlayMusic;
    }

    public Date getCampaignStart() {
        return campaignStart;
    }

    public void setCampaignStart(Date campaignStart) {
        this.campaignStart = campaignStart;
    }

    public Date getCampaignEnd() {
        return campaignEnd;
    }

    public void setCampaignEnd(Date campaignEnd) {
        this.campaignEnd = campaignEnd;
    }

    public Integer getTimeWaitAgent() {
        return timeWaitAgent;
    }

    public void setTimeWaitAgent(Integer timeWaitAgent) {
        this.timeWaitAgent = timeWaitAgent;
    }

    public Long getQuestIndex() {
        return questIndex;
    }

    public void setQuestIndex(Long questIndex) {
        this.questIndex = questIndex;
    }
}
