package com.viettel.campaign.repository.ccms_full.impl;

import com.github.tennaito.rsql.jpa.JpaCriteriaQueryVisitor;
import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.Customer;
import com.viettel.campaign.repository.ccms_full.CustomerQueryRepository;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class CustomerQueryRepositoryImpl implements CustomerQueryRepository {
    @PersistenceContext(unitName = DataSourceQualify.JPA_UNIT_NAME_CCMS_FULL)
    private EntityManager entityManager;
    private RSQLVisitor<CriteriaQuery<Customer>, EntityManager> visitor = new JpaCriteriaQueryVisitor<Customer>();
    private RSQLParser parser = new RSQLParser();

    @Override
    public List<Customer> findAll(String rsqlQuery) {
        Node node = parser.parse(rsqlQuery);
        CriteriaQuery<Customer> query = node.accept(visitor, entityManager);

        return entityManager.createQuery(query).getResultList();
    }
}
