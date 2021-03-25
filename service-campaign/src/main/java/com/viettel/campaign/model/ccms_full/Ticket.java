package com.viettel.campaign.model.ccms_full;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "TICKET")
@XmlRootElement
@Getter
@Setter
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="ticket_seq")
    @SequenceGenerator(name="ticket_seq",sequenceName="ticket_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "TICKET_ID")
    private Long ticketId;
    @Column(name = "SOURCE_ID")
    private Long sourceId;
    @Column(name = "CHANEL_ID")
    private Long chanelId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Size(max = 200)
    @Column(name = "REF_ID")
    private String refId;
    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    @Column(name = "TICKET_STATUS")
    private Short ticketStatus;
    @Column(name = "RESPONSE_STATUS")
    private Long responseStatus;
    @Column(name = "PRIORITY_ID")
    private Short priorityId;
    @Column(name = "RESOLVE_SLA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resolveSla;
    @Column(name = "FIRST_RESPONE_SLA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstResponeSla;
    @Size(max = 400)
    @Column(name = "AGENT_PROCESS")
    private String agentProcess;
    @Size(max = 400)
    @Column(name = "AGENT_ASSIGN")
    private String agentAssign;
    @Column(name = "FIRST_RESPONE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstResponeDate;
    @Column(name = "RESOLVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resolveDate;
    @Column(name = "ASSIGN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignDate;
    @Column(name = "SECOND_RESPONE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date secondResponeDate;
    @Size(max = 20)
    @Column(name = "AGENT_ID")
    private String agentId;
    @Size(max = 20)
    @Column(name = "AGENT_ASSIGN_ID")
    private String agentAssignId;
    @Column(name = "TICKET_SLA_ID")
    private Long ticketSlaId;
    @Column(name = "SITE_ID")
    private Long siteId;
    @Column(name = "REQUEST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;
    @Column(name = "LAST_REQUEST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastRequestDate;
    @Column(name = "TICKET_TYPE_ID")
    private Long ticketTypeId;
    @Column(name = "SECOND_RESPONE_SLA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date secondResponeSla;
    @Column(name = "COOPERATE_ID")
    private Long cooperateId;
    @Column(name = "POST_TYPE_ID")
    private Long postTypeId;
    @Column(name = "USER_READ")
    private String userRead;
    @Column(name = "POST_CUSTOMER")
    private Long postCustomer;
    @Column(name = "REASON")
    private String reason;
    @Column(name = "PROCESS")
    private String process;
    @Column(name = "LAST_UPDATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

    @Column(name = "SLA_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date slaDate;
    @Column(name = "SLA_PROCESS_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date slaProcessDate;
    @Column(name = "SLA_COOPERATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date slaCooperateDate;
    @Column(name = "SLA_PROCESS_CONFIG")
    private Long slaProcessConfig;
    @Column(name = "SLA_COOPERATE_CONFIG")
    private Long slaCooperateConfig;
//    @Column(name = "COMPANY_SITE_ID")
//    private Long companySiteId;
    @Column(name = "REOPEN_DATE")
    private Date reopenDate;
    @Column(name = "COMPANY_SITE_ID")
    private Long companyId;

    @Column(name = "ASSIGN_TYPE")
    private Long assignType;

    //bo sung ignored va close reason
    @Column(name = "IGNORED")
    private Long ignored;
    @Column(name = "REASON_CAT_ITEM_ID")
    private Long reasonCatItemId;
    @Column(name = "PROBLEM_CAT_ITEM_ID")
    private Long problemCatItemId;
    @Column(name = "VIEW_TYPE")
    private Long viewType;
    @Column(name = "SUBJECT")
    private String subject;
    @Column(name = "CLASSIFY_REQUIRE_ID")
    private Long classifyRequireId;
    @Column(name = "AGENT_RECEIVE")
    private String agentReceive;

    @Lob
    @Column(name = "CONTENT_RECEIVE")
    private String contentReceive;

}

