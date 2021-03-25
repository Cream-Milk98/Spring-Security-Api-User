with contact as (
    select customer_id, contact
    from (
        select * from customer_contact
        where status = 1
            and contact_type = 5
            and is_direct_line = 1
        order by create_date desc
    )
    where rownum = 1
),
connect_status as (
    select complete_value, complete_name
    from campaign_complete_code
    where company_site_id = :p_company_site_id
        and complete_type = 1
        and complete_value <> 1
),
connect_status_list as (
    select trim (regexp_substr(:p_list_connect_status, '[^,]+', 1, level)) connect_status
    from dual
    connect by level <= regexp_count(:p_list_connect_status, ',') +1
),
data as (
    select  a.campaign_customer_id campaignCustomerId,
            b.name customerName,
            c.contact mobileNumber,
            to_char(a.call_time, 'DD/MM/YYYY HH24:MI:SS') connectTime,
            d.complete_name connectStatus
    from campaign_customer a
    left join customer b on a.customer_id = b.customer_id
    left join contact c on a.customer_id = c.customer_id
    left join connect_status d on d.complete_value = a.status
    where a.campaign_id = :p_campaign_id
        and a.in_campaign_status = 1
        and a.status in (select connect_status from connect_status_list)
    order by connectTime desc, customerName
),
final_data as (
    select a.*, rownum row_ from data a
)
select * from final_data
where row_ >= ((:p_page_number - 1) * :p_page_size + 1)
    and row_ < (:p_page_number * :p_page_size + 1)
