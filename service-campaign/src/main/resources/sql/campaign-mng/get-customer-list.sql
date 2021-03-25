with campaign_customer_id as (
    select ccl.CUSTOMER_LIST_ID from campaign_customerlist ccl
    where ccl.campaign_id = :p_campaign_id and ccl.company_site_id = :p_company_site_id
),
customer_table as (
    select count(a.customer_id) totalCustomer, a.customer_list_id customerListId from customer_list_mapping a
    left join customer b on a.customer_id = b.customer_id
    where b.status = 1
    group by a.customer_list_id
),
customer_active_table as (
    select count(a.customer_id) customerActive, a.customer_list_id customerListId from customer_list_mapping a
    left join customer b on a.customer_id = b.customer_id
    where b.status = 1 and b.ipcc_status = 'active'
    group by a.customer_list_id
),
customer_lock_table as (
    select count(a.customer_id) customerLock, a.customer_list_id customerListId from customer_list_mapping a
    left join customer b on a.customer_id = b.customer_id
    where b.status = 1 and b.ipcc_status = 'locked'
    group by a.customer_list_id
),
customer_dnc_table as (
    select count(a.customer_id) customerDnc, a.customer_list_id customerListId from customer_list_mapping a
    left join customer b on a.customer_id = b.customer_id
    where b.status = 1 and b.call_allowed = 0
    group by a.customer_list_id
),
data_temp as (
select  a.customer_list_id customerListId,
        a.customer_list_code customerListCode,
        a.customer_list_name customerListName,
        nvl(b.totalCustomer, 0) totalCusList,
        nvl(c.customerActive, 0) totalCusActive,
        nvl(d.customerLock, 0) totalCusLock,
        nvl(e.customerDnc, 0) totalCusDnc,
        a.create_at createAt,
        a.company_site_id companySiteId,
        a.status status
from customer_list a
left join customer_table b on a.customer_list_id = b.customerListId
left join customer_active_table c on a.customer_list_id = c.customerListId
left join customer_lock_table d on a.customer_list_id = d.customerListId
left join customer_dnc_table e on a.customer_list_id = e.customerListId
where a.status = 1
    and (:p_cus_list_code is null or upper(a.customer_list_code) like '%'||:p_cus_list_code||'%')
    and (:p_cus_list_name is null or upper(a.customer_list_name) like '%'||:p_cus_list_name||'%')
    and (a.create_at <= to_date(:p_to_date, 'DD/MM/YYYY'))
    and (a.create_at >= to_date(:p_from_date, 'DD/MM/YYYY'))
    and (a.company_site_id = :p_company_site_id)
    and (a.customer_list_id not in (select CUSTOMER_LIST_ID from campaign_customer_id))
),
data as (
select a.*, rownum row_ from data_temp a
),
count_data as (
select count(*) totalRow from data_temp
)
select a.customerListId, a.customerListCode, a.customerListName, a.totalCusList, a.totalCusActive, a.totalCusLock, a.totalCusDnc, totalRow from data a, count_data
where row_ >= ((:p_page_number - 1) * :p_page_size + 1) and row_ < (:p_page_number * :p_page_size + 1)
