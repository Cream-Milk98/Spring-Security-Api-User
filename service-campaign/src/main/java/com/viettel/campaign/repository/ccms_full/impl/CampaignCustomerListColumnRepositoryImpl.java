package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.repository.ccms_full.CampaignCustomerListColumnRepositoryCustom;
import com.viettel.campaign.service.impl.CampaignExecuteServiceImp;
import com.viettel.campaign.utils.Constants;
import com.viettel.campaign.utils.HibernateUtil;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.request_dto.CampaignCustomerListColumnRequestDTO;
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
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CampaignCustomerListColumnRepositoryImpl implements CampaignCustomerListColumnRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(CampaignCustomerListColumnRepositoryImpl.class);

    @Override
    public List<CampaignCustomerListColumnRequestDTO> getCustomerInfor(Long companySiteId, Long customerId, Long campaignId) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        List<CampaignCustomerListColumnRequestDTO> data = new ArrayList<>();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT");
            sb.append("    DECODE(a.CUSTOMIZE_FIELD_TITLE, NULL, a.COLUMN_NAME, a.CUSTOMIZE_FIELD_TITLE) customizeFieldTitle,");
            sb.append("    c.VALUE_TEXT valueText,");
            sb.append("    c.VALUE_NUMBER valueNumber,");
            sb.append("    c.VALUE_DATE valueDate,");
            sb.append("    c.VALUE_CHECKBOX valueCheckbox,");
            sb.append("    d.NAME valueCombobox,");
            sb.append("    DECODE(b.TYPE, NULL, 'text', b.TYPE) type, decode (a.customize_field_title, null, '0', '1') dynamic ");
            sb.append(" FROM CAMPAIGN_CUSTOMERLIST_COLUMN a");
            sb.append("    LEFT JOIN CUSTOMIZE_FIELDS b ON a.CUSTOMIZE_FIELD_ID = b.CUSTOMIZE_FIELD_ID");
            sb.append("    LEFT JOIN CUSTOMIZE_FIELD_OBJECT c ON b.CUSTOMIZE_FIELD_ID = c.CUSTOMIZE_FIELDS_ID AND b.FUNCTION_CODE = 'CUSTOMER' AND c.OBJECT_ID = :p_customer_id");
            sb.append("    LEFT JOIN CUSTOMIZE_FIELD_OPTION_VALUE d ON c.FIELD_OPTION_VALUE_ID = d.FIELD_OPTION_VALUE_ID");
            sb.append(" WHERE 1 = 1");
            sb.append("    AND a.COMPANY_SITE_ID = :p_company_site_id");
            sb.append("    AND a.CAMPAIGN_ID = :p_campaign_id");
            sb.append("    ORDER BY a.ORDER_INDEX");

            SQLQuery query = session.createSQLQuery(sb.toString());

            query.setParameter("p_customer_id", customerId);
            query.setParameter("p_company_site_id", companySiteId);
            query.setParameter("p_campaign_id", campaignId);

            query.addScalar("customizeFieldTitle", new StringType());
            query.addScalar("valueText", new StringType());
            query.addScalar("valueNumber", new LongType());
            query.addScalar("valueDate", new DateType());
            query.addScalar("valueCheckbox", new ShortType());
            query.addScalar("valueCombobox", new StringType());
            query.addScalar("type", new StringType());
            query.addScalar("dynamic", new StringType());

            query.setResultTransformer(Transformers.aliasToBean(CampaignCustomerListColumnRequestDTO.class));

            data = query.list();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            session.close();
        }

        return data;
    }
}
