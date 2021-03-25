package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.CustomizeFieldObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomizeFieldObjectRepository extends JpaRepository<CustomizeFieldObject, Long> {
    List<CustomizeFieldObject> findCustomizeFieldObjectsByFunctionCodeEqualsAndObjectId(String str, Long id);

    List<CustomizeFieldObject> findCustomizeFieldObjectsByObjectId(Long objectId);
}
