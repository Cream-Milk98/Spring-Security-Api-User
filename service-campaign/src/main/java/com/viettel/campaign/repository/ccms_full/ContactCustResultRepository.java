package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.ContactCustResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ContactCustResultRepository extends JpaRepository<ContactCustResult, Long> {

    ContactCustResult findByContactCustResultId(Long contactCustResultId);

    @Query(value = "SELECT MAX(createTime) FROM ContactCustResult WHERE campaignId = :campaignId AND agentId = :agentId AND status = 1")
    Date getMaxCreateTime(@Param("campaignId") Long campaignId, @Param("agentId") Long agentId);


    @Query(value = "SELECT MAX(CONTACT_CUST_RESULT_ID) FROM CONTACT_CUST_RESULT" +
            " WHERE CUSTOMER_ID = :p_customer_id" +
            "  AND CAMPAIGN_ID = :p_campaign_id" +
            "  AND COMPANY_SITE_ID = :p_site_id", nativeQuery = true)
    Long getContactCusResultId(@Param("p_customer_id") Long customerId, @Param("p_campaign_id") Long campaignId, @Param("p_site_id") Long companySiteId);

    List<ContactCustResult> findByReceiveCustLogId(Long receiveCustLogId);

    List<ContactCustResult> findByReceiveCustLogIdAndStatusNotIn(Long receiveCustLogId, Short status);

    ContactCustResult findByCustomerIdAndCampaignIdAndCompanySiteIdAndContactStatusAndStatus(Long customerId, Long campaignId,
                                                                                             Long companySiteId, Short contactStatus,
                                                                                             Short Status);

    ContactCustResult findByCallIdAndStatus(String callId, Short status);
}
