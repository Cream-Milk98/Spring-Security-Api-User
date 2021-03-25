package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.repository.ccms_full.TicketRepositoryCustom;
import com.viettel.campaign.utils.DateTimeUtil;
import com.viettel.campaign.utils.TimeZoneUtils;
import com.viettel.campaign.web.dto.TicketDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Repository
public class TicketRepositoryImpl implements TicketRepositoryCustom {

    private Logger logger = LoggerFactory.getLogger(TicketRepositoryImpl.class);

    @Autowired
    @PersistenceContext(unitName = DataSourceQualify.JPA_UNIT_NAME_CCMS_FULL)
    EntityManager entityManager;

    public List<TicketDTO> getHistory(String customerId, Long timezone, Pageable pageable) {
        List<TicketDTO> lst = new ArrayList<>();
        Integer tz = timezone.intValue() / 60;
        String expression = new StringBuilder()
                .append(" SELECT T.TICKET_ID, T.CREATE_DATE, T.SUBJECT, TCS.STATUS_NAME, T.RESOLVE_DATE ")
                .append(" FROM TICKET T ")
                .append(" INNER JOIN TICKET_CAT_STATUS TCS ON T.TICKET_STATUS = TCS.STATUS_ID ")
                .append(" WHERE 1 = 1 ")
                .append(" AND T.CUSTOMER_ID = :pCustomerId ")
                .toString();

        try {
            Query query = entityManager.createNativeQuery(expression);
            query.setParameter("pCustomerId", customerId);

            if (pageable != null) {
                query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                query.setMaxResults(pageable.getPageSize());
            }

            List<Object[]> data = query.getResultList();

            for (Object[] obj : data) {
                TicketDTO item = new TicketDTO();
                item.setTicketId(((BigDecimal) obj[0]).longValueExact());
                item.setCreateDate(DateTimeUtil.addHoursToJavaUtilDate((Date) obj[1], tz));
                item.setSubject((String) obj[2]);
                item.setStatusName((String) obj[3]);
                item.setResolveDate((Date) obj[4]);

                lst.add(item);
            }

            return lst;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return lst;
    }
}
