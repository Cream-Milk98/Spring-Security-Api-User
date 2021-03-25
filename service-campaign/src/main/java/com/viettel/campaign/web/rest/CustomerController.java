package com.viettel.campaign.web.rest;

import com.viettel.campaign.model.ccms_full.Customer;
import com.viettel.campaign.model.ccms_full.CustomizeFieldObject;
import com.viettel.campaign.model.ccms_full.CustomizeFields;
import com.viettel.campaign.repository.ccms_full.CustomerQueryRepository;
import com.viettel.campaign.service.CustomerService;
import com.viettel.campaign.utils.Config;
import com.viettel.campaign.utils.RedisUtil;
import com.viettel.campaign.web.dto.*;
import com.viettel.campaign.web.dto.request_dto.CustomerRequestDTO;
import com.viettel.campaign.web.dto.request_dto.SearchCustomerListRequestDTO;
import com.viettel.campaign.web.dto.request_dto.SearchCustomerRequestDTO;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ipcc/customer")
@CrossOrigin("*")
public class CustomerController {
    private CustomerQueryRepository customerQueryRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired(required = true)
    CustomerService customerService;

    @GetMapping("/findCustomerId")
    @ResponseBody
    public ResponseEntity<ResultDTO> findAllCustomerName(@RequestParam("customerId") Long customerId) {
        ResultDTO result = customerService.getCustomerId(customerId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/searchAllCustomerByParams")
    @ResponseBody
    public ResponseEntity findAllCustomerByParams(@RequestBody SearchCustomerRequestDTO searchCustomerRequestDTO) {
        ResultDTO result = customerService.searchAllCustomer(searchCustomerRequestDTO);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public ResultDTO createCustomer(@RequestBody @Valid CustomerDTO customerDTO) {
        ResultDTO result = customerService.createCustomer(customerDTO);
        return result;
    }

    @PostMapping("/deleteIds")
    @ResponseBody
    public ResultDTO deleteIds(@RequestBody @Valid CustomerRequestDTO customerRequestDTO) {
        ResultDTO result = customerService.deleteIds(customerRequestDTO);
        return result;
    }

    @GetMapping("/getInformation")
    @ResponseBody
    public ResponseEntity getAllInformation(@RequestParam("companySiteId") Long companySiteId, @RequestParam("customerId") Long customerId, @RequestParam("timezoneOffset") Long timezoneOffset) {
        ResultDTO result = customerService.getCustomerDetailById(companySiteId, customerId, timezoneOffset);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    // --------------- customer list table ----------------- //
    @PostMapping("/createCustomerList")
    @ResponseBody
    public ResultDTO createCustomerList(@RequestBody @Valid CustomerListDTO customerListDTO, HttpServletRequest request) {
        // THÍM NÀO MERGE CONFLICT THÌ GIỮ LẠI HỘ E CÁI METHOD NÀY VỚI
        // VIẾT ĐI VIẾT LẠI 4 LẦN RỒI ĐẤY
        String xAuthToken = request.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);
        return customerService.createCustomerList(customerListDTO, userSession.getUserName());
    }

    @PostMapping("/updateCustomerList")
    @ResponseBody
    public ResultDTO updateCustomerList(@RequestBody @Valid CustomerListDTO customerListDTO, HttpServletRequest request) {
        String xAuthToken = request.getHeader("X-Auth-Token");
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(xAuthToken);
        if (userSession == null) {
            userSession = new UserSession();
            userSession.setSiteId(customerListDTO.getCompanySiteId());
            userSession.setUserName("its4");
        }
        return customerService.updateCustomerList(customerListDTO, userSession.getUserName());
    }

    @PostMapping("/deleteCustomerListIds")
    @ResponseBody
    public ResultDTO deleteCustomerListIds(@RequestBody @Valid CustomerRequestDTO customerRequestDTO) {
        ResultDTO result = customerService.deleteCustomerListIds(customerRequestDTO);
        return result;
    }

    @RequestMapping(value = "/searchCustomerList", method = RequestMethod.POST)
    public ResponseEntity searchCustomerList(@RequestBody SearchCustomerListRequestDTO searchCustomerListRequestDTO) {
        ResultDTO result = customerService.searchCustomerList(searchCustomerListRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/findCustomerContact")
    @ResponseBody
    public ResponseEntity<ResultDTO> findAllCustomerContact(@RequestBody CustomerContactDTO customerContactDTO) {
        ResultDTO result = customerService.getCustomerContact(customerContactDTO);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/getCustomerRecall")
    @ResponseBody
    public ResponseEntity<ResultDTO> getCustomerRecall(@RequestParam("campaignId") Long campaignId, @RequestParam("customerId") Long customerId) {
        ResultDTO result = customerService.getCustomerRecall(campaignId, customerId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    //<editor-fold desc="Download and import excel" defaultstate="collapsed">
    //@PostMapping(value = "/downloadFileTemplate")
    @RequestMapping(value = "/downloadFileTemplate", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFileTemplate(@RequestParam("companySiteId") Long companySiteId,
                                                       @RequestParam("language") String language,
                                                       HttpServletResponse response) {
        LOGGER.info("--------DOWNLOAD FILE TEMPLATE---------");
        XSSFWorkbook workbook = null;
        byte[] contentReturn = null;
        try {
            String fileName = "import_scenario_template.xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            OutputStream outputStream;
            workbook = customerService.buildTemplate(companySiteId, language);
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            contentReturn = byteArrayOutputStream.toByteArray();

//            byte[] content = customerService.buildTemplate(companySiteId, language);
//            return ResponseEntity.ok()
//                    // 2 dòng này không hiểu sao lại không hoạt động nữa
////                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template_import_customer.xlsx")
////                    .header("Content-Type", Constants.MIME_TYPE.EXCEL_XLSX)
//                    .body(content);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new ResponseEntity<byte[]>(null, null, HttpStatus.BAD_REQUEST);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        return new ResponseEntity<byte[]>(contentReturn, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/importFile")
    public ResponseEntity<?> importFile(@RequestPart("file") MultipartFile file,
                                        @RequestPart("customerListId") String customerListId,
                                        @RequestPart("language") String language,
                                        HttpServletRequest request) {
        LOGGER.info("------------IMPORT FILE TEMPLATE--------------");
        try {
            UserSession userSession = (UserSession) RedisUtil.getInstance().get(request.getHeader("X-Auth-Token"));
            if (file.isEmpty()) {
                return new ResponseEntity<>("file-empty", HttpStatus.NO_CONTENT);
            }
            String path = saveUploadFile(file);
            List<CustomizeFields> dynamicHeaders = customerService.getDynamicHeader(userSession.getCompanySiteId());
            Map<String, Object> map = customerService.readAndValidateCustomer(path, dynamicHeaders, userSession, Long.parseLong(customerListId), language);
            String message = (String) map.get("message");
            byte[] content = (byte[]) map.get("content");
            return ResponseEntity.ok()
                    .header("Message", message)
                    .body(content);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    //</editor-fold>

    @PostMapping("/getCustomerListInfo")
    public ResponseEntity<?> getCustomerListInfo(@RequestBody CampaignCustomerDTO dto) {
        List<CustomerListDTO> customers = customerService.getCustomerListInfo(dto);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PostMapping("/getIndividualCustomerInfo")
    public ResponseEntity<ResultDTO> getIndividualCustomerInfo(@RequestBody CampaignCustomerDTO dto) {
        ResultDTO result = customerService.getIndividualCustomerInfo(dto);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/getCustomizeField")
    @ResponseBody
    public ResponseEntity getCustomizeField(@RequestParam("companySiteId") Long companySiteId, @RequestParam("customerId") Long customerId, @RequestParam("timezoneOffset") Long timezoneOffset) {
        ResultDTO result = customerService.getCustomizeField(companySiteId, customerId, timezoneOffset);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/deleteCustomerFromCampaign")
    @ResponseBody
    public ResponseEntity<?> deleteCustomerFromCampaign(@RequestBody CampaignCustomerDTO dto) {
        ResultDTO resultDTO = customerService.deleteCustomerFromCampaign(dto);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/searchCampaignInformation")
    @ResponseBody
    public ResponseEntity<?> searchCampaignInformation(@RequestBody CampaignCustomerDTO dto) {
        ResultDTO resultDTO = customerService.searchCampaignInformation(dto);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/addCustomerToCampaign")
    @ResponseBody
    public ResponseEntity<?> addCustomerToCampaign(@RequestBody CampaignCustomerDTO dto) {
        ResultDTO resultDTO = customerService.addCustomerToCampaign(dto);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/getDataForCombobox")
    @ResponseBody
    public ResponseEntity<?> getDataForCombobox(@RequestBody CampaignCustomerDTO dto) {
        ResultDTO resultDTO = customerService.getDataForCombobox(dto);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }


    private String saveUploadFile(MultipartFile file) {
        try {
            String currentTime = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
            String fileName = FilenameUtils.getBaseName(file.getOriginalFilename()) + "_" + currentTime + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            byte[] content = file.getBytes();
            File uploadFolder = new File(Config.EXCEL_DIR);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdir();
            }
            Path path = Paths.get(Config.EXCEL_DIR, fileName);
            Files.write(path, content);
            return path.toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    //    @GetMapping("/query")
//    public ResponseEntity<List<Customer>> query(@RequestParam(value = "search") String query) {
//        List<Customer> things = customerQueryRepo.findAll(query);
//        if (things.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(things);
//    }
    @GetMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Customer>> query(@RequestParam(value = "search") String query) {
        List<Customer> result = null;
        try {
            result = customerService.searchByQuery(query);
        } catch (IllegalArgumentException iae) {
            LOGGER.error(iae.getMessage(), iae);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(result);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/getCustomizeFields")
    @ResponseBody
    public ResponseEntity<?> getListCustomer(@RequestBody CustomizeFieldsDTO customizeRequestDTo) {
        ResultDTO resultDTO = customerService.listCustomizeFields(customizeRequestDTo);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/searchIndividualCustomer")
    @ResponseBody
    public ResponseEntity searchCustomizeFields(@RequestBody CampaignCustomerDTO campaignCustomerDTO, HttpServletRequest request) {
        UserSession userSession = (UserSession) RedisUtil.getInstance().get(request.getHeader("X-Auth-Token"));
        ResultDTO result = customerService.searchCustomizeFields(campaignCustomerDTO, userSession);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
