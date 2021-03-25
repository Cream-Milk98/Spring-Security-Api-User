package com.viettel.campaign.service;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.ProcessConfig;
import com.viettel.campaign.repository.ccms_full.ProcessConfigRepository;
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
public class ProcessConfigService {

    @Autowired
    private ProcessConfigRepository processConfigRepository;

    @Transactional(DataSourceQualify.CCMS_FULL)
    public List<ProcessConfig> findAll(){
        return processConfigRepository.findAll();
    }


    @Transactional(DataSourceQualify.CCMS_FULL)
    public List<ProcessConfig> findAllByCode(String configCode){
        return processConfigRepository.findAllByConfigCode(configCode);
    }

    @Transactional(DataSourceQualify.CCMS_FULL)
    public void update(ProcessConfig pc){
        processConfigRepository.save(pc);
    }

}
