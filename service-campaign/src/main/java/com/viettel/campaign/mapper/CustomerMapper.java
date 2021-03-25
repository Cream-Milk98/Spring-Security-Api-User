package com.viettel.campaign.mapper;

import com.viettel.campaign.web.dto.CustomerDTO;
import com.viettel.campaign.model.ccms_full.Customer;

public class CustomerMapper extends BaseMapper<Customer, CustomerDTO> {

    @Override
    public CustomerDTO toDtoBean(Customer model) {
        CustomerDTO obj = new CustomerDTO();

        if (model != null) {
            obj = new CustomerDTO();
            obj.setUserName(model.getUserName());
            obj.setCompanyName(model.getCompanyName());
            obj.setCurrentAddress(model.getCurrentAddress());
            obj.setCustomerId(model.getCustomerId());
            obj.setCustomerImg(model.getCustomerImg());
            obj.setEmail(model.getEmail());
            obj.setGender(model.getGender());
            obj.setMobileNumber(model.getMobileNumber());
            obj.setName(model.getName());
            obj.setSiteId(model.getSiteId());
            obj.setStatus(model.getStatus().toString());
//            String additionalInfo = "";
//            if (model.getName() != null && !"".equals(model.getName())) {
//                additionalInfo += model.getName();
//            }
//            if (model.getUserName() != null && !"".equals(model.getUserName())) {
//                additionalInfo += "".equals(additionalInfo) ? model.getUserName() : " - " + model.getUserName();
//            }
//            if (model.getMobileNumber() != null && !"".equals(model.getMobileNumber())) {
//                additionalInfo += "".equals(additionalInfo) ? model.getMobileNumber() : " - " + model.getMobileNumber();
//            }
//            if (model.getEmail() != null && !"".equals(model.getEmail())) {
//                additionalInfo += "".equals(additionalInfo) ? model.getEmail() : " - " + model.getEmail();
//            }
//            obj.setAdditionalInfo(additionalInfo);
            obj.setAreaCode(model.getAreaCode());
            obj.setCustomerType(model.getCustomerType());
            obj.setDescription(model.getDescription());
        }

        return obj;
    }

    @Override
    public Customer toPersistenceBean(CustomerDTO dtoBean) {
        Customer obj = new Customer();

        if (dtoBean != null) {
            obj = new Customer();
            //obj.setCustomerId(dtoBean.getCustomerId() == null ? null : dtoBean.getCustomerId());
            obj.setCode(dtoBean.getCode());
            obj.setName(dtoBean.getName());
            obj.setDescription(dtoBean.getDescription());
            obj.setCompanyName(dtoBean.getCompanyName());
            obj.setCustomerImg(dtoBean.getCustomerImg());
            obj.setCreateDate(dtoBean.getCreateDate());
            obj.setUpdateDate(dtoBean.getUpdateDate());
            obj.setStatus(Long.parseLong(dtoBean.getStatus()));
            obj.setCreateBy(dtoBean.getCreateBy());
            obj.setUpdateBy(dtoBean.getUpdateBy());
            obj.setSiteId(dtoBean.getSiteId());
            obj.setGender(dtoBean.getGender());
            obj.setCurrentAddress(dtoBean.getCurrentAddress());
            obj.setPlaceOfBirth(dtoBean.getPlaceOfBirth());
            obj.setDateOfBirth(dtoBean.getDateOfBirth());
            obj.setMobileNumber(dtoBean.getMobileNumber());
            obj.setEmail(dtoBean.getEmail());
            obj.setUserName(dtoBean.getUserName());
            obj.setAreaCode(dtoBean.getAreaCode());
            obj.setCustomerType(dtoBean.getCustomerType());
            obj.setCallAllowed(Long.parseLong(dtoBean.getCallAllowed()));
            obj.setEmailAllowed(dtoBean.getEmailAllowed());
            obj.setSmsAllowed(dtoBean.getSmsAllowed());
            obj.setIpccStatus(dtoBean.getIpccStatus());
        }

        return obj;
    }
}
