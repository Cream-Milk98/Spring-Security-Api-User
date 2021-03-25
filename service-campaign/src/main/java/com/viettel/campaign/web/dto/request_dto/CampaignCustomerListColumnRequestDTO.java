package com.viettel.campaign.web.dto.request_dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CampaignCustomerListColumnRequestDTO {
    private String customizeFieldTitle;
    private String valueText;
    private Long valueNumber;
    private Date valueDate;
    private Short valueCheckbox;
    private String type;
    private String valueCombobox;
    private String checkMobileOrEmail;
    private String dynamic;
}
