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
campaign_customer_table as (
    select count(a.customer_id) campaignCustomer, a.customer_list_id customerListId, a.campaign_id from campaign_customer a
    where a.campaign_id = :p_campaign_id
    group by a.customer_list_id, a.campaign_id
),
customer_interactive_table as (
    select count(a.customer_id) campaignCustomerCalled, a.customer_list_id customerListId, a.campaign_id from campaign_customer a
    where a.status <> 0 and a.campaign_id = :p_campaign_id
    group by a.customer_list_id, a.campaign_id
),
customer_not_interactive_table as (
    select count(a.customer_id) cusNotInteractive, a.customer_list_id customerListId, a.campaign_id from campaign_customer a
    where a.status = 0 and a.campaign_id = :p_campaign_id and a.in_campaign_status = 1
    group by a.customer_list_id, a.campaign_id
),
data_temp as (
select  a.customer_list_id customerListId,
        a.customer_list_code customerListCode,
        a.customer_list_name customerListName,
        nvl(b.totalCustomer, 0) totalCusList,
        nvl(c.campaignCustomer, 0) totalCusCampaign,
        nvl(d.campaignCustomerCalled, 0) totalCusCalled,
        nvl(e.cusNotInteractive, 0) totalCusNotInteract
from customer_list a
left join customer_table b on a.customer_list_id = b.customerListId
left join campaign_customer_table c on a.customer_list_id = c.customerListId
left join customer_interactive_table d on a.customer_list_id = d.customerListId
left join customer_not_interactive_table e on a.customer_list_id = e.customerListId
where a.customer_list_id in (select CUSTOMER_LIST_ID from campaign_customer_id)
),
data as (
select a.*, rownum row_ from data_temp a
),
count_data as (
select count(*) totalRow from data_temp
)
select a.customerListId, a.customerListCode, a.customerListName, a.totalCusList, a.totalCusCampaign, a.totalCusCalled, a.totalCusNotInteract, totalRow from data a, count_data
where row_ >= ((:p_page_number - 1) * :p_page_size + 1) and row_ < (:p_page_number * :p_page_size + 1)
