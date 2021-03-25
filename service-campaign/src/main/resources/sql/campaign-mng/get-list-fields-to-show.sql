with field_name as (
select a.campaign_cus_list_column_id id, to_char(a.column_name) columnName, a.order_index, a.customize_field_id customizeFieldId, 1 isFix
from campaign_customerlist_column a, dual
where a.campaign_id = :p_campaign_id
    and a.company_site_id = :p_company_site_id
    and column_name is not null
union all
select a.campaign_cus_list_column_id id, a.customize_field_title columnName, a.order_index, a.customize_field_id customizeFieldId, 0 isFix
from campaign_customerlist_column a
where a.campaign_id = :p_campaign_id
    and a.company_site_id = :p_company_site_id
)
select id, columnName, customizeFieldId, isFix from field_name where columnName is not null order by order_index
