package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CAMPAIGN_CUSTOMERLIST")
@Getter
@Setter
public class CampaignCustomerList {
    @Id
    @GeneratedValue(generator = "campaign_customer_seq")
    @SequenceGenerator(name = "campaign_customer_seq", sequenceName = "campaign_customer_seq", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "CAMPAIGN_CUSTOMERLIST_ID")
    private Long campaignCustomerListId;
    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;
    @Column(name = "CUSTOMER_LIST_ID")
    private Long customerListId;
    @Column(name = "CUSTOMER_NUMBER")
    private Long customerNumber;
    @Column(name = "FILTER_TYPE")
    private Short filterType;
    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;
    @Column(name = "AGENT_FILTER_STATUS")
    private Short agentFilterStatus;
}
