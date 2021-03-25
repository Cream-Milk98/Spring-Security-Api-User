package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author anhvd_itsol
 */

@Entity
@Table(name = "SCENARIO_QUESTION")
//@Getter
//@Setter
public class ScenarioQuestion implements Serializable {
    @Id
    @NotNull
    @GeneratedValue(generator = "SCENARIO_QES_SEQ")
    @SequenceGenerator(name = "SCENARIO_QES_SEQ", sequenceName = "SCENARIO_QES_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "SCENARIO_QUESTION_ID")
    private Long scenarioQuestionId;

    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;

    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;

    @Column(name = "SCENARIO_ID")
    private Long scenarioId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "TYPE")
    private Short type;

    @Column(name = "QUESTION")
    private String question;

    @Column(name = "ORDER_INDEX")
    private Long orderIndex;

    @Column(name = "STATUS")
    private Short status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELETE_TIME")
    private Date deleteTime;

    @Column(name = "IS_REQUIRE")
    private Short isRequire;

    @Column(name = "IS_DEFAULT")
    private Short isDefault;

    @Column(name = "ANSWER_INDEX")
    private Short answerIndex;

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
}
