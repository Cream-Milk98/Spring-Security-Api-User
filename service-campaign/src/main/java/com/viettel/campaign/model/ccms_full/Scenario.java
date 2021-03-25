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
@Table(name = "SCENARIO")
@Getter
@Setter
public class Scenario implements Serializable {
    @Id
    @NotNull
    @GeneratedValue(generator = "SCENARIO_SEQ")
    @SequenceGenerator(name = "SCENARIO_SEQ", sequenceName = "SCENARIO_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "SCENARIO_ID")
    private Long scenarioId;

    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;

    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "UPDATE_BY")
    private Long updateBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
}
