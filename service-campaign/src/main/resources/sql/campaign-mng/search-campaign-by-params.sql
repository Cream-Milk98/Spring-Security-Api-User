SELECT
    a.CAMPAIGN_ID campaignId,
    a.CAMPAIGN_CODE campaignCode,
    a.CAMPAIGN_NAME campaignName,
    a.CAMPAIGN_TYPE campaignType,
    (SELECT PAR_NAME FROM AP_PARAM WHERE STATUS = 1 AND PAR_TYPE = 'CAMPAIGN_TYPE' AND PAR_VALUE = a.CAMPAIGN_TYPE) campaignTypeName,
    a.CHANEL chanel,
    a.START_TIME startTime,
    a.END_TIME endTime,
    a.CUSTOMER_NUMBER customerNumber,
    a.STATUS status,
    a.CUSTOMER_NUMBER cusNum,
    b.SLKHThamgiaCD numOfJoinedCus,
    e.SLKHDaTuongTac numOfInteractedCus,
    c.SLKHChuaTuongTac numOfNotInteractedCus,
    d.SLKHDoNotCall_Khoa numOfLockCus,
    a.COMPANY_SITE_ID companySiteId,
    a.CONTENT content,
    a.MAX_RECALL maxRecall,
    a.RECALL_TYPE recallType,
    a.RECALL_DURATION recallDuration,
    a.CURRENT_TIME_MODE currentTimeMode,
    a.WRAPUP_TIME_CONNECT wrapupTimeConnect,
    a.WRAPUP_TIME_DISCONNECT wrapupTimeDisconnect
FROM CAMPAIGN a
LEFT JOIN (SELECT campaign_id, COUNT (*) AS SLKHThamgiaCD
                  FROM   campaign_customer cc INNER JOIN CUSTOMER cus ON cc.CUSTOMER_ID = cus.CUSTOMER_ID
                  WHERE 1 = 1 AND cc.IN_CAMPAIGN_STATUS = 1 AND cus.STATUS = 1
                  group by campaign_id) b
ON a.CAMPAIGN_ID = b.CAMPAIGN_ID
LEFT JOIN (SELECT campaign_id, COUNT (*) AS SLKHChuaTuongTac
                  FROM   campaign_customer cc INNER JOIN CUSTOMER cus ON cc.CUSTOMER_ID = cus.CUSTOMER_ID
                  WHERE 1 = 1 AND cc.STATUS = 0 AND cus.STATUS = 1
                  group by campaign_id) c
ON c.CAMPAIGN_ID = a.CAMPAIGN_ID
LEFT JOIN (SELECT cc.campaign_id, count(*) AS SLKHDoNotCall_Khoa
        FROM CAMPAIGN_CUSTOMER cc , CUSTOMER c
        WHERE cc.CUSTOMER_ID = c.CUSTOMER_ID
        AND (c.IPCC_STATUS = 'locked' or c.CALL_ALLOWED = 0) AND cc.STATUS = 1
        GROUP BY cc.CAMPAIGN_ID) d
ON d.CAMPAIGN_ID = a.CAMPAIGN_ID
LEFT JOIN (SELECT campaign_id, COUNT (*) AS SLKHDaTuongTac
                  FROM   campaign_customer cc INNER JOIN CUSTOMER cus ON cc.CUSTOMER_ID = cus.CUSTOMER_ID
                  WHERE 1 = 1 AND cc.STATUS <> 0 AND cus.STATUS = 1
                  group by campaign_id) e
ON e.CAMPAIGN_ID = a.CAMPAIGN_ID
WHERE 1 = 1
AND COMPANY_SITE_ID = :p_company_site_id
AND a.STATUS <> -1
