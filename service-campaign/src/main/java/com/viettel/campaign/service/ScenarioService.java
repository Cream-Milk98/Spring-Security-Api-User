package com.viettel.campaign.service;

import com.viettel.campaign.model.ccms_full.ContactQuestResult;
import com.viettel.campaign.model.ccms_full.Scenario;
import com.viettel.campaign.web.dto.*;
import com.viettel.econtact.filter.UserSession;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author anhvd_itsol
 */

public interface ScenarioService {
    Scenario findScenarioByCampaignIdAndCompanySiteId(Long campaignId, Long companySiteId);

    ResultDTO update(ScenarioDTO scenario);

    ResultDTO sortQuestionAndAnswer(ScenarioDTO scenarioDTO);

    Integer countDuplicateScenarioCode(Long companySiteId, String code, Long scenarioId);

    ResultDTO saveContacQuestResult(ContactQuestResultDTO dto);

    XSSFWorkbook buildTemplate(String language) throws IOException;

    Map<String, Object> readAndValidateScenario(String path, Long scenarioId, Long campaignId, Long companySiteId, String language) throws IOException;

    List<ContactQuestResult> getContactQuestResult(Long companySiteId, Long campaignId, Long customerId);

    ScenarioDTO getScenarioData(Long campaignId, Long companySiteId);

    ScenarioDTO getOldScenarioData(Long campaignId, Long customerId, Long companySiteId, Long contactCusResultId);
}
