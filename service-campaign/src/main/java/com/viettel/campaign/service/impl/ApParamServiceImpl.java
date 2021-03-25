package com.viettel.campaign.service.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.mapper.ApParamMapper;
import com.viettel.campaign.model.ccms_full.ApParam;
import com.viettel.campaign.repository.ccms_full.ApParamRepository;
import com.viettel.campaign.service.ApParamService;
import com.viettel.campaign.utils.Constants;
import com.viettel.campaign.web.dto.ApParamDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApParamServiceImpl implements ApParamService {

    private static final Logger logger = LoggerFactory.getLogger(ApParamServiceImpl.class);


    @Autowired
    ApParamRepository apParamRepository;


    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public Iterable<ApParam> getAllParams(int page, int pageSize, String sort) {
        return apParamRepository.findAll(PageRequest.of(page, pageSize, Sort.by(sort)));
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public List<ApParam> getParamByName(int page, int pageSize, String sort, String parName) {
        List<ApParam> lst;

        lst = apParamRepository.findParamByName(parName, PageRequest.of(page, pageSize, Sort.by(sort)));

        return lst;
    }


    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO createApParam(ApParamDTO apParamDTO) {
        ResultDTO result = new ResultDTO();
        ApParamMapper apParamMapper = new ApParamMapper();
        ApParam apparam = new ApParam();

        try {
            if (apParamDTO != null) {
                apparam = apParamRepository.save(apParamMapper.toPersistenceBean(apParamDTO));

                result.setErrorCode("0");
                result.setDescription("create apparam");
                result.setData(apparam);
            } else {
                result.setErrorCode("-1");
                result.setDescription("create apparam FAIL");
                result.setData(apparam);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO findParamByParType(String parType) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            List<ApParam> lst = apParamRepository.findParamByParType(parType);
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setData(lst);
            logger.info("find params by type: " + resultDTO.getErrorCode());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    public List<ApParam> findAllParam() {
        return apParamRepository.findAllParam("CAMPAIGN_TYPE") ;
    }


}
