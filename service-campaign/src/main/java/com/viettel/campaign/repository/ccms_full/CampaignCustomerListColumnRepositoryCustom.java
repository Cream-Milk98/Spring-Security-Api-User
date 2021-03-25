package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.web.dto.request_dto.CampaignCustomerListColumnRequestDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(DataSourceQualify.CCMS_FULL)
public interface CampaignCustomerListColumnRepositoryCustom {
    List<CampaignCustomerListColumnRequestDTO> getCustomerInfor(Long companySiteId, Long customerId, Long campaignId);
}
