package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.web.dto.VSAUsersDTO;
import com.viettel.campaign.web.dto.request_dto.AgentRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentCustomRepository {

    Page<VSAUsersDTO> getAgentsSelected(AgentRequestDTO agentRequestDTO, Pageable pageable);

    Page<VSAUsersDTO> getAgents(AgentRequestDTO agentRequestDTO, Pageable pageable);

}
