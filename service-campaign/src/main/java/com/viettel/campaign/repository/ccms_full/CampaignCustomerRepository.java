package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.CampaignCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CampaignCustomerRepository extends JpaRepository<CampaignCustomer, Long>, CampaignCustomerRepositoryCustom {

    CampaignCustomer findCampaignCustomerByCampaignCustomerId(Long id);

    CampaignCustomer findCampaignCustomerByCampaignIdAndCompanySiteIdAndCustomerId(Long campaignId, Long companySiteId, Long customerId);

    CampaignCustomer findCampaignCustomersByCampaignIdAndCustomerId(Long campaignId, Long customerId);

    @Query(value = "SELECT COUNT(*) " +
            " FROM CAMPAIGN_CUSTOMER CC " +
            " JOIN CAMPAIGN C ON CC.CAMPAIGN_ID = C.CAMPAIGN_ID " +
            " LEFT JOIN CAMPAIGN_COMPLETE_CODE CCC ON CC.STATUS = CCC.COMPLETE_VALUE" +
            " WHERE CC.COMPANY_SITE_ID = :companySiteId " +
            " AND CC.AGENT_ID = :agentId" +
            " AND C.STATUS = 2 " +
            " AND CC.RECALL_TIME <= :sysdate " +
            " AND CCC.IS_RECALL = 1", nativeQuery = true)
    Long countRecallCustomerUserRole(@Param("companySiteId") Long pCompanySiteId, @Param("agentId") Long agentId, @Param("sysdate") Date sysdate);

    @Query(value = "SELECT COUNT(*) " +
            " FROM CAMPAIGN_CUSTOMER CC " +
            " JOIN CAMPAIGN C ON CC.CAMPAIGN_ID = C.CAMPAIGN_ID " +
            " LEFT JOIN CAMPAIGN_COMPLETE_CODE CCC ON CC.STATUS = CCC.COMPLETE_VALUE" +
            " WHERE CC.COMPANY_SITE_ID = :companySiteId " +
            " AND C.STATUS = 2 " +
            " AND CC.RECALL_TIME <= :sysdate " +
            " AND CCC.IS_RECALL = 1", nativeQuery = true)
    Long countRecallCustomerAdminRole(@Param("companySiteId") Long pCompanySiteId, @Param("sysdate") Date sysdate);

    @Query(value = "SELECT COUNT(*) " +
            "FROM CAMPAIGN_CUSTOMER CC JOIN CAMPAIGN_COMPLETE_CODE CCC ON CC.STATUS = CCC.COMPLETE_VALUE " +
            "WHERE CC.CAMPAIGN_ID = :campaignId " +
            "AND CC.CUSTOMER_ID = :customerId " +
            "AND CCC.IS_RECALL = 1 " +
            "AND CCC.STATUS = 1 ", nativeQuery = true)
    Long getCustomerRecall(@Param("campaignId") Long campaignId, @Param("customerId") Long customerId);

    @Query(value = "select C.NAME, " +
            "C.EMAIL, " +
            "C.PLACE_OF_BIRTH, " +
            "C.AREA_CODE, " +
            "C.COMPANY_NAME, " +
            "C.CURRENT_ADDRESS, " +
            "C.CUSTOMER_TYPE, " +
            "C.DATE_OF_BIRTH, " +
            "C.CUSTOMER_ID," +
            "C.USERNAME," +
            "C.GENDER, CF.TITLE " +
//            " from CAMPAIGN_COMPLETE_CODE cm " +
            "from CUSTOMER C " +
            "left join CUSTOMIZE_FIELDS CF " +
            "on C.CUSTOMER_ID = CF.CUSTOMIZE_FIELD_ID " +
            "where CF.FUNCTION_CODE = 'CUSTORMER' and c.STATUS =1 and CF.ACTIVE =1 and CF.SITE_ID =?"
            , nativeQuery = true)
    Long searchCustomer(@Param("site_id") Long pSiteId);

    @Query(value = "SELECT * FROM CAMPAIGN_CUSTOMER CC " +
            "WHERE CC.STATUS = 0 " +
            "AND CC.CAMPAIGN_ID = :campaignId " +
            "AND CC.COMPANY_SITE_ID = :companySiteId " +
            "AND CC.CUSTOMER_ID NOT IN (SELECT CUSTOMER_ID FROM RECEIVE_CUST_LOG WHERE END_TIME IS NULL) " +
            "AND CC.CUSTOMER_LIST_ID = :customerListId", nativeQuery = true)
    List<CampaignCustomer> findCustomerNoContact(@Param("campaignId") Long campaignId, @Param("companySiteId") Long companySiteId, @Param("customerListId") Long customerListId);

    @Query(value = "SELECT * FROM CAMPAIGN_CUSTOMER CC " +
            "WHERE CC.STATUS IN (SELECT COMPLETE_VALUE from CAMPAIGN_COMPLETE_CODE where STATUS = 1 and COMPLETE_TYPE=1)" +
            "AND CC.CAMPAIGN_ID = :campaignId " +
            "AND CC.COMPANY_SITE_ID = :companySiteId " +
            "AND CC.CUSTOMER_ID NOT IN (SELECT CUSTOMER_ID FROM RECEIVE_CUST_LOG WHERE END_TIME IS NULL) " +
            "AND CC.CUSTOMER_LIST_ID = :customerListId", nativeQuery = true)
    List<CampaignCustomer> findCustomerContacted(@Param("campaignId") Long campaignId, @Param("companySiteId") Long companySiteId, @Param("customerListId") Long customerListId);

    @Query(value = "with status_customer as (\n" +
            "select complete_value \n" +
            "from campaign_complete_code\n" +
            "where status = 1\n" +
            "    and complete_type = 1\n" +
            "    and company_site_id = :p_company_site_id\n" +
            ")\n" +
            "select * from campaign_customer\n" +
            "where campaign_id = :p_campaign_id\n" +
            "    and customer_list_id = :p_cus_list_id\n" +
            "    AND CUSTOMER_ID NOT IN (SELECT CUSTOMER_ID FROM RECEIVE_CUST_LOG WHERE END_TIME IS NULL) " +
            "    and (status = 0 or status in (select * from status_customer))", nativeQuery = true)
    List<CampaignCustomer> findListCustomerToDel(@Param("p_company_site_id") Long companySiteId, @Param("p_campaign_id") Long campaignId, @Param("p_cus_list_id") Long customerListId);

    @Query(value = "select complete_value from campaign_complete_code where status = 1 and complete_type = 1", nativeQuery = true)
    List<Short> getStatus();

    CampaignCustomer findByCampaignIdAndCompanySiteIdAndCustomerIdAndInCampaignStatus(Long campaignId, Long companySiteId, Long customerId, Short inCampaignStatus);

    @Query(value = "SELECT * FROM CAMPAIGN_CUSTOMER cc\n" +
            "WHERE cc.CAMPAIGN_ID = :p_campaign_id\n" +
            "AND cc.COMPANY_SITE_ID = :p_company_site_id\n" +
            "AND cc.IN_CAMPAIGN_STATUS = 1\n" +
            "AND cc.CUSTOMER_ID IN (:p_customer_id_list)\n" +
            "AND cc.STATUS IN (SELECT COMPLETE_VALUE FROM CAMPAIGN_COMPLETE_CODE ccc\n" +
            "WHERE ccc.COMPANY_SITE_ID = :p_company_site_id AND ccc.STATUS = 1 AND COMPLETE_TYPE = 2)", nativeQuery = true)
    List<CampaignCustomer> findCampaignCustomers2(@Param("p_campaign_id") Long campaignId, @Param("p_company_site_id") Long companySiteId, @Param("p_customer_id_list") String[] listId);

    @Query(value = "SELECT * FROM CAMPAIGN_CUSTOMER cc\n" +
            "WHERE cc.CAMPAIGN_ID = :p_campaign_id\n" +
            "AND cc.COMPANY_SITE_ID = :p_company_site_id\n" +
            "AND cc.IN_CAMPAIGN_STATUS = 1\n" +
            "AND cc.CUSTOMER_ID IN (:p_customer_id)\n" +
            "AND cc.STATUS IN (SELECT COMPLETE_VALUE FROM CAMPAIGN_COMPLETE_CODE ccc\n" +
            "AND cc.CUSTOMER_ID NOT IN (SELECT CUSTOMER_ID FROM RECEIVE_CUST_LOG WHERE END_TIME IS NULL) " +
            "WHERE ccc.COMPANY_SITE_ID = :p_company_site_id AND ccc.STATUS = 1 AND COMPLETE_TYPE = 1)", nativeQuery = true)
    CampaignCustomer findCampaignCustomers1(@Param("p_campaign_id") Long campaignId, @Param("p_company_site_id") Long companySiteId, @Param("p_customer_id") String customerId);

    @Query(value = "SELECT COUNT(*) FROM CAMPAIGN_CUSTOMER WHERE 1 = 1 AND COMPANY_SITE_ID = :p_company_site_id AND CAMPAIGN_ID = :p_campaign_id AND IN_CAMPAIGN_STATUS = 1 AND CUSTOMER_LIST_ID IS NULL", nativeQuery = true)
    int countAllByCompanySiteIdAndCampaignId(@Param("p_company_site_id") Long companySiteId, @Param("p_campaign_id") Long campaignId);

    CampaignCustomer findByCampaignIdAndCompanySiteIdAndInCampaignStatusAndStatusAndCustomerId(Long campaignId, Long companySiteId, Short inCampaignStatus, Short status, Long customerId);

}
