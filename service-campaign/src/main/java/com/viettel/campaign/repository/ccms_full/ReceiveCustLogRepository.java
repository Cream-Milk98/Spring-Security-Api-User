package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.ReceiveCustLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiveCustLogRepository extends JpaRepository<ReceiveCustLog, Long> {
    ReceiveCustLog getByReceiveCustLogId(Long receiveCustLogId);
}
