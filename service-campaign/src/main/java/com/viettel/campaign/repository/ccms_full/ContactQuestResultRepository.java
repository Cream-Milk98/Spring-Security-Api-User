package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.ContactQuestResult;
import com.viettel.campaign.web.dto.ContactQuestResultDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactQuestResultRepository extends JpaRepository<ContactQuestResult, Long> {

    List<ContactQuestResultDTO> findByCompanySiteIdAndCampaignId(Long companySiteId, Long campaignId);

    List<ContactQuestResult> findByContactCustResultId(Long contactCustResultId);

    List<ContactQuestResult> findByCompanySiteIdAndCampaignIdAndCustomerId(Long companySiteId, Long campaignId, Long customerId);

    Integer deleteByScenarioQuestionId(Long questionId);
}
