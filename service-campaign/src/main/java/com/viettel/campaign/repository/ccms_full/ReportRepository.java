package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.ScenarioQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
@Service
public interface ReportRepository extends JpaRepository<ScenarioQuestion, Long> {
    int getTotal(String queryString, HashMap<String, Object> hmapParams);
}
