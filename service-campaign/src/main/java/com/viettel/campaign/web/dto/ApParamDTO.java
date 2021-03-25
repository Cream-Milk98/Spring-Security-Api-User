package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApParamDTO extends BaseDTO {

    private Long apParamId;
    private String parType;
    private String parName;
    private String parValue;
    private String parCode;
    private String description;
    private Long isDelete;
    private Long isDefault;
    private Long enableEdit;
    private Long siteId;
}
