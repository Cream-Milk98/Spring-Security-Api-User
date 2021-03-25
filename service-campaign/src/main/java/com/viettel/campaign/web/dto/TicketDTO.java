package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TicketDTO extends BaseDTO {

    private Long ticketId;
    private Long sourceId;
    private Date createDate;
    private String refId;
    private Long customerId;
    private Short ticketStatus;
    private Short responseStatus;
    private Short priorityId;
    private Date resolveSla;
    private Date firstResponeSla;
    private String agentProcess;
    private Date firstResponeDate;
    private Date resolveDate;
    private Date assignDate;
    private Date secondResponeDate;
    private String agentId;
    private Long ticketSlaId;
    private Long companyId;
    private Long siteId;
    private Date requestDate;
    private Date lastRequestDate;
    private Long ticketTypeId;
    private Long ticketTypeTemp;
    private Date secondResponeSla;
    private Long cooperateId;
    private String subject;
    private String customerName;
    private String sourceName;
    private Long chanelId;
    private int overDue;
    private String fromDate;
    private String toDate;
    private String customerCode;
    private String agentAssignId;
    private String agentAssign;
    private String userLogin;
    private Long readed;
    private String emailFrom;

    private String ticketTypeName;
    private Short ticketStatusTemp;
    private Short priorityTemp;
    private Long postTypeId;
    private String postTypeName;
    private Long postCustomer;
    private Long agentAssignType;
    private String reason;
    private String process;
    private Long agentSiteId;
    private String agentRole;

    private String agentAssignTemp;
    private String agentAssignIdTemp;

    private Long siteAssignIdTemp;
    private Long siteAssign;

    private Double dateDiff;
    private String fromUser;
    private Long sendType;
    private String content;
    private Date createDateDetail;
    private String toUser;
    private String postContent;
    private Date postTime;
    private String link;
    private Long numLike;
    private Long numShare;
    private Long numView;
    private Double timeResponse;

    private String name;
    private String lastReflect;
    private String statusName;
    private String priorityName;
    private String siteName;
    private int maxRow;
    private String channelName;
    private String sendTypeName;
    private Date fromDateValue;
    private Date toDateValue;
}
