package com.viettel.campaign.service;

import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.ScenarioAnswerDTO;

/**
 * @author anhvd_itsol
 */

public interface ScenarioAnswerService {
    ResultDTO findByScenarioQuestionCompany(Long scenarioQuestionId, Long companySiteId);

    Long getMaxAnswerOrderId(Long scenarioQuestionId, Long companySiteId);

    ResultDTO delete(ScenarioAnswerDTO scenarioAnswerDTO);

    Integer countDuplicateScenarioCode(String code, Long scenarioQuestionId, Long scenarioAnswerId);
}
