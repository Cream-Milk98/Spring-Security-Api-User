package com.viettel.campaign.service;

import com.viettel.campaign.web.dto.ApParamDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.model.ccms_full.ApParam;

import java.util.List;

public interface ApParamService {

    Iterable<ApParam> getAllParams(int page, int pageSize, String sort);

    List<ApParam> getParamByName(int page, int pageSize, String sort, String parName);

    ResultDTO createApParam(ApParamDTO apParamDTO);

    ResultDTO findParamByParType(String parType);

    List<ApParam>findAllParam();


}
