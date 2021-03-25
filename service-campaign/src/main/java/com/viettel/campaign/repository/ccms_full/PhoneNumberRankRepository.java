package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.PhoneNumberRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneNumberRankRepository extends JpaRepository<PhoneNumberRank, Long> {

    @Query("select pnr from PhoneNumberRank as pnr where pnr.accountId = :accountId" +
            " and pnr.phoneNumber = :phoneNumber" +
            " and pnr.partitionHelper =  :partitionHelper")
    List<PhoneNumberRank> findBy(
            @Param("accountId") String accountId,
            @Param("phoneNumber") String phoneNumber,
            @Param("partitionHelper") int partitionHelper);
}
