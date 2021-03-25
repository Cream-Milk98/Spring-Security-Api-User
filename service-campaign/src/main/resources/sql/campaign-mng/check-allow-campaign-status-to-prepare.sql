WITH COUNT_LIST AS (SELECT (SELECT COUNT(1) FROM CAMPAIGN_CUSTOMER WHERE CAMPAIGN_ID = :p_campaignId) countCamp,
                           (SELECT COUNT(1) FROM CAMPAIGN_AGENT WHERE CAMPAIGN_ID = :p_campaignId)    countAgent,
                           (select COUNT(1)
                            FROM SCENARIO s
                                     INNER JOIN SCENARIO_QUESTION sq ON s.SCENARIO_ID = sq.SCENARIO_ID
                                     INNER JOIN SCENARIO_ANSWER sa ON sq.SCENARIO_QUESTION_ID = sa.SCENARIO_QUESTION_ID
                            WHERE s.CAMPAIGN_ID = :p_campaignId)                                      countScenario
                    FROM DUAL)

SELECT c.countCamp          countCamp,
       c.countAgent         countAgent,
       c.countScenario      countScenario,
       CASE
           WHEN (c.countCamp IS NOT NULL AND c.countAgent IS NOT NULL AND c.countScenario IS NOT NULL) THEN '00'
           ELSE '01' END as code
FROM COUNT_LIST c
