package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CUSTOMER")
@Getter
@Setter
public class Customer implements Serializable {

    @Id
    @GeneratedValue(generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "customer_seq", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    @Size(max = 1000)
    @Column(name = "CODE")
    private String code;
    @Size(max = 1000)
    @Column(name = "NAME")
    private String name;
    @Size(max = 2000)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 100)
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Column(name = "CUSTOMER_IMG")
    private String customerImg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "STATUS")
    private Long status;
    @Size(max = 50)
    @Column(name = "CREATE_BY")
    private String createBy;
    @Size(max = 50)
    @Column(name = "UPDATE_BY")
    private String updateBy;
    @Column(name = "SITE_ID")
    private Long siteId;
    @Column(name = "GENDER")
    private Short gender;
    @Size(max = 500)
    @Column(name = "CURRENT_ADDRESS")
    private String currentAddress;
    @Size(max = 500)
    @Column(name = "PLACE_OF_BIRTH")
    private String placeOfBirth;
    @Column(name = "DATE_OF_BIRTH")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBirth;
    @Size(max = 20)
    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;
    @Size(max = 500)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 200)
    @Column(name = "USERNAME")
    private String userName;
    @Size(max = 25)
    @Column(name = "AREA_CODE")
    private String areaCode;
    @Column(name = "CUSTOMER_TYPE")
    private Long customerType;
    @Column(name = "CALL_ALLOWED")
    private Long callAllowed;
    @Column(name = "EMAIL_ALLOWED")
    private Long emailAllowed;
    @Column(name = "SMS_ALLOWED")
    private Long smsAllowed;
    @Size(max = 100)
    @Column(name = "IPCC_STATUS")
    private String ipccStatus;
    @Size(max = 2000)
    @Column(name = "AVATAR_LINK")
    private String avatarLink;
}
