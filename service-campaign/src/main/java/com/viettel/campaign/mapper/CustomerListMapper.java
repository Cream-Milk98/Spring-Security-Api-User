package com.viettel.campaign.mapper;

import com.viettel.campaign.model.ccms_full.CustomerList;
import com.viettel.campaign.web.dto.CustomerListDTO;

public class CustomerListMapper extends BaseMapper<CustomerList, CustomerListDTO> {
    @Override
    public CustomerListDTO toDtoBean(CustomerList customerList) {
        CustomerListDTO obj = new CustomerListDTO();
        if (customerList != null) {
            obj.setCreateAt(customerList.getCreateAt());
            obj.setCreateBy(customerList.getCreateBy());
            obj.setCustomerListCode(customerList.getCustomerListCode());
            obj.setCustomerListId(customerList.getCustomerListId());
            obj.setCompanySiteId(customerList.getCompanySiteId());
            obj.setCustomerListName(customerList.getCustomerListName());
            obj.setDeptCreate(customerList.getDeptCreate());
            obj.setSource(customerList.getSource());
            obj.setStatus(customerList.getStatus());
            obj.setUpdateAt(customerList.getUpdateAt());
            obj.setUpdateBy(customerList.getUpdateBy());
        }
        return obj;
    }

    @Override
    public CustomerList toPersistenceBean(CustomerListDTO dtoBean) {
        CustomerList obj = new CustomerList();
        if (dtoBean != null) {
            obj.setCreateAt(dtoBean.getCreateAt());
            obj.setCreateBy(dtoBean.getCreateBy());
            obj.setCustomerListCode(dtoBean.getCustomerListCode());
            obj.setCustomerListId(dtoBean.getCustomerListId());
            obj.setCustomerListName(dtoBean.getCustomerListName());
            obj.setCompanySiteId(dtoBean.getCompanySiteId());
            obj.setDeptCreate(dtoBean.getDeptCreate());
            obj.setSource(dtoBean.getSource());
            obj.setStatus(dtoBean.getStatus());
            obj.setUpdateAt(dtoBean.getUpdateAt());
            obj.setUpdateBy(dtoBean.getUpdateBy());
        }
        return obj;
    }
}
