package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.repository.ccms_full.AgentCustomRepository;
import com.viettel.campaign.utils.DataUtil;
import com.viettel.campaign.utils.HibernateUtil;
import com.viettel.campaign.web.dto.VSAUsersDTO;
import com.viettel.campaign.web.dto.request_dto.AgentRequestDTO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AgentRepositoryImpl implements AgentCustomRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentRepositoryImpl.class);

    @Override
    public Page<VSAUsersDTO> getAgentsSelected(AgentRequestDTO agentRequestDTO, Pageable pageable) {
        List<VSAUsersDTO> data = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Page<VSAUsersDTO> dataPage = new PageImpl<>(data, pageable, 0);
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT at.*, row_number() over (ORDER BY NLSSORT(at.fullName, 'NLS_SORT = Vietnamese')) FROM (");
            sb.append("SELECT");
            sb.append("    ca.CAMPAIGN_AGENT_ID campaignAgentId,");
            sb.append("    ca.FILTER_TYPE filterType,");
            sb.append("    ca.COMPANY_SITE_ID companySiteId,");
            sb.append("    vu.USER_ID userId,");
            sb.append("    vu.USER_NAME userName,");
            sb.append("    vu.STATUS status,");
            sb.append("    vu.FULL_NAME fullName");
            sb.append(" FROM CAMPAIGN_AGENT ca");
            sb.append(" INNER JOIN VSA_USERS vu ON ca.AGENT_ID = vu.USER_ID");
            sb.append(" WHERE 1 = 1");
            sb.append(" AND ca.COMPANY_SITE_ID = :p_company_site_id");
            sb.append(" AND ca.CAMPAIGN_ID = :p_campaign_id");

            if (!DataUtil.isNullOrEmpty(agentRequestDTO.getUserName())) {
                sb.append(" AND UPPER(vu.USER_NAME) LIKE UPPER(:p_user_name)");
            }

            if (!DataUtil.isNullOrEmpty(agentRequestDTO.getFullName())) {
                sb.append(" AND UPPER(vu.FULL_NAME) LIKE UPPER(:p_full_name)");
            }

            sb.append(") at");

            SQLQuery query = session.createSQLQuery(sb.toString());

            query.setParameter("p_company_site_id", agentRequestDTO.getCompanySiteId());
            query.setParameter("p_campaign_id", agentRequestDTO.getCampaignId());

            if (!DataUtil.isNullOrEmpty(agentRequestDTO.getUserName())) {
                query.setParameter("p_user_name", "%" +
                        agentRequestDTO.getUserName().trim()
                                .replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            if (!DataUtil.isNullOrEmpty(agentRequestDTO.getFullName())) {
                query.setParameter("p_full_name", "%" +
                        agentRequestDTO.getFullName().trim()
                                .replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            query.addScalar("userId", new LongType());
            query.addScalar("userName", new StringType());
            query.addScalar("status", new ShortType());
            query.addScalar("fullName", new StringType());
            query.addScalar("companySiteId", new LongType());
            query.addScalar("filterType", new ShortType());
            query.addScalar("campaignAgentId", new LongType());

            query.setResultTransformer(Transformers.aliasToBean(VSAUsersDTO.class));

            int total = 0;
            total = query.list().size();
            if (total > 0) {
                if (pageable != null) {
                    query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                    query.setMaxResults(pageable.getPageSize());
                }

                data = query.list();

                dataPage = new PageImpl<>(data, pageable, total);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if(null != session){
                session.close();
            }
        }

        return dataPage;
    }

    @Override
    public Page<VSAUsersDTO> getAgents(AgentRequestDTO agentRequestDTO, Pageable pageable) {
        List<VSAUsersDTO> data = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Page<VSAUsersDTO> dataPage = new PageImpl<>(data, pageable, 0);
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            StringBuilder sqlStrBuilder = new StringBuilder();

            sqlStrBuilder.append("SELECT at.userId, at.userName, at.roleCode, at.fullName, at.companySiteId, at.status FROM (");
            sqlStrBuilder.append("SELECT");
            sqlStrBuilder.append("    vu.USER_ID         userId,");
            sqlStrBuilder.append("    vu.USER_NAME       userName,");
            sqlStrBuilder.append("    vu.FULL_NAME       fullName,");
            sqlStrBuilder.append("    vu.COMPANY_SITE_ID companySiteId,");
            sqlStrBuilder.append("    vu.STATUS          status,");
            sqlStrBuilder.append("    r.ROLE_CODE       roleCode");
            sqlStrBuilder.append(" FROM VSA_USERS vu");
            sqlStrBuilder.append(" INNER JOIN USER_ROLE ur on vu.USER_ID = ur.USER_ID");
            sqlStrBuilder.append(" INNER JOIN ROLE r on ur.ROLE_ID = r.ROLE_ID");
            sqlStrBuilder.append(" WHERE 1 = 1");
            sqlStrBuilder.append(" AND vu.COMPANY_SITE_ID = :p_company_site_id");
            sqlStrBuilder.append(" AND vu.STATUS = 1");
            sqlStrBuilder.append(" AND vu.USER_KAZOO_ID is not null and length(vu.USER_KAZOO_ID) > 10 ");
            sqlStrBuilder.append(" AND r.ROLE_CODE IN ('AGENT', 'SUPERVISOR')");
            sqlStrBuilder.append(" AND vu.USER_ID NOT IN (SELECT vu.USER_ID");
            sqlStrBuilder.append("                         FROM CAMPAIGN_AGENT ca");
            sqlStrBuilder.append("                            INNER JOIN VSA_USERS vu ON ca.AGENT_ID = vu.USER_ID");
            sqlStrBuilder.append("                         WHERE 1 = 1");
            sqlStrBuilder.append("                            AND ca.COMPANY_SITE_ID = :p_company_site_id");
            sqlStrBuilder.append("                            AND ca.CAMPAIGN_ID = :p_campaign_id)");

            if (!DataUtil.isNullOrEmpty(agentRequestDTO.getUserName())) {
                sqlStrBuilder.append(" AND UPPER(vu.USER_NAME) LIKE UPPER(:p_user_name)");
            }

            if (!DataUtil.isNullOrEmpty(agentRequestDTO.getFullName())) {
                sqlStrBuilder.append(" AND UPPER(vu.FULL_NAME) LIKE UPPER(:p_full_name)");
            }

            sqlStrBuilder.append(") at ORDER BY NLSSORT(at.fullName, 'NLS_SORT = Vietnamese')");

            SQLQuery query = session.createSQLQuery(sqlStrBuilder.toString());

            query.setParameter("p_company_site_id", agentRequestDTO.getCompanySiteId());
            query.setParameter("p_campaign_id", agentRequestDTO.getCampaignId());

            if (!DataUtil.isNullOrEmpty(agentRequestDTO.getUserName())) {
                query.setParameter("p_user_name", "%" +
                        agentRequestDTO.getUserName().trim()
                                .replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            if (!DataUtil.isNullOrEmpty(agentRequestDTO.getFullName())) {
                query.setParameter("p_full_name", "%" +
                        agentRequestDTO.getFullName().trim()
                                .replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                        + "%");
            }

            query.addScalar("userId", new LongType());
            query.addScalar("userName", new StringType());
            query.addScalar("status", new ShortType());
            query.addScalar("fullName", new StringType());
            query.addScalar("companySiteId", new LongType());
            query.addScalar("roleCode", new StringType());

            query.setResultTransformer(Transformers.aliasToBean(VSAUsersDTO.class));

            int total = 0;
            total = query.list().size();
            if (total > 0) {
                if (pageable != null) {
                    query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                    query.setMaxResults(pageable.getPageSize());
                }

                data = query.list();

                dataPage = new PageImpl<>(data, pageable, total);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if(session != null) session.close();
        }

        return dataPage;
    }
}
