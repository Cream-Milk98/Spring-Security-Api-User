package com.viettel.campaign.web.rest;

import com.viettel.campaign.service.AgentsService;
import com.viettel.campaign.utils.RedisUtil;
import com.viettel.campaign.web.dto.CampaignAgentDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.request_dto.AgentRequestDTO;
import com.viettel.campaign.web.dto.request_dto.CampaignAgentRequestDTO;
import com.viettel.econtact.filter.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ipcc/agents")
@CrossOrigin(origins = "*")
public class AgentsController {

    private static final Logger logger = LoggerFactory.getLogger(AgentsController.class);

    @Autowired
    AgentsService agentsService;

    @GetMapping("/getAgentsById")
    @ResponseBody
    public ResponseEntity<ResultDTO> getAgentsById(@RequestParam("agentId") String agentId) {
        ResultDTO result = agentsService.getAgentsByAgentId(agentId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/createCampaignAgent")
    @ResponseBody
    public ResultDTO createCampaignAgent(@RequestBody @Valid CampaignAgentDTO campaignAgentDTO) {
        ResultDTO result = agentsService.createCampaignAgent(campaignAgentDTO);
        return result;
    }

    @PostMapping("/deleteCampaignAgent")
    @ResponseBody
    public ResultDTO deleteCampaignAgentById(@RequestBody @Valid List<Long> campaignAgentId) {
        ResultDTO result = agentsService.deleteCampaignAgentById(campaignAgentId);
        return result;
    }

    @PostMapping("/searchCampaignAgent")
    @ResponseBody
    public ResponseEntity<ResultDTO> searchCampaignAgent(@RequestBody AgentRequestDTO agentRequestDTO) {
        ResultDTO result = agentsService.searchCampaignAgentByName(agentRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/searchCampaignAgentSelect")
    @ResponseBody
    public ResponseEntity<ResultDTO> searchCampaignAgentSelect(@RequestBody AgentRequestDTO agentRequestDTO) {
        ResultDTO result = agentsService.searchCampaignAgentSelectByName(agentRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/createMultipleCampaignAgent")
    @ResponseBody
    public ResultDTO createMultipleCampaignAgent(@RequestBody @Valid CampaignAgentRequestDTO campaignAgentRequestDTO) {
        ResultDTO result = agentsService.createMultipleCampaignAgent(campaignAgentRequestDTO);
        return result;
    }

    @PostMapping("/updateAgent")
    @ResponseBody
    public ResultDTO updateAgent(@RequestBody @Valid CampaignAgentDTO requestDTO) {
        ResultDTO result = agentsService.updateAgent(requestDTO);
        return result;
    }
}
