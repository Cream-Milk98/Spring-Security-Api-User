package com.viettel.campaign.service.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.mapper.CampaignCompleteCodeMapper;
import com.viettel.campaign.model.ccms_full.CampaignCfg;
import com.viettel.campaign.repository.ccms_full.CampaignCfgRepository;
import com.viettel.campaign.repository.ccms_full.CampaignCfgRepositoryCustom;
import com.viettel.campaign.service.CampaignCfgService;
import com.viettel.campaign.utils.Constants;
import com.viettel.campaign.utils.DataUtil;
import com.viettel.campaign.web.dto.CampaignCfgDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.request_dto.CampaignCfgRequestDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CampaignCfgServiceImpl implements CampaignCfgService {

    protected Logger logger = LoggerFactory.getLogger(CampaignCfgServiceImpl.class);

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CampaignCfgRepository completeCodeRepository;

    @Autowired
    CampaignCfgRepositoryCustom completeCodeRepositoryCustom;

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO listAllCompleteCode(int page, int pageSize, String sort, Long companySiteId) {
        ResultDTO resultDTO = new ResultDTO();
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc(sort)));

        if (DataUtil.isNullOrZero(companySiteId)) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            return resultDTO;
        }

        try {
            //resultDTO.setData(completeCodeRepositoryCustom.listAllCompleteCode(companySiteId, pageable));
            Page<CampaignCfg> data = completeCodeRepository.listAllCompleteCode(companySiteId, pageable);
            resultDTO.setData(data);
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            logger.error(e.getMessage(), e);
        }

        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public Map listCompleteCodeByName(int page, int pageSize, String sort, String name) {
        Map result = new HashMap();
        List<CampaignCfg> lst = new ArrayList<>();
        List<CampaignCfg> count = new ArrayList<>();

        try {
            lst = completeCodeRepository.findByCompleteNameContains(name, PageRequest.of(page, pageSize, Sort.by(sort)));
            count = completeCodeRepository.findByCompleteNameContains(name, null);

            result.put("totalItem", count.size());
            result.put("customers", lst);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return result;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO createCompleteCode(CampaignCfgDTO completeCodeDTO, Long userId) {
        ResultDTO resultDTO = new ResultDTO();
        CampaignCfg compCode = new CampaignCfg();

        try {
            if (completeCodeDTO != null) {

                CampaignCfg cl = new CampaignCfg();
                cl.setStatus((short) 1);
                cl.setCreateBy(String.valueOf(userId));
                cl.setCreateAt(new Date());
                cl.setUpdateBy(null);
                cl.setUpdateAt(null);
                cl.setChanel(completeCodeDTO.getChanel());
                cl.setCompleteName(completeCodeDTO.getCompleteName().trim());
                cl.setCompleteValue(completeCodeDTO.getCompleteValue());
                cl.setCampaignType(completeCodeDTO.getCampaignType());
                cl.setDescription(completeCodeDTO.getDescription().trim());
                cl.setCompleteType(completeCodeDTO.getCompleteType());
                cl.setCompanySiteId(completeCodeDTO.getCompanySiteId());
                cl.setCampaignCompleteCodeId(completeCodeDTO.getCampaignCompleteCodeId());
                cl.setCampaignId(completeCodeDTO.getCampaignId());
                cl.setDurationLock(completeCodeDTO.getDurationLock());
                cl.setIsFinish(completeCodeDTO.getIsFinish());
                cl.setIsLock(completeCodeDTO.getIsLock());
                cl.setIsRecall(completeCodeDTO.getIsRecall());

                resultDTO.setErrorCode("0");
                resultDTO.setDescription("Complete Code: " + compCode.getCampaignCompleteCodeId() + " created!");
                resultDTO.setData(completeCodeRepository.save(cl));
                return resultDTO;
            } else {
                resultDTO.setErrorCode("-2");
                resultDTO.setDescription("CompleteCodeDTO null");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO updateCompleteCode(CampaignCfgDTO completeCodeDTO) {
        ResultDTO resultDTO = new ResultDTO();
        CampaignCompleteCodeMapper compCodeMapper = new CampaignCompleteCodeMapper();
        CampaignCfg compCode = new CampaignCfg();

        try {
            if (completeCodeDTO != null) {
                // update
                compCode = compCodeMapper.toPersistenceBean(completeCodeDTO);
                completeCodeRepository.save(compCode);

                resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                resultDTO.setDescription("Complete Code: " + compCode.getCampaignCompleteCodeId() + " updated!");
            } else {
                resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                resultDTO.setDescription("CompleteCodeDTO null");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO deleteCompleteCode(CampaignCfgRequestDTO completeCodeDTO) {
        ResultDTO resultDTO = new ResultDTO();
        List<Long> listId = new ArrayList<>();
        try {
            // delete
            if (completeCodeDTO != null) {
                listId.add(completeCodeDTO.getCampaignCompleteCodeID());
//                completeCodeRepository.deleteCampaignCompleteCodeBy(completeCodeDTO.getCampaignCompleteCodeID(), completeCodeDTO.getCompanySiteId());

                if (completeCodeRepository.findAllCampaignCfg(listId, completeCodeDTO.getCompanySiteId()).size() > 0) {
                    completeCodeRepository.deleteCampaignCompleteCodeBy(completeCodeDTO.getCampaignCompleteCodeID(), completeCodeDTO.getCompanySiteId());

                    resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                    resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
                } else {
                    resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                    resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
                }

            } else {
                resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return resultDTO;
    }


    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO deleteList(CampaignCfgRequestDTO completeCodeDTO) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            if (completeCodeDTO != null) {
                if (completeCodeRepository.findAllCampaignCfg(completeCodeDTO.getListId(), completeCodeDTO.getCompanySiteId()).size() > 0) {
                    completeCodeRepository.deletedList(completeCodeDTO.getListId(), completeCodeDTO.getCompanySiteId());

                    resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                    resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
                } else {
                    resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                    resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
                }

            } else {
                resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO deleteById(CampaignCfgRequestDTO completeCodeDTO) {
        ResultDTO result = new ResultDTO();
        try {
            if (completeCodeDTO != null) {
                if (completeCodeRepository.findAllCampaignCfg(completeCodeDTO.getListId(), completeCodeDTO.getCompanySiteId()).size() > 0) {
                    completeCodeRepository.deletedList(completeCodeDTO.getListId(), completeCodeDTO.getCompanySiteId());

                    result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                    result.setDescription(Constants.ApiErrorDesc.SUCCESS);
                } else {
                    result.setErrorCode(Constants.ApiErrorCode.ERROR);
                    result.setDescription(Constants.ApiErrorDesc.ERROR);
                }

            } else {
                result.setErrorCode(Constants.ApiErrorCode.ERROR);
                result.setDescription(Constants.ApiErrorDesc.ERROR);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO findMaxValueCampaignCompleteCode(CampaignCfgDTO completeCodeDTO) {
        ResultDTO resultDTO = new ResultDTO();
        Long tmp;

        if (DataUtil.isNullOrZero(completeCodeDTO.getCompanySiteId())) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            return resultDTO;
        }

        try {
            // damdt
            //tmp = completeCodeRepositoryCustom.findMaxValueCampaignCompleteCode(completeCodeDTO);

            // tubn
            Long data = completeCodeRepository.findMaxValueCampaignCompleteCode(completeCodeDTO.getCompanySiteId());
            if (data != null && data >= 4) {
                tmp = data + 1;
            } else {
                tmp = 0L;
            }

            resultDTO.setData(tmp == 0 ? null : tmp);
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getListStatus(String completeValue, Short completeType, Long companySiteId) {
        ResultDTO resultDTO = new ResultDTO();
        List<CampaignCfg> list = new ArrayList<>();
        CampaignCfg cfg = new CampaignCfg();

        try {
            if (completeValue == null || "null".equalsIgnoreCase(completeValue))
                list = completeCodeRepository.getCustomerStatusByType(completeType, companySiteId);
            else if (completeType == -1)
                cfg = completeCodeRepository.findByCompanySiteIdAndCompleteValue(companySiteId, completeValue);
            else
                list = completeCodeRepository.getCustomerStatus(completeValue, completeType, companySiteId);

            resultDTO.setListData(list);
            resultDTO.setData(cfg);
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(e.getMessage());
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getListStatusWithoutType(String completeValue, Short completeType, Long companySiteId) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            List<CampaignCfg> list = completeCodeRepository.getCustomerStatusWithoutValue(completeValue, completeType, companySiteId);
            resultDTO.setListData(list);
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(e.getMessage());
        }
        return resultDTO;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, readOnly = true)
    public ResultDTO editCampaignCompleteCode(Long campaignCompleteCodeId) {
        ResultDTO resultDTO = new ResultDTO();

        if (DataUtil.isNullOrZero(campaignCompleteCodeId)) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            return resultDTO;
        }

        try {
            //List<CampaignCfgDTO> data = completeCodeRepositoryCustom.editCCC(campaignCompleteCodeId);
            CampaignCfgDTO data =  modelMapper.map(completeCodeRepository.getOne(campaignCompleteCodeId), CampaignCfgDTO.class);
            resultDTO.setData(data);
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            logger.error(e.getMessage(), e);
        }

        return resultDTO;
    }

}
