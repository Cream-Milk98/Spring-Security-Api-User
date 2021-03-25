package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author anhvd_itsol
 */

@Getter
@Setter
public class TimeZoneDialModeDTO {
    private Long timeZoneDialModeId;
    private Long companySiteId;
    private String hour;
    private String minute;
    private Long campaignId;
    private Short dialMode;
    private Long userId;
    private Date dateTime;
}
