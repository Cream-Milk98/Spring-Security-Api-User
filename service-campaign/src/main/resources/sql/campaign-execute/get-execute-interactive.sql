with campaign_type_list as (
    select trim (regexp_substr(:p_list_compaign_type, '[^,]+', 1, level)) campaign_type
    from dual
    connect by level <= regexp_count(:p_list_compaign_type, ',') +1
),
contact_status_list as (
select trim (regexp_substr(:p_list_contact_status, '[^,]+', 1, level)) contact_status
from dual
connect by level <= regexp_count(:p_list_contact_status, ',') +1
),
survey_status_list as (
select trim (regexp_substr(:p_list_survey_status, '[^,]+', 1, level)) survey_status
from dual
connect by level <= regexp_count(:p_list_survey_status, ',') +1
),
record_status_list as (
select trim (regexp_substr(:p_list_record_status, '[^,]+', 1, level)) record_status
from dual
connect by level <= regexp_count(:p_list_record_status, ',') +1
),
campaign_id_list as (
select trim (regexp_substr(:p_list_campaign_id, '[^,]+', 1, level)) campaign_id
from dual
connect by level <= regexp_count(:p_list_campaign_id, ',') +1
),
data_temp as (
select  b.campaign_code campaignCode,
        b.campaign_name campaignName,
        c.user_name userName,
        a.phone_number phoneNumber,
        d.name customerName,
        to_date(a.start_call, 'DD/MM/YYYY') startCall,
        e.complete_name contactStatus,
        f.complete_name surveyStatus,
        g.status status,
        a.status recordStatus,
        (a.end_time - a.start_call)*24*60 callTime
from contact_cust_result a
    left join campaign b on a.campaign_id = b.campaign_id
    left join vsa_users c on a.agent_id = c.user_id
    left join customer d on a.customer_id = d.customer_id
    left join campaign_complete_code e on a.contact_status = e.complete_value
    left join campaign_complete_code f on a.call_status = e.complete_value
    left join campaign g on a.campaign_id = g.campaign_id
where a.status <> 0
    and a.company_site_id = :p_company_site_id
    and a.create_time >= to_date(:p_date_from, 'DD/MM/YYYY')
    and a.create_time <= to_date(:p_date_to, 'DD/MM/YYYY')
    and a.duration_call >= :p_call_time_from
    and a.duration_call <= :p_call_time_to
    and to_char(a.customer_id) like '%'||:p_customer_id||'%'
    and b.campaign_type in (select campaign_type from campaign_type_list)
    and to_char(a.contact_status) in (select contact_status from contact_status_list)
    and to_char(a.call_status) in (select survey_status from survey_status_list)
    and to_char(a.status) in (select record_status from record_status_list)
    and (:p_phone_number is null or to_char(a.phone_number) like '%'||:p_phone_number||'%')
    and (:p_list_campaign_id is null or b.campaign_code in (select campaign_id from campaign_id_list))
    and (:p_campaign_name is null or upper(b.campaign_name) like '%'||:p_campaign_name||'%')
    and (:p_user_name is null or upper(c.user_name) like '%'||:p_user_name||'%')
),
data as (
select a.*, rownum row_ from data_temp a
),
count_data as (
select count(*) totalRow from data_temp
)
select campaignCode, campaignName, userName, phoneNumber, customerName, startCall, contactStatus, surveyStatus, status, recordStatus, callTime, totalRow from data, count_data
where :p_page_size = 0 or (row_ >= ((:p_page_number - 1) * :p_page_size + 1) and row_ < (:p_page_number * :p_page_size + 1))
