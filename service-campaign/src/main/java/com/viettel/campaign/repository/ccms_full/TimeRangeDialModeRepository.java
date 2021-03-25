package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.TimeRangeDialMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author anhvd_itsol
 */

@Repository
public interface TimeRangeDialModeRepository extends JpaRepository<TimeRangeDialMode, Long> {

    List<TimeRangeDialMode> findTimeRangeDialModeByCampaignIdAndCompanySiteId(Long campaignId, Long companySiteId);

    @Query(value = "SELECT * " +
            "FROM (SELECT R.* " +
            "      FROM TIME_RANGE_DIAL_MODE R INNER JOIN CAMPAIGN C ON R.CAMPAIGN_ID = C.CAMPAIGN_ID" +
            "      WHERE R.COMPANY_SITE_ID = :companySiteId " +
            "        AND R.CAMPAIGN_ID = :campaignId " +
            "        AND (C.START_TIME + (:timezoneOffset / 60)/24) <= SYSDATE " +
            "        AND (R.START_TIME + (:timezoneOffset / 60)/24) <= SYSDATE " +
            "      ORDER BY R.START_TIME DESC " +
            "     ) WHERE ROWNUM = 1", nativeQuery = true)
    TimeRangeDialMode findDialModeAtCurrent(@Param("companySiteId") Long companySiteId, @Param("campaignId") Long campaignId, @Param("timezoneOffset") Integer timezoneOffset);

    void deleteAllByCampaignIdAndCompanySiteId(Long campaignId, Long companySiteId);
}
