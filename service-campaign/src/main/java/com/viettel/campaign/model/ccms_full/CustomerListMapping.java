package com.viettel.campaign.model.ccms_full;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "CUSTOMER_LIST_MAPPING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerListMapping implements Serializable {

    @Id
    @GeneratedValue(generator = "customer_list_mapping_seq")
    @SequenceGenerator(name = "customer_list_mapping_seq", sequenceName = "customer_list_mapping_seq", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTOMER_LIST_MAPPING_ID")
    private Long customerListMappingId;
    @NotNull
    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;
    @NotNull
    @Column(name = "CUSTOMER_LIST_ID")
    private Long customerListId;
}
