package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.repository.ccms_full.UserActionLogRepositoryCustom;
import com.viettel.campaign.utils.Constants;
import com.viettel.campaign.utils.HibernateUtil;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.UserActionLogDTO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author anhvd_itsol
 */

@Repository
@Transactional
public class UserActionLogRepositoryImpl implements UserActionLogRepositoryCustom {
    private static final Logger logger = LoggerFactory.getLogger(UserActionLogRepositoryImpl.class);


    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO insertToUserActionLog(UserActionLogDTO userActionLogDTO) {
        logger.info("Start insertToUserActionLog::");

        ResultDTO result = new ResultDTO();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try{
//            StringBuilder sqlStr = new StringBuilder();
//            sqlStr.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CAMPAIGN_MNG, "insert-to-user-action-log"));
            StringBuilder sb = new StringBuilder();

            sb.append("INSERT INTO \"USER_ACTION_LOG\" (");
            sb.append("    AGENT_ID,");
            sb.append("    COMPANY_SITE_ID,");
            sb.append("    SESSION_ID,");
            sb.append("    START_TIME,");
            sb.append("    END_TIME,");
            sb.append("    ACTION_TYPE,");
            sb.append("    DESCRIPTION,");
            sb.append("    OBJECT_ID)");
            sb.append(" VALUES (");
            sb.append("    :par_agentId,");
            sb.append("    :par_companySiteId,");
            sb.append("    :par_sessionId,");
            sb.append("    :par_startTime,");
            sb.append("    null,");
            sb.append("    :par_actionType,");
            sb.append("    null,");
            sb.append("    :par_objectId)");

            SQLQuery query = session.createSQLQuery(sb.toString());
            query.setParameter("par_agentId", userActionLogDTO.getAgentId() == null ? "" : userActionLogDTO.getAgentId());
            query.setParameter("par_companySiteId", userActionLogDTO.getCompanySiteId());
            query.setParameter("par_sessionId", userActionLogDTO.getSessionId());
            query.setParameter("par_startTime", userActionLogDTO.getStartTime());
            query.setParameter("par_actionType", userActionLogDTO.getActionType());
            query.setParameter("par_objectId", userActionLogDTO.getObjectId());

            query.executeUpdate();

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }finally {
            session.close();
        }

        return result;
    }
}
