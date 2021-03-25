package com.viettel.campaign.model.ccms_full;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PHONE_NUMBER_RANK")
public class PhoneNumberRank {
    private Long id;
    private String accountId;
    private String phoneNumber;
    private int currentRank;
    private Integer syncedRank;
    private long createTime;
    private Long lastUpdateTime;
    private Long lastSyncTime;
    private int partitionHelper;

    @Basic
    @Column(name = "ACCOUNT_ID")
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "PHONE_NUMBER")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "CURRENT_RANK")
    public int getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(int currentRank) {
        this.currentRank = currentRank;
    }

    @Basic
    @Column(name = "SYNCED_RANK")
    public Integer getSyncedRank() {
        return syncedRank;
    }

    public void setSyncedRank(Integer syncedRank) {
        this.syncedRank = syncedRank;
    }

    @Basic
    @Column(name = "CREATE_TIME")
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "LAST_UPDATE_TIME")
    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Basic
    @Column(name = "LAST_SYNC_TIME")
    public Long getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(Long lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    @Basic
    @Column(name = "PARTITION_HELPER")
    public int getPartitionHelper() {
        return partitionHelper;
    }

    public void setPartitionHelper(int partitionHelper) {
        this.partitionHelper = partitionHelper;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "PHONE_NUMBER_RANK_SEQ")
    @SequenceGenerator(name = "PHONE_NUMBER_RANK_SEQ", sequenceName = "PHONE_NUMBER_RANK_SEQ", allocationSize = 1)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumberRank that = (PhoneNumberRank) o;
        return currentRank == that.currentRank &&
                createTime == that.createTime &&
                partitionHelper == that.partitionHelper &&
                id == that.id &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(syncedRank, that.syncedRank) &&
                Objects.equals(lastUpdateTime, that.lastUpdateTime) &&
                Objects.equals(lastSyncTime, that.lastSyncTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(accountId, phoneNumber, currentRank, syncedRank, createTime, lastUpdateTime, lastSyncTime, partitionHelper, id);
    }
}
