package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.ScenarioQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author anhvd_itsol
 */

@Repository
public interface ScenarioQuestionRepository extends JpaRepository<ScenarioQuestion, Long> {

    List<ScenarioQuestion> findScenarioQuestionsByScenarioIdAndCampaignIdAndCompanySiteIdAndStatusOrderByOrderIndex(Long scenarioId, Long campaignId, Long companySiteId, Short status);

    @Query(value="SELECT MAX(orderIndex) FROM ScenarioQuestion WHERE scenarioId = :scenarioId AND campaignId = :campaignId AND companySiteId = :companySiteId AND status = 1")
    Long getMaxOrderId(@Param("scenarioId") Long scenarioId, @Param("campaignId") Long campaignId, @Param("companySiteId") Long companySiteId);

    @Query(value = "SELECT COUNT(1) FROM ScenarioQuestion WHERE code = :code AND scenarioId = :scenarioId AND campaignId = :campaignId AND companySiteId = :companySiteId AND status = 1")
    Integer countDuplicateQuestionCode(@Param("code") String code, @Param("scenarioId") Long scenarioId, @Param("campaignId") Long campaignId, @Param("companySiteId") Long companySiteId);

    Long countByStatusAndScenarioQuestionIdNotAndCodeAndCampaignIdAndCompanySiteId(Short status, Long scenarioQuestionId, String code, Long campaignId, Long companySiteId);

    Long countByStatusAndScenarioQuestionIdNotAndOrderIndexAndCampaignIdAndCompanySiteId(Short status, Long scenarioQuestionId, Long orderIndex, Long campaignId, Long companySiteId);

    ScenarioQuestion findScenarioQuestionByScenarioQuestionId(Long scenarioQuestionId);

    ScenarioQuestion findScenarioQuestionByCodeAndCompanySiteIdAndStatus(String code, Long companySiteId, Short status);

    List<ScenarioQuestion> findScenarioQuestionsByScenarioIdAndStatusOrderByOrderIndex(Long scenarioId, Short status);

}
