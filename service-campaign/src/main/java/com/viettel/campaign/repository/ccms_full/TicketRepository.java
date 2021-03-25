package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.Ticket;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(DataSourceQualify.CCMS_FULL)
public interface TicketRepository extends BaseRepository<Ticket>, TicketRepositoryCustom {
}
