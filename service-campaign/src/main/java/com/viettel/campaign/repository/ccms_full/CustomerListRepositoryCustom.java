package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.web.dto.CustomerListDTO;
import com.viettel.campaign.web.dto.request_dto.SearchCustomerListRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomerListRepositoryCustom {
    Page<CustomerListDTO> getAllCustomerListByParams(SearchCustomerListRequestDTO searchCustomerListRequestDTO, Pageable pageable);
}
