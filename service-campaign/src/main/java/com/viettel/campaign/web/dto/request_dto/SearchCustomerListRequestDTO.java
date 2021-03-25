package com.viettel.campaign.web.dto.request_dto;

import com.viettel.campaign.web.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCustomerListRequestDTO extends BaseDTO {
    String customerListCode;
    String customerListName;
    String convertedDateFrom;
    String convertedDateTo;
    String companySiteId;
    Long timezoneOffset;
}
