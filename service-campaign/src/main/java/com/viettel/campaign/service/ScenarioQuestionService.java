package com.viettel.campaign.service;

import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.ScenarioQuestionDTO;

/**
 * @author anhvd_itsol
 */

public interface ScenarioQuestionService {
    ResultDTO findByScenarioIdAndCampaignIdAndCompanySiteId(Long scenarioId, Long campaignId, Long companySiteId);

    ResultDTO add(ScenarioQuestionDTO scenarioQuestionDTO);

    Long getMaxOrderId(Long scenarioId, Long campaignId, Long companySiteId);

    Integer countDuplicateQuestionCode(ScenarioQuestionDTO questionDTO);

    ResultDTO delete(ScenarioQuestionDTO scenarioQuestionDTO);

    ResultDTO update(ScenarioQuestionDTO scenarioQuestionDTO);

    Integer countDuplicateOrderIndex(ScenarioQuestionDTO questionDTO);

}
