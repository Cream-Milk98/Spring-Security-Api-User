package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.repository.ccms_full.CampaignCustomerRepository;
import com.viettel.campaign.repository.ccms_full.CustomerListMappingRepository;
import com.viettel.campaign.repository.ccms_full.CustomerRepository;
import com.viettel.campaign.repository.ccms_full.CustomerRepositoryCustom;
import com.viettel.campaign.utils.DataUtil;
import com.viettel.campaign.utils.DateTimeUtil;
import com.viettel.campaign.utils.HibernateUtil;
import com.viettel.campaign.utils.TimeZoneUtils;
import com.viettel.campaign.web.dto.*;
import com.viettel.campaign.web.dto.request_dto.CustomerDetailRequestDTO;
import com.viettel.campaign.web.dto.request_dto.CustomerQueryDTO;
import com.viettel.campaign.web.dto.request_dto.SearchCustomerRequestDTO;
import com.viettel.econtact.filter.UserSession;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    private Logger logger = LoggerFactory.getLogger(CustomerRepositoryImpl.class);

    @Autowired
    @Qualifier(DataSourceQualify.NAMED_JDBC_PARAMETER_TEMPLATE_CCMS_FULL)
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    CustomerListMappingRepository customerListMappingRepository;

    @Autowired
    CampaignCustomerRepository campaignCustomerRepository;

    @Override
    public List<CustomerDetailRequestDTO> getCustomerDetailById(Long companySiteId, Long customerId, Long timezoneOffset) {
        List<CustomerDetailRequestDTO> data = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            StringBuilder sb = new StringBuilder();

            sb.append("select distinct a.CUSTOMER_ID     customerId,");
            sb.append("    a.SITE_ID companySiteId,");
            sb.append("    f.TITLE title,");
            sb.append("    f.TYPE type,");
            sb.append("    h.NAME valueCombobox,");
            sb.append("    e.VALUE_CHECKBOX valueCheckbox,");
            sb.append("    (e.VALUE_DATE + (:p_timezone_offset / 60)/24) valueDate,");
            sb.append("    e.VALUE_NUMBER valueNumber,");
            sb.append("    e.VALUE_TEXT valueText");
            sb.append(" from CUSTOMER a");
            sb.append("         inner join CUSTOMIZE_FIELDS f on a.SITE_ID = f.SITE_ID and f.STATUS = 1 and f.ACTIVE = 1");
            sb.append("         left join CUSTOMIZE_FIELD_OBJECT e on f.CUSTOMIZE_FIELD_ID = e.CUSTOMIZE_FIELDS_ID and a.CUSTOMER_ID = e.OBJECT_ID and e.FUNCTION_CODE = 'CUSTOMER'");
            sb.append("         left join CUSTOMIZE_FIELD_OPTION_VALUE h on h.FIELD_OPTION_VALUE_ID = e.FIELD_OPTION_VALUE_ID");
            sb.append(" where 1 = 1");
            sb.append("  and a.SITE_ID = :p_company_site_id");
            sb.append("  and a.CUSTOMER_ID = :p_customer_id");

            SQLQuery query = session.createSQLQuery(sb.toString());


            query.setParameter("p_company_site_id", companySiteId);
            query.setParameter("p_customer_id", customerId);
            query.setParameter("p_timezone_offset", timezoneOffset);

            query.addScalar("companySiteId", new LongType());
            query.addScalar("customerId", new LongType());
            query.addScalar("title", new StringType());
            query.addScalar("type", new StringType());
            query.addScalar("valueCombobox", new StringType());
            query.addScalar("valueCheckbox", new ShortType());
            query.addScalar("valueDate", new DateType());
            query.addScalar("valueNumber", new LongType());
            query.addScalar("valueText", new StringType());

            query.setResultTransformer(Transformers.aliasToBean(CustomerDetailRequestDTO.class));

            data = query.list();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (session != null) session.close();
        }

        return data;
    }

    private int getSizeCustomerList(SearchCustomerRequestDTO searchCustomerRequestDTO) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        int total = 0;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            StringBuilder sb = new StringBuilder();

            sb.append("SELECT COUNT(*) ");
            sb.append("FROM CUSTOMER_LIST_MAPPING a");
            sb.append("         join CUSTOMER b on a.CUSTOMER_ID = b.CUSTOMER_ID");
            sb.append("         left join (SELECT CUSTOMER_ID, LISTAGG(CONTACT, ', ') WITHIN GROUP (ORDER BY NULL) AS MOBILE");
            sb.append("               FROM CUSTOMER_CONTACT WHERE CONTACT_TYPE = 5 AND STATUS = 1 GROUP BY CUSTOMER_ID) c on b.CUSTOMER_ID = c.CUSTOMER_ID");
            sb.append("         left join (SELECT CUSTOMER_ID, LISTAGG(CONTACT, ', ') WITHIN GROUP (ORDER BY NULL) AS EMAIL");
            sb.append("               FROM CUSTOMER_CONTACT WHERE CONTACT_TYPE = 2 AND STATUS = 1 GROUP BY CUSTOMER_ID) d on b.CUSTOMER_ID = d.CUSTOMER_ID");
            sb.append(" WHERE 1 = 1 ");
            sb.append("AND COMPANY_SITE_ID = :p_company_site_id ");
            sb.append("AND CUSTOMER_LIST_ID = :p_customer_list_id ");

            if (!DataUtil.isNullOrEmpty(searchCustomerRequestDTO.getName())) {
                sb.append("AND UPPER(b.NAME) LIKE UPPER(:p_name) ");
            }
            if (!DataUtil.isNullOrEmpty(searchCustomerRequestDTO.getMobileNumber())) {
                sb.append("AND UPPER(c.MOBILE) LIKE UPPER(:p_mobile_number) ");
            }
            if (!DataUtil.isNullOrEmpty(searchCustomerRequestDTO.getEmail())) {
                sb.append("AND UPPER(d.EMAIL) LIKE UPPER(:p_email) ");
            }

            SQLQuery query = session.createSQLQuery(sb.toString());

            query.setParameter("p_company_site_id", searchCustomerRequestDTO.getCompanySiteId());
            query.setParameter("p_customer_list_id", searchCustomerRequestDTO.getCustomerListId());

            if (!DataUtil.isNullOrEmpty(searchCustomerRequestDTO.getName())) {
                query.setParameter("p_name", "%" +
                        searchCustomerRequestDTO.getName().trim().replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            if (!DataUtil.isNullOrEmpty(searchCustomerRequestDTO.getMobileNumber())) {
                query.setParameter("p_mobile_number", "%" +
                        searchCustomerRequestDTO.getMobileNumber().trim().replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            if (!DataUtil.isNullOrEmpty(searchCustomerRequestDTO.getEmail())) {
                query.setParameter("p_email", "%" +
                        searchCustomerRequestDTO.getEmail().trim().replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            //query.setResultTransformer(Transformers.aliasToBean(CustomerCustomDTO.class));

            total = DataUtil.safeToInt(query.uniqueResult());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (session != null) session.close();
        }

        return total;
    }

    @Override
    public Page<CustomerCustomDTO> getAllCustomerByParams(SearchCustomerRequestDTO searchCustomerRequestDTO, Pageable pageable) {
        List<CustomerCustomDTO> data = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Page<CustomerCustomDTO> dataPage = new PageImpl<>(data, pageable, 0);
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            StringBuilder sb = new StringBuilder();

            sb.append("SELECT ct.* FROM (");
            sb.append("select");
            sb.append("    a.CUSTOMER_LIST_MAPPING_ID customerListMappingId,");
            sb.append("    a.COMPANY_SITE_ID companySiteId,");
            sb.append("    a.CUSTOMER_LIST_ID customerListId,");
            sb.append("    a.CUSTOMER_ID customerId,");
            sb.append("    b.NAME name,");
            sb.append("    b.DESCRIPTION description,");
            sb.append("    b.COMPANY_NAME companyName,");
            sb.append("    b.CUSTOMER_TYPE customerType,");
            sb.append("    b.CURRENT_ADDRESS currentAddress,");
            sb.append("    b.CALL_ALLOWED callAllowed,");
            sb.append("    b.EMAIL_ALLOWED emailAllowed,");
            sb.append("    b.SMS_ALLOWED smsAllowed,");
            sb.append("    b.IPCC_STATUS ipccStatus,");
            sb.append("    c.MOBILE mobileNumber,");
            sb.append("    d.EMAIL email");
            sb.append(" from CUSTOMER_LIST_MAPPING a");
            sb.append("         join CUSTOMER b on a.CUSTOMER_ID = b.CUSTOMER_ID");
            sb.append("         left join (SELECT CUSTOMER_ID, LISTAGG(CONTACT, ', ') WITHIN GROUP (ORDER BY NULL) AS MOBILE");
            sb.append("               FROM CUSTOMER_CONTACT WHERE CONTACT_TYPE = 5 AND STATUS = 1 GROUP BY CUSTOMER_ID) c on b.CUSTOMER_ID = c.CUSTOMER_ID");
            sb.append("         left join (SELECT CUSTOMER_ID, LISTAGG(CONTACT, ', ') WITHIN GROUP (ORDER BY NULL) AS EMAIL");
            sb.append("               FROM CUSTOMER_CONTACT WHERE CONTACT_TYPE = 2 AND STATUS = 1 GROUP BY CUSTOMER_ID) d on b.CUSTOMER_ID = d.CUSTOMER_ID");
            sb.append(" where 1 = 1 and b.status = 1 ");
            sb.append("   and a.COMPANY_SITE_ID = :p_company_site_id");
            sb.append("   and a.CUSTOMER_LIST_ID = :p_customer_list_id");

            if (!DataUtil.isNullOrEmpty(searchCustomerRequestDTO.getName())) {
                sb.append(" AND UPPER(b.NAME) LIKE UPPER(:p_name)");
            }
            if (!DataUtil.isNullOrEmpty(searchCustomerRequestDTO.getMobileNumber())) {
                sb.append(" AND UPPER(c.MOBILE) LIKE UPPER(:p_mobile_number)");
            }
            if (!DataUtil.isNullOrEmpty(searchCustomerRequestDTO.getEmail())) {
                sb.append(" AND UPPER(d.EMAIL) LIKE UPPER(:p_email)");
            }
            sb.append(") ct ORDER BY NLSSORT(ct.name, 'NLS_SORT = Vietnamese')");

            SQLQuery query = session.createSQLQuery(sb.toString());

            query.setParameter("p_company_site_id", searchCustomerRequestDTO.getCompanySiteId());
            query.setParameter("p_customer_list_id", searchCustomerRequestDTO.getCustomerListId());

            if (!DataUtil.isNullOrEmpty(searchCustomerRequestDTO.getName())) {
                query.setParameter("p_name", "%" +
                        searchCustomerRequestDTO.getName().trim().replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            if (!DataUtil.isNullOrEmpty(searchCustomerRequestDTO.getMobileNumber())) {
                query.setParameter("p_mobile_number", "%" +
                        searchCustomerRequestDTO.getMobileNumber().trim().replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            if (!DataUtil.isNullOrEmpty(searchCustomerRequestDTO.getEmail())) {
                query.setParameter("p_email", "%" +
                        searchCustomerRequestDTO.getEmail().trim().replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            query.addScalar("customerListMappingId", new LongType());
            query.addScalar("companySiteId", new LongType());
            query.addScalar("customerListId", new LongType());
            query.addScalar("customerId", new LongType());
            query.addScalar("name", new StringType());
            query.addScalar("description", new StringType());
            query.addScalar("companyName", new StringType());
            query.addScalar("customerType", new StringType());
            query.addScalar("currentAddress", new StringType());
            query.addScalar("mobileNumber", new StringType());
            query.addScalar("email", new StringType());
            query.addScalar("ipccStatus", new StringType());
            query.addScalar("smsAllowed", new ShortType());
            query.addScalar("emailAllowed", new ShortType());
            query.addScalar("callAllowed", new ShortType());

            query.setResultTransformer(Transformers.aliasToBean(CustomerCustomDTO.class));

            int total = 0;
//            total = query.list().size();
//            total = customerListMappingRepository.countAllByCompanySiteIdAndCustomerListId(searchCustomerRequestDTO.getCompanySiteId(), searchCustomerRequestDTO.getCustomerListId());
            total = getSizeCustomerList(searchCustomerRequestDTO);
            if (total > 0) {
                if (pageable != null) {
                    query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                    query.setMaxResults(pageable.getPageSize());
                }

                data = query.list();

                dataPage = new PageImpl<>(data, pageable, total);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (session != null) session.close();
        }

        return dataPage;
    }

    @Override
    public List<CampaignInformationDTO> getCampaignInformation(CampaignCustomerDTO campaignCustomerDTO) {
        List<CampaignInformationDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(" with count_customer as (");
            sb.append(" select  campaign_id campaignId,");
            sb.append("         sum(case");
            sb.append("                 when customer_list_id is null then 1");
            sb.append("                     else 0");
            sb.append("                 end) totalIndividual,");
            sb.append("         sum(case");
            sb.append("                 when customer_list_id is not null then 1");
            sb.append("                     else 0");
            sb.append("         end) totalCusInList");
            sb.append(" from campaign_customer");
            sb.append(" group by campaign_id");
            sb.append(" )");
            sb.append(" select b.campaign_id campaignId, nvl(totalIndividual, 0) totalIndividual, nvl(totalCusInList, 0) totalCusInList, b.customer_number campaignCustomer");
            sb.append(" from campaign b");
            sb.append(" left join count_customer a on a.campaignId = b.campaign_id");
            sb.append(" where b.campaign_id = :p_campaign_id");
            params.put("p_campaign_id", campaignCustomerDTO.getCampaignId());
            params.put("p_company_site_id", campaignCustomerDTO.getCompanySiteId());
            list = namedParameterJdbcTemplate.query(sb.toString(), params, BeanPropertyRowMapper.newInstance(CampaignInformationDTO.class));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Page<CustomerCustomDTO> getIndividualCustomerInfo(CampaignCustomerDTO campaignCustomerDTO, Pageable pageable) {
        List<CustomerCustomDTO> data = new ArrayList<>();
        int total;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Page<CustomerCustomDTO> dataPage = new PageImpl<>(data, pageable, 0);
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            StringBuilder sb = new StringBuilder();

            sb.append("SELECT ct.* FROM (");
            sb.append("select distinct a.CUSTOMER_ID     customerId,");
            sb.append("    a.COMPANY_SITE_ID companySiteId,");
            sb.append("    a.CAMPAIGN_CUSTOMER_ID campaignCustomerId,");
            sb.append("    b.NAME name,");
            sb.append("    b.DESCRIPTION description,");
            sb.append("    b.COMPANY_NAME companyName,");
            sb.append("    b.CUSTOMER_TYPE customerType,");
            sb.append("    b.CURRENT_ADDRESS currentAddress,");
            sb.append("    b.CALL_ALLOWED callAllowed,");
            sb.append("    b.EMAIL_ALLOWED emailAllowed,");
            sb.append("    b.SMS_ALLOWED smsAllowed,");
            sb.append("    b.IPCC_STATUS ipccStatus,");
            sb.append("    c.MOBILE mobileNumber,");
            sb.append("    d.EMAIL email");
            sb.append(" from CAMPAIGN_CUSTOMER a");
            sb.append("         join CUSTOMER b on a.CUSTOMER_ID = b.CUSTOMER_ID");
            sb.append("         left join (SELECT CUSTOMER_ID, LISTAGG(CONTACT, ', ') WITHIN GROUP (ORDER BY NULL) AS MOBILE");
            sb.append("               FROM CUSTOMER_CONTACT WHERE CONTACT_TYPE = 5 AND STATUS = 1 GROUP BY CUSTOMER_ID) c on b.CUSTOMER_ID = c.CUSTOMER_ID");
            sb.append("         left join (SELECT CUSTOMER_ID, LISTAGG(CONTACT, ', ') WITHIN GROUP (ORDER BY NULL) AS EMAIL");
            sb.append("               FROM CUSTOMER_CONTACT WHERE CONTACT_TYPE = 2 AND STATUS = 1 GROUP BY CUSTOMER_ID) d on b.CUSTOMER_ID = d.CUSTOMER_ID");
            sb.append(" where 1 = 1");
            sb.append("   and a.COMPANY_SITE_ID = :p_company_site_id");
            sb.append("   and a.CAMPAIGN_ID = :p_campaign_id");
            sb.append("   and a.IN_CAMPAIGN_STATUS = 1");
            sb.append("   and a.CUSTOMER_LIST_ID IS NULL");
            sb.append(") ct ORDER BY NLSSORT(ct.name, 'NLS_SORT = Vietnamese')");

            SQLQuery query = session.createSQLQuery(sb.toString());

            query.setParameter("p_company_site_id", campaignCustomerDTO.getCompanySiteId());
            query.setParameter("p_campaign_id", campaignCustomerDTO.getCampaignId());

            query.addScalar("companySiteId", new LongType());
            query.addScalar("campaignCustomerId", new LongType());
            query.addScalar("customerId", new LongType());
            query.addScalar("name", new StringType());
            query.addScalar("description", new StringType());
            query.addScalar("companyName", new StringType());
            query.addScalar("customerType", new StringType());
            query.addScalar("currentAddress", new StringType());
            query.addScalar("mobileNumber", new StringType());
            query.addScalar("email", new StringType());
            query.addScalar("ipccStatus", new StringType());
            query.addScalar("smsAllowed", new ShortType());
            query.addScalar("emailAllowed", new ShortType());
            query.addScalar("callAllowed", new ShortType());

            query.setResultTransformer(Transformers.aliasToBean(CustomerCustomDTO.class));

            total = campaignCustomerRepository.countAllByCompanySiteIdAndCampaignId(campaignCustomerDTO.getCompanySiteId(), campaignCustomerDTO.getCampaignId());
            if (total > 0) {
                if (pageable != null) {
                    query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                    query.setMaxResults(pageable.getPageSize());
                }

                data = query.list();

                dataPage = new PageImpl<>(data, pageable, total);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (session != null) session.close();
        }

        return dataPage;
    }

    @Override
    public List<CustomerListDTO> getCustomerListInfo(CampaignCustomerDTO campaignCustomerDTO) {
        List<CustomerListDTO> customerList = new ArrayList<>();
        try {
            //String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CAMPAIGN_MNG, "search-customer-list-info-from-customer-list");
            // BuildMyString.com generated code. Please enjoy your string responsibly.

            StringBuilder sb = new StringBuilder();

            sb.append(" with totalCustomer as (");
            sb.append("     select clm.customer_list_id,");
            sb.append("         clm.company_site_id,");
            sb.append("         count(clm.customer_id) total");
            sb.append("     from customer_list_mapping clm");
            sb.append("         inner join customer c on clm.customer_id = c.customer_id");
            sb.append("     where c.status = 1");
            sb.append("     group by clm.customer_list_id,clm.company_site_id),");
            sb.append(" customerInteractive as (");
            sb.append("     select customer_list_id,");
            sb.append("         company_site_id,");
            sb.append("         count(customer_id) tt");
            sb.append("     from campaign_customer");
            sb.append("     where campaign_id = :p_campaign_id and status <> 0");
            sb.append("     group by customer_list_id, company_site_id),");
            sb.append(" customerNotInteractive as (");
            sb.append("     select customer_list_id,");
            sb.append("         company_site_id,");
            sb.append("         count(customer_id) ktt");
            sb.append("     from campaign_customer");
            sb.append("      where campaign_id = :p_campaign_id and status = 0");
            sb.append("         and in_campaign_status = 1");
            sb.append("     group by customer_list_id, company_site_id),");
            sb.append(" datas as (");
            sb.append("     select customer_list_code customerListCode,");
            sb.append("         customer_list_name customerListName,");
            sb.append("         nvl(total, 0) totalCusInList,");
            sb.append("         nvl(tt, 0) totalCusInteract,");
            sb.append("         nvl(ktt, 0) totalCusNotInteract");
            sb.append("     from customer_list cl");
            sb.append("         left join totalCustomer tc on (cl.customer_list_id = tc.customer_list_id and cl.company_site_id = tc.company_site_id)");
            sb.append("         left join customerInteractive ci on (cl.customer_list_id = ci.customer_list_id and cl.company_site_id = ci.company_site_id)");
            sb.append("         left join customerNotInteractive cni on (cl.customer_list_id = cni.customer_list_id and cl.company_site_id = cni.company_site_id)");
            sb.append("     where cl.company_site_id = :p_company_site_id )");
            sb.append(" select * from");
            sb.append(" (");
            sb.append("    select a.*, rownum r__");
            sb.append("    from");
            sb.append("    (");
            sb.append("        select * from datas");
            sb.append("        order by customerListCode");
            sb.append("    ) a");
            sb.append("    where rownum < ((:p_page_number * :p_page_size) + 1 )");
            sb.append(" )");
            sb.append(" where r__ >= (((:p_page_number-1) * :p_page_size) + 1)");


            Map<String, Object> param = new HashMap<>();
            param.put("p_campaign_id", campaignCustomerDTO.getCampaignId());
            param.put("p_company_site_id", campaignCustomerDTO.getCompanySiteId());
            param.put("p_page_number", campaignCustomerDTO.getPage());
            param.put("p_page_size", campaignCustomerDTO.getPageSize());

            customerList = namedParameterJdbcTemplate.query(sb.toString(), param, new BeanPropertyRowMapper<>(CustomerListDTO.class));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return customerList;
    }

//    @Override
//    public Page<CustomerDTO> getCustomizeFields(CampaignCustomerDTO campaignCustomerDTO, UserSession userSession, Pageable pageable) {
//        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
//        List<CustomerDTO> data = new ArrayList<>();
//        Page<CustomerDTO> dataPage = new PageImpl<>(data, pageable, 0);
//        Session session = sessionFactory.openSession();
//        session.beginTransaction();
//        Map<String, String> requestCustomer = new HashMap<>();
//        List<CustomerQueryDTO> customerDTOList = campaignCustomerDTO.getListQuery();
//        requestCustomer.put("-1", "c.code");
//        requestCustomer.put("-2", "c.name");
//        requestCustomer.put("-3", "c.company_name");
//        requestCustomer.put("-4", "c.gender");
//        requestCustomer.put("-5", "c.current_address");
//        requestCustomer.put("-6", "c.place_of_birth");
//        requestCustomer.put("-7", "c.date_of_birth");
//        requestCustomer.put("-8", "cc.contact");
//        requestCustomer.put("-9", "cc2.contact");
//        requestCustomer.put("-10", "c.username");
//        requestCustomer.put("-11", "c.customer_type");
//        requestCustomer.put("-12", "c.area_code");
//        requestCustomer.put("-13", "c.call_allowed");
//        requestCustomer.put("-14", "c.email_allowed");
//        requestCustomer.put("-15", "c.sms_allowed");
//        requestCustomer.put("-16", "c.ipcc_status");
//
//
//        try {
//            List<CustomerQueryDTO> staticQuery = new ArrayList<>();
//            List<CustomerQueryDTO> dynamicQuery = new ArrayList<>();
//            List<CustomerQueryDTO> dynamicAndQuery = new ArrayList<>();
//            List<CustomerQueryDTO> dynamicOrQuery = new ArrayList<>();
//            for (int i = 0; i < customerDTOList.size(); i++) {
//                if (customerDTOList.get(i).getField() > 0) {
//                    dynamicQuery.add(customerDTOList.get(i));
//                } else staticQuery.add(customerDTOList.get(i));
//            }
//            for (int i = 0; i < dynamicQuery.size(); i++) {
//                if ("AND".equalsIgnoreCase(dynamicQuery.get(i).getJoin())) {
//                    dynamicAndQuery.add(customerDTOList.get(i));
//                } else dynamicOrQuery.add(customerDTOList.get(i));
//            }
//            StringBuilder sb = new StringBuilder();
//
//            sb.append(" WITH temp AS");
//            sb.append(" (SELECT c.customer_id");
//            sb.append(" FROM CUSTOMER C");
//            sb.append(" LEFT JOIN customize_field_object cfo");
//            sb.append(" ON c.customer_id = cfo.object_id");
//            sb.append(" AND cfo.status = 1");
//            sb.append(" WHERE 1 = 1");
//            sb.append(" AND C.STATUS = 1");
//            sb.append(" AND C.SITE_ID = :p_site_id");
//            sb.append(" AND c.CUSTOMER_ID NOT IN");
//            sb.append(" (SELECT cc.CUSTOMER_ID");
//            sb.append(" FROM CAMPAIGN_CUSTOMER cc");
//            sb.append(" WHERE 1 = 1");
//            sb.append(" AND cc.COMPANY_SITE_ID = :p_site_id");
//            sb.append(" AND cc.CAMPAIGN_ID = :p_campaign_id");
//            sb.append(" AND cc.IN_CAMPAIGN_STATUS = 1");
//            sb.append(" )");
//
//            if (dynamicQuery.size() > 0) {
//                sb.append("AND (");
//                for (int i = 0; i < dynamicQuery.size(); i++) {
//                    if (i == 0) {
//                        switch (dynamicQuery.get(0).getType()) {
//                            case "combobox":
//                                sb.append("(cfo.CUSTOMIZE_FIELDS_ID = " + dynamicQuery.get(0).getField() + " and cfo.field_option_value_id " + dynamicQuery.get(0).getOperator() + " " + dynamicQuery.get(0).getCondition() + " )");
//                                break;
//                            case "text":
//                                if ("like".equals(dynamicQuery.get(0).getOperator()) || "not like".equals(dynamicQuery.get(0).getOperator())) {
//                                    sb.append("(cfo.customize_fields_id = " + dynamicQuery.get(0).getField() + " and (upper(cfo.value_text) " + dynamicQuery.get(0).getOperator() + " '%" + dynamicQuery.get(0).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_").toUpperCase() + "%'");
//                                } else {
//                                    sb.append("(cfo.customize_fields_id = " + dynamicQuery.get(0).getField() + " and (cfo.value_text " + dynamicQuery.get(0).getOperator() + " '" + dynamicQuery.get(0).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_") + "'");
//                                }
//                                if ("not like".equals(dynamicQuery.get(0).getOperator()) || "<>".equals(dynamicQuery.get(0).getOperator())) {
//                                    sb.append("or cfo.value_text is null)");
//                                } else sb.append(")");
//                                sb.append(")");
//                                break;
//                            case "date":
//                                sb.append("(cfo.customize_fields_id = " + dynamicQuery.get(0).getField() + " and trunc(cfo.value_date) " + dynamicQuery.get(0).getOperator() + " trunc(to_date('" + dynamicQuery.get(0).getCondition().trim() + "', 'DD/MM/YYYY') - " + campaignCustomerDTO.getTimezoneOffset() + "/60/24))");
//                                break;
//                            case "number":
//                                sb.append("(cfo.customize_fields_id = " + dynamicQuery.get(0).getField() + " and cfo.value_number " + dynamicQuery.get(0).getOperator() + " " + dynamicQuery.get(0).getCondition() + ")");
//                                break;
//                            case "checkbox":
//                                sb.append("(cfo.customize_fields_id = " + dynamicQuery.get(0).getField() + " and cfo.value_checkbox " + dynamicQuery.get(0).getOperator() + " " + ("true".equals(dynamicQuery.get(0).getCondition()) ? "1" : "0") + ")");
//                                break;
//                        }
//                    } else {
//                        if (dynamicAndQuery.size() > 0) {
//                            switch (dynamicQuery.get(i).getType()) {
//                                case "combobox":
//                                    sb.append("or (cfo.CUSTOMIZE_FIELDS_ID = " + dynamicQuery.get(i).getField() + " and cfo.field_option_value_id " + dynamicQuery.get(i).getOperator() + " " + dynamicQuery.get(i).getCondition() + " )");
//                                    break;
//                                case "text":
//                                    if ("like".equals(dynamicQuery.get(i).getOperator()) || "not like".equals(dynamicQuery.get(i).getOperator())) {
//                                        sb.append("or (cfo.customize_fields_id = " + dynamicQuery.get(i).getField() + " and (upper(cfo.value_text) " + dynamicQuery.get(i).getOperator() + " '%" + dynamicQuery.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_").toUpperCase() + "%'");
//                                    } else {
//                                        sb.append("or (cfo.customize_fields_id = " + dynamicQuery.get(i).getField() + " and (cfo.value_text " + dynamicQuery.get(i).getOperator() + " '" + dynamicQuery.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_") + "'");
//                                    }
//                                    if ("not like".equals(dynamicQuery.get(i).getOperator()) || "<>".equals(dynamicQuery.get(i).getOperator())) {
//                                        sb.append("or cfo.value_text is null)");
//                                    } else sb.append(")");
//                                    sb.append(")");
//                                    break;
//                                case "date":
//                                    sb.append("or (cfo.customize_fields_id = " + dynamicQuery.get(i).getField() + " and trunc(cfo.value_date) " + dynamicQuery.get(i).getOperator() + " trunc(to_date('" + dynamicQuery.get(i).getCondition().trim() + "', 'DD/MM/YYYY') - " + campaignCustomerDTO.getTimezoneOffset() + "/60/24))");
//                                    break;
//                                case "number":
//                                    sb.append("or (cfo.customize_fields_id = " + dynamicQuery.get(i).getField() + " and cfo.value_number " + dynamicQuery.get(i).getOperator() + " " + dynamicQuery.get(i).getCondition() + ")");
//                                    break;
//                                case "checkbox":
//                                    sb.append("or (cfo.customize_fields_id = " + dynamicQuery.get(i).getField() + " and cfo.value_checkbox " + dynamicQuery.get(i).getOperator() + " " + ("true".equals(dynamicQuery.get(i).getCondition()) ? "1" : "0") + ")");
//                                    break;
//                            }
//                        }
//                    }
//                }
//                sb.append(")");
//                if (dynamicOrQuery.size() > 0) {
//                    for (int i = 0; i < dynamicOrQuery.size(); i++) {
//                        switch (dynamicOrQuery.get(i).getType()) {
//                            case "combobox":
//                                sb.append("or (cfo.CUSTOMIZE_FIELDS_ID = " + dynamicOrQuery.get(i).getField() + " and cfo.field_option_value_id " + dynamicOrQuery.get(i).getOperator() + " " + dynamicOrQuery.get(i).getCondition() + " )");
//                                break;
//                            case "text":
//                                if ("like".equals(dynamicOrQuery.get(i).getOperator()) || "not like".equals(dynamicOrQuery.get(i).getOperator())) {
//                                    sb.append("or (cfo.customize_fields_id = " + dynamicOrQuery.get(i).getField() + " and (upper(cfo.value_text) " + dynamicOrQuery.get(i).getOperator() + " '%" + dynamicOrQuery.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_").toUpperCase() + "%'");
//                                } else {
//                                    sb.append("or (cfo.customize_fields_id = " + dynamicOrQuery.get(i).getField() + " and (cfo.value_text " + dynamicOrQuery.get(i).getOperator() + " '" + dynamicOrQuery.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_") + "'");
//                                }
//                                if ("not like".equals(dynamicOrQuery.get(i).getOperator()) || "<>".equals(dynamicOrQuery.get(i).getOperator())) {
//                                    sb.append("or cfo.value_text is null)");
//                                } else sb.append(")");
//                                sb.append(")");
//                                break;
//                            case "date":
//                                sb.append("or (cfo.customize_fields_id = " + dynamicOrQuery.get(i).getField() + " and trunc(cfo.value_date) " + dynamicOrQuery.get(i).getOperator() + " trunc(to_date('" + dynamicOrQuery.get(i).getCondition().trim() + "', 'DD/MM/YYYY') - " + campaignCustomerDTO.getTimezoneOffset() + "/60/24))");
//                                break;
//                            case "number":
//                                sb.append("or (cfo.customize_fields_id = " + dynamicOrQuery.get(i).getField() + " and cfo.value_number " + dynamicOrQuery.get(i).getOperator() + " " + dynamicOrQuery.get(i).getCondition() + ")");
//                                break;
//                            case "checkbox":
//                                sb.append("or (cfo.customize_fields_id = " + dynamicOrQuery.get(i).getField() + " and cfo.value_checkbox " + dynamicOrQuery.get(i).getOperator() + " " + ("true".equals(dynamicOrQuery.get(i).getCondition()) ? "1" : "0") + ")");
//                                break;
//                        }
//                    }
//                }
//            }
//
//            sb.append(" GROUP BY c.customer_id");
//            sb.append(" )");
//            sb.append(" SELECT DISTINCT c.customer_id customerId,");
//            sb.append(" C.NAME name,");
//            sb.append(" cc.CONTACT mobileNumber,");
//            sb.append(" cc2.CONTACT email,");
//            sb.append(" c.customer_type customerType,");
//            sb.append(" C.COMPANY_NAME companyName,");
//            sb.append(" c.current_address currentAddress,");
//            sb.append(" c.CALL_ALLOWED callAllowed,");
//            sb.append(" c.EMAIL_ALLOWED emailAllowed,");
//            sb.append(" c.SMS_ALLOWED smsAllowed,");
//            sb.append(" c.IPCC_STATUS ipccStatus,");
//            sb.append(" c.Description description");
//            sb.append(" FROM CUSTOMER C");
//            sb.append(" LEFT JOIN");
//            sb.append(" (SELECT CUSTOMER_ID,");
//            sb.append(" LISTAGG(CONTACT, ', ') WITHIN GROUP (");
//            sb.append(" ORDER BY NULL) AS CONTACT");
//            sb.append(" FROM CUSTOMER_CONTACT");
//            sb.append(" WHERE CONTACT_TYPE = 5");
//            sb.append(" AND STATUS = 1");
//            sb.append(" GROUP BY CUSTOMER_ID");
//            sb.append(" ) cc");
//            sb.append(" ON c.CUSTOMER_ID = cc.CUSTOMER_ID");
//            sb.append(" LEFT JOIN");
//            sb.append(" (SELECT CUSTOMER_ID,");
//            sb.append(" LISTAGG(CONTACT, ', ') WITHIN GROUP (");
//            sb.append(" ORDER BY NULL) AS CONTACT");
//            sb.append(" FROM CUSTOMER_CONTACT");
//            sb.append(" WHERE CONTACT_TYPE = 2");
//            sb.append(" AND STATUS = 1");
//            sb.append(" GROUP BY CUSTOMER_ID");
//            sb.append(" ) cc2");
//            sb.append(" ON cc2.CUSTOMER_ID = c.CUSTOMER_ID");
//            sb.append(" WHERE 1 = 1");
//            sb.append(" AND C.STATUS = 1");
//            sb.append(" AND C.SITE_ID = :p_site_id");
//            sb.append(" AND c.CUSTOMER_ID NOT IN");
//            sb.append(" (SELECT cc.CUSTOMER_ID");
//            sb.append(" FROM CAMPAIGN_CUSTOMER cc");
//            sb.append(" WHERE 1 = 1");
//            sb.append(" AND cc.COMPANY_SITE_ID = :p_site_id");
//            sb.append(" AND cc.CAMPAIGN_ID = :p_campaign_id");
//            sb.append(" AND cc.IN_CAMPAIGN_STATUS = 1");
//            sb.append(" )");
//            sb.append(" AND c.CUSTOMER_ID IN");
//            sb.append(" (SELECT CUSTOMER_ID FROM temp)");
//
//            if (staticQuery.size() > 0) {
//                for (int i = 0; i < staticQuery.size(); i++) {
//                    if ("like".equals(staticQuery.get(i).getOperator()) || "not like".equals(staticQuery.get(i).getOperator())) {
//                        sb.append(staticQuery.get(i).getJoin() + " (upper(" + requestCustomer.get(staticQuery.get(i).getField().toString()) + ") " + staticQuery.get(i).getOperator() + " '%" + staticQuery.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_").toUpperCase() + "%'");
//                    } else if (staticQuery.get(i).getField() == -8 || staticQuery.get(i).getField() == -9) {
//                        sb.append(staticQuery.get(i).getJoin() + " (" + requestCustomer.get(staticQuery.get(i).getField().toString()) + " " + ("=".equals(staticQuery.get(i).getOperator()) ? "like " : "not like") + " '%" + staticQuery.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_") + "%'");
//                    } else if ("text".equals(staticQuery.get(i).getType())) {
//                        if ("=".equals(staticQuery.get(i).getOperator()) || "<>".equals(staticQuery.get(i).getOperator())) {
//                            sb.append(staticQuery.get(i).getJoin() + " (" + requestCustomer.get(staticQuery.get(i).getField().toString()) + " " + staticQuery.get(i).getOperator() + " '" + staticQuery.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_") + "'");
//                        } else {
//                            sb.append(staticQuery.get(i).getJoin() + " (upper(" + requestCustomer.get(staticQuery.get(i).getField().toString()) + ") " + staticQuery.get(i).getOperator() + " '%" + staticQuery.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_").toUpperCase() + "%'");
//                        }
//                    } else {
//                        if ("date".equalsIgnoreCase(staticQuery.get(i).getType())) {
//                            sb.append(staticQuery.get(i).getJoin() + " (trunc(" + requestCustomer.get(staticQuery.get(i).getField().toString()) + ") " + staticQuery.get(i).getOperator() + " trunc(to_date('" + staticQuery.get(i).getCondition() + "', 'DD/MM/YYYY') - " + campaignCustomerDTO.getTimezoneOffset() + "/60/24)");
//                        } else {
//                            sb.append(staticQuery.get(i).getJoin() + " (" + requestCustomer.get(staticQuery.get(i).getField().toString()) + " " + staticQuery.get(i).getOperator() + " " + staticQuery.get(i).getCondition());
//                        }
//                    }
//                    if ("<>".equals(staticQuery.get(i).getOperator()) || "not like".equals(staticQuery.get(i).getOperator())) {
//                        sb.append(" or " + requestCustomer.get(staticQuery.get(i).getField().toString()) + " is null)");
//                        continue;
//                    }
//                    sb.append(")");
//                }
//            }
//            SQLQuery query = session.createSQLQuery(sb.toString());
//            query.addScalar("name", new StringType());
//            query.addScalar("customerId", new LongType());
//            query.addScalar("mobileNumber", new StringType());
//            query.addScalar("email", new StringType());
//            query.addScalar("customerType", new LongType());
//            query.addScalar("companyName", new StringType());
//            query.addScalar("currentAddress", new StringType());
//            query.addScalar("description", new StringType());
//            query.addScalar("callAllowed", new StringType());
//            query.addScalar("emailAllowed", new LongType());
//            query.addScalar("smsAllowed", new LongType());
//            query.addScalar("ipccStatus", new StringType());
//
//            query.setParameter("p_campaign_id", campaignCustomerDTO.getCampaignId());
//            query.setParameter("p_site_id", userSession.getSiteId());
//
//            query.setResultTransformer(Transformers.aliasToBean(CustomerDTO.class));
//
//            Integer count = 0;
//            count = query.list().size();
//            if (count > 0) {
//                if (pageable != null) {
//                    query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
//                    query.setMaxResults(pageable.getPageSize());
//                }
//
//                data = query.list();
//
//                dataPage = new PageImpl<>(data, pageable, count);
//            }
//
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        } finally {
//            session.close();
//        }
//
//        return dataPage;
//    }

    //AI THICH FIX THI FIX E BO TAY
    @Override
    public Page<CustomerDTO> getCustomizeFields(CampaignCustomerDTO campaignCustomerDTO, UserSession userSession, Pageable pageable) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        List<CustomerDTO> data = new ArrayList<>();
        Page<CustomerDTO> dataPage = new PageImpl<>(data, pageable, 0);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Map<String, String> requestCustomer = new HashMap<>();
        boolean weirdCase = false;
        List<CustomerQueryDTO> customerDTOList = campaignCustomerDTO.getListQuery();
        requestCustomer.put("-1", "c.code");
        requestCustomer.put("-2", "c.name");
        requestCustomer.put("-3", "c.company_name");
        requestCustomer.put("-4", "c.gender");
        requestCustomer.put("-5", "c.current_address");
        requestCustomer.put("-6", "c.place_of_birth");
        requestCustomer.put("-7", "c.date_of_birth");
        requestCustomer.put("-8", "cc.contact");
        requestCustomer.put("-9", "cc2.contact");
        requestCustomer.put("-10", "c.username");
        requestCustomer.put("-11", "c.customer_type");
        requestCustomer.put("-12", "c.area_code");
        requestCustomer.put("-13", "c.call_allowed");
        requestCustomer.put("-14", "c.email_allowed");
        requestCustomer.put("-15", "c.sms_allowed");
        requestCustomer.put("-16", "c.ipcc_status");
        int andConditionCount = 1;
        boolean andCondition = false;

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(" with temp as ( ");
            sb.append(" select c.customer_id customerId, ");
            sb.append(" count(*) ");
            sb.append(" from customer c ");
            sb.append(" left join ");
            sb.append(" (SELECT CUSTOMER_ID, ");
            sb.append("   LISTAGG(CONTACT, ', ') WITHIN GROUP ( ");
            sb.append("   ORDER BY NULL) AS CONTACT ");
            sb.append("   FROM CUSTOMER_CONTACT ");
            sb.append("   WHERE CONTACT_TYPE = 5 ");
            sb.append("   AND STATUS         = 1 ");
            sb.append("   and site_id = :p_site_id ");
            sb.append("   GROUP BY CUSTOMER_ID ");
            sb.append("   ) cc ");
            sb.append("   ON c.CUSTOMER_ID = cc.CUSTOMER_ID ");
            sb.append("   LEFT JOIN ");
            sb.append("   (SELECT CUSTOMER_ID, ");
            sb.append("     LISTAGG(CONTACT, ', ') WITHIN GROUP ( ");
            sb.append("     ORDER BY NULL) AS CONTACT ");
            sb.append("     FROM CUSTOMER_CONTACT ");
            sb.append("     WHERE CONTACT_TYPE = 2 ");
            sb.append("     AND STATUS         = 1 ");
            sb.append("     and site_id = :p_site_id ");
            sb.append("     GROUP BY CUSTOMER_ID ");
            sb.append("     ) cc2 ");
            sb.append("     ON cc2.CUSTOMER_ID = c.CUSTOMER_ID ");
            sb.append("     LEFT JOIN customize_field_object cfo ");
            sb.append("     ON c.customer_id       = cfo.object_id ");
            sb.append("     AND cfo.status         = 1 ");
            sb.append("     WHERE 1                = 1 ");
            sb.append("     AND C.STATUS           = 1 ");
            sb.append("     AND C.SITE_ID          = :p_site_id ");
            sb.append(" AND c.CUSTOMER_ID NOT IN ");
            sb.append(" (SELECT cc.CUSTOMER_ID ");
            sb.append(" FROM CAMPAIGN_CUSTOMER cc ");
            sb.append(" WHERE 1                   = 1 ");
            sb.append(" AND cc.COMPANY_SITE_ID    = :p_site_id ");
            sb.append(" AND cc.CAMPAIGN_ID        = :p_campaign_id ");
            sb.append(" AND cc.IN_CAMPAIGN_STATUS = 1) ");
            List<CustomerQueryDTO> dynamicQueryList = new ArrayList<>();
            //giai phap tinh the
            if (customerDTOList.size() > 1) {
                if (customerDTOList.get(0).getField() < 0 && customerDTOList.get(1).getField() > 0 && "or".equalsIgnoreCase(customerDTOList.get(1).getJoin())) {
                    weirdCase = true;
                }
            }

            for (int i = 0; i < customerDTOList.size(); i++) {
                if (customerDTOList.get(i).getField() > 0) {//truong dong
                    dynamicQueryList.add(customerDTOList.get(i));
                }
            }
            if (dynamicQueryList.size() > 0) {
                customerDTOList.removeAll(dynamicQueryList);
                sb.append(" and (cfo.CUSTOMIZE_FIELDS_ID in (");
                for (int i = 0; i < dynamicQueryList.size(); i++) {
                    sb.append(dynamicQueryList.get(i).getField());
                    if (i == (dynamicQueryList.size() - 1)) {
                        sb.append(") ");
                        break;
                    } else sb.append(",");
                    if ((i + 1) < dynamicQueryList.size() && "and".equalsIgnoreCase(dynamicQueryList.get(i + 1).getJoin())) {
                        andCondition = true;
                        andConditionCount++;
                    }
                }
                sb.append(" and ( ");
                for (int i = 0; i < dynamicQueryList.size(); i++) {
                    if (i != 0) {
                        sb.append(" or ");
                    }
                    switch (dynamicQueryList.get(i).getType()) {
                        case "combobox":
                            sb.append(" cfo.field_option_value_id " + dynamicQueryList.get(i).getOperator() + " " + dynamicQueryList.get(i).getCondition() + " ");
                            break;
                        case "text":
                            if ("like".equals(dynamicQueryList.get(i).getOperator()) || "not like".equals(dynamicQueryList.get(i).getOperator())) {
                                sb.append(" (upper(cfo.value_text) " + dynamicQueryList.get(i).getOperator() + " '%" + dynamicQueryList.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_").toUpperCase() + "%' ");
                            } else {
                                sb.append(" (cfo.value_text " + dynamicQueryList.get(i).getOperator() + " '" + dynamicQueryList.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_") + "' ");
                            }
                            if ("not like".equals(dynamicQueryList.get(i).getOperator()) || "<>".equals(dynamicQueryList.get(i).getOperator())) {
                                sb.append("or cfo.value_text is null) ");
                            } else sb.append(") ");
                            break;
                        case "date":
                            sb.append(" trunc(cfo.value_date) " + dynamicQueryList.get(i).getOperator() + " trunc(to_date('" + dynamicQueryList.get(i).getCondition().trim() + "', 'DD/MM/YYYY') + " + campaignCustomerDTO.getTimezoneOffset() + "/60/24) ");
                            break;
                        case "number":
                            sb.append(" cfo.value_number " + dynamicQueryList.get(i).getOperator() + " " + dynamicQueryList.get(i).getCondition() + " ");
                            break;
                        case "checkbox":
                            sb.append(" cfo.value_checkbox " + dynamicQueryList.get(i).getOperator() + " " + ("true".equals(dynamicQueryList.get(i).getCondition()) ? "1" : "0") + " ");
                            break;
                    }
                }
                sb.append(" ) ");
                for (int i = 0; i < customerDTOList.size(); i++) {
                    if ("like".equals(customerDTOList.get(i).getOperator()) || "not like".equals(customerDTOList.get(i).getOperator())) {
                        sb.append(" " + (i==0 && weirdCase? " or " : customerDTOList.get(i).getJoin()) + " (upper(" + requestCustomer.get(customerDTOList.get(i).getField().toString()) + ") " + customerDTOList.get(i).getOperator() + " '%" + customerDTOList.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_").toUpperCase() + "%' ");
                    } else if (customerDTOList.get(i).getField() == -8 || customerDTOList.get(i).getField() == -9) {
                        sb.append(" " + (i==0 && weirdCase? " or " : customerDTOList.get(i).getJoin()) + " (" + requestCustomer.get(customerDTOList.get(i).getField().toString()) + " " + ("=".equals(customerDTOList.get(i).getOperator()) ? "like " : "not like") + " '%" + customerDTOList.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_") + "%' ");
                    } else if ("text".equals(customerDTOList.get(i).getType())) {
                        if ("=".equals(customerDTOList.get(i).getOperator()) || "<>".equals(customerDTOList.get(i).getOperator())) {
                            sb.append(" " + (i==0 && weirdCase? " or " : customerDTOList.get(i).getJoin()) + " (" + requestCustomer.get(customerDTOList.get(i).getField().toString()) + " " + customerDTOList.get(i).getOperator() + " '" + customerDTOList.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_") + "' ");
                        } else {
                            sb.append(" " + (i==0 && weirdCase? " or " : customerDTOList.get(i).getJoin()) + " (upper(" + requestCustomer.get(customerDTOList.get(i).getField().toString()) + ") " + customerDTOList.get(i).getOperator() + " '%" + customerDTOList.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_").toUpperCase() + "%' ");
                        }
                    } else {
                        if ("date".equalsIgnoreCase(customerDTOList.get(i).getType())) {
                            sb.append(" " + (i==0 && weirdCase? " or " : customerDTOList.get(i).getJoin()) + " (trunc(" + requestCustomer.get(customerDTOList.get(i).getField().toString()) + ") " + customerDTOList.get(i).getOperator() + " trunc(to_date('" + customerDTOList.get(i).getCondition() + "', 'DD/MM/YYYY') - " + campaignCustomerDTO.getTimezoneOffset() + "/60/24) ");
                        } else {
                            sb.append(" " + (i==0 && weirdCase? " or " : customerDTOList.get(i).getJoin()) + " (" + requestCustomer.get(customerDTOList.get(i).getField().toString()) + " " + customerDTOList.get(i).getOperator() + " " + customerDTOList.get(i).getCondition() + " ");
                        }
                    }
                    if ("<>".equals(customerDTOList.get(i).getOperator()) || "not like".equals(customerDTOList.get(i).getOperator())) {
                        sb.append(" or " + requestCustomer.get(customerDTOList.get(i).getField().toString()) + " is null)");
                        continue;
                    }
                    sb.append(")");
                }
            } else {
                for (int i = 0; i < customerDTOList.size(); i++) {
                    if ("like".equals(customerDTOList.get(i).getOperator()) || "not like".equals(customerDTOList.get(i).getOperator())) {
                        sb.append(" " + customerDTOList.get(i).getJoin() + " (upper(" + requestCustomer.get(customerDTOList.get(i).getField().toString()) + ") " + customerDTOList.get(i).getOperator() + " '%" + customerDTOList.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_").toUpperCase() + "%' ");
                    } else if (customerDTOList.get(i).getField() == -8 || customerDTOList.get(i).getField() == -9) {
                        sb.append(" " +  customerDTOList.get(i).getJoin() + " (" + requestCustomer.get(customerDTOList.get(i).getField().toString()) + " " + ("=".equals(customerDTOList.get(i).getOperator()) ? "like " : "not like") + " '%" + customerDTOList.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_") + "%' ");
                    } else if ("text".equals(customerDTOList.get(i).getType())) {
                        if ("=".equals(customerDTOList.get(i).getOperator()) || "<>".equals(customerDTOList.get(i).getOperator())) {
                            sb.append(" " + customerDTOList.get(i).getJoin() + " (" + requestCustomer.get(customerDTOList.get(i).getField().toString()) + " " + customerDTOList.get(i).getOperator() + " '" + customerDTOList.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_") + "' ");
                        } else {
                            sb.append(" " + customerDTOList.get(i).getJoin() + " (upper(" + requestCustomer.get(customerDTOList.get(i).getField().toString()) + ") " + customerDTOList.get(i).getOperator() + " '%" + customerDTOList.get(i).getCondition().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_").toUpperCase() + "%' ");
                        }
                    } else {
                        if ("date".equalsIgnoreCase(customerDTOList.get(i).getType())) {
                            sb.append(" " + customerDTOList.get(i).getJoin() + " (trunc(" + requestCustomer.get(customerDTOList.get(i).getField().toString()) + ") " + customerDTOList.get(i).getOperator() + " trunc(to_date('" + customerDTOList.get(i).getCondition() + "', 'DD/MM/YYYY') - " + campaignCustomerDTO.getTimezoneOffset() + "/60/24) ");
                        } else {
                            sb.append(" " + customerDTOList.get(i).getJoin() + " (" + requestCustomer.get(customerDTOList.get(i).getField().toString()) + " " + customerDTOList.get(i).getOperator() + " " + customerDTOList.get(i).getCondition() + " ");
                        }
                    }
                    if ("<>".equals(customerDTOList.get(i).getOperator()) || "not like".equals(customerDTOList.get(i).getOperator())) {
                        sb.append(" or " + requestCustomer.get(customerDTOList.get(i).getField().toString()) + " is null)");
                        continue;
                    }
                    sb.append(")");
                }
            }
            if(dynamicQueryList.size() > 0){
                sb.append(" ) ");
            }else {
               sb.append(" ");
            }

            sb.append(" group by c.customer_id ");
            if (andCondition) {
                sb.append(" having count(*) = " + andConditionCount);
            }
            sb.append(" ) ");
            sb.append(" SELECT c.customer_id customerId, ");
            sb.append("   C.NAME name, ");
            sb.append("   cc.CONTACT mobileNumber, ");
            sb.append("   cc2.CONTACT email, ");
            sb.append("   c.customer_type customerType, ");
            sb.append("   C.COMPANY_NAME companyName, ");
            sb.append(" c.current_address currentAddress, " +
                    "  c.CALL_ALLOWED callAllowed, " +
                    "  c.EMAIL_ALLOWED emailAllowed, " +
                    "  c.SMS_ALLOWED smsAllowed, " +
                    "  c.IPCC_STATUS ipccStatus, " +
                    "  c.Description description " +
                    " FROM CUSTOMER C " +
                    " LEFT JOIN " +
                    "  (SELECT CUSTOMER_ID, " +
                    "    LISTAGG(CONTACT, ', ') WITHIN GROUP ( " +
                    "  ORDER BY NULL) AS CONTACT " +
                    "  FROM CUSTOMER_CONTACT " +
                    "  WHERE CONTACT_TYPE = 5 " +
                    "  AND STATUS         = 1 " +
                    "  GROUP BY CUSTOMER_ID " +
                    "  ) cc " +
                    " ON c.CUSTOMER_ID = cc.CUSTOMER_ID " +
                    " LEFT JOIN " +
                    "  (SELECT CUSTOMER_ID, " +
                    "    LISTAGG(CONTACT, ', ') WITHIN GROUP ( " +
                    "  ORDER BY NULL) AS CONTACT " +
                    "  FROM CUSTOMER_CONTACT " +
                    "  WHERE CONTACT_TYPE = 2 " +
                    "  AND STATUS         = 1 " +
                    "  GROUP BY CUSTOMER_ID " +
                    "  ) cc2 " +
                    " ON cc2.CUSTOMER_ID = c.CUSTOMER_ID " +
                    " where c.customer_id in (select customerId from temp)");

            SQLQuery query = session.createSQLQuery(sb.toString());
            query.addScalar("customerId", new LongType());
            query.addScalar("name", new StringType());
            query.addScalar("mobileNumber", new StringType());
            query.addScalar("email", new StringType());
            query.addScalar("customerType", new LongType());
            query.addScalar("companyName", new StringType());
            query.addScalar("currentAddress", new StringType());
            query.addScalar("description", new StringType());
            query.addScalar("callAllowed", new StringType());
            query.addScalar("emailAllowed", new LongType());
            query.addScalar("smsAllowed", new LongType());
            query.addScalar("ipccStatus", new StringType());
            query.setParameter("p_campaign_id", campaignCustomerDTO.getCampaignId());
            query.setParameter("p_site_id", userSession.getSiteId());

            query.setResultTransformer(Transformers.aliasToBean(CustomerDTO.class));

            Integer count = 0;
            count = query.list().size();
            if (count > 0) {
                query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                query.setMaxResults(pageable.getPageSize());

                data = query.list();

                dataPage = new PageImpl<>(data, pageable, count);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            session.close();
        }
        return dataPage;
    }

    @Override
    public List<CustomerDetailRequestDTO> getIndividualCustomerDetailById(Long companySiteId, Long customerId, Long timezoneOffset) {
        List<CustomerDetailRequestDTO> data = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            StringBuilder sb = new StringBuilder();

            sb.append("select distinct a.CUSTOMER_ID     customerId,");
            sb.append("    a.SITE_ID companySiteId,");
            sb.append("    f.TITLE title,");
            sb.append("    f.TYPE type,");
            sb.append("    h.NAME valueCombobox,");
            sb.append("    e.VALUE_CHECKBOX valueCheckbox,");
            sb.append("    (e.VALUE_DATE + (:p_timezone_offset / 60)/24) valueDate,");
            sb.append("    e.VALUE_NUMBER valueNumber,");
            sb.append("    e.VALUE_TEXT valueText");
            sb.append(" from CUSTOMER a");
            sb.append("         inner join CUSTOMIZE_FIELDS f on a.SITE_ID = f.SITE_ID and f.STATUS = 1 and f.ACTIVE = 1");
            sb.append("         left join CUSTOMIZE_FIELD_OBJECT e on f.CUSTOMIZE_FIELD_ID = e.CUSTOMIZE_FIELDS_ID and a.CUSTOMER_ID = e.OBJECT_ID and e.FUNCTION_CODE = 'CUSTOMER'");
            sb.append("         left join CUSTOMIZE_FIELD_OPTION_VALUE h on h.FIELD_OPTION_VALUE_ID = e.FIELD_OPTION_VALUE_ID");
            sb.append(" where 1 = 1");
            sb.append("  and a.SITE_ID = :p_company_site_id");
            sb.append("  and a.CUSTOMER_ID = :p_customer_id");

            SQLQuery query = session.createSQLQuery(sb.toString());

            query.setParameter("p_timezone_offset", timezoneOffset);
            query.setParameter("p_company_site_id", companySiteId);
            query.setParameter("p_customer_id", customerId);

            query.addScalar("companySiteId", new LongType());
            query.addScalar("customerId", new LongType());
            query.addScalar("title", new StringType());
            query.addScalar("type", new StringType());
            query.addScalar("valueCombobox", new StringType());
            query.addScalar("valueCheckbox", new ShortType());
            query.addScalar("valueDate", new DateType());
            query.addScalar("valueNumber", new LongType());
            query.addScalar("valueText", new StringType());

            query.setResultTransformer(Transformers.aliasToBean(CustomerDetailRequestDTO.class));

            data = query.list();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (session != null) session.close();
        }

        return data;
    }
}
