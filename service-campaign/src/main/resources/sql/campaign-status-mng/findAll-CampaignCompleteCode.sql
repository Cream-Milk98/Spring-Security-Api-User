SELECT
    CAMPAIGN_COMPLETE_CODE_ID campaignCompleteCodeId,
    IS_RECALL isRecall ,
    CAMPAIGN_ID campaignId,
    COMPLETE_VALUE completeValue,
    COMPLETE_NAME completeName,
    COMPLETE_TYPE completeType,
    CAMPAIGN_TYPE campaignType,
    DESCRIPTION description,
    STATUS  status,
    COMPANY_SITE_ID companySiteId,
    UPDATE_BY updateBy ,
    UPDATE_AT updateAt,
    CREATE_BY createBy,
    CREATE_AT createAt,
    IS_FINISH isFinish,
    IS_LOCK isLock,
    DURATION_LOCK durationLock,
    CHANEL chanel


 FROM CAMPAIGN_COMPLETE_CODE
 where 1 = 1
  AND STATUS = 1
  and COMPANY_SITE_ID = :p_company_site_id

