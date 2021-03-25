package com.viettel.campaign.model.ccms_full;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author hanv_itsol
 * @project campaign
 */
@Entity
@Table(name = "CUSTOMER_TIME")
@Getter
@Setter
public class CustomerTime {

    @Id
    @GeneratedValue(generator = "CUSTOMER_TIME_SEQ")
    @SequenceGenerator(name = "CUSTOMER_TIME_SEQ", sequenceName = "CUSTOMER_TIME_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTOMER_TIME_ID")
    private Long customerTimeId;

    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "START_TIME")
    private Date startTime;

    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "STATUS")
    private Short status;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    @Column(name = "CREATE_BY")
    private Long createBy;

    @Column(name = "UPDATE_BY")
    private Long updateBy;

    @Column(name = "CONTACT_CUST_RESULT_ID")
    private Long contactCustResultId;
}
