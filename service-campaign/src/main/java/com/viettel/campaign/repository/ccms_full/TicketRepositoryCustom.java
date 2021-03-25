package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.web.dto.TicketDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TicketRepositoryCustom {
    List<TicketDTO> getHistory(String customerId, Long timezone, Pageable pageable);
}
