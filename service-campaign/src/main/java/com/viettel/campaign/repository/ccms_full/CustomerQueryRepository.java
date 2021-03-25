package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomerQueryRepository {
    List<Customer> findAll(String rsqlQuery);
}
