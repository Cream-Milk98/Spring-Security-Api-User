package com.viettel.campaign.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportResponseDTO {

    private ReportStatusDTO status;
    private Object data;

    public ReportStatusDTO getStatus() {
        return status;
    }

    public void setStatus(ReportStatusDTO status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

