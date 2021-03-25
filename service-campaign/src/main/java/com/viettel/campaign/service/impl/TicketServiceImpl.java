package com.viettel.campaign.service.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.repository.ccms_full.TicketRepository;
import com.viettel.campaign.service.TicketService;
import com.viettel.campaign.utils.Constants;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.TicketDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Autowired
    TicketRepository ticketRepository;

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getHistory(int page, int pageSize, String sort, String customerId, Long timezone) {
        ResultDTO result = new ResultDTO();
        List<TicketDTO> lst = new ArrayList<>();

        try {
            Integer c = ticketRepository.getHistory(customerId, timezone, null).size();
            if (c > 0) {
                lst = ticketRepository.getHistory(customerId, timezone, PageRequest.of(page, pageSize, Sort.by(sort)));
                result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                result.setDescription(Constants.ApiErrorDesc.SUCCESS);
                result.setListData(lst);
                result.setTotalRow(Long.valueOf(c));
            } else {
                result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                result.setDescription(Constants.ApiErrorDesc.SUCCESS);
                result.setTotalRow(0L);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }
}
