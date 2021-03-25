package com.viettel.campaign.repository.ccms_full.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.repository.ccms_full.ReportRepositoryCustom;
import oracle.jdbc.OracleTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Repository
public class ReportRepositoryImpl implements ReportRepositoryCustom {

    @PersistenceContext(unitName = DataSourceQualify.JPA_UNIT_NAME_CCMS_FULL)
    private EntityManager entityManager;

    @Override
    public int getTotal(String queryString, HashMap<String, Object> hmapParams) {
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append("Select count(*) From (");
            strBuild.append(queryString);
            strBuild.append(") tbcount");
            Query query = this.entityManager.createNativeQuery(strBuild.toString());
            if (hmapParams != null) {
                Set set = hmapParams.entrySet();
                Map.Entry mentry;
                Object value;
                Iterator iterator = set.iterator();
                query.setParameter("out", OracleTypes.CURSOR);
                while (iterator.hasNext()) {
                    mentry = (Map.Entry) iterator.next();
                    value = mentry.getValue();
                    if (value == null) {
                        value = "";
                    }
                    query.setParameter(mentry.getKey().toString(), value);
                }
            }

            List resultQr = query.getResultList();
            if (resultQr != null && resultQr.size() > 0) {
                Object value = resultQr.get(0);
                String result = String.valueOf(value);
                this.entityManager.close();
                return Integer.valueOf(result);
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
