package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.CampaignCfg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CampaignCfgRepository extends JpaRepository<CampaignCfg, Long> {

    @Query("select c from CampaignCfg c where c.companySiteId=:p_company_site_id and c.status = 1")
    List<CampaignCfg> findByCampaignCompleteCodeId( @Param("p_company_site_id") Long p_company_site_id);

    @Query("select c from CampaignCfg c where c.campaignCompleteCodeId in (:p_ids) and c.companySiteId=:p_company_site_id and c.status = 1")
    List<CampaignCfg> findAllCampaignCfg(@Param("p_ids") List<Long> p_ids, @Param("p_company_site_id") Long p_company_site_id);

    @Query("FROM CampaignCfg u WHERE u.status = 1 AND u.completeValue NOT IN (1,2,3,4)")
    Page<CampaignCfg> findAll(Pageable pageable);

    @Query("FROM CampaignCfg WHERE completeName LIKE concat('%', :name, '%') ")
    List<CampaignCfg> findByName(@Param("name") String name, Pageable pageable);

    List<CampaignCfg> findByCompleteNameContains(String name, Pageable pageable);

    @Modifying
    @Query("update CampaignCfg c set c.status = 0 where c.campaignCompleteCodeId in (:p_ids) and c.companySiteId=:p_company_site_id")
    int deletedList(@Param("p_ids") List<Long> p_ids, @Param("p_company_site_id") Long p_company_site_id);

    List<CampaignCfg> findCampaignCompleteCodesByCompanySiteId(Long companySiteId);

    @Query(value = "SELECT max(completeValue) FROM CampaignCfg WHERE companySiteId = :companySiteId GROUP BY companySiteId")
    Short findByMaxCompanySiteId(Long companySiteId);

    @Modifying
    @Query("update CampaignCfg c set c.status = 0 where c.campaignCompleteCodeId=:p_campaignCompleteCode_list_id and c.companySiteId=:p_company_site_id")
    int deleteCampaignCompleteCodeBy(@Param("p_campaignCompleteCode_list_id") Long p_campaignCompleteCode_list_id, @Param("p_company_site_id") Long p_company_site_id);

    @Query(value = "FROM CampaignCfg WHERE status = 1 AND completeValue = :completeValue AND completeType = :completeType AND companySiteId = :companySiteId")
    List<CampaignCfg> getCustomerStatus(@Param("completeValue") String completeValue, @Param("completeType") Short completeType, @Param("companySiteId") Long companySiteId);

    @Query(value = "FROM CampaignCfg WHERE status = 1 AND completeType = :completeType AND companySiteId = :companySiteId")
    List<CampaignCfg> getCustomerStatusByType(@Param("completeType") Short completeType, @Param("companySiteId") Long companySiteId);

    @Query(value = "FROM CampaignCfg WHERE completeType = :completeType AND companySiteId = :companySiteId")
    List<CampaignCfg> getAllCustomerStatusByType(@Param("completeType") Short completeType, @Param("companySiteId") Long companySiteId);

    @Query(value = "FROM CampaignCfg WHERE status = 1 AND completeValue <> :completeValue AND completeValue <> 4 AND completeType = :completeType AND companySiteId = :companySiteId")
    List<CampaignCfg> getCustomerStatusWithoutValue(@Param("completeValue") String completeValue, @Param("completeType") Short completeType, @Param("companySiteId") Long companySiteId);

    @Query(value = "FROM CampaignCfg WHERE completeValue <> :completeValue AND completeType = :completeType AND companySiteId = :companySiteId")
    List<CampaignCfg> getConnectStatusWithoutValue(@Param("completeValue") String completeValue, @Param("completeType") Short completeType, @Param("companySiteId") Long companySiteId);

    @Query("select c from CampaignCfg c where c.companySiteId =:p_company_site_id")
    List<CampaignCfg> findAllCampaignCompleteCode();

    @Query(value = "FROM CampaignCfg WHERE status = 1 AND completeValue = :completeValue AND companySiteId = :companySiteId")
    CampaignCfg findByCompanySiteIdAndCompleteValue(@Param("companySiteId") Long companySiteId, @Param("completeValue") String completeValue);

    @Query(value = "SELECT c FROM CampaignCfg c WHERE c.status = 1 AND c.companySiteId = :p_company_site_id ORDER BY function('TO_NUMBER', c.completeValue) ASC")
    Page<CampaignCfg> listAllCompleteCode(@Param("p_company_site_id") Long companySiteId, Pageable pageable);

    @Query(value = "SELECT COALESCE(MAX(TO_NUMBER(COMPLETE_VALUE)), 0) FROM CAMPAIGN_COMPLETE_CODE WHERE COMPANY_SITE_ID = :p_company_site_id", nativeQuery = true)
    Long findMaxValueCampaignCompleteCode(@Param("p_company_site_id") Long companySiteId);

}
