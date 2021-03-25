package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.CustomizeFields;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(DataSourceQualify.CCMS_FULL)
public interface CustomizeFieldsRepository extends JpaRepository<CustomizeFields, Long> {

    List<CustomizeFields> findCustomizeFieldsByFunctionCodeEqualsAndStatusAndActiveAndSiteId(String functionCode, Long status, Long active, Long siteId);

    List<CustomizeFields> findByFunctionCodeAndActiveAndStatusAndSiteId(String functionCode, Long active, Long status, Long siteId);
}
