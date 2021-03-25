package com.viettel.campaign.model.ccms_full;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CUSTOMIZE_FIELDS")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class CustomizeFields implements Serializable {
    @Id
    @GeneratedValue(generator = "CUSTOMIZE_FIELDS_SEQ")
    @SequenceGenerator(name = "CUSTOMIZE_FIELDS_SEQ", sequenceName = "CUSTOMIZE_FIELDS_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "CUSTOMIZE_FIELD_ID")
    private Long customizeFieldId;
    @Column(name = "SITE_ID")
    private Long siteId;
    @Column(name = "FUNCTION_CODE")
    private String functionCode;
    @Column(name = "CREATE_BY")
    private String createBy;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_BY")
    private String updateBy;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "PLACEHOLDER")
    private String placeholder;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "POSITION")
    private Long position;
    @Column(name = "REQUIRED")
    private Long required;
    @Column(name = "FIELD_OPTIONS_ID")
    private Long fieldOptionsId;
    @Column(name = "REGEXP_FOR_VALIDATION")
    private String regexpForValidation;
    @Column(name = "MAX_LENGTH")
    private Long maxLength;
    @Column(name = "MIN_LENGTH")
    private Long minLength;
    @Column(name = "MIN")
    private Long min;
    @Column(name = "MAX")
    private Long max;
    @Column(name = "ACTIVE")
    private Long active;

    public CustomizeFields() {
    }

    public CustomizeFields(String type, String title) {
//    public CustomizeFields(@NotNull String type, @NotNull String title) {
        this.type = type;
        this.title = title;
    }

    public Long getCustomizeFieldId() {
        return customizeFieldId;
    }

    public void setCustomizeFieldId(Long customizeFieldId) {
        this.customizeFieldId = customizeFieldId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Long getRequired() {
        return required;
    }

    public void setRequired(Long required) {
        this.required = required;
    }

    public Long getFieldOptionsId() {
        return fieldOptionsId;
    }

    public void setFieldOptionsId(Long fieldOptionsId) {
        this.fieldOptionsId = fieldOptionsId;
    }

    public String getRegexpForValidation() {
        return regexpForValidation;
    }

    public void setRegexpForValidation(String regexpForValidation) {
        this.regexpForValidation = regexpForValidation;
    }

    public Long getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Long maxLength) {
        this.maxLength = maxLength;
    }

    public Long getMinLength() {
        return minLength;
    }

    public void setMinLength(Long minLength) {
        this.minLength = minLength;
    }

    public Long getMin() {
        return min;
    }

    public void setMin(Long min) {
        this.min = min;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }
}
