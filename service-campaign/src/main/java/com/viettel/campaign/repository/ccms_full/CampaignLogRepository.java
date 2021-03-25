package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.CampaignLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anhvd_itsol
 */

@Repository
public interface CampaignLogRepository extends JpaRepository<CampaignLog, Long> {
}
