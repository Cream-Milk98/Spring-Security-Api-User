package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.model.ccms_full.CallInteractionOtherLeg;
import com.viettel.campaign.repository.ccms_full.CallInteractionOrtherLegRepositoryCustom;
import com.viettel.campaign.utils.HibernateUtil;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gpdn-009 on 11/29/2019.
 */

@Repository
public class CallInteractionOrtherLegRepositoryIpml implements CallInteractionOrtherLegRepositoryCustom {
//    private static final Logger logger = LogManager.getLogger(CallInteractionOrtherLegRepositoryIpml.class);

    private static final Logger logger = LoggerFactory.getLogger(CallInteractionOrtherLegRepositoryIpml.class);
    @Override
    public List<CallInteractionOtherLeg> getCallInteractionByPhoneNumber(String phoneNumber) {
        Date dtNow = new Date();
        List<CallInteractionOtherLeg> list = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("SELECT A.CALL_ID legCallId, " +
                    "           A.INTERACTION_ID interactionId, " +
                    "           B.CREATE_TIME createTime, " +
                    "           B.ANSWER_TIME answerTime, " +
                    "           B.DISCONNECT_TIME disconnectTime, " +
                    "           B.DIRECTION direction, " +
                    "           B.ANSWER_DURATION answerDuration " +
                    " FROM CALL_INTERACTION A INNER JOIN CALL_INTERACTION_OTHER_LEG B ON A.INTERACTION_ID = B.INTERACTION_ID" +
                    " WHERE A.CALLEE_ID_NUMBER = :p_phone_number and (B.CREATE_TIME*1000 - :p_date) <= 5*60*1000 " +
                    " ORDER BY B.CREATE_TIME DESC, B.ID DESC ");
            SQLQuery query = session.createSQLQuery(sb.toString());
            query.setParameter("p_date", dtNow.getTime());
            query.setParameter("p_phone_number", phoneNumber);

            query.addScalar("legCallId", new StringType());
            query.addScalar("interactionId", new StringType());
            query.addScalar("createTime", new LongType());
            query.addScalar("answerTime", new LongType());
            query.addScalar("disconnectTime", new LongType());
            query.addScalar("direction", new StringType());
            query.addScalar("answerDuration", new LongType());
            query.setResultTransformer(Transformers.aliasToBean(CallInteractionOtherLeg.class));
            list = query.list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public List<CallInteractionOtherLeg> getCallInteractionByCallID(String callID) {
        Date dtNow = new Date();
        List<CallInteractionOtherLeg> list = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("SELECT A.CALL_ID legCallId, " +
                    "           A.INTERACTION_ID interactionId, " +
                    "           B.CREATE_TIME createTime, " +
                    "           B.ANSWER_TIME answerTime, " +
                    "           B.DISCONNECT_TIME disconnectTime, " +
                    "           B.DIRECTION direction, " +
                    "           B.ANSWER_DURATION answerDuration " +
                    " FROM CALL_INTERACTION A INNER JOIN CALL_INTERACTION_OTHER_LEG B ON A.INTERACTION_ID = B.INTERACTION_ID" +
                    " WHERE A.CALL_ID = :p_call_id and (B.CREATE_TIME*1000 - :p_date) <= 60*60*1000 " +
                    " ORDER BY B.CREATE_TIME DESC, B.ID DESC ");
            SQLQuery query = session.createSQLQuery(sb.toString());
            query.setParameter("p_date", dtNow.getTime());
            query.setParameter("p_call_id", callID);

            query.addScalar("legCallId", new StringType());
            query.addScalar("interactionId", new StringType());
            query.addScalar("createTime", new LongType());
            query.addScalar("answerTime", new LongType());
            query.addScalar("disconnectTime", new LongType());
            query.addScalar("direction", new StringType());
            query.addScalar("answerDuration", new LongType());
            query.setResultTransformer(Transformers.aliasToBean(CallInteractionOtherLeg.class));
            list = query.list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            session.close();
        }
        return list;
    }
}
