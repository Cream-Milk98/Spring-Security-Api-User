with cusPhone as (
select customer_id cusId,
contact phone
from customer_contact cc
where cc.contact_type = 5
and status = 1
), cusEmail as (
select customer_id cusId,
contact email
from customer_contact cc
where cc.contact_type = 2
and status = 1
), datas as (
select c.customer_id customerId,
c.name,
cP.phone,
cE.email,
c.customer_type cusType,
c.company_name compName,
c.current_address currentAddress,
c.description
from customer c
left join cusPhone cP on c.customer_id = cP.cusId
left join cusEmail cE on c.customer_id = cE.cusId
)
select * from
(
    select a.*, rownum r__
    from
    (
        select * from datas
        order by datas.name
    ) a
    where rownum < ((:p_page_number * :p_page_size) + 1 )
)
where r__ >= (((:p_page_number-1) * :p_page_size) + 1)