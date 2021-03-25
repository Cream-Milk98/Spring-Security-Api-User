package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.CampaignCfg;
import com.viettel.campaign.repository.ccms_full.CampaignCfgRepositoryCustom;
import com.viettel.campaign.utils.Constants;
import com.viettel.campaign.utils.DataUtil;
import com.viettel.campaign.utils.HibernateUtil;
import com.viettel.campaign.web.dto.CampaignCfgDTO;
import com.viettel.campaign.web.dto.ResultDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CampaignCfgRepositoryIpml implements CampaignCfgRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(CampaignCfgRepositoryIpml.class);


    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public Page<CampaignCfgDTO> listAllCompleteCode(Long companySiteId, Pageable pageable) {
        Page<CampaignCfgDTO> dataPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            StringBuilder sb = new StringBuilder();
            //sqlStrBuilder.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CAMPAIGN_STATUS_MNG, "findAll-CampaignCompleteCode"));

            sb.append("SELECT");
            sb.append("    CAMPAIGN_COMPLETE_CODE_ID campaignCompleteCodeId,");
            sb.append("    IS_RECALL isRecall ,");
            sb.append("    CAMPAIGN_ID campaignId,");
            sb.append("    COMPLETE_VALUE completeValue,");
            sb.append("    COMPLETE_NAME completeName,");
            sb.append("    COMPLETE_TYPE completeType,");
            sb.append("    CAMPAIGN_TYPE campaignType,");
            sb.append("    DESCRIPTION description,");
            sb.append("    STATUS  status,");
            sb.append("    COMPANY_SITE_ID companySiteId,");
            sb.append("    UPDATE_BY updateBy ,");
            sb.append("    UPDATE_AT updateAt,");
            sb.append("    CREATE_BY createBy,");
            sb.append("    CREATE_AT createAt,");
            sb.append("    IS_FINISH isFinish,");
            sb.append("    IS_LOCK isLock,");
            sb.append("    DURATION_LOCK durationLock,");
            sb.append("    CHANEL chanel");
            sb.append(" FROM CAMPAIGN_COMPLETE_CODE");
            sb.append(" WHERE 1 = 1");
            sb.append("   AND STATUS = 1");
            sb.append("   AND COMPANY_SITE_ID = :p_company_site_id");
            sb.append("   AND COMPLETE_VALUE not in (1,2,3,4)");
            sb.append(" ORDER BY to_number(COMPLETE_VALUE) ");
            // logger.info("SQL statement: " + sb);

            SQLQuery query = session.createSQLQuery(sb.toString());

            query.setParameter("p_company_site_id", companySiteId);

            query.addScalar("campaignCompleteCodeId", new LongType());
            query.addScalar("campaignId", new LongType());
            query.addScalar("completeName", new StringType());
            query.addScalar("completeValue", new StringType());
            query.addScalar("description", new StringType());
            query.addScalar("status", new ShortType());
            query.addScalar("completeType", new ShortType());
            query.addScalar("isRecall", new ShortType());
            query.addScalar("updateAt", new DateType());
            query.addScalar("updateBy", new StringType());
            query.addScalar("createBy", new StringType());
            query.addScalar("createAt", new DateType());
            query.addScalar("campaignType", new StringType());
            query.addScalar("isFinish", new ShortType());
            query.addScalar("isLock", new ShortType());
            query.addScalar("companySiteId", new LongType());
            query.addScalar("durationLock", new LongType());
            query.addScalar("chanel", new LongType());

            query.setResultTransformer(Transformers.aliasToBean(CampaignCfgDTO.class));

            int count = 0;
            List<CampaignCfgDTO> dtoList = query.list();
            if (dtoList.size() > 0) {
                count = query.list().size();
            }

            if (pageable != null) {
                query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                query.setMaxResults(pageable.getPageSize());
            }

            List<CampaignCfgDTO> data = query.list();

            dataPage = new PageImpl<>(data, pageable, count);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (session != null) session.close();
        }

        return dataPage;
    }

    @Override
    public Integer findMaxValueCampaignCompleteCode(CampaignCfgDTO completeCodeDTO) {
        Integer tmp = -1;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            //StringBuilder sqlStrBuilder = new StringBuilder();
            //sqlStrBuilder.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CAMPAIGN_STATUS_MNG, "get-max-value-completevalue"));
            StringBuilder sb = new StringBuilder();

            sb.append(" SELECT MAX(TO_NUMBER(COMPLETE_VALUE)) completeValue, COMPANY_SITE_ID companySiteId ");
            sb.append(" FROM CAMPAIGN_COMPLETE_CODE ");
            sb.append(" WHERE COMPANY_SITE_ID = :p_site_id ");
            sb.append(" GROUP BY COMPANY_SITE_ID ");

            SQLQuery query = session.createSQLQuery(sb.toString());
            query.setParameter("p_site_id", completeCodeDTO.getCompanySiteId());

            query.addScalar("completeValue", new StringType());
            query.addScalar("companySiteId", new LongType());
            query.setResultTransformer(Transformers.aliasToBean(CampaignCfgDTO.class));

            List<CampaignCfgDTO> data = query.list();
            if (data != null && data.size() > 0) {
                tmp = 5;
                String completeValue = data.get(0).getCompleteValue();
                if (completeValue != null && !"".trim().equals(completeValue)) {
                    tmp = Integer.parseInt(completeValue) + 1;
                }
            } else {
                tmp = 0;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (session != null) session.close();
        }

        return tmp;
    }

    @Override
    public List<CampaignCfgDTO> editCCC(Long campaignCompleteCodeId) {
        List<CampaignCfgDTO> data = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            //StringBuilder sqlStrBuilder = new StringBuilder();
            //sqlStrBuilder.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CAMPAIGN_STATUS_MNG,"findCampaignCodeById"));

            StringBuilder sb = new StringBuilder();

            sb.append(" SELECT CAMPAIGN_COMPLETE_CODE_ID    campaignCompleteCodeId,");
            sb.append("        IS_RECALL                    isRecall ,");
            sb.append("        CAMPAIGN_ID                  campaignId,");
            sb.append("        COMPLETE_VALUE               completeValue,");
            sb.append("        COMPLETE_NAME                completeName,");
            sb.append("        COMPLETE_TYPE                completeType,");
            sb.append("        CAMPAIGN_TYPE                campaignType,");
            sb.append("        DESCRIPTION                  description,");
            sb.append("        STATUS                       status,");
            sb.append("        COMPANY_SITE_ID              companySiteId,");
            sb.append("        UPDATE_BY                    updateBy ,");
            sb.append("        UPDATE_AT                    updateAt,");
            sb.append("        CREATE_BY                    createBy,");
            sb.append("        CREATE_AT                    createAt,");
            sb.append("        IS_FINISH                    isFinish,");
            sb.append("        IS_LOCK                      isLock,");
            sb.append("        DURATION_LOCK                durationLock,");
            sb.append("        CHANEL                       chanel");
            sb.append(" FROM CAMPAIGN_COMPLETE_CODE WHERE ");
            sb.append(" CAMPAIGN_COMPLETE_CODE_ID = :p_campaignComleteCode");

            SQLQuery query = session.createSQLQuery(sb.toString());

            query.setParameter("p_campaignComleteCode", campaignCompleteCodeId);

            query.addScalar("campaignCompleteCodeId", new LongType());
            query.addScalar("campaignId", new LongType());
            query.addScalar("completeName", new StringType());
            query.addScalar("completeValue", new StringType());
            query.addScalar("description", new StringType());
            query.addScalar("status", new ShortType());
            query.addScalar("completeType", new ShortType());
            query.addScalar("isRecall", new ShortType());
            query.addScalar("updateAt", new DateType());
            query.addScalar("updateBy", new StringType());
            query.addScalar("createBy", new StringType());
            query.addScalar("createAt", new DateType());
            query.addScalar("campaignType", new StringType());
            query.addScalar("isFinish", new ShortType());
            query.addScalar("isLock", new ShortType());
            query.addScalar("companySiteId", new LongType());
            query.addScalar("durationLock", new LongType());
            query.addScalar("chanel", new LongType());

            query.setResultTransformer(Transformers.aliasToBean(CampaignCfgDTO.class));

            data = query.list();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return data;
    }
}
