with column_name_temp as (
    select column_name columnName, null customizeFieldId, 1 isFix from user_tab_columns, dual
    where table_name = 'CUSTOMER'
)
select * from column_name_temp where columnName not in (select column_name from campaign_customerlist_column where column_name is not null)
union all
select title columnName, customize_field_id customizeFieldId, 0 isFix from customize_fields, dual
where function_code = 'CUSTOMER'
    and site_id = :p_company_site_id
    and customize_field_id not in (select customize_field_id from campaign_customerlist_column where campaign_customerlist_column.campaign_id = :p_campaign_id)
