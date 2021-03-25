SELECT
    a.USER_ID userId,
    a.USER_NAME userName,
    a.STATUS status,
    a.FULL_NAME fullName,
    a.COMPANY_SITE_ID companySiteId,
    b.FILTER_TYPE filterType,
    b.CAMPAIGN_AGENT_ID campaignAgentId
FROM VSA_USERS a
