package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.web.dto.CampaignCustomerDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CampaignCustomerRepositoryCustom {

    List<CampaignCustomerDTO> getDataCampaignCustomer(CampaignCustomerDTO dto, String expression, String dungsai);
}
