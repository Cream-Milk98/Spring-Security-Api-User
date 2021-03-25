package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.repository.ccms_full.CustomerListRepositoryCustom;
import com.viettel.campaign.utils.*;
import com.viettel.campaign.web.dto.ContactCustResultDTO;
import com.viettel.campaign.web.dto.CustomerListDTO;
import com.viettel.campaign.web.dto.request_dto.SearchCustomerListRequestDTO;
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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Repository
public class CustomerListRepositoryImpl implements CustomerListRepositoryCustom {

    private Logger logger = LoggerFactory.getLogger(CustomerListRepositoryImpl.class);

    @Override
    public Page<CustomerListDTO> getAllCustomerListByParams(SearchCustomerListRequestDTO searchCustomerListRequestDTO, Pageable pageable) {
        TimeZone tzClient = TimeZoneUtils.getZoneMinutes(searchCustomerListRequestDTO.getTimezoneOffset());
        List<CustomerListDTO> listData = new ArrayList<>();
        Page<CustomerListDTO> dataPage = new PageImpl<>(listData, pageable, 0);
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT");
            sb.append("    a.CUSTOMER_LIST_ID customerListId,");
            sb.append("    a.COMPANY_SITE_ID companySiteId,");
            sb.append("    a.CUSTOMER_LIST_CODE customerListCode,");
            sb.append("    a.CUSTOMER_LIST_NAME customerListName,");
            sb.append("    a.STATUS status,");
            sb.append("    a.CREATE_BY createBy,");
            sb.append("    (a.CREATE_AT + (:p_timezone_offset / 60)/24) createAt,");
            sb.append("    a.UPDATE_BY updateBy,");
            sb.append("    (a.UPDATE_AT + (:p_timezone_offset / 60)/24) updateAt,");
            sb.append("    a.SOURCE source,");
            sb.append("    a.DEPT_CREATE deptCreate,");
            sb.append("    b.count count");
            sb.append(" FROM CUSTOMER_LIST a");
            sb.append(" LEFT JOIN (SELECT COUNT(CAMPAIGN_ID) as count, CUSTOMER_LIST_ID");
            sb.append(" FROM CAMPAIGN_CUSTOMERLIST");
            sb.append(" GROUP BY CUSTOMER_LIST_ID) b");
            sb.append(" ON a.CUSTOMER_LIST_ID = b.CUSTOMER_LIST_ID");
            sb.append(" WHERE 1 = 1");
            sb.append("  AND a.STATUS = 1");
            sb.append("  AND a.COMPANY_SITE_ID = :p_company_site_id");

            sb.append(" AND CREATE_AT >= to_date(:p_date_from, 'DD/MM/YYYY HH24:MI:SS') AND CREATE_AT <= to_date(:p_date_to, 'DD/MM/YYYY HH24:MI:SS')");

            if (!DataUtil.isNullOrEmpty(searchCustomerListRequestDTO.getCustomerListCode())) {
                sb.append(" AND UPPER(CUSTOMER_LIST_CODE) LIKE UPPER(:p_list_code)");
            }
            if (!DataUtil.isNullOrEmpty(searchCustomerListRequestDTO.getCustomerListName())) {
                sb.append(" AND UPPER(CUSTOMER_LIST_NAME) LIKE UPPER(:p_list_name)");
            }

            sb.append(" ORDER BY CREATE_AT DESC");

            SQLQuery query = session.createSQLQuery(sb.toString());

            query.setParameter("p_timezone_offset", searchCustomerListRequestDTO.getTimezoneOffset());
            query.setParameter("p_company_site_id", searchCustomerListRequestDTO.getCompanySiteId());
            query.setParameter("p_date_from", TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(searchCustomerListRequestDTO.getConvertedDateFrom() + " 00:00:00", "yyyyMMdd HH:mm:ss", tzClient)));
            query.setParameter("p_date_to", TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(searchCustomerListRequestDTO.getConvertedDateTo() + " 23:59:59", "yyyyMMdd HH:mm:ss", tzClient)));

            logger.info("list cust search from: " + TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(searchCustomerListRequestDTO.getConvertedDateFrom() + " 00:00:00", "yyyyMMdd HH:mm:ss", tzClient))
                    + " to: " + TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(searchCustomerListRequestDTO.getConvertedDateTo() + " 23:59:59", "yyyyMMdd HH:mm:ss", tzClient)));
            if (!DataUtil.isNullOrEmpty(searchCustomerListRequestDTO.getCustomerListCode())) {
                query.setParameter("p_list_code", "%" +
                        searchCustomerListRequestDTO.getCustomerListCode().trim()
                                .replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            if (!DataUtil.isNullOrEmpty(searchCustomerListRequestDTO.getCustomerListName())) {
                query.setParameter("p_list_name", "%" +
                        searchCustomerListRequestDTO.getCustomerListName().trim()
                                .replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            query.addScalar("customerListId", new LongType());
            query.addScalar("companySiteId", new LongType());
            query.addScalar("customerListCode", new StringType());
            query.addScalar("customerListName", new StringType());
            query.addScalar("status", new ShortType());
            query.addScalar("createBy", new StringType());
            query.addScalar("createAt", new DateType());
            query.addScalar("updateBy", new StringType());
            query.addScalar("updateAt", new DateType());
            query.addScalar("source", new StringType());
            query.addScalar("deptCreate", new StringType());
            query.addScalar("count", new StringType());

            query.setResultTransformer(Transformers.aliasToBean(CustomerListDTO.class));

            int total = 0;
            total = query.list().size();
            if (total > 0) {
                if (pageable != null) {
                    query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                    query.setMaxResults(pageable.getPageSize());
                }

                listData = query.list();
            }

            dataPage = new PageImpl<>(listData, pageable, total);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (session != null) session.close();
        }

        return dataPage;
    }
}
