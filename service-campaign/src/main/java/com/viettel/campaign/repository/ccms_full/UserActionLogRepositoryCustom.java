package com.viettel.campaign.repository.ccms_full;


import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.UserActionLogDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author anhvd_itsol
 */

public interface UserActionLogRepositoryCustom {
     ResultDTO insertToUserActionLog(UserActionLogDTO userActionLogDTO);
}
