package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author anhvd_itsol
 */
@Entity
@Table(name = "TIME_RANGE_DIAL_MODE")
@Getter
@Setter
public class TimeRangeDialMode implements Serializable {
    @Id
    @NotNull
    @GeneratedValue(generator = "TIME_RANGE_DIAL_MODE_SEQ")
    @SequenceGenerator(name = "TIME_RANGE_DIAL_MODE_SEQ", sequenceName = "TIME_RANGE_DIAL_MODE_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "TIME_RANGE_DIAL_MODE_ID")
    private Long timeRangeDialModeId;

    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_TIME")
    private Date startTime;

    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;

    @Column(name = "DIAL_MODE")
    private Short dialMode;

    @Column(name = "USER_ID")
    private Long userId;

    @Transient
    private String startTimeStr;
}
