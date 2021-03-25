INSERT INTO "CCMS_FULL"."USER_ACTION_LOG" (
    AGENT_ID,
    COMPANY_SITE_ID,
    SESSION_ID,
    START_TIME,
    END_TIME,
    ACTION_TYPE,
    DESCRIPTION,
    OBJECT_ID)
VALUES (
    :par_agentId,
    :par_companySiteId,
    :par_sessionId,
    :par_startTime,
    null,
    :par_actionType,
    null,
    :par_objectId)
