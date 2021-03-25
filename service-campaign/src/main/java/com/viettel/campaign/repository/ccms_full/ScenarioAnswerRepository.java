package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.ScenarioAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author anhvd_itsol
 */

@Repository
public interface ScenarioAnswerRepository extends JpaRepository<ScenarioAnswer, Long> {

    List<ScenarioAnswer> findByScenarioQuestionIdAndCompanySiteIdAndStatusOrderByOrderIndex(Long scenarioQuestionId, Long companySiteId, Short status);

    @Query(value="SELECT MAX(orderIndex) FROM ScenarioAnswer WHERE scenarioQuestionId = :scenarioQuestionId AND companySiteId = :companySiteId AND status = 1")
    Long getMaxAnswerOrderId(@Param("scenarioQuestionId") Long scenarioQuestionId, @Param("companySiteId") Long companySiteId);

    Integer deleteScenarioAnswersByScenarioQuestionId(Long scenarioQuestionId);

    ScenarioAnswer findScenarioAnswerByScenarioAnswerId(Long scenarioAnswerId);

    @Query(value = "SELECT COUNT(1) FROM ScenarioAnswer WHERE 1 = 1 AND code = :code AND scenarioQuestionId = :scenarioQuestionId AND scenarioAnswerId <> :scenarioAnswerId")
    Integer countDuplicateScenarioCode(@Param("code") String code, @Param("scenarioQuestionId") Long scenarioQuestionId, @Param("scenarioAnswerId") Long scenarioAnswerId);

    ScenarioAnswer findScenarioAnswerByCodeAndStatus(String code, Short status);

    List<ScenarioAnswer> findScenarioAnswerByScenarioQuestionIdAndStatusOrderByOrderIndex(Long scenarioQuestionId, Short status);
}
