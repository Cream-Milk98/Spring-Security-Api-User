package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long>, CampaignRepositoryCustom {

    List<Campaign> findAllByCompanySiteId(Long companyId);

    List<Campaign> findCampaignByCompanySiteIdAndStartTimeIsLessThanEqualAndStatusIn(Long siteId, Date startTime, List<Long> status);

    List<Campaign> findCampaignByCompanySiteIdAndEndTimeIsLessThanEqualAndStatusIn(Long siteId, Date endTime, List<Long> status);

//    @Query("SELECT COUNT(c.campaignId) " +
//            " FROM Campaign c JOIN CampaignCustomer cc ON c.campaignId = cc.campaignId " +
//            " WHERE cc.companySiteId = :pCompanySiteId " +
//            " AND cc.status = 1 " +
//            " AND cc.recallTime <= sysdate " +
//            " AND cc.agentId = :pAgentId")
//    Long countRecallCustomer(@Param("pCompanySiteId") Long pCompanySiteId, @Param("pAgentId") Long pAgentId);

    Campaign findByCampaignId(Long campaignId);

    Campaign findCampaignByCampaignIdAndCompanySiteId(Long campaignId, Long companySiteId);

    Page<Campaign> findByCompanySiteIdAndStatusNotOrderByCreateTimeDesc(Long companySiteId, Long status, Pageable pageable);

    Page<Campaign> findByCompanySiteIdAndStatusNotAndCampaignCodeContainingOrderByCreateTimeDesc(Long companySiteId, Long status, String campaignCode, Pageable pageable);

    @Query(value = "SELECT COUNT(1) FROM CAMPAIGN WHERE CAMPAIGN_ID = :pId AND STATUS <> 2", nativeQuery = true)
    Integer checkInterruptCampaigns(@Param("pId") Long pId);

}
