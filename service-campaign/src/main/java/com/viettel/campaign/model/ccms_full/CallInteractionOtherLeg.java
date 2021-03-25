package com.viettel.campaign.model.ccms_full;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "CALL_INTERACTION_OTHER_LEG")
public class CallInteractionOtherLeg implements Serializable {

    @Id
    @GeneratedValue(generator = "CALL_INTERACTION_OTHER_LEG_SEQ")
    @SequenceGenerator(name = "CALL_INTERACTION_OTHER_LEG_SEQ", sequenceName = "CALL_INTERACTION_OTHER_LEG_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;

    @Column(name = "INTERACTION_ID")
    private String interactionId;

    @Column(name = "LEG_CALL_ID")
    private String legCallId;

    @Column(name = "OWNER_ID")
    private String ownerId;

    @Column(name = "DIRECTION")
    private String direction;

    @Column(name = "SIP_TO")
    private String to;

    @Column(name = "REQUEST")
    private String request;

    @Column(name = "RESOURCE_TYPE")
    private String resourceType;

    @Column(name = "SIP_HANGUP_DISPOSITION")
    private String sipHangupDisposition;

    @Column(name = "CREATE_TIME")
    private Long createTime;

    @Column(name = "ANSWER_TIME")
    private Long answerTime;

    @Column(name = "DISCONNECT_TIME")
    private Long disconnectTime;

    @Column(name = "RING_DURATION")
    private Long ringDuration;

    @Column(name = "ANSWER_DURATION")
    private Long answerDuration;

    public CallInteractionOtherLeg() {
    }

    public CallInteractionOtherLeg(String interactionId, String legCallId, long createTime) {
        this.interactionId = interactionId;
        this.legCallId = legCallId;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(String interactionId) {
        this.interactionId = interactionId;
    }

    public String getLegCallId() {
        return legCallId;
    }

    public void setLegCallId(String legCallId) {
        this.legCallId = legCallId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getSipHangupDisposition() {
        return sipHangupDisposition;
    }

    public void setSipHangupDisposition(String sipHangupDisposition) {
        this.sipHangupDisposition = sipHangupDisposition;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Long answerTime) {
        this.answerTime = answerTime;
    }

    public Long getDisconnectTime() {
        return disconnectTime;
    }

    public void setDisconnectTime(Long disconnectTime) {
        this.disconnectTime = disconnectTime;
    }

    public Long getRingDuration() {
        return ringDuration;
    }

    public void setRingDuration(Long ringDuration) {
        this.ringDuration = ringDuration;
    }

    public Long getAnswerDuration() {
        return answerDuration;
    }

    public void setAnswerDuration(Long answerDuration) {
        this.answerDuration = answerDuration;
    }
}
