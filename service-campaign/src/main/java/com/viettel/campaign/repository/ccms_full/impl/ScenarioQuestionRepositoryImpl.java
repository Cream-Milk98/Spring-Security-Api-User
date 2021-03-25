package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.repository.ccms_full.ScenarioQuestionRepositoryCustom;
import com.viettel.campaign.utils.HibernateUtil;
import com.viettel.campaign.web.dto.ScenarioQuestionDTO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anhvd_itsol
 */

@Repository
public class ScenarioQuestionRepositoryImpl implements ScenarioQuestionRepositoryCustom {
    private static final Logger logger = LoggerFactory.getLogger(ScenarioQuestionRepositoryImpl.class);

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Integer countDuplicateQuestionCode(ScenarioQuestionDTO questionDTO) {
        logger.info("Start search duplicate quest code row::");
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Integer count = null;

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT COUNT(1) FROM SCENARIO_QUESTION WHERE 1 = 1 AND STATUS = 1 ");
            if (questionDTO.getScenarioQuestionId() != null) {
                sb.append(" AND SCENARIO_QUESTION_ID <> :p_question_id ");
            }
            if (questionDTO.getCode() != null) {
                sb.append(" AND CODE = :p_code ");
            }
            if (questionDTO.getCampaignId() != null) {
                sb.append(" AND CAMPAIGN_ID = :p_campaign_id");
            }
            if (questionDTO.getCompanySiteId() != null) {
                sb.append(" AND COMPANY_SITE_ID = :p_site_id");
            }
            SQLQuery query = session.createSQLQuery(sb.toString());
            if (questionDTO.getScenarioQuestionId() != null) {
                query.setParameter("p_question_id", questionDTO.getScenarioQuestionId());
            }
            if (questionDTO.getCode() != null) {
                query.setParameter("p_code", questionDTO.getCode());
            }
            if (questionDTO.getCampaignId() != null) {
                query.setParameter("p_campaign_id", questionDTO.getCampaignId());
            }
            if (questionDTO.getCompanySiteId() != null) {
                query.setParameter("p_site_id", questionDTO.getCompanySiteId());
            }

            final List<BigDecimal> obj = query.list();

            for (BigDecimal i : obj) {
                if (i != null) {
                    count = i.intValue();
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            session.close();
        }
        return count;
    }

    @Override
    public Integer countDuplicateOrderIndex(ScenarioQuestionDTO questionDTO) {
        logger.info("Start search duplicate order index::");
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Integer count = null;

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT COUNT(1) FROM SCENARIO_QUESTION WHERE 1 = 1 AND STATUS = 1 ");
            if (questionDTO.getScenarioQuestionId() != null) {
                sb.append(" AND SCENARIO_QUESTION_ID <> :p_question_id ");
            }
            if (questionDTO.getOrderIndex() != null) {
                sb.append(" AND ORDER_INDEX = :p_orderIndex ");
            }
            if (questionDTO.getCampaignId() != null) {
                sb.append(" AND CAMPAIGN_ID = :p_campaign_id");
            }
            if (questionDTO.getCompanySiteId() != null) {
                sb.append(" AND COMPANY_SITE_ID = :p_site_id");
            }
            SQLQuery query = session.createSQLQuery(sb.toString());
            if (questionDTO.getScenarioQuestionId() != null) {
                query.setParameter("p_question_id", questionDTO.getScenarioQuestionId());
            }
            if (questionDTO.getOrderIndex() != null) {
                query.setParameter("p_orderIndex", questionDTO.getOrderIndex());
            }
            if (questionDTO.getCampaignId() != null) {
                query.setParameter("p_campaign_id", questionDTO.getCampaignId());
            }
            if (questionDTO.getCompanySiteId() != null) {
                query.setParameter("p_site_id", questionDTO.getCompanySiteId());
            }

            final List<BigDecimal> obj = query.list();

            for (BigDecimal i : obj) {
                if (i != null) {
                    count = i.intValue();
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            session.close();
        }
        return count;
    }

    @Override
    public List<ScenarioQuestionDTO> getOldQuestionsData(Long campaignId, Long customerId, Long contactCustResultId) {
        logger.info("--- START GET INTERACTED QUESTIONS::");

        List<ScenarioQuestionDTO> lstResult = new ArrayList<>();
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();
            StringBuilder sb = new StringBuilder();
            sb.append("with temp as (" +
                    "select DISTINCT sq.SCENARIO_QUESTION_ID                scenarioQuestionId," +
                    "       sq.COMPANY_SITE_ID                              companySiteId," +
                    "       sq.CAMPAIGN_ID                                  campaignId," +
                    "       sq.SCENARIO_ID                                  scenarioId," +
                    "       sq.CODE                                         code," +
                    "       sq.TYPE                                         type," +
                    "       sq.QUESTION                                     question," +
                    "       sq.ORDER_INDEX                                  orderIndex," +
                    "       sq.STATUS                                       status," +
                    "       sq.CREATE_TIME                                  createTime," +
                    "       sq.DELETE_TIME                                  deleteTime," +
                    "       sq.IS_REQUIRE                                   isRequire," +
                    "       sq.IS_DEFAULT                                   isDefault," +
                    "       sq.ANSWER_INDEX                                 answerIndex," +
                    "       cqr.SCENARIO_ANSWER_ID                          scenarioAnswerId," +
                    "       (CASE    WHEN B.MAPPING_QUESTION_ID IS NOT NULL THEN 2 " +
                    "                WHEN C.MAPPING_QUESTION_ID IS NOT NULL THEN 1  " +
                    "        ELSE 0 END) typeMap " +
                    " from SCENARIO_QUESTION sq" +
                    "         left join CONTACT_QUEST_RESULT cqr" +
                    "                   on sq.SCENARIO_QUESTION_ID = cqr.SCENARIO_QUESTION_ID" +
                    "                       and cqr.status = 1" +
                    "                       and CUSTOMER_ID = :p_customer_id" +
                    "                       and CONTACT_CUST_RESULT_ID = :p_contact_cust_id" +
                    "       LEFT JOIN SCENARIO_ANSWER B ON SQ.SCENARIO_QUESTION_ID = B.MAPPING_QUESTION_ID " +
                    "       LEFT JOIN SCENARIO_ANSWER C ON SQ.SCENARIO_QUESTION_ID = C.SCENARIO_QUESTION_ID " +
                    " where sq.CAMPAIGN_ID = :p_campaign_id AND sq.status = 1)" +
                    " select scenarioQuestionId scenarioQuestionId, " +
                    "        companySiteId companySiteId, " +
                    "        campaignId campaignId," +
                    "        scenarioId scenarioId," +
                    "        code code," +
                    "        type type," +
                    "        question question," +
                    "        orderIndex orderIndex," +
                    "        status status," +
                    "        createTime createTime," +
                    "        deleteTime deleteTime," +
                    "        isRequire isRequire," +
                    "        isDefault isDefault," +
                    "        answerIndex answerIndex," +
                    "        scenarioAnswerId scenarioAnswerId," +
                    "        typeMap " +
                    " from temp t" +
                    " where 1 = 1" +
                    " and t.createTime < (select CREATE_TIME from CONTACT_CUST_RESULT where CONTACT_CUST_RESULT_ID = :p_contact_cust_id)" +
                    " and (t.status = 1 or (t.STATUS = 0 and t.deleteTime > (select CREATE_TIME" +
                    "                                                                          from CONTACT_CUST_RESULT" +
                    "                                                                          where CONTACT_CUST_RESULT_ID = :p_contact_cust_id)))");

            SQLQuery query = session.createSQLQuery(sb.toString());
            query.setParameter("p_contact_cust_id", contactCustResultId);
            query.setParameter("p_campaign_id", campaignId);
            query.setParameter("p_customer_id", customerId);

            query.addScalar("scenarioQuestionId", new LongType());
            query.addScalar("companySiteId", new LongType());
            query.addScalar("campaignId", new LongType());
            query.addScalar("scenarioId", new LongType());
            query.addScalar("code", new StringType());
            query.addScalar("type", new ShortType());
            query.addScalar("question", new StringType());
            query.addScalar("orderIndex", new LongType());
            query.addScalar("status", new ShortType());
            query.addScalar("createTime", new DateType());
            query.addScalar("deleteTime", new DateType());
            query.addScalar("isRequire", new ShortType());
            query.addScalar("isDefault", new ShortType());
            query.addScalar("answerIndex", new ShortType());
            query.addScalar("scenarioAnswerId", new LongType());
            query.addScalar("typeMap", new ShortType());
            query.setResultTransformer(Transformers.aliasToBean(ScenarioQuestionDTO.class));
            lstResult = query.list();

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return  lstResult;
    }

    @Override
    public List<ScenarioQuestionDTO> getListQuestions(Long companySiteId, Long scenarioId, Short status) {
        List<ScenarioQuestionDTO> lstResult = new ArrayList<>();
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT DISTINCT a.scenario_question_id scenarioQuestionId, " +
                    "        a.company_site_id companySiteId, a.campaign_id campaignId," +
                    "       a.scenario_id scenarioId, a.code, a.type, a.question, " +
                    "       a.order_index orderIndex, a.status, a.create_time createTime, " +
                    "       a.delete_time deleteTime, a.is_require isRequire, " +
                    "       a.is_default isDefault, a.answer_index answerIndex, " +
                    "(CASE   WHEN B.MAPPING_QUESTION_ID IS NOT NULL THEN 2 " +
                    "        WHEN C.MAPPING_QUESTION_ID IS NOT NULL THEN 1  " +
                    "ELSE 0 END) typeMap " +
                    "FROM SCENARIO_QUESTION A " +
                    "    LEFT JOIN SCENARIO_ANSWER B ON A.SCENARIO_QUESTION_ID = B.MAPPING_QUESTION_ID " +
                    "    LEFT JOIN SCENARIO_ANSWER C ON A.SCENARIO_QUESTION_ID = C.SCENARIO_QUESTION_ID " +
                    "WHERE A.STATUS = :p_status " +
                    "AND A.COMPANY_SITE_ID = :p_company_site_id " +
                    "AND a.SCENARIO_ID = :p_scenario_id " +
                    "ORDER BY A.ORDER_INDEX");

            SQLQuery query = session.createSQLQuery(sb.toString());
            query.setParameter("p_company_site_id", companySiteId);
            query.setParameter("p_scenario_id", scenarioId);
            query.setParameter("p_status", status);

            query.addScalar("scenarioQuestionId", new LongType());
            query.addScalar("companySiteId", new LongType());
            query.addScalar("campaignId", new LongType());
            query.addScalar("scenarioId", new LongType());
            query.addScalar("code", new StringType());
            query.addScalar("type", new ShortType());
            query.addScalar("question", new StringType());
            query.addScalar("orderIndex", new LongType());
            query.addScalar("status", new ShortType());
            query.addScalar("createTime", new DateType());
            query.addScalar("deleteTime", new DateType());
            query.addScalar("isRequire", new ShortType());
            query.addScalar("isDefault", new ShortType());
            query.addScalar("answerIndex", new ShortType());
            query.addScalar("typeMap", new ShortType());
            query.setResultTransformer(Transformers.aliasToBean(ScenarioQuestionDTO.class));
            lstResult = query.list();

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return  lstResult;
    }
}
