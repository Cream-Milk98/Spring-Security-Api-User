package com.viettel.campaign.web.dto;

import com.viettel.campaign.web.dto.request_dto.CustomerQueryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CustomizeFieldsDTO extends BaseDTO {
    private Long customizeFieldId;
    private Long siteId;
    private String functionCode;
    private String createBy;
    private Date createDate;
    private String updateBy;
    private Date updateDate;
    private Long status;
    private String type;
    private String title;
    private String placeholder;
    private String description;
    private Long position;
    private Long required;
    private Long fieldOptionsId;
    private String regexpForValidation;
    private Long maxLength;
    private Long minLength;
    private Long min;
    private Long max;
    private Long active;
    private String language;

}
