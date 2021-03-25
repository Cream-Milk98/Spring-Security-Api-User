package com.viettel.campaign.web.rest;

import com.viettel.campaign.service.ScenarioAnswerService;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.ScenarioAnswerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author anhvd_itsol
 */

@RestController
@RequestMapping("/ipcc/campaign/scenario-answer")
@CrossOrigin
public class ScenarioAnswerController {
    @Autowired
    ScenarioAnswerService scenarioAnswerService;

    @RequestMapping(value = "/findByScenarioQuestionCompany", method= RequestMethod.GET)
    public ResponseEntity<ResultDTO> findByScenarioQuestionCampaignCompany(@RequestParam Long scenarioQuestionId,
                                                                           @RequestParam Long companySiteId) {
        ResultDTO resultDTO = scenarioAnswerService.findByScenarioQuestionCompany(scenarioQuestionId, companySiteId);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/get-max-orderId", method = RequestMethod.GET)
    public Long getMaxOrderId(@RequestParam Long scenarioQuestionId, @RequestParam Long companySiteId)
    {
        return scenarioAnswerService.getMaxAnswerOrderId(scenarioQuestionId, companySiteId);
    }

    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public ResponseEntity<ResultDTO> deleteQuestion(@RequestBody ScenarioAnswerDTO answerDTO) {
        ResultDTO resultDTO = scenarioAnswerService.delete(answerDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/count-duplicate-code", method = RequestMethod.GET)
    public ResponseEntity<Integer> countDuplicateCode(@RequestParam String code, @RequestParam Long scenarioQuestionId, @RequestParam Long scenarioAnswerId) {
        Integer count = scenarioAnswerService.countDuplicateScenarioCode(code, scenarioQuestionId, scenarioAnswerId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
