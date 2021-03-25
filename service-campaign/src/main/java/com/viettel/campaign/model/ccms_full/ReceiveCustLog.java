package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RECEIVE_CUST_LOG")
@Getter
@Setter
public class ReceiveCustLog implements Serializable {
    @Id
    @GeneratedValue(generator = "RECEIVE_CUST_LOG_SEQ")
    @SequenceGenerator(name = "RECEIVE_CUST_LOG_SEQ", sequenceName = "RECEIVE_CUST_LOG_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "RECEIVE_CUST_LOG_ID")
    private Long receiveCustLogId;
    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;
    @Column(name = "CUSTOMER_ID")
    @NotNull
    private Long customerId;
    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date startTime;
    @Column(name = "AGENT_ID")
    private Long agentId;
    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;
    @Column(name = "END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
}
