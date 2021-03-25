package com.viettel.campaign.service.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.*;
import com.viettel.campaign.repository.ccms_full.*;
import com.viettel.campaign.service.CampaignService;
import com.viettel.campaign.utils.*;
import com.viettel.campaign.web.dto.*;
import com.viettel.campaign.web.dto.request_dto.CampaignRequestDTO;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

//import com.viettel.campaign.model.UserActionLog;
//import com.viettel.campaign.repository.UserActionLogRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class CampaignServiceImpl implements CampaignService {
    private static final Logger logger = LoggerFactory.getLogger(CampaignServiceImpl.class);

    public static final String CAMPAIGN_TYPE = "CAMPAIGN_TYPE";
    public static final String CAMPAIGN_STATUS = "CAMPAIGN_STATUS";

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    TimeZoneDialModeRepository timeZoneDialModeRepository;

    @Autowired
    TimeRangeDialModeRepository timeRangeDialModeRepository;

    @Autowired
    UserActionLogRepositoryCustom userActionLogRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ApParamRepository apParamRepository;

    @Autowired
    ScenarioRepository scenarioRepository;

    @Autowired
    CampaignCustomerListRepository campaignCustomerListRepository;

    @Autowired
    CampaignCustomerRepository campaignCustomerRepository;

    @Autowired
    CampaignCustomerListColumnRepository campaignCustomerListColumnRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CampaignCfgRepository cfgRepository;

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO search(CampaignRequestDTO requestDto) {
        return campaignRepository.search(requestDto);
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO findByCampaignCode(CampaignRequestDTO requestDTO) {
        ResultDTO result = new ResultDTO();

        if (DataUtil.isNullOrEmpty(requestDTO.getCompanySiteId())) {
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
            return result;
        }

        try {
            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            //result.setData(campaignRepository.findByCampaignCode(requestDTO));
            Page<Campaign> data = null;
            if (DataUtil.isNullOrEmpty(requestDTO.getCampaignCode())) {
                data = campaignRepository.findByCompanySiteIdAndStatusNotOrderByCreateTimeDesc(Long.parseLong(requestDTO.getCompanySiteId()), -1L, SQLBuilder.buildPageable(requestDTO));
            } else {
                data = campaignRepository.findByCompanySiteIdAndStatusNotAndCampaignCodeContainingOrderByCreateTimeDesc(Long.parseLong(requestDTO.getCompanySiteId()), -1L, requestDTO.getCampaignCode(), SQLBuilder.buildPageable(requestDTO));
            }
            result.setData(data);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public List<Campaign> findAllCondition(Long companySiteId) {
        return campaignRepository.findAllByCompanySiteId(companySiteId);
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public List<Campaign> findCampaignByCompanySiteIdAndStartTimeIsLessThanEqualAndStatusIn(Long siteId, Date startTime, List<Long> status) {
        return campaignRepository.findCampaignByCompanySiteIdAndStartTimeIsLessThanEqualAndStatusIn(siteId, startTime, status);
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public List<Campaign> findCampaignByCompanySiteIdAndEndTimeIsLessThanEqualAndStatusIn(Long siteId, Date endTime, List<Long> status) {
        return campaignRepository.findCampaignByCompanySiteIdAndEndTimeIsLessThanEqualAndStatusIn(siteId, endTime, status);
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public Campaign updateProcess(Campaign c) {
        return campaignRepository.save(c);
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, readOnly = true)
    public ResultDTO findByCampaignId(Long campaignId, Integer offSet) {
        ResultDTO result = new ResultDTO();
        Campaign campaign = campaignRepository.findByCampaignId(campaignId);

        if (campaign != null) {
            campaign.setStartTime(DateTimeUtil.addHoursToJavaUtilDate(campaign.getStartTime(), offSet / 60));
            campaign.setEndTime(DateTimeUtil.addHoursToJavaUtilDate(campaign.getEndTime(), offSet / 60));
            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            result.setData(campaign);
        } else {
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO addNewCampaign(CampaignDTO campaignDTO) throws ParseException {
        logger.info("=== Start add new  campaign:: " + campaignDTO.getCampaignName() + " | StartTime: " + campaignDTO.getStartTime() + " | EndTime: " + campaignDTO.getEndTime());
        ResultDTO resultDTO = new ResultDTO();
        campaignDTO.setCampaignName(campaignDTO.getCampaignName().trim());
        campaignDTO.setContent(campaignDTO.getContent().trim());
        //campaignDTO.setStartTime(TimeZoneUtils.changeTimeZone(campaignDTO.getStartTime(), 0L));
        //campaignDTO.setEndTime(TimeZoneUtils.changeTimeZone(campaignDTO.getEndTime(), 0L));
        String dtStartTime = TimeZoneUtils.convertDateToStringDate(campaignDTO.getStartTime()) + " 00:00:00";
        campaignDTO.setStartTime(TimeZoneUtils.changeTimeZone(TimeZoneUtils.convertStringToDate(dtStartTime, "dd/MM/yyyy HH:mm:ss", TimeZoneUtils.getZone(Config.timeZone)), 0l));
        String dtEndTime = TimeZoneUtils.convertDateToStringDate(campaignDTO.getEndTime()) + " 23:59:59";
        campaignDTO.setEndTime(TimeZoneUtils.changeTimeZone(TimeZoneUtils.convertStringToDate(dtEndTime, "dd/MM/yyyy HH:mm:ss", TimeZoneUtils.getZone(Config.timeZone)), 0l));
        logger.info("=== Change timezone:: " + campaignDTO.getCampaignCode() + " | StartTime: " + campaignDTO.getStartTime() + " | EndTime: " + campaignDTO.getEndTime());
        campaignDTO.setCreateTime(new Date());
        Campaign campaign = modelMapper.map(campaignDTO, Campaign.class);
        Long campaignId;
        List<TimeZoneDialModeDTO> timeZoneDialModes = new ArrayList<>();
        List<TimeRangeDialModeDTO> timeRangeDialModes = new ArrayList<>();
        List<TimeRangeDialMode> lstTimeRangeModeToInsert = new ArrayList<>();
        List<TimeZoneDialMode> lstTimeZoneModeToInser = new ArrayList<>();
        try {
            String campaignCode = generateCampaignCode(campaignDTO.getCampaignType(), campaignDTO.getChanel());
            campaign.setCampaignCode(campaignCode);
            Campaign campaignResult = campaignRepository.save(campaign);
            campaignId = campaign.getCampaignId();
            //insert sub tables

            if (campaignDTO.getLstTimeRange().size() > 0) {
                timeRangeDialModes = campaignDTO.getLstTimeRange();
                timeRangeDialModes.forEach(item -> {
                    item.setCampaignId(campaignId);
                    item.setStartTime(TimeZoneUtils.changeTimeZone(item.getStartTime(), 0L));
                    TimeRangeDialMode timeRangeDialMode = modelMapper.map(item, TimeRangeDialMode.class);
                    lstTimeRangeModeToInsert.add(timeRangeDialMode);
                });
                timeRangeDialModeRepository.saveAll(lstTimeRangeModeToInsert);
            }
            if (campaignDTO.getLstTimeZone().size() > 0) {
                timeZoneDialModes = campaignDTO.getLstTimeZone();
                timeZoneDialModes.forEach(item -> {
                    item.setCampaignId(campaignId);
                    TimeZoneDialMode timeZoneDialMode = modelMapper.map(item, TimeZoneDialMode.class);
                    lstTimeZoneModeToInser.add(timeZoneDialMode);
                });
                timeZoneDialModeRepository.saveAll(lstTimeZoneModeToInser);
            }

            Scenario scenario = new Scenario();
            scenario.setCampaignId(campaignId);
            scenario.setCode(campaignId + "_" + "01");
            scenario.setCreateTime(new Date());
            scenario.setCompanySiteId(campaign.getCompanySiteId());
            scenarioRepository.save(scenario);

            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setData(campaignResult);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        logger.info("=== End add new  campaign:: " + campaign.getCampaignId() + " | StartTime: " + campaignDTO.getStartTime() + " | EndTime: " + campaignDTO.getEndTime());
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO findCampaignById(Long campaignId) {
        logger.info("=== Start find campaign by id: " + campaignId);
        ResultDTO resultDTO = new ResultDTO();
        try {
            Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
            if (campaign != null) {
                resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
                resultDTO.setData(campaign);
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
    public ResultDTO changeCampaignStatus(CampaignDTO dto) {
        ResultDTO result = new ResultDTO();
        try {
            Campaign entity = campaignRepository.findCampaignByCampaignIdAndCompanySiteId(dto.getCampaignId(), dto.getCompanySiteId());
            if (entity != null) {
                if (entity.getStatus().equals(dto.getStatus().longValue())) {
                    result.setDescription("Duplicate status");
                    result.setErrorCode("02");
                    return result;
                }
                entity.setStatus(dto.getStatus().longValue());
                entity.setUpdateTime(new Date());
                entity.setUpdateBy(dto.getUpdateBy());
                campaignRepository.save(entity);
                UserActionLogDTO userActionLog = new UserActionLogDTO();
                userActionLog.setAgentId(null);
                userActionLog.setSessionId(dto.getSessionId());
                userActionLog.setCompanySiteId(dto.getCompanySiteId());
                userActionLog.setDescription(null);
                userActionLog.setStartTime(new Date());
                userActionLog.setEndTime(null);
                userActionLog.setObjectId(entity.getCampaignId());
                if (dto.getStatus().equals((short) -1)) {
                    userActionLog.setActionType((short) 2);
                } else if (dto.getStatus().equals((short) 1)) {
                    userActionLog.setActionType((short) 3);
                } else if (dto.getStatus().equals((short) 2)) {
                    userActionLog.setActionType((short) 5);
                } else if (dto.getStatus().equals((short) 3)) {
                    userActionLog.setActionType((short) 4);
                } else if (dto.getStatus().equals((short) 4)) {
                    userActionLog.setActionType((short) 7);
                }

                userActionLogRepository.insertToUserActionLog(userActionLog);
                result.setData(entity);
                result.setDescription(Constants.ApiErrorDesc.SUCCESS);
                result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            } else {
                result.setErrorCode(Constants.ApiErrorCode.ERROR);
                result.setDescription("Entity not found");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO checkAllowStatusToPrepare(Long campaignId) {
        return campaignRepository.checkAllowStatusToPrepare(campaignId);
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public XSSFWorkbook exportCampaigns(CampaignRequestDTO dto) {
        Locale locale = Locale.forLanguageTag(dto.getLanguage());
        ResultDTO resultDTO = campaignRepository.search(dto);
        List<CampaignDTO> listData = (List<CampaignDTO>) resultDTO.getData();
        List<ApParam> lstType = apParamRepository.findParamByParType(CAMPAIGN_TYPE);

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet;

        CellStyle styleTitle = WorkBookBuilder.buildDefaultStyleTitle(workbook);
        CellStyle styleRowHeader = WorkBookBuilder.buildDefaultStyleRowHeader(workbook);
        CellStyle styleRow = WorkBookBuilder.buildDefaultStyleRow(workbook);

        styleRowHeader.setWrapText(true);
        styleRow.setWrapText(true);
        // list header
        List<String> fileHeaderList = new ArrayList<>();
        fileHeaderList.add(BundleUtils.getLangString("stt", locale));
        fileHeaderList.add(BundleUtils.getLangString("campaign.code", locale));
        fileHeaderList.add(BundleUtils.getLangString("campaign.name", locale));
        fileHeaderList.add(BundleUtils.getLangString("campaign.type", locale));
        fileHeaderList.add(BundleUtils.getLangString("campaign.chanel", locale));
        fileHeaderList.add(BundleUtils.getLangString("campaign.startTime", locale));
        fileHeaderList.add(BundleUtils.getLangString("campaign.endTime", locale));
        fileHeaderList.add(BundleUtils.getLangString("campaign.cusNum", locale));
        fileHeaderList.add(BundleUtils.getLangString("campaign.joinedCusNum", locale));
        fileHeaderList.add(BundleUtils.getLangString("campaign.interactedCusNum", locale));
        fileHeaderList.add(BundleUtils.getLangString("campaign.notInteractedCusNum", locale));
        fileHeaderList.add(BundleUtils.getLangString("campaign.logCusNum", locale));
        fileHeaderList.add(BundleUtils.getLangString("campaign.status", locale));

        //
        String sheetName = BundleUtils.getLangString("campaign", locale);
        sheet = workbook.createSheet(sheetName);
        // Title
        String title = BundleUtils.getLangString("campaign.title", locale);
        int rowTitleStart = 3;
        Row rowTitle = sheet.createRow(rowTitleStart);
        rowTitle.setHeight((short) 800);

        WorkBookBuilder.writeCellContent(rowTitle, styleTitle, 3, title);
        sheet.addMergedRegion(new CellRangeAddress(rowTitleStart, rowTitleStart, 3, 8));

        // Header
        int startRowTable = 5;
        int count = 1;
        Row rowHeader = sheet.createRow(startRowTable);
        for (int i = 0; i < fileHeaderList.size(); i++) {
            sheet.setColumnWidth(i, 6500);
            WorkBookBuilder.writeCellContent(rowHeader, styleRowHeader, i, fileHeaderList.get(i));
        }
        // Content
        for (int i = 0, rowIndex = 1; i < listData.size(); i++) {
            Row row = sheet.createRow(startRowTable + count);
            CampaignDTO c = listData.get(i);
            int col = 0;
            WorkBookBuilder.writeCellContent(row, styleRow, col++, rowIndex);
            WorkBookBuilder.writeCellContent(row, styleRow, col++, c.getCampaignCode());
            WorkBookBuilder.writeCellContent(row, styleRow, col++, c.getCampaignName());
            WorkBookBuilder.writeCellContent(row, styleRow, col++, getCampaignTypeName(lstType, c.getCampaignType()));
            WorkBookBuilder.writeCellContent(row, styleRow, col++, DataUtil.isNullOrZero(c.getChanel()) ? "" : BundleUtils.getLangString("campaign.chanel." + c.getChanel(), locale));
            WorkBookBuilder.writeCellContent(row, styleRow, col++, DateTimeUtil.format("dd/MM/yyyy", c.getStartTime(), ""));
            WorkBookBuilder.writeCellContent(row, styleRow, col++, DateTimeUtil.format("dd/MM/yyyy", c.getEndTime(), ""));
            WorkBookBuilder.writeCellContent(row, styleRow, col++, DataUtil.isNullOrZero(c.getCustomerNumber()) ? 0 : c.getCustomerNumber());
            WorkBookBuilder.writeCellContent(row, styleRow, col++, DataUtil.isNullOrZero(c.getNumOfJoinedCus()) ? 0 : c.getNumOfJoinedCus());
            WorkBookBuilder.writeCellContent(row, styleRow, col++, DataUtil.isNullOrZero(c.getNumOfInteractedCus()) ? 0 : c.getNumOfInteractedCus());
            WorkBookBuilder.writeCellContent(row, styleRow, col++, DataUtil.isNullOrZero(c.getNumOfNotInteractedCus()) ? 0 : c.getNumOfNotInteractedCus());
            WorkBookBuilder.writeCellContent(row, styleRow, col++, DataUtil.isNullOrZero(c.getNumOfLockCus()) ? 0 : c.getNumOfLockCus());
            WorkBookBuilder.writeCellContent(row, styleRow, col++, c.getStatus() == null ? "" : BundleUtils.getLangString("campaign.status." + c.getStatus(), locale));
            ++rowIndex;
            ++count;
        }
        return workbook;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, readOnly = true)
    public List<TimeRangeDialMode> getCampaignTimeRangeMode(Long campaignId, Long companySiteId, Integer timeZoneOffset) {
        Integer tz = timeZoneOffset / 60;
        List<TimeRangeDialMode> lstDatas = new ArrayList<>();
        List<TimeRangeDialMode> lstRawDatas = timeRangeDialModeRepository.findTimeRangeDialModeByCampaignIdAndCompanySiteId(campaignId, companySiteId);
        if (lstRawDatas != null && lstRawDatas.size() > 0) {
            lstRawDatas.forEach(item -> {
                item.setStartTimeStr(DateTimeUtil.format("yyyy/MM/dd HH:mm:ss",
                        DateTimeUtil.addHoursToJavaUtilDate(item.getStartTime(), tz), null));
                lstDatas.add(item);
            });
        }
        return lstDatas;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, readOnly = true)
    public List<TimeZoneDialMode> getCampaignTimeZoneMode(Long campaignId, Long companySiteId) {
        return timeZoneDialModeRepository.findTimeZoneDialModeByCampaignIdAndCompanySiteId(campaignId, companySiteId);
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO renewCampaign(CampaignDTO campaignDTO) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            Campaign entity = campaignRepository.findCampaignByCampaignIdAndCompanySiteId(campaignDTO.getCampaignId(), campaignDTO.getCompanySiteId());
            if (entity != null) {
                entity.setStatus(campaignDTO.getStatus().longValue());
                entity.setUpdateTime(new Date());
                entity.setUpdateBy(campaignDTO.getUpdateBy());
                entity.setEndTime(TimeZoneUtils.changeTimeZone(campaignDTO.getEndTime(), 0L));

                //campaignRepository.save(entity);

                UserActionLogDTO userActionLog = new UserActionLogDTO();
                userActionLog.setAgentId(null);
                userActionLog.setSessionId(campaignDTO.getSessionId());
                userActionLog.setCompanySiteId(campaignDTO.getCompanySiteId());
                userActionLog.setDescription(null);
                userActionLog.setStartTime(new Date());
                userActionLog.setEndTime(null);
                userActionLog.setObjectId(entity.getCampaignId());
                userActionLog.setActionType((short) 6);
                //userActionLogRepository.insertToUserActionLog(userActionLog);
                resultDTO.setData(entity);
                resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
                resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
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
    public ResultDTO updateCampaign(CampaignDTO dto) throws ParseException {
        ResultDTO resultDTO = new ResultDTO();
        List<TimeZoneDialModeDTO> lstTimeZone = dto.getLstTimeZone();
        List<TimeRangeDialModeDTO> lstTimeRange = dto.getLstTimeRange();
        Campaign campaignEntity = campaignRepository.findByCampaignId(dto.getCampaignId());
        campaignEntity.setCampaignName(dto.getCampaignName());
        campaignEntity.setCampaignCode(dto.getCampaignCode());
        campaignEntity.setContent(dto.getContent());
        campaignEntity.setCustomerNumber(dto.getCustomerNumber());
        String dtStartTime = TimeZoneUtils.convertDateToStringDate(dto.getStartTime()) + " 00:00:00";
        campaignEntity.setStartTime(TimeZoneUtils.changeTimeZone(TimeZoneUtils.convertStringToDate(dtStartTime, "dd/MM/yyyy HH:mm:ss", TimeZoneUtils.getZone(Config.timeZone)), 0l));
        String dtEndTime = TimeZoneUtils.convertDateToStringDate(dto.getEndTime()) + " 23:59:59";
        campaignEntity.setEndTime(TimeZoneUtils.changeTimeZone(TimeZoneUtils.convertStringToDate(dtEndTime, "dd/MM/yyyy HH:mm:ss", TimeZoneUtils.getZone(Config.timeZone)), 0l));
        logger.info("updateCampaign: " + dtStartTime + " | " + dtEndTime + " => dtStartTime" + campaignEntity.getStartTime() + " dtEndTime" + campaignEntity.getEndTime());
        //campaignEntity.setStartTime(TimeZoneUtils.changeTimeZone(dto.getStartTime(), 0L));
        //campaignEntity.setEndTime(TimeZoneUtils.changeTimeZone(dto.getEndTime(), 0L));
        campaignEntity.setMaxRecall(dto.getMaxRecall());
        campaignEntity.setRecallType(dto.getRecallType());
        campaignEntity.setRecallDuration(dto.getRecallDuration());
        campaignEntity.setUpdateBy(dto.getUpdateBy());
        campaignEntity.setUpdateTime(dto.getUpdateTime());
        campaignEntity.setCampaignType(dto.getCampaignType());
        campaignEntity.setProcessStatus(null);
        campaignEntity.setDialMode(null);
        campaignEntity.setTimeRange(null);
        campaignEntity.setCurrentTimeMode(dto.getCurrentTimeMode());
        campaignEntity.setIsApplyCustLock(dto.getIsApplyCustLock());
        campaignEntity.setWrapupTimeConnect(dto.getWrapupTimeConnect());
        campaignEntity.setWrapupTimeDisconnect(dto.getWrapupTimeDisconnect());
        campaignEntity.setPreviewTime(dto.getPreviewTime());
        campaignEntity.setRateDial(dto.getRateDial());
        campaignEntity.setRateMiss(dto.getRateMiss());
        campaignEntity.setAvgTimeProcess(dto.getAvgTimeProcess());
        campaignEntity.setMusicList(dto.getMusicList());
        campaignEntity.setTimePlayMusic(dto.getTimePlayMusic());
        campaignEntity.setCampaignStart(dto.getCampaignStart());
        campaignEntity.setCampaignEnd(dto.getCampaignEnd());
        campaignEntity.setTimeWaitAgent(null);
        campaignEntity.setQuestIndex(null);
        try {
            // Save campaign to database
            campaignRepository.save(campaignEntity);
            // Save time dial
            timeRangeDialModeRepository.deleteAllByCampaignIdAndCompanySiteId(dto.getCampaignId(), dto.getCompanySiteId());
            timeZoneDialModeRepository.deleteAllByCampaignIdAndCompanySiteId(dto.getCampaignId(), dto.getCompanySiteId());
            for (TimeRangeDialModeDTO timeRangeDto : lstTimeRange) {
                timeRangeDto.setCampaignId(dto.getCampaignId());
                timeRangeDto.setStartTime(TimeZoneUtils.changeTimeZone(timeRangeDto.getStartTime(), 0L));
                TimeRangeDialMode timeRangeEntity = modelMapper.map(timeRangeDto, TimeRangeDialMode.class);
                timeRangeDialModeRepository.save(timeRangeEntity);
            }
            for (TimeZoneDialModeDTO timeZoneDto : lstTimeZone) {
                timeZoneDto.setCampaignId(dto.getCampaignId());
                TimeZoneDialMode timeZoneEntity = modelMapper.map(timeZoneDto, TimeZoneDialMode.class);
                timeZoneDialModeRepository.save(timeZoneEntity);
            }
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    // hungtt
    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO findCustomerListReallocation(CampaignRequestDTO dto) {
        return campaignRepository.findCustomerListReallocation(dto);
    }

    // hungtt
    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO reallocationCustomer(CampaignRequestDTO dto) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            List<CustomerCustomDTO> list = dto.getCustomerCustomDTOList();
            for (CustomerCustomDTO customerCustomDTO : list) {
                CampaignCustomer campaignCustomer = campaignCustomerRepository.findCampaignCustomerByCampaignCustomerId(customerCustomDTO.getCampaignCustomerId());
                campaignCustomer.setStatus((short) 0);
                campaignCustomer.setCallStatus(null);
                campaignCustomer.setRedistribute((short) 1);
                campaignCustomer.setRecallCount(null);
            }
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    // hungtt
    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getListFieldsNotShow(CampaignRequestDTO dto) {
        Locale locale = Locale.forLanguageTag(dto.getLanguage());
        Map<String, String> mapColumn = new HashMap<>();
        mapColumn = setMapData(mapColumn, locale);
        ResultDTO resultDTO = campaignRepository.getListFieldsNotShow(dto);
        List<FieldsToShowDTO> list = (List<FieldsToShowDTO>) resultDTO.getListData();
        for (FieldsToShowDTO fieldsToShowDTO : list) {
            if (fieldsToShowDTO.getIsFix()) {
                fieldsToShowDTO.setColumnTitle(mapColumn.get(fieldsToShowDTO.getColumnName()));
            } else {
                fieldsToShowDTO.setColumnTitle(fieldsToShowDTO.getColumnName());
            }
        }
        resultDTO.setListData(list);
        return resultDTO;
    }


    // hungtt
    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getListFieldsToShow(CampaignRequestDTO dto) {
        Locale locale = Locale.forLanguageTag(dto.getLanguage());
        Map<String, String> mapColumn = new HashMap<>();
        mapColumn = setMapData(mapColumn, locale);
        ResultDTO resultDTO = campaignRepository.getListFieldsToShow(dto);
        List<FieldsToShowDTO> list = (List<FieldsToShowDTO>) resultDTO.getListData();
        for (FieldsToShowDTO fieldsToShowDTO : list) {
            if (fieldsToShowDTO.getIsFix()) {
                fieldsToShowDTO.setColumnTitle(mapColumn.get(fieldsToShowDTO.getColumnName()));
            } else {
                fieldsToShowDTO.setColumnTitle(fieldsToShowDTO.getColumnName());
            }
        }
        resultDTO.setListData(list);
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCampaignCustomerList(CampaignRequestDTO dto) {
        return campaignRepository.getCampaignCustomerList(dto);
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCustomerList(CampaignRequestDTO dto) {
        return campaignRepository.getCustomerList(dto);
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCustomerChoosenList(CampaignRequestDTO dto) {
        return campaignRepository.getCustomerChoosenList(dto);
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO addCustomerListToCampaign(CampaignRequestDTO dto) {
        ResultDTO resultDTO = new ResultDTO();
        String[] lstCusListId = dto.getLstCustomerListId().split(",");
        try {
            for (String cusListId : lstCusListId) {
                CampaignCustomerList entity = new CampaignCustomerList();
                entity.setCampaignId(Long.parseLong(dto.getCampaignId()));
                entity.setCompanySiteId(Long.parseLong(dto.getCompanySiteId()));
                entity.setCustomerListId(Long.parseLong(cusListId));
                campaignCustomerListRepository.save(entity);
            }
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
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO deleteCustomerListFromCampaign(CampaignRequestDTO dto) {
        ResultDTO resultDTO = new ResultDTO();
        String[] lstCusListId = dto.getLstCustomerListId().split(",");
        long campaignId = Long.parseLong(dto.getCampaignId());
        long companySiteId = Long.parseLong(dto.getCompanySiteId());
        try {
            for (String cusListId : lstCusListId) {
                // Xoa danh sach khach hang khoi campaign_customerList
                campaignCustomerListRepository.deleteCampaignCustomerListByCampaignIdAndCustomerListIdAndCompanySiteId(campaignId, Long.parseLong(cusListId), companySiteId);
                // Thuc hien xoa cac khach hang chua lien lac tai bang campaign_customer
                List<CampaignCustomer> listCampaignCustomer = campaignCustomerRepository.findCustomerNoContact(campaignId, companySiteId, Long.parseLong(cusListId));
                if (listCampaignCustomer.size() > 0) {
                    for (CampaignCustomer entity : listCampaignCustomer) {
                        campaignCustomerRepository.delete(entity);
                    }
                }
                // Thuc hien update cac khach hang da lien lac
                List<CampaignCustomer> list = campaignCustomerRepository.findCustomerContacted(campaignId, companySiteId, Long.parseLong(cusListId));
                if (list.size() > 0) {
                    for (CampaignCustomer campaignCustomer : list) {
                        campaignCustomer.setInCampaignStatus((short) 0);
                        campaignCustomer.setCustomerListId(null);
                        campaignCustomerRepository.save(campaignCustomer);
                    }
                }
            }
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
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO saveFieldCustomer(CampaignRequestDTO dto) {
        ResultDTO resultDTO = new ResultDTO();
        List<FieldsToShowDTO> list = dto.getLstFiedCustomer();
        long campaignId = Long.parseLong(dto.getCampaignId());
        long companySiteId = Long.parseLong(dto.getCompanySiteId());
        try {
            List<CampaignCustomerListColumn> listColumns = campaignCustomerListColumnRepository.findByCampaignIdAndCompanySiteId(campaignId, companySiteId);

            // Them moi cac truong hien thi
            for (FieldsToShowDTO fieldsToShowDTO : list) {
                if (fieldsToShowDTO.getId() == null) {
                    CampaignCustomerListColumn entity = new CampaignCustomerListColumn();
                    entity.setCampaignId(campaignId);
                    entity.setCompanySiteId(companySiteId);
                    if (fieldsToShowDTO.getIsFix()) {
                        entity.setColumnName(fieldsToShowDTO.getColumnName());
                    } else {
                        entity.setCustomizeFieldId(fieldsToShowDTO.getCustomizeFieldId());
                        entity.setCustomizeFieldTitle(fieldsToShowDTO.getColumnTitle());
                    }
                    entity.setOrderIndex((long) (list.indexOf(fieldsToShowDTO) + 1));
                    campaignCustomerListColumnRepository.save(entity);
                }
            }

            if (listColumns != null && listColumns.size() > 0) {
                // Cap nhat cac truong da co san
                for (FieldsToShowDTO fieldsToShowDTO : list) {
                    if (fieldsToShowDTO.getId() != null) {
                        listColumns.removeIf(p -> p.getCampaignCusListColId().equals(fieldsToShowDTO.getId()));
                        CampaignCustomerListColumn entity = campaignCustomerListColumnRepository.findByCampaignCusListColId(fieldsToShowDTO.getId());
                        entity.setOrderIndex((long) (list.indexOf(fieldsToShowDTO) + 1));
                        campaignCustomerListColumnRepository.save(entity);
                    }
                }
                // Xoa cac truong khong con hien thi nua
                for (CampaignCustomerListColumn entity : listColumns) {
                    campaignCustomerListColumnRepository.delete(entity);
                }
            }
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
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCampaignCustomerInformation(CampaignRequestDTO dto) {
        ResultDTO resultDTO = new ResultDTO();

        try {
            resultDTO.setData(campaignRepository.getCampaignCustomerInformation(dto));
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
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCustomerListInformation(CampaignRequestDTO dto) {
        ResultDTO resultDTO = new ResultDTO();

        try {
            resultDTO.setListData(campaignRepository.getCustomerListInformation(dto));
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
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCountIndividualOnList(CampaignRequestDTO dto) {
        ResultDTO resultDTO = new ResultDTO();

        try {
            List<CampaignInformationDTO> list = campaignRepository.getCountIndividualOnList(dto);
            resultDTO.setListData(list);
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
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO saveCustomerCampaign(CampaignRequestDTO dto) {
        ResultDTO resultDTO = new ResultDTO();
        long campaignId = Long.parseLong(dto.getCampaignId());
        long companySiteId = Long.parseLong(dto.getCompanySiteId());
        List<CustomerListDTO> listCustomerDto = dto.getLstCustomerCampaign();
        try {
            // Thuc hien them giam khach hang
            for (CustomerListDTO customerListDTO : listCustomerDto) { // Duyet tung customerList
                if (customerListDTO.getTotalCusAddRemove() > 0) { // Them khach hang
                    if (customerListDTO.getTotalCusAddRemove() > (customerListDTO.getTotalCusActive() - customerListDTO.getTotalCusFilter())) {
                        resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                        resultDTO.setDescription("errActiveCust");
                        return resultDTO;
                    }
                    // Lay ra danh sach khach hang phu hop de them
                    List<Customer> listCustomerToAdd = customerRepository.findAllCutomerNotInCampagin(customerListDTO.getCustomerListId(), campaignId);
                    if (listCustomerToAdd == null || listCustomerToAdd.size() == 0) {
                        resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                        resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
                        return resultDTO;
                    }
                    int numOfCusInList = listCustomerToAdd.size(); // Số khách hàng còn lại của chiến dịch chưa đc add vào campaign hoặc đã add nhưng in_campaign_status = 0
                    for (int i = 0, j = 0; (i < customerListDTO.getTotalCusAddRemove() && j < numOfCusInList); j++, i++) {
                        CampaignCustomer tempEntity = campaignCustomerRepository.findCampaignCustomerByCampaignIdAndCompanySiteIdAndCustomerId(campaignId, companySiteId, listCustomerToAdd.get(j).getCustomerId());
                        if (tempEntity != null) { // Khach hang đã đc chèn vào campaign theo individual / hoặc in_campaign_status = 0
                            if (tempEntity.getInCampaignStatus() == 0) {
                                tempEntity.setInCampaignStatus((short) 1);
                                campaignCustomerRepository.save(tempEntity);
                                //i += 1;
                            }
                            continue;
                        } else {
                            CampaignCustomer campaignCustomerEntity = new CampaignCustomer();
                            campaignCustomerEntity.setCampaignId(campaignId);
                            campaignCustomerEntity.setCustomerId(listCustomerToAdd.get(j).getCustomerId());
                            campaignCustomerEntity.setStatus((short) 0);
                            campaignCustomerEntity.setRecallCount(0L);
                            campaignCustomerEntity.setCustomerListId(customerListDTO.getCustomerListId());
                            campaignCustomerEntity.setCompanySiteId(companySiteId);
                            campaignCustomerEntity.setInCampaignStatus((short) 1);
                            campaignCustomerRepository.save(campaignCustomerEntity);
                            //i += 1;
                        }
                    }
                } else if (customerListDTO.getTotalCusAddRemove() < 0) { // Loai bo khach hang
                    long custToDel = Math.abs(customerListDTO.getTotalCusAddRemove());
                    // Lay ra danh sach khach hang can loai bo
                    List<CampaignCustomer> listCustomerToDelete = campaignCustomerRepository.findListCustomerToDel(companySiteId, campaignId, customerListDTO.getCustomerListId());
                    if (listCustomerToDelete != null && listCustomerToDelete.size() > 0) {
                        for (int j = 0; j < custToDel; j++) {
                            if (listCustomerToDelete.get(j).getStatus() == 0) {
                                campaignCustomerRepository.delete(listCustomerToDelete.get(j));
                            } else {
                                listCustomerToDelete.get(j).setInCampaignStatus((short) 0);
                                listCustomerToDelete.get(j).setCustomerListId(null);
                                campaignCustomerRepository.save(listCustomerToDelete.get(j));
                            }
                        }
                    }
                }
            }
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
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getConnectStatus(Long companySiteId) {
        ResultDTO resultDTO = new ResultDTO();

        try {
            List<ApParamDTO> list = new ArrayList<>();
            //list = campaignRepository.getConnectStatus(companySiteId);
            List<CampaignCfg> cfgLst = cfgRepository.getConnectStatusWithoutValue("1", (short) 1, companySiteId);
            for (CampaignCfg item : cfgLst) {
                ApParamDTO ap = new ApParamDTO();
                ap.setApParamId(DataUtil.safeToLong(item.getCompleteValue()));
                ap.setParName(item.getCompleteName());
                list.add(ap);
            }
            resultDTO.setListData(list);
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return resultDTO;
    }

    // hungtt
    private Map<String, String> setMapData(Map<String, String> mapColumn, Locale locale) {
        mapColumn.put("CUSTOMER_ID", BundleUtils.getLangString("CUSTOMER_ID", locale));
        mapColumn.put("CODE", BundleUtils.getLangString("CODE", locale));
        mapColumn.put("NAME", BundleUtils.getLangString("NAME", locale));
        mapColumn.put("DESCRIPTION", BundleUtils.getLangString("DESCRIPTION", locale));
        mapColumn.put("COMPANY_NAME", BundleUtils.getLangString("COMPANY_NAME", locale));
        mapColumn.put("CUSTOMER_IMG", BundleUtils.getLangString("CUSTOMER_IMG", locale));
        mapColumn.put("CREATE_DATE", BundleUtils.getLangString("CREATE_DATE", locale));
        mapColumn.put("UPDATE_DATE", BundleUtils.getLangString("UPDATE_DATE", locale));
        mapColumn.put("STATUS", BundleUtils.getLangString("STATUS", locale));
        mapColumn.put("CREATE_BY", BundleUtils.getLangString("CREATE_BY", locale));
        mapColumn.put("UPDATE_BY", BundleUtils.getLangString("UPDATE_BY", locale));
        mapColumn.put("SITE_ID", BundleUtils.getLangString("SITE_ID", locale));
        mapColumn.put("GENDER", BundleUtils.getLangString("GENDER", locale));
        mapColumn.put("CURRENT_ADDRESS", BundleUtils.getLangString("CURRENT_ADDRESS", locale));
        mapColumn.put("PLACE_OF_BIRTH", BundleUtils.getLangString("PLACE_OF_BIRTH", locale));
        mapColumn.put("DATE_OF_BIRTH", BundleUtils.getLangString("DATE_OF_BIRTH", locale));
        mapColumn.put("MOBILE_NUMBER", BundleUtils.getLangString("MOBILE_NUMBER", locale));
        mapColumn.put("USERNAME", BundleUtils.getLangString("USERNAME", locale));
        mapColumn.put("AREA_CODE", BundleUtils.getLangString("AREA_CODE", locale));
        mapColumn.put("CALL_ALLOWED", BundleUtils.getLangString("CALL_ALLOWED", locale));
        mapColumn.put("EMAIL_ALLOWED", BundleUtils.getLangString("EMAIL_ALLOWED", locale));
        mapColumn.put("SMS_ALLOWED", BundleUtils.getLangString("SMS_ALLOWED", locale));
        mapColumn.put("IPCC_STATUS", BundleUtils.getLangString("IPCC_STATUS", locale));
        mapColumn.put("EMAIL", BundleUtils.getLangString("EMAIL", locale));
        mapColumn.put("CUSTOMER_TYPE", BundleUtils.getLangString("CUSTOMER_TYPE", locale));
        mapColumn.put("AVATAR_LINK", BundleUtils.getLangString("AVATAR_LINK", locale));
        return mapColumn;
    }

    private String generateCampaignCode(String campaignType, Short chanel) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String maxIndexStr = campaignRepository.getMaxCampaignIndex();
        if (maxIndexStr != null) {
            Long maxIndex = Long.valueOf(maxIndexStr) + 1;
            String result = campaignType + "_" + chanel + "_" + year + "_" + maxIndex.toString();
            return result;
        }
        return null;
    }

    public String getCampaignTypeName(List<ApParam> lstApParams, String type) {
        String name = "";
        if (!DataUtil.isNullOrEmpty(type)) {
            for (int i = 0; i < lstApParams.size(); i++) {
                if (type.equals(lstApParams.get(i).getParValue())) {
                    name = lstApParams.get(i).getParName();
                    break;
                }
            }
        }
        return name;
    }
}
