package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.CustomerTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hanv_itsol
 * @project campaign
 */

@Repository
public interface CustomerTimeRepository extends JpaRepository<CustomerTime, Long> {

    List<CustomerTime> findByCustomerId(Long customerId);

    CustomerTime findByCompanySiteIdAndCustomerId(Long companySiteId, Long customerId);
}
