with status_customer as (
select complete_value
from campaign_complete_code
where complete_value <> 4
    and is_finish <> 1
    and campaign_type = 1
    and company_site_id = :p_company_site_id
),
count_customer as (
select  campaign_id campaignId,
        sum(case
                when customer_list_id is null then 1
                else 0
            end) totalIndividual,
        sum(case
                when status = 0 then 1
                else 0
            end) totalNotInteractive,
        sum(case
                when status in (select * from status_customer) then 1
                else 0
            end) totalNotCall
from campaign_customer
where :p_cus_list_id is null or customer_list_id = :p_cus_list_id
group by campaign_id
)
select a.*, b.customer_number campaignCustomer
from count_customer a
left join campaign b on a.campaignId = b.campaign_id
where a.campaignId = :p_campaign_id
