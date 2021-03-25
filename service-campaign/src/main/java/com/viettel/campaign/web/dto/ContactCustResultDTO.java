package com.viettel.campaign.web.dto;

import com.viettel.campaign.model.ccms_full.CampaignCfg;
import com.viettel.campaign.model.ccms_full.CampaignCustomer;
import com.viettel.campaign.model.ccms_full.ContactQuestResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

//@Getter
//@Setter
@NoArgsConstructor
public class ContactCustResultDTO extends BaseDTO {
    private Long contactCustResultId;
    private Long companySiteId;
    private Short callStatus;
    private Short contactStatus;
    private Short status;
    private Integer satisfaction;
    private String description;
    private Date createTime;
    private Long agentId;
    private Date updateTime;
    private Long updateBy;
    private Long campaignId;
    private Long oldContactCustResultId;
    private Long customerId;
    private Long durationCall;
    private Long startCallL; // api get call return Long
    private Date startCall;
    private Long receiveCustLogId;
    private Short ipccCallStatus;
    private String callId;
    private String phoneNumber;
    private Date receiveTime;
    private Date preEndTime;
    private String urlCall;
    private String transactionId;
    private Date recallTime;
    private Short isFinalRecall;
    private Short isSendEmail = 0;
    private Long saledOnTpin;
    private Long endTimeL; // api get call return Long
    private Date endTime;
    // private Date endTime;
    private Long waitTime;
    private String dialMode;
    private Long wrapupTime;
    private Long timeMakeCall = 0L;
    private Long timeReceiveCust = 0L;
    private Short recordStatus;
    private Long callTime;
    private String campaignName;
    private String customerName;
    private String campaignCode;
    private String userName;
    private String surveyStatus;
    private String connectStatus;
    private String surveyStatusName;
    private String connectStatusName;
    private Boolean enableEdit;

    private Integer counter;
    private String eventCall;
    private Boolean checkUpdate = false;
    private CampaignCfg contactStatusObj;
    private List<CampaignCfg> lstContactStatus;
    private CampaignCfg callStatusObj;
    private List<CampaignCfg> lstCallStatus;
    private CampaignCustomer campaignCustomerObj;
    private List<ContactQuestResult> lstContactQuest;
    private CampaignCustomerDTO campaignCustomerObjDTO;
    private Boolean isReceiveEndCall = false;

    public Long getContactCustResultId() {
        return contactCustResultId;
    }

    public void setContactCustResultId(Long contactCustResultId) {
        this.contactCustResultId = contactCustResultId;
    }

    public Long getCompanySiteId() {
        return companySiteId;
    }

    public void setCompanySiteId(Long companySiteId) {
        this.companySiteId = companySiteId;
    }

    public Short getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(Short callStatus) {
        this.callStatus = callStatus;
    }

    public Short getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(Short contactStatus) {
        this.contactStatus = contactStatus;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Integer getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Integer satisfaction) {
        this.satisfaction = satisfaction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getOldContactCustResultId() {
        return oldContactCustResultId;
    }

    public void setOldContactCustResultId(Long oldContactCustResultId) {
        this.oldContactCustResultId = oldContactCustResultId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getDurationCall() {
        return durationCall;
    }

    public void setDurationCall(Long durationCall) {
        this.durationCall = durationCall;
    }

    public Long getStartCallL() {
        return startCallL;
    }

    public void setStartCallL(Long startCallL) {
        this.startCallL = startCallL;
    }

    public Date getStartCall() {
        return startCall;
    }

    public void setStartCall(Date startCall) {
        this.startCall = startCall;
    }

    public Long getReceiveCustLogId() {
        return receiveCustLogId;
    }

    public void setReceiveCustLogId(Long receiveCustLogId) {
        this.receiveCustLogId = receiveCustLogId;
    }

    public Short getIpccCallStatus() {
        return ipccCallStatus;
    }

    public void setIpccCallStatus(Short ipccCallStatus) {
        this.ipccCallStatus = ipccCallStatus;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getPreEndTime() {
        return preEndTime;
    }

    public void setPreEndTime(Date preEndTime) {
        this.preEndTime = preEndTime;
    }

    public String getUrlCall() {
        return urlCall;
    }

    public void setUrlCall(String urlCall) {
        this.urlCall = urlCall;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getRecallTime() {
        return recallTime;
    }

    public void setRecallTime(Date recallTime) {
        this.recallTime = recallTime;
    }

    public Short getIsFinalRecall() {
        return isFinalRecall;
    }

    public void setIsFinalRecall(Short isFinalRecall) {
        this.isFinalRecall = isFinalRecall;
    }

    public Short getIsSendEmail() {
        return isSendEmail;
    }

    public void setIsSendEmail(Short isSendEmail) {
        this.isSendEmail = isSendEmail;
    }

    public Long getSaledOnTpin() {
        return saledOnTpin;
    }

    public void setSaledOnTpin(Long saledOnTpin) {
        this.saledOnTpin = saledOnTpin;
    }

    public Long getEndTimeL() {
        return endTimeL;
    }

    public void setEndTimeL(Long endTimeL) {
        this.endTimeL = endTimeL;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
    }

    public String getDialMode() {
        return dialMode;
    }

    public void setDialMode(String dialMode) {
        this.dialMode = dialMode;
    }

    public Long getWrapupTime() {
        return wrapupTime;
    }

    public void setWrapupTime(Long wrapupTime) {
        this.wrapupTime = wrapupTime;
    }

    public Long getTimeMakeCall() {
        return timeMakeCall;
    }

    public void setTimeMakeCall(Long timeMakeCall) {
        this.timeMakeCall = timeMakeCall;
    }

    public Long getTimeReceiveCust() {
        return timeReceiveCust;
    }

    public void setTimeReceiveCust(Long timeReceiveCust) {
        this.timeReceiveCust = timeReceiveCust;
    }

    public Short getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Short recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Long getCallTime() {
        return callTime;
    }

    public void setCallTime(Long callTime) {
        this.callTime = callTime;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSurveyStatus() {
        return surveyStatus;
    }

    public void setSurveyStatus(String surveyStatus) {
        this.surveyStatus = surveyStatus;
    }

    public String getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(String connectStatus) {
        this.connectStatus = connectStatus;
    }

    public String getConnectStatusName() {
        return connectStatusName;
    }

    public void setConnectStatusName(String connectStatusName) {
        this.connectStatusName = connectStatusName;
    }

    public String getSurveyStatusName() {
        return surveyStatusName;
    }

    public void setSurveyStatusName(String surveyStatusName) {
        this.surveyStatusName = surveyStatusName;
    }

    public Boolean getEnableEdit() {
        return enableEdit;
    }

    public void setEnableEdit(Boolean enableEdit) {
        this.enableEdit = enableEdit;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public String getEventCall() {
        return eventCall;
    }

    public void setEventCall(String eventCall) {
        this.eventCall = eventCall;
    }

    public Boolean getCheckUpdate() {
        return checkUpdate;
    }

    public void setCheckUpdate(Boolean checkUpdate) {
        this.checkUpdate = checkUpdate;
    }

    public CampaignCfg getContactStatusObj() {
        return contactStatusObj;
    }

    public void setContactStatusObj(CampaignCfg contactStatusObj) {
        this.contactStatusObj = contactStatusObj;
    }

    public List<CampaignCfg> getLstContactStatus() {
        return lstContactStatus;
    }

    public void setLstContactStatus(List<CampaignCfg> lstContactStatus) {
        this.lstContactStatus = lstContactStatus;
    }

    public CampaignCfg getCallStatusObj() {
        return callStatusObj;
    }

    public void setCallStatusObj(CampaignCfg callStatusObj) {
        this.callStatusObj = callStatusObj;
    }

    public List<CampaignCfg> getLstCallStatus() {
        return lstCallStatus;
    }

    public void setLstCallStatus(List<CampaignCfg> lstCallStatus) {
        this.lstCallStatus = lstCallStatus;
    }

    public CampaignCustomer getCampaignCustomerObj() {
        return campaignCustomerObj;
    }

    public void setCampaignCustomerObj(CampaignCustomer campaignCustomerObj) {
        this.campaignCustomerObj = campaignCustomerObj;
    }

    public List<ContactQuestResult> getLstContactQuest() {
        return lstContactQuest;
    }

    public void setLstContactQuest(List<ContactQuestResult> lstContactQuest) {
        this.lstContactQuest = lstContactQuest;
    }

    public CampaignCustomerDTO getCampaignCustomerObjDTO() {
        return campaignCustomerObjDTO;
    }

    public void setCampaignCustomerObjDTO(CampaignCustomerDTO campaignCustomerObjDTO) {
        this.campaignCustomerObjDTO = campaignCustomerObjDTO;
    }

    public Boolean getIsReceiveEndCall() {
        return isReceiveEndCall;
    }

    public void setIsReceiveEndCall(Boolean isReceiveEndCall) {
        this.isReceiveEndCall = isReceiveEndCall;
    }
}
