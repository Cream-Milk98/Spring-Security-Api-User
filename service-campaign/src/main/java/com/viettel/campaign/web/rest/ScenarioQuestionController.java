package com.viettel.campaign.web.rest;

import com.viettel.campaign.service.ScenarioQuestionService;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.ScenarioQuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author anhvd_itsol
 */

@RestController
@RequestMapping("/ipcc/campaign/scenario-question")
@CrossOrigin("*")
public class ScenarioQuestionController {
    @Autowired
    ScenarioQuestionService scenarioQuestionService;


    @RequestMapping(value = "/find-by-scenarioId-campaignId-companySiteId", method = RequestMethod.GET)
    public ResponseEntity<ResultDTO> findByScenarioIdAndCampaignIdAndCompanySiteId(Long scenarioId, Long campaignId, Long companySiteId){
        ResultDTO resultDTO = scenarioQuestionService.findByScenarioIdAndCampaignIdAndCompanySiteId(scenarioId, campaignId, companySiteId);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ResultDTO> add(@RequestBody ScenarioQuestionDTO scenarioQuestionDTO) {
        ResultDTO resultDTO = scenarioQuestionService.add(scenarioQuestionDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/get-max-orderId", method = RequestMethod.GET)
    public Long getMaxOrderId(@RequestParam Long scenarioId, @RequestParam Long campaignId, @RequestParam Long companySiteId)
    {
        return scenarioQuestionService.getMaxOrderId(scenarioId, campaignId, companySiteId);
    }

    @RequestMapping(value = "/count-duplicate-question-code", method = RequestMethod.POST)
    public Integer countDuplicateQuestionCode(@RequestBody ScenarioQuestionDTO questionDTO) {
        return scenarioQuestionService.countDuplicateQuestionCode(questionDTO);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<ResultDTO> deleteQuestion(@RequestBody ScenarioQuestionDTO scenarioQuestionDTO) {
        ResultDTO resultDTO = scenarioQuestionService.delete(scenarioQuestionDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value="/update", method = RequestMethod.PUT)
    public ResponseEntity<ResultDTO> update(@RequestBody ScenarioQuestionDTO scenarioQuestionDTO) {
        ResultDTO resultDTO = scenarioQuestionService.update(scenarioQuestionDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/count-duplicate-order-index", method = RequestMethod.POST)
    public Integer countDuplicateOrderIndex(@RequestBody ScenarioQuestionDTO questionDTO) {
        return scenarioQuestionService.countDuplicateOrderIndex(questionDTO);
    }

}
