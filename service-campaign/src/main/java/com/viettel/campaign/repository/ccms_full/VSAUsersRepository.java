package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.VSAUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VSAUsersRepository extends JpaRepository<VSAUsers, Long> {

    List<VSAUsers> findAllByCompanySiteId(Long companySiteId);

    VSAUsers findByUserId(Long userId);
}
