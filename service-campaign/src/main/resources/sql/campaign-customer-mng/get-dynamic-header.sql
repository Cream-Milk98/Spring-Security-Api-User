select CUSTOMIZE_FIELD_ID customizeFieldId,
SITE_ID companySiteId,
FUNCTION_CODE functionCode,
CREATE_BY createBy,
CREATE_DATE createDate,
UPDATE_BY updateBy,
UPDATE_DATE updateDate,
STATUS status,
TYPE type,
TITLE title,
PLACEHOLDER placeholder,
DESCRIPTION description,
POSITION position,
REQUIRED required,
FIELD_OPTIONS_ID fieldOptionsId,
REGEXP_FOR_VALIDATION regexpForValidation,
MAX_LENGTH maxLength,
MIN min,
MAX max,
MIN_LENGTH minLength,
ACTIVE active
from customize_fields
where function_code = 'CUSTOMER'
and site_id = :p_company_site_id