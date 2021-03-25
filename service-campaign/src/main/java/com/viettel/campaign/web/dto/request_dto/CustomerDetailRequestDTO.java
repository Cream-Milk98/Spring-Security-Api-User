package com.viettel.campaign.web.dto.request_dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CustomerDetailRequestDTO {
    Long companySiteId;
    Long customerListId;
    Long customerId;
    String title;
    String type;
    String valueCombobox;
    Short valueCheckbox;
    Date valueDate;
    Long valueNumber;
    String valueText;
}
