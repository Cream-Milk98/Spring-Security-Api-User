package com.viettel.campaign.web.rest;

import com.viettel.campaign.model.ccms_full.TimeRangeDialMode;
import com.viettel.campaign.model.ccms_full.TimeZoneDialMode;
import com.viettel.campaign.service.CampaignService;
import com.viettel.campaign.web.dto.CampaignDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.request_dto.CampaignRequestDTO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/ipcc/campaign")
@CrossOrigin
public class CampaignController {

    private static final Logger logger = LoggerFactory.getLogger(CampaignController.class);

    @Autowired
    CampaignService campaignService;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<ResultDTO> search(@RequestBody CampaignRequestDTO requestDto) {
        ResultDTO result = campaignService.search(requestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/find-by-campaign-code", method = RequestMethod.POST)
    public ResultDTO findByCampaignCode(@RequestBody CampaignRequestDTO dto) {
        return campaignService.findByCampaignCode(dto);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResultDTO addNewCampaign(@RequestBody CampaignDTO dto) throws ParseException {
        return campaignService.addNewCampaign(dto);
    }

    @GetMapping("/findByCampaignId")
    @ResponseBody
    public ResponseEntity findByCampaignId(@RequestParam("campaignId") Long campaignId, @RequestParam("offSet") Integer offSet) {
        ResultDTO result = campaignService.findByCampaignId(campaignId, offSet);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findCampaignTimeRangeMode")
    @ResponseBody
    public ResponseEntity<?> findCampaignTimeRangeMode(@RequestParam("campaignId") Long campaignId,
                                                       @RequestParam("companySiteId") Long companySiteId,
                                                       @RequestParam("timezoneOffset") Integer timezoneOffset) {
        List<TimeRangeDialMode> list = campaignService.getCampaignTimeRangeMode(campaignId, companySiteId, timezoneOffset);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/findCampaignTimeZoneMode")
    @ResponseBody
    public ResponseEntity<?> findCampaignTimeZoneMode(@RequestParam("campaignId") Long campaignId, @RequestParam("companySiteId") Long companySiteId) {
        List<TimeZoneDialMode> list = campaignService.getCampaignTimeZoneMode(campaignId, companySiteId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/changeCampaignStatus", method = RequestMethod.POST)
    public ResultDTO changeCampaignStatus(@RequestBody CampaignDTO dto, HttpServletRequest request) {
        dto.setSessionId(request.getSession().getId());
        return campaignService.changeCampaignStatus(dto);
    }

    @RequestMapping(value = "/check-allow-status-to-prepare", method = RequestMethod.GET)
    public ResultDTO checkAllowStatusToPrepare(@RequestParam("campaignId") Long campaignId) {
        return campaignService.checkAllowStatusToPrepare(campaignId);
    }

    @RequestMapping(value = "/exportCampaigns", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> exportCampaigns(HttpServletResponse response, @RequestBody CampaignRequestDTO dto) {
        XSSFWorkbook book = null;
        byte[] contentReturn = null;
        try {
            String fileName = "report_campaigns.xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            book = campaignService.exportCampaigns(dto);
            OutputStream output = response.getOutputStream();
            book.write(output);
            output.flush();
            output.close();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            book.write(byteArrayOutputStream);
            contentReturn = byteArrayOutputStream.toByteArray();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<byte[]>(null, null, HttpStatus.BAD_REQUEST);
        } finally {
            if (book != null) {
                try {
                    book.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        return new ResponseEntity<byte[]>(contentReturn, headers, HttpStatus.OK);
    }

    @PostMapping("/updateCampaign")
    @ResponseBody
    public ResponseEntity<?> updateCampaign(@RequestBody CampaignDTO dto) throws ParseException {
        ResultDTO resultDTO = campaignService.updateCampaign(dto);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/findCustomerListReallocation")
    @ResponseBody
    public ResponseEntity<?> findCustomerListReallocation(@RequestBody CampaignRequestDTO dto) {
        ResultDTO result = campaignService.findCustomerListReallocation(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/reallocationCustomer")
    @ResponseBody
    public ResponseEntity<?> reallocationCustomer(@RequestBody CampaignRequestDTO dto) {
        ResultDTO result = campaignService.reallocationCustomer(dto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/getListFieldsNotShow")
    @ResponseBody
    public ResponseEntity<?> getListFieldsNotShow(@RequestBody CampaignRequestDTO dto) {
        ResultDTO resultDTO = campaignService.getListFieldsNotShow(dto);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/getListFieldsToShow")
    @ResponseBody
    public ResponseEntity<?> getListFieldsToShow(@RequestBody CampaignRequestDTO dto) {
        ResultDTO resultDTO = campaignService.getListFieldsToShow(dto);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/getCampaignCustomerList")
    @ResponseBody
    public ResponseEntity getCampaignCustomerList(@RequestBody CampaignRequestDTO campaignRequestDTO) {
        ResultDTO resultDTO = campaignService.getCampaignCustomerList(campaignRequestDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/getCustomerList")
    @ResponseBody
    public ResponseEntity getCustomerList(@RequestBody CampaignRequestDTO campaignRequestDTO) {
        ResultDTO resultDTO = campaignService.getCustomerList(campaignRequestDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/getCustomerChoosenList")
    @ResponseBody
    public ResponseEntity getCustomerChoosenList(@RequestBody CampaignRequestDTO campaignRequestDTO) {
        ResultDTO resultDTO = campaignService.getCustomerChoosenList(campaignRequestDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);

    }

    @PostMapping("/addCustomerListToCampaign")
    @ResponseBody
    public ResponseEntity addCustomerListToCampaign(@RequestBody CampaignRequestDTO campaignRequestDTO) {
        ResultDTO resultDTO = campaignService.addCustomerListToCampaign(campaignRequestDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/deleteCustomerListFromCampaign")
    @ResponseBody
    public ResponseEntity deleteCustomerListFromCampaign(@RequestBody CampaignRequestDTO campaignRequestDTO) {
        ResultDTO resultDTO = campaignService.deleteCustomerListFromCampaign(campaignRequestDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/saveFieldCustomer")
    @ResponseBody
    public ResponseEntity saveFieldCustomer(@RequestBody CampaignRequestDTO campaignRequestDTO) {
        ResultDTO resultDTO = campaignService.saveFieldCustomer(campaignRequestDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/getCampaignCustomerInformation")
    @ResponseBody
    public ResponseEntity getCampaignCustomerInformation(@RequestBody CampaignRequestDTO campaignRequestDTO) {
        campaignRequestDTO.setCustomerListId(null);
        ResultDTO resultDTO = campaignService.getCampaignCustomerInformation(campaignRequestDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/getCustomerListInformation")
    @ResponseBody
    public ResponseEntity getCustomerListInformation(@RequestBody CampaignRequestDTO campaignRequestDTO) {
        ResultDTO resultDTO = campaignService.getCustomerListInformation(campaignRequestDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/getCountIndividualOnList")
    @ResponseBody
    public ResponseEntity getCountIndividualOnList(@RequestBody CampaignRequestDTO campaignRequestDTO) {
        ResultDTO resultDTO = campaignService.getCountIndividualOnList(campaignRequestDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/saveCustomerCampaign")
    @ResponseBody
    public ResponseEntity saveCustomerCampaign(@RequestBody CampaignRequestDTO campaignRequestDTO) {
        ResultDTO resultDTO = campaignService.saveCustomerCampaign(campaignRequestDTO);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/renewCampaign", method = RequestMethod.PUT)
    public ResponseEntity<ResultDTO> renewCampaign(@RequestBody CampaignDTO campaignDTO) {
        ResultDTO result = campaignService.renewCampaign(campaignDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getConnectStatus")
    @ResponseBody
    public ResponseEntity<ResultDTO> getConnectStatus(@RequestParam("companySiteId") Long companySiteId) {
        ResultDTO resultDTO = campaignService.getConnectStatus(companySiteId);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

}
