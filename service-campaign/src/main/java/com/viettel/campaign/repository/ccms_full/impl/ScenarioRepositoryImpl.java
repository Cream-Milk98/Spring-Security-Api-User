package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.repository.ccms_full.ScenarioRepositoryCustom;
import com.viettel.campaign.utils.HibernateUtil;
import com.viettel.campaign.web.dto.InteractiveResultDTO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author anhvd_itsol
 */

@Repository
public class ScenarioRepositoryImpl implements ScenarioRepositoryCustom {
    private static final Logger logger = LoggerFactory.getLogger(ScenarioRepositoryImpl.class);

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<InteractiveResultDTO> getInteractiveResult(Long contactCusResultId, Long customerId, Long campaignId) {
        logger.info("--- START GET INTERACTED RESULTS::");
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(" WITH temp AS  " +
                    "  (SELECT sq.CAMPAIGN_ID campaignId,  " +
                    "    sq.SCENARIO_QUESTION_ID scenarioQuestionId,  " +
                    "    sq.TYPE questionType,  " +
                    "    sq.ORDER_INDEX questionOrderIndex,  " +
                    "    sa.SCENARIO_ANSWER_ID answerId,  " +
                    "    sa.ORDER_INDEX answerOrderIndex,  " +
                    "    sa.HAS_INPUT answerHashInput,  " +
                    "    sa.STATUS answerStatus,  " +
                    "    sa.ANSWER answer,  " +
                    "    sa.MAPPING_QUESTION_ID mappingQuestionId,  " +
                    "    cqr.OTHER_OPINION opinion,  " +
                    "    sq.QUESTION question,  " +
                    "    cqr.CONTACT_CUST_RESULT_ID contactCusId,  " +
                    "    CASE  " +
                    "      WHEN sq.CREATE_TIME <  " +
                    "        (SELECT CREATE_TIME  " +
                    "        FROM CONTACT_CUST_RESULT  " +
                    "        WHERE CONTACT_CUST_RESULT_ID = :p_contact_cust_id  " +
                    "        )  " +
                    "      AND (sa.CREATE_TIME <  " +
                    "        (SELECT CREATE_TIME  " +
                    "        FROM CONTACT_CUST_RESULT  " +
                    "        WHERE CONTACT_CUST_RESULT_ID = :p_contact_cust_id  " +
                    "        )  " +
                    "      OR sa.CREATE_TIME IS NULL)  " +
                    "      AND sq.STATUS      = 1  " +
                    "      AND (sa.status      = 1 or sa.status is null)  " +
                    "      THEN 'old_scenario'  " +
                    "    END old_scenario,  " +
                    "    CASE  " +
                    "      WHEN sq.STATUS = 1  " +
                    "      AND (sa.STATUS  = 1 or sa.STATUS is null)  " +
                    "      THEN 'new_scenario'  " +
                    "    END new_scenario  " +
                    "  FROM SCENARIO_QUESTION sq  " +
                    "  LEFT JOIN SCENARIO_ANSWER sa  " +
                    "  ON sq.SCENARIO_QUESTION_ID = sa.SCENARIO_QUESTION_ID  " +
                    "  LEFT JOIN CONTACT_QUEST_RESULT cqr  " +
                    "  ON ((sq.TYPE                  <> 3  " +
                    "  AND sq.SCENARIO_QUESTION_ID    = cqr.SCENARIO_QUESTION_ID  " +
                    "  AND sa.SCENARIO_ANSWER_ID      = cqr.SCENARIO_ANSWER_ID)  " +
                    "  OR (sq.TYPE                    = 3  " +
                    "  AND sq.SCENARIO_QUESTION_ID    = cqr.SCENARIO_QUESTION_ID  " +
                    "  AND cqr.OTHER_OPINION         IS NOT NULL))  " +
                    "  AND cqr.status                 = 1  " +
                    "  AND cqr.CUSTOMER_ID            = :p_customer_id  " +
                    "  AND cqr.CONTACT_CUST_RESULT_ID = :p_contact_cust_id  " +
                    "  )  " +
                    " SELECT t.* ,  " +
                    "  concat(old_scenario, new_scenario)  " +
                    " FROM temp t  " +
                    " WHERE campaignId = :p_campaign_id  " +
                    " AND concat(old_scenario, new_scenario) LIKE '%old_scenario%' "
            );

            SQLQuery query = session.createSQLQuery(sb.toString());
            query.setParameter("p_contact_cust_id", contactCusResultId);
            query.setParameter("p_customer_id", customerId);
            query.setParameter("p_campaign_id", campaignId);

            query.addScalar("campaignId", new LongType());
            query.addScalar("scenarioQuestionId", new LongType());
            query.addScalar("questionType", new ShortType());
            query.addScalar("questionOrderIndex", new IntegerType());
            query.addScalar("answerId", new LongType());
            query.addScalar("answerOrderIndex", new IntegerType());
            query.addScalar("answerHashInput", new ShortType());
            query.addScalar("answerStatus", new ShortType());
            query.addScalar("opinion", new StringType());
            query.addScalar("question", new StringType());
            query.addScalar("contactCusId", new LongType());
            query.addScalar("answer", new StringType());
            query.addScalar("mappingQuestionId", new LongType());

            query.setResultTransformer(Transformers.aliasToBean(InteractiveResultDTO.class));
            List<InteractiveResultDTO> lstResult = query.list();
            return lstResult;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }
}
