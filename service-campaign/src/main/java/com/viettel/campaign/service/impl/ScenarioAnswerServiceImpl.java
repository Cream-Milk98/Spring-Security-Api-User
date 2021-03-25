package com.viettel.campaign.service.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.Campaign;
import com.viettel.campaign.model.ccms_full.CampaignLog;
import com.viettel.campaign.model.ccms_full.ScenarioAnswer;
import com.viettel.campaign.repository.ccms_full.CampaignLogRepository;
import com.viettel.campaign.repository.ccms_full.CampaignRepository;
import com.viettel.campaign.repository.ccms_full.ContactQuestResultRepository;
import com.viettel.campaign.repository.ccms_full.ScenarioAnswerRepository;
import com.viettel.campaign.service.ScenarioAnswerService;
import com.viettel.campaign.utils.Constants;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.ScenarioAnswerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author anhvd_itsol
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class ScenarioAnswerServiceImpl implements ScenarioAnswerService {
    private static final Logger logger = LoggerFactory.getLogger(ScenarioAnswerServiceImpl.class);

    @Autowired
    ScenarioAnswerRepository scenarioAnswerRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    CampaignLogRepository campaignLogRepository;

    @Autowired
    ContactQuestResultRepository contactQuestResultRepository;

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO findByScenarioQuestionCompany(Long scenarioQuestionId, Long companySiteId) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            List<ScenarioAnswer> lst = scenarioAnswerRepository.findByScenarioQuestionIdAndCompanySiteIdAndStatusOrderByOrderIndex(scenarioQuestionId, companySiteId, (short) 1);
            resultDTO.setData(lst);
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch(Exception ex) {
            logger.error(ex.getMessage(), ex);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public Long getMaxAnswerOrderId(Long scenarioQuestionId, Long companySiteId) {
        try {
            return scenarioAnswerRepository.getMaxAnswerOrderId(scenarioQuestionId, companySiteId);
        }catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO delete(ScenarioAnswerDTO scenarioAnswerDTO) {
        ResultDTO resultDTO = new ResultDTO();
        CampaignLog campaignLog;
        try {
            //find campaign
            Campaign campaign = campaignRepository.findByCampaignId(scenarioAnswerDTO.getCampaignId());
            if(campaign != null) {
                if (campaign.getStatus() == 0L || campaign.getStatus() == 1L) {
                    scenarioAnswerRepository.deleteById(scenarioAnswerDTO.getScenarioAnswerId());
                    campaignLog = new CampaignLog();
                    campaignLog.setCompanySiteId(scenarioAnswerDTO.getCompanySiteId());
                    campaignLog.setCreateTime(new Date());
                    campaignLog.setAgentId(null);
                    campaignLog.setTableName("SCENARIO_ANSWER");
                    campaignLog.setDescription("Xoa cau tra loi");
                    campaignLog.setCampaignId(scenarioAnswerDTO.getCampaignId());
                    campaignLog.setCustomerId(scenarioAnswerDTO.getScenarioAnswerId());
                    campaignLogRepository.save(campaignLog);
                }
                else {
                    ScenarioAnswer answer = scenarioAnswerRepository.findScenarioAnswerByScenarioAnswerId(scenarioAnswerDTO.getScenarioAnswerId());
                    answer.setStatus((short) 0);
                    answer.setDeleteTime(new Date());
                    scenarioAnswerRepository.save(answer);

                    campaignLog = new CampaignLog();
                    campaignLog.setCompanySiteId(scenarioAnswerDTO.getCompanySiteId());
                    campaignLog.setCreateTime(new Date());
                    campaignLog.setAgentId(null);
                    campaignLog.setTableName("SCENARIO_ANSWER");
                    campaignLog.setDescription("Chinh sua tra loi");
                    campaignLog.setCampaignId(scenarioAnswerDTO.getCampaignId());
                    campaignLog.setCustomerId(scenarioAnswerDTO.getScenarioAnswerId());
                    campaignLogRepository.save(campaignLog);

                    //delete interacted record from  contact_quest_result by question_id
                    contactQuestResultRepository.deleteByScenarioQuestionId(answer.getScenarioQuestionId());
                }
                resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public Integer countDuplicateScenarioCode(String code, Long scenarioQuestionId, Long scenarioAnswerId) {
        try {
            return scenarioAnswerRepository.countDuplicateScenarioCode(code, scenarioQuestionId, scenarioAnswerId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }
}
