package com.viettel.campaign.service;

import com.viettel.campaign.model.ccms_full.Customer;
import com.viettel.campaign.model.ccms_full.CustomerList;
import com.viettel.campaign.model.ccms_full.CustomizeFieldObject;
import com.viettel.campaign.model.ccms_full.CustomizeFields;
import com.viettel.campaign.web.dto.*;
import com.viettel.campaign.web.dto.request_dto.CustomerRequestDTO;
import com.viettel.campaign.web.dto.request_dto.SearchCustomerListRequestDTO;
import com.viettel.campaign.web.dto.request_dto.SearchCustomerRequestDTO;
import com.viettel.econtact.filter.UserSession;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CustomerService {

    ResultDTO getCustomerId(Long customerId);

    ResultDTO searchAllCustomer(SearchCustomerRequestDTO searchCustomerRequestDTO);

    ResultDTO createCustomer(CustomerDTO customerDTO);

    ResultDTO deleteIds(CustomerRequestDTO customerRequestDTO);

    ResultDTO getCustomerDetailById(Long companySiteId, Long customerId, Long timezoneOffset);

    // ------------ customer list ------------ //

    // THÍM NÀO MERGE CONFLICT THÌ GIỮ LẠI HỘ E CÁI METHOD NÀY VỚI
    // VIẾT ĐI VIẾT LẠI 4 LẦN RỒI ĐẤY
    ResultDTO createCustomerList(CustomerListDTO customerListDTO, String userName);

    ResultDTO updateCustomerList(CustomerListDTO customerListDTO, String userName);

    ResultDTO deleteCustomerListIds(CustomerRequestDTO customerRequestDTO);

    ResultDTO searchCustomerList(SearchCustomerListRequestDTO searchCustomerListRequestDTO);

    // ------------ customer contact ------------ //

    ResultDTO getCustomerContact(CustomerContactDTO customer);

    // danh sach khach hang cua chien dich //

    List<CustomerListDTO> getCustomerListInfo(CampaignCustomerDTO campaignCustomerDTO);

    ResultDTO getIndividualCustomerInfo(CampaignCustomerDTO campaignCustomerDTO);

    ResultDTO deleteCustomerFromCampaign(CampaignCustomerDTO campaignCustomerDTO);

    ResultDTO searchCampaignInformation(CampaignCustomerDTO campaignCustomerDTO);

    ResultDTO addCustomerToCampaign(CampaignCustomerDTO campaignCustomerDTO);

    ResultDTO getDataForCombobox(CampaignCustomerDTO campaignCustomerDTO);

    // ------------ customer  ------------ //

    ResultDTO getCustomerRecall(Long campaignId, Long customerId);

    List<Customer> findAllByCondition(Long siteId, Date endTime);

    Customer update(Customer c);

    List<CustomizeFields> getDynamicHeader(Long companySiteId);

    XSSFWorkbook buildTemplate(Long companySiteId, String language) throws IOException;

    Map<String, Object> readAndValidateCustomer(String path, List<CustomizeFields> headerDTOS, UserSession userSession, Long customerListId, String language) throws IOException;

    ResultDTO getCustomizeField(Long companySiteId, Long customerId, Long timezoneOffset);

    List<Customer> searchByQuery(String queryString);

    Long countByQuery(String queryString);

    //    Map<String, CustomizeRequestDTo>  searchCustomer();
////    List<CustomizeFields> searchCustomize();
    ResultDTO listCustomizeFields(CustomizeFieldsDTO customizeFields);

    ResultDTO searchCustomizeFields(CampaignCustomerDTO campaignCustomerDTO, UserSession userSession);

}
