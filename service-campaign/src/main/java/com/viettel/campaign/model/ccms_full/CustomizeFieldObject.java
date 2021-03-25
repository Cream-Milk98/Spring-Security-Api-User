package com.viettel.campaign.model.ccms_full;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CUSTOMIZE_FIELD_OBJECT")
@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class CustomizeFieldObject implements Serializable {
    @Id
    @GeneratedValue(generator = "customize_field_object_seq")
    @SequenceGenerator(name = "customize_field_object_seq", sequenceName = "customize_field_object_seq", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTOMIZE_FIELD_OBJECT_ID")
    private Long customizeFieldObjectId;
    @Column(name = "OBJECT_ID")
    private Long objectId;
    @Column(name = "CUSTOMIZE_FIELDS_ID")
    private Long customizeFieldId;
    @Column(name = "VALUE_TEXT")
    private String valueText;
    @Column(name = "VALUE_NUMBER")
    private Long valueNumber;
    @Column(name = "VALUE_DATE")
    private Date valueDate;
    @Column(name = "VALUE_CHECKBOX")
    private Long valueCheckbox;
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
    @Column(name = "FIELD_OPTION_VALUE_ID")
    private Long fieldOptionValueId;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "FUNCTION_CODE")
    private String functionCode;
}
