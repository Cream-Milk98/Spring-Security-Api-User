package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.ProcessConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hanv_itsol
 * @project campaign
 */

@Repository
public interface ProcessConfigRepository extends JpaRepository<ProcessConfig, Long> {

    List<ProcessConfig> findAllByConfigCode(String configCode);

}
