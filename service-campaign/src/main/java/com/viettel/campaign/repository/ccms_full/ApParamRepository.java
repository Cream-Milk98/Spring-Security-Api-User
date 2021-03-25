package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.ApParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApParamRepository extends JpaRepository<ApParam, Long> {

    Page<ApParam> findAll(Pageable pageable);

    @Query(value = "SELECT p FROM ApParam p WHERE parName LIKE concat('%', :parName, '%') ")
    List<ApParam> findParamByName(@Param("parName") String parName, Pageable pageable);

    @Query(value = "SELECT p FROM ApParam p WHERE status = 1 AND parType LIKE concat('%', :parType, '%') ")
    List<ApParam> findParamByParType(@Param("parType") String parType);

    @Query(value = "FROM  ApParam WHERE status = 1 AND parType = :parType")
    List<ApParam> findAllParam(@Param("parType") String parType);

    // ----------- sql lay so thu tu bang customer list ------------ //
    @Query("select a from ApParam a where a.parType = 'CUSTOMER_LIST_SEQ'")
    ApParam getCustomerListSeq();

    @Modifying
    @Query("update ApParam a set a.parValue = :p_par_value, a.description = :p_description where a.parType = 'CUSTOMER_LIST_SEQ'")
    int updateCustomerListSeq(@Param("p_par_value") String p_par_value, @Param("p_description") String p_description);

    List<ApParam> findBySiteIdAndParTypeAndIsDelete(Long siteId, String parType, Long isDelete);

}
