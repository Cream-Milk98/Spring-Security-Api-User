package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.repository.ccms_full.CampaignExecuteRepository;
import com.viettel.campaign.repository.ccms_full.ContactQuestResultRepository;
import com.viettel.campaign.repository.ccms_full.TimeRangeDialModeRepository;
import com.viettel.campaign.repository.ccms_full.TimeZoneDialModeRepository;
import com.viettel.campaign.utils.*;
import com.viettel.campaign.web.dto.CampaignDTO;
import com.viettel.campaign.web.dto.ContactCustResultDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.request_dto.CampaignRequestDTO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository
public class CampaignExecuteRepositoryImp implements CampaignExecuteRepository {

    private static final Logger logger = LoggerFactory.getLogger(CampaignExecuteRepositoryImp.class);
    private static final String CONNECT_STATUS_TYPE_RE = "3";

    @Autowired
    @Qualifier(DataSourceQualify.NAMED_JDBC_PARAMETER_TEMPLATE_CCMS_FULL)
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    @PersistenceContext(unitName = DataSourceQualify.JPA_UNIT_NAME_CCMS_FULL)
    EntityManager entityManager;

    @Autowired
    TimeZoneDialModeRepository zoneDialModeRepository;

    @Autowired
    TimeRangeDialModeRepository rangeDialModeRepository;

    @Autowired
    ContactQuestResultRepository contactQuestResultRepository;

    private boolean isLower24Hour(Date createTime) {
        Date currTime = new Date();
        long diffMilSec = currTime.getTime() - createTime.getTime();
        long hour = TimeUnit.MILLISECONDS.toHours(diffMilSec);
        if (hour > 24) return false;
        return true;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, readOnly = true)
    public Page<CampaignDTO> searchCampaignExecute(CampaignRequestDTO campaignRequestDto, Pageable pageable) {
        List<CampaignDTO> result = new ArrayList<>();
        Page<CampaignDTO> dataPage = new PageImpl<>(result, pageable, 0);
        TimeZone tzClient = TimeZoneUtils.getZoneMinutes(campaignRequestDto.getTimezone());
        Integer tz = campaignRequestDto.getTimezone().intValue() / 60;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;

        StringBuilder expression = new StringBuilder()
                .append(" SELECT C.CAMPAIGN_CODE, C.CAMPAIGN_NAME, C.CONTENT, (C.START_TIME + (:pTimeZoneOffset / 24)) START_TIME, (C.END_TIME + (:pTimeZoneOffset / 24)) END_TIME, C.STATUS, CA.STATUS AS AGENT_STATUS, C.CAMPAIGN_ID ")
                .append(" FROM CAMPAIGN C INNER JOIN CAMPAIGN_AGENT CA ON C.CAMPAIGN_ID = CA.CAMPAIGN_ID ")
                .append(" WHERE 1 = 1 ")
                .append(" AND CA.AGENT_ID = :pAgentId ");


        if (!DataUtil.isNullOrEmpty(campaignRequestDto.getCampaignCode().trim())) {
            expression.append(" AND C.CAMPAIGN_CODE IN (:pCampaignCode) ");
        }

        if (!DataUtil.isNullOrEmpty(campaignRequestDto.getCampaignName())) {
            expression.append(" AND UPPER(C.CAMPAIGN_NAME) LIKE UPPER(:pCampaignName) ");
        }

        if (!DataUtil.isNullOrZero(campaignRequestDto.getStatus())) {
            expression.append(" AND C.STATUS = :pStatus " +
                    "AND CA.STATUS = :pAgentStatus  ");
        } else {
            expression.append(" AND C.STATUS IN (2, 3) ");
        }

        if (!DataUtil.isNullOrEmpty(campaignRequestDto.getFromDateFr())) {
            expression.append(" AND C.START_TIME >= TO_DATE(:pStartTimeFr, 'DD/MM/YYYY HH24:MI:SS') ");
        }

        if (!DataUtil.isNullOrEmpty(campaignRequestDto.getFromDateTo())) {
            expression.append(" AND C.START_TIME <= TO_DATE(:pStartTimeTo, 'DD/MM/YYYY HH24:MI:SS') ");
        }

        if (!DataUtil.isNullOrEmpty(campaignRequestDto.getToDateFr())) {
            expression.append(" AND C.END_TIME >= TO_DATE(:pEndTimeFr, 'DD/MM/YYYY HH24:MI:SS') ");
        }

        if (!DataUtil.isNullOrEmpty(campaignRequestDto.getToDateTo())) {
            expression.append(" AND  C.END_TIME <= TO_DATE(:pEndTimeTo, 'DD/MM/YYYY HH24:MI:SS') ");
        }

        if (!DataUtil.isNullOrZero(campaignRequestDto.getNumOfCusFr())) {
            expression.append(" AND C.CUSTOMER_NUMBER >= :pCustNumFr ");
        }

        if (!DataUtil.isNullOrZero(campaignRequestDto.getNumOfCusTo())) {
            expression.append(" AND C.CUSTOMER_NUMBER <= :pCustNumTo ");
        }

        expression.append(" ORDER BY C.START_TIME DESC ");

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            SQLQuery query = session.createSQLQuery(expression.toString());
            query.setParameter("pAgentId", DataUtil.safeToLong(campaignRequestDto.getAgentId()));
            query.setParameter("pTimeZoneOffset", tz);

//            if (!DataUtil.isNullOrEmpty(campaignRequestDto.getFromDateFr()) || !DataUtil.isNullOrEmpty(campaignRequestDto.getFromDateTo())
//                    || !DataUtil.isNullOrEmpty(campaignRequestDto.getToDateFr()) || !DataUtil.isNullOrEmpty(campaignRequestDto.getToDateTo())) {
//                query.setParameter("pTimeZoneOffset", tz);
//            }

            if (!DataUtil.isNullOrEmpty(campaignRequestDto.getCampaignCode()) && !DataUtil.isNullOrEmpty(campaignRequestDto.getCampaignCode().trim())) {
                String[] lstCode = campaignRequestDto.getCampaignCode().trim().split(",");
                query.setParameterList("pCampaignCode", lstCode);
            }

            if (!DataUtil.isNullOrEmpty(campaignRequestDto.getCampaignName())) {
                query.setParameter("pCampaignName", "%" +
                        campaignRequestDto.getCampaignName().toUpperCase().trim()
                                .replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            if (!DataUtil.isNullOrZero(campaignRequestDto.getStatus())) {
                if (campaignRequestDto.getStatus() != 0) {
                    query.setParameter("pStatus", campaignRequestDto.getStatus());
                    query.setParameter("pAgentStatus", campaignRequestDto.getAgentStatus());
                }
            }

            if (!DataUtil.isNullOrEmpty(campaignRequestDto.getFromDateFr())) {
                query.setParameter("pStartTimeFr", TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(campaignRequestDto.getFromDateFr() + " 00:00:00", "dd/MM/yyyy HH:mm:ss", tzClient)));
            }

            if (!DataUtil.isNullOrEmpty(campaignRequestDto.getFromDateTo())) {
                query.setParameter("pStartTimeTo", TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(campaignRequestDto.getFromDateTo() + " 23:59:59", "dd/MM/yyyy HH:mm:ss", tzClient)));
            }

            if (!DataUtil.isNullOrEmpty(campaignRequestDto.getToDateFr())) {
                query.setParameter("pEndTimeFr", TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(campaignRequestDto.getToDateFr() + " 00:00:00", "dd/MM/yyyy HH:mm:ss", tzClient)));
            }

            if (!DataUtil.isNullOrEmpty(campaignRequestDto.getToDateTo())) {
                query.setParameter("pEndTimeTo", TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(campaignRequestDto.getToDateTo() + " 23:59:59", "dd/MM/yyyy HH:mm:ss", tzClient)));
            }

            if (!DataUtil.isNullOrZero(campaignRequestDto.getNumOfCusFr())) {
                query.setParameter("pCustNumFr", campaignRequestDto.getNumOfCusFr());
            }

            if (!DataUtil.isNullOrZero(campaignRequestDto.getNumOfCusTo())) {
                query.setParameter("pCustNumTo", campaignRequestDto.getNumOfCusTo());
            }

            Integer count = 0;
            count = query.list().size();
            if (count > 0) {

            }

            if (pageable != null && pageable != Pageable.unpaged()) {
                query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                query.setMaxResults(pageable.getPageSize());
            }

            List<Object[]> data = query.list();

            for (Object[] obj : data) {
                CampaignDTO item = new CampaignDTO();
                item.setPage(campaignRequestDto.getPage());
                item.setPageSize(campaignRequestDto.getPageSize());
                item.setSort(campaignRequestDto.getSort());
                item.setCampaignCode((String) obj[0]);
                item.setCampaignName((String) obj[1]);
                item.setContent((String) obj[2]);
                item.setStartTime(DateTimeUtil.addHoursToJavaUtilDate((Date) obj[3], tz));
                item.setEndTime(DateTimeUtil.addHoursToJavaUtilDate((Date) obj[4], tz));
                item.setStatus(((BigDecimal) obj[5]).shortValueExact());
                item.setAgentStatus(((BigDecimal) obj[6]).shortValueExact());
                item.setCampaignId(((BigDecimal) obj[7]).longValueExact());

                result.add(item);
            }

            dataPage = new PageImpl<>(result, pageable, count);

            return dataPage;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (null != session) {
                session.close();
            }
        }

        return dataPage;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, readOnly = true)
    public Page<ContactCustResultDTO> getInteractiveResult(CampaignRequestDTO dto, Pageable pageable, boolean isExport) {
        ResultDTO resultDTO = new ResultDTO();
        List<ContactCustResultDTO> list = new ArrayList<>();
        Page<ContactCustResultDTO> dataPage = new PageImpl<>(list, pageable, 0);
        TimeZone tzClient = TimeZoneUtils.getZoneMinutes((long) dto.getTimezoneOffset());
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            StringBuilder sql = new StringBuilder();

            sql.append(" SELECT CCR.CONTACT_CUST_RESULT_ID   AS contactCustResultId, ");
            sql.append("    (CCR.CREATE_TIME + (:p_timezone_offset / 60)/24)                AS createTime, ");
            sql.append("    C.CAMPAIGN_CODE                 AS campaignCode, ");
            sql.append("    C.CAMPAIGN_NAME                 AS campaignName, ");
            sql.append("    VU.USER_NAME                    AS userName, ");
            sql.append("    CCR.PHONE_NUMBER                AS phoneNumber, ");
            sql.append("    CUS.NAME                        AS customerName, ");
            if (isExport) {
                sql.append("    (CCR.START_CALL + (:p_timezone_offset / 60)/24)                  AS startCall, ");
            } else {
                sql.append("    CCR.START_CALL                  AS startCall, ");
            }
            sql.append("    CC1.COMPLETE_VALUE               AS connectStatus, ");
            sql.append("    CC2.COMPLETE_VALUE               AS surveyStatus, ");
            sql.append("    CC1.COMPLETE_NAME               AS connectStatusName, ");
            sql.append("    CC2.COMPLETE_NAME               AS surveyStatusName, ");
            sql.append("    C.STATUS                        AS status, ");
            sql.append("    CCR.STATUS                      AS recordStatus, ");
//            sql.append("    ROUND(( CCR.END_TIME - CCR.START_CALL ) * 24 * 60 * 60) AS callTime, ");
            sql.append("    CCR.DURATION_CALL AS callTime, ");
            sql.append("    C.CAMPAIGN_ID                   AS campaignId, ");
            sql.append("    CUS.CUSTOMER_ID                 AS customerId ");
            sql.append(" FROM CONTACT_CUST_RESULT CCR ");
            sql.append("    INNER JOIN CAMPAIGN                 C ON CCR.CAMPAIGN_ID = C.CAMPAIGN_ID ");
            sql.append("    INNER JOIN VSA_USERS                VU ON CCR.AGENT_ID = VU.USER_ID ");
            sql.append("    INNER JOIN CUSTOMER                 CUS ON CCR.CUSTOMER_ID = CUS.CUSTOMER_ID ");
            sql.append("    LEFT JOIN CAMPAIGN_COMPLETE_CODE CC1 ON CCR.CONTACT_STATUS = CC1.COMPLETE_VALUE ");
            sql.append("    LEFT JOIN CAMPAIGN_COMPLETE_CODE CC2 ON CCR.CALL_STATUS = CC2.COMPLETE_VALUE ");
            sql.append(" WHERE CCR.STATUS <> 0 ");
            sql.append("  AND CCR.COMPANY_SITE_ID = :p_company_site_id ");
            sql.append("  AND CCR.CREATE_TIME >= to_date(:p_date_from, 'DD/MM/YYYY HH24:MI:SS') ");
            sql.append("  AND CCR.CREATE_TIME <= to_date(:p_date_to, 'DD/MM/YYYY HH24:MI:SS') ");

            if (!DataUtil.isNullOrEmpty(dto.getCustomerId())) {
                sql.append(" AND CCR.CUSTOMER_ID LIKE (:p_customer_id) ");
            }

            if (!DataUtil.isNullOrEmpty(dto.getCampaignType())) {
                sql.append(" AND C.CAMPAIGN_TYPE IN (:p_list_compaign_type) ");
            }

            if (!DataUtil.isNullOrEmpty(dto.getContactStatus())) {
                sql.append(" AND CCR.CONTACT_STATUS IN (:p_list_contact_status) ");
            }

            if (!DataUtil.isNullOrEmpty(dto.getSurveyStatus())) {
                sql.append(" AND CCR.CALL_STATUS IN (:p_list_survey_status) ");
            }

            if (!DataUtil.isNullOrEmpty(dto.getRecordStatus())) {
                sql.append(" AND CCR.STATUS IN (:p_list_record_status) ");
            }

            if (!DataUtil.isNullOrEmpty(dto.getCampaignCode())) {
                sql.append(" AND C.CAMPAIGN_CODE IN (:p_list_campaign_code) ");
            }

            if (!DataUtil.isNullOrEmpty(dto.getPhoneNumber())) {
                sql.append(" AND CCR.PHONE_NUMBER LIKE (:p_phone_number) ");
            }

            if (!DataUtil.isNullOrEmpty(dto.getCampaignName())) {
                sql.append(" AND UPPER(C.CAMPAIGN_NAME) LIKE (:p_campaign_name) ");
            }

            if (!DataUtil.isNullOrEmpty(dto.getAgentId())) {
                sql.append(" AND UPPER(VU.USER_NAME) LIKE (:p_user_name) ");
            }

            if (!DataUtil.isNullOrEmpty(dto.getCallTimeFrom())) {
                sql.append(" AND CCR.DURATION_CALL >= (:p_call_time_from) ");
            }

            if (!DataUtil.isNullOrEmpty(dto.getCallTimeTo())) {
                sql.append(" AND CCR.DURATION_CALL <= (:p_call_time_to) ");
            }

            sql.append(" ORDER BY CCR.CREATE_TIME DESC ");

            SQLQuery query = session.createSQLQuery(sql.toString());
            query.setParameter("p_company_site_id", dto.getCompanySiteId());
            //query.setParameter("p_date_from", dto.getFromDate());
            query.setParameter("p_date_from", TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(dto.getFromDate() + " 00:00:00", "dd/MM/yyyy HH:mm:ss", tzClient)));
            //query.setParameter("p_date_to", dto.getToDate());
            query.setParameter("p_date_to", TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(dto.getToDate() + " 23:59:59", "dd/MM/yyyy HH:mm:ss", tzClient)));
            query.setParameter("p_timezone_offset", dto.getTimezoneOffset());

            if (!DataUtil.isNullOrEmpty(dto.getCustomerId())) {
                query.setParameter("p_customer_id", "%" +
                        dto.getCustomerId().toUpperCase()
                                .replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            if (!DataUtil.isNullOrEmpty(dto.getCampaignType())) {
                String[] lstCode = dto.getCampaignType().split(",");
                query.setParameterList("p_list_compaign_type", lstCode);
            }

            if (!DataUtil.isNullOrEmpty(dto.getContactStatus())) {
                String[] lstCode = dto.getContactStatus().split(",");
                query.setParameterList("p_list_contact_status", lstCode);
            }

            if (!DataUtil.isNullOrEmpty(dto.getSurveyStatus())) {
                String[] lstCode = dto.getSurveyStatus().split(",");
                query.setParameterList("p_list_survey_status", lstCode);
            }

            if (!DataUtil.isNullOrEmpty(dto.getRecordStatus())) {
                String[] lstCode = dto.getRecordStatus().split(",");
                query.setParameterList("p_list_record_status", lstCode);
            }

            if (!DataUtil.isNullOrEmpty(dto.getCampaignCode())) {
                String[] lstCode = dto.getCampaignCode().trim().split(",");
                query.setParameterList("p_list_campaign_code", lstCode);
            }

            if (!DataUtil.isNullOrEmpty(dto.getPhoneNumber())) {
                query.setParameter("p_phone_number", "%" +
                        dto.getPhoneNumber().toUpperCase().trim()
                                .replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            if (!DataUtil.isNullOrEmpty(dto.getCampaignName())) {
                query.setParameter("p_campaign_name", "%" +
                        dto.getCampaignName().toUpperCase()
                                .replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            if ("SUPERVISOR".equalsIgnoreCase(dto.getRoleUser())) {
                if (!DataUtil.isNullOrEmpty(dto.getAgentId())) {
                    query.setParameter("p_user_name", "%" +
                            dto.getAgentId().toUpperCase()
                                    .replace("\\", "\\\\")
                                    .replaceAll("%", "\\%")
                                    .replaceAll("_", "\\_")
                            + "%");
                }
            } else {
                if (!DataUtil.isNullOrEmpty(dto.getAgentId())) {
                    query.setParameter("p_user_name", "%" +
                            dto.getAgentId().toUpperCase()
                                    .replace("\\", "\\\\")
                                    .replaceAll("%", "\\%")
                                    .replaceAll("_", "\\_")
                            + "%");
                }
            }

            if (!DataUtil.isNullOrEmpty(dto.getCallTimeFrom())) {
                query.setParameter("p_call_time_from", dto.getCallTimeFrom());
            }

            if (!DataUtil.isNullOrEmpty(dto.getCallTimeTo())) {
                query.setParameter("p_call_time_to", dto.getCallTimeTo());
            }

            // add data to parameter
            query.addScalar("contactCustResultId", new LongType());
            query.addScalar("createTime", new TimestampType());
            query.addScalar("campaignCode", new StringType());
            query.addScalar("campaignName", new StringType());
            query.addScalar("userName", new StringType());
            query.addScalar("phoneNumber", new StringType());
            query.addScalar("customerName", new StringType());
            query.addScalar("startCall", new TimestampType());
            query.addScalar("connectStatus", new StringType());
            query.addScalar("surveyStatus", new StringType());
            query.addScalar("status", new ShortType());
            query.addScalar("recordStatus", new ShortType());
            query.addScalar("callTime", new LongType());
            query.addScalar("campaignId", new LongType());
            query.addScalar("customerId", new LongType());
            query.addScalar("connectStatusName", new StringType());
            query.addScalar("surveyStatusName", new StringType());

            Integer count = 0;
            count = query.list().size();

            if (pageable != null && pageable != Pageable.unpaged()) {
                query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                query.setMaxResults(pageable.getPageSize());
            }

            query.setResultTransformer(Transformers.aliasToBean(ContactCustResultDTO.class));
            list = query.list();

            for (ContactCustResultDTO item : list) {
                if (!"AGENT".equals(dto.getRoleUser())) { // ko phải nhân viên
                    item.setEnableEdit(true);
                } else {
                    if (2 == item.getRecordStatus()) {// là nhân viên thường
                        item.setEnableEdit(true);
                    } else if (1 == item.getRecordStatus() && isLower24Hour(item.getCreateTime())) {
                        item.setEnableEdit(true);
                    } else {
                        item.setEnableEdit(false);
                    }
                }
            }

            dataPage = new PageImpl<>(list, pageable, count);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        } finally {
            session.close();
        }

        return dataPage;
    }
}
