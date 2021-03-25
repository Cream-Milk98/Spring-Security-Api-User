select  PAR_VALUE apParamId,
        PAR_NAME parName
from AP_PARAM
where PAR_TYPE = 'CAMPAIGN_TYPE'
    and COMPANY_SITE_ID = :p_company_site_id
    and IS_DELETE = 0
