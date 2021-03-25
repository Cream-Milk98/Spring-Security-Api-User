package com.viettel.campaign.model.ccms_full;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author anhvd_itsol
 */

@Entity
@Table(name = "TIME_ZONE_DIAL_MODE")
@Getter
@Setter
public class TimeZoneDialMode implements Serializable{
    @Id
    @NotNull
    @GeneratedValue(generator = "TIME_ZONE_DIAL_MODE_SEQ")
    @SequenceGenerator(name = "TIME_ZONE_DIAL_MODE_SEQ", sequenceName = "TIME_ZONE_DIAL_MODE_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "TIME_ZONE_DIAL_MODE_ID")
    private Long timeZoneDialModeId;

    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;

    @Column(name = "HOUR")
    private String hour;

    @Column(name = "MINUTE")
    private String minute;

    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;

    @Column(name = "DIAL_MODE")
    private Short dialMode;

    @Column(name = "USER_ID")
    private Long userId;
}


