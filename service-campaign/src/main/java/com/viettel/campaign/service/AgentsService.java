package com.viettel.campaign.service;

import com.viettel.campaign.web.dto.CampaignAgentDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.request_dto.AgentRequestDTO;
import com.viettel.campaign.web.dto.request_dto.CampaignAgentRequestDTO;

import java.util.List;

public interface AgentsService {
    ResultDTO getAgentsByAgentId(String agentId);

    ResultDTO createCampaignAgent(CampaignAgentDTO campaignAgentDTO);

    ResultDTO deleteCampaignAgentById(List<Long> campaignAgentId);

    ResultDTO searchCampaignAgentByName(AgentRequestDTO agentRequestDTO);

    ResultDTO searchCampaignAgentSelectByName(AgentRequestDTO agentRequestDTO);

    ResultDTO createMultipleCampaignAgent(CampaignAgentRequestDTO campaignAgentRequestDTO);

    ResultDTO updateAgent(CampaignAgentDTO campaignAgentDTO);

}
