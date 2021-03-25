package com.viettel.campaign.web.dto.request_dto;

import com.viettel.campaign.web.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCustomerRequestDTO extends BaseDTO {
    Long companySiteId;
    Long customerListId;
    String name;
    String mobileNumber;
    String email;
}
