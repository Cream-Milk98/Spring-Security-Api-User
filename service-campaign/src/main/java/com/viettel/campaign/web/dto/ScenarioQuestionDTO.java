package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author anhvd_itsol
 */

//@Getter
//@Setter
public class ScenarioQuestionDTO implements Serializable {
    private Long scenarioQuestionId;
    private Long companySiteId;
    private Long campaignId;
    private Long scenarioId;
    private String code;
    private Short type;
    private String question;
    private Long orderIndex;
    private Short status;
    private Date createTime;
    private Date deleteTime;
    private Short isRequire;
    private Short isDefault;
    private Short answerIndex;
    private List<ScenarioAnswerDTO> lstAnswers;
    private String importCode;
    private String opinion;
    private Long scenarioAnswerId;
    private Short typeMap;

    public Long getScenarioQuestionId() {
        return scenarioQuestionId;
    }

    public void setScenarioQuestionId(Long scenarioQuestionId) {
        this.scenarioQuestionId = scenarioQuestionId;
    }

    public Long getCompanySiteId() {
        return companySiteId;
    }

    public void setCompanySiteId(Long companySiteId) {
        this.companySiteId = companySiteId;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(Long scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
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

    public Short getIsRequire() {
        return isRequire;
    }

    public void setIsRequire(Short isRequire) {
        this.isRequire = isRequire;
    }

    public Short getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Short isDefault) {
        this.isDefault = isDefault;
    }

    public Short getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(Short answerIndex) {
        this.answerIndex = answerIndex;
    }

    public List<ScenarioAnswerDTO> getLstAnswers() {
        return lstAnswers;
    }

    public void setLstAnswers(List<ScenarioAnswerDTO> lstAnswers) {
        this.lstAnswers = lstAnswers;
    }

    public String getImportCode() {
        return importCode;
    }

    public void setImportCode(String importCode) {
        this.importCode = importCode;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Long getScenarioAnswerId() {
        return scenarioAnswerId;
    }

    public void setScenarioAnswerId(Long scenarioAnswerId) {
        this.scenarioAnswerId = scenarioAnswerId;
    }

    public Short getTypeMap() {
        return typeMap;
    }

    public void setTypeMap(Short typeMap) {
        this.typeMap = typeMap;
    }
}
