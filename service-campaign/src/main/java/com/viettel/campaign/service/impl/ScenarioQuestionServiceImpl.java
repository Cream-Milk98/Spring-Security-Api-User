package com.viettel.campaign.service.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.Campaign;
import com.viettel.campaign.model.ccms_full.CampaignLog;
import com.viettel.campaign.model.ccms_full.ScenarioAnswer;
import com.viettel.campaign.model.ccms_full.ScenarioQuestion;
import com.viettel.campaign.repository.ccms_full.*;
import com.viettel.campaign.service.ScenarioQuestionService;
import com.viettel.campaign.utils.Constants;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.ScenarioAnswerDTO;
import com.viettel.campaign.web.dto.ScenarioQuestionDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author anhvd_itsol
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class ScenarioQuestionServiceImpl implements ScenarioQuestionService {
    private static final Logger logger = LoggerFactory.getLogger(ScenarioQuestionServiceImpl.class);

    @Autowired
    ScenarioQuestionRepository scenarioQuestionRepository;

    @Autowired
    CampaignLogRepository campaignLogRepository;

    @Autowired
    ScenarioAnswerRepository scenarioAnswerRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ScenarioQuestionRepositoryCustom questionRepositoryCustom;

    @Autowired
    ContactQuestResultRepository contactQuestResultRepository;

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO findByScenarioIdAndCampaignIdAndCompanySiteId(Long scenarioId, Long campaignId, Long companySiteId) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            List<ScenarioQuestion> lst = scenarioQuestionRepository.findScenarioQuestionsByScenarioIdAndCampaignIdAndCompanySiteIdAndStatusOrderByOrderIndex(scenarioId, campaignId, companySiteId, (short) 1);
            resultDTO.setData(lst);
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO add(ScenarioQuestionDTO scenarioQuestionDTO) {
        ResultDTO resultDTO = new ResultDTO();
        List<ScenarioAnswerDTO> lstAnswers;
        List<ScenarioAnswer> lstAnswersToInsert = new ArrayList<>();
        scenarioQuestionDTO.setQuestion(scenarioQuestionDTO.getQuestion().trim());
        try {
            ScenarioQuestion scenarioQuestion = modelMapper.map(scenarioQuestionDTO, ScenarioQuestion.class);

            scenarioQuestion.setCreateTime(new Date());
            scenarioQuestion.setStatus((short) 1);
            scenarioQuestionRepository.save(scenarioQuestion);

            if (scenarioQuestionDTO.getLstAnswers().size() > 0) {
                lstAnswers = scenarioQuestionDTO.getLstAnswers();
                lstAnswers.forEach(item -> {
                    item.setScenarioQuestionId(scenarioQuestion.getScenarioQuestionId());
                    item.setCreateTime(new Date());
                    item.setStatus((short) 1);
                    item.setAnswer(item.getAnswer().trim());
                    ScenarioAnswer answer = modelMapper.map(item, ScenarioAnswer.class);
                    lstAnswersToInsert.add(answer);
                });

                scenarioAnswerRepository.saveAll(lstAnswersToInsert);

                for (ScenarioAnswer a : lstAnswersToInsert) {
                    CampaignLog campaignLogAnswer = new CampaignLog();
                    campaignLogAnswer.setCompanySiteId(scenarioQuestion.getCompanySiteId());
                    campaignLogAnswer.setCreateTime(new Date());
                    campaignLogAnswer.setAgentId(null);
                    campaignLogAnswer.setTableName("SCENARIO_ANSWER");
                    campaignLogAnswer.setColumnName(null);
                    campaignLogAnswer.setPreValue(null);
                    campaignLogAnswer.setPostValue(null);
                    campaignLogAnswer.setCampaignId(scenarioQuestion.getCampaignId());
                    campaignLogAnswer.setCustomerId(a.getScenarioAnswerId());
                    campaignLogAnswer.setDescription("Them moi cau tra loi");
                    campaignLogRepository.save(campaignLogAnswer);
                }
            }

            CampaignLog campaignLog = new CampaignLog();
            campaignLog.setCompanySiteId(scenarioQuestion.getCompanySiteId());
            campaignLog.setCreateTime(new Date());
            campaignLog.setAgentId(null);
            campaignLog.setTableName("SCENARIO_QUESTION");
            campaignLog.setColumnName(null);
            campaignLog.setPreValue(null);
            campaignLog.setPostValue(null);
            campaignLog.setDescription("Them moi cau hoi rieng le");
            campaignLog.setCampaignId(scenarioQuestion.getCampaignId());
            campaignLog.setCustomerId(scenarioQuestion.getScenarioQuestionId());
            campaignLogRepository.save(campaignLog);
            resultDTO.setData(scenarioQuestion);
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public Long getMaxOrderId(Long scenarioId, Long campaignId, Long companySiteId) {
        try {
            Long index = scenarioQuestionRepository.getMaxOrderId(scenarioId, campaignId, companySiteId);
            if (index == null) return 0L;
            else return index;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public Integer countDuplicateQuestionCode(ScenarioQuestionDTO questionDTO) {
        try {
            return questionRepositoryCustom.countDuplicateQuestionCode(questionDTO);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO delete(ScenarioQuestionDTO scenarioQuestionDTO) {
        ResultDTO resultDTO = new ResultDTO();
        CampaignLog campaignLog;
        try {
            //find campaign
            Campaign campaign = campaignRepository.findByCampaignId(scenarioQuestionDTO.getCampaignId());
            if (campaign != null) {
                if (campaign.getStatus() == 0L || campaign.getStatus() == 1L) {
                    //delete question by questionId
                    scenarioQuestionRepository.deleteById(scenarioQuestionDTO.getScenarioQuestionId());
                    campaignLog = new CampaignLog();
                    campaignLog.setCompanySiteId(scenarioQuestionDTO.getCompanySiteId());
                    campaignLog.setCreateTime(new Date());
                    campaignLog.setTableName("SCENARIO_QUESTION");
                    campaignLog.setDescription("Xoa cau hoi");
                    campaignLog.setCampaignId(scenarioQuestionDTO.getCampaignId());
                    campaignLog.setCustomerId(scenarioQuestionDTO.getScenarioQuestionId());
                    campaignLogRepository.save(campaignLog);

                    //delete answer by questionId
                    scenarioAnswerRepository.deleteScenarioAnswersByScenarioQuestionId(scenarioQuestionDTO.getScenarioQuestionId());
                    campaignLog = new CampaignLog();
                    campaignLog.setCompanySiteId(scenarioQuestionDTO.getCompanySiteId());
                    campaignLog.setCreateTime(new Date());
                    campaignLog.setTableName("SCENARIO_ANSWER");
                    campaignLog.setDescription("Xoa cau tra loi");
                    campaignLog.setCampaignId(scenarioQuestionDTO.getCampaignId());
                    campaignLog.setCustomerId(scenarioQuestionDTO.getScenarioQuestionId());
                    campaignLogRepository.save(campaignLog);
                } else {
                    //get and update status question
                    ScenarioQuestion scenarioQuestion = scenarioQuestionRepository.findScenarioQuestionByScenarioQuestionId(scenarioQuestionDTO.getScenarioQuestionId());
                    scenarioQuestion.setStatus((short) 0);
                    scenarioQuestion.setDeleteTime(new Date());
                    scenarioQuestionRepository.save(scenarioQuestion);

                    campaignLog = new CampaignLog();
                    campaignLog.setCompanySiteId(scenarioQuestionDTO.getCompanySiteId());
                    campaignLog.setCreateTime(new Date());
                    campaignLog.setTableName("SCENARIO_QUESTION");
                    campaignLog.setDescription("Chinh sua cau hoi");
                    campaignLog.setCampaignId(scenarioQuestionDTO.getCampaignId());
                    campaignLog.setCustomerId(scenarioQuestionDTO.getScenarioQuestionId());
                    campaignLogRepository.save(campaignLog);

                    List<ScenarioAnswer> lstAnswer = scenarioAnswerRepository.findByScenarioQuestionIdAndCompanySiteIdAndStatusOrderByOrderIndex(scenarioQuestion.getScenarioQuestionId(),
                            scenarioQuestion.getCompanySiteId(), (short) 1);
                    if (lstAnswer.size() > 0) {
                        lstAnswer.forEach(answer -> {
                            answer.setStatus((short) 0);
                            answer.setDeleteTime(new Date());

                            CampaignLog log = new CampaignLog();
                            log.setCompanySiteId(scenarioQuestionDTO.getCompanySiteId());
                            log.setCreateTime(new Date());
                            log.setTableName("SCENARIO_ANSWER");
                            log.setDescription("Chinh sua cau tra loi");
                            log.setCampaignId(scenarioQuestionDTO.getCampaignId());
                            log.setCustomerId(answer.getScenarioAnswerId());
                            campaignLogRepository.save(log);
                        });
                    }
                    //delete interacted record from  contact_quest_result by question_id
                    contactQuestResultRepository.deleteByScenarioQuestionId(scenarioQuestion.getScenarioQuestionId());
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
    public ResultDTO update(ScenarioQuestionDTO scenarioQuestionDTO) {
        ResultDTO resultDTO = new ResultDTO();
        List<ScenarioAnswerDTO> lstAnswers;
        if (scenarioQuestionDTO.getScenarioQuestionId() == null) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            return resultDTO;
        }
        try {
            scenarioQuestionDTO.setCode(scenarioQuestionDTO.getCode().trim());
            scenarioQuestionDTO.setQuestion(scenarioQuestionDTO.getQuestion().trim());
            ScenarioQuestion scenarioQuestion = modelMapper.map(scenarioQuestionDTO, ScenarioQuestion.class);

            if (scenarioQuestion.getScenarioQuestionId() != null) {
                scenarioQuestionRepository.save(scenarioQuestion);
                CampaignLog campaignLog = new CampaignLog();
                campaignLog.setCompanySiteId(scenarioQuestion.getCompanySiteId());
                campaignLog.setCreateTime(new Date());
                campaignLog.setAgentId(null);
                campaignLog.setTableName("SCENARIO_QUESTION");
                campaignLog.setColumnName(null);
                campaignLog.setPreValue(null);
                campaignLog.setPostValue(null);
                campaignLog.setDescription("Chinh sua cau hoi rieng le");
                campaignLog.setCampaignId(scenarioQuestion.getCampaignId());
                campaignLog.setCustomerId(scenarioQuestion.getScenarioQuestionId());
                campaignLogRepository.save(campaignLog);

                if (scenarioQuestionDTO.getLstAnswers().size() > 0) {
                    lstAnswers = scenarioQuestionDTO.getLstAnswers();

                    lstAnswers.forEach(item -> {
                        CampaignLog campaignLogAnswer = new CampaignLog();
                        campaignLogAnswer.setCompanySiteId(scenarioQuestion.getCompanySiteId());
                        campaignLogAnswer.setCreateTime(new Date());
                        campaignLogAnswer.setAgentId(null);
                        campaignLogAnswer.setTableName("SCENARIO_ANSWER");
                        campaignLogAnswer.setColumnName(null);
                        campaignLogAnswer.setPreValue(null);
                        campaignLogAnswer.setPostValue(null);
                        campaignLogAnswer.setCampaignId(scenarioQuestion.getCampaignId());

                        if (item.getScenarioAnswerId() != null) {
                            item.setCode(item.getCode().trim());
                            item.setAnswer(item.getAnswer().trim());
                            ScenarioAnswer answer = modelMapper.map(item, ScenarioAnswer.class);
                            scenarioAnswerRepository.save(answer);
                            campaignLogAnswer.setCustomerId(answer.getScenarioAnswerId());
                            campaignLogAnswer.setDescription("Chinh sua cau tra loi");
                            campaignLogRepository.save(campaignLogAnswer);
                        } else {
                            item.setCode(scenarioQuestion.getScenarioQuestionId() + "_" + item.getOrderIndex());
                            item.setScenarioQuestionId(scenarioQuestion.getScenarioQuestionId());
                            item.setCreateTime(new Date());
                            item.setStatus((short) 1);
                            item.setAnswer(item.getAnswer().trim());
                            ScenarioAnswer answer = modelMapper.map(item, ScenarioAnswer.class);
                            scenarioAnswerRepository.save(answer);
                            campaignLogAnswer.setCustomerId(answer.getScenarioAnswerId());
                            campaignLogAnswer.setDescription("Them moi cau tra loi");
                            campaignLogRepository.save(campaignLogAnswer);
                        }
                    });
                }

                //delete interacted record from  contact_quest_result by question_id
                contactQuestResultRepository.deleteByScenarioQuestionId(scenarioQuestion.getScenarioQuestionId());
            }

            resultDTO.setData(scenarioQuestion);
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public Integer countDuplicateOrderIndex(ScenarioQuestionDTO questionDTO) {
        try {
            return questionRepositoryCustom.countDuplicateOrderIndex(questionDTO);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

}
