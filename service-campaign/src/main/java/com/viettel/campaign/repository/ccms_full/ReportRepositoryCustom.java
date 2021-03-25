package com.viettel.campaign.repository.ccms_full;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

public interface ReportRepositoryCustom {

    int getTotal(String sql, HashMap<String, Object> hmapParams);

}
