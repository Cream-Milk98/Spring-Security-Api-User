package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.AgentStatusStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AgentStatusStatRepository extends JpaRepository<AgentStatusStat, Long> {

    @Query(value = "FROM AgentStatusStat WHERE kzUserId = :kzUserId ORDER BY timestamp DESC")
    List<AgentStatusStat> getStatusByKzUserId(@Param("kzUserId") String kzUserId);

}
