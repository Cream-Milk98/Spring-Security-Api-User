package com.viettel.campaign.model.ccms_full;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by gpdn-019 on 12/5/2019.
 */
@Entity
@Table(name = "USER_ROLE")
@Data
public class UserRole implements Serializable{
    @Id
    @NotNull
    @Column(name = "USER_ROLE_ID")
    @GeneratedValue(generator = "USER_ROLE_SEQ")
    @SequenceGenerator(name = "USER_ROLE_SEQ", sequenceName = "USER_ROLE_SEQ", allocationSize = 1)
    @Basic(optional = false)
    private Long userRoleId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Column(name = "UPDATE_BY")
    private String updateBy;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "USER_ID")
    private Long userId;
}
