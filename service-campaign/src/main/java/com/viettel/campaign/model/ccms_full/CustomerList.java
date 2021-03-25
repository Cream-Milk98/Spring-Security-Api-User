package com.viettel.campaign.model.ccms_full;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CUSTOMER_LIST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerList implements Serializable {

    @Id
    @GeneratedValue(generator = "customer_list_seq")
    @SequenceGenerator(name = "customer_list_seq", sequenceName = "customer_list_seq", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTOMER_LIST_ID")
    private Long customerListId;
    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;
    @Size(max = 200)
    @Column(name = "CUSTOMER_LIST_CODE")
    private String customerListCode;
    @Size(max = 500)
    @Column(name = "CUSTOMER_LIST_NAME")
    private String customerListName;
    @NotNull
    @Column(name = "STATUS")
    private Short status;
    @Size(max = 200)
    @Column(name = "CREATE_BY")
    private String createBy;
    @Column(name = "CREATE_AT")
    private Date createAt;
    @Size(max = 200)
    @Column(name = "UPDATE_BY")
    private String updateBy;
    @Column(name = "UPDATE_AT")
    private Date updateAt;
    @Size(max = 100)
    @Column(name = "SOURCE")
    private String source;
    @Size(max = 100)
    @Column(name = "DEPT_CREATE")
    private String deptCreate;
}
