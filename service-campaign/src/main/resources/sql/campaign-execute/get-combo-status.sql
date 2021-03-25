select distinct COMPLETE_VALUE apParamId,
        COMPLETE_NAME parName
from CAMPAIGN_COMPLETE_CODE
where to_char(COMPLETE_TYPE) = :p_complete_type and STATUS = 1 and COMPANY_SITE_ID = :p_company_site_id
