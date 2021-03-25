package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "AGENT_STATUS_STAT")
@Getter
@Setter
public class AgentStatusStat {
    @Id
    @NotNull
    @GeneratedValue(generator = "AGENT_STATUS_STAT_SEQ")
    @SequenceGenerator(name = "AGENT_STATUS_STAT_SEQ", sequenceName = "AGENT_STATUS_STAT_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "KZ_ACCOUNT_ID")
    @NotNull
    private String kzAccountId;
    @Column(name = "KZ_USER_ID")
    @NotNull
    private String kzUserId;
    @Column(name = "CURRENT_STATUS")
    @NotNull
    private String currentStatus;
    @Column(name = "TIMESTAMP")
    @NotNull
    private String timestamp;
    @Column(name = "PREVIOUS_STATUS")
    private String previousStatus;
    @Column(name = "PREVIOUS_STATUS_DURATION")
    private Long previousStatusDuration;
    @Column(name = "ID")
    @NotNull
    private Long id;
    @Column(name = "CHANNEL")
    @NotNull
    private String channel;
}
