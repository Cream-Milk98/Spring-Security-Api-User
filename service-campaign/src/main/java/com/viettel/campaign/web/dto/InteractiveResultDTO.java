package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author anhvd_itsol
 */

//@Getter
//@Setter
public class InteractiveResultDTO implements Serializable {
    Long campaignId;
    Long scenarioQuestionId;
    Short questionType;
    Integer questionOrderIndex;
    Long answerId;
    Integer answerOrderIndex;
    Short answerHashInput;
    Short answerStatus;
    String opinion;
    String question;
    Long contactCusId;
    String answer;
    private Long mappingQuestionId;

    public InteractiveResultDTO() {
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getScenarioQuestionId() {
        return scenarioQuestionId;
    }

    public void setScenarioQuestionId(Long scenarioQuestionId) {
        this.scenarioQuestionId = scenarioQuestionId;
    }

    public Short getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Short questionType) {
        this.questionType = questionType;
    }

    public Integer getQuestionOrderIndex() {
        return questionOrderIndex;
    }

    public void setQuestionOrderIndex(Integer questionOrderIndex) {
        this.questionOrderIndex = questionOrderIndex;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Integer getAnswerOrderIndex() {
        return answerOrderIndex;
    }

    public void setAnswerOrderIndex(Integer answerOrderIndex) {
        this.answerOrderIndex = answerOrderIndex;
    }

    public Short getAnswerHashInput() {
        return answerHashInput;
    }

    public void setAnswerHashInput(Short answerHashInput) {
        this.answerHashInput = answerHashInput;
    }

    public Short getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(Short answerStatus) {
        this.answerStatus = answerStatus;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Long getContactCusId() {
        return contactCusId;
    }

    public void setContactCusId(Long contactCusId) {
        this.contactCusId = contactCusId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getMappingQuestionId() {
        return mappingQuestionId;
    }

    public void setMappingQuestionId(Long mappingQuestionId) {
        this.mappingQuestionId = mappingQuestionId;
    }
}
