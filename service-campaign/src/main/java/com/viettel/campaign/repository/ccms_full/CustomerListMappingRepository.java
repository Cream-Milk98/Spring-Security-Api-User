package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.CustomerListMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerListMappingRepository extends JpaRepository<CustomerListMapping, Long> {

    // ----------- customer ------------ //

    @Query("select c from CustomerListMapping c where c.customerId in (:p_ids) and c.customerListId =:p_customer_list_id and c.companySiteId=:p_company_site_id")
    List<CustomerListMapping> findAllCustomerListMapping(@Param("p_ids") List<Long> p_ids, @Param("p_customer_list_id") Long p_customer_list_id, @Param("p_company_site_id") Long p_company_site_id);

    @Modifying
    @Query("delete from CustomerListMapping c where c.customerId in (:p_ids) and c.customerListId =:p_customer_list_id and c.companySiteId=:p_company_site_id")
    int deleteMappingByCustomerIds(@Param("p_ids") List<Long> p_ids, @Param("p_customer_list_id") Long p_customer_list_id, @Param("p_company_site_id") Long p_company_site_id);

    // ----------- customer list --------------- //

    @Modifying
    @Query("delete from CustomerListMapping c where c.customerListId in (:p_ids) and c.companySiteId=:p_company_site_id")
    int deleteMappingByCustomerListIds(@Param("p_ids") List<Long> p_ids, @Param("p_company_site_id") Long p_company_site_id);

    @Query(value = "SELECT COUNT(*) FROM CUSTOMER_LIST_MAPPING WHERE 1 = 1 AND COMPANY_SITE_ID = :p_company_site_id AND CUSTOMER_LIST_ID = :p_customer_list_id", nativeQuery = true)
    int countAllByCompanySiteIdAndCustomerListId(@Param("p_company_site_id") Long companySiteId, @Param("p_customer_list_id") Long customerListId);

    CustomerListMapping getByCustomerIdAndCompanySiteIdAndCustomerListId(Long customerId, Long companySiteId, Long customerListId);
}
