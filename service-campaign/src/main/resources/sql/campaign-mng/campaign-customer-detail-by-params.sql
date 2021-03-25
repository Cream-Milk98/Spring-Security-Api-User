select
    a.CUSTOMER_LIST_MAPPING_ID customerListMappingId,
    a.COMPANY_SITE_ID companySiteId,
    a.CUSTOMER_LIST_ID customerListId,
    a.CUSTOMER_ID customerId,
    b.NAME name,
    b.DESCRIPTION description,
    b.COMPANY_NAME companyName,
    b.CUSTOMER_TYPE customerType,
    b.CURRENT_ADDRESS currentAddress,
    b.CALL_ALLOWED callAllowed,
    b.EMAIL_ALLOWED emailAllowed,
    b.SMS_ALLOWED smsAllowed,
    b.IPCC_STATUS ipccStatus,
    c.MOBILE mobileNumber,
    d.EMAIL email
 from CUSTOMER_LIST_MAPPING a
         join CUSTOMER b on a.CUSTOMER_ID = b.CUSTOMER_ID
         left join (SELECT CUSTOMER_ID, LISTAGG(CONTACT, ', ') WITHIN GROUP (ORDER BY NULL) AS MOBILE
               FROM CUSTOMER_CONTACT WHERE CONTACT_TYPE = 5 GROUP BY CUSTOMER_ID) c on b.CUSTOMER_ID = c.CUSTOMER_ID
         left join (SELECT CUSTOMER_ID, LISTAGG(CONTACT, ', ') WITHIN GROUP (ORDER BY NULL) AS EMAIL
               FROM CUSTOMER_CONTACT WHERE CONTACT_TYPE = 2 GROUP BY CUSTOMER_ID) d on b.CUSTOMER_ID = d.CUSTOMER_ID
 where 1 = 1
   and a.COMPANY_SITE_ID = :p_company_site_id
   and a.CUSTOMER_LIST_ID = :p_customer_list_id
