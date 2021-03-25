package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by gpdn-019 on 12/5/2019.
 */

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findUserRoleByUserId(Long userId);
}

