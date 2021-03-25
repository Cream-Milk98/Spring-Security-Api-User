package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.CustomizeFieldOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomizeFieldOptionValueRepository extends JpaRepository<CustomizeFieldOptionValue, Long> {
    List<CustomizeFieldOptionValue> findCustomizeFieldOptionValuesByFieldOptionIdAndStatus(Long id, Long status);

    CustomizeFieldOptionValue findCustomizeFieldOptionValueByNameEqualsAndStatus(String name, Long status);

    List<CustomizeFieldOptionValue> findCustomizeFieldOptionValueByFieldOptionIdOrderByName(Long fieldOptionId);
}
