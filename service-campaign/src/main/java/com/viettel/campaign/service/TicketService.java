package com.viettel.campaign.service;

import com.viettel.campaign.web.dto.ResultDTO;

public interface TicketService {
    ResultDTO getHistory(int page, int pageSize, String sort, String customerId, Long timezone);
}
