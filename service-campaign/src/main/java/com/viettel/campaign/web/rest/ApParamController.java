package com.viettel.campaign.web.rest;

import com.viettel.campaign.model.ccms_full.ApParam;
import com.viettel.campaign.service.ApParamService;
import com.viettel.campaign.service.CampaignExecuteService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ipcc/apParam")
@CrossOrigin(origins = "*")
public class ApParamController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ApParamController.class);


    private static final String CONNECT_STATUS = "CONNECT_STATUS";
    private static final String CONNECT_STATUS_TYPE = "1";
    private static final String SURVEY_STATUS = "SURVEY_STATUS";
    private static final String SURVEY_STATUS_TYPE = "2";
    private static final String CAMPAIGN_TYPE = "CAMPAIGN_TYPE";

    @Autowired(required = true)
    ApParamService apParamService;

    @Autowired
    CampaignExecuteService campaignExecuteService;

    @GetMapping(path = "/findAlls")
    @ResponseBody
    public ResponseEntity listAllCustomer(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("sort") String sort) {
        Iterable<ApParam> listCustomer = apParamService.getAllParams(page, pageSize, sort);
        if (listCustomer == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(listCustomer.iterator(), HttpStatus.OK);
    }

    @PostMapping(path = "/findByName")
    @ResponseBody
    public ResponseEntity searchParamByName(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("sort") String sort, @RequestParam(name = "parName") String parName) {
        List<ApParam> lst = apParamService.getParamByName(page, pageSize, sort, parName);
        if (lst.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity(lst, HttpStatus.OK);
    }

    @RequestMapping(path = "/findParamByParType", method = RequestMethod.GET)
    public ResponseEntity findParamByParType(@RequestParam String parType) {
        return new ResponseEntity<>(apParamService.findParamByParType(parType), HttpStatus.OK);
    }

    @GetMapping(path ="/findAllParam")
    @ResponseBody
    public ResponseEntity findAllParam() {
        List<ApParam> findAll = apParamService.findAllParam();
        if(findAll.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(findAll, HttpStatus.OK);
    }

    //<editor-fold: hungtt>
    @GetMapping("/getAllDataComboBox")
    @ResponseBody
    public ResponseEntity getComboBox(@RequestParam("parType") String parType, @RequestParam("companySiteId") String companySiteId) {
        if (CONNECT_STATUS.equals(parType)) {
            return new ResponseEntity<>(campaignExecuteService.getComboBoxStatus(companySiteId, CONNECT_STATUS_TYPE), HttpStatus.OK);
        } else if (SURVEY_STATUS.equals(parType)){
            return new ResponseEntity<>(campaignExecuteService.getComboBoxStatus(companySiteId, SURVEY_STATUS_TYPE), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(campaignExecuteService.getComboCampaignType(companySiteId), HttpStatus.OK);
        }
    }

    //</editor-fold: hungtt>
}

