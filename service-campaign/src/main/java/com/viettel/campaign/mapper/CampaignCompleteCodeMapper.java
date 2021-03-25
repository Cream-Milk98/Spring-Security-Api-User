package com.viettel.campaign.mapper;

import com.viettel.campaign.web.dto.CampaignCfgDTO;
import com.viettel.campaign.model.ccms_full.CampaignCfg;

public class CampaignCompleteCodeMapper extends BaseMapper<CampaignCfg, CampaignCfgDTO> {

    @Override
    public CampaignCfgDTO toDtoBean(CampaignCfg entity) {
        CampaignCfgDTO dto = new CampaignCfgDTO();

        if (entity != null) {
            dto.setCampaignCompleteCodeId(entity.getCampaignCompleteCodeId());
            dto.setCampaignId(entity.getCampaignId());
            dto.setCompleteValue(entity.getCompleteValue());
            dto.setCompleteName(entity.getCompleteName());
            dto.setDescription(entity.getDescription());
            dto.setStatus(entity.getStatus());
            dto.setCompleteType(entity.getCompleteType());
            dto.setIsRecall(entity.getIsRecall());
            dto.setUpdateBy(entity.getUpdateBy());
            dto.setUpdateAt(entity.getUpdateAt());
            dto.setCreateBy(entity.getCreateBy());
            dto.setCreateAt(entity.getCreateAt());
            dto.setCampaignType(entity.getCampaignType());
            dto.setIsFinish(entity.getIsFinish());
            dto.setCompanySiteId(entity.getCompanySiteId());
            dto.setIsLock(entity.getIsLock());
            dto.setDurationLock(entity.getDurationLock());
            dto.setChanel(entity.getChanel());
        }

        return dto;
    }

    @Override
    public CampaignCfg toPersistenceBean(CampaignCfgDTO dtoBean) {
        CampaignCfg entity = new CampaignCfg();

        if (dtoBean != null) {
            entity.setCampaignCompleteCodeId(dtoBean.getCampaignCompleteCodeId());
            entity.setCampaignId(dtoBean.getCampaignId());
            entity.setCompleteValue(dtoBean.getCompleteValue());
            entity.setCompleteName(dtoBean.getCompleteName());
            entity.setDescription(dtoBean.getDescription());
            entity.setStatus(dtoBean.getStatus());
            entity.setCompleteType(dtoBean.getCompleteType());
            entity.setIsRecall(dtoBean.getIsRecall());
            entity.setUpdateBy(dtoBean.getUpdateBy());
            entity.setUpdateAt(dtoBean.getUpdateAt());
            entity.setCreateBy(dtoBean.getCreateBy());
            entity.setCreateAt(dtoBean.getCreateAt());
            entity.setCampaignType(dtoBean.getCampaignType());
            entity.setIsFinish(dtoBean.getIsFinish());
            entity.setCompanySiteId(dtoBean.getCompanySiteId());
            entity.setIsLock(dtoBean.getIsLock());
            entity.setDurationLock(dtoBean.getDurationLock());
            entity.setChanel(dtoBean.getChanel());
        }

        return entity;
    }
}
