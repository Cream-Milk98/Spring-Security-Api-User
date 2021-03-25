package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author anhvd_itsol
 */

@Entity
@Table(name = "CAMPAIGN_LOG")
//@Getter
//@Setter
public class CampaignLog implements Serializable {

    @Id
    @NotNull
    @GeneratedValue(generator = "CAMPAIGN_LOG_SEQ")
    @SequenceGenerator(name = "CAMPAIGN_LOG_SEQ", sequenceName = "CAMPAIGN_LOG_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "CAMPAIGN_LOG_ID")
    private Long campaignLogId;

    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "AGENT_ID")
    private Long agentId;

    @Column(name = "TABLE_NAME")
    private String tableName;

    @Column(name = "COLUMN_NAME")
    private String columnName;

    @Column(name = "PRE_VALUE")
    private String preValue;

    @Column(name = "POST_VALUE")
    private String postValue;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    public Long getCampaignLogId() {
        return campaignLogId;
    }

    public void setCampaignLogId(Long campaignLogId) {
        this.campaignLogId = campaignLogId;
    }

    public Long getCompanySiteId() {
        return companySiteId;
    }

    public void setCompanySiteId(Long companySiteId) {
        this.companySiteId = companySiteId;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getPreValue() {
        return preValue;
    }

    public void setPreValue(String preValue) {
        this.preValue = preValue;
    }

    public String getPostValue() {
        return postValue;
    }

    public void setPostValue(String postValue) {
        this.postValue = postValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
