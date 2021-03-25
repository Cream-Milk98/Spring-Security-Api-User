package com.viettel.campaign.mapper;

import com.viettel.campaign.model.ccms_full.CampaignAgent;
import com.viettel.campaign.web.dto.CampaignAgentDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class CampaignAgentMapper extends BaseMapper<CampaignAgent, CampaignAgentDTO> {

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CampaignAgentDTO toDtoBean(CampaignAgent campaignAgent) {
        return modelMapper.map(campaignAgent, CampaignAgentDTO.class);
    }

    @Override
    public CampaignAgent toPersistenceBean(CampaignAgentDTO dtoBean) {
        return modelMapper.map(dtoBean, CampaignAgent.class);
    }
}
