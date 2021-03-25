package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.web.dto.ScenarioQuestionDTO;

import java.util.List;

/**
 * @author anhvd_itsol
 */

public interface ScenarioQuestionRepositoryCustom {
    Integer countDuplicateQuestionCode(ScenarioQuestionDTO questionDTO);

    Integer countDuplicateOrderIndex(ScenarioQuestionDTO questionDTO);

    List<ScenarioQuestionDTO> getOldQuestionsData(Long campaignId, Long customerId, Long companySiteId);

    List<ScenarioQuestionDTO> getListQuestions(Long companySiteId, Long scenarioId, Short status);
}
