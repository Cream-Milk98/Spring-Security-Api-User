package com.viettel.campaign.model.acd_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AGENTS")
@Getter
@Setter
public class Agents {
    @Id
    @Basic(optional = false)
    @Column(name = "AGENT_ID")
    private String agentId;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "SYSTEM_STATUS")
    private String systemStatus;
    @Column(name = "USER_STATUS")
    private String userStatus;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_START_WORK")
    private Date lastStartWork;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_FINISH_WORK")
    private Date lastFinishWork;
    @Column(name = "LOGIN_TYPE")
    private String loginType;
    @Column(name = "VSA_USER_LOGIN")
    private String vsaUserLogin;
    @Column(name = "CALL_STATUS")
    private String callStatus;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_CHANGE_STATUS")
    private Date lastChangeStatus;
    @Column(name = "IP_LOGIN")
    private String ipLogin;
    @Column(name = "NUM_REJECTCALL")
    private String numRejectcall;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOGIN_TIME")
    private Date loginTime;
    @Column(name = "GROUP_NAME")
    private String groupName;
    @Column(name = "TOTAL_ANSWER_CALL")
    private String totalAnswerCall;
    @Column(name = "TOTAL_ANSWER_TIME")
    private String totalAnswerTime;
    @Column(name = "CALLOUT_ID")
    private Integer calloutId;
    @Column(name = "LAST_QUEUE_ANSWER")
    private String lastQueueAnswer;
    @Column(name = "EMAIL_USER_STATUS")
    private String emailUserAnswer;
    @Column(name = "CHAT_USER_STATUS")
    private String chatUserStatus;
    @Column(name = "SMS_USER_STATUS")
    private String smsUserStatus;
    @Column(name = "MULTI_CHANNEL_USER_STATUS")
    private String multiChannelUserStatus;
    @Column(name = "MAX_TRANSACTION_EMAIL")
    private Integer maxTransactionEmail = 1;
    @Column(name = "MAX_TRANSACTION_CHAT")
    private Integer maxTransactionChat = 1;
    @Column(name = "MAX_CURRENT_TRANSACTION")
    private Integer maxCurrentTransaction = 6;
    @Column(name = "TOTAL_TRANSACTION")
    private Integer totalTransaction = 0;
    @Column(name = "EMAIL_SYSTEM_STATUS")
    private Integer emailSystemStatus = 0;
    @Column(name = "CHAT_SYSTEM_STATUS")
    private Integer chatSystemStatus = 0;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_CHANGE_CHAT_STATUS")
    private Date lastChangeChatStatus;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_CHANGE_EMAIL_STATUS")
    private Date lastChangeEmailStatus;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_CHANGE_MULTI_STATUS")
    private Date lastChangeMultiStatus;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_START_WORK_CHAT")
    private Date lastStartWorkChat;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_START_WORK_EMAIL")
    private Date lastStartWorkEmail;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_FINISH_WORK_CHAT")
    private Date lastFinishWorkChat;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_FINISH_WORK_EMAIL")
    private Date lastFinishWorkEmail;
    @Column(name = "TOTAL_ANSWER_CHAT")
    private Long totalAnswerChat;
    @Column(name = "TOTAL_ANSWER_EMAIL")
    private Long totalAnswerEmail;
    @Column(name = "TOTAL_ANSWER_TIME_EMAIL")
    private Long totalAnswerTimeEmail;
    @Column(name = "TOTAL_ANSWER_TIME_CHAT")
    private Long totalAnswerTimeChat;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_ASSIGN_TIME_SMS")
    private Date lastAssignTimeSms;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_CHANGE_SMS_STATUS")
    private Date lastChangeSmsStatus;
    @Column(name = "MAX_TRANSACTION_SMS")
    private Integer maxTransactionSms = 1;
    @Column(name = "SMS_SYSTEM_STATUS")
    private Integer smsSystemStatus = 0;
    @Column(name = "FACEBOOK_SYSTEM_STATUS")
    private Integer facebookSystemStatus = 0;
    @Column(name = "FACEBOOK_USER_STATUS")
    private String facebookUserStatus;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_START_WORK_SMS")
    private Date lastStartWorkSms;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_START_WORK_FACEBOOK")
    private Date lastStartWorkFacebook;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_FINISH_WORK_SMS")
    private Date lastFinishWorkSms;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_FINISH_WORK_FACEBOOK")
    private Date lastFinishWorkFacebook;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_CHANGE_FACEBOOK_STATUS")
    private Date lastChangeFacebookStatus;
    @Column(name = "MAX_TRANSACTION_FACEBOOK")
    private Integer maxTransactionFacebook = 1;
    @Column(name = "TICKET_USER_STATUS")
    private String ticketUserStatus;
    @Column(name = "TICKET_SYSTEM_STATUS")
    private Integer ticketSystemStatus;
    @Column(name = "MAX_TRANSACTION_TICKET")
    private Integer maxTransactionTicket;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_START_WORK_TICKET")
    private Date lastStartWorkTicket;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_FINISH_WORK_TICKET")
    private Date lastFinishWorkTicket;
    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;
    @Column(name = "SITE_ID")
    private Long siteId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_CHANGE_TICKET_STATUS")
    private Date lastChangeTicketStatus;
    @Column(name = "AGENT_TYPE")
    private Short agentType;
    @Column(name = "STATUS")
    private Long status = 1L;
    @Column(name = "CREATE_BY")
    private String createBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    private Date createDate;
    @Column(name = "UPDATE_BY")
    private String updateBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_DATE")
    private Date updateDate;
    @Column(name = "USER_KAZOO_ID")
    private String userKazooId;
    @Column(name = "CAMPAIGN_SYSTEM_STATUS")
    private String campaignSystemStatus;
    @Column(name = "CURRENT_CAMPAIGN_ID")
    private Long currentCampaignId;
}
