package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author anhvd_itsol
 */

//@Getter
//@Setter
public class ScenarioDTO {
    private Long scenarioId;
    private Long companySiteId;
    private Long campaignId;
    private String code;
    private String description;
    private String createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
    private List<ScenarioQuestionDTO> lstQuestion;
    private List<ScenarioAnswerDTO> lstAnswer;

    public Long getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(Long scenarioId) {
        this.scenarioId = scenarioId;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<ScenarioQuestionDTO> getLstQuestion() {
        return lstQuestion;
    }

    public void setLstQuestion(List<ScenarioQuestionDTO> lstQuestion) {
        this.lstQuestion = lstQuestion;
    }

    public List<ScenarioAnswerDTO> getLstAnswer() {
        return lstAnswer;
    }

    public void setLstAnswer(List<ScenarioAnswerDTO> lstAnswer) {
        this.lstAnswer = lstAnswer;
    }
}

