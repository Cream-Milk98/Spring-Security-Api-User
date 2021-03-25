package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "AP_PARAM")
@Getter
@Setter
public class ApParam implements Serializable {

    @Id
    @NotNull
    @Column(name = "AP_PARAM_ID")
    private Long apParamId;

    @Column(name = "PAR_TYPE")
    private String parType;

    @Column(name = "PAR_NAME")
    private String parName;

    @Column(name = "PAR_VALUE")
    private String parValue;

    @Column(name = "PAR_CODE")
    private String parCode;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_DELETE")
    private Long isDelete;

    @Column(name = "IS_DEFAULT")
    private Long isDefault;

    @Column(name = "ENABLE_EDIT")
    private Long enableEdit;

    @Column(name = "COMPANY_SITE_ID")
    private Long siteId;
}
