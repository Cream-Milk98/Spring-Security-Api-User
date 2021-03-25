package com.viettel.campaign.mapper;

import com.viettel.campaign.web.dto.ApParamDTO;
import com.viettel.campaign.model.ccms_full.ApParam;

public class ApParamMapper extends BaseMapper<ApParam, ApParamDTO> {

    @Override
    public ApParamDTO toDtoBean(ApParam apParam) {
        ApParamDTO apParamDTO = new ApParamDTO();

        if (apParam != null) {
            apParamDTO.setApParamId(apParam.getApParamId());
            apParamDTO.setParType(apParam.getParType());
            apParamDTO.setParName(apParam.getParName());
            apParamDTO.setParValue(apParam.getParValue());
            apParamDTO.setParCode(apParam.getParCode());
            apParamDTO.setDescription(apParam.getDescription());
            apParamDTO.setIsDelete(apParam.getIsDelete());
            apParamDTO.setIsDefault(apParam.getIsDefault());
            apParamDTO.setEnableEdit(apParam.getEnableEdit());
            apParamDTO.setSiteId(apParam.getSiteId());
        }

        return apParamDTO;
    }

    @Override
    public ApParam toPersistenceBean(ApParamDTO dtoBean) {
        ApParam apParam = new ApParam();

        if (dtoBean != null) {
            apParam.setApParamId(dtoBean.getApParamId());
            apParam.setParType(dtoBean.getParType());
            apParam.setParName(dtoBean.getParName());
            apParam.setParValue(dtoBean.getParValue());
            apParam.setParCode(dtoBean.getParCode());
            apParam.setDescription(dtoBean.getDescription());
            apParam.setIsDelete(dtoBean.getIsDelete());
            apParam.setIsDefault(dtoBean.getIsDefault());
            apParam.setEnableEdit(dtoBean.getEnableEdit());
            apParam.setSiteId(dtoBean.getSiteId());
        }

        return apParam;
    }
}
