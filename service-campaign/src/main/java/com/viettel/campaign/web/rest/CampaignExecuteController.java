package com.viettel.campaign.web.rest;

import com.viettel.campaign.model.ccms_full.ContactCustResult;
import com.viettel.campaign.service.CampaignExecuteService;
import com.viettel.campaign.utils.DataUtil;
import com.viettel.campaign.utils.RedisUtil;
import com.viettel.campaign.web.dto.*;
import com.viettel.campaign.web.dto.request_dto.CampaignRequestDTO;
import com.viettel.campaign.web.dto.request_dto.UpdateInteractiveResultsRequestDTO;
import com.viettel.econtact.filter.UserSession;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/ipcc/campaign/execute")
@CrossOrigin(origins = "*")
public class CampaignExecuteController {

    private static final Logger logger = LoggerFactory.getLogger(CampaignExecuteController.class);

    @Autowired
    CampaignExecuteService campaignExecuteService;

    @PostMapping("/searchCampaignExecute")
    @ResponseBody
    public ResponseEntity<ResultDTO> searchCampaignExecute(@RequestBody CampaignRequestDTO requestDto) {
        ResultDTO result = campaignExecuteService.searchCampaignExecute(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/checkExecuteCampaign")
    @ResponseBody
    public ResponseEntity<ResultDTO> checkExecuteCampaign(@RequestBody CampaignRequestDTO requestDto) {
        ResultDTO result = campaignExecuteService.checkExecuteCampaign(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/getExecuteCampaign")
    @ResponseBody
    public ResponseEntity<ResultDTO> getExecuteCampaign(@RequestBody CampaignRequestDTO requestDto) {
        ResultDTO result = campaignExecuteService.getExecuteCampaign(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/getCustomer")
    @ResponseBody
    public ResponseEntity<ResultDTO> getCustomer(@RequestBody CampaignCustomerDTO requestDto) {
        logger.info("--- REQUEST GET CUSTOMER: campaignID: " + requestDto.getCampaignId() + " | agentID:" + requestDto.getAgentId());
        ResultDTO result = campaignExecuteService.getCustomer(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/getCustomerComplete")
    @ResponseBody
    public ResponseEntity<ResultDTO> getCustomerComplete(@RequestBody ReceiveCustLogDTO requestDto) {
        ResultDTO result = campaignExecuteService.getCustomerComplete(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/getCallLog")
    @ResponseBody
    public ResponseEntity<ResultDTO> getCallLog(@RequestBody ReceiveCustLogDTO requestDto) {
        ResultDTO result = campaignExecuteService.getCallLog(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/countRecallCustomer")
    @ResponseBody
    public ResponseEntity countRecallCustomer(@RequestParam("companySiteId") Long companySiteId, @RequestParam("agentId") Long agentId) {
        ResultDTO result = campaignExecuteService.countRecallCustomer(companySiteId, agentId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/getAgentLogout")
    @ResponseBody
    public ResponseEntity<ResultDTO> getAgentLogout(@RequestBody CampaignRequestDTO requestDto) {
        ResultDTO result = campaignExecuteService.getAgentLogout(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/getLogoutContactResult")
    @ResponseBody
    public ResponseEntity<ResultDTO> getLogoutContactResult(@RequestBody ReceiveCustLogDTO requestDto) {
        ResultDTO result = campaignExecuteService.getLogoutContactResult(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/searchInteractiveResult")
    @ResponseBody
    public ResponseEntity<ResultDTO> searchInteractiveResult(@RequestBody CampaignRequestDTO dto) throws Exception {
        ResultDTO result = campaignExecuteService.searchInteractiveResult(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/exportInteractiveResult")
    @ResponseBody
    public ResponseEntity<?> exportInteractiveResult(HttpServletResponse response, @RequestBody CampaignRequestDTO dto) {
        XSSFWorkbook workbook = null;
        byte[] contentReturn = null;
        try {
            String fileName = "report_campaign_exec_interactive_result.xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            OutputStream outputStream;
            workbook = campaignExecuteService.exportInteractiveResult(dto);
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            contentReturn = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<byte[]>(null, null, HttpStatus.BAD_REQUEST);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        return new ResponseEntity<byte[]>(contentReturn, headers, HttpStatus.OK);
    }

    @GetMapping("/getCustomerInfor")
    @ResponseBody
    public ResponseEntity getCustomerInfor(@RequestParam("companySiteId") Long companySiteId, @RequestParam("customerId") Long customerId,
                                           @RequestParam("campaignId") Long campaignId, @RequestParam("language") String language) {
        ResultDTO result = campaignExecuteService.getCustomerInfor(companySiteId, customerId, campaignId, language);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/updateContactCustResult")
    @ResponseBody
    public ResponseEntity<ResultDTO> updateContactCustResult(@RequestBody ContactCustResultDTO requestDto, HttpServletRequest request) {
        logger.info("--- Rest request to updateContactCustResult, CALL [Call status: " + requestDto.getEventCall()
                + " | Call id:" + requestDto.getCallId() + " | Agent id:" + requestDto.getAgentId() + " | Campaign id:" + requestDto.getCampaignId()
                + "] --> CUSTOMER [Customer id:" + requestDto.getCustomerId() + " | Phone number:" + requestDto.getPhoneNumber() + " | ReceiveCustLogId:" + requestDto.getReceiveCustLogId() + "]");
        String xAuthToken = request.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);
        ResultDTO result = new ResultDTO();

        if (!DataUtil.isNullOrEmpty(requestDto.getEventCall())) {
            switch (requestDto.getEventCall()) {
//                case "call":
//                    result = campaignExecuteService.updateContactCustResultAtCall(requestDto, userSession);
//                    break;
                case "ringing":
                    result = campaignExecuteService.updateContactCustResultAtCall(requestDto, userSession);
                    break;
                case "ended":
                    result = campaignExecuteService.updateContactCustResultAtEnded(requestDto, userSession);
                    break;
                case "save":
                    result = campaignExecuteService.updateContactCustResultAtSave(requestDto, userSession);
                    break;
                case "draftAtTen":
                    result = campaignExecuteService.updateContactCustResultAtTen(requestDto, userSession);
                    break;
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/deleteContactCustResult")
    @ResponseBody
    public ResponseEntity<ResultDTO> deleteContactCustResult(@RequestBody ContactCustResultDTO requestDto, HttpServletRequest request) {
        ResultDTO result = campaignExecuteService.deleteContactCustResult(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/doSaveContactResult")
    @ResponseBody
    public ResponseEntity<ResultDTO> doSaveContactResult(@RequestBody ContactCustResultDTO requestDto, HttpServletRequest request) {
        String xAuthToken = request.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);
        ResultDTO result = campaignExecuteService.doSaveContacResult(requestDto, userSession);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/recallCustomer")
    @ResponseBody
    public ResponseEntity<ResultDTO> recallCustomer(@RequestBody ContactCustResultDTO requestDto) {
        ResultDTO result = campaignExecuteService.recallCustomer(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getCustomerRecall")
    @ResponseBody
    public ResponseEntity getCustomerRecall(@RequestParam("campaignId") Long campaignId, @RequestParam("customerId") Long customerId) {
        ResultDTO result = campaignExecuteService.getCustomerRecall(campaignId, customerId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/createListContactQuestResult")
    @ResponseBody
    public ResponseEntity<ResultDTO> createListContactQuestResult(@RequestBody List<ContactQuestResultDTO> dtoList) {
        ResultDTO result = null;
        result = campaignExecuteService.createListContactQuestResult(dtoList);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/updateListContactQuestResult")
    @ResponseBody
    public ResponseEntity<ResultDTO> updateListContactQuestResult(@RequestBody ContactQuestResultDTO dto) {
        ResultDTO result = null;
        result = campaignExecuteService.updateListContactQuestResult(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getDialModeAtCurrent")
    @ResponseBody
    public ResponseEntity getDialModeAtCurrent(@RequestParam("companySiteId") Long companySiteId, @RequestParam("campaignId") Long campaignId,
                                               @RequestParam("timezone") Integer timezone) {
        String result = campaignExecuteService.getDialModeAtCurrent(companySiteId, campaignId, timezone);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getContactCustResultById")
    @ResponseBody
    public ResponseEntity getContactCustResultById(@RequestParam("contactCustResultId") Long contactCustResultId, @RequestParam("timeZone") Integer timeZone) {
        ResultDTO result = campaignExecuteService.getContactCustResultById(contactCustResultId, timeZone);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/draftAtTen")
    @ResponseBody
    public ResponseEntity<ResultDTO> draftAtTen(@RequestBody ContactCustResultDTO requestDto, HttpServletRequest request) {
        String xAuthToken = request.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);
        ResultDTO result = campaignExecuteService.draftAtTen(requestDto, userSession);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/updateCampaignCustomer")
    @ResponseBody
    public ResponseEntity<ResultDTO> updateCampaignCustomer(@RequestBody CampaignCustomerDTO requestDto) {
        ResultDTO result = campaignExecuteService.updateCampaignCustomer(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/updateSurveyStatus")
    @ResponseBody
    public ResponseEntity<ResultDTO> updateSurveyStatus(@RequestBody CustomerTimeDTO requestDto, HttpServletRequest request) {
        String xAuthToken = request.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);
        ResultDTO result = campaignExecuteService.updateSurveyStatus(requestDto, userSession);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //AnhVD created: UPDATE INTERACTIVE RESULTS FOR CASE EDIT CONTACT INTERACTIVE RESULT
    @RequestMapping(value = "/update-interactive-results", method = RequestMethod.POST)
    public ResponseEntity<ResultDTO> updateInteractiveResults(@RequestBody ContactCustResultDTO updateObj, HttpServletRequest request) {
        String xAuthToken = request.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);
        updateObj.setCompanySiteId(userSession.getCompanySiteId());
        updateObj.setAgentId(userSession.getUserId());
        ResultDTO resultDTO = campaignExecuteService.updateInteractiveResults(updateObj, userSession);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/doInitForRecall", method = RequestMethod.POST)
    public ResponseEntity<ResultDTO> doInitForRecall(@RequestBody ContactCustResultDTO dto, HttpServletRequest request) {
        String xAuthToken = request.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);
        dto.setCompanySiteId(userSession.getCompanySiteId());
        dto.setAgentId(userSession.getUserId());
        ResultDTO resultDTO = campaignExecuteService.doInitForRecall(dto.getReceiveCustLogId(), userSession);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/check-interrupt-campaigns", method = RequestMethod.POST)
    public ResponseEntity<ResultDTO> checkInterruptCampaigns(@RequestBody Long campaignId) {
        ResultDTO resultDTO = campaignExecuteService.checkInterruptCampaigns(campaignId);
        return new ResponseEntity<ResultDTO>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/get-contact-cus-result-id", method = RequestMethod.GET)
    public ResponseEntity<ResultDTO> getContactCusResultId(@RequestParam("customerId") Long customerId, @RequestParam("campaignId") Long campaignId, HttpServletRequest request) {
        String xAuthToken = request.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);
        ResultDTO resultDTO = campaignExecuteService.getContactCusResultId(customerId, campaignId, userSession.getCompanySiteId());
        return new ResponseEntity<ResultDTO>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/check-recall-duration", method = RequestMethod.GET)
    public ResponseEntity<ResultDTO> checkRecallDuration(@RequestParam("customerId") Long customerId, @RequestParam("campaignId") Long campaignId, HttpServletRequest request) {
        String xAuthToken = request.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);
        ResultDTO resultDTO = campaignExecuteService.checkRecallDuration(customerId, campaignId, userSession.getCompanySiteId());
        return new ResponseEntity<ResultDTO>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/update-contact-cust-after-callend", method = RequestMethod.GET)
    public ResponseEntity<ResultDTO> updateContactCustAfterCallEnd(@RequestParam("customerId") Long customerId, @RequestParam("campaignId") Long campaignId, HttpServletRequest request) {
        String xAuthToken = request.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);
        ResultDTO resultDTO = campaignExecuteService.checkRecallDuration(customerId, campaignId, userSession.getCompanySiteId());
        return new ResponseEntity<ResultDTO>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/update-after-call-end", method = RequestMethod.POST)
    public ResponseEntity<ResultDTO> updateCCRAfterCallEnd(@RequestBody ContactCustResultDTO contactCustResult, HttpServletRequest request) {
        logger.info("--- Rest request to updateCCRAfterCallEnd:{} " + contactCustResult.toString());
        String xAuthToken = request.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);
        ResultDTO resultDTO = campaignExecuteService.updateCCRAfterCallEnd(contactCustResult, userSession);
        logger.info("Rest response to updateCCRAfterCallEnd: " + resultDTO.getDescription());
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/check-agent-logout", method = RequestMethod.GET)
    public ResponseEntity<ResultDTO> checkAgentLogout(HttpServletRequest request) {
        String xAuthToken = request.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);
        logger.info("--- Rest request to checkAgentLogout:{} " + userSession.getUserId());
        ResultDTO resultDTO = campaignExecuteService.checkAgentLogout(userSession.getUserId().toString());
        logger.info("Rest response of checkAgentLogout:{} " + resultDTO.getDescription());
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}
