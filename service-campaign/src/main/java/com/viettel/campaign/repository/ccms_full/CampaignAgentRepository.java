package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.CampaignAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignAgentRepository extends JpaRepository<CampaignAgent, Long> {

    @Query(value = "SELECT campaign_agent_seq.nextval FROM DUAL", nativeQuery = true)
    Long getNextSeqId();

    CampaignAgent findByCampaignIdAndAgentId(Long campaignId, Long agentId);

    List<CampaignAgent> findByAgentId(Long agentId);

    List<CampaignAgent> findByAgentIdAndStatus(Long agentId, Integer status);

    CampaignAgent findByCampaignIdAndCompanySiteIdAndAgentId(Long campaignId, Long companySiteId, Long agentId);

//    @Modifying
//    @Query("delete from CampaignAgent c where c.campaignAgentId in (:p_campaign_agent_id)")
//    int deleteCampaignAgent(@Param("p_campaign_agent_id") List<Long> p_campaign_agent_id);

//    //quangdn
    void deleteByCampaignIdAndAgentIdIn(Long campaignId,List<Long> campaignAgentId);
}
