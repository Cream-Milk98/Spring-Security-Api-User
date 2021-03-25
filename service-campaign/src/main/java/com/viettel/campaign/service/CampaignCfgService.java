package com.viettel.campaign.service;

import com.viettel.campaign.web.dto.CampaignCfgDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.request_dto.CampaignCfgRequestDTO;

import java.util.Map;

public interface CampaignCfgService {

    ResultDTO listAllCompleteCode(int page, int pageSize, String sort,Long companySiteId);

    Map listCompleteCodeByName(int page, int pageSize, String sort, String name);

    ResultDTO createCompleteCode(CampaignCfgDTO completeCodeDTO, Long userId);

    ResultDTO updateCompleteCode(CampaignCfgDTO completeCodeDTO);

    ResultDTO deleteCompleteCode(CampaignCfgRequestDTO completeCodeDTO);

    ResultDTO deleteList(CampaignCfgRequestDTO campaignCfgRequestDTO);

    ResultDTO deleteById(CampaignCfgRequestDTO campaignCfgRequestDTO);

    ResultDTO findMaxValueCampaignCompleteCode(CampaignCfgDTO completeCodeDTO);

    ResultDTO getListStatus(String completeValue, Short completeType, Long companySiteId);

    ResultDTO getListStatusWithoutType(String completeValue, Short completeType, Long companySiteId);

    ResultDTO editCampaignCompleteCode (Long campaignCompleteCodeId);

}
