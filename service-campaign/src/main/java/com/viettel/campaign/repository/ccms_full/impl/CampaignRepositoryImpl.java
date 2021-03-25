package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.repository.ccms_full.CampaignCfgRepository;
import com.viettel.campaign.repository.ccms_full.CampaignCustomerRepository;
import com.viettel.campaign.repository.ccms_full.CampaignRepositoryCustom;
import com.viettel.campaign.utils.*;
import com.viettel.campaign.web.dto.*;
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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
public class CampaignRepositoryImpl implements CampaignRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(CampaignRepositoryImpl.class);

    @Autowired
    @PersistenceContext(unitName = DataSourceQualify.JPA_UNIT_NAME_CCMS_FULL)
    EntityManager entityManager;

    @Autowired
    @Qualifier(DataSourceQualify.NAMED_JDBC_PARAMETER_TEMPLATE_CCMS_FULL)
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    CampaignCustomerRepository campaignCustomerRepository;

    @Autowired
    CampaignCfgRepository cfgRepository;

    @Override
    public ResultDTO search(CampaignRequestDTO requestDto) {
        TimeZone tzClient = TimeZoneUtils.getZoneMinutes((long) requestDto.getTimezoneOffset());
        logger.info("Start search campaign::");

        ResultDTO result = new ResultDTO();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        Session session = null;

        if (DataUtil.isNullOrEmpty(requestDto.getCompanySiteId())) {
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
            return result;
        }
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            StringBuilder sb = new StringBuilder();

            sb.append(" SELECT");
            sb.append("    a.CAMPAIGN_ID campaignId,");
            sb.append("    a.CAMPAIGN_CODE campaignCode,");
            sb.append("    a.CAMPAIGN_NAME campaignName,");
            sb.append("    a.CAMPAIGN_TYPE campaignType,");
            sb.append("    (SELECT PAR_NAME FROM AP_PARAM WHERE STATUS = 1 AND PAR_TYPE = 'CAMPAIGN_TYPE' AND PAR_VALUE = a.CAMPAIGN_TYPE) campaignTypeName,");
            sb.append("    a.CHANEL chanel,");
            sb.append("    (a.START_TIME + (:p_timezone_offset / 60)/24) startTime,");
            sb.append("    (a.END_TIME + (:p_timezone_offset / 60)/24) endTime,");
            sb.append("    a.CUSTOMER_NUMBER customerNumber,");
            sb.append("    a.STATUS status,");
            sb.append("    a.CUSTOMER_NUMBER cusNum,");
            sb.append("    b.SLKHThamgiaCD numOfJoinedCus,");
            sb.append("    e.SLKHDaTuongTac numOfInteractedCus,");
            sb.append("    c.SLKHChuaTuongTac numOfNotInteractedCus,");
            sb.append("    d.SLKHDoNotCall_Khoa numOfLockCus,");
            sb.append("    a.COMPANY_SITE_ID companySiteId,");
            sb.append("    a.CONTENT content,");
            sb.append("    a.MAX_RECALL maxRecall,");
            sb.append("    a.RECALL_TYPE recallType,");
            sb.append("    a.RECALL_DURATION recallDuration,");
            sb.append("    a.CURRENT_TIME_MODE currentTimeMode,");
            sb.append("    a.WRAPUP_TIME_CONNECT wrapupTimeConnect,");
            sb.append("    a.WRAPUP_TIME_DISCONNECT wrapupTimeDisconnect");
            sb.append(" FROM CAMPAIGN a");
            sb.append(" LEFT JOIN (SELECT campaign_id, COUNT (*) AS SLKHThamgiaCD");
            sb.append("                  FROM   campaign_customer cc INNER JOIN CUSTOMER cus ON cc.CUSTOMER_ID = cus.CUSTOMER_ID");
            sb.append("                  WHERE 1 = 1 AND cus.STATUS = 1 AND cc.IN_CAMPAIGN_STATUS = 1");
            sb.append("                  group by campaign_id) b");
            sb.append(" ON a.CAMPAIGN_ID = b.CAMPAIGN_ID");
            sb.append(" LEFT JOIN (SELECT campaign_id, COUNT (*) AS SLKHChuaTuongTac");
            sb.append("                  FROM   campaign_customer cc INNER JOIN CUSTOMER cus ON cc.CUSTOMER_ID = cus.CUSTOMER_ID");
            sb.append("                  WHERE 1 = 1 AND cc.STATUS = 0 AND cus.STATUS = 1 AND cc.IN_CAMPAIGN_STATUS = 1");
            sb.append("                  group by campaign_id) c");
            sb.append(" ON c.CAMPAIGN_ID = a.CAMPAIGN_ID");
            sb.append(" LEFT JOIN (SELECT cc.campaign_id, count(*) AS SLKHDoNotCall_Khoa");
            sb.append("        FROM CAMPAIGN_CUSTOMER cc , CUSTOMER c");
            sb.append("        WHERE cc.CUSTOMER_ID = c.CUSTOMER_ID");
            sb.append("        AND (c.IPCC_STATUS = 'locked' or c.CALL_ALLOWED = 0) AND c.STATUS = 1");
            sb.append("        GROUP BY cc.CAMPAIGN_ID) d");
            sb.append(" ON d.CAMPAIGN_ID = a.CAMPAIGN_ID");
            sb.append(" LEFT JOIN (SELECT campaign_id, COUNT (*) AS SLKHDaTuongTac");
            sb.append("                  FROM   campaign_customer cc INNER JOIN CUSTOMER cus ON cc.CUSTOMER_ID = cus.CUSTOMER_ID");
            sb.append("                  WHERE 1 = 1 AND cc.STATUS <> 0 AND cus.STATUS = 1 AND cc.IN_CAMPAIGN_STATUS = 1");
            sb.append("                  group by campaign_id) e");
            sb.append(" ON e.CAMPAIGN_ID = a.CAMPAIGN_ID");
            sb.append(" WHERE 1 = 1");
            sb.append(" AND COMPANY_SITE_ID = :p_company_site_id");
            sb.append(" AND a.STATUS <> -1");


            if (!DataUtil.isNullOrEmpty(requestDto.getCampaignCode()) && !DataUtil.isNullOrEmpty(requestDto.getCampaignCode().trim())) {
                sb.append(" AND a.CAMPAIGN_CODE IN (:p_code) ");
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getCampaignName())) {
                sb.append(" AND UPPER(a.CAMPAIGN_NAME) LIKE :p_name");
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getStatuses())) {
                sb.append(" AND a.STATUS IN (:p_statuses) ");
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getFromDateFr())) {
                sb.append(" AND a.START_TIME >= TO_DATE(:p_frDateFr, 'DD/MM/YYYY HH24:MI:SS')");
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getToDateFr())) {
                sb.append(" AND a.START_TIME <= TO_DATE(:p_toDateFr, 'DD/MM/YYYY HH24:MI:SS')");
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getFromDateTo())) {
                sb.append(" AND a.END_TIME >= TO_DATE(:p_frDateTo, 'DD/MM/YYYY HH24:MI:SS')");
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getToDateTo())) {
                sb.append(" AND a.END_TIME <= TO_DATE(:p_toDateTo, 'DD/MM/YYYY HH24:MI:SS')");
            }
            if (!DataUtil.isNullOrZero(requestDto.getNumOfCusFr())) {
                sb.append(" AND a.CUSTOMER_NUMBER >= :p_cusNumFr");
            }
            if (!DataUtil.isNullOrZero(requestDto.getNumOfCusTo())) {
                sb.append(" AND a.CUSTOMER_NUMBER <= :p_cusNumTo");
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getTypes())) {
                sb.append(" AND a.CAMPAIGN_TYPE IN (:p_type)");
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getChannels())) {
                sb.append(" AND a.CHANEL IN (:p_channels)");
            }
            if (!DataUtil.isNullOrZero(requestDto.getNumOfCusFr())) {
                sb.append(" AND a.CUSTOMER_NUMBER >= :p_cusNumFr");
            }
            if (!DataUtil.isNullOrZero(requestDto.getNumOfCusTo())) {
                sb.append(" AND a.CUSTOMER_NUMBER <= :p_cusNumTo");
            }

            sb.append(" ORDER BY a.CREATE_TIME DESC, a.UPDATE_TIME DESC ");

            SQLQuery query = session.createSQLQuery(sb.toString());
            if (!DataUtil.isNullOrEmpty(requestDto.getCampaignCode()) && !DataUtil.isNullOrEmpty(requestDto.getCampaignCode().trim())) {
                String[] lstCode = requestDto.getCampaignCode().trim().split(",");
                query.setParameterList("p_code", lstCode);
            }

            if (!DataUtil.isNullOrEmpty(requestDto.getCampaignName())) {
                query.setParameter("p_name", "%" +
                        requestDto.getCampaignName().trim().toUpperCase()
                                .replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            query.setParameter("p_company_site_id", requestDto.getCompanySiteId());

            query.setParameter("p_timezone_offset", requestDto.getTimezoneOffset());

            if (!DataUtil.isNullOrEmpty(requestDto.getStatuses())) {
                String[] statuses = requestDto.getStatuses().split(",");
                query.setParameterList("p_statuses", statuses);
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getFromDateFr())) {
                query.setParameter("p_frDateFr", TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(requestDto.getFromDateFr() + " 00:00:00", "yyyyMMdd HH:mm:ss", tzClient)));
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getFromDateTo())) {
                query.setParameter("p_frDateTo", TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(requestDto.getFromDateTo() + " 00:00:00", "yyyyMMdd HH:mm:ss", tzClient)));
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getToDateFr())) {
                query.setParameter("p_toDateFr", TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(requestDto.getToDateFr() + " 23:59:59", "yyyyMMdd HH:mm:ss", tzClient)));
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getToDateTo())) {
                query.setParameter("p_toDateTo", TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(requestDto.getToDateTo() + " 23:59:59", "yyyyMMdd HH:mm:ss", tzClient)));
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getTypes())) {
                String[] types = requestDto.getTypes().split(",");
                query.setParameterList("p_type", types);
            }
            if (!DataUtil.isNullOrEmpty(requestDto.getChannels())) {
                String[] channels = requestDto.getChannels().split(",");
                query.setParameterList("p_channels", channels);
            }
            if (!DataUtil.isNullOrZero(requestDto.getNumOfCusFr())) {
                query.setParameter("p_cusNumFr", requestDto.getNumOfCusFr() == 0 ? null : requestDto.getNumOfCusFr());
            }
            if (!DataUtil.isNullOrZero(requestDto.getNumOfCusTo())) {
                query.setParameter("p_cusNumTo", requestDto.getNumOfCusTo() == 0 ? null : requestDto.getNumOfCusTo());
            }

            query.addScalar("campaignId", new LongType());
            query.addScalar("campaignCode", new StringType());
            query.addScalar("campaignName", new StringType());
            query.addScalar("campaignType", new StringType());
            query.addScalar("campaignTypeName", new StringType());
            query.addScalar("chanel", new ShortType());
            query.addScalar("startTime", new DateType());
            query.addScalar("endTime", new DateType());
            query.addScalar("customerNumber", new LongType());
            query.addScalar("status", new ShortType());
            query.addScalar("numOfJoinedCus", new LongType());
            query.addScalar("numOfInteractedCus", new LongType());
            query.addScalar("numOfNotInteractedCus", new LongType());
            query.addScalar("numOfLockCus", new LongType());
            query.addScalar("companySiteId", new LongType());
            query.addScalar("content", new StringType());
            query.addScalar("maxRecall", new IntegerType());
            query.addScalar("recallType", new IntegerType());
            query.addScalar("recallDuration", new IntegerType());
            query.addScalar("currentTimeMode", new LongType());
            query.addScalar("wrapupTimeConnect", new LongType());
            query.addScalar("wrapupTimeDisconnect", new LongType());

            query.setResultTransformer(Transformers.aliasToBean(CampaignDTO.class));
            int count = 0;
            List<CampaignDTO> list = query.list();
            if (list.size() > 0) {
                count = query.list().size();
            }
            if (requestDto.getPage() != null && requestDto.getPageSize() != null) {
                Pageable pageable = SQLBuilder.buildPageable(requestDto);
                if (pageable != null) {
                    query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                    query.setMaxResults(pageable.getPageSize());
                }
                List<CampaignDTO> data = query.list();
//                data.forEach(item -> {
//                    if (item.getStartTime() != null)
//                        item.setStartTime(DateTimeUtil.parseDate("dd/MM/yyyy HH:mm:ss", TimeZoneUtils.toDateStringWithTimeZone(item.getStartTime(), tzClient)));
//                    if (item.getEndTime() != null)
//                        item.setEndTime(DateTimeUtil.parseDate("dd/MM/yyyy HH:mm:ss", TimeZoneUtils.toDateStringWithTimeZone(item.getEndTime(), tzClient)));
//                });
                Page<CampaignDTO> dataPage = new PageImpl<>(data, pageable, count);
                result.setData(dataPage);
            } else {
                List<CampaignDTO> dataExport = query.list();
//                dataExport.forEach(item -> {
//                    if (item.getStartTime() != null)
//                        item.setStartTime(DateTimeUtil.parseDate("dd/MM/yyyy HH:mm:ss", TimeZoneUtils.toDateStringWithTimeZone(item.getStartTime(), tzClient)));
//                    if (item.getEndTime() != null)
//                        item.setEndTime(DateTimeUtil.parseDate("dd/MM/yyyy HH:mm:ss", TimeZoneUtils.toDateStringWithTimeZone(item.getEndTime(), tzClient)));
//                });
                result.setData(dataExport);
            }
            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception ex) {
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
            logger.error(ex.getMessage(), ex);
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return result;
    }

    @Override
    public Page<CampaignDTO> findByCampaignCode(CampaignRequestDTO requestDto) {
        logger.info("Start search campaign by code::");
        Page<CampaignDTO> dataPage = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            StringBuilder sb = new StringBuilder();
            sb.append(" SELECT CAMPAIGN_ID campaignId," +
                    "CAMPAIGN_CODE campaignCode,        " +
                    "CAMPAIGN_NAME campaignName,       " +
                    "START_TIME startTime,    " +
                    "END_TIME endTime,     " +
                    "STATUS status  " +
                    "FROM CAMPAIGN " +
                    "WHERE COMPANY_SITE_ID = :p_company_site_id " +
                    " AND STATUS <> -1  " +
                    " AND (:p_code is null or upper(CAMPAIGN_CODE) LIKE '%'||:p_code||'%')" +
                    "ORDER BY START_TIME DESC");
            SQLQuery query = session.createSQLQuery(sb.toString());
            query.setParameter("p_company_site_id", requestDto.getCompanySiteId());
            query.setParameter("p_code", DataUtil.isNullOrEmpty(requestDto.getCampaignCode()) ? null : requestDto.getCampaignCode().trim().toUpperCase());

            query.addScalar("campaignId", new LongType());
            query.addScalar("campaignCode", new StringType());
            query.addScalar("campaignName", new StringType());
            query.addScalar("startTime", new DateType());
            query.addScalar("endTime", new DateType());
            query.addScalar("status", new ShortType());

            query.setResultTransformer(Transformers.aliasToBean(CampaignDTO.class));

            int count = 0;
            List<CampaignDTO> list = query.list();
            if (list.size() > 0) {
                count = query.list().size();
            }

            Pageable pageable = SQLBuilder.buildPageable(requestDto);
            if (pageable != null) {
                query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                query.setMaxResults(pageable.getPageSize());
            }

            dataPage = new PageImpl<>(query.list(), pageable, count);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (session != null) session.close();
        }

        return dataPage;
    }

    @Override
    public String getMaxCampaignIndex() {
        logger.info("Start search max campaign code index::");
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT CASE WHEN");
            sb.append("            (SELECT COUNT(1) FROM CAMPAIGN) > 0");
            sb.append("       THEN (SELECT SUBSTR((SELECT CAMPAIGN_CODE FROM CAMPAIGN WHERE CAMPAIGN_ID = (SELECT MAX(CAMPAIGN_ID) FROM CAMPAIGN)),");
            sb.append("                    INSTR((SELECT CAMPAIGN_CODE FROM CAMPAIGN WHERE CAMPAIGN_ID = (SELECT MAX(CAMPAIGN_ID) FROM CAMPAIGN)), '_', -1, 1 )+1) FROM DUAL)");
            sb.append("       ELSE '0'");
            sb.append("       END FROM DUAL");
            SQLQuery query = session.createSQLQuery(sb.toString());
            List<String> list = query.list();
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (session != null) session.close();
        }
        return null;
    }

    @Override
    public ResultDTO checkAllowStatusToPrepare(Long campaignId) {
        logger.info("Start check allow campaign status to prepare::");
        ResultDTO result = new ResultDTO();
        if (DataUtil.isNullOrZero(campaignId)) {
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
            return result;
        }
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(
                    "select " +
                            "      (select count(1) from CAMPAIGN_AGENT where CAMPAIGN_ID = :p_campaignId) count_agent," +
                            "      (select count(1) from CAMPAIGN_CUSTOMER where CAMPAIGN_ID = :p_campaignId) count_cus," +
                            "      (select count(tmp.SCENARIO_ID) from (select s.SCENARIO_ID, s.CAMPAIGN_ID, sa.SCENARIO_ANSWER_ID, sq.SCENARIO_QUESTION_ID" +
                            "                                             from SCENARIO s INNER JOIN SCENARIO_QUESTION sq on (s.SCENARIO_ID = sq.SCENARIO_ID and sq.STATUS = 1)" +
                            "                                                             LEFT JOIN SCENARIO_ANSWER sa on sq.SCENARIO_QUESTION_ID = sa.SCENARIO_QUESTION_ID" +
                            "                                             where s.CAMPAIGN_ID = :p_campaignId" +
                            "                                                   and ((sa.SCENARIO_ANSWER_ID is not null and sa.STATUS = 1) or" +
                            "                                                   (sq.TYPE = 3 and sa.SCENARIO_ANSWER_ID is null))" +
                            "                                             group by sa.SCENARIO_ANSWER_ID, s.SCENARIO_ID, sq.SCENARIO_QUESTION_ID, s.CAMPAIGN_ID) tmp) count_sce from dual"
            );

            SQLQuery query = session.createSQLQuery(sb.toString());
            query.setParameter("p_campaignId", campaignId);
            List<Object[]> list = query.list();
            if (list.size() > 0) {
                result.setData(list.get(0));
                result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO findCustomerListReallocation(CampaignRequestDTO dto) {
        ResultDTO resultDTO = new ResultDTO();
        List<CustomerCustomDTO> list = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(" with contact as (");
            stringBuilder.append("     select customer_id, contact");
            stringBuilder.append("     from (");
            stringBuilder.append("         select * from customer_contact");
            stringBuilder.append("         where status = 1");
            stringBuilder.append("             and contact_type = 5");
            stringBuilder.append("             and is_direct_line = 1");
            stringBuilder.append("         order by create_date desc");
            stringBuilder.append("     )");
            stringBuilder.append("     where rownum = 1");
            stringBuilder.append(" ),");
            stringBuilder.append(" connect_status as (");
            stringBuilder.append("     select complete_value, complete_name, complete_type");
            stringBuilder.append("     from campaign_complete_code");
            stringBuilder.append("     where company_site_id = :p_company_site_id");
            stringBuilder.append("         and complete_type = 1");
            stringBuilder.append("         and complete_value <> 1");
            stringBuilder.append(" ),");
            stringBuilder.append(" connect_status_list as (");
            stringBuilder.append("     select trim (regexp_substr(:p_list_connect_status, '[^,]+', 1, level)) connect_status");
            stringBuilder.append("     from dual");
            stringBuilder.append("     connect by level <= regexp_count(:p_list_connect_status, ',') +1");
            stringBuilder.append(" ),");
            stringBuilder.append(" data as (");
            stringBuilder.append("     select  a.campaign_customer_id campaignCustomerId,");
            stringBuilder.append("             b.name customerName,");
            stringBuilder.append("             c.contact mobileNumber,");
            stringBuilder.append("             to_char(a.call_time, 'DD/MM/YYYY HH24:MI:SS') connectTime,");
            stringBuilder.append("             d.complete_name connectStatus,");
            stringBuilder.append("             d.complete_type completeType");
            stringBuilder.append("     from campaign_customer a");
            stringBuilder.append("     left join customer b on a.customer_id = b.customer_id");
            stringBuilder.append("     left join contact c on a.customer_id = c.customer_id");
            stringBuilder.append("     left join connect_status d on d.complete_value = a.status");
            stringBuilder.append("     where a.campaign_id = :p_campaign_id");
            stringBuilder.append("         and a.in_campaign_status = 1");
            stringBuilder.append("         and ( :p_list_connect_status is null or a.status in (select connect_status from connect_status_list))");
            stringBuilder.append("         and d.complete_type = 1");
            stringBuilder.append("     order by connectTime desc, customerName");
            stringBuilder.append(" ),");
            stringBuilder.append(" count_data as (");
            stringBuilder.append(" select count(*) totalRow from data");
            stringBuilder.append(" ),");
            stringBuilder.append(" final_data as (");
            stringBuilder.append("     select a.*, rownum row_, totalRow from data a, count_data");
            stringBuilder.append(" )");
            stringBuilder.append(" select * from final_data");
            stringBuilder.append(" where row_ >= ((:p_page_number - 1) * :p_page_size + 1)");
            stringBuilder.append("     and row_ < (:p_page_number * :p_page_size + 1)");
            params.put("p_company_site_id", dto.getCompanySiteId());
            params.put("p_campaign_id", dto.getCampaignId());
            params.put("p_list_connect_status", dto.getConnectStatus());
            params.put("p_page_size", dto.getPageSize().toString());
            params.put("p_page_number", dto.getPage().toString());
            list = namedParameterJdbcTemplate.query(stringBuilder.toString(), params, BeanPropertyRowMapper.newInstance(CustomerCustomDTO.class));
            resultDTO.setListData(list);
            int total = 0;
            if (list.size() > 0) {
                total = list.get(0).getTotalRow();
            }
            resultDTO.setTotalRow(Long.valueOf(total));
            //resultDTO.setTotalRow(list.size());
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getListFieldsNotShow(CampaignRequestDTO dto) {
        ResultDTO resultDTO = new ResultDTO();
        Map<String, String> params = new HashMap<>();
        List<FieldsToShowDTO> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(" with column_name_temp as (");
            sb.append("     select column_name columnName, null customizeFieldId, 1 isFix from user_tab_columns, dual");
            sb.append("     where table_name = 'CUSTOMER' and column_name in ('CALL_ALLOWED','CODE','COMPANY_NAME','CURRENT_ADDRESS','CUSTOMER_TYPE','DESCRIPTION','EMAIL','EMAIL_ALLOWED','IPCC_STATUS','MOBILE_NUMBER','NAME','SMS_ALLOWED') ");
            sb.append(" )");
            sb.append(" select * from column_name_temp where columnName not in (select column_name from campaign_customerlist_column where campaign_id = :p_campaign_id and column_name is not null)");
            sb.append(" union all");
            sb.append(" select title columnName, customize_field_id customizeFieldId, 0 isFix from customize_fields, dual");
            sb.append(" where function_code = 'CUSTOMER'");
            sb.append("     and site_id = :p_company_site_id");
            sb.append("     and status = 1");
            sb.append("     and active = 1");
            sb.append("     and customize_field_id not in (select NVL(customize_field_id,0) from campaign_customerlist_column where campaign_customerlist_column.campaign_id = :p_campaign_id)");
            params.put("p_company_site_id", dto.getCompanySiteId());
            params.put("p_campaign_id", dto.getCampaignId());
            list = namedParameterJdbcTemplate.query(sb.toString(), params, BeanPropertyRowMapper.newInstance(FieldsToShowDTO.class));
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setListData(list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getListFieldsToShow(CampaignRequestDTO dto) {
        ResultDTO resultDTO = new ResultDTO();
        Map<String, String> params = new HashMap<>();
        List<FieldsToShowDTO> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(" with field_name as (");
            sb.append(" select a.campaign_cus_list_column_id id, to_char(a.column_name) columnName, a.order_index, a.customize_field_id customizeFieldId, 1 isFix");
            sb.append(" from campaign_customerlist_column a, dual");
            sb.append(" where a.campaign_id = :p_campaign_id");
            sb.append("     and a.company_site_id = :p_company_site_id");
            sb.append("     and column_name is not null");
            sb.append(" union all");
            sb.append(" select a.campaign_cus_list_column_id id, a.customize_field_title columnName, a.order_index, a.customize_field_id customizeFieldId, 0 isFix");
            sb.append(" from campaign_customerlist_column a");
            sb.append(" where a.campaign_id = :p_campaign_id");
            sb.append("     and a.company_site_id = :p_company_site_id");
            sb.append(" )");
            sb.append(" select id, columnName, customizeFieldId, isFix from field_name where columnName is not null order by order_index");
            params.put("p_campaign_id", dto.getCampaignId());
            params.put("p_company_site_id", dto.getCompanySiteId());
            list = namedParameterJdbcTemplate.query(sb.toString(), params, BeanPropertyRowMapper.newInstance(FieldsToShowDTO.class));
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setListData(list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCampaignCustomerList(CampaignRequestDTO dto) {
        List<CustomerListDTO> list = new ArrayList();
        ResultDTO resultDTO = new ResultDTO();
        Map<String, String> params = new HashMap<>();
        try {
            String sql = "with campaign_customer_id as ( " +
                    "    select ccl.CUSTOMER_LIST_ID from campaign_customerlist ccl " +
                    "    where ccl.campaign_id = :p_campaign_id and ccl.company_site_id = :p_company_site_id " +
                    "), " +
                    "customer_table as ( " +
                    "    select count(a.customer_id) totalCustomer, a.customer_list_id customerListId from customer_list_mapping a " +
                    "    left join customer b on a.customer_id = b.customer_id " +
                    "    where b.status = 1 " +
                    "    group by a.customer_list_id " +
                    "), " +
                    "campaign_customer_table as ( " +
                    "    select count(a.customer_id) campaignCustomer, a.customer_list_id customerListId, a.campaign_id from campaign_customer a " +
                    "    inner join customer cus ON a.CUSTOMER_ID = cus.CUSTOMER_ID " +
                    "    where a.campaign_id = :p_campaign_id and in_campaign_status = 1 and cus.STATUS = 1" +
                    "    group by a.customer_list_id, a.campaign_id " +
                    "), " +
                    "customer_interactive_table as ( " +
                    "    select count(a.customer_id) campaignCustomerCalled, a.customer_list_id customerListId, a.campaign_id from campaign_customer a " +
                    "    inner join customer cus ON a.CUSTOMER_ID = cus.CUSTOMER_ID " +
                    "    where a.status <> 0 and a.campaign_id = :p_campaign_id and cus.STATUS = 1" +
                    "    group by a.customer_list_id, a.campaign_id " +
                    "), " +
                    "customer_not_interactive_table as ( " +
                    "    select count(a.customer_id) cusNotInteractive, a.customer_list_id customerListId, a.campaign_id from campaign_customer a " +
                    "    inner join customer cus ON a.CUSTOMER_ID = cus.CUSTOMER_ID " +
                    "    where a.status = 0 and a.campaign_id = :p_campaign_id and a.in_campaign_status = 1 and cus.STATUS = 1 " +
                    "    group by a.customer_list_id, a.campaign_id " +
                    "), " +
                    "data_temp as ( " +
                    "select  a.customer_list_id customerListId, " +
                    "        a.customer_list_code customerListCode, " +
                    "        a.customer_list_name customerListName, " +
                    "        nvl(b.totalCustomer, 0) totalCusList, " +
                    "        nvl(c.campaignCustomer, 0) totalCusCampaign, " +
                    "        nvl(d.campaignCustomerCalled, 0) totalCusCalled, " +
                    "        nvl(e.cusNotInteractive, 0) totalCusNotInteract " +
                    "from customer_list a " +
                    "left join customer_table b on a.customer_list_id = b.customerListId " +
                    "left join campaign_customer_table c on a.customer_list_id = c.customerListId " +
                    "left join customer_interactive_table d on a.customer_list_id = d.customerListId " +
                    "left join customer_not_interactive_table e on a.customer_list_id = e.customerListId " +
//                    "where a.customer_list_id in (select CUSTOMER_LIST_ID from campaign_customer_id) " +
                    "), " +
                    "data as ( " +
                    "select a.*, rownum row_ from data_temp a " +
                    "where a.totalCusCampaign > 0" +
                    "), " +
                    "count_data as ( " +
                    "select count(*) totalRow from data " +
                    ") " +
                    "select a.customerListId, a.customerListCode, a.customerListName, a.totalCusList, a.totalCusCampaign, a.totalCusCalled, a.totalCusNotInteract, totalRow from data a, count_data " +
                    "where row_ >= ((:p_page_number - 1) * :p_page_size + 1) and row_ < (:p_page_number * :p_page_size + 1) " +
                    "order by a.customerListName";
            params.put("p_campaign_id", dto.getCampaignId());
            params.put("p_company_site_id", dto.getCompanySiteId());
            params.put("p_page_number", dto.getPage().toString());
            params.put("p_page_size", dto.getPageSize().toString());
            list = namedParameterJdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(CustomerListDTO.class));
            int total = 0;
            if (list.size() > 0) {
                total = list.get(0).getTotalRow();
            }
            resultDTO.setListData(list);
            resultDTO.setTotalRow(Long.valueOf(total));
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCustomerList(CampaignRequestDTO dto) {
        TimeZone tzClient = TimeZoneUtils.getZoneMinutes(Long.valueOf(dto.getTimezoneOffset() * 60));
        List<CustomerListDTO> list = new ArrayList();
        ResultDTO resultDTO = new ResultDTO();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        StringBuilder sb = new StringBuilder();
        try {
            sb.append(" with campaign_customer_id as (");
            sb.append("     select ccl.CUSTOMER_LIST_ID from campaign_customerlist ccl");
            sb.append("     where ccl.campaign_id = :p_campaign_id and ccl.company_site_id = :p_company_site_id");
            sb.append(" ),");
            sb.append(" customer_table as (");
            sb.append("     select count(a.customer_id) totalCustomer, a.customer_list_id customerListId from customer_list_mapping a");
            sb.append("     left join customer b on a.customer_id = b.customer_id");
            sb.append("     where b.status = 1");
            sb.append("     group by a.customer_list_id");
            sb.append(" ),");
            sb.append(" customer_active_table as (");
            sb.append("     select count(a.customer_id) customerActive, a.customer_list_id customerListId from customer_list_mapping a");
            sb.append("     left join customer b on a.customer_id = b.customer_id");
            sb.append("     where b.status = 1 and b.ipcc_status = 'active'");
            sb.append("     group by a.customer_list_id");
            sb.append(" ),");
            sb.append(" customer_lock_table as (");
            sb.append("     select count(a.customer_id) customerLock, a.customer_list_id customerListId from customer_list_mapping a");
            sb.append("     left join customer b on a.customer_id = b.customer_id");
            sb.append("     where b.status = 1 and b.ipcc_status = 'locked'");
            sb.append("     group by a.customer_list_id");
            sb.append(" ),");
            sb.append(" customer_dnc_table as (");
            sb.append("     select count(a.customer_id) customerDnc, a.customer_list_id customerListId from customer_list_mapping a");
            sb.append("     left join customer b on a.customer_id = b.customer_id");
            sb.append("     where b.status = 1 and b.call_allowed = 0");
            sb.append("     group by a.customer_list_id");
            sb.append(" ),");
            sb.append(" data_temp as (");
            sb.append(" select  a.customer_list_id customerListId,");
            sb.append("         a.customer_list_code customerListCode,");
            sb.append("         a.customer_list_name customerListName,");
            sb.append("         nvl(b.totalCustomer, 0) totalCusList,");
            sb.append("         nvl(c.customerActive, 0) totalCusActive,");
            sb.append("         nvl(d.customerLock, 0) totalCusLock,");
            sb.append("         nvl(e.customerDnc, 0) totalCusDnc,");
            sb.append("         (a.create_at + :p_time_zone_offset/24) createAt,");
            sb.append("         a.company_site_id companySiteId,");
            sb.append("         a.status status");
            sb.append(" from customer_list a");
            sb.append(" left join customer_table b on a.customer_list_id = b.customerListId");
            sb.append(" left join customer_active_table c on a.customer_list_id = c.customerListId");
            sb.append(" left join customer_lock_table d on a.customer_list_id = d.customerListId");
            sb.append(" left join customer_dnc_table e on a.customer_list_id = e.customerListId");
            sb.append(" where a.status = 1");
            sb.append("     and (:p_cus_list_code is null or upper(a.customer_list_code) like '%'||:p_cus_list_code||'%')");
            sb.append("     and (:p_cus_list_name is null or upper(a.customer_list_name) like '%'||:p_cus_list_name||'%')");
            sb.append("     and (:p_to_date is null or (a.create_at <= to_date(:p_to_date, 'DD/MM/YYYY HH24:MI:SS')))");
            sb.append("     and (:p_from_date is null or (a.create_at >= to_date(:p_from_date, 'DD/MM/YYYY HH24:MI:SS')))");
            sb.append("     and (a.company_site_id = :p_company_site_id)");
            sb.append("     and (a.customer_list_id not in (select CUSTOMER_LIST_ID from campaign_customer_id))");
            sb.append(" ),");
            sb.append(" data as (");
            sb.append(" select a.*, rownum row_ from data_temp a");
            sb.append(" ),");
            sb.append(" count_data as (");
            sb.append(" select count(*) totalRow from data_temp");
            sb.append(" )");
            sb.append(" select a.customerListId, a.customerListCode, a.customerListName, a.totalCusList, a.totalCusActive, a.totalCusLock, a.totalCusDnc, totalRow from data a, count_data");
            sb.append(" where row_ >= ((:p_page_number - 1) * :p_page_size + 1) and row_ < (:p_page_number * :p_page_size + 1)");
            SQLQuery query = session.createSQLQuery(sb.toString());
            query.setParameter("p_company_site_id", dto.getCompanySiteId());
            query.setParameter("p_campaign_id", dto.getCampaignId());
            query.setParameter("p_cus_list_code", DataUtil.isNullOrEmpty(dto.getCustListCode()) ? null : dto.getCustListCode().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_").toUpperCase());
            query.setParameter("p_cus_list_name", DataUtil.isNullOrEmpty(dto.getCustListName()) ? null : dto.getCustListName().trim().replace("\\", "\\\\").replaceAll("%", "\\%").replaceAll("_", "\\_").toUpperCase());
            if (!DataUtil.isNullOrEmpty(dto.getCreateTimeTo())) {
                String dtTo = TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(dto.getCreateTimeTo() + " 23:59:59", "dd/MM/yyyy HH:mm:ss", tzClient));
                query.setParameter("p_to_date", dtTo);
            } else {
                query.setParameter("p_to_date", null);
            }
            if (!DataUtil.isNullOrEmpty(dto.getCreateTimeFr())) {
                String dtFrom = TimeZoneUtils.toDateStringWithTimeZoneZero(TimeZoneUtils.convertStringToDate(dto.getCreateTimeFr() + " 00:00:00", "dd/MM/yyyy HH:mm:ss", tzClient));
                query.setParameter("p_from_date", dtFrom);
            } else {
                query.setParameter("p_from_date", null);
            }
            query.setParameter("p_page_number", dto.getPage());
            query.setParameter("p_page_size", dto.getPageSize());
            query.setParameter("p_time_zone_offset", dto.getTimezoneOffset());

            query.addScalar("customerListId", new LongType());
            query.addScalar("customerListCode", new StringType());
            query.addScalar("customerListName", new StringType());
            query.addScalar("totalCusList", new LongType());
            query.addScalar("totalCusActive", new LongType());
            query.addScalar("totalCusLock", new LongType());
            query.addScalar("totalCusDnc", new LongType());
            query.addScalar("totalRow", new IntegerType());

            query.setResultTransformer(Transformers.aliasToBean(CustomerListDTO.class));

            int total = 0;
            list = query.list();
            if (list.size() > 0) {
                total = list.get(0).getTotalRow();
            }
            resultDTO.setListData(list);
            resultDTO.setTotalRow(Long.valueOf(total));
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        } finally {
            session.close();
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCustomerChoosenList(CampaignRequestDTO dto) {
        List<CustomerListDTO> list = new ArrayList();
        ResultDTO resultDTO = new ResultDTO();
        Map<String, String> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        try {
            sb.append(" with campaign_customer_id as (");
            sb.append("     select ccl.CUSTOMER_LIST_ID from campaign_customerlist ccl");
            sb.append("     where ccl.campaign_id = :p_campaign_id and ccl.company_site_id = :p_company_site_id");
            sb.append(" ),");
            sb.append(" customer_table as (");
            sb.append("     select count(a.customer_id) totalCustomer, a.customer_list_id customerListId from customer_list_mapping a");
            sb.append("     left join customer b on a.customer_id = b.customer_id");
            sb.append("     where b.status = 1");
            sb.append("     group by a.customer_list_id");
            sb.append(" ),");
            sb.append(" customer_active_table as (");
            sb.append("     select count(a.customer_id) customerActive, a.customer_list_id customerListId from customer_list_mapping a");
            sb.append("     left join customer b on a.customer_id = b.customer_id");
            sb.append("     where b.status = 1 and b.ipcc_status = 'active'");
            sb.append("     group by a.customer_list_id");
            sb.append(" ),");
            sb.append(" customer_lock_table as (");
            sb.append("     select count(a.customer_id) customerLock, a.customer_list_id customerListId from customer_list_mapping a");
            sb.append("     left join customer b on a.customer_id = b.customer_id");
            sb.append("     where b.status = 1 and b.ipcc_status = 'locked'");
            sb.append("     group by a.customer_list_id");
            sb.append(" ),");
            sb.append(" customer_dnc_table as (");
            sb.append("     select count(a.customer_id) customerDnc, a.customer_list_id customerListId from customer_list_mapping a");
            sb.append("     left join customer b on a.customer_id = b.customer_id");
            sb.append("     where b.status = 1 and b.call_allowed = 0");
            sb.append("     group by a.customer_list_id");
            sb.append(" ),");
            sb.append(" customer_filter_table as (");
            sb.append("     select count(a.customer_id) customerFilter, a.customer_list_id customerListId from campaign_customer a");
            sb.append("  INNER JOIN customer cus  ON a.CUSTOMER_ID = cus.CUSTOMER_ID");
            sb.append("     where a.campaign_id = :p_campaign_id and a.in_campaign_status = 1  and cus.STATUS = 1");
            sb.append("     group by a.customer_list_id");
            sb.append(" ),");
            sb.append(" data_temp as (");
            sb.append(" select  a.customer_list_id customerListId,");
            sb.append("         a.customer_list_code customerListCode,");
            sb.append("         a.customer_list_name customerListName,");
            sb.append("         nvl(b.totalCustomer, 0) totalCusList,");
            sb.append("         nvl(c.customerActive, 0) totalCusActive,");
            sb.append("         nvl(d.customerLock, 0) totalCusLock,");
            sb.append("         nvl(e.customerDnc, 0) totalCusDnc,");
            sb.append("         nvl(null, 0) totalCusAddRemove,");
            sb.append("         nvl(f.customerFilter, 0) totalCusFilter");
            sb.append(" from customer_list a");
            sb.append(" left join customer_table b on a.customer_list_id = b.customerListId");
            sb.append(" left join customer_active_table c on a.customer_list_id = c.customerListId");
            sb.append(" left join customer_lock_table d on a.customer_list_id = d.customerListId");
            sb.append(" left join customer_dnc_table e on a.customer_list_id = e.customerListId");
            sb.append(" left join customer_filter_table f on a.customer_list_id = f.customerListId");
            sb.append(" where a.customer_list_id in (select CUSTOMER_LIST_ID from campaign_customer_id)");
            sb.append(" ),");
            sb.append(" data as (");
            sb.append(" select a.*, rownum row_ from data_temp a");
            sb.append(" ),");
            sb.append(" count_data as (");
            sb.append(" select count(*) totalRow from data_temp");
            sb.append(" )");
            sb.append(" select a.customerListId, a.customerListCode, a.customerListName, a.totalCusList, a.totalCusActive, a.totalCusLock, a.totalCusDnc, a.totalCusAddRemove, a.totalCusFilter, totalRow from data a, count_data");
            sb.append(" where row_ >= ((:p_page_number - 1) * :p_page_size + 1) and row_ < (:p_page_number * :p_page_size + 1)");
            sb.append(" order by a.customerListName");
            params.put("p_campaign_id", dto.getCampaignId());
            params.put("p_company_site_id", dto.getCompanySiteId());
            params.put("p_page_number", dto.getPage().toString());
            params.put("p_page_size", dto.getPageSize().toString());
            list = namedParameterJdbcTemplate.query(sb.toString(), params, BeanPropertyRowMapper.newInstance(CustomerListDTO.class));
            int total = 0;
            if (list.size() > 0) {
                total = list.get(0).getTotalRow();
            }
            resultDTO.setListData(list);
            resultDTO.setTotalRow(Long.valueOf(total));
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }


    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public CampaignInformationDTO getCampaignCustomerInformation(CampaignRequestDTO dto) {
        List<CampaignInformationDTO> list = new ArrayList<>();
        CampaignInformationDTO returnData = new CampaignInformationDTO();
        StringBuilder sb = new StringBuilder();
        Map<String, String> params = new HashMap<>();

        try {
            sb.append(" with status_customer as (");
            sb.append(" select complete_value");
            sb.append(" from campaign_complete_code");
            sb.append(" where status = 1");
            sb.append("     and complete_type = 1");
            sb.append("     and company_site_id = :p_company_site_id");
            sb.append(" ),");
            sb.append(" count_customer as (");
            sb.append(" select  campaign_id campaignId,");
            sb.append("         sum(case");
            sb.append("                 when customer_list_id is null then 1");
            sb.append("                 else 0");
            sb.append("             end) totalIndividual,");
            sb.append("         sum(case");
            sb.append("                 when status = 0 and in_campaign_status = 1 then 1");
            sb.append("                 else 0");
            sb.append("             end) totalNotInteractive,");
            sb.append("         sum(case");
            sb.append("                 when status in (select * from status_customer) and in_campaign_status = 1 then 1");
            sb.append("                 else 0");
            sb.append("             end) totalNotCall,");
            sb.append("         sum(case");
            sb.append("                 when customer_list_id is not null and in_campaign_status = 1 then 1");
            sb.append("                 else 0");
            sb.append("             end) totalCusInList");
            sb.append(" from campaign_customer");
            sb.append(" group by campaign_id");
            sb.append(" )");
            sb.append(" select  b.campaign_id campaignId,");
            sb.append("         nvl(a.totalIndividual, 0) totalIndividual,");
            sb.append("         nvl(a.totalNotInteractive, 0) totalNotInteractive,");
            sb.append("         nvl(a.totalNotCall, 0) totalNotCall,");
            sb.append("         nvl(a.totalCusInList, 0) totalCusInList,");
            sb.append("         b.customer_number campaignCustomer");
            sb.append(" from campaign b");
            sb.append(" left join count_customer a on a.campaignId = b.campaign_id");
            sb.append(" where b.campaign_id = :p_campaign_id");
            params.put("p_campaign_id", dto.getCampaignId());
            params.put("p_company_site_id", dto.getCompanySiteId());
            list = namedParameterJdbcTemplate.query(sb.toString(), params, BeanPropertyRowMapper.newInstance(CampaignInformationDTO.class));

            if (list.size() > 0) {
                returnData = list.get(0);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return returnData;
    }

    @Override
    public List<CampaignInformationDTO> getCustomerListInformation(CampaignRequestDTO dto) {
        List<CampaignInformationDTO> list = new ArrayList();
        Map<String, String> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        try {
            sb.append(" with status_customer as (");
            sb.append(" select complete_value");
            sb.append(" from campaign_complete_code");
            sb.append(" where status =1");
            sb.append("     and complete_type = 1");
            sb.append("     and company_site_id = :p_company_site_id");
            sb.append(" ),");
            sb.append(" count_customer as (");
            sb.append(" select  campaign_id campaignId,");
            sb.append("         customer_list_id customerListId,");
            sb.append("         sum(case");
            sb.append("                 when customer_list_id is null then 1");
            sb.append("                 else 0");
            sb.append("             end) totalIndividual,");
            sb.append("         sum(case");
            sb.append("                 when status = 0 and in_campaign_status = 1 then 1");
            sb.append("                 else 0");
            sb.append("             end) totalNotInteractive,");
            sb.append("         sum(case");
            sb.append("                 when status in (select * from status_customer) and in_campaign_status = 1 then 1");
            sb.append("                 else 0");
            sb.append("             end) totalNotCall");
            sb.append(" from campaign_customer");
            sb.append(" group by customer_list_id, campaign_id");
            sb.append(" )");
            sb.append(" select a.*, b.customer_number campaignCustomer");
            sb.append(" from count_customer a");
            sb.append(" left join campaign b on a.campaignId = b.campaign_id");
            sb.append(" where a.campaignId = :p_campaign_id");
            sb.append("     and customerListId is not null");
            params.put("p_campaign_id", dto.getCampaignId());
            params.put("p_company_site_id", dto.getCompanySiteId());
            list = namedParameterJdbcTemplate.query(sb.toString(), params, BeanPropertyRowMapper.newInstance(CampaignInformationDTO.class));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return list;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public List<CampaignInformationDTO> getCountIndividualOnList(CampaignRequestDTO dto) {
        List<CampaignInformationDTO> list = new ArrayList();
        Map<String, String> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        try {
            sb.append(" with customer_temp as (");
            sb.append(" select c.customer_id, clm.customer_list_id");
            sb.append(" from customer c");
            sb.append(" inner join customer_list_mapping clm on c.customer_id = clm.customer_id");
            sb.append(" where clm.customer_list_id in (select distinct cc.customer_list_id from campaign_customerlist cc where cc.campaign_id = :p_campaign_id and cc.company_site_id = :p_company_site_id)");
            sb.append(" )");
            sb.append(" select count(cc.customer_id) totalIndividual, c.customer_list_id customerListId");
            sb.append(" from campaign_customer cc");
            sb.append(" inner join customer_temp c on c.customer_id = cc.customer_id");
            sb.append(" where campaign_id = :p_campaign_id");
            sb.append("     and cc.customer_list_id is null");
            sb.append("     and cc.in_campaign_status = 1");
            sb.append("     and cc.customer_id in (select customer_id from customer_temp)");
            sb.append(" group by c.customer_list_id");
            params.put("p_campaign_id", dto.getCampaignId());
            params.put("p_company_site_id", dto.getCompanySiteId());
            list = namedParameterJdbcTemplate.query(sb.toString(), params, BeanPropertyRowMapper.newInstance(CampaignInformationDTO.class));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return list;
    }

}
