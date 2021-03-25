package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "VSA_USERS")
@Getter
@Setter

public class VSAUsers {
    @Id
    @NotNull
    @GeneratedValue(generator = "vsa_users_renew")
    @SequenceGenerator(name = "vsa_users_renew", sequenceName = "vsa_users_renew", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "USER_TYPE_ID")
    private Long userTypeId;
    @Column(name = "CREATE_DATE")
    private Date createDate;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STAFF_CODE")
    private String staffCode;
    @Column(name = "MANAGER_ID")
    private Long managerId;
    @Column(name = "LOCATION_ID")
    private Long locationId;
    @Column(name = "DEPT_ID")
    private Long deptId;
    @Column(name = "DEPT_LEVEL")
    private String deptLevel;
    @Column(name = "POS_ID")
    private Long posId;
    @Column(name = "DEPT_NAME")
    private String deptName;
    @Column(name = "GROUP_ID")
    private Long groupId;
    @Column(name = "SITE_ID")
    private Long siteId;
    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;
    @Column(name = "AGENT_TYPE")
    private Short agentType;
    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;
    @Column(name = "FACEBOOK_ID")
    private String facebookId;
    @Column(name = "LOGIN_TYPE")
    private Short loginType;
    @Column(name = "GOOGLE_ID")
    private String googleId;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "AVAILABLE_TICKET")
    private Long availableTicket;
    @Column(name = "USER_KAZOO_ID")
    private String userKazooId;
}
