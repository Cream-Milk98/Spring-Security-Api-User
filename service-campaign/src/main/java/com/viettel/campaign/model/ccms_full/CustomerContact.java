package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CUSTOMER_CONTACT")
@Getter
@Setter
public class CustomerContact implements Serializable {
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "customer_contact_seq")
    @SequenceGenerator(name = "customer_contact_seq", sequenceName = "customer_contact_seq", allocationSize = 1)
    @NotNull
    @Column(name = "CONTACT_ID")
    private Long contactId;
    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    @Column(name = "CONTACT_TYPE")
    private Short contactType;
    @Column(name = "CONTACT")
    private String contact;
    @Column(name = "IS_DIRECT_LINE")
    private Short isDirectLine;
    @Column(name = "STATUS")
    private Short status;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "CREATE_BY")
    private String createBy;
    @Column(name = "UPDATE_BY")
    private String updateBy;
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "SITE_ID")
    private Long siteId;
}
