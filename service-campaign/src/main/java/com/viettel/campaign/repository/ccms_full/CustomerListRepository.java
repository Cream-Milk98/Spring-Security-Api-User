package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.CustomerList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerListRepository extends JpaRepository<CustomerList, Long>, CustomerListRepositoryCustom {

    CustomerList findByCustomerListIdAndCompanySiteId(long customerListId, long companySiteId);

    CustomerList findByCustomerListCode(String customerListCode);

    @Query("select c from CustomerList c where c.customerListId in (:p_ids) and c.companySiteId=:p_company_site_id and c.status = 1")
    List<CustomerList> findAllCustomerList(@Param("p_ids") List<Long> p_ids, @Param("p_company_site_id") Long p_company_site_id);

    @Modifying
    @Query("update CustomerList c set c.status = 0 where c.customerListId in (:p_ids) and c.companySiteId=:p_company_site_id")
    int deleteCustomerListIds(@Param("p_ids") List<Long> p_ids, @Param("p_company_site_id") Long p_company_site_id);
}
