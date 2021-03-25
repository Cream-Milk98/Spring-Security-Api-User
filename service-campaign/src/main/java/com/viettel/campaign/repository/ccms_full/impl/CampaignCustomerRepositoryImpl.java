package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.ApParam;
import com.viettel.campaign.repository.ccms_full.ApParamRepository;
import com.viettel.campaign.repository.ccms_full.CampaignCustomerRepositoryCustom;
import com.viettel.campaign.service.impl.CampaignExecuteServiceImp;
import com.viettel.campaign.utils.Constants;
import com.viettel.campaign.utils.DataUtil;
import com.viettel.campaign.utils.HibernateUtil;
import com.viettel.campaign.utils.SQLBuilder;
import com.viettel.campaign.web.dto.CampaignCustomerDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
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
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Transactional(DataSourceQualify.CCMS_FULL)
public class CampaignCustomerRepositoryImpl implements CampaignCustomerRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(CampaignCustomerRepositoryImpl.class);

    @Autowired
    @PersistenceContext(unitName = DataSourceQualify.JPA_UNIT_NAME_CCMS_FULL)
    EntityManager entityManager;

    @Override
    public List<CampaignCustomerDTO> getDataCampaignCustomer(CampaignCustomerDTO customerDTO, String expression, String dungsai) {
        List<CampaignCustomerDTO> result = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            SQLQuery query = session.createSQLQuery(expression);
            query.setParameter("campaignId", customerDTO.getCampaignId());
            query.setParameter("companySiteId", customerDTO.getCompanySiteId());
            if (expression.contains(":agentId")) {
                query.setParameter("agentId", customerDTO.getAgentId());
            }
            if (expression.contains(":dungSai")) {
                query.setParameter("dungSai", dungsai);
            }
            if (expression.contains(":sysdate")) {
                query.setParameter("sysdate", new Date());
            }

            query.addScalar("customerId", new LongType());

            query.setResultTransformer(Transformers.aliasToBean(CampaignCustomerDTO.class));

            result = query.list();

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            session.close();
        }

        return result;
    }
}
