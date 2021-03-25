package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.web.dto.*;
import com.viettel.campaign.web.dto.request_dto.CampaignRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignExecuteRepository {

    Page<CampaignDTO> searchCampaignExecute(CampaignRequestDTO campaignRequestDto, Pageable pageable);

    Page<ContactCustResultDTO> getInteractiveResult(CampaignRequestDTO dto, Pageable pageable, boolean isExport);

}
