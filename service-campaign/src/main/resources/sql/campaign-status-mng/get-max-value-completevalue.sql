SELECT max(to_number(COMPLETE_VALUE)) completeValue, COMPANY_SITE_ID companySiteId from CAMPAIGN_COMPLETE_CODE where COMPANY_SITE_ID = :p_site_id GROUP BY COMPANY_SITE_ID