package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.CampaignCustomerListColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CampaignCustomerListColumnRepository extends JpaRepository<CampaignCustomerListColumn, Long>, CampaignCustomerListColumnRepositoryCustom {

    List<CampaignCustomerListColumn> findByCampaignIdAndCompanySiteId(Long campaignId, Long companaySiteId);

    CampaignCustomerListColumn findByCampaignCusListColId(Long campaignCusListColId);
}
