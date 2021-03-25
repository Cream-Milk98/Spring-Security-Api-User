package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "TICKET_CAT_STATUS")
@Getter
@Setter
public class TicketCatStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="ticket_cat_status_seq")
    @SequenceGenerator(name="ticket_cat_status_seq",sequenceName="ticket_cat_status_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS_ID")
    private Long statusId;
    @Size(max = 100)
    @Column(name = "STATUS_NAME")
    private String statusName;
    @Size(max = 50)
    @Column(name = "STATUS_CODE")
    private String statusCode;
    @Size(max = 100)
    @Column(name = "CREATE_BY")
    private String createBy;
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "COMPANY_ID")
    private Long companyId;
    @Column(name = "ACTIVE")
    private Short active;
}
