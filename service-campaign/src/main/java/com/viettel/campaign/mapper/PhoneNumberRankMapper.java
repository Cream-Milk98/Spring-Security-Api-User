package com.viettel.campaign.mapper;

import com.viettel.campaign.model.ccms_full.PhoneNumberRank;
import com.viettel.campaign.web.dto.PhoneNumberRankDTO;


public class PhoneNumberRankMapper extends BaseMapper<PhoneNumberRank, PhoneNumberRankDTO> {
    @Override
    public PhoneNumberRankDTO toDtoBean(PhoneNumberRank model) {
        PhoneNumberRankDTO obj = null;
        if (model != null) {
            obj = new PhoneNumberRankDTO();
            obj.setCreateTime(model.getCreateTime());
            obj.setCurrentRank(model.getCurrentRank());
            obj.setId(model.getId());
            obj.setLastSyncTime(model.getLastSyncTime());
            obj.setLastUpdateTime(model.getLastUpdateTime());
            obj.setPartitionHelper(model.getPartitionHelper());
            obj.setPhoneNumber (model.getPhoneNumber());
            obj.setAccountId(model.getAccountId());
            obj.setSyncedRank(model.getSyncedRank());
        }
        return obj;
    }

    @Override
    public PhoneNumberRank toPersistenceBean(PhoneNumberRankDTO dtoBean) {
        PhoneNumberRank obj = null;
        if (dtoBean != null) {
            obj = new PhoneNumberRank();
            obj.setCreateTime(dtoBean.getCreateTime());
            obj.setCurrentRank(dtoBean.getCurrentRank());
            obj.setId(dtoBean.getId());
            obj.setLastSyncTime(dtoBean.getLastSyncTime());
            obj.setLastUpdateTime(dtoBean.getLastUpdateTime());
            obj.setPartitionHelper(dtoBean.getPartitionHelper());
            obj.setPhoneNumber (dtoBean.getPhoneNumber());
            obj.setAccountId(dtoBean.getAccountId());
            obj.setSyncedRank(dtoBean.getSyncedRank());
        }
        return obj;
    }

}
