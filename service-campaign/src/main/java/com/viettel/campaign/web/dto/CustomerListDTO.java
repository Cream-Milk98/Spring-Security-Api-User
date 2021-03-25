package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CustomerListDTO extends BaseDTO {

    private Long customerListId;
//    private String customerListPK;
    private Long companySiteId;
    private String customerListCode;
    private String customerListName;
    private Short status;
    private String createBy;
    private Date createAt;
    private String updateBy;
    private Date updateAt;
    private String source;
    private String deptCreate;
    private String count;
    private Long totalCusInList;
    private Long totalCusInteract;
    private Long totalCusNotInteract;
    private Long totalCusList;
    private Long totalCusCampaign;
    private Long totalCusCalled;
    private Long totalCusActive;
    private Long totalCusLock;
    private Long totalCusDnc;
    private Long totalCusAddRemove;
    private Long totalCusFilter;
    private Integer totalRow;
}
