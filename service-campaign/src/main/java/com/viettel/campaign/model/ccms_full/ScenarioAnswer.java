package com.viettel.campaign.model.ccms_full;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author anhvd_itsol
 */

@Entity
@Table(name = "SCENARIO_ANSWER")
//@Getter
//@Setter
public class ScenarioAnswer {

    @Id
    @NotNull
    @GeneratedValue(generator = "SCENARIO_ANSWER_SEQ")
    @SequenceGenerator(name = "SCENARIO_ANSWER_SEQ", sequenceName = "SCENARIO_ANSWER_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "SCENARIO_ANSWER_ID")
    private Long scenarioAnswerId;

    @Column(name = "COMPANY_SITE_ID")
    private Long companySiteId;

    @Column(name = "SCENARIO_QUESTION_ID")
    private Long scenarioQuestionId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "ANSWER")
    private String answer;

    @Column(name = "ORDER_INDEX")
    private Integer orderIndex;

    @Column(name = "HAS_INPUT")
    private Short hasInput;

    @Column(name = "STATUS")
    private Short status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELETE_TIME")
    private Date deleteTime;

    @Column(name = "MAPPING_QUESTION_ID")
    private Long mappingQuestionId;

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
}
