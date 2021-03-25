package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.CampaignCustomerList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignCustomerListRepository extends JpaRepository<CampaignCustomerList, Long> {

    @Query("select count (c.campaignId) from CampaignCustomerList c where c.customerListId=:customerListId")
    Long campaignCount(@Param("customerListId") long customerListId);

    @Query("select count (c.campaignId) from CampaignCustomerList c where c.customerListId in (:ids)")
    Long campaignIdsCount(@Param("ids") List<Long> ids);

    void deleteCampaignCustomerListByCampaignIdAndCustomerListIdAndCompanySiteId(Long campaignId, Long customerListId, Long companySiteId);
}
