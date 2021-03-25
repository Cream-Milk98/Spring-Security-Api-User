package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.web.dto.ScenarioAnswerDTO;

import java.util.List;

/**
 * @author anhvd_itsol
 */

public interface ScenarioAnswerRepositoryCustom {
    List<ScenarioAnswerDTO> getOldAnswersData(Long questionId, Long customerId, Long contactCusResultId);
}
