package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.model.ccms_full.ReceiveCustLog;
import com.viettel.campaign.repository.ccms_full.ReceiveCustLogRepositoryCustom;
import com.viettel.campaign.utils.DataUtil;
import com.viettel.campaign.utils.HibernateUtil;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by gpdn-009 on 11/29/2019.
 */

@Repository
public class ReceiveCustLogRepositoryIpml implements ReceiveCustLogRepositoryCustom {
    private static final Logger logger = LoggerFactory.getLogger(ReceiveCustLogRepositoryIpml.class);

    @Override
    public Boolean checkCampaignStartAtLeastOnce(Long campaignId, Date startTime, Date endTime) {
        logger.info("--- Start checkCampaignStartAtLeastOnce:: ");
        if (DataUtil.isNullOrZero(campaignId) || startTime == null || endTime == null) return false;

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("SELECT COUNT(CAMPAIGN_ID) FROM RECEIVE_CUST_LOG ")
                    .append(" WHERE CAMPAIGN_ID = :p_campaign_id ")
                    .append(" AND START_TIME >= :p_start_time ")
                    .append(" AND START_TIME <= :p_end_time ")
                    .append("  GROUP BY CAMPAIGN_ID ");
            SQLQuery query = session.createSQLQuery(sb.toString());
            query.setParameter("p_campaign_id", campaignId);
            query.setParameter("p_start_time", startTime);
            query.setParameter("p_end_time", endTime);
            List count = query.list();
            if (count != null && count.size() > 0 && DataUtil.safeToInt(count.get(0)) > 0) {
                return true;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            session.close();
        }
        return false;
    }

    @Override
    public List<ReceiveCustLog> findTop100EndTimeIsNullAndCondition(String condition) {
        String pattern = "dd/MM/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        List<ReceiveCustLog> list = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("SELECT LOG.RECEIVE_CUST_LOG_ID receiveCustLogId, LOG.COMPANY_SITE_ID companySiteId, LOG.CUSTOMER_ID customerId, LOG.START_TIME startTime, LOG.AGENT_ID agentId, LOG.CAMPAIGN_ID campaignId, LOG.END_TIME endTime FROM RECEIVE_CUST_LOG LOG")
                    .append(" WHERE LOG.END_TIME IS NULL")
                    .append(" AND ((TO_DATE(:p_date, 'DD-MM-YYYY HH24:MI:SS') - LOG.START_TIME)*24*60*60) >= :p_condition")
                    .append(" AND ROWNUM <= 100 ORDER BY LOG.START_TIME DESC");
            SQLQuery query = session.createSQLQuery(sb.toString());
            query.setParameter("p_date", df.format(new Date()));
            query.setParameter("p_condition", Integer.parseInt(condition) * 60);

            query.addScalar("receiveCustLogId", new LongType());
            query.addScalar("companySiteId", new LongType());
            query.addScalar("customerId", new LongType());
            query.addScalar("startTime", new DateType());
            query.addScalar("agentId", new LongType());
            query.addScalar("campaignId", new LongType());
            query.addScalar("endTime", new DateType());
            query.setResultTransformer(Transformers.aliasToBean(ReceiveCustLog.class));
            //logger.info(df.format(new Date()));
            list = query.list();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            session.close();
        }
        return list;
    }
}
