package com.viettel.campaign.web.dto;

public class PhoneNumberRankDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String accountId;
    private String phoneNumber;
    private int currentRank;
    private int syncedRank;
    private Long createTime;
    private Long lastUpdateTime;
    private Long lastSyncTime;
    private int partitionHelper;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Long getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(Long lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }


    public int getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(int currentRank) {
        this.currentRank = currentRank;
    }

    public int getPartitionHelper() {
        return partitionHelper;
    }

    public void setPartitionHelper(int partitionHelper) {
        this.partitionHelper = partitionHelper;
    }

    public int getSyncedRank() {
        return syncedRank;
    }

    public void setSyncedRank(int syncedRank) {
        this.syncedRank = syncedRank;
    }
}
