SELECT
    CUSTOMER_LIST_ID customerListId,
    COMPANY_SITE_ID companySiteId,
    CUSTOMER_LIST_CODE customerListCode,
    CUSTOMER_LIST_NAME customerListName,
    STATUS status,
    CREATE_BY createBy,
    CREATE_AT createAt,
    UPDATE_BY updateBy,
    UPDATE_AT updateAt,
    SOURCE source,
    DEPT_CREATE deptCreate
FROM CUSTOMER_LIST
WHERE 1 = 1
  AND STATUS = 1
  AND COMPANY_SITE_ID = :p_company_site_id
