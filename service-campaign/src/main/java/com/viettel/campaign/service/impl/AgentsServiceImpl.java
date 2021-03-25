package com.viettel.campaign.service.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.mapper.CampaignAgentMapper;
import com.viettel.campaign.model.acd_full.Agents;
import com.viettel.campaign.model.ccms_full.CampaignAgent;
import com.viettel.campaign.repository.acd_full.AgentsRepository;
import com.viettel.campaign.repository.ccms_full.AgentCustomRepository;
import com.viettel.campaign.repository.ccms_full.CampaignAgentRepository;
import com.viettel.campaign.repository.ccms_full.impl.CustomerListRepositoryImpl;
import com.viettel.campaign.service.AgentsService;
import com.viettel.campaign.utils.Constants;
import com.viettel.campaign.utils.DataUtil;
import com.viettel.campaign.utils.SQLBuilder;
import com.viettel.campaign.web.dto.CampaignAgentDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.VSAUsersDTO;
import com.viettel.campaign.web.dto.request_dto.AgentRequestDTO;
import com.viettel.campaign.web.dto.request_dto.CampaignAgentRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgentsServiceImpl implements AgentsService {

    private Logger logger = LoggerFactory.getLogger(AgentsServiceImpl.class);

    @Autowired
    AgentsRepository agentsRepository;

    @Autowired
    AgentCustomRepository agentCustomRepository;

    @Autowired
    CampaignAgentRepository campaignAgentRepository;

    @Override
    public ResultDTO getAgentsByAgentId(String agentId) {
        ResultDTO result = new ResultDTO();

        try {
            Agents data = agentsRepository.findByAgentId(agentId);

            if (data != null) {
                result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                result.setDescription("agents data");
                result.setData(data);

            } else {
                result.setErrorCode(Constants.ApiErrorCode.ERROR);
                result.setDescription("agents data null");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return result;
    }

    @Override
    public ResultDTO createCampaignAgent(CampaignAgentDTO campaignAgentDTO) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            if (campaignAgentDTO != null) {
                CampaignAgent campaignAgent = campaignAgentRepository.findByCampaignIdAndCompanySiteIdAndAgentId(campaignAgentDTO.getCampaignId(), campaignAgentDTO.getCompanySiteId(), campaignAgentDTO.getAgentId());
                // insert
                if (campaignAgent == null) {
                    campaignAgent = new CampaignAgent();
                    campaignAgent.setAgentId(campaignAgentDTO.getAgentId());
                    campaignAgent.setStatus(0);
                    campaignAgent.setReSchedule(0L);
                    campaignAgent.setFilterType((short) 0);
                    campaignAgent.setCompanySiteId(campaignAgentDTO.getCompanySiteId());
                    campaignAgent.setCampaignId(campaignAgentDTO.getCampaignId());

                    campaignAgentRepository.save(campaignAgent);

                    resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                    resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
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
    public ResultDTO deleteCampaignAgentById(List<Long> campaignAgentId) {
        ResultDTO resultDTO = new ResultDTO();

        try {
            if (campaignAgentId.size() > 1) {//bản cũ
                // delete
                Long campaignId = campaignAgentId.get(campaignAgentId.size() - 1);
                campaignAgentRepository.deleteByCampaignIdAndAgentIdIn(campaignId, campaignAgentId);

                resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            }
            else {
                resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO searchCampaignAgentByName(AgentRequestDTO agentRequestDTO) {
        ResultDTO resultDTO = new ResultDTO();

        if (DataUtil.isNullOrZero(agentRequestDTO.getCompanySiteId())) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            return resultDTO;
        }

        try {
            Page<VSAUsersDTO> dtoList = agentCustomRepository.getAgents(agentRequestDTO, SQLBuilder.buildPageable(agentRequestDTO));

            resultDTO.setTotalRow(dtoList.getTotalElements());
            resultDTO.setListData(dtoList.getContent());
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
    public ResultDTO searchCampaignAgentSelectByName(AgentRequestDTO agentRequestDTO) {
        ResultDTO resultDTO = new ResultDTO();

        if (DataUtil.isNullOrZero(agentRequestDTO.getCompanySiteId())) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            return resultDTO;
        }
        try {
            Page<VSAUsersDTO> dtoList = agentCustomRepository.getAgentsSelected(agentRequestDTO, SQLBuilder.buildPageable(agentRequestDTO));

            resultDTO.setTotalRow(dtoList.getTotalElements());
            resultDTO.setListData(dtoList.getContent());
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
    public ResultDTO createMultipleCampaignAgent(CampaignAgentRequestDTO campaignAgentRequestDTO) {
        ResultDTO resultDTO = new ResultDTO();
//        CampaignAgentMapper campaignAgentsMapper = new CampaignAgentMapper();
        List<CampaignAgent> campaignAgentList = new ArrayList<>();
        try {
            if (campaignAgentRequestDTO != null) {
                for (int i = 0; i < campaignAgentRequestDTO.getAgentId().size(); i++) {
                    CampaignAgent campaignAgent = new CampaignAgent();
                    campaignAgent.setAgentId(campaignAgentRequestDTO.getAgentId().get(i));
                    campaignAgent.setStatus(0);
                    campaignAgent.setReSchedule(0L);
                    campaignAgent.setFilterType((short) 0);
                    campaignAgent.setCompanySiteId(campaignAgentRequestDTO.getCompanySiteId());
                    campaignAgent.setCampaignId(campaignAgentRequestDTO.getCampaignId());
                    campaignAgentList.add(campaignAgent);
                }
                // insert
//                campaignAgent = campaignAgentsMapper.toPersistenceBean();
                campaignAgentRepository.saveAll(campaignAgentList);

                resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
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
    public ResultDTO updateAgent(CampaignAgentDTO campaignAgentDTO) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            if (campaignAgentDTO != null) {
                Agents agents = new Agents();
                agents.setAgentId(campaignAgentDTO.getAgentId().toString());
                agents.setCampaignSystemStatus("AVAILABLE");
                agentsRepository.save(agents);

                resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);

            } else {
                resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return resultDTO;
    }
}
