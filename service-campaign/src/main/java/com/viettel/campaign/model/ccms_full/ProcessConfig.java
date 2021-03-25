package com.viettel.campaign.model.ccms_full;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author hanv_itsol
 * @project campaign
 */

@Entity
@Table(name = "PROCESS_CONFIG")
@Data
public class ProcessConfig implements Serializable {

    @Id
    @NotNull
    @Column(name = "CONFIG_ID")
    private Long configId;

    @Column(name = "CONFIG_NAME")
    private String configName;

    @Column(name = "CONFIG_CODE")
    private String configCode;

    @Column(name = "CONFIG_VALUE")
    private String configValue;

    @Column(name = "LAST_PROCESS")
    private Date lastProcess;

    @Column(name = "COMPANY_SITE_ID")
    private  Long siteId;
}
