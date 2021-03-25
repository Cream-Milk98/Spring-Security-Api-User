package com.viettel.campaign.model.ccms_full;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "CAMPAIGN_CUSTOMERLIST_COLUMN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CampaignCustomerListColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "CAMPAIGN_CUS_LIST_COLUMN_SEQ")
    @SequenceGenerator(name = "CAMPAIGN_CUS_LIST_COLUMN_SEQ", sequenceName = "CAMPAIGN_CUS_LIST_COLUMN_SEQ", allocationSize = 1)
    @NotNull
    @Column(name = "CAMPAIGN_CUS_LIST_COLUMN_ID")
    private Long campaignCusListColId;

    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;

    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;

    @Size(max = 200)
    @Column(name = "COLUMN_NAME")
    private String columnName;

    @Column(name = "ORDER_INDEX")
    private Long orderIndex;

    @Column(name = "CUSTOMIZE_FIELD_ID")
    private Long customizeFieldId;

    @Size(max = 200)
    @Column(name = "CUSTOMIZE_FIELD_TITLE")
    private String customizeFieldTitle;
}
