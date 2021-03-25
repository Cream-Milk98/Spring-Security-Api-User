package com.viettel.campaign.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldsToShowDTO implements Serializable {

    private Long id;

    private String columnName;

    private String columnTitle;

    private Boolean isFix;

    private Long customizeFieldId;
}
