package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.repository.ccms_full.ScenarioAnswerRepositoryCustom;
import com.viettel.campaign.utils.DataUtil;
import com.viettel.campaign.utils.HibernateUtil;
import com.viettel.campaign.web.dto.ScenarioAnswerDTO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anhvd_itsol
 */

@Repository
public class ScenarioAnswerRepositoryImpl implements ScenarioAnswerRepositoryCustom {
    private static final Logger logger = LoggerFactory.getLogger(ScenarioAnswerRepositoryImpl.class);

    @Override
    public List<ScenarioAnswerDTO> getOldAnswersData(Long questionId, Long customerId, Long contactCusResultId) {
        logger.info("--- START GET INTERACTED QUESTIONS::");
        List<ScenarioAnswerDTO> lstResult = new ArrayList<>();
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();
            StringBuilder sb = new StringBuilder();
            sb.append("select sa.SCENARIO_ANSWER_ID   scenarioAnswerId," +
                    "       sa.COMPANY_SITE_ID      companySiteId," +
                    "       sa.SCENARIO_QUESTION_ID scenarioQuestionId," +
                    "       sa.CODE                 code," +
                    "       sa.ANSWER               answer," +
                    "       sa.ORDER_INDEX          orderIndex," +
                    "       sa.HAS_INPUT            hashInput," +
                    "       sa.STATUS               status," +
//                    "       sa.CREATE_TIME          createTime," +
//                    "       sa.DELETE_TIME          deleteTime," +
                    "       sa.MAPPING_QUESTION_ID  mappingQuestionId," +
                    "       cqr.OTHER_OPINION       otherOpinion" +
                    " from SCENARIO_ANSWER sa" +
                    "         left join CONTACT_QUEST_RESULT cqr" +
                    "                   on sa.SCENARIO_ANSWER_ID = cqr.SCENARIO_ANSWER_ID" +
                    "                       and cqr.status = 1" +
                    "                       and CUSTOMER_ID = :p_customer_id" +
                    "                       and CONTACT_CUST_RESULT_ID = :p_contact_cust_id" +
                    " where sa.SCENARIO_QUESTION_ID = :p_question_id" +
                    "  and (sa.status = 1 or (sa.status = 0 and sa.delete_time > (select CREATE_TIME" +
                    "                                                             from CONTACT_CUST_RESULT" +
                    "                                                             where CONTACT_CUST_RESULT_ID = :p_contact_cust_id)) or" +
                    "       sa.status is null)");

            SQLQuery query = session.createSQLQuery(sb.toString());
            query.setParameter("p_contact_cust_id", contactCusResultId);
            query.setParameter("p_question_id", questionId);
            query.setParameter("p_customer_id", customerId);

            List<Object[]> lstTmp = query.list();
            if (lstTmp.size() > 0) {
                lstTmp.forEach(item -> {
                    ScenarioAnswerDTO answerDTO = new ScenarioAnswerDTO();
                    answerDTO.setScenarioAnswerId(DataUtil.safeToLong(item[0]));
                    answerDTO.setCompanySiteId(DataUtil.safeToLong(item[1]));
                    answerDTO.setScenarioQuestionId(DataUtil.safeToLong(item[2]));
                    answerDTO.setCode(DataUtil.safeToString(item[3]));
                    answerDTO.setAnswer(DataUtil.safeToString(item[4]));
                    answerDTO.setOrderIndex(DataUtil.safeToInt(item[5]));
                    answerDTO.setHasInput(DataUtil.safeToShort(item[6]));
                    answerDTO.setStatus(DataUtil.safeToShort(item[7]));
                    answerDTO.setMappingQuestionId(DataUtil.safeToLong(item[8]));
                    answerDTO.setOpinion(DataUtil.safeToString(item[9]));

                    lstResult.add(answerDTO);
                });
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lstResult;
    }
}
