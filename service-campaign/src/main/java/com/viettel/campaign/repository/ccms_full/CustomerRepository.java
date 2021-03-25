package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.Customer;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.request_dto.CustomizeRequestDTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {

    Page<Customer> findAll(Pageable pageable);

    Customer getByCustomerId(Long customerId);

    Customer findByCustomerId(Long customerId);

    @Query("FROM Customer WHERE name = ?1")
    List<Customer> findByName(String firstName, Pageable pageable);

    @Query("SELECT COUNT(0) FROM Customer t WHERE t.mobileNumber = ?1 and t.siteId = ?2 and t.status = 1 ")
    Long findByMobileNumberAndSiteId(String mobileNumber, Long siteId);

    @Modifying
    @Query("delete from Customer c where c.customerId in (:ids)")
    int deleteIds(@Param("ids") List<Long> ids);

    @Query("select c from Customer c left join CustomerTime ct on c.customerId = ct.customerId " +
            "where c.ipccStatus = 'locked' and c.siteId =?1 and ct.endTime <= ?2")
    List<Customer> findAllByCondition(Long siteId, Date endTime);


    @Query(value = "select * from customer a\n" +
            "left join customer_list_mapping b on a.customer_id = b.customer_id\n" +
            "where b.customer_list_id = :p_customer_list_id and a.ipcc_status <> 'locked'\n" +
            "    and a.customer_id not in (select cc.customer_id from campaign_customer cc where cc.campaign_id = :p_campaign_id and cc.customer_list_id = :p_customer_list_id and cc.in_campaign_status <> 0)", nativeQuery = true)
    List<Customer> findAllCutomerNotInCampagin(@Param("p_customer_list_id") Long customerListId, @Param("p_campaign_id") Long campaignId);
}
