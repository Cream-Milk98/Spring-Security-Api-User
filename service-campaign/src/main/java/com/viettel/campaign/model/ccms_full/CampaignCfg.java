package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CAMPAIGN_COMPLETE_CODE")
@Getter
@Setter
public class CampaignCfg implements Serializable {

    @Id
    @GeneratedValue(generator = "CAMPAIGN_COMPLETE_CODE_SEQ")
    @SequenceGenerator(name = "CAMPAIGN_COMPLETE_CODE_SEQ", sequenceName = "CAMPAIGN_COMPLETE_CODE_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "CAMPAIGN_COMPLETE_CODE_ID")
    private Long campaignCompleteCodeId;
    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;
    @Column(name = "COMPLETE_VALUE")
    private String completeValue;
    @Column(name = "COMPLETE_NAME")
    private String completeName;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "COMPLETE_TYPE")
    private Short completeType;
    @Column(name = "IS_RECALL")
    private Short isRecall;
    @Column(name = "UPDATE_BY")
    private String updateBy;
    @Column(name = "UPDATE_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
    @Column(name = "CREATE_BY")
    private String createBy;
    @Column(name = "CREATE_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @Column(name = "CAMPAIGN_TYPE")
    private String campaignType;
    @Column(name = "IS_FINISH")
    private Short isFinish;
    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;
    @Column(name = "IS_LOCK")
    private Short isLock;
    @Column(name = "DURATION_LOCK")
    private Long durationLock;
    @Column(name = "CHANEL")
    private Long chanel;

}
