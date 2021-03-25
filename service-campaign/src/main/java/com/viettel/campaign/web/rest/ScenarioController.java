package com.viettel.campaign.web.rest;

import com.viettel.campaign.model.ccms_full.ContactQuestResult;
import com.viettel.campaign.model.ccms_full.Scenario;
import com.viettel.campaign.service.ScenarioService;
import com.viettel.campaign.utils.BundleUtils;
import com.viettel.campaign.utils.Constants;
import com.viettel.campaign.utils.DataUtil;
import com.viettel.campaign.utils.RedisUtil;
import com.viettel.campaign.web.dto.ContactQuestResultDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.ScenarioDTO;
import com.viettel.econtact.filter.UserSession;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author anhvd_itsol
 */

@RestController
@RequestMapping("/ipcc/campaign/scenario")
@CrossOrigin(origins = "*")
public class ScenarioController {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioController.class);

    @Autowired
    ScenarioService scenarioService;

    @RequestMapping(value = "/findOneByCampaignIdAndCompanyId", method = RequestMethod.GET)
    public Scenario findOneByCampaignIdAndCompanyId(@RequestParam Long campaignId, @RequestParam Long companySiteId) {
        return scenarioService.findScenarioByCampaignIdAndCompanySiteId(campaignId, companySiteId);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<ResultDTO> update(@RequestBody ScenarioDTO scenario) {
        ResultDTO resultDTO = scenarioService.update(scenario);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/sort-question-answer", method = RequestMethod.POST)
    public ResponseEntity<ResultDTO> sortQuestionAndAnswer(@RequestBody ScenarioDTO scenarioDTO) {
        ResultDTO resultDTO = scenarioService.sortQuestionAndAnswer(scenarioDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/count-duplicate-code", method = RequestMethod.GET)
    public ResponseEntity<Integer> countDuplicateCode(@RequestParam String code, @RequestParam Long scenarioId, @RequestParam Long companySiteId) {
        Integer count = scenarioService.countDuplicateScenarioCode(companySiteId, code, scenarioId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @RequestMapping(value = "/saveContactQuestResult", method = RequestMethod.POST)
    public ResponseEntity<ResultDTO> saveContactQuestResult(@RequestBody ContactQuestResultDTO dto) {
        ResultDTO resultDTO = scenarioService.saveContacQuestResult(dto);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/download-file-template", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFileTemplate(@RequestParam String language, HttpServletResponse response) {
        XSSFWorkbook workbook = null;
        byte[] contentReturn = null;
        try {
            String fileName = "import_scenario_template.xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            OutputStream outputStream;
            workbook = scenarioService.buildTemplate(language);
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            contentReturn = byteArrayOutputStream.toByteArray();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<byte[]>(null, null, HttpStatus.BAD_REQUEST);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        return new ResponseEntity<byte[]>(contentReturn, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/import-file", method = RequestMethod.POST)
    public ResponseEntity<?> importFile(@RequestParam MultipartFile file, @RequestParam Long scenarioId, @RequestParam Long campaignId,
                                        @RequestParam String language, HttpServletRequest request) {
        logger.info("------------IMPORT FILE TEMPLATE--------------");
        Locale locale = Locale.forLanguageTag(language);
        try {
            UserSession userSession = (UserSession) RedisUtil.getInstance().get(request.getHeader("X-Auth-Token"));
            if (file.isEmpty()) {
                return new ResponseEntity<>(BundleUtils.getLangString("common.fileNotSelected"), HttpStatus.OK);
            }
            if (!Objects.equals(FilenameUtils.getExtension(file.getOriginalFilename()), Constants.FileType.xlsx)) {
                return new ResponseEntity<>(BundleUtils.getLangString("common.fileInvalidFormat", locale), HttpStatus.OK);
            }
            String path = saveUploadFile(file);
            Map<String, Object> map = scenarioService.readAndValidateScenario(path, scenarioId, campaignId, userSession.getCompanySiteId(), language);
            String code = (String) map.get("code");
            byte[] content = (byte[]) map.get("content");
            return ResponseEntity.ok()
                    .header("Message", code)
                    .body(content);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private String saveUploadFile(MultipartFile file) {
        try {
            String currentTime = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
            String fileName = FilenameUtils.getBaseName(file.getOriginalFilename()) + "_" + currentTime + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            byte[] content = file.getBytes();
            File uploadFolder = new File(System.getProperty("user.dir") + File.separator + "etc" + File.separator + "upload");
            if (!uploadFolder.exists()) {
                uploadFolder.mkdir();
            }
            Path path = Paths.get(System.getProperty("user.dir") + File.separator + "etc" + File.separator + "upload", fileName);
            Files.write(path, content);
            return path.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @RequestMapping(value = "/getContactQuestResult", method = RequestMethod.GET)
    public List<ContactQuestResult> getContactQuestResult(@RequestParam Long companySiteId, @RequestParam Long campaignId, @RequestParam Long customerId) {
        return scenarioService.getContactQuestResult(companySiteId, campaignId, customerId);
    }

    //function to get full scenario data scenario + lst questions (all status) + lst answers (all status)
    @RequestMapping(value = "/getScenarioData", method = RequestMethod.GET)
    public ResponseEntity<ScenarioDTO> getScenarioData(@RequestParam Long campaignId, HttpServletRequest request) {
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(request.getHeader("X-Auth-Token"));
        ScenarioDTO scenarioDTO = scenarioService.getScenarioData(campaignId, userSession.getCompanySiteId());
        return new ResponseEntity<>(scenarioDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/getOldScenarioData", method = RequestMethod.GET)
    public ResponseEntity<ScenarioDTO> getOldScenarioData(@RequestParam Long campaignId, @RequestParam Long customerId, @RequestParam Long contactCusResultId, HttpServletRequest request) {
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(request.getHeader("X-Auth-Token"));

        ScenarioDTO scenarioDTO = scenarioService.getOldScenarioData(campaignId, customerId, userSession.getCompanySiteId(), contactCusResultId);
        return new ResponseEntity<>(scenarioDTO, HttpStatus.OK);
    }

}
