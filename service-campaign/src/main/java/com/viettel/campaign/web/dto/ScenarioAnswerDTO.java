package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author anhvd_itsol
 */

//@Getter
//@Setter
public class ScenarioAnswerDTO implements Serializable {
    private Long scenarioAnswerId;
    private Long companySiteId;
    private Long scenarioQuestionId;
    private String code;
    private String answer;
    private Integer orderIndex;
    private Short hasInput;
    private Short status;
    private Date createTime;
    private Date deleteTime;
    private Long mappingQuestionId;
    private Long campaignId;
    private String importQuestionCode;
    private String mappingQuestionCode;
    private String opinion;
    private boolean checked;

    public Long getScenarioAnswerId() {
        return scenarioAnswerId;
    }

    public void setScenarioAnswerId(Long scenarioAnswerId) {
        this.scenarioAnswerId = scenarioAnswerId;
    }

    public Long getCompanySiteId() {
        return companySiteId;
    }

    public void setCompanySiteId(Long companySiteId) {
        this.companySiteId = companySiteId;
    }

    public Long getScenarioQuestionId() {
        return scenarioQuestionId;
    }

    public void setScenarioQuestionId(Long scenarioQuestionId) {
        this.scenarioQuestionId = scenarioQuestionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Short getHasInput() {
        return hasInput;
    }

    public void setHasInput(Short hasInput) {
        this.hasInput = hasInput;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Long getMappingQuestionId() {
        return mappingQuestionId;
    }

    public void setMappingQuestionId(Long mappingQuestionId) {
        this.mappingQuestionId = mappingQuestionId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getImportQuestionCode() {
        return importQuestionCode;
    }

    public void setImportQuestionCode(String importQuestionCode) {
        this.importQuestionCode = importQuestionCode;
    }

    public String getMappingQuestionCode() {
        return mappingQuestionCode;
    }

    public void setMappingQuestionCode(String mappingQuestionCode) {
        this.mappingQuestionCode = mappingQuestionCode;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
