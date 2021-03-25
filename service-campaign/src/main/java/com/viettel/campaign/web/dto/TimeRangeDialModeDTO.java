package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author anhvd_itsol
 */

@Getter
@Setter
public class TimeRangeDialModeDTO {
    private Long timeRangeDialModeId;
    private Long companySiteId;
    private Date startTime;
    private Long campaignId;
    private Short dialMode;
    private Long userId;
}
