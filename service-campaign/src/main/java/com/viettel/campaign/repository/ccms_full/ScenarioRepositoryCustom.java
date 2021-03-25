package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.web.dto.InteractiveResultDTO;

import java.util.List;

/**
 * @author anhvd_itsol
 */

public interface ScenarioRepositoryCustom {
    List<InteractiveResultDTO> getInteractiveResult(Long contactCusResultId, Long customerId, Long campaignId);
}
