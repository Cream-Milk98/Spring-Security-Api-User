package com.viettel.campaign.service;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.CustomerTime;
import com.viettel.campaign.repository.ccms_full.CustomerTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hanv_itsol
 * @project campaign
 */

@Service
@Transactional
public class CustomerTimeService {

    @Autowired
    private CustomerTimeRepository customerTimeRepository;

    @Transactional(DataSourceQualify.CCMS_FULL)
    public CustomerTime update(CustomerTime customerTime){
        return customerTimeRepository.save(customerTime);
    }

    @Transactional(DataSourceQualify.CCMS_FULL)
    public List<CustomerTime> findByCustomerId(Long customerId){
        return customerTimeRepository.findByCustomerId(customerId);
    }
}
