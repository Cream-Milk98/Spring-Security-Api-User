package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "CONTACT_QUEST_RESULT")
@Getter
@Setter
public class ContactQuestResult implements Serializable {
    @Id
    @NotNull
    @GeneratedValue(generator = "CONTACT_QUEST_RESULT_SEQ")
    @SequenceGenerator(name = "CONTACT_QUEST_RESULT_SEQ", sequenceName = "CONTACT_QUEST_RESULT_SEQ", allocationSize = 1)
//    @Basic(optional = false)
    @Column(name = "CONTACT_QUEST_RESULT_ID")
    private Long contactQuestResultId;
    @Column(name = "CONTACT_CUST_RESULT_ID")
    @NotNull
    private Long contactCustResultId;
    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;
    @Column(name = "SCENARIO_QUESTION_ID")
    @NotNull
    private Long scenarioQuestionId;
    @Column(name = "OTHER_OPINION")
    private String otherOpinion;
    @Column(name = "SCENARIO_ANSWER_ID")
    private Long scenarioAnswerId;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "OLD_CONTACT_CUST_RESULT_ID")
    private Long oldContactCustResultId;
    @Column(name = "CUSTOMER_ID")
    @NotNull
    private Long customerId;
    @Column(name = "CAMPAIGN_ID")
    @NotNull
    private Long campaignId;


    public Long getContactQuestResultId() {
        return contactQuestResultId;
    }

    public void setContactQuestResultId(Long contactQuestResultId) {
        this.contactQuestResultId = contactQuestResultId;
    }

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

    public Long getScenarioQuestionId() {
        return scenarioQuestionId;
    }

    public void setScenarioQuestionId(Long scenarioQuestionId) {
        this.scenarioQuestionId = scenarioQuestionId;
    }

    public String getOtherOpinion() {
        return otherOpinion;
    }

    public void setOtherOpinion(String otherOpinion) {
        this.otherOpinion = otherOpinion;
    }

    public Long getScenarioAnswerId() {
        return scenarioAnswerId;
    }

    public void setScenarioAnswerId(Long scenarioAnswerId) {
        this.scenarioAnswerId = scenarioAnswerId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
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

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }
}
