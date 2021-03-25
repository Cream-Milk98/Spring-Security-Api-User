select
    a.COMPANY_SITE_ID companySiteId,
    a.CUSTOMER_LIST_ID customerListId,
    a.CUSTOMER_ID customerId,
    e.TITLE title,
    f.TYPE type,
    h.VALUE_COMBOBOX valueCombobox,
    e.VALUE_CHECKBOX valueCheckbox,
    e.VALUE_DATE valueDate,
    e.VALUE_NUMBER valueNumber,
    e.VALUE_TEXT valueText
from CUSTOMER_LIST_MAPPING a
         left join CUSTOMIZE_FIELD_OBJECT e on a.CUSTOMER_ID = e.OBJECT_ID
         left join CUSTOMIZE_FIELDS f on e.CUSTOMIZE_FIELDS_ID = f.CUSTOMIZE_FIELD_ID
         left join (SELECT FIELD_OPTION_VALUE_ID, NAME AS VALUE_COMBOBOX
                    FROM CUSTOMIZE_FIELD_OPTION_VALUE) h on h.FIELD_OPTION_VALUE_ID = e.FIELD_OPTION_VALUE_ID
where 1 = 1
  and a.COMPANY_SITE_ID = :p_company_site_id
  and a.CUSTOMER_LIST_ID = :p_customer_list_id
  and a.CUSTOMER_ID = :p_customer_id
