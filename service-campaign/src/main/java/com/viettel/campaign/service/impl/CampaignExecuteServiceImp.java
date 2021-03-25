package com.viettel.campaign.service.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.acd_full.Agents;
import com.viettel.campaign.model.ccms_full.*;
import com.viettel.campaign.repository.acd_full.AgentsRepository;
import com.viettel.campaign.repository.ccms_full.*;
import com.viettel.campaign.service.CampaignExecuteService;
import com.viettel.campaign.utils.*;
import com.viettel.campaign.web.dto.*;
import com.viettel.campaign.web.dto.request_dto.CampaignCustomerListColumnRequestDTO;
import com.viettel.campaign.web.dto.request_dto.CampaignRequestDTO;
import com.viettel.econtact.filter.UserSession;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.viettel.campaign.utils.Config.timeEditInteractionResult;

@Service
public class CampaignExecuteServiceImp implements CampaignExecuteService {

    private static final Logger logger = LoggerFactory.getLogger(CampaignExecuteServiceImp.class);
    private SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT.FOMART_DATE_TYPE_1);

    private Random rand = SecureRandom.getInstanceStrong();

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ReceiveCustLogRepository custLogRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    CampaignExecuteRepository campaignExecuteRepository;

    @Autowired
    CampaignCustomerRepository campaignCustomerRepository;

    @Autowired
    AgentsRepository agentsRepository;

    @Autowired
    CampaignAgentRepository campaignAgentRepository;

    @Autowired
    TimeZoneDialModeRepository zoneDialModeRepository;

    @Autowired
    TimeRangeDialModeRepository rangeDialModeRepository;

    @Autowired
    CampaignCustomerListColumnRepository campaignCustomerListColumnRepository;

    @Autowired
    ContactCustResultRepository ccrRepository;

    @Autowired
    ContactQuestResultRepository cqrRepository;

    @Autowired
    CampaignCfgRepository cfgRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AgentStatusStatRepository agentStatusStatRepository;

    @Autowired
    VSAUsersRepository vsaUsersRepository;

    @Autowired
    CustomerTimeRepository customerTimeRepository;

    @Autowired
    ApParamRepository apParamRepository;

    @Autowired
    CustomerContactRepository customerContactRepository;

    @Autowired
    ContactCustResultRepository contactCustResultRepository;

    @Autowired
    ReceiveCustLogRepositoryCustom receiveCustLogRepositoryCustom;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    ReceiveCustLogRepository receiveCustLogRepository;

    @Autowired
    CallInteractionOrtherLegRepositoryCustom callInteractionOrtherLegRepositoryCustom;


    public CampaignExecuteServiceImp() throws NoSuchAlgorithmException {
    }

    //<editor-fold: hungtt>
    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, readOnly = true)
    public ResultDTO getComboBoxStatus(String companySiteId, String completeType) {
        ResultDTO resultDTO = new ResultDTO();
        List<ApParamDTO> lst = new ArrayList<>();

        try {
            // List<ApParamDTO> lst = campaignExecuteRepository.getComboBoxStatus(companySiteId, completeType);
            // Lấy cả các trạng thái đã xóa trong bảng CAMPAIGN_COMPLETE_CODE
            List<CampaignCfg> cfgLst = cfgRepository.getAllCustomerStatusByType(Short.parseShort(completeType), Long.parseLong(companySiteId));
            for (CampaignCfg item : cfgLst) {
                ApParamDTO ap = new ApParamDTO();
                ap.setApParamId(DataUtil.safeToLong(item.getCompleteValue()));
                ap.setParName(item.getCompleteName());
                lst.add(ap);
            }
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setData(lst);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return resultDTO;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, readOnly = true)
    public ResultDTO getComboCampaignType(String companySiteId) {
        ResultDTO resultDTO = new ResultDTO();
        List<ApParamDTO> lst = new ArrayList<>();

        try {
            //List<ApParamDTO> lst = campaignExecuteRepository.getComboCampaignType(companySiteId);
            List<ApParam> apParams = apParamRepository.findBySiteIdAndParTypeAndIsDelete(Long.parseLong(companySiteId), "CAMPAIGN_TYPE", 0L);
            for (ApParam item : apParams) {
                ApParamDTO ap = new ApParamDTO();
                ap.setApParamId(Long.parseLong(item.getParValue()));
                ap.setParName(item.getParName());
                lst.add(ap);
            }
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setData(lst);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return resultDTO;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, readOnly = true)
    public ResultDTO searchInteractiveResult(CampaignRequestDTO dto) {
        logger.info("--- Request to searchInteractiveResult:: ");
        logger.info("Timezone Offset:: " + dto.getTimezoneOffset());
        ResultDTO resultDTO = new ResultDTO();

        try {
            Page<ContactCustResultDTO> list = campaignExecuteRepository.getInteractiveResult(dto, SQLBuilder.buildPageable(dto), false);
            resultDTO.setListData(list.getContent());
            resultDTO.setTotalRow(list.getTotalElements());
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        logger.info("--- End searchInteractiveResult:: " + resultDTO.getErrorCode());

        return resultDTO;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, readOnly = true)
    public XSSFWorkbook exportInteractiveResult(CampaignRequestDTO dto) {
        Locale locale = Locale.forLanguageTag(dto.getLanguage());
        List<ContactCustResultDTO> list = campaignExecuteRepository.getInteractiveResult(dto, Pageable.unpaged(), true).getContent();

        XSSFWorkbook workbook = new XSSFWorkbook();

        try {
            Sheet sheet;
            // create font style
            Font defaultFont = workbook.createFont();
            defaultFont.setFontHeightInPoints((short) 13);
            defaultFont.setFontName("Times New Roman");
            defaultFont.setColor(IndexedColors.BLACK.getIndex());

            Font titleFont = workbook.createFont();
            titleFont.setFontHeightInPoints((short) 18);
            titleFont.setFontName("Times New Roman");
            titleFont.setColor(IndexedColors.BLACK.getIndex());
            titleFont.setBold(true);

            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 13);
            headerFont.setFontName("Times New Roman");
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            headerFont.setBold(true);

            CellStyle styleTitle = workbook.createCellStyle();
            styleTitle.setFont(titleFont);
            styleTitle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle styleRowHeader = workbook.createCellStyle();
            styleRowHeader.setFont(headerFont);
            styleRowHeader.setAlignment(HorizontalAlignment.CENTER);
            styleRowHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            styleRowHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleRowHeader.setBorderRight(BorderStyle.THIN);
            styleRowHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
            styleRowHeader.setBorderBottom(BorderStyle.THIN);
            styleRowHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            styleRowHeader.setBorderLeft(BorderStyle.THIN);
            styleRowHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            styleRowHeader.setBorderTop(BorderStyle.THIN);
            styleRowHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
            styleRowHeader.setWrapText(true);

            CellStyle styleRow = workbook.createCellStyle();
            styleRow.setFont(defaultFont);
            styleRow.setWrapText(true);

            // list header
            List<String> headerList = new ArrayList<>();
            headerList.add(BundleUtils.getLangString("stt", locale));
            headerList.add(BundleUtils.getLangString("campaign.execute.interactive.campaignCode", locale));
            headerList.add(BundleUtils.getLangString("campaign.execute.interactive.campaignName", locale));
            headerList.add(BundleUtils.getLangString("campaign.execute.interactive.agentId", locale));
            headerList.add(BundleUtils.getLangString("campaign.execute.interactive.phoneNumber", locale));
            headerList.add(BundleUtils.getLangString("campaign.execute.interactive.customerName", locale));
            headerList.add(BundleUtils.getLangString("campaign.execute.interactive.createTime", locale));
            headerList.add(BundleUtils.getLangString("campaign.execute.interactive.contactStatus", locale));
            headerList.add(BundleUtils.getLangString("campaign.execute.interactive.surveyStatus", locale));
            headerList.add(BundleUtils.getLangString("campaign.execute.interactive.status", locale));
            headerList.add(BundleUtils.getLangString("campaign.execute.interactive.recordStatus", locale));
            headerList.add(BundleUtils.getLangString("campaign.execute.interactive.callTime", locale));

            //
            String sheetName = BundleUtils.getLangString("detail", locale);
            sheet = workbook.createSheet(sheetName);

            // Title
            String title = BundleUtils.getLangString("campaign.execute.interactive.title", locale);
            int rowTitleStart = 3;
            Row rowTitle = sheet.createRow(rowTitleStart);
            rowTitle.setHeight((short) 800);
            writeCellContent(rowTitle, styleTitle, 3, title);
            sheet.addMergedRegion(new CellRangeAddress(rowTitleStart, rowTitleStart, 3, 8));
            // Header
            int startRowTable = 5;
            int count = 1;
            Row rowHeader = sheet.createRow(startRowTable);
            for (int i = 0; i < headerList.size(); i++) {
                sheet.setColumnWidth(i, 6500);
                if (i == 0) sheet.setColumnWidth(i, 1800);
                writeCellContent(rowHeader, styleRowHeader, i, headerList.get(i));
            }
            // Content
            for (int i = 0, rowIndex = 1; i < list.size(); i++, rowIndex++) {
                Row row = sheet.createRow(startRowTable + count);
                int col = 0;
                writeCellContent(row, styleRow, col++, rowIndex);
                writeCellContent(row, styleRow, col++, list.get(i).getCampaignCode());
                writeCellContent(row, styleRow, col++, list.get(i).getCampaignName());
                writeCellContent(row, styleRow, col++, list.get(i).getUserName());
                writeCellContent(row, styleRow, col++, list.get(i).getPhoneNumber());
                writeCellContent(row, styleRow, col++, list.get(i).getCustomerName());
                writeCellContent(row, styleRow, col++, list.get(i).getStartCall() == null ? "" : DateTimeUtil.format("dd/MM/yyyy HH:mm:ss", list.get(i).getStartCall(), ""));
                //writeCellContent(row, styleRow, col++, list.get(i).getConnectStatus() == null ? "" : BundleUtils.getLangString("campaign.connect-status." + list.get(i).getConnectStatus(), locale));
                //writeCellContent(row, styleRow, col++, list.get(i).getSurveyStatus() == null ? "" : BundleUtils.getLangString("campaign.survey-status." + list.get(i).getSurveyStatus(), locale));
                writeCellContent(row, styleRow, col++, list.get(i).getConnectStatusName());
                writeCellContent(row, styleRow, col++, list.get(i).getSurveyStatusName());
                writeCellContent(row, styleRow, col++, list.get(i).getStatus() == null ? "" : BundleUtils.getLangString("campaign.status." + list.get(i).getStatus(), locale));
                writeCellContent(row, styleRow, col++, list.get(i).getRecordStatus() == null ? "" : BundleUtils.getLangString("campaign.recordStatus." + list.get(i).getRecordStatus(), locale));
                writeCellContent(row, styleRow, col, list.get(i).getCallTime());
                //++rowIndex;
                ++count;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return workbook;
    }

    private void writeCellContent(Row row, CellStyle rowStyle, int colNo, Object content) {
        Cell cell = row.createCell(colNo);
        if (content == null) {
            cell.setCellValue("");
        } else {
            cell.setCellValue(String.valueOf(content));
        }
        cell.setCellStyle(rowStyle);
    }
    //</editor-fold: hungtt>

    @Override
    @Transactional(value = DataSourceQualify.CHAINED, readOnly = true)
    public ResultDTO searchCampaignExecute(CampaignRequestDTO requestDto) {
        logger.info("---- Request to searchCampaignExecute:: ");
        ResultDTO result = new ResultDTO();
        Map data = new HashMap();
        String campaignSystemStatus = "";
        String campaignExecuting = "";
        String dialMode = "";
        data.put("availableAgent", "1");

        try {
            Page<CampaignDTO> dataPage = campaignExecuteRepository.searchCampaignExecute(requestDto, SQLBuilder.buildPageable(requestDto));

            if (dataPage.getTotalElements() > 0) {
                result.setTotalRow(dataPage.getTotalElements());
                //List<CampaignDTO> campaignList = campaignExecuteRepository.searchCampaignExecute(requestDto, SQLBuilder.buildPageable(requestDto));
                result.setListData(dataPage.getContent());

                Agents agents = agentsRepository.findByAgentId(requestDto.getAgentId());
                if (agents != null && "AVAILABLE".equalsIgnoreCase(agents.getCampaignSystemStatus())) {
                    campaignSystemStatus = "1";
                    for (CampaignDTO item : dataPage.getContent()) {
                        if (item.getStatus() == 2 && item.getAgentStatus() == 1) {
                            dialMode = getDialModeAtCurrent(DataUtil.safeToLong(requestDto.getCompanySiteId()), DataUtil.safeToLong(item.getCampaignId()),
                                    DataUtil.safeToInt(requestDto.getTimezoneOffset()));
                            campaignExecuting = item.getCampaignId().toString();
                            break;
                        }
                    }
                } else if (agents != null && "NOT AVAILABLE".equalsIgnoreCase(agents.getCampaignSystemStatus())) {
                    data.put("availableAgent", "0");
                }
            }
            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            data.put("campaignSystemStatus", campaignSystemStatus);
            data.put("campaignExecuting", campaignExecuting);
            data.put("dialModeCampaignExecuting", dialMode);
            result.setData(data);
//            else {
//                result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
//                result.setDescription(Constants.ApiErrorDesc.SUCCESS);
//                data.put("campaignSystemStatus", campaignSystemStatus);
//                data.put("campaignExecuting", campaignExecuting);
//                data.put("dialModeCampaignExecuting", dialMode);
//                result.setData(data);
//            }
            logger.info("---- End to searchCampaignExecute:: " + result.getErrorCode());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, readOnly = true)
    public ResultDTO checkExecuteCampaign(CampaignRequestDTO requestDto) {
        ResultDTO result = new ResultDTO();
        List<AgentStatusStat> ass = new ArrayList<>();

        try {
            String dialMode = getDialModeAtCurrent(DataUtil.safeToLong(requestDto.getCompanySiteId()), DataUtil.safeToLong(requestDto.getCampaignId()),
                    DataUtil.safeToInt(requestDto.getTimezoneOffset()));
            VSAUsers vsa = vsaUsersRepository.findByUserId(DataUtil.safeToLong(requestDto.getAgentId()));

            if (vsa != null) {
                ass = agentStatusStatRepository.getStatusByKzUserId(vsa.getUserKazooId());
            }

            if (ass != null && ass.size() > 0) {
                if ("logged_out".equalsIgnoreCase(ass.get(0).getCurrentStatus())) {
                    result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                    result.setDescription(Constants.ApiErrorDesc.SUCCESS);
                } else if ("campaign".equalsIgnoreCase(ass.get(0).getCurrentStatus())) {
                    result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                    result.setDescription(Constants.ApiErrorDesc.SUCCESS);
                } else {
                    result.setErrorCode("02");
                    result.setDescription("campaign-agents-err-callout");
                }
            }
            if ("-1".equalsIgnoreCase(dialMode)) {
                result.setErrorCode("03");
                result.setDescription("campaign-agents-err-dial-mode");
            } else {
                result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CHAINED, rollbackFor = Exception.class)
    public ResultDTO getExecuteCampaign(CampaignRequestDTO requestDto) {
        ResultDTO result = new ResultDTO();
        Map data = new HashMap();

        try {
            if (requestDto.getType() == 2) {
                List<CampaignAgent> ca = campaignAgentRepository.findByAgentIdAndStatus(Long.parseLong(requestDto.getAgentId()), 1);
                ca.forEach(c -> c.setStatus(0));
                campaignAgentRepository.saveAll(ca);
            }

            // update acd_full.agents table
            Agents agents = agentsRepository.findByAgentId(requestDto.getAgentId());
            agents.setAgentId(requestDto.getAgentId());
            agents.setCampaignSystemStatus("AVAILABLE");
            agents.setCurrentCampaignId(Long.parseLong(requestDto.getCampaignId()));
            agents.setUpdateDate(new Date());
            agentsRepository.save(agents);

            // update ccms_full.campaign_agent table
            CampaignAgent ca = campaignAgentRepository.findByCampaignIdAndAgentId(Long.parseLong(requestDto.getCampaignId()), Long.parseLong(requestDto.getAgentId()));
            ca.setStatus(1);
            campaignAgentRepository.save(ca);

            data.put("campaignExecuting", requestDto.getCampaignId());
            data.put("dialModeCampaignExecuting", getDialModeAtCurrent(DataUtil.safeToLong(requestDto.getCompanySiteId()),
                    DataUtil.safeToLong(requestDto.getCampaignId()), DataUtil.safeToInt(requestDto.getTimezoneOffset())));

            result.setData(data);
            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, readOnly = true)
    public ResultDTO getCustomer(CampaignCustomerDTO dto) {
        ResultDTO result = new ResultDTO();
        // String dialMode = getDialModeAtCurrent(dto.getCompanySiteId(), dto.getCampaignId());
        List<ApParam> apParam = apParamRepository.findAllParam("DUNG_SAI");
        List<CampaignCustomerDTO> lst = new ArrayList<>();

        // Khách hàng đến thời điểm hẹn gọi lại và là khách hàng mà chính NSD hẹn gọi lại
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT CC.CUSTOMER_ID AS customerId");
        sb.append(" FROM CAMPAIGN_CUSTOMER CC");
        sb.append(" WHERE CC.CAMPAIGN_ID = :campaignId");
        sb.append("  AND CC.COMPANY_SITE_ID = :companySiteId");
        sb.append("  AND CC.IN_CAMPAIGN_STATUS = 1");
        sb.append("  AND CC.CUSTOMER_ID NOT IN (SELECT CUSTOMER_ID FROM RECEIVE_CUST_LOG WHERE END_TIME IS NULL AND START_TIME >= SYSDATE - 10 AND START_TIME <= SYSDATE)");
        sb.append("  AND EXISTS(SELECT 1");
        sb.append("        FROM CAMPAIGN_COMPLETE_CODE CCC");
        sb.append("        WHERE CC.STATUS = CCC.COMPLETE_VALUE");
        sb.append("          AND STATUS = 1");
        sb.append("          AND IS_RECALL = 1");
        sb.append("          AND COMPLETE_TYPE = 2");
        sb.append("          AND COMPANY_SITE_ID = :companySiteId)");
        sb.append("  AND CC.RECALL_TIME <= :sysdate");
        sb.append("  AND CC.RECALL_TIME + NUMTODSINTERVAL(:dungSai, 'MINUTE') >= :sysdate");
        sb.append("  AND CC.AGENT_ID = :agentId");

        // Khách hàng đến thời điểm hẹn gọi lại và không là khách hàng mà NSD dùng hẹn gọi lại
        // nhưng TVV hẹn gọi lại không đăng nhập hoặc không thực hiện chiến dịch hiện tại
//        StringBuilder sb2 = new StringBuilder();
//        sb2.append(" SELECT CC.CUSTOMER_ID AS customerId");
//        sb2.append(" FROM CCMS_FULL.CAMPAIGN_CUSTOMER CC");
//        sb2.append("    LEFT JOIN ACD_FULL.AGENTS AG ON CC.AGENT_ID = AG.AGENT_ID AND CC.CAMPAIGN_ID = AG.CURRENT_CAMPAIGN_ID");
//        sb2.append(" WHERE CC.CAMPAIGN_ID = :campaignId");
//        sb2.append("  AND CC.COMPANY_SITE_ID = :companySiteId");
//        sb2.append("  AND CC.IN_CAMPAIGN_STATUS = 1");
//        sb2.append("  AND CC.CUSTOMER_ID NOT IN (SELECT CUSTOMER_ID FROM RECEIVE_CUST_LOG WHERE END_TIME IS NULL)");
//        sb2.append("  AND EXISTS(SELECT 1");
//        sb2.append("        FROM CCMS_FULL.CAMPAIGN_COMPLETE_CODE CCC");
//        sb2.append("        WHERE CC.STATUS = CCC.COMPLETE_VALUE");
//        sb2.append("          AND STATUS = 1");
//        sb2.append("          AND IS_RECALL = 1");
//        sb2.append("          AND COMPLETE_TYPE = 2");
//        sb2.append("          AND COMPANY_SITE_ID = :companySiteId)");
//        sb2.append("  AND CC.RECALL_TIME <= :sysdate");
//        sb2.append("  AND CC.RECALL_TIME + NUMTODSINTERVAL(:dungSai, 'MINUTE') >= :sysdate");
//        sb2.append("  AND (AG.CAMPAIGN_SYSTEM_STATUS IS NULL OR AG.CAMPAIGN_SYSTEM_STATUS <> 'AVAILABLE')");

        // Khách hàng là khách hàng hẹn gọi lại nhưng thời gian hẹn gọi lại đã quá thời gian hẹn gọi lại + dung sai
        StringBuilder sb3 = new StringBuilder();
        sb3.append(" SELECT CC.CUSTOMER_ID AS customerId");
        sb3.append(" FROM CAMPAIGN_CUSTOMER CC");
        sb3.append(" WHERE CC.CAMPAIGN_ID = :campaignId");
        sb3.append("  AND CC.COMPANY_SITE_ID = :companySiteId");
        sb3.append("  AND CC.IN_CAMPAIGN_STATUS = 1");
        sb3.append("  AND CC.CUSTOMER_ID NOT IN (SELECT CUSTOMER_ID FROM RECEIVE_CUST_LOG WHERE END_TIME IS NULL AND START_TIME >= SYSDATE - 10 AND START_TIME <= SYSDATE)");
        sb3.append("  AND EXISTS(SELECT 1");
        sb3.append("        FROM CAMPAIGN_COMPLETE_CODE CCC");
        sb3.append("        WHERE CC.STATUS = CCC.COMPLETE_VALUE");
        sb3.append("          AND STATUS = 1");
        sb3.append("          AND IS_RECALL = 1");
        sb3.append("          AND COMPLETE_TYPE = 2");
        sb3.append("          AND COMPANY_SITE_ID = :companySiteId)");
        sb3.append("  AND CC.RECALL_TIME + NUMTODSINTERVAL(:dungSai, 'MINUTE') <= :sysdate");

        // Khách hàng mới
        StringBuilder sb4 = new StringBuilder();
        sb4.append(" SELECT CC.CUSTOMER_ID AS customerId ");
        sb4.append(" FROM CAMPAIGN_CUSTOMER CC");
        sb4.append("    INNER JOIN CUSTOMER C ON CC.CUSTOMER_ID = C.CUSTOMER_ID");
        sb4.append(" WHERE CC.CAMPAIGN_ID = :campaignId");
        sb4.append("  AND CC.COMPANY_SITE_ID = :companySiteId");
        sb4.append("  AND CC.IN_CAMPAIGN_STATUS = 1");
        sb4.append("  AND CC.CUSTOMER_ID NOT IN (SELECT CUSTOMER_ID FROM RECEIVE_CUST_LOG WHERE END_TIME IS NULL AND START_TIME >= SYSDATE - 10 AND START_TIME <= SYSDATE)");
        sb4.append("  AND CC.STATUS = 0");
        sb4.append("  AND C.STATUS = 1");
        sb4.append("  AND C.CALL_ALLOWED = 1");
        sb4.append("  AND C.IPCC_STATUS = 'active'");

        // Khách hàng không liên lạc được nhưng chưa phải là cuộc gọi kết thúc
        // và có số lần đã gọi < Số lần gọi tối đa được cấu hình và khoảng cách giữa 2 lần gọi < thời gian tương tác giữa 2 lần gọi được cấu hình
        StringBuilder sb5 = new StringBuilder();
        sb5.append(" SELECT CC.CUSTOMER_ID AS customerId");
        sb5.append(" FROM CAMPAIGN_CUSTOMER CC");
        sb5.append("    INNER JOIN CAMPAIGN CA ON CC.CAMPAIGN_ID = CA.CAMPAIGN_ID");
        sb5.append("    INNER JOIN CUSTOMER CU ON CC.CUSTOMER_ID = CU.CUSTOMER_ID");
        sb5.append(" WHERE CC.CAMPAIGN_ID = :campaignId");
        sb5.append("  AND CC.COMPANY_SITE_ID = :companySiteId");
        sb5.append("  AND CC.IN_CAMPAIGN_STATUS = 1");
        sb5.append("  AND CC.CUSTOMER_ID NOT IN (SELECT CUSTOMER_ID FROM RECEIVE_CUST_LOG WHERE END_TIME IS NULL)");
        sb5.append("  AND EXISTS(SELECT 1");
        sb5.append("        FROM CAMPAIGN_COMPLETE_CODE CCC");
        sb5.append("        WHERE CC.STATUS = CCC.COMPLETE_VALUE");
        sb5.append("          AND STATUS = 1");
        sb5.append("          AND IS_FINISH = 0");
        sb5.append("          AND COMPLETE_TYPE = 1");
        sb5.append("          AND COMPANY_SITE_ID = :companySiteId)");
        sb5.append("  AND CC.RECALL_COUNT < CA.MAX_RECALL");
        sb5.append("  AND ((CA.RECALL_TYPE = 2 AND TRUNC(24*(SYSDATE - CC.CALL_TIME)) >= CA.RECALL_DURATION) " +
                "OR (CA.RECALL_TYPE = 1 AND TRUNC(SYSDATE - CC.CALL_TIME) >= CA.RECALL_DURATION)) ");
        sb5.append("  AND CU.STATUS = 1");
        sb5.append("  AND CU.CALL_ALLOWED = 1");
        sb5.append("  AND CU.IPCC_STATUS = 'active'");

        String getExecuteCus1Str = sb.toString();
//        String getExecuteCus2Str = sb2.toString();
        String getExecuteCus3Str = sb3.toString();
        String getExecuteCus4Str = sb4.toString();
        String getExecuteCus5Str = sb5.toString();
        String type = "";

        try {
            List<CampaignCustomerDTO> lst1 = campaignCustomerRepository.getDataCampaignCustomer(dto, getExecuteCus1Str, apParam.get(0).getParValue());
            if (lst1.size() > 0) {
                lst = lst1;
                type = "1";
            } else {
                List<CampaignCustomerDTO> lst3 = campaignCustomerRepository.getDataCampaignCustomer(dto, getExecuteCus3Str, apParam.get(0).getParValue());
                if (lst3.size() > 0) {
                    lst = lst3;
                    type = "3";
                } else {
                    List<CampaignCustomerDTO> lst4 = campaignCustomerRepository.getDataCampaignCustomer(dto, getExecuteCus4Str, apParam.get(0).getParValue());
                    if (lst4.size() > 0) {
                        lst = lst4;
                        type = "4";
                    } else {
                        List<CampaignCustomerDTO> lst5 = campaignCustomerRepository.getDataCampaignCustomer(dto, getExecuteCus5Str, apParam.get(0).getParValue());
                        lst = lst5;
                        type = "5";
                    }
                }
//                List<CampaignCustomerDTO> lst2 = campaignCustomerRepository.getDataCampaignCustomer(dto, getExecuteCus2Str, apParam.get(0).getParValue());
//                if (lst2.size() > 0) {
//                    lst = lst2;
//                    type = "2";
//                } else {
//                    List<CampaignCustomerDTO> lst3 = campaignCustomerRepository.getDataCampaignCustomer(dto, getExecuteCus3Str, apParam.get(0).getParValue());
//                    if (lst3.size() > 0) {
//                        lst = lst3;
//                        type = "3";
//                    } else {
//                        List<CampaignCustomerDTO> lst4 = campaignCustomerRepository.getDataCampaignCustomer(dto, getExecuteCus4Str, apParam.get(0).getParValue());
//                        if (lst4.size() > 0) {
//                            lst = lst4;
//                            type = "4";
//                        } else {
//                            List<CampaignCustomerDTO> lst5 = campaignCustomerRepository.getDataCampaignCustomer(dto, getExecuteCus5Str, apParam.get(0).getParValue());
//                            lst = lst5;
//                            type = "5";
//                        }
//                    }
//                }
            }

            if (lst != null) {
                result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                result.setDescription(Constants.ApiErrorDesc.SUCCESS);
                result.setListData(lst);
                result.setData(type);
            } else {
                result.setErrorCode(Constants.ApiErrorCode.ERROR);
                result.setDescription(Constants.ApiErrorDesc.ERROR);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CHAINED, rollbackFor = Exception.class)
    public ResultDTO getCustomerComplete(ReceiveCustLogDTO dto) {
        ResultDTO result = new ResultDTO();

        try {
            agentsRepository.updateAgentLogoutFromCampaign(dto.getAgentId().toString(), "NOT AVAILABLE");
            ReceiveCustLog rclResult = custLogRepository.save(modelMapper.map(dto, ReceiveCustLog.class));

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            result.setData(rclResult);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    public ResultDTO getCallLog(ReceiveCustLogDTO dto) {
        ResultDTO result = new ResultDTO();

        List<CampaignCustomer> lst = null;//campaignCustomerRepository.getCustomerRecallDate(dto.getCampaignId(), dto.getAgentId(), dto.getCompanySiteId(), );

        result.setErrorCode(Constants.ApiErrorCode.ERROR);
        result.setDescription(Constants.ApiErrorDesc.ERROR);

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CHAINED, rollbackFor = Exception.class)
    public ResultDTO getAgentLogout(CampaignRequestDTO dto) {
        ResultDTO result = new ResultDTO();

        try {
            agentsRepository.updateAgentLogoutFromCampaign(dto.getAgentId(), "LOGOUT");
            // update ccms_full.campaign_agent table
            if (dto.getCampaignId() != null) {
                CampaignAgent ca = campaignAgentRepository.findByCampaignIdAndAgentId(Long.parseLong(dto.getCampaignId()), Long.parseLong(dto.getAgentId()));
                if (ca != null) {
                    ca.setStatus(0);
                    campaignAgentRepository.save(ca);
                }
            } else {
                List<CampaignAgent> ca = campaignAgentRepository.findByAgentId(Long.parseLong(dto.getAgentId()));
                for (CampaignAgent item : ca) {
                    item.setStatus(0);
                }
                campaignAgentRepository.saveAll(ca);
            }

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // forced rollback
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CHAINED, rollbackFor = Exception.class)
    public ResultDTO getLogoutContactResult(ReceiveCustLogDTO dto) {
        ResultDTO result = new ResultDTO();

        try {
            agentsRepository.updateAgentLogoutFromCampaign(dto.getAgentId().toString(), "AVAILABLE");
            // update receive customer log
            ReceiveCustLog rcl = custLogRepository.getByReceiveCustLogId(dto.getReceiveCustLogId());
            if (dto.isUpdateEndTime()) {
                rcl.setEndTime(new Date());
                custLogRepository.save(rcl);
            }

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO deleteContactCustResult(ContactCustResultDTO dto) {
        ResultDTO result = new ResultDTO();
        ContactCustResult ccr = new ContactCustResult();

        try {
            ccrRepository.deleteById(dto.getContactCustResultId());

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            result.setData(ccr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO updateContactCustResultAtCall(ContactCustResultDTO dto, UserSession userSession) {
        ResultDTO result = new ResultDTO();
        ContactCustResult ccr = new ContactCustResult();
        logger.info("--- Request to updateContactCustResultAtCall:: ");
        try {
            //Update các bản ghi cùng Receive_cust_log_id về status = 0
            result = updateCCRToStatus0(dto.getReceiveCustLogId(), userSession);

            ccr.setCompanySiteId(dto.getCompanySiteId());
            ccr.setContactStatus((short) 2);
            ccr.setStatus((short) 2);
            ccr.setCreateTime(new Date());
            ccr.setStartCall(new Date());
            ccr.setAgentId(dto.getAgentId());
            ccr.setCampaignId(dto.getCampaignId());
            ccr.setCustomerId(dto.getCustomerId());
            ccr.setReceiveCustLogId(dto.getReceiveCustLogId());
            ccr.setPhoneNumber(dto.getPhoneNumber());
            ccr.setReceiveTime(new Date());
            ccr.setIsFinalRecall((short) 1);
            ccr.setDialMode("MANUAL");

            ccr.setCallId(dto.getCallId());
            ccr.setUpdateBy(userSession.getUserId());
            ccr.setUpdateTime(new Date());

            ccr = ccrRepository.save(ccr);

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            result.setData(ccr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO updateContactCustResultAtRinging(ContactCustResultDTO dto, UserSession userSession) {
        ResultDTO result = new ResultDTO();
        ContactCustResult ccr;

        try {
            ccr = ccrRepository.findByContactCustResultId(dto.getContactCustResultId());

            ccr.setCallId(dto.getCallId());
            ccr.setUpdateBy(userSession.getUserId());
            ccr.setUpdateTime(new Date());
            ccr = ccrRepository.save(ccr);

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            result.setData(ccr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CHAINED, rollbackFor = Exception.class)
    public ResultDTO updateContactCustResultAtEnded(ContactCustResultDTO dto, UserSession userSession) {
        ResultDTO result = new ResultDTO();
        ContactCustResult ccr = new ContactCustResult();
        logger.info("--- Request to updateContactCustResultAtEnded:: ");
        try {
            if (dto.getContactCustResultId() != null) {
                ccr = ccrRepository.findByContactCustResultId(dto.getContactCustResultId());
            } else {
                //Update các bản ghi cùng Receive_cust_log_id về status = 0
                result = updateCCRToStatus0(dto.getReceiveCustLogId(), userSession);

                ccr.setCompanySiteId(dto.getCompanySiteId());
                ccr.setContactStatus((short) 2);
                ccr.setStatus((short) 2);
                ccr.setCreateTime(new Date());
                ccr.setStartCall(new Date());
                ccr.setAgentId(dto.getAgentId());
                ccr.setCampaignId(dto.getCampaignId());
                ccr.setCustomerId(dto.getCustomerId());
                ccr.setReceiveCustLogId(dto.getReceiveCustLogId());
                ccr.setPhoneNumber(dto.getPhoneNumber());
                ccr.setIsFinalRecall((short) 1);
                ccr.setDialMode("MANUAL");
            }

            // update contact customer result
            ccr.setDurationCall(dto.getDurationCall());
            //Lấy lại duration cho các TH kết nối thành công nhưng duration = 0
            if (dto.getDurationCall() != null && dto.getContactStatus() != null && dto.getCallId() != null
                    && dto.getDurationCall() == 0 && dto.getContactStatus() == 1) {
                List<CallInteractionOtherLeg> lstCI = callInteractionOrtherLegRepositoryCustom.getCallInteractionByCallID(dto.getCallId());
                if (lstCI != null && lstCI.size() > 0) {
                    ccr.setDurationCall(lstCI.get(0).getAnswerDuration());
                }
            }
            if (dto.getStartCallL() != null && dto.getStartCallL() > 0) {
                ccr.setStartCall(TimeZoneUtils.changeTimeZone(new Date(dto.getStartCallL() * 1000L), 0L));
            }
            ccr.setReceiveTime(DateTimeUtil.parseDate("dd/MM/yyyy HH:mm:ss", TimeZoneUtils.toDateStringWithTimeZone(dto.getReceiveTime(), TimeZone.getTimeZone("UTC"))));
            ccr.setPreEndTime(ccrRepository.getMaxCreateTime(dto.getCampaignId(), dto.getAgentId()));
            ccr.setEndTime(dto.getEndTimeL() == null ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getEndTimeL() * 1000L), 0L));
            ccr.setWaitTime(dto.getWaitTime());
            ccr.setCallId(dto.getCallId());
            ccr.setContactStatus(dto.getContactStatus());
            ccr.setCallStatus(dto.getCallStatus());
            ccr.setUpdateBy(userSession.getUserId());
            ccr.setUpdateTime(new Date());
            ccr = ccrRepository.save(ccr);
            // update campaign customer
            CampaignCustomer cc = campaignCustomerRepository.findCampaignCustomersByCampaignIdAndCustomerId(dto.getCampaignId(), dto.getCustomerId());
//            cc.setCallTime(dto.getStartCallL() == null ? null : DateTimeUtil.parseDate("dd/MM/yyyy HH:mm:ss", TimeZoneUtils.toDateStringWithTimeZone(new Date(dto.getStartCallL() * 1000L), tzClient)));
            cc.setCallTime(dto.getStartCallL() == null ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getStartCallL() * 1000L), 0L));
            cc.setStatus((dto.getDurationCall() != null && dto.getDurationCall() > 0) ? (dto.getCallStatus() != null ? dto.getCallStatus().shortValue() : (short) 3) : (short) 2);
            campaignCustomerRepository.save(cc);
            // update agents
            Agents ag = agentsRepository.findByAgentId(dto.getAgentId().toString());
            ag.setSystemStatus("AVAILABLE");
            agentsRepository.save(ag);

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            result.setData(ccr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ResultDTO updateContactCustResultAtSave(ContactCustResultDTO dto, UserSession userSession) {
        ResultDTO result = new ResultDTO();
        ContactCustResult ccr;

        try {
            // update old contact customer result
            ContactCustResult ccrOld = ccrRepository.findByContactCustResultId(dto.getOldContactCustResultId());
            ccrOld.setStatus(Short.parseShort("0"));
            ccrOld.setUpdateBy(userSession.getUserId());
            ccrOld.setUpdateTime(new Date());
            ccrRepository.save(ccrOld);

            //Update các bản ghi cùng Receive_cust_log_id về status = 0
            result = updateCCRToStatus0(dto.getReceiveCustLogId(), userSession);

            // insert new contact customer result
            ContactCustResult ccrNew = new ContactCustResult();
            ccrNew.setCompanySiteId(dto.getCompanySiteId());
            ccrNew.setCallStatus(dto.getCallStatus());
            ccrNew.setContactStatus(dto.getContactStatus());
            ccrNew.setStatus((short) 1);
            ccrNew.setDescription(dto.getDescription());
            ccrNew.setAgentId(dto.getAgentId());
            ccrNew.setCreateTime(new Date());
            ccrNew.setOldContactCustResultId(dto.getOldContactCustResultId());
            ccrNew.setCampaignId(dto.getCampaignId());
            ccrNew.setCustomerId(dto.getCustomerId());
            ccrNew.setDurationCall(dto.getDurationCall());
            ccrNew.setStartCall(dto.getStartCallL() == null || dto.getStartCallL() == 0 ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getStartCallL() * 1000L), 0L));
            ccrNew.setCallId(dto.getCallId());
            ccrNew.setEndTime(dto.getEndTimeL() == null ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getEndTimeL() * 1000L), 0L));
            ccrNew.setWaitTime(dto.getWaitTime());
            ccrNew.setReceiveCustLogId(dto.getReceiveCustLogId());
            ccrNew.setPhoneNumber(dto.getPhoneNumber());
            ccrNew.setReceiveTime(dto.getReceiveTime() == null ? null : TimeZoneUtils.changeTimeZone(dto.getReceiveTime(), 0L));
            ccrNew.setRecallTime(dto.getRecallTime() == null ? null : TimeZoneUtils.changeTimeZone(dto.getRecallTime(), 0L));
            ccrNew.setIsFinalRecall((short) 1);
            ccrNew.setIsSendEmail((short) 0);
            ccrNew.setDialMode("MANUAL");
            ccr = ccrRepository.save(ccrNew);

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            result.setData(ccr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO updateContactCustResultAtTen(ContactCustResultDTO dto, UserSession userSession) {
        ResultDTO result = new ResultDTO();
        logger.info("--- Request to updateContactCustResultAtTen:: ");
        try {
            //Update các bản ghi cùng Receive_cust_log_id về status = 0
            result = updateCCRToStatus0(dto.getReceiveCustLogId(), userSession);

            ContactCustResult ccr = new ContactCustResult();

            ccr.setCompanySiteId(dto.getCompanySiteId());
            ccr.setCallStatus(dto.getCallStatus());
            ccr.setContactStatus(dto.getContactStatus());
            ccr.setStatus((short) 2);
            ccr.setDescription(dto.getDescription());
            ccr.setCreateTime(new Date());
            ccr.setAgentId(dto.getAgentId());
            ccr.setCampaignId(dto.getCampaignId());
            ccr.setUpdateBy(userSession.getUserId());
            ccr.setCustomerId(dto.getCustomerId());
            ccr.setDurationCall(dto.getDurationCall());
            ccr.setStartCall(dto.getStartCallL() == null || dto.getStartCallL() == 0 ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getStartCallL() * 1000L), 0L));
            ccr.setReceiveCustLogId(dto.getReceiveCustLogId());
            ccr.setCallId(dto.getCallId());
            ccr.setPhoneNumber(dto.getPhoneNumber());
            ccr.setReceiveTime(dto.getReceiveTime() == null ? null : TimeZoneUtils.changeTimeZone(dto.getReceiveTime(), 0L));
            ccr.setPreEndTime(ccrRepository.getMaxCreateTime(dto.getCampaignId(), dto.getAgentId()));
            ccr.setRecallTime(dto.getRecallTime() == null ? null : TimeZoneUtils.changeTimeZone(dto.getRecallTime(), 0L));
            ccr.setIsFinalRecall((short) 1);
            ccr.setIsSendEmail((short) 0);
            ccr.setEndTime(dto.getEndTimeL() == null ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getEndTimeL() * 1000L), 0L));
            ccr.setWaitTime(dto.getWaitTime());
            ccr.setDialMode("MANUAL");
            ccr = ccrRepository.save(ccr);

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            result.setData(ccr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    public ResultDTO updateContactCustResult(ContactCustResultDTO dto, UserSession userSession) {
        ResultDTO result = new ResultDTO();
        ContactCustResult ccr = new ContactCustResult();
        TimeZone tzClient = TimeZoneUtils.getZoneMinutes(dto.getTimezone());

        try {
            if ("call".equalsIgnoreCase(dto.getEventCall())) {
                ccr.setCompanySiteId(dto.getCompanySiteId());
                ccr.setContactStatus((short) 2);
                ccr.setStatus((short) 2);
                ccr.setCreateTime(new Date());
                ccr.setAgentId(dto.getAgentId());
                ccr.setCampaignId(dto.getCampaignId());
                ccr.setCustomerId(dto.getCustomerId());
                ccr.setReceiveCustLogId(dto.getReceiveCustLogId());
                ccr.setPhoneNumber(dto.getPhoneNumber());
                ccr.setReceiveTime(new Date());
                ccr.setIsFinalRecall((short) 1);
                ccr.setDialMode("MANUAL");
                ccr = ccrRepository.save(ccr);
            }

            if (dto.getContactCustResultId() != null) {
                ccr = ccrRepository.findByContactCustResultId(dto.getContactCustResultId());

                if ("ringing".equalsIgnoreCase(dto.getEventCall())) {
                    ccr.setCallId(dto.getCallId());
                    ccr.setUpdateBy(userSession.getUserId());
                    ccr.setUpdateTime(new Date());
                    ccr = ccrRepository.save(ccr);
                }

                if ("ended".equalsIgnoreCase(dto.getEventCall())) {
                    // update contact customer result
                    ccr.setDurationCall(dto.getDurationCall());
                    ccr.setStartCall(dto.getStartCallL() == null || dto.getStartCallL() == 0 ? null : DateTimeUtil.parseDate("dd/MM/yyyy HH:mm:ss", TimeZoneUtils.toDateStringWithTimeZone(new Date(dto.getStartCallL() * 1000L), tzClient)));
                    ccr.setReceiveTime(DateTimeUtil.parseDate("dd/MM/yyyy HH:mm:ss", TimeZoneUtils.toDateStringWithTimeZone(dto.getReceiveTime(), tzClient)));
                    ccr.setPreEndTime(ccrRepository.getMaxCreateTime(dto.getCampaignId(), dto.getAgentId()));
                    ccr.setEndTime(dto.getEndTimeL() == null ? null : DateTimeUtil.parseDate("dd/MM/yyyy HH:mm:ss", TimeZoneUtils.toDateStringWithTimeZone(new Date(dto.getEndTimeL() * 1000L), tzClient)));
                    ccr.setWaitTime(dto.getWaitTime());
                    ccr.setCallId(dto.getCallId());
                    ccr.setUpdateBy(userSession.getUserId());
                    ccr.setUpdateTime(new Date());
                    ccr = ccrRepository.save(ccr);
                    // update campaign customer
                    CampaignCustomer cc = campaignCustomerRepository.findCampaignCustomersByCampaignIdAndCustomerId(dto.getCampaignId(), dto.getCustomerId());
                    cc.setCallTime(dto.getStartCallL() == null ? null : DateTimeUtil.parseDate("dd/MM/yyyy HH:mm:ss", TimeZoneUtils.toDateStringWithTimeZone(new Date(dto.getStartCallL() * 1000L), tzClient)));
                    campaignCustomerRepository.save(cc);
                    // update agents
                    Agents ag = agentsRepository.findByAgentId(dto.getAgentId().toString());
                    ag.setSystemStatus("AVAILABLE");
                    agentsRepository.save(ag);
                }
            }

            if ("save".equalsIgnoreCase(dto.getEventCall())) {
                // update old contact customer result
                ContactCustResult ccrOld;
                ccrOld = ccrRepository.findByContactCustResultId(dto.getOldContactCustResultId());
                ccrOld.setStatus(Short.parseShort("0"));
                ccrOld.setUpdateBy(userSession.getUserId());
                ccrOld.setUpdateTime(new Date());
                ccrRepository.save(ccrOld);

                //Update các bản ghi cùng Receive_cust_log_id về status = 0
                result = updateCCRToStatus0(dto.getReceiveCustLogId(), userSession);

                // insert new contact customer result
                ContactCustResult ccrNew = new ContactCustResult();
                ccrNew.setCompanySiteId(dto.getCompanySiteId());
                ccrNew.setCallStatus(dto.getCallStatus());
                ccrNew.setContactStatus(dto.getContactStatus());
                ccrNew.setStatus((short) 1);
                ccrNew.setDescription(dto.getDescription());
                ccrNew.setAgentId(dto.getAgentId());
                ccrNew.setCreateTime(new Date());
                ccrNew.setOldContactCustResultId(dto.getOldContactCustResultId());
                ccrNew.setCampaignId(dto.getCampaignId());
                ccrNew.setCustomerId(dto.getCustomerId());
                ccrNew.setDurationCall(dto.getDurationCall());
                ccrNew.setStartCall(dto.getStartCallL() == null || dto.getStartCallL() == 0 ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getStartCallL() * 1000L), 0L));
                ccrNew.setCallId(dto.getCallId());
                ccrNew.setEndTime(dto.getEndTimeL() == null ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getEndTimeL() * 1000L), 0L));
                ccrNew.setWaitTime(dto.getWaitTime());
                ccrNew.setReceiveCustLogId(dto.getReceiveCustLogId());
                ccrNew.setPhoneNumber(dto.getPhoneNumber());
                ccrNew.setReceiveTime(dto.getReceiveTime() == null ? null : TimeZoneUtils.changeTimeZone(dto.getReceiveTime(), 0L));
                ccrNew.setRecallTime(dto.getRecallTime() == null ? null : TimeZoneUtils.changeTimeZone(dto.getRecallTime(), 0L));
                ccrNew.setIsFinalRecall((short) 1);
                ccrNew.setIsSendEmail((short) 0);
                ccrNew.setDialMode("MANUAL");
                ccr = ccrRepository.save(ccrNew);
            }

            if ("draftAtTen".equalsIgnoreCase(dto.getEventCall())) {
                ccr.setCompanySiteId(dto.getCompanySiteId());
                ccr.setCallStatus(dto.getCallStatus());
                ccr.setContactStatus(dto.getContactStatus());
                ccr.setStatus((short) 2);
                ccr.setDescription(dto.getDescription());
                ccr.setCreateTime(new Date());
                ccr.setAgentId(dto.getAgentId());
                ccr.setCampaignId(dto.getCampaignId());
                ccr.setUpdateBy(userSession.getUserId());
                ccr.setCustomerId(dto.getCustomerId());
                ccr.setDurationCall(dto.getDurationCall());
                ccr.setStartCall(dto.getStartCallL() == null || dto.getStartCallL() == 0 ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getStartCallL() * 1000L), 0L));
                ccr.setReceiveCustLogId(dto.getReceiveCustLogId());
                ccr.setCallId(dto.getCallId());
                ccr.setPhoneNumber(dto.getPhoneNumber());
                ccr.setReceiveTime(dto.getReceiveTime() == null ? null : TimeZoneUtils.changeTimeZone(dto.getReceiveTime(), 0L));
                ccr.setPreEndTime(ccrRepository.getMaxCreateTime(dto.getCampaignId(), dto.getAgentId()));
                ccr.setRecallTime(dto.getRecallTime() == null ? null : TimeZoneUtils.changeTimeZone(dto.getRecallTime(), 0L));
                ccr.setIsFinalRecall((short) 1);
                ccr.setIsSendEmail((short) 0);
                ccr.setEndTime(dto.getEndTimeL() == null ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getEndTimeL() * 1000L), 0L));
                ccr.setWaitTime(dto.getWaitTime());
                ccr.setDialMode("MANUAL");
                ccr = ccrRepository.save(ccr);
            }

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            result.setData(ccr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL_CHAINED, rollbackFor = Exception.class)
    public ResultDTO doSaveContacResult(ContactCustResultDTO dto, UserSession userSession) {
        //logger.info("--- Request to doSaveContacResult:: ");
        logger.info("--- Button Save:: CALL [Call status: " + dto.getEventCall()
                + " | Call id:" + dto.getCallId() + " | Agent id:" + dto.getAgentId() + " | Campaign id:" + dto.getCampaignId()
                + "] --> CUSTOMER [Customer id:" + dto.getCustomerId() + " | Phone number:" + dto.getPhoneNumber() + " | ReceiveCustLogId:" + dto.getReceiveCustLogId() + "]");
        ResultDTO result = new ResultDTO();

        try {
            CallInteractionOtherLeg ci = null;
            if (!dto.getIsReceiveEndCall()) {
                //Lấy thông tin bản tin END_CALL trong Call_interaction_orther_leg
                List<CallInteractionOtherLeg> lstCI = callInteractionOrtherLegRepositoryCustom.getCallInteractionByCallID(dto.getCallId());
                if (lstCI != null && lstCI.size() > 0) {
                    ci = lstCI.get(0);
                } else {
                    if (dto.getCallId() != null && !"".equals(dto.getCallId())) {
                        result.setErrorCode("-2");
                        return result;
                    }
                }
            }

            ContactCustResult ccrOld = null;
            if (dto.getOldContactCustResultId() != null) {
                ccrOld = ccrRepository.findByContactCustResultId(dto.getOldContactCustResultId());
                ccrOld.setStatus((short) 0);
                ccrRepository.save(ccrOld);
                //ccrRepository.deleteById(dto.getOldContactCustResultId());
            }

            //Update các bản ghi cùng Receive_cust_log_id về status = 0
            result = updateCCRToStatus0(dto.getReceiveCustLogId(), userSession);

            //update receive_cus_log
            ReceiveCustLog rcl = receiveCustLogRepository.getByReceiveCustLogId(dto.getReceiveCustLogId());
            if (rcl != null && rcl.getEndTime() == null) {
                rcl.setEndTime(new Date());
                receiveCustLogRepository.save(rcl);
            }

            // insert new contact customer result
            ContactCustResult ccr = new ContactCustResult();
            ccr.setCompanySiteId(dto.getCompanySiteId());
            ccr.setCallStatus(dto.getCallStatus()); // trạng thái khảo sát
            ccr.setContactStatus(dto.getContactStatus()); // trạng thái kết nối
            ccr.setStatus((short) 1);
            ccr.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : "");
            ccr.setAgentId(dto.getAgentId());
            ccr.setCreateTime(new Date());
            ccr.setOldContactCustResultId(dto.getOldContactCustResultId());
            ccr.setCampaignId(dto.getCampaignId());
            ccr.setCustomerId(dto.getCustomerId());
            ccr.setDurationCall(dto.getDurationCall());
            if (dto.getDurationCall() != null && dto.getContactStatus() != null && ci != null
                    && dto.getDurationCall() == 0 && dto.getContactStatus() == 1) {
                ccr.setDurationCall(ci.getAnswerDuration());
            }
            ccr.setStartCall(dto.getStartCallL() == null || dto.getStartCallL() == 0 ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getStartCallL() * 1000L), 0L));
            ccr.setCallId(dto.getCallId());
            ccr.setEndTime(dto.getEndTimeL() == null ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getEndTimeL() * 1000L), 0L));
            ccr.setWaitTime(dto.getWaitTime());
            ccr.setReceiveCustLogId(dto.getReceiveCustLogId());
            ccr.setPhoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : "");
            ccr.setReceiveTime(dto.getReceiveTime() == null ? null : TimeZoneUtils.changeTimeZone(dto.getReceiveTime(), 0L));
            ccr.setRecallTime(dto.getRecallTime() == null ? null : TimeZoneUtils.changeTimeZone(dto.getRecallTime(), 0L));
            ccr.setIsFinalRecall((short) 1);
            ccr.setIsSendEmail((short) 0);
            ccr.setDialMode("MANUAL");
            if (ccrOld != null) {
                ccr.setCallId(ccrOld.getCallId());
                ccr.setDurationCall(ccrOld.getDurationCall());
                ccr.setStartCall(ccrOld.getStartCall());
                ccr.setEndTime(ccrOld.getEndTime());
                ccr.setWaitTime(ccrOld.getWaitTime());
            }
            //Lưu thông tin cuộc gọi theo Call_interaction_orther_leg
            if (ci != null) {
                ccr.setCallId(ci.getLegCallId());
                ccr.setDurationCall(ci.getAnswerDuration());
                ccr.setStartCall(new Date(ci.getCreateTime() * 1000));
                ccr.setEndTime(new Date(ci.getDisconnectTime() * 1000));
            }
            final ContactCustResult ccrNew = ccrRepository.save(ccr);
            // scenario
            if (dto.getLstContactQuest() != null) {
                List<ContactQuestResultDTO> lstContactQuestDto = new ArrayList<>();
                for (ContactQuestResult item : dto.getLstContactQuest()) {
                    item.setContactCustResultId(ccrNew.getContactCustResultId());
                    lstContactQuestDto.add(modelMapper.map(item, ContactQuestResultDTO.class));
                }

                if (dto.getCounter() != null && dto.getCounter() < 10) {
                    // update old contact quest status to zero
                    List<ContactQuestResult> lstContactQuestResult = cqrRepository.findByContactCustResultId(dto.getOldContactCustResultId());
                    if (lstContactQuestResult != null) {
                        lstContactQuestResult.forEach(item -> item.setStatus((short) 0));
                    }
                    cqrRepository.saveAll(lstContactQuestResult);
                    // create new contact quest result
                }
                this.createListContactQuestResult(lstContactQuestDto);
            }
            // campaign customer
            CampaignCustomerDTO cc = new CampaignCustomerDTO();
            cc.setCampaignId(dto.getCampaignId());
            cc.setCustomerId(dto.getCustomerId());
            cc.setContactStatus(dto.getContactStatus());
            cc.setCallStatus(dto.getCallStatus() == null ? null : dto.getCallStatus().longValue());
            cc.setAgentId(dto.getAgentId());
            cc.setRecallTime(dto.getRecallTime());
            cc.setCallTimeL(dto.getStartCallL() != null && dto.getStartCallL() > 0 ? dto.getStartCallL() : null);
            if (ccrOld != null) {
                cc.setCallTime(ccrOld.getStartCall());
            } else {
                cc.setCallTime(null);
            }
            cc.setTimezone(dto.getTimezone());
            ResultDTO updateCampaignCustomer = this.updateCampaignCustomer(cc);

            // update survey status
            if (updateCampaignCustomer != null && updateCampaignCustomer.getErrorCode().equals(Constants.ApiErrorCode.SUCCESS)) {
                CampaignCustomer ccObj = (CampaignCustomer) updateCampaignCustomer.getData();
                if (ccObj != null && ccObj.getStatus() != null) {
                    // campaign customer time: Nếu trạng thái khảo sát có (CAMPAIGN_COMPLETE_CODE.IS_LOCK = 1):
                    // thực hiện "locked" KH tại bản CUSTOMER và insert 1 bản ghi vào CUSTOEMR_TIME
                    CustomerTimeDTO ct = new CustomerTimeDTO();
                    ct.setCompanySiteId(dto.getCompanySiteId());
                    ct.setCompleteValue(ccObj.getStatus().toString());
                    ct.setCustomerId(dto.getCustomerId());
                    ct.setContactCustResultId(ccrNew.getContactCustResultId());
                    this.updateSurveyStatus(ct, userSession);
                }
            }
            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            result.setData(ccrNew);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO draftAtTen(ContactCustResultDTO dto, UserSession userSession) {
        logger.info("--- Request to draftAtTen:: CALL [Call id: " + dto.getCallId() + " | Agent id: " + dto.getAgentId() + " | Campaign id: " + dto.getCampaignId()
                + "] --> CUSTOMER [Customer id: " + dto.getCustomerId() + " | Phone number: " + dto.getPhoneNumber() + " | ReceiveCustLogId: " + dto.getReceiveCustLogId() + "]");
        ResultDTO result = new ResultDTO();

        try {
            // delete contact cust result Old
            if (dto.getOldContactCustResultId() != null) {
                ContactCustResult ccrOld = ccrRepository.findByContactCustResultId(dto.getOldContactCustResultId());
                ccrOld.setStatus((short) 0);
                ccrRepository.save(ccrOld);
                //ccrRepository.deleteById(dto.getOldContactCustResultId());
            }

            //Update các bản ghi cùng Receive_cust_log_id về status = 0
            result = updateCCRToStatus0(dto.getReceiveCustLogId(), userSession);

            // create contact cust result New
            ContactCustResult ccrNew = new ContactCustResult();
            ccrNew.setCompanySiteId(dto.getCompanySiteId());
            ccrNew.setCallStatus(dto.getCallStatus());
            ccrNew.setContactStatus(dto.getContactStatus());
            ccrNew.setStatus((short) 2);
            ccrNew.setDescription(dto.getDescription());
            ccrNew.setCreateTime(new Date());
            ccrNew.setAgentId(dto.getAgentId());
            ccrNew.setCampaignId(dto.getCampaignId());
            ccrNew.setUpdateBy(userSession.getUserId());
            ccrNew.setCustomerId(dto.getCustomerId());
            ccrNew.setDurationCall(dto.getDurationCall());
            ccrNew.setStartCall(dto.getStartCallL() == null || dto.getStartCallL() == 0 ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getStartCallL() * 1000L), 0L));
            ccrNew.setReceiveCustLogId(dto.getReceiveCustLogId());
            ccrNew.setCallId(dto.getCallId());
            ccrNew.setPhoneNumber(dto.getPhoneNumber());
            ccrNew.setReceiveTime(dto.getReceiveTime() == null ? null : TimeZoneUtils.changeTimeZone(dto.getReceiveTime(), 0L));
            ccrNew.setPreEndTime(ccrRepository.getMaxCreateTime(dto.getCampaignId(), dto.getAgentId()));
            ccrNew.setRecallTime(dto.getRecallTime() == null ? null : TimeZoneUtils.changeTimeZone(dto.getRecallTime(), 0L));
            ccrNew.setIsFinalRecall((short) 1);
            ccrNew.setIsSendEmail((short) 0);
            ccrNew.setEndTime(dto.getEndTimeL() == null ? null : TimeZoneUtils.changeTimeZone(new Date(dto.getEndTimeL() * 1000L), 0L));
            ccrNew.setWaitTime(dto.getWaitTime());
            ccrNew.setDialMode("MANUAL");
            ccrNew = ccrRepository.save(ccrNew);
            // create scenario
            List<ContactQuestResultDTO> lstContactQuestDto = new ArrayList<>();
            for (ContactQuestResult item : dto.getLstContactQuest()) {
                item.setContactCustResultId(ccrNew.getContactCustResultId());
                lstContactQuestDto.add(modelMapper.map(item, ContactQuestResultDTO.class));
            }
            this.createListContactQuestResult(lstContactQuestDto);

            // update campaign customer
            CampaignCustomerDTO cc = new CampaignCustomerDTO();
            cc.setCampaignId(dto.getCampaignId());
            cc.setCustomerId(dto.getCustomerId());
            cc.setContactStatus(dto.getContactStatus());
            cc.setCallStatus(dto.getCallStatus() == null ? null : dto.getCallStatus().longValue());
            cc.setAgentId(dto.getAgentId());
            cc.setRecallTime(dto.getRecallTime());
            cc.setCallTimeL(dto.getStartCallL());
            cc.setTimezone(dto.getTimezone());
            cc.setStatus((dto.getDurationCall() != null && dto.getDurationCall() > 0) ? (dto.getCallStatus() != null ? dto.getCallStatus().shortValue() : (short) 3) : (short) 2);
            this.updateCampaignCustomer(cc);

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            result.setData(ccrNew);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO recallCustomer(ContactCustResultDTO dto) {
        ResultDTO result = new ResultDTO();

        try {
            //ccrRepository.deleteById(dto.getContactCustResultId());
            //Update các bản ghi cùng Receive_cust_log_id về status = 0
            result = updateCCRToStatus0(dto.getReceiveCustLogId(), new UserSession());

            ContactCustResult ccr = ccrRepository.save(modelMapper.map(dto, ContactCustResult.class));

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            result.setData(ccr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO updateCustomerResult(ContactCustResultDTO dto) {
        ResultDTO result = new ResultDTO();

        try {
            ContactCustResult ccr = ccrRepository.findByContactCustResultId(dto.getContactCustResultId());
            ccr.setDurationCall(0L);
            ccr.setStartCall(new Date());
            ccr.setReceiveTime(new Date());
            ccr.setPreEndTime(new Date());
            ccr.setEndTime(new Date());
            ccr.setWaitTime(0L);
            ContactCustResult resultUpdate = ccrRepository.save(ccr);

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription(Constants.ApiErrorDesc.SUCCESS);
            result.setData(resultUpdate);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setErrorCode(Constants.ApiErrorCode.ERROR);
            result.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return result;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL)
    public ResultDTO countRecallCustomer(Long companySiteId, Long agentId) {
        Long count;
        ResultDTO resultDTO = new ResultDTO();
        UserRole userRole = userRoleRepository.findUserRoleByUserId(agentId);
        if (userRole == null) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            resultDTO.setData(0);
            return resultDTO;
        }
        if (4L == userRole.getRoleId() || 5L == userRole.getRoleId()) {
            count = campaignCustomerRepository.countRecallCustomerAdminRole(companySiteId, new Date());
        } else {
            count = campaignCustomerRepository.countRecallCustomerUserRole(companySiteId, agentId, new Date());
        }

        if (count != null) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setData(count);
        } else {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            resultDTO.setData(0);
        }

        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCustomerRecall(Long campaignId, Long customerId) {
        ResultDTO resultDTO = new ResultDTO();

        Long count = campaignCustomerRepository.getCustomerRecall(campaignId, customerId);

        if (count != null) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setData(count);
        } else {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            resultDTO.setData(0);
        }

        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCustomerInfor(Long companySiteId, Long customerId, Long campaignId, String language) {
        ResultDTO resultDTO = new ResultDTO();
        List<CustomerContact> cc;
        Locale locale = Locale.forLanguageTag(language);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (DataUtil.isNullOrZero(companySiteId)) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            return resultDTO;
        }

        try {
            //get static customer infor
            Customer customer = customerRepository.findByCustomerId(customerId);
            cc = customerContactRepository.getLastPhone(customerId, (short) 1, (short) 5, (short) 1);
            if (cc != null && cc.size() > 0)
                customer.setMobileNumber(cc == null ? "" : cc.get(0).getContact());
            cc = customerContactRepository.getLastEmail(customerId, (short) 1, (short) 2);
            if (cc != null && cc.size() > 0)
                customer.setEmail(cc == null ? "" : cc.get(0).getContact());


            List<CampaignCustomerListColumnRequestDTO> data = campaignCustomerListColumnRepository.getCustomerInfor(companySiteId, customerId, campaignId);

            for (CampaignCustomerListColumnRequestDTO camp : data) {
                if ("MOBILE_NUMBER".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getMobileNumber());
                    camp.setCheckMobileOrEmail("mobile");
                }
                if ("PLACE_OF_BIRTH".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getPlaceOfBirth());
                }
                if ("CREATE_DATE".equals(camp.getCustomizeFieldTitle())) {
                    if (null != customer.getCreateDate())
                        camp.setValueText(dateFormat.format(customer.getCreateDate()));
                    else camp.setValueText(null);
                }
                if ("DATE_OF_BIRTH".equals(camp.getCustomizeFieldTitle())) {
                    if (null != customer.getDateOfBirth())
                        camp.setValueText(dateFormat.format(customer.getDateOfBirth()));
                    else camp.setValueText(null);
                }
                if ("SMS_ALLOWED".equals(camp.getCustomizeFieldTitle())) {
                    if (null != customer.getSmsAllowed()) {
                        camp.setValueText(String.valueOf(customer.getSmsAllowed()));
                        camp.setCheckMobileOrEmail("smsAllowed");
                    } else {
                        camp.setValueText(null);
                    }
                }
                if ("EMAIL_ALLOWED".equals(camp.getCustomizeFieldTitle())) {
                    if (null != customer.getEmailAllowed()) {
                        camp.setValueText(String.valueOf(customer.getEmailAllowed()));
                        camp.setCheckMobileOrEmail("emailAllowed");
                    } else {
                        camp.setValueText(null);
                    }
                }
                if ("GENDER".equals(camp.getCustomizeFieldTitle())) {
                    if (null != customer.getGender()) {
                        camp.setValueText(String.valueOf(customer.getGender()));
                        camp.setCheckMobileOrEmail("gender");
                    } else {
                        camp.setValueText(null);
                    }
                }
                if ("CUSTOMER_TYPE".equals(camp.getCustomizeFieldTitle())) {
                    if (null != customer.getCustomerType()) {
                        camp.setValueText(String.valueOf(customer.getCustomerType()));
                        camp.setCheckMobileOrEmail("customerType");
                    } else {
                        camp.setValueText(null);
                    }
                }
                if ("CURRENT_ADDRESS".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getCurrentAddress());
                }
                if ("COMPANY_NAME".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getCompanyName());
                }
                if ("CALL_ALLOWED".equals(camp.getCustomizeFieldTitle())) {
                    if (null != customer.getCallAllowed()) {
                        camp.setValueText(String.valueOf(customer.getCallAllowed()));
                        camp.setCheckMobileOrEmail("callAllowed");
                    } else {
                        camp.setValueText(null);
                    }
                }
                if ("UPDATE_BY".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getUpdateBy());
                }
                if ("CUSTOMER_ID".equals(camp.getCustomizeFieldTitle())) {
                    if (null != customer.getCustomerId()) camp.setValueText(String.valueOf(customer.getCustomerId()));
                    else camp.setValueText(null);
                }
                if ("DESCRIPTION".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getDescription());
                }
                if ("UPDATE_DATE".equals(camp.getCustomizeFieldTitle())) {
                    if (null != customer.getUpdateDate())
                        camp.setValueText(dateFormat.format(customer.getUpdateDate()));
                    else camp.setValueText(null);
                }
                if ("IPCC_STATUS".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getIpccStatus());
                    camp.setCheckMobileOrEmail("ipccStatus");
                }
                if ("USERNAME".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getUserName());
                }
                if ("NAME".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getName());
                }
                if ("CREATE_BY".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getCreateBy());
                }
                if ("AVATAR_LINK".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getAvatarLink());
                }
                if ("CUSTOMER_IMG".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getCustomerImg());
                }
                if ("EMAIL".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getEmail());
                    camp.setCheckMobileOrEmail("email");
                }
                if ("CODE".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getCode());
                }
                if ("STATUS".equals(camp.getCustomizeFieldTitle())) {
                    if (null != customer.getStatus()) {
                        camp.setValueText(String.valueOf(customer.getStatus()));
                        camp.setCheckMobileOrEmail("status");
                    } else {
                        camp.setValueText(null);
                    }
                }
                if ("SITE_ID".equals(camp.getCustomizeFieldTitle())) {
                    if (null != customer.getSiteId()) camp.setValueText(String.valueOf(customer.getSiteId()));
                    else camp.setValueText(null);
                }
                if ("AREA_CODE".equals(camp.getCustomizeFieldTitle())) {
                    camp.setValueText(customer.getAreaCode());
                }
                String title = camp.getCustomizeFieldTitle();
                if ("0".equals(camp.getDynamic())) {
                    title = BundleUtils.getLangString(camp.getCustomizeFieldTitle(), locale);
                }
                if (null != title) {
                    camp.setCustomizeFieldTitle(title);
                } else {
                    camp.setCustomizeFieldTitle(camp.getCustomizeFieldTitle());
                }
                logger.info("--- getCustomerInfor::" + data.size() + " | customerID:" + customerId + " | campaignID:" + campaignId + " | phoneNumber:" + customer.getMobileNumber());
            }

            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setData(data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO createListContactQuestResult(List<ContactQuestResultDTO> dtoList) {
        ResultDTO resultDTO = new ResultDTO();
        List<ContactQuestResult> lstContactQuestResult = new ArrayList<>();

        try {
            for (ContactQuestResultDTO item : dtoList) {
//                lstContactQuestResult.add(modelMapper.map(item, ContactQuestResult.class));
                lstContactQuestResult.add(item.toEntity());
            }

//            List<ContactQuestResult> dataReturn = cqrRepository.saveAll(lstContactQuestResult);
            List<ContactQuestResult> dataReturn = new ArrayList<>();
            for (int i = 0; i < lstContactQuestResult.size(); i++) {
                ContactQuestResult cqrItem = lstContactQuestResult.get(i);
                cqrItem = cqrRepository.save(cqrItem);
                dataReturn.add(cqrItem);
            }

            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setListData(dataReturn);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return resultDTO;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO updateListContactQuestResult(ContactQuestResultDTO dto) {
        ResultDTO resultDTO = new ResultDTO();
        List<ContactQuestResult> lstContactQuestResult = new ArrayList<>();

        try {
            lstContactQuestResult = cqrRepository.findByContactCustResultId(dto.getContactCustResultId());
            for (ContactQuestResult item : lstContactQuestResult) {
                item.setStatus((short) 0);
            }
            lstContactQuestResult = cqrRepository.saveAll(lstContactQuestResult);

            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setListData(lstContactQuestResult);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return resultDTO;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL)
    public String getDialModeAtCurrent(Long companySiteId, Long campaignId, Integer timezoneOffset) {
        // 0: manual, 1- Preview, 2-Progressive, 3-Super Progressive, 4- Predictive
        try {
            List<TimeRangeDialMode> lstTimeRange = rangeDialModeRepository.findTimeRangeDialModeByCampaignIdAndCompanySiteId(campaignId, companySiteId);
            List<TimeZoneDialMode> lstTimeZone = zoneDialModeRepository.findTimeZoneDialModeByCampaignIdAndCompanySiteId(campaignId, companySiteId);
            Campaign campaignObj = campaignRepository.findByCampaignId(campaignId);

            if (lstTimeRange != null && lstTimeRange.size() > 0) {
                TimeRangeDialMode dialModeAtCurrent = rangeDialModeRepository.findDialModeAtCurrent(companySiteId, campaignId, timezoneOffset);
                if (dialModeAtCurrent != null)
                    return DataUtil.safeToString(dialModeAtCurrent.getDialMode());
            } else if (lstTimeZone != null && lstTimeZone.size() > 0) {
                String rt = "-1";
                Date currentDate = new Date();
//                if (currentDate.getTime() >= campaignObj.getStartTime().getTime() &&
//                        currentDate.getTime() <= campaignObj.getEndTime().getTime()) {
                String currentDateStr = new SimpleDateFormat("dd-MM-yyyy").format(currentDate);
                String startTimeDateStr = new SimpleDateFormat("dd-MM-yyyy").format(campaignObj.getStartTime());
                //logger.info("--- Time check Dial mode: currentDate:" + currentDate + " | startTimeDate:" + campaignObj.getStartTime() + " | timezoneOffset:" + timezoneOffset);
                HashMap<Date, String> mapDualWithDate = new HashMap<>();
                if (!DateTimeUtil.inTheSameDay(currentDate, campaignObj.getStartTime()) &&
                        (currentDate.getTime() > campaignObj.getStartTime().getTime())) {
                    //logger.info("--- Time check Dial mode: inTheSameDay");
                    Date maxDialDate = new Date();
                    int count = 0;
                    for (TimeZoneDialMode item : lstTimeZone) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String itemTzDateStr = startTimeDateStr + " " + (Integer.parseInt(item.getHour()) < 10 ? "0" + item.getHour() : item.getHour()) + ":" + item.getMinute() + ":00";
                        Date itemTzDate = DateTimeUtil.minusTimezoneOffsetHours(formatter.parse(itemTzDateStr), timezoneOffset);
                        //logger.info("--- Time check Dial mode: itemTzDate" + itemTzDate);
                        if (currentDate.getTime() > itemTzDate.getTime()) {
                            count++;
                        }
                        mapDualWithDate.put(itemTzDate, item.getDialMode().toString());
                        maxDialDate = itemTzDate;
                    }
                    if (count > 0) {
//                            New CurrentDate lon hon 1 or nhieu cau hinh => chon cau hinh gan nhat
                        for (TimeZoneDialMode item : lstTimeZone) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            String itemTzDateStr = startTimeDateStr + " " + (Integer.parseInt(item.getHour()) < 10 ? "0" + item.getHour() : item.getHour()) + ":" + item.getMinute() + ":00";
                            Date itemTzDate = DateTimeUtil.minusTimezoneOffsetHours(formatter.parse(itemTzDateStr), timezoneOffset);
                            //logger.info("--- Time check Dial mode: itemTzDate2" + itemTzDate);
                            if (currentDate.getTime() >= itemTzDate.getTime()) {
                                rt = item.getDialMode().toString();
                                break;
                            }
                        }
                    } else {
//                            Neu CurrentDate < Date cua tat ca cac cau hinh => chon cau hinh lon nhat
                        for (Date d : mapDualWithDate.keySet()) {
                            if (d.getTime() > maxDialDate.getTime()) rt = mapDualWithDate.get(d);
                            //logger.info("--- Time check Dial mode: maxDialDate" + maxDialDate.getTime());
                        }
                    }
                } else {
                    for (TimeZoneDialMode item : lstTimeZone) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String itemTzDateStr = currentDateStr + " " + (Integer.parseInt(item.getHour()) < 10 ? "0" + item.getHour() : item.getHour()) + ":" + item.getMinute() + ":00";
                        Date itemTzDate = DateTimeUtil.minusTimezoneOffsetHours(formatter.parse(itemTzDateStr), timezoneOffset);
                        //logger.info("--- Time check Dial mode: itemTzDate3" + itemTzDate);
                        if (currentDate.getTime() >= itemTzDate.getTime()) {
                            rt = item.getDialMode().toString();
                        }
                    }
                }
//                }
                logger.info("--- getDialModeAtCurrent DIAL MODE: " + rt);
                return rt;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return "-1";
    }

    private List<Date> getDatesBetweenUsingJava7(Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        return datesInRange;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, readOnly = true
    )
    public ResultDTO getContactCustResultById(Long contactCustResultId, Integer timeZone) {
        logger.info("--- Request to getContactCustResultById:: " + contactCustResultId);
        ResultDTO resultDTO = new ResultDTO();

        try {
            ContactCustResult ccr = ccrRepository.findByContactCustResultId(contactCustResultId);
            ccr.setRecallTime(ccr.getRecallTime() == null ? null : DateTimeUtil.addHoursToJavaUtilDate(ccr.getRecallTime(), timeZone / 60));
            ccr.setReceiveTime(ccr.getReceiveTime() == null ? null : DateTimeUtil.addHoursToJavaUtilDate(ccr.getReceiveTime(), timeZone / 60));
            // convert ccr to dto)
            ContactCustResultDTO dtoCCR = modelMapper.map(ccr, ContactCustResultDTO.class);
            // check allow update
            Calendar createTimeAfter24h = Calendar.getInstance();
            createTimeAfter24h.setTime(dtoCCR.getCreateTime());
            createTimeAfter24h.add(Calendar.HOUR_OF_DAY, Integer.valueOf(timeEditInteractionResult));
            if (new Date().after(dtoCCR.getCreateTime())) {
                if (new Date().before(createTimeAfter24h.getTime())) {
                    dtoCCR.setCheckUpdate(true);
                }
            }
            // contact status object
            if (dtoCCR.getContactStatus() != null) {
                CampaignCfg ct = cfgRepository.findByCompanySiteIdAndCompleteValue(dtoCCR.getCompanySiteId(), dtoCCR.getContactStatus().toString());
                dtoCCR.setContactStatusObj(ct);
                dtoCCR.setLstContactStatus(cfgRepository.getCustomerStatusByType(ct.getCompleteType(), dtoCCR.getCompanySiteId()));
            }
            // call status object
            if (dtoCCR.getCallStatus() != null) {
                CampaignCfg ct = cfgRepository.findByCompanySiteIdAndCompleteValue(dtoCCR.getCompanySiteId(), dtoCCR.getCallStatus().toString());
                dtoCCR.setCallStatusObj(ct);
                dtoCCR.setLstCallStatus(cfgRepository.getCustomerStatusByType(ct.getCompleteType(), dtoCCR.getCompanySiteId()));
            }
            // campaign customer object
            CampaignCustomer cc = campaignCustomerRepository.findCampaignCustomerByCampaignIdAndCompanySiteIdAndCustomerId(dtoCCR.getCampaignId(), dtoCCR.getCompanySiteId(), dtoCCR.getCustomerId());

            if (cc.getRecallTime() == null && ccr.getRecallTime() != null) {
                cc.setRecallTimeStr(DateTimeUtil.format("yyyy/MM/dd HH:mm:ss", ccr.getRecallTime(), null));
            } else {
                cc.setRecallTimeStr(cc.getRecallTime() == null ? null : DateTimeUtil.format("yyyy/MM/dd HH:mm:ss",
                        DateTimeUtil.addHoursToJavaUtilDate(cc.getRecallTime(), timeZone / 60), null));
            }
            dtoCCR.setCampaignCustomerObj(cc);

            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setData(dtoCCR);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return resultDTO;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO updateCampaignCustomer(CampaignCustomerDTO dto) {
        ResultDTO resultDTO = new ResultDTO();

        try {
            CampaignCustomer cc = campaignCustomerRepository.findCampaignCustomersByCampaignIdAndCustomerId(dto.getCampaignId(), dto.getCustomerId());
            if (dto.getContactStatus().equals((short) 1)) {
                cc.setStatus(dto.getCallStatus() == null ? null : dto.getCallStatus().shortValue());
            } else {
                Campaign c = campaignRepository.findByCampaignId(dto.getCampaignId());
                if (cc.getRecallCount() + 1L == c.getMaxRecall().longValue()) {
                    cc.setStatus((short) 4);
                    cc.setRecallCount(cc.getRecallCount() + 1L);
                } else {
                    cc.setStatus(dto.getContactStatus().shortValue());
                    CampaignCfg cfg = cfgRepository.findByCompanySiteIdAndCompleteValue(cc.getCompanySiteId(), dto.getContactStatus().toString());
                    if (cfg != null && cfg.getIsFinish().equals((short) 0)) {
                        cc.setRecallCount(cc.getRecallCount() + 1L);
                    } else {
                        cc.setRecallCount(cc.getRecallCount() + 0L);
                    }
                }
            }
            if (dto.getRecallTime() != null) {
                cc.setRecallTime(dto.getRecallTime() == null ? null : TimeZoneUtils.changeTimeZone(dto.getRecallTime(), 0L));
            }
            cc.setCallTime(dto.getCallTimeL() == null ? dto.getCallTime() == null ? null : dto.getCallTime() : TimeZoneUtils.changeTimeZone(new Date(dto.getCallTimeL() * 1000L), 0L));
            cc.setAgentId(dto.getAgentId());
            cc = campaignCustomerRepository.save(cc);

            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setData(cc);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return resultDTO;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO updateSurveyStatus(CustomerTimeDTO dto, UserSession userSession) {
        ResultDTO resultDTO = new ResultDTO();

        try {
            CampaignCfg cfg = cfgRepository.findByCompanySiteIdAndCompleteValue(dto.getCompanySiteId(), dto.getCompleteValue());

            if (cfg != null && cfg.getIsLock() == 1) {
                Customer c = customerRepository.getByCustomerId(dto.getCustomerId());
                c.setIpccStatus("locked");
                customerRepository.save(c);
                // insert CustomerTime
                CustomerTime ct = new CustomerTime();
                ct.setCompanySiteId(dto.getCompanySiteId());
                ct.setCustomerId(dto.getCustomerId());
                ct.setStartTime(new Date());

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(ct.getStartTime());
                calendar.add(Calendar.DAY_OF_MONTH, cfg.getDurationLock().intValue());
                ct.setEndTime(calendar.getTime());

                ct.setStatus((short) 1);
                ct.setCreateTime(new Date());
                ct.setCreateBy(userSession.getUserId());
                ct.setUpdateTime(new Date());
                ct.setUpdateBy(userSession.getUserId());
                ct.setContactCustResultId(dto.getContactCustResultId());
                customerTimeRepository.save(ct);
            }

            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setData(null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return resultDTO;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO updateSurveyStatusInteractive(CustomerTimeDTO dto, UserSession userSession) {
        ResultDTO resultDTO = new ResultDTO();

        try {
            CampaignCfg cfg = cfgRepository.findByCompanySiteIdAndCompleteValue(dto.getCompanySiteId(), dto.getCompleteValue());

            if (cfg != null && cfg.getIsLock() == 1) {
                Customer c = customerRepository.getByCustomerId(dto.getCustomerId());
                c.setIpccStatus("locked");
                customerRepository.save(c);
                // insert CustomerTime
                CustomerTime ct = new CustomerTime();
                ct.setCompanySiteId(dto.getCompanySiteId());
                ct.setCustomerId(dto.getCustomerId());
                ct.setStartTime(new Date());

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(ct.getStartTime());
                calendar.add(Calendar.DAY_OF_MONTH, cfg.getDurationLock() != null ? cfg.getDurationLock().intValue() : 0);
                ct.setEndTime(calendar.getTime());

                ct.setStatus((short) 1);
                ct.setCreateTime(new Date());
                ct.setCreateBy(userSession.getUserId());
                ct.setUpdateTime(new Date());
                ct.setUpdateBy(userSession.getUserId());
                ct.setContactCustResultId(dto.getContactCustResultId());
                customerTimeRepository.save(ct);
            }

            if (cfg != null && cfg.getIsLock() == 0) {
                Customer c = customerRepository.getByCustomerId(dto.getCustomerId());
                c.setIpccStatus("active");
                customerRepository.save(c);
                // insert CustomerTime
                CustomerTime ct = customerTimeRepository.findByCompanySiteIdAndCustomerId(dto.getCompanySiteId(), dto.getCustomerId());
                if (ct != null) {
                    ct.setStatus((short) 0);
                    ct.setUpdateTime(new Date());
                    ct.setUpdateBy(userSession.getUserId());
                    customerTimeRepository.save(ct);
                }
            }

            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setData(null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return resultDTO;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL_CHAINED, rollbackFor = Exception.class)
    public ResultDTO updateInteractiveResults(ContactCustResultDTO updateObj, UserSession userSession) {
        logger.info("--- START UPDATE INTERACTIVE RESULTS:: OldContactCustResultId:" + updateObj.getOldContactCustResultId()
                + " | CampaignId:" + updateObj.getCampaignId() + " CustomerId:" + updateObj.getCustomerId() + " | ReceiveCustLogId:" + updateObj.getReceiveCustLogId());
        ResultDTO resultDTO = new ResultDTO();

        try {
            // update old contact customer result
            ContactCustResult ccrOld = ccrRepository.findByContactCustResultId(updateObj.getOldContactCustResultId());
            if (ccrOld != null) {
                ccrOld.setStatus((short) 0);
                ccrOld.setUpdateBy(userSession.getUserId());
                ccrOld.setUpdateTime(new Date());
                ccrRepository.save(ccrOld);
            }

            //Update các bản ghi cùng Receive_cust_log_id về status = 0
            resultDTO = updateCCRToStatus0(updateObj.getReceiveCustLogId(), userSession);

            //update receive_cus_log
            ReceiveCustLog rcl = receiveCustLogRepository.getByReceiveCustLogId(updateObj.getReceiveCustLogId());
            if (rcl != null && rcl.getEndTime() == null) {
                rcl.setEndTime(new Date());
                receiveCustLogRepository.save(rcl);
            }

            // insert new contact customer result
            ContactCustResult ccr = new ContactCustResult();
            ccr.setCompanySiteId(updateObj.getCompanySiteId());
            ccr.setCallStatus(updateObj.getCallStatus()); // trạng thái khảo sát
            ccr.setContactStatus(updateObj.getContactStatus()); // trạng thái kết nối
            ccr.setStatus((short) 1);
            ccr.setDescription(updateObj.getDescription() != null ? updateObj.getDescription() : "");
            ccr.setAgentId(updateObj.getAgentId());
            ccr.setCreateTime(new Date());
            ccr.setOldContactCustResultId(updateObj.getOldContactCustResultId());
            ccr.setCampaignId(updateObj.getCampaignId());
            ccr.setCustomerId(updateObj.getCustomerId());
            if (ccrOld != null) {
                ccr.setDurationCall(ccrOld.getDurationCall());
                ccr.setStartCall(ccrOld.getStartCall());
                ccr.setCallId(ccrOld.getCallId());
                ccr.setEndTime(ccrOld.getEndTime());
                ccr.setWaitTime(ccrOld.getWaitTime());
                ccr.setReceiveTime(ccrOld.getReceiveTime());
            } else {
                ccr.setReceiveTime(updateObj.getReceiveTime() == null ? null : TimeZoneUtils.changeTimeZone(updateObj.getReceiveTime(), 0L));
            }
            ccr.setReceiveCustLogId(updateObj.getReceiveCustLogId());
            ccr.setPhoneNumber(updateObj.getPhoneNumber() != null ? updateObj.getPhoneNumber() : "");
            ccr.setRecallTime(updateObj.getRecallTime() == null ? null : TimeZoneUtils.changeTimeZone(updateObj.getRecallTime(), 0L));
            ccr.setIsFinalRecall((short) 1);
            ccr.setIsSendEmail((short) 0);
            ccr.setDialMode("MANUAL");
            final ContactCustResult ccrNew = ccrRepository.save(ccr);

            //update list contact quest results
            if (updateObj.getLstContactQuest() != null) {
                List<ContactQuestResult> lstContactQuestResult = cqrRepository.findByContactCustResultId(updateObj.getOldContactCustResultId());
                if (lstContactQuestResult != null) {
                    lstContactQuestResult.forEach(item -> item.setStatus((short) 0));
                    cqrRepository.saveAll(lstContactQuestResult);
                }
                //end

                //create new contact quest results
                updateObj.getLstContactQuest().forEach(item -> item.setContactCustResultId(ccrNew.getContactCustResultId()));
                cqrRepository.saveAll(updateObj.getLstContactQuest());
            }

            // campaign customer
            CampaignCustomerDTO cc = new CampaignCustomerDTO();
            cc.setCampaignId(updateObj.getCampaignId());
            cc.setCustomerId(updateObj.getCustomerId());
            cc.setContactStatus(updateObj.getContactStatus());
            cc.setCallStatus(updateObj.getCallStatus() == null ? null : updateObj.getCallStatus().longValue());
            cc.setAgentId(updateObj.getAgentId());
            cc.setRecallTime(updateObj.getRecallTime());
            cc.setCallTimeL(updateObj.getStartCallL() != null && updateObj.getStartCallL() > 0 ? updateObj.getStartCallL() : null);
            if (ccrOld != null) {
                cc.setCallTime(ccrOld.getStartCall());
            }
//            cc.setTimezone(updateObj.getTimezone());
            ResultDTO updateCampaignCustomer = this.updateCampaignCustomer(cc);

            // update survey status
            if (updateCampaignCustomer != null && updateCampaignCustomer.getErrorCode().equals(Constants.ApiErrorCode.SUCCESS)) {
                CampaignCustomer ccObj = (CampaignCustomer) updateCampaignCustomer.getData();
                if (ccObj != null && ccObj.getStatus() != null) {
                    // campaign customer time: Nếu trạng thái khảo sát có (CAMPAIGN_COMPLETE_CODE.IS_LOCK = 1):
                    // thực hiện "locked" KH tại bản CUSTOMER và insert 1 bản ghi vào CUSTOEMR_TIME
                    CustomerTimeDTO ct = new CustomerTimeDTO();
                    ct.setCompanySiteId(updateObj.getCompanySiteId());
                    ct.setCompleteValue(ccObj.getStatus().toString());
                    ct.setCustomerId(updateObj.getCustomerId());
                    ct.setContactCustResultId(ccrNew.getContactCustResultId());
                    this.updateSurveyStatusInteractive(ct, userSession);
                }
            }

            resultDTO.setData(ccrNew);
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
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO doInitForRecall(Long receiveCustLogId, UserSession userSession) {
        ResultDTO resultDTO = new ResultDTO();

        try {
            // get list contact customer result for delete
            List<ContactCustResult> custResultList = ccrRepository.findByReceiveCustLogId(receiveCustLogId);
            List<ContactQuestResult> contactQuestResultList = new ArrayList<>();
            if (custResultList != null) {
                // get list contact question result for delete
                for (ContactCustResult item : custResultList) {
                    List<ContactQuestResult> cqrList = cqrRepository.findByContactCustResultId(item.getContactCustResultId());
                    contactQuestResultList.addAll(cqrList);
                }
                // delete all contact customer result by id
                ccrRepository.deleteAll(custResultList);
                // delete all contact quest result by idr
                if (contactQuestResultList != null) {
                    cqrRepository.deleteAll(contactQuestResultList);
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
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public ResultDTO checkInterruptCampaigns(Long campaignId) {
        logger.info("----- START CHECK INTERRUPT CAMPAIGNS::");
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
        resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        try {
            if (campaignId != null) {
                Integer countInterrupts = campaignRepository.checkInterruptCampaigns(campaignId);
                if (countInterrupts != null) {
                    resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                    resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
                    resultDTO.setData(countInterrupts);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        logger.info("----- CHECK INTERRUPT CAMPAIGNS RESULTS::" + resultDTO.getErrorCode());
        return resultDTO;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL)
    public ResultDTO getContactCusResultId(Long customerId, Long campaignId, Long companySiteId) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
        resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        logger.info("----- START GET CONTACT CUS RESULT ID::");
        try {
            Long ccrId = contactCustResultRepository.getContactCusResultId(customerId, campaignId, companySiteId);
            if (ccrId != null) {
                resultDTO.setData(ccrId);
                resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        logger.info("-----  GET CONTACT CUS RESULTS::" + resultDTO.getErrorCode());

        return resultDTO;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL)
    public ResultDTO checkRecallDuration(Long customerId, Long campaignId, Long companySiteId) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
        resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        logger.info("----- START CHECK RECALL DURATION::" + customerId + ", " + campaignId + ", " + companySiteId);
        try {
            ContactCustResult ccr = contactCustResultRepository.findByCustomerIdAndCampaignIdAndCompanySiteIdAndContactStatusAndStatus(customerId, campaignId, companySiteId, (short) 2, (short) 1);
            if (ccr != null && ccr.getContactCustResultId() != null) {
                Date endTime = ccr.getEndTime();
                Campaign campaign = campaignRepository.findByCampaignId(campaignId);
                Integer recallDuration = campaign.getRecallDuration();
                Integer recallType = campaign.getRecallType();
                if (endTime != null && recallDuration != null) {
                    if (2 == recallType && (DateTimeUtil.addHoursToJavaUtilDate(endTime, recallDuration).getTime() > new Date().getTime())) {
                        resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                        resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
                        resultDTO.setData(ccr);
                    } else if (1 == recallType && (DateTimeUtil.addHoursToJavaUtilDate(endTime, recallDuration * 24).getTime() > new Date().getTime())) {
                        resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                        resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
                        resultDTO.setData(ccr);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        logger.info("----- END CHECK RECALL DURATION::" + resultDTO.getErrorCode());
        return resultDTO;
    }

    @Override
    public ResultDTO updateCCRAfterCallEnd(ContactCustResultDTO contactCustResultDTO, UserSession userSession) {
        logger.info("----- Request to updateCCRAfterCallEnd::" + contactCustResultDTO.toString());
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
        resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        try {
            ContactCustResult ccrResult = contactCustResultRepository.findByContactCustResultId(contactCustResultDTO.getContactCustResultId());
            if (ccrResult != null && ccrResult.getContactCustResultId() != null) {
                ccrResult.setCallId(contactCustResultDTO.getCallId());
                ccrResult.setStartCall(contactCustResultDTO.getStartCallL() == null || contactCustResultDTO.getStartCallL() == 0 ? null :
                        TimeZoneUtils.changeTimeZone(new Date(contactCustResultDTO.getStartCallL() * 1000L), 0L));
                ccrResult.setEndTime(contactCustResultDTO.getEndTimeL() == null ? null :
                        TimeZoneUtils.changeTimeZone(new Date(contactCustResultDTO.getEndTimeL() * 1000L), 0L));
                ccrResult.setWaitTime(contactCustResultDTO.getWaitTime());
                ccrResult.setDurationCall(contactCustResultDTO.getDurationCall());
                ccrResult.setUpdateBy(userSession.getUserId());
                ccrResult.setUpdateTime(new Date());
                ccrResult.setPreEndTime(ccrRepository.getMaxCreateTime(ccrResult.getCampaignId(), ccrResult.getAgentId()));
                contactCustResultRepository.save(ccrResult);
                resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        logger.info("Response of updateCCRAfterCallEnd::" + resultDTO.getDescription());
        return resultDTO;
    }

    @Override
    public ResultDTO updateCCRToStatus0(Long receiveCustLogId, UserSession userSession) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
        resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        try {
            List<ContactCustResult> custResultList = ccrRepository.findByReceiveCustLogIdAndStatusNotIn(receiveCustLogId, (short) 0);
            if (custResultList != null) {
                for (ContactCustResult item : custResultList) {
                    item.setStatus((short) 0);
                    item.setUpdateBy(userSession.getUserId());
                    item.setUpdateTime(new Date());
                }
                ccrRepository.saveAll(custResultList);
                logger.info("Response of updateCCRToStatus0::" + custResultList.size());
            }
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
    @Transactional(value = DataSourceQualify.ACD_FULL)
    public ResultDTO checkAgentLogout(String agentId) {
        logger.info("--- Request to checkAgentLogout: " + agentId);
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
        resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        try {
            Agents agent = agentsRepository.findByCampaignSystemStatusAndAgentId("LOGOUT", agentId);
            if (agent != null && agent.getAgentId() != null) {
                resultDTO.setData(agent);
            }
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return resultDTO;
    }
}
