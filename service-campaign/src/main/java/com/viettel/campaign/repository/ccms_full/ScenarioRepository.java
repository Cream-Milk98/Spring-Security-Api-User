package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

/**
 * @author anhvd_itsol
 */

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    Scenario findScenarioByCampaignIdAndCompanySiteId(Long campaignId, Long companySiteId);

    @Query(value = "SELECT COUNT(1) FROM Scenario WHERE 1 = 1 AND companySiteId = :companySiteId AND code = :code AND scenarioId <> :scenarioId")
    Integer countDuplicateScenarioCode(@Param("companySiteId") Long companySiteId, @Param("code") String code, @Param("scenarioId") Long scenarioId);
}
