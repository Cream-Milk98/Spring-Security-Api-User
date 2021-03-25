package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.CampaignCfg;
import com.viettel.campaign.web.dto.CampaignCfgDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampaignCfgRepositoryCustom {

    Page<CampaignCfgDTO> listAllCompleteCode(Long companySiteId, Pageable pageable);

    Integer findMaxValueCampaignCompleteCode(CampaignCfgDTO completeCodeDTO);

    List<CampaignCfgDTO> editCCC(Long campaignCompleteCodeId);
}
