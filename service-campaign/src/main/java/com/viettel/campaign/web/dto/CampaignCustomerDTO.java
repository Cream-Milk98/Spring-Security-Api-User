package com.viettel.campaign.web.dto;

import com.viettel.campaign.web.dto.request_dto.CustomerQueryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

//@Getter
//@Setter
public class CampaignCustomerDTO extends BaseDTO{
    private Long campaignCustomerListId;
    private Long campaignId;
    private Long customerId;
    private Short status;
    private Long agentId;
    private Date recallTime;
    private Long recallCount = 0L;
    private Date callTime;
    private Long callTimeL;
    private Long customerListId;
    private Short inCampaignStatus;
    private Date sendTime;
    private Short processStatus = 0;
    private Short contactStatus;
    private Short redistribute;
    private String sessionId;
    private Long callStatus;
    private Long companySiteId;
    private Long complainId;
    private String lstCustomerId;
    private List<CustomerQueryDTO>  listQuery;
    private Long field;
    private String type;
    private Integer timezoneOffset;

    public Long getCampaignCustomerListId() {
        return campaignCustomerListId;
    }

    public void setCampaignCustomerListId(Long campaignCustomerListId) {
        this.campaignCustomerListId = campaignCustomerListId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Date getRecallTime() {
        return recallTime;
    }

    public void setRecallTime(Date recallTime) {
        this.recallTime = recallTime;
    }

    public Long getRecallCount() {
        return recallCount;
    }

    public void setRecallCount(Long recallCount) {
        this.recallCount = recallCount;
    }

    public Date getCallTime() {
        return callTime;
    }

    public void setCallTime(Date callTime) {
        this.callTime = callTime;
    }

    public Long getCallTimeL() {
        return callTimeL;
    }

    public void setCallTimeL(Long callTimeL) {
        this.callTimeL = callTimeL;
    }

    public Long getCustomerListId() {
        return customerListId;
    }

    public void setCustomerListId(Long customerListId) {
        this.customerListId = customerListId;
    }

    public Short getInCampaignStatus() {
        return inCampaignStatus;
    }

    public void setInCampaignStatus(Short inCampaignStatus) {
        this.inCampaignStatus = inCampaignStatus;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Short getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Short processStatus) {
        this.processStatus = processStatus;
    }

    public Short getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(Short contactStatus) {
        this.contactStatus = contactStatus;
    }

    public Short getRedistribute() {
        return redistribute;
    }

    public void setRedistribute(Short redistribute) {
        this.redistribute = redistribute;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(Long callStatus) {
        this.callStatus = callStatus;
    }

    public Long getCompanySiteId() {
        return companySiteId;
    }

    public void setCompanySiteId(Long companySiteId) {
        this.companySiteId = companySiteId;
    }

    public Long getComplainId() {
        return complainId;
    }

    public void setComplainId(Long complainId) {
        this.complainId = complainId;
    }

    public String getLstCustomerId() {
        return lstCustomerId;
    }

    public void setLstCustomerId(String lstCustomerId) {
        this.lstCustomerId = lstCustomerId;
    }

    public List<CustomerQueryDTO> getListQuery() {
        return listQuery;
    }

    public void setListQuery(List<CustomerQueryDTO> listQuery) {
        this.listQuery = listQuery;
    }

    public Long getField() {
        return field;
    }

    public void setField(Long field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(Integer timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }
}
