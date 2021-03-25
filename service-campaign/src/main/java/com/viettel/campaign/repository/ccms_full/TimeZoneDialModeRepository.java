package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.TimeZoneDialMode;
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
public interface TimeZoneDialModeRepository extends JpaRepository<TimeZoneDialMode, Long> {

    List<TimeZoneDialMode> findTimeZoneDialModeByCampaignIdAndCompanySiteId(Long campaignId, Long companySiteId);

    @Query(value = "SELECT * " +
            "FROM (SELECT Z.* " +
            "      FROM TIME_ZONE_DIAL_MODE Z INNER JOIN CAMPAIGN C ON Z.CAMPAIGN_ID = C.CAMPAIGN_ID " +
            "      WHERE Z.COMPANY_SITE_ID = :companySiteId " +
            "        AND Z.CAMPAIGN_ID = :campaignId " +
            "        AND TO_DATE((TO_CHAR(C.START_TIME, 'YYYY/MM/DD') || ' ' || Z.HOUR || ':' || Z.MINUTE || ':00'), 'YYYY/MM/DD HH24:MI:SS') <= SYSDATE " +
            "        AND TO_DATE((TO_CHAR(SYSDATE, 'YYYY/MM/DD') || ' ' || Z.HOUR || ':' || Z.MINUTE || ':00'), 'YYYY/MM/DD HH24:MI:SS') <= SYSDATE " +
            "      ORDER BY TO_DATE((TO_CHAR(SYSDATE, 'YYYY/MM/DD') || ' ' || Z.HOUR || ':' || Z.MINUTE || ':00'), 'YYYY/MM/DD HH24:MI:SS') DESC " +
            "     ) WHERE ROWNUM = 1", nativeQuery = true)
    TimeZoneDialMode findDialModeAtCurrent(@Param("companySiteId") Long companySiteId, @Param("campaignId") Long campaignId);

    void deleteAllByCampaignIdAndCompanySiteId(Long campaignId, Long companySiteId);
}
