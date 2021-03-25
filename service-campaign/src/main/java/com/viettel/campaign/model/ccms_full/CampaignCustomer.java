package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "CAMPAIGN_CUSTOMER")
@Getter
@Setter
public class CampaignCustomer {

    @Id
    @GeneratedValue(generator = "CAMPAIGN_CUSTOMER_SEQ")
    @SequenceGenerator(name = "CAMPAIGN_CUSTOMER_SEQ", sequenceName = "CAMPAIGN_CUSTOMER_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "CAMPAIGN_CUSTOMER_ID")
    private Long campaignCustomerId;
    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;
    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "AGENT_ID")
    private Long agentId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RECALL_TIME")
    private Date recallTime;
    @Column(name = "RECALL_COUNT")
    private Long recallCount = 0L;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CALL_TIME")
    private Date callTime;
    @Column(name = "CUSTOMER_LIST_ID")
    private Long customerListId;
    @Column(name = "IN_CAMPAIGN_STATUS")
    private Short inCampaignStatus;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SEND_TIME")
    private Date sendTime;
    @Column(name = "PROCESS_STATUS")
    private Short processStatus = 0;
    @Column(name = "CONTACT_STATUS")
    private Short contactStatus;
    @Column(name = "REDISTRIBUTE")
    private Short redistribute;
    @Column(name = "SESSION_ID")
    private String sessionId;
    @Column(name = "CALL_STATUS")
    private Long callStatus;
    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;
    @Column(name = "COMPLAIN_ID")
    private Long complainId;

    @Transient
    private String recallTimeStr;
}
