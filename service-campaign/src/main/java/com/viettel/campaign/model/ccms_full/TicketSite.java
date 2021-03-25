package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TICKET_SITE")
@Getter
@Setter
public class TicketSite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name = "SITE_ID")
    private Long siteId;
    @Size(max = 400)
    @Column(name = "SITE_CODE")
    private String siteCode;
    @Size(max = 400)
    @Column(name = "SITE_NAME")
    private String siteName;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Size(max = 200)
    @Column(name = "CREATE_BY")
    private String createBy;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 200)
    @Column(name = "UPDATE_BY")
    private String updateBy;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Size(max = 200)
    @Column(name = "ACCOUNT_KAZOO_ID")
    private String accountKazooId;
    @Column(name = "SERVICE_PLAN_ID")
    private Long servicePlanId;
}
