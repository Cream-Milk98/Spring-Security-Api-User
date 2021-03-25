package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CONTACT_CUST_RESULT")
//@Getter
//@Setter
public class ContactCustResult implements Serializable {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTACT_CUST_RESULT_SEQ")
    @SequenceGenerator(name = "CONTACT_CUST_RESULT_SEQ", sequenceName = "CONTACT_CUST_RESULT_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "CONTACT_CUST_RESULT_ID")
    private Long contactCustResultId;
    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;
    @Column(name = "CALL_STATUS")
    private Short callStatus;
    @Column(name = "CONTACT_STATUS")

    private Short contactStatus;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "SATISFACTION")
    private Integer satisfaction;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)

    private Date createTime;
    @Column(name = "AGENT_ID")
    private Long agentId;
    @Column(name = "UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Column(name = "UPDATE_BY")
    private Long updateBy;
    @Column(name = "CAMPAIGN_ID")

    private Long campaignId;
    @Column(name = "OLD_CONTACT_CUST_RESULT_ID")
    private Long oldContactCustResultId;
    @Column(name = "CUSTOMER_ID")

    private Long customerId;
    @Column(name = "DURATION_CALL")
    private Long durationCall;
    @Column(name = "START_CALL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startCall;
    @Column(name = "RECEIVE_CUST_LOG_ID")
    private Long receiveCustLogId;
    @Column(name = "IPCC_CALL_STATUS")
    private Short ipccCallStatus;
    @Column(name = "CALL_ID")
    private String callId;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "RECEIVE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveTime;
    @Column(name = "PRE_END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date preEndTime;
    @Column(name = "URL_CALL")
    private String urlCall;
    @Column(name = "TRANSACTION_ID")
    private String transactionId;
    @Column(name = "RECALL_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recallTime;
    @Column(name = "IS_FINAL_RECALL")
    private Short isFinalRecall;
    @Column(name = "IS_SEND_EMAIL")
    private Short isSendEmail = 0;
    @Column(name = "SALED_ON_TPIN")
    private Long saledOnTpin;
    @Column(name = "END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Column(name = "WAIT_TIME")
    private Long waitTime;
    @Column(name = "DIAL_MODE")
    private String dialMode;
    @Column(name = "WRAPUP_TIME")
    private Long wrapupTime;
    @Column(name = "TIME_MAKE_CALL")
    private Long timeMakeCall = 0L;
    @Column(name = "TIME_RECEIVE_CUST")
    private Long timeReceiveCust = 0L;

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
}
