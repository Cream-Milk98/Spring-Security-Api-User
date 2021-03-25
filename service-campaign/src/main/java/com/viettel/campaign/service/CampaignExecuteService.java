package com.viettel.campaign.service;

import com.viettel.campaign.model.ccms_full.ContactCustResult;
import com.viettel.campaign.web.dto.*;
import com.viettel.campaign.web.dto.request_dto.CampaignRequestDTO;
import com.viettel.campaign.web.dto.request_dto.UpdateInteractiveResultsRequestDTO;
import com.viettel.econtact.filter.UserSession;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

public interface CampaignExecuteService {
    //<editor-fold: hungtt>
    ResultDTO getComboBoxStatus(String companySiteId, String completeType);

    ResultDTO getComboCampaignType(String companySiteId);

    ResultDTO searchInteractiveResult(CampaignRequestDTO dto);

    XSSFWorkbook exportInteractiveResult(CampaignRequestDTO dto) throws IOException;

    ResultDTO searchCampaignExecute(CampaignRequestDTO requestDto);

    ResultDTO checkExecuteCampaign(CampaignRequestDTO requestDto);

    ResultDTO getExecuteCampaign(CampaignRequestDTO requestDto);

    ResultDTO getCustomer(CampaignCustomerDTO dto);

    ResultDTO updateCustomerResult(ContactCustResultDTO dto);

    ResultDTO getCustomerComplete(ReceiveCustLogDTO dto);

    ResultDTO getCallLog(ReceiveCustLogDTO dto);

    ResultDTO getAgentLogout(CampaignRequestDTO dto);

    ResultDTO getLogoutContactResult(ReceiveCustLogDTO dto);

    ResultDTO deleteContactCustResult(ContactCustResultDTO dto);

    ResultDTO updateContactCustResultAtCall(ContactCustResultDTO dto, UserSession userSession);

    ResultDTO updateContactCustResultAtRinging(ContactCustResultDTO dto, UserSession userSession);

    ResultDTO updateContactCustResultAtEnded(ContactCustResultDTO dto, UserSession userSession);

    ResultDTO updateContactCustResultAtSave(ContactCustResultDTO dto, UserSession userSession);

    ResultDTO updateContactCustResultAtTen(ContactCustResultDTO dto, UserSession userSession);

    ResultDTO updateContactCustResult(ContactCustResultDTO dto, UserSession userSession);

    ResultDTO doSaveContacResult(ContactCustResultDTO dto, UserSession userSession);

    ResultDTO draftAtTen(ContactCustResultDTO dto, UserSession userSession);

    ResultDTO recallCustomer(ContactCustResultDTO dto);

    ResultDTO countRecallCustomer(Long companySiteId, Long agentId);

    ResultDTO getCustomerRecall(Long campaignId, Long customerId);

    ResultDTO getCustomerInfor(Long companySiteId, Long customerId, Long campaignId, String language);

    ResultDTO createListContactQuestResult(List<ContactQuestResultDTO> dtoList);

    ResultDTO updateListContactQuestResult(ContactQuestResultDTO dto);

    String getDialModeAtCurrent(Long companySiteId, Long campaignId, Integer timeZone);

    ResultDTO getContactCustResultById(Long contactCustResultId, Integer timeZone);

    ResultDTO updateCampaignCustomer(CampaignCustomerDTO dto);

    ResultDTO updateSurveyStatus(CustomerTimeDTO dto, UserSession userSession);

    ResultDTO updateSurveyStatusInteractive(CustomerTimeDTO dto, UserSession userSession);

    ResultDTO updateInteractiveResults(ContactCustResultDTO updateObj, UserSession userSession);

    ResultDTO doInitForRecall(Long receiveCustLogId, UserSession userSession);

    ResultDTO checkInterruptCampaigns(Long campaignId);

    ResultDTO getContactCusResultId(Long customerId, Long campaignId, Long companySiteId);

    ResultDTO checkRecallDuration(Long customerId, Long campaignId, Long companySiteId);

    ResultDTO updateCCRAfterCallEnd(ContactCustResultDTO contactCustResultDTO, UserSession userSession);

    ResultDTO updateCCRToStatus0(Long receiveCustLogId , UserSession userSession);

    ResultDTO checkAgentLogout(String agentId);
}
