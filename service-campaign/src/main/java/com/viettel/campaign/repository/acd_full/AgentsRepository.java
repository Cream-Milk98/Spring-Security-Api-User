package com.viettel.campaign.repository.acd_full;

import com.viettel.campaign.model.acd_full.Agents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AgentsRepository extends JpaRepository<Agents, String> {

    @Transactional(readOnly = true)
    Agents findByAgentId(String agentId);

    @Modifying
    @Query(value = "UPDATE Agents SET campaignSystemStatus = :campaignSystemStatus WHERE agentId = :agentId")
    void updateAgentLogoutFromCampaign(@Param("agentId") String agentId, @Param("campaignSystemStatus") String campaignSystemStatus);

    Agents findByCampaignSystemStatusAndAgentId(String campaignSystemStatus, String agentId);
}
