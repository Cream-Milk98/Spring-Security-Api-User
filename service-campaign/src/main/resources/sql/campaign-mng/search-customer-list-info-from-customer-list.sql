with totalCustomer as (
select clm.customer_list_id,
clm.company_site_id,
count(clm.customer_id) total
from customer_list_mapping clm
inner join customer c on clm.customer_id = c.customer_id
where c.status = 1
group by clm.customer_list_id,clm.company_site_id
), customerInteractive as (
select customer_list_id,
company_site_id,
count(customer_id) tt
from campaign_customer where campaign_id = :p_campaign_id and status <> 0
group by customer_list_id, company_site_id
), customerNotInteractive as (
select customer_list_id,
company_site_id,
count(customer_id) ktt
from campaign_customer where campaign_id = :p_campaign_id and status = 0
and in_campaign_status = 1
group by customer_list_id, company_site_id
), datas as (
select customer_list_code customerListCode,
customer_list_name customerListName,
nvl(total, 0) totalCusInList,
nvl(tt, 0) totalCusInteract,
nvl(ktt, 0) totalCusNotInteract
from customer_list cl
left join totalCustomer tc on (cl.customer_list_id = tc.customer_list_id and cl.company_site_id = tc.company_site_id)
left join customerInteractive ci on (cl.customer_list_id = ci.customer_list_id and cl.company_site_id = ci.company_site_id)
left join customerNotInteractive cni on (cl.customer_list_id = cni.customer_list_id and cl.company_site_id = cni.company_site_id)
where cl.company_site_id = :p_company_site_id
)
select * from
(
    select a.*, rownum r__
    from
    (
        select * from datas
        order by customerListCode
    ) a
    where rownum < ((:p_page_number * :p_page_size) + 1 )
)
where r__ >= (((:p_page_number-1) * :p_page_size) + 1)