package com.viettel.campaign.model.ccms_full;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author hanv_itsol
 * @project campaign
 */

@Entity
@Table(name = "USER_ACTION_LOG")
@Data
public class UserActionLog implements Serializable {

    @Id
    @NotNull
    @Column(name = "AGENT_ID")
    private Long agentId;

    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;

    @Column(name = "SESSION_ID")
    private String sessionId;

    @Column(name = "START_TIME")
    private Date startTime;

    @Column(name = "END_TIME")
    private Date endTime;

    @Column(name = "ACTION_TYPE")
    private Long actionType;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "OBJECT_ID")
    private  Long objectId;
}
