package com.viettel.campaign.web.rest;

import com.viettel.campaign.service.CampaignCfgService;
import com.viettel.campaign.service.impl.ScenarioAnswerServiceImpl;
import com.viettel.campaign.utils.RedisUtil;
import com.viettel.campaign.web.dto.CampaignCfgDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.request_dto.CampaignCfgRequestDTO;
import com.viettel.econtact.filter.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/ipcc/completeCode")
@CrossOrigin(origins = "*")
public class CampaignCfgController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CampaignCfgController.class);

    @Autowired
    CampaignCfgService completeCodeService;

    @GetMapping("/findAll")
    @ResponseBody
    public ResponseEntity findAllCompleteCode(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("sort") String sort,@RequestParam("companySiteId") Long companySiteId) {
        ResultDTO result = completeCodeService.listAllCompleteCode(page, pageSize, sort,companySiteId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findCompleteCodeByName")
    @ResponseBody
    public ResponseEntity findAllCompleteCodeName(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("sort") String sort, @RequestParam("name") String name) {
        Map result = completeCodeService.listCompleteCodeByName(page, pageSize, sort, name);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/create")
    @ResponseBody
    public ResultDTO createCompleteCode(@RequestBody @Valid CampaignCfgDTO completeCodeDTO , HttpServletRequest httpServletRequest) {
        String xAuthToken = httpServletRequest.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);

        ResultDTO result = new ResultDTO();
        //LogUtil logUtil = new LogUtil();
        //logUtil.initKpiLog("createCust")
        try {
            //LOGGER.info("Returning createCustomer: start");
            result = completeCodeService.createCompleteCode(completeCodeDTO, userSession.getUserId());
            //LOGGER.info("Returning createCustomer:" + result.getErrorCode());
            //logUtil.endKpiLog(customerDTO, 0, result.getErrorCode(), result.getDetail(), CustomerController.class, customerDTO.getAgentProcess(), this.serverPort);
        } catch (Exception e) {
            result.setErrorCode("-1");
            LOGGER.error(e.getMessage());
            //logUtil.endKpiLog(customerDTO, 1, result.getErrorCode(), e.getMessage(), CustomerController.class, customerDTO.getAgentProcess(), this.serverPort);
        }
        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultDTO updateCompleteCode(@RequestBody @Valid CampaignCfgDTO completeCodeDTO
            ) {
        ResultDTO result = new ResultDTO();
        try {
            result = completeCodeService.updateCompleteCode(completeCodeDTO);

        } catch (Exception e) {
            result.setErrorCode("-1");
            LOGGER.error(e.getMessage());
            //logUtil.endKpiLog(customerDTO, 1, result.getErrorCode(), e.getMessage(), CustomerController.class, customerDTO.getAgentProcess(), this.serverPort);
        }
        return result;
    }
    @GetMapping("/edit")
    @ResponseBody
    public ResultDTO findCampaignCodeById(@RequestParam("campaignCompleteCodeId") Long campaignCompleteCodeId){
        ResultDTO resultDTO = new ResultDTO();
        try{
           resultDTO = completeCodeService.editCampaignCompleteCode(campaignCompleteCodeId);
        }catch (Exception e){
            resultDTO.setErrorCode("-1");
            LOGGER.error(e.getMessage());
        }
        return resultDTO;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultDTO deleteCompleteCode(@RequestBody @Valid CampaignCfgRequestDTO campaignCfgRequestDTO) {
        ResultDTO result = completeCodeService.deleteCompleteCode(campaignCfgRequestDTO);
        return result;
    }

    @PostMapping("/listDelete")
    @ResponseBody
    public ResponseEntity<ResultDTO> deleteLists(@RequestBody @Valid CampaignCfgRequestDTO campaignCfgRequestDTO){
        ResultDTO resultDTO = completeCodeService.deleteList(campaignCfgRequestDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/deleteById")
    @ResponseBody
    public  ResultDTO deleteById(@RequestBody @Valid CampaignCfgRequestDTO campaignCfgRequestDTO){
        ResultDTO resultDTO = completeCodeService.deleteById(campaignCfgRequestDTO);
        return  resultDTO;

    }
    @CrossOrigin(origins = "*")
    @PostMapping("/getMaxValue")
    @ResponseBody
    public ResultDTO findMaxValueCampaignType(@RequestBody CampaignCfgDTO completeCodeDTO){
         return completeCodeService.findMaxValueCampaignCompleteCode(completeCodeDTO);
    }

    @GetMapping("/getListStatus")
    @ResponseBody
    public ResponseEntity<ResultDTO> getListStatus(@RequestParam("completeValue") String completeValue, @RequestParam("completeType") Short completeType, @RequestParam("companySiteId") Long companySiteId) {
        ResultDTO result = completeCodeService.getListStatus(completeValue, completeType, companySiteId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/getListStatusWithoutType")
    @ResponseBody
    public ResponseEntity<ResultDTO> getListStatusWithoutType(@RequestParam("completeValue") String completeValue, @RequestParam("completeType") Short completeType, @RequestParam("companySiteId") Long companySiteId) {
        ResultDTO result = completeCodeService.getListStatusWithoutType(completeValue, completeType, companySiteId);
        return new ResponseEntity(result, HttpStatus.OK);
    }


}
