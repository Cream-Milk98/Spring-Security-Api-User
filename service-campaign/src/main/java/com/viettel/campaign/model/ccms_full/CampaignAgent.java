package com.viettel.campaign.model.ccms_full;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CAMPAIGN_AGENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignAgent implements Serializable {

    @Id
    @GeneratedValue(generator = "CAMPAIGN_AGENT_SEQ")
    @SequenceGenerator(name = "CAMPAIGN_AGENT_SEQ", sequenceName = "CAMPAIGN_AGENT_SEQ", allocationSize = 1)
    @Column(name = "CAMPAIGN_AGENT_ID")
    private Long campaignAgentId;
    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;
    @Column(name = "AGENT_ID")
    private Long agentId;
    @Column(name = "FILTER_TYPE")
    private Short filterType;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;
    @Column(name = "RE_SCHEDULE")
    private Long reSchedule;
}
