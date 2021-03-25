package com.viettel.campaign.service.impl;

import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.model.ccms_full.ContactQuestResult;
import com.viettel.campaign.model.ccms_full.Scenario;
import com.viettel.campaign.model.ccms_full.ScenarioAnswer;
import com.viettel.campaign.model.ccms_full.ScenarioQuestion;
import com.viettel.campaign.repository.ccms_full.*;
import com.viettel.campaign.service.ScenarioQuestionService;
import com.viettel.campaign.service.ScenarioService;
import com.viettel.campaign.utils.*;
import com.viettel.campaign.web.dto.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author anhvd_itsol
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class ScenarioServiceImpl implements ScenarioService {
    private static final Logger logger = LoggerFactory.getLogger(ScenarioServiceImpl.class);

    @Autowired
    ScenarioRepository scenarioRepository;

    @Autowired
    ScenarioQuestionRepository questionRepository;

    @Autowired
    ScenarioAnswerRepository answerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ContactQuestResultRepository questResultRepository;

    @Autowired
    ScenarioQuestionService questionService;

    @Autowired
    ContactCustResultRepository contactCustResultRepository;

    @Autowired
    ScenarioQuestionRepositoryCustom questionRepositoryCustom;

    @Autowired
    ScenarioAnswerRepositoryCustom answerRepositoryCustom;

    @Autowired
    ScenarioRepositoryCustom scenarioRepositoryCustom;

    @Autowired
    ScenarioQuestionRepositoryCustom scenarioQuestionRepositoryCustom;

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public Scenario findScenarioByCampaignIdAndCompanySiteId(Long campaignId, Long companySiteId) {
        try {
            return scenarioRepository.findScenarioByCampaignIdAndCompanySiteId(campaignId, companySiteId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO update(ScenarioDTO scenarioDTO) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            scenarioDTO.setCode(scenarioDTO.getCode().trim());
            if (scenarioDTO.getDescription() != null) scenarioDTO.setDescription(scenarioDTO.getDescription().trim());
            else scenarioDTO.setDescription("");
            scenarioDTO.setUpdateBy(null);
            scenarioDTO.setUpdateTime(new Date());

            Scenario scenario = modelMapper.map(scenarioDTO, Scenario.class);
            scenarioRepository.save(scenario);
            resultDTO.setData(scenario);
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
    public ResultDTO sortQuestionAndAnswer(ScenarioDTO scenarioDTO) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            List<ScenarioQuestionDTO> lstQuestion = scenarioDTO.getLstQuestion();
            List<ScenarioAnswerDTO> lstAnswer = scenarioDTO.getLstAnswer();
            List<ScenarioQuestion> questionsToSort = new ArrayList<>();
            List<ScenarioAnswer> answersToSort = new ArrayList<>();
            if (lstQuestion.size() > 0) {
                lstQuestion.forEach(item -> {
                    ScenarioQuestion question = questionRepository.findScenarioQuestionByScenarioQuestionId(item.getScenarioQuestionId());
                    if (question != null) {
                        question.setOrderIndex(item.getOrderIndex());
                        questionsToSort.add(question);
                    }
                });
                questionRepository.saveAll(questionsToSort);
            }
            if (lstAnswer.size() > 0) {
                lstAnswer.forEach(item -> {
                    ScenarioAnswer answer = answerRepository.findScenarioAnswerByScenarioAnswerId(item.getScenarioAnswerId());
                    if (answer != null) {
                        answer.setOrderIndex(item.getOrderIndex());
                        answersToSort.add(answer);
                    }
                });
                answerRepository.saveAll(answersToSort);
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
    @Transactional(DataSourceQualify.CCMS_FULL)
    public Integer countDuplicateScenarioCode(Long companySiteId, String code, Long scenarioId) {
        try {
            return scenarioRepository.countDuplicateScenarioCode(companySiteId, code, scenarioId);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    public ResultDTO saveContacQuestResult(ContactQuestResultDTO dto) {
        ResultDTO resultDTO = new ResultDTO();

        if (dto != null) {
            ContactQuestResult cqr = questResultRepository.save(modelMapper.map(dto, ContactQuestResult.class));
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setData(cqr);
        } else {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public XSSFWorkbook buildTemplate(String language) throws IOException {
        Locale locale = Locale.forLanguageTag(language);
        XSSFWorkbook workbook = null;
        try {

            Sheet sheet;
            workbook = new XSSFWorkbook();
            CellStyle styleTitle = WorkBookBuilder.buildDefaultStyleTitle(workbook);
            CellStyle styleRowHeader = WorkBookBuilder.buildDefaultStyleRowHeader(workbook);

            // list header
            List<String> fileHeaderList = new ArrayList<>();
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.questionCode", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.questionType", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.question", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.answer", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.hashInput", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.required", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.default", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.mappingQuestion", locale));
            //
            String sheetName = BundleUtils.getLangString("campaign", locale);
            sheet = workbook.createSheet(sheetName);

            // Title
            String title = BundleUtils.getLangString("scenario.template.title", locale);

            int rowTitleStart = 0;
            Row rowTitle = sheet.createRow(rowTitleStart);
            rowTitle.setHeight((short) 800);
            sheet.addMergedRegion(new CellRangeAddress(rowTitleStart, rowTitleStart, 0, 7));
            WorkBookBuilder.writeCellContent(rowTitle, styleTitle, 0, title);

            // Header
            int startRowTable = 2;
            Row rowHeader = sheet.createRow(startRowTable);
            for (int i = 0; i < fileHeaderList.size(); i++) {
                sheet.setColumnWidth(i, 6000);
                WorkBookBuilder.writeCellContent(rowHeader, styleRowHeader, i, fileHeaderList.get(i));
            }
            DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();

            //QuestionType
            DataValidationConstraint questionType = dataValidationHelper.createExplicitListConstraint(new String[]{BundleUtils.getLangString("scenario.template.singleChoice", locale),
                    BundleUtils.getLangString("scenario.template.multiChoice", locale), BundleUtils.getLangString("scenario.template.text", locale)});
            CellRangeAddressList typeCellRangeAddressList = new CellRangeAddressList(3, 9999, 1, 1);
            DataValidation typeValidation = dataValidationHelper.createValidation(questionType, typeCellRangeAddressList);
            typeValidation.setSuppressDropDownArrow(true);
            typeValidation.setShowErrorBox(true);
            sheet.addValidationData(typeValidation);

            //HashInputCombobox
            DataValidationConstraint hasInput = dataValidationHelper.createExplicitListConstraint(new String[]{BundleUtils.getLangString("scenario.template.yes", locale), BundleUtils.getLangString("scenario.template.no", locale)});
            CellRangeAddressList hasInputCellRangeAddressList = new CellRangeAddressList(3, 9999, 4, 4);
            DataValidation hasInputValidation = dataValidationHelper.createValidation(hasInput, hasInputCellRangeAddressList);
            hasInputValidation.setSuppressDropDownArrow(true);
            hasInputValidation.setShowErrorBox(true);
            sheet.addValidationData(hasInputValidation);

            //Required
            DataValidationConstraint required = dataValidationHelper.createExplicitListConstraint(new String[]{BundleUtils.getLangString("scenario.template.yes", locale), BundleUtils.getLangString("scenario.template.no", locale)});
            CellRangeAddressList requiredCellRangeAddressList = new CellRangeAddressList(3, 9999, 5, 5);
            DataValidation requiredValidation = dataValidationHelper.createValidation(required, requiredCellRangeAddressList);
            requiredValidation.setSuppressDropDownArrow(true);
            requiredValidation.setShowErrorBox(true);
            sheet.addValidationData(requiredValidation);

            //Required
            DataValidationConstraint defaultValue = dataValidationHelper.createExplicitListConstraint(new String[]{BundleUtils.getLangString("scenario.template.yes", locale), BundleUtils.getLangString("scenario.template.no", locale)});
            CellRangeAddressList defaultValueCellRangeAddressList = new CellRangeAddressList(3, 9999, 6, 6);
            DataValidation defaultValueValidation = dataValidationHelper.createValidation(defaultValue, defaultValueCellRangeAddressList);
            defaultValueValidation.setSuppressDropDownArrow(true);
            defaultValueValidation.setShowErrorBox(true);
            sheet.addValidationData(defaultValueValidation);


            return workbook;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            //if (workbook != null) workbook.close();
        }
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL, rollbackFor = Exception.class)
    public Map<String, Object> readAndValidateScenario(String path, Long scenarioId, Long campaignId, Long companySiteId, String language) throws IOException {
        Locale locale = Locale.forLanguageTag(language);
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Map<String, Object> result = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        boolean isValidAnswer = true;
        XSSFWorkbook workbook = null;
        FileInputStream fis = null;
        List<Object[]> rawDataValid = new ArrayList<>();
        List<Object[]> rawDataList = new ArrayList<>();
        boolean isValidQuestion = false;
        boolean isRowNull = false;

        try {
            List<String> fileHeaderList = new ArrayList<>();
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.questionCode", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.questionType", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.question", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.answer", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.hashInput", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.required", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.default", locale));
            fileHeaderList.add(BundleUtils.getLangString("scenario.template.mappingQuestion", locale));

            File file = new File(path);
            fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(2);

            //<editor-fold desc="Tạo style cho cột kết quả" defaultstate="collapsed">
            Font resultFont = workbook.createFont();
            resultFont.setBold(true);
            resultFont.setFontHeightInPoints((short) 11);
            CellStyle resultStyle = workbook.createCellStyle();
            resultStyle.setFont(resultFont);
            resultStyle.setAlignment(HorizontalAlignment.CENTER);
            resultStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            resultStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            resultStyle.setBorderRight(BorderStyle.THIN);
            resultStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            resultStyle.setBorderBottom(BorderStyle.THIN);
            resultStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            resultStyle.setBorderLeft(BorderStyle.THIN);
            resultStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            resultStyle.setBorderTop(BorderStyle.THIN);
            resultStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

            //</editor-fold>

            //<editor-fold desc="Kiểm tra header của template" defaultstate="collapsed">
            for (int i = 0; i < fileHeaderList.size(); i++) {
                Cell cell = row.getCell(i);
                if (!cell.getStringCellValue().equals(fileHeaderList.get(i).split("#")[0])) {
                    result.put("content", Files.readAllBytes(file.toPath()));
                    result.put("code", Constants.FILE_UPLOAD_RESP_CODE.INVALID_FORMAT);
                    return result;
                }
            }
            //</editor-fold>

            //kt file du lieu rong
            if (sheet.getPhysicalNumberOfRows() == 2) {
                result.put("content", Files.readAllBytes(file.toPath()));
                result.put("code", Constants.FILE_UPLOAD_RESP_CODE.EMPTY);
                return result;
            } else {
                Cell resultCell = sheet.getRow(2).createCell(sheet.getRow(2).getPhysicalNumberOfCells());
                resultCell.setCellValue(BundleUtils.getLangString("customer.result", locale));
                resultCell.setCellStyle(resultStyle);
            }

            //validate row
            for (int i = 3; i <= sheet.getPhysicalNumberOfRows(); i++) {
                Row dataRow = sheet.getRow(i);
                if (dataRow != null) {
                    Object[] obj = new Object[row.getPhysicalNumberOfCells()];
                    for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                        Cell dataCell = dataRow.getCell(j);
                        if (dataCell != null) {
                            switch (dataCell.getCellTypeEnum()) {
                                case NUMERIC:
                                    obj[j] = dataCell.getNumericCellValue();
                                    break;
                                default:
                                    obj[j] = dataCell.getStringCellValue();
                                    break;
                            }
                        }
                    }

                    rawDataList.add(obj);
                }
            }

            //<editor-fold desc="Validate dữ liệu" defaultstate="collapsed">
            String selectedType = "";
            List<String> lstImportQuestionCodes = new ArrayList<>();
            String tmpCurrentQuestionCode = null;

            rawDataList.forEach(item -> {
                if (item[0] != null && !"".equals(item[0].toString().trim()))
                    lstImportQuestionCodes.add(item[0].toString().trim());
            });
            for (int i = 0; i < rawDataList.size(); i++) {
                Row dataRow = sheet.getRow(3 + i);
                if (dataRow != null) {
                    //validate question
                    if ((rawDataList.get(i)[0] != null && !"".equals(rawDataList.get(i)[0].toString().trim())) ||
                            (rawDataList.get(i)[1] != null && !"".equals(rawDataList.get(i)[1].toString().trim())) ||
                            (rawDataList.get(i)[2] != null && !"".equals(rawDataList.get(i)[2].toString().trim())) ||
                            (rawDataList.get(i)[5] != null && !"".equals(rawDataList.get(i)[5].toString().trim())) ||
                            (rawDataList.get(i)[6] != null && !"".equals(rawDataList.get(i)[6].toString().trim()))) {
                        isValidQuestion = true;

                        if (rawDataList.get(i)[1] == null || "".equals(rawDataList.get(i)[1].toString().trim())) {
                            sb.append(BundleUtils.getLangString("scenario.questionType.required", locale)).append(" ");
                            isValidQuestion = false;
                        }
                        if (rawDataList.get(i)[2] == null || "".equals(rawDataList.get(i)[2].toString().trim())) {
                            sb.append(BundleUtils.getLangString("scenario.question.required", locale)).append(" ");
                            isValidQuestion = false;
                        }
                        if (rawDataList.get(i)[5] == null || "".equals(rawDataList.get(i)[5].toString().trim())) {
                            Cell requiredCell = dataRow.createCell(5);
                            requiredCell.setCellValue(BundleUtils.getLangString("scenario.template.no", locale));
                        }
                        if (rawDataList.get(i)[6] == null || "".equals(rawDataList.get(i)[6].toString().trim())) {
                            Cell defaultCell = dataRow.createCell(6);
                            defaultCell.setCellValue(BundleUtils.getLangString("scenario.template.yes", locale));
                        }

                        if (rawDataList.get(i)[3] != null && !"".equals(rawDataList.get(i)[3].toString().trim())) {
                            sb.append(BundleUtils.getLangString("scenario.data.invalid", locale)).append(" ");
                            isValidQuestion = false;
                        }
                        if (rawDataList.get(i)[4] != null && !"".equals(rawDataList.get(i)[4].toString().trim())) {
                            sb.append(BundleUtils.getLangString("scenario.data.invalid", locale)).append(" ");
                            isValidQuestion = false;
                        }
                        if (rawDataList.get(i)[7] != null && !"".equals(rawDataList.get(i)[7].toString().trim())) {
                            sb.append(BundleUtils.getLangString("scenario.question.text.mappingquestion.err", locale)).append(" ");
                            isValidQuestion = false;
                        }

                        if (isValidQuestion && !"".equals(DataUtil.safeToString(rawDataList.get(i)[0]).trim()) &&
                                !"".equals(DataUtil.safeToString(rawDataList.get(i)[1]).trim())) {
                            selectedType = rawDataList.get(i)[1].toString().trim();
                            tmpCurrentQuestionCode = rawDataList.get(i)[0].toString().trim();
                        }

                        if (isValidQuestion) rawDataValid.add(rawDataList.get(i));
                    }

                    //validate answer
                    if (isValidQuestion && ((rawDataList.get(i)[3] != null && !"".equals(rawDataList.get(i)[3].toString().trim())) ||
                            (rawDataList.get(i)[4] != null && !"".equals(rawDataList.get(i)[4].toString().trim())) ||
                            (rawDataList.get(i)[7] != null && !"".equals(rawDataList.get(i)[7].toString().trim())))) {
                        isValidAnswer = true;
                        if ((rawDataList.get(i)[3] == null || "".equals(rawDataList.get(i)[3].toString().trim())) &&
                                (!selectedType.equals(BundleUtils.getLangString("scenario.template.text", locale)))) {
                            sb.append(BundleUtils.getLangString("scenario.answer.required", locale)).append(" ");
                            isValidAnswer = false;
                        }
                        if (rawDataList.get(i)[4] == null || "".equals(rawDataList.get(i)[4].toString().trim())) {
                            Cell requiredCell = dataRow.createCell(4);
                            requiredCell.setCellValue(BundleUtils.getLangString("scenario.template.no", locale));
                        }
                        if ((rawDataList.get(i)[7] != null && !"".equals(rawDataList.get(i)[7].toString().trim()))) {
                            if ((rawDataList.get(i)[7].toString().trim().equals(tmpCurrentQuestionCode)) ||
                                    !validateMappingQuestion(rawDataList.get(i)[7].toString().trim(), tmpCurrentQuestionCode, lstImportQuestionCodes)) {
                                sb.append(BundleUtils.getLangString("scenario.mappingQuestion.invalid", locale)).append(" ");
                                isValidAnswer = false;
                            }
                        }

                        if (rawDataList.get(i)[0] != null && !"".equals(rawDataList.get(i)[0].toString().trim())) {
                            sb.append(BundleUtils.getLangString("scenario.data.invalid", locale)).append(" ");
                            isValidAnswer = false;
                        }
                        if (rawDataList.get(i)[1] != null && !"".equals(rawDataList.get(i)[1].toString().trim())) {
                            sb.append(BundleUtils.getLangString("scenario.data.invalid", locale)).append(" ");
                            isValidAnswer = false;
                        }
                        if (rawDataList.get(i)[2] != null && !"".equals(rawDataList.get(i)[2].toString().trim())) {
                            sb.append(BundleUtils.getLangString("scenario.data.invalid", locale)).append(" ");
                            isValidAnswer = false;
                        }
                        if (rawDataList.get(i)[5] != null && !"".equals(rawDataList.get(i)[5].toString().trim())) {
                            sb.append(BundleUtils.getLangString("scenario.data.invalid", locale)).append(" ");
                            isValidAnswer = false;
                        }
                        if (rawDataList.get(i)[6] != null && !"".equals(rawDataList.get(i)[6].toString().trim())) {
                            sb.append(BundleUtils.getLangString("scenario.data.invalid", locale)).append(" ");
                            isValidAnswer = false;
                        }

                        if (isValidAnswer) rawDataValid.add(rawDataList.get(i));
                    }
                } else {
                    isRowNull = true;
                    break;
                }
//                else if (!isValidQuestion && ((rawDataList.get(i)[3] != null && !rawDataList.get(i)[3].toString().trim().equals("")) ||
//                        (rawDataList.get(i)[4] != null && !rawDataList.get(i)[4].toString().trim().equals("")) ||
//                        (rawDataList.get(i)[7] != null && !rawDataList.get(i)[7].toString().trim().equals("")))) {
//                    sb.append(BundleUtils.getLangString("scenario.answer.invalidQuestion")).append(" ");
//                }

                Cell resultCell = dataRow.createCell(row.getPhysicalNumberOfCells() - 1);
                if (sb.length() > 0) {
                    resultCell.setCellValue(sb.toString());
                } else {
                    resultCell.setCellValue(BundleUtils.getLangString("ok", locale));
                }
                sb = new StringBuilder();
            }
            //</editor-fold>

            if (isRowNull) {
                result.put("content", null);
                result.put("code", Constants.FILE_UPLOAD_RESP_CODE.ROW_EMPTY);
                return result;
            }

            List<ScenarioQuestionDTO> lstQuestion = buildQuestionsLst(rawDataValid, scenarioId, campaignId, companySiteId, locale);
            List<ScenarioAnswerDTO> lstAnswerTmp = new ArrayList<>();
            lstQuestion.forEach(question -> {
                lstAnswerTmp.addAll(question.getLstAnswers());
                questionService.add(question);
            });

            //for import mapping question from xls: update mapping question id for answer
            lstAnswerTmp.forEach(item -> {
                if (item.getMappingQuestionCode() != null && item.getMappingQuestionId() == null) {
                    ScenarioQuestion question = questionRepository.findScenarioQuestionByCodeAndCompanySiteIdAndStatus(item.getMappingQuestionCode(), item.getCompanySiteId(), (short) 1);
                    if (question != null) {
                        ScenarioAnswer answerForUpdate = answerRepository.findScenarioAnswerByCodeAndStatus(item.getCode(), (short) 1);
                        if (answerForUpdate != null) {
                            answerForUpdate.setMappingQuestionId(question.getScenarioQuestionId());
                            answerRepository.save(answerForUpdate);
                        }
                    }
                }
            });

            workbook.write(os);
            os.flush();
            os.close();
            workbook.close();
            if (rawDataValid.size() > 0) {
                result.put("content", os.toByteArray());
                result.put("code", Constants.FILE_UPLOAD_RESP_CODE.SUCCESS);
            } else {
                result.put("content", os.toByteArray());
                result.put("code", Constants.FILE_UPLOAD_RESP_CODE.ERROR);
            }
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
            result.put("message", BundleUtils.getLangString("customer.errorValidate", locale));
        } finally {
            if (workbook != null) workbook.close();
            if (fis != null) fis.close();
        }
        return result;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public List<ContactQuestResult> getContactQuestResult(Long companySiteId, Long campaignId, Long customerId) {
        return questResultRepository.findByCompanySiteIdAndCampaignIdAndCustomerId(companySiteId, campaignId, customerId);
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ScenarioDTO getScenarioData(Long campaignId, Long companySiteId) {
        try {
            Scenario scenario = scenarioRepository.findScenarioByCampaignIdAndCompanySiteId(campaignId, companySiteId);
            ScenarioDTO result = modelMapper.map(scenario, ScenarioDTO.class);

            List<ScenarioQuestionDTO> questionsDTO = scenarioQuestionRepositoryCustom.getListQuestions(companySiteId, result.getScenarioId(), (short) 1);
            //List<ScenarioQuestion> questions = questionRepository.findScenarioQuestionsByScenarioIdAndStatusOrderByOrderIndex(result.getScenarioId(), (short) 1);
            //if (questions != null && questions.size() > 0) {
            if (questionsDTO != null && questionsDTO.size() > 0) {
                //questionsDTO.forEach(q -> {
                //ScenarioQuestionDTO dto = modelMapper.map(q, ScenarioQuestionDTO.class);
                for (int i = 0; i < questionsDTO.size(); i++) {
                    ScenarioQuestionDTO dto = questionsDTO.get(i);
                    List<ScenarioAnswerDTO> answerDTOS = new ArrayList<>();
                    List<ScenarioAnswer> answers = answerRepository.findScenarioAnswerByScenarioQuestionIdAndStatusOrderByOrderIndex(dto.getScenarioQuestionId(), (short) 1);
                    if (answers != null && answers.size() > 0) {
                        answers.forEach(a -> {
                            ScenarioAnswerDTO aDto = modelMapper.map(a, ScenarioAnswerDTO.class);
                            answerDTOS.add(aDto);
                        });
                    }
                    dto.setLstAnswers(answerDTOS);
                    //questionsDTO.add(dto);
                    //});
                }
            }
            result.setLstQuestion(questionsDTO);
            return result;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    Long tmpQuestionId;

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ScenarioDTO getOldScenarioData(Long campaignId, Long customerId, Long companySiteId, Long contactCusResultId) {
        logger.info("--- START GET OLD SCENARIO DATA ::");
        tmpQuestionId = null;
        ScenarioDTO result;
        if (DataUtil.isNullOrZero(campaignId) || DataUtil.isNullOrZero(customerId) || DataUtil.isNullOrZero(companySiteId))
            return null;
        try {
            result = modelMapper.map(scenarioRepository.findScenarioByCampaignIdAndCompanySiteId(campaignId, companySiteId), ScenarioDTO.class);
            if (contactCusResultId != null) {
                List<InteractiveResultDTO> lstInteractResult = scenarioRepositoryCustom.getInteractiveResult(contactCusResultId, customerId, campaignId);
                //get questions interacted
                List<ScenarioQuestionDTO> questionsResult = questionRepositoryCustom.getOldQuestionsData(campaignId, customerId, contactCusResultId);
                List<ScenarioQuestionDTO> scenarioQuestions = getScenarioData(campaignId, companySiteId).getLstQuestion();

                if (questionsResult != null) {
                    List<ScenarioQuestionDTO> questionsResultToResponse = new ArrayList<>();
                    questionsResult.forEach(q -> {
                        if (!checkExistQuestionId(questionsResultToResponse, q.getScenarioQuestionId())) {
                            tmpQuestionId = q.getScenarioQuestionId();
                            List<ScenarioAnswerDTO> answers = new ArrayList<>();
                            lstInteractResult.forEach(r -> {
                                if (q.getScenarioQuestionId().equals(r.getScenarioQuestionId())) {
                                    if (q.getType().equals((short) 3)) {
                                        q.setOpinion(r.getOpinion());
                                    }
                                    ScenarioAnswerDTO saDTO = mappingInteractedResultToAnswer(r, q.getScenarioQuestionId());
                                    answers.add(saDTO);
                                }
                            });

                            for (int i = 0; i < scenarioQuestions.size(); i++) {
                                if (scenarioQuestions.get(i).getScenarioQuestionId().equals(q.getScenarioQuestionId())) {
                                    List<ScenarioAnswerDTO> itemAnswers = scenarioQuestions.get(i).getLstAnswers();
                                    List<ScenarioAnswerDTO> inExistAnswers = checkInExist(itemAnswers, answers);
                                    if (inExistAnswers.size() > 0) answers.addAll(inExistAnswers);
                                }
                            }
                            q.setLstAnswers(answers.stream().sorted(Comparator.comparingLong(ScenarioAnswerDTO::getScenarioAnswerId)).collect(Collectors.toList()));
                            questionsResultToResponse.add(q);
                        }
                    });
                    result.setLstQuestion(questionsResultToResponse);

                    List<ScenarioQuestionDTO> rawQuestions = getScenarioData(campaignId, companySiteId).getLstQuestion();
                    List<ScenarioQuestionDTO> rawNewQuestions = new ArrayList<>();

                    for (int i = 0; i < result.getLstQuestion().size(); i++) {
                        ScenarioQuestionDTO q = result.getLstQuestion().get(i);
                        Long qId = q.getScenarioQuestionId();
                        rawNewQuestions = rawQuestions
                                .stream()
                                .filter(rq -> !rq.getScenarioQuestionId().equals(qId))
                                .collect(Collectors.toList());
                        rawQuestions = rawNewQuestions;
                    }

                    if (rawNewQuestions.size() > 0) {
                        rawNewQuestions.forEach(rq -> result.getLstQuestion().add(rq));
                    }
                }
            }
            logger.info("--- END GET OLD SCENARIO DATA ::");
            return result;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    Boolean checkExistQuestionId(List<ScenarioQuestionDTO> lstCheck, Long tmpQuestionId) {
        boolean check = false;
        for (int i = 0; i < lstCheck.size(); i++) {
            if (lstCheck.get(i).getScenarioQuestionId().equals(tmpQuestionId)) {
                check = true;
                break;
            }
        }
        return check;
    }

    List<ScenarioAnswerDTO> checkInExist(List<ScenarioAnswerDTO> checkAnswers, List<ScenarioAnswerDTO> answers) {
        List<ScenarioAnswerDTO> result = new ArrayList<>();
        if (answers.size() > 0 && checkAnswers.size() > 0) {
            checkAnswers.forEach(itemCheck -> {
                int count = 0;
                for (int j = 0; j < answers.size(); j++) {
                    ScenarioAnswerDTO item = answers.get(j);
                    if (itemCheck.getScenarioAnswerId().equals(item.getScenarioAnswerId())) count++;
                }
                if (count == 0) result.add(itemCheck);
            });
        }
        return result;
    }

    private ScenarioAnswerDTO mappingInteractedResultToAnswer(InteractiveResultDTO irDTO, Long scenarioQuestionId) {
        ScenarioAnswerDTO saDTO = new ScenarioAnswerDTO();
        saDTO.setScenarioAnswerId(irDTO.getAnswerId());
        saDTO.setMappingQuestionId(irDTO.getMappingQuestionId());
        saDTO.setOpinion(DataUtil.safeToString(irDTO.getOpinion()));
        if (scenarioQuestionId != null && irDTO.getScenarioQuestionId() != null &&
                scenarioQuestionId.equals(irDTO.getScenarioQuestionId()) &&
                irDTO.getContactCusId() != null) {
            saDTO.setChecked(true);
        }
        saDTO.setAnswer(irDTO.getAnswer());
        saDTO.setHasInput(irDTO.getAnswerHashInput());
        saDTO.setScenarioQuestionId(irDTO.getScenarioQuestionId());
        return saDTO;
    }

    private boolean validateMappingQuestion(String mappingQuestion, String currentQuestionCode, List<String> lstQuestionCode) {
        if (mappingQuestion != null && mappingQuestion.equals(currentQuestionCode)) return false;

        String existMappingQuestionCode = lstQuestionCode.stream().
                filter(p -> (p.equals(mappingQuestion) && !p.equals(currentQuestionCode))).
                findAny().orElse(null);
        if (existMappingQuestionCode == null) return false;

        return true;
    }


    private Long questionOrderIndex = null;
    private Integer answerIndex = null;

    private List<ScenarioQuestionDTO> buildQuestionsLst(List<Object[]> rawDatas, Long scenarioId, Long campaignId, Long companySiteId, Locale locale) {
        List<ScenarioQuestionDTO> lstQuestions = new ArrayList<>();
        List<ScenarioAnswerDTO> lstAnswerOfQuestion = new ArrayList<>();
        ScenarioQuestionDTO questionDTO = null;
        String questionCode = null;
        for (int i = 0; i < rawDatas.size(); i++) {
            if ((rawDatas.get(i)[1] != null && !"".equals(rawDatas.get(i)[1].toString().trim()))) {
                questionCode = DataUtil.safeToString(rawDatas.get(i)[0]).trim() != "" ? (DataUtil.safeToString(rawDatas.get(i)[0]).trim()) : (DateTimeUtil.currentTimeMillis() + i + "");
                questionDTO = new ScenarioQuestionDTO();
                questionDTO.setScenarioId(scenarioId);
                questionDTO.setCampaignId(campaignId);
                questionDTO.setCompanySiteId(companySiteId);
                questionDTO.setImportCode(questionCode);

                if (rawDatas.get(i)[1].toString().trim().equals(BundleUtils.getLangString("scenario.template.text", locale)))
                    questionDTO.setType((short) 3);
                else if (rawDatas.get(i)[1].toString().trim().equals(BundleUtils.getLangString("scenario.template.singleChoice", locale)))
                    questionDTO.setType((short) 1);
                else questionDTO.setType((short) 2);

                questionDTO.setQuestion(rawDatas.get(i)[2].toString().trim());

                if (DataUtil.safeToString(rawDatas.get(i)[5]).trim().equals(BundleUtils.getLangString("scenario.template.yes", locale)))
                    questionDTO.setIsRequire((short) 1);
                else questionDTO.setIsRequire((short) 0);

                questionDTO.setIsDefault((short) 1);
                if (DataUtil.safeToString(rawDatas.get(i)[6]).trim().equals(BundleUtils.getLangString("scenario.template.no", locale)))
                    questionDTO.setIsDefault((short) 0);

                lstQuestions.add(questionDTO);
            } else {
                ScenarioAnswerDTO answerDto = new ScenarioAnswerDTO();
                answerDto.setAnswer(rawDatas.get(i)[3].toString().trim());
                if (DataUtil.safeToString(rawDatas.get(i)[4]).trim().equals(BundleUtils.getLangString("scenario.template.yes", locale)))
                    answerDto.setHasInput((short) 1);
                else answerDto.setHasInput((short) 0);
                if (rawDatas.get(i)[7] != null && !"".equals(rawDatas.get(i)[7].toString().trim())) {
                    answerDto.setMappingQuestionCode(rawDatas.get(i)[7].toString().trim());
                }
                answerDto.setImportQuestionCode(questionCode);
                answerDto.setCompanySiteId(companySiteId);

                lstAnswerOfQuestion.add(answerDto);
            }
        }

        questionOrderIndex = questionRepository.getMaxOrderId(scenarioId, campaignId, companySiteId);

        if (questionOrderIndex == null) questionOrderIndex = 0L;
        lstQuestions.forEach(q -> {
            questionOrderIndex += 1;
            q.setOrderIndex(questionOrderIndex);
            q.setCode(campaignId + "_" + questionOrderIndex);
            answerIndex = 0;
            List<ScenarioAnswerDTO> answers = new ArrayList<>();
            lstAnswerOfQuestion.forEach(a -> {
                if (a.getImportQuestionCode().equals(q.getImportCode())) {
                    answerIndex += 1;
                    a.setOrderIndex(answerIndex);
                    answers.add(a);
                }
            });
            q.setLstAnswers(answers);
        });

        questionOrderIndex = null;

        lstQuestions.forEach(q2 -> {
            List<ScenarioAnswerDTO> lstAnswers = q2.getLstAnswers();
            lstAnswers.forEach(a2 -> {
                a2.setCode(q2.getCode() + "_" + a2.getOrderIndex());
                ScenarioQuestionDTO mappingQuestion = lstQuestions.stream().filter(mq -> mq.getImportCode().equals(a2.getMappingQuestionCode())).findFirst().orElse(null);
                if (mappingQuestion != null) {
                    a2.setMappingQuestionCode(mappingQuestion.getCode());
                }
            });
        });

        //neu cau hoi la cau hoi lien ket => set lai isDefault = 0
        lstQuestions.forEach(ques -> {
            for (int i = 0; i < lstAnswerOfQuestion.size(); i++) {
                ScenarioAnswerDTO ans = lstAnswerOfQuestion.get(i);
                if (ques.getCode().equals(ans.getMappingQuestionCode())) {
                    ques.setIsDefault((short) 0);
                    break;
                }
            }
        });
        questionOrderIndex = null;
        return lstQuestions;
    }

}
