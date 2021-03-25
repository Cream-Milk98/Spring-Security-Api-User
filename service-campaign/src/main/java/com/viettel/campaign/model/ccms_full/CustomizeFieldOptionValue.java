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
@Table(name = "CUSTOMIZE_FIELD_OPTION_VALUE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomizeFieldOptionValue implements Serializable {
    @Id
    @Column(name = "FIELD_OPTION_VALUE_ID")
    @Basic(optional = false)
    @NotNull
    private Long fieldOptionValueId;

    @Column(name = "FIELD_OPTION_ID")
    private Long fieldOptionId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "POSITION")
    private Long position;

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
}
