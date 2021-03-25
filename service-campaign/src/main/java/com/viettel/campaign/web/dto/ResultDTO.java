package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//@Getter
//@Setter
public class ResultDTO {
    private String errorCode;
    private String description;
    private List<?> listData = new ArrayList();
    private Object data;
    private Long totalRow = 0L;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<?> getListData() {
        return listData;
    }

    public void setListData(List<?> listData) {
        this.listData = listData;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(Long totalRow) {
        this.totalRow = totalRow;
    }
}
