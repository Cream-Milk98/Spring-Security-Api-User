package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.CustomerContact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(DataSourceQualify.CCMS_FULL)
public interface CustomerContactRepository extends JpaRepository<CustomerContact, Long> {

    @Query("FROM CustomerContact WHERE status = 1 AND customerId = :customerId AND contactType = :contactType AND (contact IS NULL OR UPPER(contact) LIKE UPPER(concat('%', :contact, '%')))")
    List<CustomerContact> findByCustomerIdAndAndContactTypeAndContact(@Param("customerId") Long customerId, @Param("contactType") Short contactType, @Param("contact") String contact, Pageable pageable);

    Page<CustomerContact> getByStatusAndCustomerIdAndContactTypeAndContactContaining(Short status, Long customerId, Short contactType, String contact, Pageable pageable);

    Page<CustomerContact> getByStatusAndCustomerIdAndContactType(Short status, Long customerId, Short contactType, Pageable pageable);

    List<CustomerContact> findCustomerContactsByContactAndStatusAndIsDirectLineAndSiteId(String contact, Short status, Short isDirectLine, Long siteId);

    List<CustomerContact> findCustomerContactsByStatusAndIsDirectLineAndCustomerIdAndContactTypeAndSiteId(Short status, Short isDirectLine, Long customerId, Short contactType, Long siteId);

    CustomerContact getByContactAndStatusAndIsDirectLine(String contact, Short status, Short isDirectLine);

    CustomerContact getByContactAndStatusAndIsDirectLineAndCustomerIdAndSiteId(String contact, Short status, Short isDirectLine, Long customerId, Long siteId);

    @Query("SELECT cc FROM CustomerContact cc WHERE cc.customerId = :customerId AND cc.status = :status AND cc.contactType = :contactType AND cc.isDirectLine = :isDirectLine ORDER BY cc.createBy DESC")
    List<CustomerContact> getLastPhone(@Param("customerId") Long customerId, @Param("status") Short status, @Param("contactType") Short contactType, @Param("isDirectLine") Short isDirectLine);

    @Query("SELECT cc FROM CustomerContact cc WHERE cc.customerId = :customerId AND cc.status = :status AND cc.contactType = :contactType ORDER BY cc.createBy DESC")
    List<CustomerContact> getLastEmail(@Param("customerId") Long customerId, @Param("status") Short status, @Param("contactType") Short contactType);

}
