package com.viettel.campaign.web.dto;

import com.viettel.campaign.model.ccms_full.ContactQuestResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactQuestResultDTO extends BaseDTO {
    private Long contactQuestResultId;
    private Long contactCustResultId;
    private Long companySiteId;
    private Long scenarioQuestionId;
    private String otherOpinion;
    private Long scenarioAnswerId;
    private Short status;
    private Long oldContactCustResultId;
    private Long customerId;
    private Long campaignId;


    public ContactQuestResult toEntity() {
        ContactQuestResult ccr = new ContactQuestResult();
        ccr.setContactCustResultId(this.contactCustResultId);
        ccr.setCompanySiteId(this.companySiteId);
        ccr.setScenarioAnswerId(this.scenarioAnswerId);
        ccr.setScenarioQuestionId(this.scenarioQuestionId);
        ccr.setOtherOpinion(this.otherOpinion);
        ccr.setStatus(this.status);
        ccr.setOldContactCustResultId(this.oldContactCustResultId);
        ccr.setCustomerId(this.customerId);
        ccr.setCampaignId(this.campaignId);
        return ccr;
    }
}
