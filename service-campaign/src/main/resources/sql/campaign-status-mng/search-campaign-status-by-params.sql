SELECT
       a.CAMPAIGN_ID campaignId,
       a.COMPLETE_VALUE completeValue,
       a.COMPLETE_NAME completeName,
       a.COMPLETE_TYPE completeType,
       c.PAR_NAME campaignType,
       b.CHANEL chanel,
       a.DESCRIPTION description,
       a.STATUS  status

FROM CAMPAIGN_COMPLETE_CODE a
LEFT JOIN CAMPAIGN b ON a.CAMPAIGN_ID = b.CAMPAIGN_ID
LEFT JOIN AP_PARAM c ON a.CAMPAIGN_TYPE = to_char(c.AP_PARAM_ID);






