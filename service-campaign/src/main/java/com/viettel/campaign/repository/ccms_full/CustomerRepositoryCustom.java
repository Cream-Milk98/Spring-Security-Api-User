package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.web.dto.*;
import com.viettel.campaign.web.dto.request_dto.CustomerDetailRequestDTO;
import com.viettel.campaign.web.dto.request_dto.SearchCustomerRequestDTO;
import com.viettel.econtact.filter.UserSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomerRepositoryCustom {

    List<CustomerDetailRequestDTO> getCustomerDetailById(Long companySiteId, Long customerId, Long timezoneOffset);

    Page<CustomerCustomDTO> getAllCustomerByParams(SearchCustomerRequestDTO searchCustomerRequestDTO, Pageable pageable);

    List<CampaignInformationDTO> getCampaignInformation(CampaignCustomerDTO campaignCustomerDTO);

    Page<CustomerCustomDTO> getIndividualCustomerInfo(CampaignCustomerDTO campaignCustomerDTO, Pageable pageable);

    List<CustomerListDTO> getCustomerListInfo(CampaignCustomerDTO campaignCustomerDTO);

    Page<CustomerDTO> getCustomizeFields(CampaignCustomerDTO campaignCustomerDTO, UserSession userSession, Pageable pageable);

    List<CustomerDetailRequestDTO> getIndividualCustomerDetailById(Long companySiteId, Long customerId, Long timezoneOffset);
}
