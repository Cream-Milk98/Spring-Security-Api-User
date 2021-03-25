package com.viettel.campaign.service.impl;

import com.github.tennaito.rsql.jpa.JpaCriteriaCountQueryVisitor;
import com.github.tennaito.rsql.jpa.JpaCriteriaQueryVisitor;
import com.viettel.campaign.config.DataSourceQualify;
import com.viettel.campaign.mapper.CustomerMapper;
import com.viettel.campaign.mapper.PhoneNumberRankMapper;
import com.viettel.campaign.model.ccms_full.*;
import com.viettel.campaign.repository.ccms_full.*;
import com.viettel.campaign.service.CustomerService;
import com.viettel.campaign.utils.*;
import com.viettel.campaign.web.dto.*;
import com.viettel.campaign.web.dto.request_dto.CustomerRequestDTO;
import com.viettel.campaign.web.dto.request_dto.SearchCustomerListRequestDTO;
import com.viettel.campaign.web.dto.request_dto.SearchCustomerRequestDTO;
import com.viettel.econtact.filter.UserSession;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.viettel.campaign.utils.Config.maxRecordImport;
import static com.viettel.campaign.utils.Config.phoneNumberStart;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    //    private static final Pattern EMAIL_REGEXP = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern EMAIL_REGEXP = Pattern.compile("^(.+)@(.+)$", Pattern.CASE_INSENSITIVE);

    private List<CustomerContact> phoneNumbersPrimary = new ArrayList<>();
    private List<CustomerContact> phoneNumbers = new ArrayList<>();
    private List<CustomerContact> emails = new ArrayList<>();
    private List<CustomizeFieldObject> customizeFieldObjects = new ArrayList<>();
    private List<CustomerListMapping> customerListMappings = new ArrayList<>();
    private List<PhoneNumberRank> phoneNumberRanks = new ArrayList<>();

    @Autowired
    @PersistenceContext(unitName = DataSourceQualify.JPA_UNIT_NAME_CCMS_FULL)
    EntityManager entityManager;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerListRepository customerListRepository;

    @Autowired
    CustomerContactRepository customerContactRepository;

    @Autowired
    CampaignCustomerListRepository campaignCustomerListRepository;

    @Autowired
    CampaignCustomerRepository campaignCustomerRepository;

    @Autowired
    CustomerListMappingRepository customerListMappingRepository;

    @Autowired
    @Qualifier(DataSourceQualify.NAMED_JDBC_PARAMETER_TEMPLATE_CCMS_FULL)
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    VSAUsersRepository vsaUsersRepository;

    @Autowired
    CustomizeFieldsRepository customizeFieldsRepository;

    @Autowired
    CustomizeFieldObjectRepository customizeFieldObjectRepository;

    @Autowired
    CustomizeFieldOptionValueRepository customizeFieldOptionValueRepository;

    @Autowired
    ApParamRepository apParamRepository;

    @Autowired
    private com.viettel.campaign.repository.ccms_full.PhoneNumberRankRepository phoneNumberRankRepository;

    private PhoneNumberRankMapper phoneNumberRankMapper = new PhoneNumberRankMapper();

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCustomerId(Long customerId) {
        LOGGER.info("--- START GET CUSTOMER BY ID ::");
        ResultDTO resultDTO = new ResultDTO();
        List<CustomerContact> cc = new ArrayList<>();

        try {
            Customer customer = customerRepository.findByCustomerId(customerId);
            cc = customerContactRepository.getLastPhone(customerId, (short) 1, (short) 5, (short) 1);
            if (cc != null && cc.size() > 0)
                customer.setMobileNumber(cc.get(0).getContact());
            cc = customerContactRepository.getLastEmail(customerId, (short) 1, (short) 2);
            if (cc != null && cc.size() > 0)
                customer.setEmail(cc.get(0).getContact());

            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription("customer data");
            //resultDTO.setListData(customer);
            resultDTO.setData(customer);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription("customer data null");
        }

        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO searchAllCustomer(SearchCustomerRequestDTO searchCustomerRequestDTO) {
        LOGGER.info("--- START GET ALL CUSTOMER IN CUSTOMER LIST ::");
        ResultDTO resultDTO = new ResultDTO();

        if (DataUtil.isNullOrZero(searchCustomerRequestDTO.getCompanySiteId())) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            return resultDTO;
        }

        try {
            Page<CustomerCustomDTO> data = customerRepository.getAllCustomerByParams(searchCustomerRequestDTO, SQLBuilder.buildPageable(searchCustomerRequestDTO));

            resultDTO.setTotalRow(data.getTotalElements());
            resultDTO.setListData(data.getContent());
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO createCustomer(CustomerDTO customerDTO) {
        LOGGER.info("--- START CREATE CUSTOMER ::");
        ResultDTO resultDTO = new ResultDTO();
        CustomerMapper customerMapper = new CustomerMapper();
        Customer customer = new Customer();

        try {
            if (customerDTO != null) {
                //check trung mobile number
                Long cusExist = customerRepository.findByMobileNumberAndSiteId(customerDTO.getMobileNumber(), customerDTO.getSiteId());
                if (cusExist > 0) {
                    resultDTO.setErrorCode("-7");
                    resultDTO.setDescription("Mobile number is existed");
                    return resultDTO;
                }

                // insert
                customer = customerMapper.toPersistenceBean(customerDTO);
                customer.setCreateDate(new Date());
                customer = customerRepository.save(customer);

                resultDTO.setErrorCode(customer.getCustomerId() + "");
                resultDTO.setDescription("customer created");
            } else {
                resultDTO.setErrorCode("-2");
                resultDTO.setDescription("customerDTO null");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO deleteIds(CustomerRequestDTO customerRequestDTO) {
        LOGGER.info("--- START DELETE CUSTOMER BY LIST CUSTOMER ID ::");
        ResultDTO resultDTO = new ResultDTO();
        try {
            if (customerRequestDTO != null) {
                if (customerListMappingRepository.findAllCustomerListMapping(customerRequestDTO.getIds(), customerRequestDTO.getCustomerListId(), customerRequestDTO.getCompanySiteId()).size() > 0) {
                    //customerRepository.deleteIds(ids);
                    customerListMappingRepository.deleteMappingByCustomerIds(customerRequestDTO.getIds(), customerRequestDTO.getCustomerListId(), customerRequestDTO.getCompanySiteId());
                    resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                    resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
                } else {
                    resultDTO.setErrorCode(Constants.ApiErrorCode.DELETE_ERROR);
                    resultDTO.setDescription(Constants.ApiErrorDesc.DELETE_ERROR);
                }
            } else {
                resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCustomerDetailById(Long companySiteId, Long customerId, Long timezoneOffset) {
        LOGGER.info("--- START GET CUSTOMER DETAIL BY ID ::");
        ResultDTO resultDTO = new ResultDTO();
        if (DataUtil.isNullOrZero(companySiteId)) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            return resultDTO;
        }
        try {
            resultDTO.setData(customerRepository.getCustomerDetailById(companySiteId, customerId, timezoneOffset));
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    public Date getSysdate() {
        try {
            Query query = entityManager.createNativeQuery("SELECT to_char(sysdate, 'dd/MM/YYYY hh24:mi:ss') FROM DUAL ");
            List list = query.getResultList();
            if (list != null && list.size() > 0) {
                return DataUtil.convertStringToDateDDMMYYYYHHMISS(DataUtil.safeToString(list.get(0)));
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        }
        return new Date();
    }

    // ------------- customer list ----------------- //
    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO createCustomerList(CustomerListDTO customerListDTO, String userName) {
        LOGGER.info("--- START CREATE CUSTOMER LIST ::");
        ApParam apParam = apParamRepository.getCustomerListSeq();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date currentDate = new Date(dateFormat.format(new Date()));
        Date oldDate = new Date(apParam.getDescription());
        Long newDay = (currentDate.getTime() - oldDate.getTime()) / (1000 * 60 * 60 * 24);
        String convertDate = dateFormat.format(new Date());
        String[] dateArray = convertDate.split("/");

        if (customerListDTO.getCustomerListCode().trim().length() == 0) {
            if (newDay == 0) {
                apParamRepository.updateCustomerListSeq(String.valueOf(Integer.parseInt(apParam.getParValue()) + 1), apParam.getDescription());
                customerListDTO.setCustomerListCode(dateArray[0] + dateArray[2] + dateArray[1] + "_" + (Integer.parseInt(apParam.getParValue()) + 1));
            } else {
                apParamRepository.updateCustomerListSeq("1", dateFormat.format(new Date()));
                customerListDTO.setCustomerListCode(dateArray[0] + dateArray[2] + dateArray[1] + "_1");
            }
        }

        // THÍM NÀO MERGE CONFLICT THÌ GIỮ LẠI HỘ E CÁI METHOD NÀY VỚI
        // VIẾT ĐI VIẾT LẠI 4 LẦN RỒI ĐẤY
        ResultDTO resultDTO = new ResultDTO();
        try {
            CustomerList cl = customerListRepository.findByCustomerListCode(customerListDTO.getCustomerListCode());
            if (cl != null) {
                resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                resultDTO.setDescription("Entity exists");
                return resultDTO;
            } else {
                cl = new CustomerList();
                cl.setCustomerListCode(customerListDTO.getCustomerListCode());
                cl.setCustomerListName(customerListDTO.getCustomerListName());
                cl.setStatus((short) 1);
                cl.setCreateBy(userName);
                cl.setCreateAt(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                cl.setUpdateBy(null);
                cl.setUpdateAt(null);
                cl.setSource(null);
                cl.setDeptCreate(null);
                cl.setCompanySiteId(customerListDTO.getCompanySiteId());
                resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
                resultDTO.setData(customerListRepository.save(cl));
                return resultDTO;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription("Error");
            return resultDTO;
        }
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO updateCustomerList(CustomerListDTO customerListDTO, String userName) {
        LOGGER.info("--- START UPDATE CUSTOMER LIST ::");
        ResultDTO resultDTO = new ResultDTO();

        try {
            if (customerListDTO != null) {
                // update
                CustomerList customerList = customerListRepository.findByCustomerListIdAndCompanySiteId(customerListDTO.getCustomerListId(), customerListDTO.getCompanySiteId());

                if (customerList != null) {
//                    customerList.setCreateBy(customerListDTO.getCreateBy());
                    customerList.setCompanySiteId(customerListDTO.getCompanySiteId());
//                    customerList.setCreateAt(customerListDTO.getCreateAt());
                    customerList.setCustomerListCode(customerListDTO.getCustomerListCode());
                    customerList.setCustomerListName(customerListDTO.getCustomerListName());
                    customerList.setDeptCreate(customerListDTO.getDeptCreate());
                    customerList.setStatus(customerList.getStatus());
                    customerList.setUpdateBy(userName);
                    customerList.setUpdateAt(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                    customerList.setSource(customerListDTO.getSource());
                    customerList.setCustomerListId(customerListDTO.getCustomerListId());
                    customerListRepository.save(customerList);

                    resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                    resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
                } else {
                    resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                    resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
                }
            } else {
                resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO deleteCustomerListIds(CustomerRequestDTO customerRequestDTO) {
        LOGGER.info("--- START DELETE CUSTOMER LIST BY LIST CUSTOMER LIST ID ::");
        ResultDTO resultDTO = new ResultDTO();
        try {
            if (customerRequestDTO != null) {
                if (DataUtil.isNullOrZero(campaignCustomerListRepository.campaignIdsCount(customerRequestDTO.getIds()))) {
                    if (customerListRepository.findAllCustomerList(customerRequestDTO.getIds(), customerRequestDTO.getCompanySiteId()).size() > 0) {
                        customerListRepository.deleteCustomerListIds(customerRequestDTO.getIds(), customerRequestDTO.getCompanySiteId());

                        customerListMappingRepository.deleteMappingByCustomerListIds(customerRequestDTO.getIds(), customerRequestDTO.getCompanySiteId());

                        resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                        resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
                    } else {
                        resultDTO.setErrorCode(Constants.ApiErrorCode.DELETE_ERROR);
                        resultDTO.setDescription(Constants.ApiErrorDesc.DELETE_ERROR);
                    }
                } else {
                    resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                    resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
                }
            } else {
                resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
                resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ;
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO searchCustomerList(SearchCustomerListRequestDTO searchCustomerListRequestDTO) {
        LOGGER.info("=== Start search customer list::");
        ResultDTO resultDTO = new ResultDTO();

        if (DataUtil.isNullOrEmpty(searchCustomerListRequestDTO.getCompanySiteId())) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            return resultDTO;
        }

        Page<CustomerListDTO> customerList = customerListRepository.getAllCustomerListByParams(searchCustomerListRequestDTO, SQLBuilder.buildPageable(searchCustomerListRequestDTO));
        try {
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setTotalRow(customerList.getTotalElements());
            resultDTO.setListData(customerList.getContent());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCustomerContact(CustomerContactDTO customer) {
        LOGGER.info("--- START GET CUSTOMER CONTACT ::");
        ResultDTO result = new ResultDTO();

        try {
            Page<CustomerContact> data;
            if (customer != null && !DataUtil.isNullOrEmpty(customer.getContact()))
                data = customerContactRepository.getByStatusAndCustomerIdAndContactTypeAndContactContaining((short) 1, customer.getCustomerId(), customer.getContactType(), customer.getContact().trim(), SQLBuilder.buildPageable(customer));
            else
                data = customerContactRepository.getByStatusAndCustomerIdAndContactType((short) 1, customer == null ? (long) 0 : customer.getCustomerId(), customer == null ? (short) 0 : customer.getContactType(), SQLBuilder.buildPageable(customer));

            result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            result.setDescription("customer contact data");
            result.setListData(data.getContent());
            result.setTotalRow(data.getTotalElements());

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCustomerRecall(Long campaignId, Long customerId) {
        LOGGER.info("--- START GET CUSTOMER RECALL ::");
        ResultDTO result = new ResultDTO();

        try {
            Long data = campaignCustomerRepository.getCustomerRecall(campaignId, customerId);

            if (data != null) {
                result.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                result.setDescription("campaign customer data");
                result.setData(data);

            } else {
                result.setErrorCode(Constants.ApiErrorCode.ERROR);
                result.setDescription("campaign customer data null");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public List<CustomizeFields> getDynamicHeader(Long companySiteId) {
        LOGGER.info("------------------GET DYNAMIC HEADER-------------------");
        List<CustomizeFields> headerList;
        try {
            headerList = customizeFieldsRepository.findCustomizeFieldsByFunctionCodeEqualsAndStatusAndActiveAndSiteId("CUSTOMER", 1L, 1L, companySiteId);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        return headerList;
    }

    @Override
    @Transactional(value = DataSourceQualify.CCMS_FULL_CHAINED, rollbackFor = Exception.class)
    public Map<String, Object> readAndValidateCustomer(String path, List<CustomizeFields> dynamicHeader, UserSession userSession, Long customerListId, String language) throws IOException {
        LOGGER.info("------------READ AND VALIDATE--------------");
        Locale locale = Locale.forLanguageTag(language);
        DataFormatter dataFormat = new DataFormatter();
        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> headerMap = new HashMap<>();
        List<String> stringHeader = new ArrayList<>();
        List<String> stringMainPhone = new ArrayList<>();
        List<String> stringEmail = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int objectSize;
        Long isPhoneUsed, isEmailUsed, customerId;
        XSSFWorkbook workbook = null;
        boolean isMainPhoneNull = false, isSecondPhoneNull = false, isEmailNull = false;
        phoneNumbersPrimary = new ArrayList<>();
        phoneNumbers = new ArrayList<>();
        emails = new ArrayList<>();
        customizeFieldObjects = new ArrayList<>();
        customerListMappings = new ArrayList<>();
        try {

            //<editor-fold desc="Khởi tạo mảng header tĩnh" defaultstate="collapsed">

            List<CustomizeFields> header = new ArrayList<>();
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.no", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.fullname", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.mainPhone", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.secondPhone", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.email", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.address", locale)));
            header.add(new CustomizeFields("combobox", BundleUtils.getLangString("customer.cusType", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.companyName", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.description", locale)));
            header.add(new CustomizeFields("combobox", BundleUtils.getLangString("customer.callAllowed", locale)));
            header.add(new CustomizeFields("combobox", BundleUtils.getLangString("customer.emailAllowed", locale)));
            header.add(new CustomizeFields("combobox", BundleUtils.getLangString("customer.smsAllowed", locale)));

            header.addAll(dynamicHeader);

            for (CustomizeFields cf : header) {
                stringHeader.add(cf.getTitle().split("#")[0].trim());
            }
            //</editor-fold>

            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(3);

            //<editor-fold desc="Kiểm tra header của template" defaultstate="collapsed">
            if (row.getLastCellNum() > header.size()) {
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell c = row.getCell(i);
                    if (c != null && !"".equals(c.getStringCellValue().trim())) {
                        if (stringHeader.contains(c.getStringCellValue().trim()) && !headerMap.containsKey(c.getStringCellValue().trim())) {
                            headerMap.put(c.getStringCellValue().trim(), c.getColumnIndex());
                        }
                    }
                }
                if (headerMap.size() != header.size()) {
                    result.put("content", Files.readAllBytes(file.toPath()));
                    result.put("message", "template-invalid");
                    return result;
                }
                objectSize = row.getLastCellNum();
            } else if (row.getLastCellNum() < header.size()) {
                result.put("content", Files.readAllBytes(file.toPath()));
                result.put("message", "template-invalid");
                return result;
            } else {
                for (int i = 0; i < header.size(); i++) {
                    Cell c = row.getCell(i);
                    if (c != null && !"".equals(c.getStringCellValue().trim())) {
                        if (stringHeader.contains(c.getStringCellValue().trim()) && !headerMap.containsKey(c.getStringCellValue().trim())) {
                            headerMap.put(c.getStringCellValue().trim(), c.getColumnIndex());
                        }
                    }
                }
                if (headerMap.size() != header.size()) {
                    result.put("content", Files.readAllBytes(file.toPath()));
                    result.put("message", "template-invalid");
                    return result;
                }
                objectSize = row.getLastCellNum();
            }
            //</editor-fold>

            List<Object[]> rawDataList = new ArrayList<>();

            //<editor-fold desc="Kiểm tra file dữ liệu rỗng" defaultstate="collapsed">
            if (sheet.getPhysicalNumberOfRows() == 3) {
                result.put("message", "template-no-data");
                result.put("content", Files.readAllBytes(file.toPath()));
                return result;
            }
            //</editor-fold>

            //<editor-fold desc="Kiểm tra max record number" defaultstate="collapsed">
            if (sheet.getPhysicalNumberOfRows() > (Long.valueOf(maxRecordImport) + 3)) {
                result.put("message", "template-max-record");
                result.put("content", Files.readAllBytes(file.toPath()));
                return result;
            }
            //</editor-fold>

            //<editor-fold desc="Đọc dữ liệu từng dòng 1" defaultstate="collapsed">
            for (int i = 4; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row dataRow = sheet.getRow(i);
                if (!isRowEmpty(dataRow)) {
                    Object[] obj = new Object[objectSize + 1];
                    headerMap.forEach((k, v) -> {
                        Cell dataCell = dataRow.getCell(v);
                        if (dataCell != null) {
//                            if (k.equals(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])
//                                    || k.equals(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])) {
//                                obj[v] = DataUtil.formatPhoneNumberMy(DataUtil.removeNonBMPCharacters(dataFormat.formatCellValue(dataCell)));
//                            } else {
//                                obj[v] = dataFormat.formatCellValue(dataCell);
//                            }
                            obj[v] = dataFormat.formatCellValue(dataCell);
                        } else {
                            if (k.equals(BundleUtils.getLangString("customer.cusType", locale))) {
                                obj[v] = BundleUtils.getLangString("customer.cusType.normal", locale);
                            } else if (k.equals(BundleUtils.getLangString("customer.callAllowed", locale)) || k.equals(BundleUtils.getLangString("customer.emailAllowed", locale))
                                    || k.equals(BundleUtils.getLangString("customer.smsAllowed", locale))) {
                                obj[v] = BundleUtils.getLangString("customer.yes", locale);
                            } else obj[v] = null;
                        }
                    });
                    rawDataList.add(obj);
                } else break;
            }

            //<editor-fold desc="Validate dữ liệu" defaultstate="collapsed">
            for (int i = 0; i < rawDataList.size(); i++) {
                customerId = 0l;
                if (rawDataList.get(i).length > headerMap.get(BundleUtils.getLangString("customer.fullname", locale).split("#")[0])
                        && rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.fullname", locale).split("#")[0])] != null
                        && !"".equals(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.fullname", locale).split("#")[0])].toString().trim())) {
                    String str = validateLength(BundleUtils.getLangString("customer.fullname", locale).split("#")[0], rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.fullname", locale).split("#")[0])].toString(), 1000, locale);
                    if (!"".equals(str)) {
                        sb.append(str);
                    }
                } else sb.append(BundleUtils.getLangString("customer.nameRequired", locale));
                if (rawDataList.get(i).length > headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])
                        && (rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])] == null
                        || "".equals(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])].toString().trim()))) {
                    isMainPhoneNull = true;
                } else {
                    String str = validateLength(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0], rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])].toString(), 50, locale);
                    if (!"".equals(str)) {
                        sb.append(str);
                    } else if (validateNumberOnly(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])].toString().trim())) {
                        sb.append(validatePhone(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])].toString().trim(), locale));
                        if (sb.length() <= 0) {
                            isPhoneUsed = validateExistPhoneEmail(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])].toString().trim(), stringMainPhone, userSession.getSiteId(), true);
                            if (isPhoneUsed != 0 && isPhoneUsed != 2) {
                                customerId = isPhoneUsed;
                            } else if (isPhoneUsed > 1) {
                                sb.append(BundleUtils.getLangString("customer.phoneExists", locale));
                            }
                        }
                        stringMainPhone.add(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])].toString().trim());
                    } else {
                        sb.append(BundleUtils.getLangString("customer.onlyNumber", locale));
                    }
                }
                if (rawDataList.get(i).length > headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0]) && (rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])] == null
                        || "".equals(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])].toString().trim()))) {
                    isSecondPhoneNull = true;
                } else {
                    String str = validateLength(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0], rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])].toString(), 50, locale);
                    if (!"".equals(str)) {
                        sb.append(str);
                    } else if (validateNumberOnly(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])].toString().trim())) {
                        sb.append(validatePhone(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])].toString(), locale));
                    } else {
                        sb.append(BundleUtils.getLangString("customer.onlyNumber", locale));
                    }
                }
                if (rawDataList.get(i).length > headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])
                        && (rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])] == null
                        || "".equals(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])].toString().trim()))) {
                    isEmailNull = true;
                } else {
                    if (!validateEmail(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])].toString().trim())) {
                        sb.append(BundleUtils.getLangString("customer.emailInvalid", locale));
                    } else {
                        sb.append(validateLength(BundleUtils.getLangString("customer.email", locale).split("#")[0], rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])].toString(), 100, locale));
                        if (sb.length() <= 0 && isMainPhoneNull) {
                            isEmailUsed = validateExistPhoneEmail(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])].toString().trim(), stringEmail, userSession.getSiteId(), false);
                            if (isEmailUsed != 0 && isEmailUsed != 2) {
                                customerId = isEmailUsed;
                            } else if (isEmailUsed == 2) {
                                sb.append(BundleUtils.getLangString("customer.emailExists", locale));
                            }
                        }
                        stringEmail.add(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])].toString().trim());
                    }
                }
                if (isEmailNull && isMainPhoneNull && isSecondPhoneNull) {
                    sb.append(BundleUtils.getLangString("customer.invalidRecord", locale));
                }
                if (rawDataList.get(i).length > headerMap.get(BundleUtils.getLangString("customer.address", locale).split("#")[0]) && rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.address", locale).split("#")[0])] != null) {
                    sb.append(validateLength(BundleUtils.getLangString("customer.address", locale).split("#")[0], rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.address", locale).split("#")[0])].toString(), 500, locale));
                }
                if (rawDataList.get(i).length > headerMap.get(BundleUtils.getLangString("customer.cusType", locale))
                        && rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.cusType", locale))] != null) {
                    sb.append(validateCusType(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.cusType", locale))].toString().trim(), locale));
                }
                if (rawDataList.get(i).length > headerMap.get(BundleUtils.getLangString("customer.companyName", locale).split("#")[0])
                        && rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.companyName", locale).split("#")[0])] != null) {
                    sb.append(validateLength(BundleUtils.getLangString("customer.companyName", locale).split("#")[0], rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.companyName", locale).split("#")[0])].toString(), 100, locale));
                }
                if (rawDataList.get(i).length > headerMap.get(BundleUtils.getLangString("customer.description", locale).split("#")[0])
                        && rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.description", locale).split("#")[0])] != null) {
                    sb.append(validateLength(BundleUtils.getLangString("customer.description", locale).split("#")[0], rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.description", locale).split("#")[0])].toString(), 2000, locale));
                }
                if (rawDataList.get(i).length > headerMap.get(BundleUtils.getLangString("customer.callAllowed", locale).split("#")[0])
                        && rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.callAllowed", locale).split("#")[0])] != null) {
                    sb.append(validateYesNo(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.callAllowed", locale))].toString().trim(), locale));
                }
                if (rawDataList.get(i).length > headerMap.get(BundleUtils.getLangString("customer.emailAllowed", locale).split("#")[0])
                        && rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.emailAllowed", locale).split("#")[0])] != null) {
                    sb.append(validateYesNo(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.emailAllowed", locale))].toString().trim(), locale));
                }
                if (rawDataList.get(i).length > headerMap.get(BundleUtils.getLangString("customer.smsAllowed", locale).split("#")[0])
                        && rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.smsAllowed", locale).split("#")[0])] != null) {
                    sb.append(validateYesNo(rawDataList.get(i)[headerMap.get(BundleUtils.getLangString("customer.smsAllowed", locale))].toString().trim(), locale));
                }

                if (sb.length() > 0) {
                    rawDataList.get(i)[objectSize] = sb.toString();
                } else if (customerId > 0 && customerId.toString().length() > 1) {
                    rawDataList.get(i)[objectSize] = updateCustomer(customerId, rawDataList.get(i), headerMap, dynamicHeader, userSession, customerListId, language);
                } else {
                    rawDataList.get(i)[objectSize] = createCustomer(rawDataList.get(i), headerMap, dynamicHeader, userSession, customerListId, language);
                }
                sb = new StringBuilder();
                isEmailNull = false;
                isMainPhoneNull = false;
                isSecondPhoneNull = false;
            }
            //</editor-fold>

            customerContactRepository.saveAll(phoneNumbersPrimary);
            customerContactRepository.saveAll(phoneNumbers);
            customerContactRepository.saveAll(emails);
            LOGGER.info("------------SAVE CUSTOMER_CONTACT COUNT::" + (phoneNumbersPrimary.size() + phoneNumbers.size() + emails.size()));
            customizeFieldObjectRepository.saveAll(customizeFieldObjects);
            LOGGER.info("------------SAVE CUSTOMER_FIELD_OBJECT COUNT::" + customizeFieldObjects.size());
            customerListMappingRepository.saveAll(customerListMappings);
            LOGGER.info("------------SAVE CUSTOMER_LIST_MAPPING COUNT::" + customerListMappings.size());
            phoneNumberRankRepository.saveAll(phoneNumberRanks);
            LOGGER.info("------------SAVE PHONE_NUMBER_RANK COUNT::" + phoneNumberRanks.size());
            byte[] content = buildResultTemplate(rawDataList, header, headerMap, row.getLastCellNum(), language);
            workbook.close();
            result.put("content", content);
            result.put("message", rawDataList.size() > 0 ? "import-success" : "import-error");

            LOGGER.info("------------READ AND VALIDATE CUSTOMER RESULT:: " + result.get("message").toString());
            return result;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result.put("message", "validate-error");
            return result;
        } finally {
            if (workbook != null) workbook.close();
        }

    }

    private String updateCustomer(Long customerId, Object[] objectEdit, Map<String, Integer> headerMap, List<CustomizeFields> dynamicHeader, UserSession userSession, Long customerListId, String language) {
        LOGGER.info("------------UPDATE CUSTOMER IMPORT: " + customerId + "--------------");
        Locale locale = Locale.forLanguageTag(language);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strError = "";
        List<CustomerContact> phoneRanks = new ArrayList<>();
        try {
            Customer c;
            if (objectEdit.length > headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])
                    && objectEdit[headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])] != null
                    && !"".equals(objectEdit[headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])].toString().trim())) {
                String[] mainPhone = DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])]).trim().split("[,;]");
                CustomerContact customerContact;
                for (int j = 0; j < mainPhone.length; j++) {
                    CustomerContact cc = new CustomerContact();
                    mainPhone[j] = DataUtil.formatPhoneNumberMy(DataUtil.removeNonBMPCharacters(mainPhone[j].trim()));
                    customerContact = customerContactRepository.getByContactAndStatusAndIsDirectLineAndCustomerIdAndSiteId(mainPhone[j], (short) 1, (short) 1, customerId, userSession.getSiteId());
                    if (customerContact != null) {
                        cc = customerContact;
                        cc.setUpdateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                        cc.setUpdateBy(userSession.getUserName());
                    } else {
                        cc.setCustomerId(customerId);
                        cc.setSiteId(userSession.getSiteId());
                        cc.setContactType((short) 5);
                        cc.setContact(DataUtil.safeToString(mainPhone[j]));
                        cc.setIsDirectLine((short) 1);
                        cc.setStatus((short) 1);
                        cc.setCreateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                        cc.setCreateBy(userSession.getUserName());
                    }
                    phoneNumbersPrimary.add(cc);
                    phoneRanks.add(cc);
                }
            }
            if (objectEdit.length > headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])
                    && objectEdit[headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])] != null
                    && !"".equals(objectEdit[headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])].toString().trim())) {
                String[] email = DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])]).split("[,;]");
                List<CustomerContact> customerContactList = customerContactRepository.findCustomerContactsByStatusAndIsDirectLineAndCustomerIdAndContactTypeAndSiteId((short) 1, (short) 1, customerId, (short) 2, userSession.getSiteId());
                for (int j = 0; j < email.length; j++) {
                    CustomerContact cc = new CustomerContact();
                    if (customerContactList.size() > 0) {
                        for (int index = 0; index < customerContactList.size(); index++) {
                            if (email[j].equals(customerContactList.get(index).getContact())) {
                                cc = customerContactList.get(index);
                                cc.setUpdateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                                cc.setUpdateBy(userSession.getUserName());
                            } else {
                                cc.setCustomerId(customerId);
                                cc.setSiteId(userSession.getSiteId());
                                cc.setContactType((short) 2);
                                cc.setContact(DataUtil.safeToString(email[j]).trim());
                                cc.setIsDirectLine((short) 0); //Nếu có email chính rồi thì insert bản ghi thành email phụ
                                cc.setStatus((short) 1);
                                cc.setCreateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                                cc.setCreateBy(userSession.getUserName());
                            }
                        }
                    } else {
                        cc.setCustomerId(customerId);
                        cc.setSiteId(userSession.getSiteId());
                        cc.setContactType((short) 2);
                        cc.setContact(DataUtil.safeToString(email[j]).trim());
                        cc.setIsDirectLine((short) 1);
                        cc.setStatus((short) 1);
                        cc.setCreateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                        cc.setCreateBy(userSession.getUserName());
                    }
                    emails.add(cc);
                }
            }
            if (objectEdit.length > headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])
                    && objectEdit[headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])] != null
                    && !"".equals(objectEdit[headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])].toString().trim())) {
                String[] subPhone = DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])]).trim().split("[,;]");
                CustomerContact customerContact;
                for (int j = 0; j < subPhone.length; j++) {
                    CustomerContact cc = new CustomerContact();
                    subPhone[j] = DataUtil.formatPhoneNumberMy(DataUtil.removeNonBMPCharacters(subPhone[j].trim()));
                    customerContact = customerContactRepository.getByContactAndStatusAndIsDirectLineAndCustomerIdAndSiteId(subPhone[j], (short) 1, (short) 0, customerId, userSession.getSiteId());
                    if (customerContact != null) {
                        cc = customerContact;
                        cc.setUpdateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                        cc.setUpdateBy(userSession.getUserName());
                    } else {
                        cc.setCustomerId(customerId);
                        cc.setSiteId(userSession.getSiteId());
                        cc.setContactType((short) 5);
                        cc.setContact(DataUtil.safeToString(subPhone[j]));
                        cc.setIsDirectLine((short) 0);
                        cc.setStatus((short) 1);
                        cc.setCreateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                        cc.setCreateBy(userSession.getUserName());
                    }
                    phoneNumbers.add(cc);
                }
            }
            c = customerRepository.getByCustomerId(customerId);
            c.setName(DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.fullname", locale).split("#")[0])]).trim());
            c.setSiteId(userSession.getSiteId());
            if (objectEdit.length > headerMap.get(BundleUtils.getLangString("customer.description", locale).split("#")[0])
                    && objectEdit[headerMap.get(BundleUtils.getLangString("customer.description", locale).split("#")[0])] != null
                    && !"".equals(DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.description", locale).split("#")[0])]).trim())) {
                c.setDescription(DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.description", locale).split("#")[0])]).trim());
            }
            if (objectEdit.length > headerMap.get(BundleUtils.getLangString("customer.companyName", locale).split("#")[0])
                    && objectEdit[headerMap.get(BundleUtils.getLangString("customer.companyName", locale).split("#")[0])] != null
                    && !"".equals(DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.companyName", locale).split("#")[0])]).trim())) {
                c.setCompanyName(DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.companyName", locale).split("#")[0])]).trim());
            }
            c.setUpdateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
            c.setStatus(1L);
            c.setUpdateBy(userSession.getUserName());
            c.setGender((short) 1);
            if (objectEdit.length > headerMap.get(BundleUtils.getLangString("customer.address", locale).split("#")[0])
                    && objectEdit[headerMap.get(BundleUtils.getLangString("customer.address", locale).split("#")[0])] != null
                    && !"".equals(DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.address", locale).split("#")[0])]).trim())) {
                c.setCurrentAddress(DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.address", locale).split("#")[0])]).trim());
            }
            switch (DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.cusType", locale))]).trim()) {
                case "VIP":
                    c.setCustomerType(2L);
                    break;
                case "Blacklist":
                    c.setCustomerType(3L);
                    break;
                default:
                    c.setCustomerType(1L);
                    break;
            }
            if (DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.callAllowed", locale))]).trim().equals(BundleUtils.getLangString("customer.yes", locale))) {
                c.setCallAllowed(1L);
            } else {
                c.setCallAllowed(0L);
            }
            if (DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.emailAllowed", locale))]).trim().equals(BundleUtils.getLangString("customer.yes", locale))) {
                c.setEmailAllowed(1L);
            } else {
                c.setEmailAllowed(0L);
            }
            if (DataUtil.safeToString(objectEdit[headerMap.get(BundleUtils.getLangString("customer.smsAllowed", locale))]).trim().equals(BundleUtils.getLangString("customer.yes", locale))) {
                c.setSmsAllowed(1L);
            } else {
                c.setSmsAllowed(0L);
            }
            c.setIpccStatus("active");
            customerRepository.save(c);
            List<CustomizeFieldObject> customizeFOList;
            customizeFOList = customizeFieldObjectRepository.findCustomizeFieldObjectsByObjectId(customerId);
            for (int j = 0; j < dynamicHeader.size(); j++) {
                CustomizeFieldObject cfo = null;
                for (int i = 0; i < customizeFOList.size(); i++) {
                    if (objectEdit.length > headerMap.get(dynamicHeader.get(j).getTitle().trim())
                            && objectEdit[headerMap.get(dynamicHeader.get(j).getTitle().trim())] != null
                            && customizeFOList.get(i).getTitle().equals(dynamicHeader.get(j).getTitle().trim())
                            && !customizeFieldObjects.contains(customizeFOList.get(i))) {
                        cfo = customizeFOList.get(i);
                        try {
                            switch (dynamicHeader.get(j).getType()) {
                                case "combobox":
                                    CustomizeFieldOptionValue cfov = customizeFieldOptionValueRepository.findCustomizeFieldOptionValueByNameEqualsAndStatus(
                                            DataUtil.safeToString(objectEdit[headerMap.get(dynamicHeader.get(j).getTitle().trim())]).trim(), 1L);
                                    cfo.setFieldOptionValueId(cfov.getFieldOptionValueId());
                                    break;
                                case "checkbox":
                                    if (DataUtil.safeToString(objectEdit[headerMap.get(dynamicHeader.get(j).getTitle().trim())]).trim().equals(BundleUtils.getLangString("customer.yes", locale))) {
                                        cfo.setValueCheckbox(1L);
                                    } else {
                                        cfo.setValueCheckbox(0L);
                                    }
                                    break;
                                case "date":
                                    Date date = dateFormat.parse(DataUtil.safeToString(objectEdit[headerMap.get(dynamicHeader.get(j).getTitle().trim())]));
                                    cfo.setValueDate(TimeZoneUtils.changeTimeZone(date, 0L));
                                    break;
                                case "number":
                                    cfo.setValueNumber(DataUtil.safeToLong(objectEdit[headerMap.get(dynamicHeader.get(j).getTitle().trim())]));
                                    break;
                                default:
                                    cfo.setValueText(DataUtil.safeToString(objectEdit[headerMap.get(dynamicHeader.get(j).getTitle().trim())]).trim());
                                    break;
                            }
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                            strError = "\"" + DataUtil.safeToString(dynamicHeader.get(j).getTitle()).trim() + "\" is error";
                        }
                        cfo.setUpdateBy(userSession.getUserName());
                        cfo.setUpdateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                        customizeFieldObjects.add(cfo);
                    }
                }
                if (cfo == null) {
                    cfo = new CustomizeFieldObject();
                    cfo.setObjectId(c.getCustomerId());
                    cfo.setCustomizeFieldId(dynamicHeader.get(j).getCustomizeFieldId());
                    if (objectEdit.length > headerMap.get(dynamicHeader.get(j).getTitle().trim())
                            && objectEdit[headerMap.get(dynamicHeader.get(j).getTitle().trim())] != null
                            && !"".equals(objectEdit[headerMap.get(dynamicHeader.get(j).getTitle().trim())])) {
                        try {
                            switch (dynamicHeader.get(j).getType()) {
                                case "combobox":
                                    CustomizeFieldOptionValue cfov =
                                            customizeFieldOptionValueRepository.findCustomizeFieldOptionValueByNameEqualsAndStatus(
                                                    DataUtil.safeToString(objectEdit[headerMap.get(dynamicHeader.get(j).getTitle().trim())]).trim(), 1L);
                                    cfo.setFieldOptionValueId(cfov.getFieldOptionValueId());
                                    break;
                                case "checkbox":
                                    if (DataUtil.safeToString(objectEdit[headerMap.get(dynamicHeader.get(j).getTitle().trim())]).trim().equals(BundleUtils.getLangString("customer.yes", locale))) {
                                        cfo.setValueCheckbox(1L);
                                    } else {
                                        cfo.setValueCheckbox(0L);
                                    }
                                    break;
                                case "date":
                                    Date date = dateFormat.parse(DataUtil.safeToString(objectEdit[headerMap.get(dynamicHeader.get(j).getTitle().trim())]));
                                    cfo.setValueDate(TimeZoneUtils.changeTimeZone(date, 0L));
                                    break;
                                case "number":
                                    cfo.setValueNumber(DataUtil.safeToLong(objectEdit[headerMap.get(dynamicHeader.get(j).getTitle().trim())]));
                                    break;
                                default:
                                    cfo.setValueText(DataUtil.safeToString(objectEdit[headerMap.get(dynamicHeader.get(j).getTitle().trim())]).trim());
                                    break;
                            }
                            cfo.setCreateBy(userSession.getUserName());
                            cfo.setCreateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                            cfo.setStatus(1L);
                            cfo.setFunctionCode("CUSTOMER");
                            cfo.setTitle(DataUtil.safeToString(dynamicHeader.get(j).getTitle()).trim());
                            customizeFieldObjects.add(cfo);
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage(), e);
                            strError = "\"" + DataUtil.safeToString(dynamicHeader.get(j).getTitle()).trim() + "\" is error";
                        }
                    }
                }
            }
            CustomerListMapping clm = customerListMappingRepository.getByCustomerIdAndCompanySiteIdAndCustomerListId(customerId, userSession.getSiteId(), customerListId);
            if (clm == null) {
                clm = new CustomerListMapping();
                clm.setCustomerId(customerId);
                clm.setCompanySiteId(userSession.getSiteId());
                clm.setCustomerListId(customerListId);
                customerListMappings.add(clm);
            }

            PhoneNumberRank phoneNumberRank;
            if (phoneRanks.size() > 0) {
                for (CustomerContact customerContact : phoneRanks) {
                    customerContact.setCustomerId(c.getCustomerId());
                    PhoneNumberRankDTO phoneNumberRankDTO;
                    phoneNumberRankDTO = updatePhoneRank(customerContact, c, userSession.getAccountId());

                    List<PhoneNumberRank> listPNR = phoneNumberRankRepository.findBy(userSession.getAccountId(), phoneNumberRankDTO.getPhoneNumber(), phoneNumberRankDTO.getPartitionHelper());
                    if (listPNR != null && !listPNR.isEmpty()) {
                        if (listPNR.size() > 1) {
                            for (int i = 1; i < listPNR.size(); i++) {
                                phoneNumberRankRepository.deleteById(listPNR.get(i).getId());
                            }
                        }
                        phoneNumberRank = listPNR.get(0);
                        phoneNumberRank.setSyncedRank(listPNR.get(0).getCurrentRank());
                        phoneNumberRank.setCurrentRank(phoneNumberRankDTO.getCurrentRank());
                        phoneNumberRank.setLastSyncTime(new Date(System.currentTimeMillis()).getTime() / 1000);
                        phoneNumberRank.setLastUpdateTime(new Date(System.currentTimeMillis()).getTime() / 1000);
                    } else {
                        phoneNumberRankDTO = creatPhoneRank(customerContact, c, userSession.getAccountId());
                        phoneNumberRank = phoneNumberRankMapper.toPersistenceBean(phoneNumberRankDTO);
                    }
                    phoneNumberRanks.add(phoneNumberRank);
                }
            }

            return strError != "" ? strError : "Update OK";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "Update failed";
        }
    }

    private String createCustomer(Object[] objectAdd, Map<String, Integer> headerMap, List<CustomizeFields> dynamicHeader, UserSession userSession, Long customerListId, String language) {
        LOGGER.info("------------INSERT CUSTOMER IMPORT--------------");
        Locale locale = Locale.forLanguageTag(language);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strError = "";
        List<CustomerContact> phoneRanks = new ArrayList<>();
        try {
            Customer c = new Customer();
            c.setName(DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.fullname", locale).split("#")[0])]).trim());
            c.setSiteId(userSession.getSiteId());
            c.setCode(null);
            if (objectAdd.length > headerMap.get(BundleUtils.getLangString("customer.description", locale).split("#")[0])
                    && objectAdd[headerMap.get(BundleUtils.getLangString("customer.description", locale).split("#")[0])] != null
                    && !"".equals(DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.description", locale).split("#")[0])]).trim())) {
                c.setDescription(DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.description", locale).split("#")[0])]).trim());
            }
            if (objectAdd.length > headerMap.get(BundleUtils.getLangString("customer.companyName", locale).split("#")[0])
                    && objectAdd[headerMap.get(BundleUtils.getLangString("customer.companyName", locale).split("#")[0])] != null
                    && !"".equals(DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.companyName", locale).split("#")[0])]).trim())) {
                c.setCompanyName(DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.companyName", locale).split("#")[0])]).trim());
            }
            c.setCustomerImg(null);
            c.setCreateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
            c.setUpdateDate(null);
            c.setStatus(1L);
            c.setCreateBy(userSession.getUserName());
            c.setUpdateBy(null);
            c.setGender((short) 1);
            if (objectAdd.length > headerMap.get(BundleUtils.getLangString("customer.address", locale).split("#")[0])
                    && objectAdd[headerMap.get(BundleUtils.getLangString("customer.address", locale).split("#")[0])] != null
                    && !"".equals(DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.address", locale).split("#")[0])]).trim())) {
                c.setCurrentAddress(DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.address", locale).split("#")[0])]).trim());
            }
            c.setMobileNumber(null);
            c.setEmail(null);
            c.setUserName(null);
            c.setAreaCode(null);
            switch (DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.cusType", locale))]).trim()) {
                case "VIP":
                    c.setCustomerType(2L);
                    break;
                case "Blacklist":
                    c.setCustomerType(3L);
                    break;
                default:
                    c.setCustomerType(1L);
                    break;
            }
            if (DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.callAllowed", locale))]).trim().equals(BundleUtils.getLangString("customer.yes", locale))) {
                c.setCallAllowed(1L);
            } else {
                c.setCallAllowed(0L);
            }
            if (DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.emailAllowed", locale))]).trim().equals(BundleUtils.getLangString("customer.yes", locale))) {
                c.setEmailAllowed(1L);
            } else {
                c.setEmailAllowed(0L);
            }
            if (DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.smsAllowed", locale))]).trim().equals(BundleUtils.getLangString("customer.yes", locale))) {
                c.setSmsAllowed(1L);
            } else {
                c.setSmsAllowed(0L);
            }
            c.setIpccStatus("active");

            Customer saved = customerRepository.save(c);
            if (objectAdd.length > headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])
                    && objectAdd[headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])] != null
                    && !"".equals(objectAdd[headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])].toString().trim())) {
                String[] mainPhone = DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.mainPhone", locale).split("#")[0])]).trim().split("[,;]");
                for (int j = 0; j < mainPhone.length; j++) {
                    mainPhone[j] = DataUtil.formatPhoneNumberMy(DataUtil.removeNonBMPCharacters(mainPhone[j].trim()));
                    CustomerContact cc = new CustomerContact();
                    cc.setCustomerId(saved.getCustomerId());
                    cc.setSiteId(userSession.getSiteId());
                    cc.setContactType((short) 5);
                    cc.setContact(DataUtil.safeToString(mainPhone[j]));
                    cc.setIsDirectLine((short) 1);
                    cc.setStatus((short) 1);
                    cc.setCreateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                    cc.setCreateBy(userSession.getUserName());
                    phoneNumbersPrimary.add(cc);
                    phoneRanks.add(cc);
                }
            }
            if (objectAdd.length > headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])
                    && objectAdd[headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])] != null
                    && !"".equals(objectAdd[headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])].toString().trim())) {
                String[] subPhone = DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.secondPhone", locale).split("#")[0])]).trim().split("[,;]");
                for (int j = 0; j < subPhone.length; j++) {
                    subPhone[j] = DataUtil.formatPhoneNumberMy(DataUtil.removeNonBMPCharacters(subPhone[j].trim()));
                    CustomerContact cc = new CustomerContact();
                    cc.setCustomerId(saved.getCustomerId());
                    cc.setSiteId(userSession.getSiteId());
                    cc.setContactType((short) 5);
                    cc.setContact(DataUtil.safeToString(subPhone[j]));
                    cc.setIsDirectLine((short) 0);
                    cc.setStatus((short) 1);
                    cc.setCreateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                    cc.setCreateBy(userSession.getUserName());
                    phoneNumbers.add(cc);
                }
            }
            if (objectAdd.length > headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])
                    && objectAdd[headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])] != null
                    && !"".equals(objectAdd[headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])].toString().trim())) {
                String[] email = DataUtil.safeToString(objectAdd[headerMap.get(BundleUtils.getLangString("customer.email", locale).split("#")[0])]).split("[,;]");
                for (int j = 0; j < email.length; j++) {
                    CustomerContact cc = new CustomerContact();
                    cc.setCustomerId(saved.getCustomerId());
                    cc.setSiteId(userSession.getSiteId());
                    cc.setContactType((short) 2);
                    cc.setContact(DataUtil.safeToString(email[j]).trim());
                    cc.setIsDirectLine((short) 1);
                    cc.setStatus((short) 1);
                    cc.setCreateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                    cc.setCreateBy(userSession.getUserName());
                    emails.add(cc);
                }
            }
            for (int j = 0; j < dynamicHeader.size(); j++) {
                CustomizeFieldObject cfo = new CustomizeFieldObject();
                cfo.setObjectId(saved.getCustomerId());
                cfo.setCustomizeFieldId(dynamicHeader.get(j).getCustomizeFieldId());
                if (objectAdd.length > headerMap.get(dynamicHeader.get(j).getTitle().trim()) &&
                        objectAdd[headerMap.get(dynamicHeader.get(j).getTitle().trim())] != null) {
                    try {
                        switch (dynamicHeader.get(j).getType()) {
                            case "combobox":
                                CustomizeFieldOptionValue cfov =
                                        customizeFieldOptionValueRepository.findCustomizeFieldOptionValueByNameEqualsAndStatus(
                                                DataUtil.safeToString(objectAdd[headerMap.get(dynamicHeader.get(j).getTitle().trim())]).trim(), 1L);
                                cfo.setFieldOptionValueId(cfov.getFieldOptionValueId());
                                break;
                            case "checkbox":
                                if (DataUtil.safeToString(objectAdd[headerMap.get(dynamicHeader.get(j).getTitle().trim())]).trim().equals(BundleUtils.getLangString("customer.yes", locale))) {
                                    cfo.setValueCheckbox(1L);
                                } else {
                                    cfo.setValueCheckbox(0L);
                                }
                                break;
                            case "date":
                                Date date = dateFormat.parse(DataUtil.safeToString(objectAdd[headerMap.get(dynamicHeader.get(j).getTitle().trim())]));
                                cfo.setValueDate(TimeZoneUtils.changeTimeZone(date, 0L));
                                break;
                            case "number":
                                cfo.setValueNumber(DataUtil.safeToLong(objectAdd[headerMap.get(dynamicHeader.get(j).getTitle().trim())]));
                                break;
                            default:
                                cfo.setValueText(DataUtil.safeToString(objectAdd[headerMap.get(dynamicHeader.get(j).getTitle().trim())]).trim());
                                break;
                        }
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                        strError += "\"" + DataUtil.safeToString(dynamicHeader.get(j).getTitle()).trim() + "\" is error; ";
                    }
                }
                cfo.setCreateBy(userSession.getUserName());
                cfo.setCreateDate(TimeZoneUtils.changeTimeZone(new Date(), 0L));
                cfo.setStatus(1L);
                cfo.setFunctionCode("CUSTOMER");
                cfo.setTitle(DataUtil.safeToString(dynamicHeader.get(j).getTitle()).trim());
                customizeFieldObjects.add(cfo);
            }

            CustomerListMapping clm = customerListMappingRepository.getByCustomerIdAndCompanySiteIdAndCustomerListId(saved.getCustomerId(), userSession.getSiteId(), customerListId);
            if (clm == null) {
                clm = new CustomerListMapping();
                clm.setCustomerId(saved.getCustomerId());
                clm.setCompanySiteId(userSession.getSiteId());
                clm.setCustomerListId(customerListId);
                customerListMappings.add(clm);
            }

            PhoneNumberRank phoneNumberRank;
            if (phoneRanks.size() > 0) {
                for (CustomerContact customerContact : phoneRanks) {
                    customerContact.setCustomerId(c.getCustomerId());
                    PhoneNumberRankDTO phoneNumberRankDTO;
                    phoneNumberRankDTO = creatPhoneRank(customerContact, c, userSession.getAccountId());
                    List<PhoneNumberRank> listPNR = phoneNumberRankRepository.findBy(userSession.getAccountId(), phoneNumberRankDTO.getPhoneNumber(), phoneNumberRankDTO.getPartitionHelper());
                    if (listPNR != null && !listPNR.isEmpty()) {
                        if (listPNR.size() > 1) {
                            for (int i = 1; i < listPNR.size(); i++) {
                                phoneNumberRankRepository.deleteById(listPNR.get(i).getId());
                            }
                        }
                        phoneNumberRank = listPNR.get(0);
                        phoneNumberRank.setSyncedRank(listPNR.get(0).getCurrentRank());
                        phoneNumberRank.setCurrentRank(phoneNumberRankDTO.getCurrentRank());
                        phoneNumberRank.setLastSyncTime(new Date(System.currentTimeMillis()).getTime() / 1000);
                        phoneNumberRank.setLastUpdateTime(new Date(System.currentTimeMillis()).getTime() / 1000);
                    } else {
                        phoneNumberRank = phoneNumberRankMapper.toPersistenceBean(phoneNumberRankDTO);
                    }
                    phoneNumberRanks.add(phoneNumberRank);
                }
            }
            return strError != "" ? strError : "OK";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "Insert failed";
        }
    }

    //<editor-fold desc="Validate Methods" defaultstate="collapsed">
    private boolean isRowEmpty(Row row) {
        for (int i = 0; i <= row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private Long validateExistPhoneEmail(String str, List<String> existList, Long siteId, boolean checkPhone) {
        Long result = 0l;
        Long customerId = 0l;
        String[] arr = str.split("[;,]");
        String[] arrExist;
        List<Long> customerIdList = new ArrayList<>();
        List<CustomerContact> contactList;
        try {
            if (checkPhone) {
                for (int i = 0; i < existList.size(); i++) {
                    arrExist = existList.get(i).split("[;,]");
                    for (int index1 = 0; index1 < arr.length; index1++) {
                        for (int index2 = 0; index2 < arrExist.length; index2++) {
                            if (arr[index1].equals(arrExist[index2])) {
                                return 2l;
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < arr.length; i++) {
                String phoneNumber = DataUtil.formatPhoneNumberMy(DataUtil.removeNonBMPCharacters(arr[i]));
                contactList = customerContactRepository.findCustomerContactsByContactAndStatusAndIsDirectLineAndSiteId(phoneNumber, (short) 1, (short) 1, siteId);
                if (contactList.size() == 0) {
                    result = Math.max(result, 0l);
                } else if (contactList.size() == 1) {
                    customerIdList.add(contactList.get(0).getCustomerId());
                    for (int j = 0; j < customerIdList.size(); j++) {
                        if (contactList.get(0).getCustomerId() != customerIdList.get(j)) {
                            result = Math.max(result, 2l);
                            break;
                        }
                    }
                    customerId = contactList.get(0).getCustomerId();
                    result = Math.max(result, 1l);
                } else if (contactList.size() > 1) {
                    result = Math.max(result, 2l);
                    break;
                }
            }
            return result == 1l ? customerId : result;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return 2l;
        }
    }

    private String validatePhone(String str, Locale locale) {
        String result = "";
        String[] arr = str.split("[;,]");
        if (str.length() > 50) {
            result += BundleUtils.getLangString("customer.phoneMax50", locale);
        }
        if (arr.length > 3) {
            result += BundleUtils.getLangString("customer.3phone", locale);
        } else {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].length() < 8 || arr[i].length() > 15) {
                    result += BundleUtils.getLangString("customer.phone8to15", locale);
                    break;
                }
            }
        }
        return result;
    }

    private boolean validateNumberOnly(String str) {
//        String regexp = "^(\\+" + phoneNumberStart.trim() + "|0|)?[1-9][0-9]+";
//        String[] arr = str.split("[,;]");
//        int count = 0;
//        for (int i = 0; i < arr.length; i++) {
//            if (!arr[i].matches(regexp)) {
//                count++;
//            }
//        }
//        return count == 0;
        return true;
    }

    private String validateUsingRegexp(String header, String data, String regexp, Locale locale) {
        if (data.matches(regexp)) {
            return header + " " + BundleUtils.getLangString("customer.notMatch", locale);
        } else return "";
    }

    private String validateLength(String header, String str, int length, Locale locale) {
        if (str.trim().length() > length) {
            return header + " " + BundleUtils.getLangString("customer.notGreaterThan", locale) + " " + length;
        }
        return "";
    }

    private String validateDynamicLength(String header, String str, Long min, Long max, Locale locale) {
        if (str.trim().length() < min || str.trim().length() > max) {
            return header + " " + BundleUtils.getLangString("customer.notGreaterThan", locale) + " " + max + " " + BundleUtils.getLangString("customer.notLessThan", locale) + " " + min;
        }
        return "";
    }

    private boolean validateEmail(String str) {
        String[] arr = str.split("[;,]");
        for (int i = 0; i < arr.length; i++) {
            Matcher matcher = EMAIL_REGEXP.matcher(arr[i]);
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    private String validateCusType(String str, Locale locale) {
        String[] arr = {BundleUtils.getLangString("customer.cusType.normal", locale),
                BundleUtils.getLangString("customer.cusType.vip", locale),
                BundleUtils.getLangString("customer.cusType.blacklist", locale)};
        List<String> cusTypes = Arrays.asList(arr);
        if (cusTypes.contains(str.trim())) {
            return "";
        } else {
            return BundleUtils.getLangString("customer.cusTypeInvalid", locale);
        }
    }

    private String validateYesNo(String str, Locale locale) {
        String[] arr = {BundleUtils.getLangString("customer.yes", locale),
                BundleUtils.getLangString("customer.not", locale)};
        List<String> cusTypes = Arrays.asList(arr);
        if (cusTypes.contains(str.trim())) {
            return "";
        } else {
            return BundleUtils.getLangString("customer.comboboxInvalid", locale);
        }
    }

    private boolean validateLetter(String str) {
        String regexp = "@\"^\\p{L}+$\"";
        return str.matches(regexp);
    }

    private String validateCombobox(String[] optionValue, String str, Locale locale) {
        List<String> arr = Arrays.asList(optionValue);
        if (!arr.contains(str)) {
            return BundleUtils.getLangString("customer.comboboxInvalid", locale);
        } else {
            return "";
        }
    }

    //</editor-fold>

    private byte[] buildResultTemplate(List<Object[]> rawData, List<CustomizeFields> header, Map<String, Integer> dataMap, int lastNum, String language) throws IOException {
        LOGGER.info("---------------CREATE RESULT FILE--------------");
        Locale locale = Locale.forLanguageTag(language);
        XSSFWorkbook workbook = null;
        Map<String, Integer> headerMap = new HashMap<>();
        try {
            workbook = new XSSFWorkbook();
            CreationHelper creationHelper = workbook.getCreationHelper();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Sheet sheet = workbook.createSheet("RESULT");
            dataMap.put(BundleUtils.getLangString("customer.result", locale), lastNum);

            //<editor-fold desc="Tạo style và font" defaultstate="collapsed">

            Font headerFont = workbook.createFont();
            Font importantFont = workbook.createFont();
            importantFont.setColor(IndexedColors.RED.index);
            headerFont.setBold(true);
            CellStyle headerStyle = workbook.createCellStyle();
            CellStyle importantStyle = workbook.createCellStyle();
            CellStyle columnStyle = workbook.createCellStyle();
            importantStyle.setFont(importantFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFont(headerFont);
            columnStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            columnStyle.setAlignment(HorizontalAlignment.CENTER);
            columnStyle.setWrapText(true);

            //</editor-fold>

            //<editor-fold desc="Thêm combobox" defaultstate="collapsed">

            //<editor-fold desc="Cho những trường động" defaultstate="collapsed">
            DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();
            for (int i = 12; i < header.size(); i++) {
                if ("combobox".equals(header.get(i).getType())) {
                    String[] constraint;
                    List<CustomizeFieldOptionValue> list =
                            customizeFieldOptionValueRepository.findCustomizeFieldOptionValuesByFieldOptionIdAndStatus(header.get(i).getFieldOptionsId(), 1L);
                    constraint = new String[list.size()];
                    for (int j = 0; j < list.size(); j++) {
                        constraint[j] = list.get(j).getName();
                    }
                    DataValidationConstraint comboboxConstraint = dataValidationHelper.createExplicitListConstraint(constraint);
                    CellRangeAddressList comboboxCellRange = new CellRangeAddressList(4, 9999, 12 + i, 12 + i);
                    DataValidation comboboxValidation = dataValidationHelper.createValidation(comboboxConstraint, comboboxCellRange);
                    comboboxValidation.setSuppressDropDownArrow(true);
                    comboboxValidation.setShowErrorBox(true);
                    sheet.addValidationData(comboboxValidation);
                } else if ("checkbox".equals(header.get(i).getType())) {
                    DataValidationConstraint yesNoConstraint = dataValidationHelper.createExplicitListConstraint(new String[]{BundleUtils.getLangString("customer.yes", locale), BundleUtils.getLangString("customer.not", locale)});
                    CellRangeAddressList checkboxCellRange = new CellRangeAddressList(4, 9999, 12 + i, 12 + i);
                    DataValidation yesNoValidation = dataValidationHelper.createValidation(yesNoConstraint, checkboxCellRange);
                    yesNoValidation.setShowErrorBox(true);
                    yesNoValidation.setSuppressDropDownArrow(true);
                    sheet.addValidationData(yesNoValidation);
                }
            }
            //</editor-fold>

            //<editor-fold desc="Cho những trường tĩnh" defaultstate="collapsed">
            DataValidationConstraint cusTypeConstraint = dataValidationHelper.createExplicitListConstraint(new String[]{BundleUtils.getLangString("customer.cusType.normal", locale), BundleUtils.getLangString("customer.cusType.vip", locale), BundleUtils.getLangString("customer.cusType.blacklist", locale)});
            DataValidationConstraint isAllowConstraint = dataValidationHelper.createExplicitListConstraint(new String[]{BundleUtils.getLangString("customer.yes", locale), BundleUtils.getLangString("customer.not", locale)});
            CellRangeAddressList cusTypeCellRangeAddressList = new CellRangeAddressList(4, 9999, 6, 6);
            CellRangeAddressList callCellRangeAddressList = new CellRangeAddressList(4, 9999, 9, 9);
            CellRangeAddressList emailCellRangeAddressList = new CellRangeAddressList(4, 9999, 10, 10);
            CellRangeAddressList smsCellRangeAddressList = new CellRangeAddressList(4, 9999, 11, 11);
            DataValidation cusTypeValidation = dataValidationHelper.createValidation(cusTypeConstraint, cusTypeCellRangeAddressList);
            DataValidation callValidation = dataValidationHelper.createValidation(isAllowConstraint, callCellRangeAddressList);
            DataValidation emailValidation = dataValidationHelper.createValidation(isAllowConstraint, emailCellRangeAddressList);
            DataValidation smsValidation = dataValidationHelper.createValidation(isAllowConstraint, smsCellRangeAddressList);
            cusTypeValidation.setSuppressDropDownArrow(true);
            cusTypeValidation.setShowErrorBox(true);
            callValidation.setSuppressDropDownArrow(true);
            callValidation.setShowErrorBox(true);
            emailValidation.setSuppressDropDownArrow(true);
            emailValidation.setShowErrorBox(true);
            smsValidation.setSuppressDropDownArrow(true);
            smsValidation.setShowErrorBox(true);
            sheet.addValidationData(cusTypeValidation);
            sheet.addValidationData(callValidation);
            sheet.addValidationData(emailValidation);
            sheet.addValidationData(smsValidation);
            //</editor-fold>

            //</editor-fold>

            //<editor-fold desc="Ghi header" defaultstate="collapsed">

            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = creationHelper.createClientAnchor();
            Row row0 = sheet.createRow(0);
            Row row2 = sheet.createRow(2);
            Row row3 = sheet.createRow(3);
            Cell cell0 = row0.createCell(0);
            cell0.setCellStyle(headerStyle);
            cell0.setCellValue(BundleUtils.getLangString("customer.importCustomer", locale));
            Cell cell2 = row2.createCell(0);
            cell2.setCellStyle(importantStyle);
            cell2.setCellValue(BundleUtils.getLangString("customer.notice", locale));
            for (int i = 0; i < header.size(); i++) {
                sheet.setDefaultColumnStyle(i, columnStyle);
                Cell headerCell = row3.createCell(i);
                headerCell.setCellValue(header.get(i).getTitle().split("#")[0]);
                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerFont.setFontHeightInPoints((short) 11);
                headerCell.setCellStyle(headerStyle);
                if (i == 0) {
                    sheet.setColumnWidth(i, 2500);
                } else {
                    sheet.setColumnWidth(i, 6000);
                }
                anchor.setCol1(headerCell.getColumnIndex());
                anchor.setCol2(headerCell.getColumnIndex() + 2);
                anchor.setRow1(row3.getRowNum());
                anchor.setRow2(row3.getRowNum() + 4);
                if (header.get(i).getTitle().contains("#")) {
                    Comment comment = drawing.createCellComment(anchor);
                    RichTextString str = creationHelper.createRichTextString(header.get(i).getTitle().split("#")[1]);
                    comment.setString(str);
                    comment.setAuthor("APACHE POI");
                    headerCell.setCellComment(comment);
                }
                headerMap.put(header.get(i).getTitle().split("#")[0].trim(), i);
            }
            sheet.setColumnWidth(header.size(), 8000);
            Cell resultCell = row3.createCell(header.size());
            resultCell.setCellStyle(headerStyle);
            resultCell.setCellValue(BundleUtils.getLangString("customer.result", locale));
            headerMap.put(BundleUtils.getLangString("customer.result", locale), headerMap.size());
            //</editor-fold>

            //<editor-fold desc="Ghi dữ liệu">

            for (int i = 0; i < rawData.size(); i++) {
                Row dataRow = sheet.createRow(4 + i);
                int finalI = i;
                headerMap.forEach((k, v) -> {
                    Cell c = dataRow.createCell(v);
                    c.setCellType(CellType.STRING);
                    if (rawData.get(finalI).length > dataMap.get(k)
                            && rawData.get(finalI)[dataMap.get(k)] != null
                            && !"".equals(DataUtil.safeToString(rawData.get(finalI)[dataMap.get(k)]).trim())) {
                        c.setCellValue(rawData.get(finalI)[dataMap.get(k)].toString());
                    }
                });
//                for (int j = 0; j < header.size(); j++) {

//                    headerMap.get(header.get(j).getTitle().split("#")[0]);
//                    if (rawData.get(i).length > header.size() + 1 && rawData.get(i)[j] != null && !"".equals(rawData.get(i)[j].toString().trim())) {
//                        Cell c = dataRow.createCell(j);
//                        c.setCellType(CellType.STRING);
//                        c.setCellValue(rawData.get(i)[j].toString());
//                    }
//                }
            }

            //</editor-fold>

            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 13));
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 13));
            workbook.write(os);
            os.flush();
            os.close();
            return os.toByteArray();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        } finally {
            if (workbook != null) workbook.close();
        }
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public XSSFWorkbook buildTemplate(Long companySiteId, String language) throws IOException {
        LOGGER.info("-----------BUILD TEMPLATE-----------");
        Locale locale = Locale.forLanguageTag(language);
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook();
            CreationHelper creationHelper = workbook.getCreationHelper();
            //ByteArrayOutputStream os = new ByteArrayOutputStream();
            Sheet sheet = workbook.createSheet("IMPORT");
            DataFormat dataFormat = workbook.createDataFormat();

            //<editor-fold desc="Tạo array header" defaultstate="collapsed">
            List<CustomizeFields> header = new ArrayList<>();
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.no", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.fullname", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.mainPhone", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.secondPhone", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.email", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.address", locale)));
            header.add(new CustomizeFields("combobox", BundleUtils.getLangString("customer.cusType", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.companyName", locale)));
            header.add(new CustomizeFields("text", BundleUtils.getLangString("customer.description", locale)));
            header.add(new CustomizeFields("combobox", BundleUtils.getLangString("customer.callAllowed", locale)));
            header.add(new CustomizeFields("combobox", BundleUtils.getLangString("customer.emailAllowed", locale)));
            header.add(new CustomizeFields("combobox", BundleUtils.getLangString("customer.smsAllowed", locale)));

            List<CustomizeFields> dynamicHeader = getDynamicHeader(companySiteId);
            header.addAll(dynamicHeader);
            //</editor-fold>

            //<editor-fold desc="Tạo style và font" defaultstate="collapsed">
            Font headerFont = workbook.createFont();
            Font importantFont = workbook.createFont();
            importantFont.setColor(IndexedColors.RED.index);
            headerFont.setBold(true);
            CellStyle headerStyle = workbook.createCellStyle();
            CellStyle importantStyle = workbook.createCellStyle();
            CellStyle columnStyle = workbook.createCellStyle();
            importantStyle.setFont(importantFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFont(headerFont);
            columnStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            columnStyle.setAlignment(HorizontalAlignment.CENTER);
            columnStyle.setWrapText(true);
            columnStyle.setDataFormat(dataFormat.getFormat("@"));
            //</editor-fold>

            //<editor-fold desc="Thêm combobox" defaultstate="collapsed">

            //<editor-fold desc="Cho những trường động" defaultstate="collapsed">
            DataValidationHelper dataValidationHelper = sheet.getDataValidationHelper();
            for (int i = 0; i < dynamicHeader.size(); i++) {
                if ("combobox".equals(dynamicHeader.get(i).getType())) {
                    String[] constraint;
                    List<CustomizeFieldOptionValue> list =
                            customizeFieldOptionValueRepository.findCustomizeFieldOptionValuesByFieldOptionIdAndStatus(dynamicHeader.get(i).getFieldOptionsId(), 1L);
                    constraint = new String[list.size()];
                    for (int j = 0; j < list.size(); j++) {
                        constraint[j] = list.get(j).getName();
                    }
                    DataValidationConstraint comboboxConstraint = dataValidationHelper.createExplicitListConstraint(constraint);
                    CellRangeAddressList comboboxCellRange = new CellRangeAddressList(4, 9999, 12 + i, 12 + i);
                    DataValidation comboboxValidation = dataValidationHelper.createValidation(comboboxConstraint, comboboxCellRange);
                    comboboxValidation.setSuppressDropDownArrow(true);
                    comboboxValidation.setShowErrorBox(true);
                    sheet.addValidationData(comboboxValidation);
                } else if ("checkbox".equals(dynamicHeader.get(i).getType())) {
                    DataValidationConstraint yesNoConstraint = dataValidationHelper.createExplicitListConstraint(new String[]{BundleUtils.getLangString("customer.yes", locale), BundleUtils.getLangString("customer.not", locale)});
                    CellRangeAddressList checkboxCellRange = new CellRangeAddressList(4, 9999, 12 + i, 12 + i);
                    DataValidation yesNoValidation = dataValidationHelper.createValidation(yesNoConstraint, checkboxCellRange);
                    yesNoValidation.setShowErrorBox(true);
                    yesNoValidation.setSuppressDropDownArrow(true);
                    sheet.addValidationData(yesNoValidation);
                }
            }
            //</editor-fold>

            //<editor-fold desc="Cho những trường tĩnh" defaultstate="collapsed">
            DataValidationConstraint cusTypeConstraint = dataValidationHelper.createExplicitListConstraint(new String[]{BundleUtils.getLangString("customer.cusType.normal", locale), BundleUtils.getLangString("customer.cusType.vip", locale), BundleUtils.getLangString("customer.cusType.blacklist", locale)});
            DataValidationConstraint isAllowConstraint = dataValidationHelper.createExplicitListConstraint(new String[]{BundleUtils.getLangString("customer.yes", locale), BundleUtils.getLangString("customer.not", locale)});
            CellRangeAddressList cusTypeCellRangeAddressList = new CellRangeAddressList(4, 9999, 6, 6);
            CellRangeAddressList callCellRangeAddressList = new CellRangeAddressList(4, 9999, 9, 9);
            CellRangeAddressList emailCellRangeAddressList = new CellRangeAddressList(4, 9999, 10, 10);
            CellRangeAddressList smsCellRangeAddressList = new CellRangeAddressList(4, 9999, 11, 11);
            DataValidation cusTypeValidation = dataValidationHelper.createValidation(cusTypeConstraint, cusTypeCellRangeAddressList);
            DataValidation callValidation = dataValidationHelper.createValidation(isAllowConstraint, callCellRangeAddressList);
            DataValidation emailValidation = dataValidationHelper.createValidation(isAllowConstraint, emailCellRangeAddressList);
            DataValidation smsValidation = dataValidationHelper.createValidation(isAllowConstraint, smsCellRangeAddressList);
            cusTypeValidation.setSuppressDropDownArrow(true);
            cusTypeValidation.setShowErrorBox(true);
            callValidation.setSuppressDropDownArrow(true);
            callValidation.setShowErrorBox(true);
            emailValidation.setSuppressDropDownArrow(true);
            emailValidation.setShowErrorBox(true);
            smsValidation.setSuppressDropDownArrow(true);
            smsValidation.setShowErrorBox(true);
            sheet.addValidationData(cusTypeValidation);
            sheet.addValidationData(callValidation);
            sheet.addValidationData(emailValidation);
            sheet.addValidationData(smsValidation);
            //</editor-fold>

            //</editor-fold>

            //<editor-fold desc="Ghi header" defaultstate="collapsed">
            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = creationHelper.createClientAnchor();
            Row row0 = sheet.createRow(0);
            Row row2 = sheet.createRow(2);
            Row row3 = sheet.createRow(3);
            Cell cell0 = row0.createCell(0);
            cell0.setCellStyle(headerStyle);
            cell0.setCellValue(BundleUtils.getLangString("customer.importCustomer", locale));
            Cell cell2 = row2.createCell(0);
            cell2.setCellStyle(importantStyle);
            cell2.setCellValue(BundleUtils.getLangString("customer.notice", locale));
            for (int i = 0; i < header.size(); i++) {
                sheet.setDefaultColumnStyle(i, columnStyle);
                Cell headerCell = row3.createCell(i);
                headerCell.setCellValue(header.get(i).getTitle().split("#")[0].trim());
                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerFont.setFontHeightInPoints((short) 11);
                headerCell.setCellStyle(headerStyle);
                if (i == 0) {
                    sheet.setColumnWidth(i, 2500);
                } else {
                    sheet.setColumnWidth(i, 6000);
                }
                anchor.setCol1(headerCell.getColumnIndex());
                anchor.setCol2(headerCell.getColumnIndex() + 2);
                anchor.setRow1(row3.getRowNum());
                anchor.setRow2(row3.getRowNum() + 4);
                if (header.get(i).getTitle().contains("#")) {
                    Comment comment = drawing.createCellComment(anchor);
                    RichTextString str = creationHelper.createRichTextString(header.get(i).getTitle().split("#")[1]);
                    comment.setString(str);
                    comment.setAuthor("APACHE POI");
                    headerCell.setCellComment(comment);
                }
            }
            //</editor-fold>

            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 13));
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 13));
//            workbook.write(os);
//            os.flush();
//            os.close();
//            return os.toByteArray();
            return workbook;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        } finally {
//            if (workbook != null) workbook.close();
        }
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getCustomizeField(Long companySiteId, Long customerId, Long timezoneOffset) {
        LOGGER.info("--- START GET CUSTOMIZE FIELD ::");
        ResultDTO resultDTO = new ResultDTO();
        if (DataUtil.isNullOrZero(companySiteId)) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            return resultDTO;
        }
        try {
            resultDTO.setData(customerRepository.getIndividualCustomerDetailById(companySiteId, customerId, timezoneOffset));
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public List<CustomerListDTO> getCustomerListInfo(CampaignCustomerDTO campaignCustomerDTO) {
        LOGGER.info("--- START GET CUSTOMER LIST INFO ::");
        List<CustomerListDTO> customerList;
        try {
            customerList = customerRepository.getCustomerListInfo(campaignCustomerDTO);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
        return customerList;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getIndividualCustomerInfo(CampaignCustomerDTO campaignCustomerDTO) {
        LOGGER.info("--- START GET INDIVIDUAL CUSTOMER INFO ::");
        ResultDTO resultDTO = new ResultDTO();

        if (DataUtil.isNullOrZero(campaignCustomerDTO.getCompanySiteId())) {
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
            return resultDTO;
        }

        try {
            Page<CustomerCustomDTO> data = customerRepository.getIndividualCustomerInfo(campaignCustomerDTO, SQLBuilder.buildPageable(campaignCustomerDTO));

            resultDTO.setTotalRow(data.getTotalElements());
            resultDTO.setListData(data.getContent());
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO deleteCustomerFromCampaign(CampaignCustomerDTO campaignCustomerDTO) {
        LOGGER.info("--- START DELETE CUSTOMER FROM CAMPAIGN ::");
        ResultDTO resultDTO = new ResultDTO();
        Long companySiteId = campaignCustomerDTO.getCompanySiteId();
        Long campaignId = campaignCustomerDTO.getCampaignId();
        String[] lstCusId = campaignCustomerDTO.getLstCustomerId().split(",");
//        List<Short> lstStatus = campaignCustomerRepository.getStatus();
        try {
            List<CampaignCustomer> campaignCustomerList = campaignCustomerRepository.findCampaignCustomers2(campaignId, companySiteId, lstCusId);
            if (0 < campaignCustomerList.size()) {
                resultDTO.setErrorCode(Constants.ApiErrorCode.DELETE_ERROR);
                resultDTO.setDescription(Constants.ApiErrorDesc.DELETE_ERROR);
            } else {
                for (String cusId : lstCusId) {
                    CampaignCustomer campaignCustomer = campaignCustomerRepository.findCampaignCustomers1(campaignId, companySiteId, cusId);
                    if (null != campaignCustomer) {
                        campaignCustomer.setInCampaignStatus((short) 0);
                        campaignCustomer.setCustomerListId(null);
                        campaignCustomerRepository.save(campaignCustomer);
                    } else {
                        campaignCustomer = campaignCustomerRepository.findByCampaignIdAndCompanySiteIdAndInCampaignStatusAndStatusAndCustomerId(campaignId, companySiteId, (short) 1, (short) 0, Long.valueOf(cusId));
                        if (null != campaignCustomer) {
                            campaignCustomerRepository.delete(campaignCustomer);
                        }
                    }
                }
                resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
                resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO searchCampaignInformation(CampaignCustomerDTO campaignCustomerDTO) {
        LOGGER.info("--- START SEARCH CAMPAIGN INFORMATION ::");
        ResultDTO resultDTO = new ResultDTO();
        List<CampaignInformationDTO> list = customerRepository.getCampaignInformation(campaignCustomerDTO);
        try {
            if (list.size() > 0) {
                resultDTO.setData(list.get(0));
            } else {
                resultDTO.setData(null);
            }
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO addCustomerToCampaign(CampaignCustomerDTO campaignCustomerDTO) {
        LOGGER.info("--- START ADD CUSTOMER TO CAMPAIGN ::");
        ResultDTO resultDTO = new ResultDTO();
        Long companySiteId = campaignCustomerDTO.getCompanySiteId();
        Long campaignId = campaignCustomerDTO.getCampaignId();
        String[] lstCusId = campaignCustomerDTO.getLstCustomerId().split(",");
        try {
            for (String cusId : lstCusId) {
                CampaignCustomer entity = campaignCustomerRepository.findByCampaignIdAndCompanySiteIdAndCustomerIdAndInCampaignStatus(campaignId, companySiteId, Long.parseLong(cusId), (short) 0);
                if (entity != null) {
                    entity.setCompanySiteId(companySiteId);
                    entity.setStatus((short) 0);
                    entity.setCampaignId(campaignId);
                    entity.setCustomerId(Long.parseLong(cusId));
                    entity.setRecallCount(0L);
                    entity.setInCampaignStatus((short) 1);
                    campaignCustomerRepository.save(entity);
                } else {
                    entity = new CampaignCustomer();
                    entity.setCompanySiteId(companySiteId);
                    entity.setStatus((short) 0);
                    entity.setCampaignId(campaignId);
                    entity.setCustomerId(Long.parseLong(cusId));
                    entity.setRecallCount(0L);
                    entity.setInCampaignStatus((short) 1);
                    campaignCustomerRepository.save(entity);
                }
            }
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }
        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO getDataForCombobox(CampaignCustomerDTO campaignCustomerDTO) {
        LOGGER.info("--- START GET DATA FOR COMBOBOX ::");
        ResultDTO resultDTO = new ResultDTO();
        List<ComboboxDTO> returnList = new ArrayList<>();

        try {
            returnList.add(new ComboboxDTO("null", "Lựa Chọn"));
            List<CustomizeFieldOptionValue> cfov = customizeFieldOptionValueRepository.findCustomizeFieldOptionValueByFieldOptionIdOrderByName(campaignCustomerDTO.getField());
            for (CustomizeFieldOptionValue item : cfov) {
                returnList.add(new ComboboxDTO(item.getFieldOptionValueId().toString(), item.getName()));
            }

            resultDTO.setListData(returnList);
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public List<Customer> findAllByCondition(Long siteId, Date endTime) {
        return customerRepository.findAllByCondition(siteId, endTime);
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public Customer update(Customer c) {
        return customerRepository.save(c);
    }

    @Override
    public List<Customer> searchByQuery(String queryString) {
        RSQLVisitor<CriteriaQuery<Customer>, EntityManager> visitor = new JpaCriteriaQueryVisitor<>();
        CriteriaQuery<Customer> query;
        query = getCriteriaQuery(queryString, visitor);
        List<Customer> resultList = entityManager.createQuery(query).getResultList();
        if (resultList == null || resultList.isEmpty()) {
            return Collections.emptyList();
        }
        return resultList;
    }

    @Override
    public Long countByQuery(String queryString) {
        RSQLVisitor<CriteriaQuery<Long>, EntityManager> visitor = new JpaCriteriaCountQueryVisitor<Customer>();
        CriteriaQuery<Long> query;
        query = getCriteriaQuery(queryString, visitor);

        return entityManager.createQuery(query).getSingleResult();
    }


    private <T> CriteriaQuery<T> getCriteriaQuery(String queryString, RSQLVisitor<CriteriaQuery<T>, EntityManager> visitor) {
        Node rootNode;
        CriteriaQuery<T> query;
        try {
            rootNode = new RSQLParser().parse(queryString);
            query = rootNode.accept(visitor, entityManager);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ;
            throw new IllegalArgumentException(e.getMessage());
        }
        return query;
    }

//        params.put('1', ["AND","AGE",">=","30"] )
//        lst.push(params);
//        StringBuilder sql = "Select ..... WHERE 1 = 1 "
//        for(lst){
//            String[] data = lst[i].value
//            sql.append(data[0] + data[1] + data[2] + data[3]);
//        }


//    Map<String, String> params = new HashMap<>();


    //    public ResultDTO searchCustomize(List<CustomizeFields> customizeFields) {
//        Map<String, CustomizeRequestDTo> hashCustomer = new HashMap<>();
//        CustomizeRequestDTo customizeRequestDTo = new CustomizeRequestDTo();
//        hashCustomer.put("1", customizeRequestDTo);
//        hashCustomer.put("2", customizeRequestDTo);
//        hashCustomer.put("3", customizeRequestDTo);
//        StringBuilder sb = new StringBuilder();
//        sb.append("");
//        sb.append("");
//        sb.append("");
//        sb.append("");
    @Override
    public ResultDTO listCustomizeFields(CustomizeFieldsDTO customizeFields) {
        LOGGER.info("--- START GET LIST CUSTOMIZE FIELDS ::");
        ResultDTO resultDTO = new ResultDTO();
        Locale locale = Locale.forLanguageTag(customizeFields.getLanguage());
        try {
            List<CustomizeFields> lstCustomizeFields = customizeFieldsRepository.findByFunctionCodeAndActiveAndStatusAndSiteId("CUSTOMER", 1L, 1L, customizeFields.getSiteId());
            String cf[][] = {
                    {"-1", BundleUtils.getLangString("CODE", locale), "text"},
                    {"-2", BundleUtils.getLangString("NAME", locale), "text"},
                    {"-3", BundleUtils.getLangString("COMPANY_NAME", locale), "text"},
                    {"-4", BundleUtils.getLangString("GENDER", locale), "combobox"},
                    {"-5", BundleUtils.getLangString("CURRENT_ADDRESS", locale), "text"},
                    {"-6", BundleUtils.getLangString("PLACE_OF_BIRTH", locale), "text"},
                    {"-7", BundleUtils.getLangString("DATE_OF_BIRTH", locale), "date"},
                    {"-8", BundleUtils.getLangString("MOBILE_NUMBER", locale), "text"},
                    {"-9", "Email", "text"},
                    {"-10", BundleUtils.getLangString("USER_LOGIN", locale), "text"},
                    {"-11", BundleUtils.getLangString("CUSTOMER_TYPE", locale), "combobox"},
            };
            for (int x = 0; x < 11; x++) {
                CustomizeFields datafill = new CustomizeFields();
                datafill.setCustomizeFieldId(Long.parseLong(cf[x][0]));
                datafill.setTitle(cf[x][1]);
                datafill.setType(cf[x][2]);
                lstCustomizeFields.add(datafill);
            }
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
            resultDTO.setListData(lstCustomizeFields);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        return resultDTO;
    }

    @Override
    @Transactional(DataSourceQualify.CCMS_FULL)
    public ResultDTO searchCustomizeFields(CampaignCustomerDTO campaignCustomerDTO, UserSession userSession) {
        LOGGER.info("--- START SEARCH CUSTOMIZE FIELDS ::");
        ResultDTO resultDTO = new ResultDTO();

        try {
            Page<CustomerDTO> data = customerRepository.getCustomizeFields(campaignCustomerDTO, userSession, SQLBuilder.buildPageable(campaignCustomerDTO));
            resultDTO.setListData(data.getContent());
            resultDTO.setTotalRow(data.getTotalElements());
            resultDTO.setErrorCode(Constants.ApiErrorCode.SUCCESS);
            resultDTO.setDescription(Constants.ApiErrorDesc.SUCCESS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultDTO.setErrorCode(Constants.ApiErrorCode.ERROR);
            resultDTO.setDescription(Constants.ApiErrorDesc.ERROR);
        }

        LOGGER.info("--- SEARCH CUSTOMIZE FIELDS RESPONSE ::" + resultDTO.getErrorCode());
        return resultDTO;
    }

    private PhoneNumberRankDTO creatPhoneRank(CustomerContact customerContact, Customer customer, String companyId) {
        PhoneNumberRankDTO phoneNumberRankDTO;
        phoneNumberRankDTO = new PhoneNumberRankDTO();
        phoneNumberRankDTO.setCurrentRank(customer.getCustomerType() != null ? customer.getCustomerType().intValue() : 1);
        phoneNumberRankDTO.setSyncedRank(customer.getCustomerType() != null ? customer.getCustomerType().intValue() : 1);
        phoneNumberRankDTO.setLastSyncTime(new Date(System.currentTimeMillis()).getTime() / 1000);
        if (phoneNumberRankDTO.getCreateTime() == null) {
            phoneNumberRankDTO.setCreateTime(new Date(System.currentTimeMillis()).getTime() / 1000);
        }
        phoneNumberRankDTO.setAccountId(companyId);
        phoneNumberRankDTO.setLastUpdateTime(new Date(System.currentTimeMillis()).getTime() / 1000);
        phoneNumberRankDTO.setPhoneNumber(DataUtil.formatPhoneNumberMy(customerContact.getContact()));
        phoneNumberRankDTO.setPartitionHelper(Integer.parseInt(customerContact.getContact().substring(customerContact.getContact().length() - 3)));
        return phoneNumberRankDTO;
    }

    private PhoneNumberRankDTO updatePhoneRank(CustomerContact customerContact, Customer customer, String companyId) {
        PhoneNumberRankDTO phoneNumberRankDTO;
        // Chi can update so dien thoai chinh sdt phu bo va cac kenh khac bo qua
        if (customerContact.getContactType() != Constants.CHANNEL_ID.VOICE) return null;
        if (customerContact.getContactType() == Constants.CHANNEL_ID.VOICE
                && customerContact.getIsDirectLine() != Constants.PHONE_TYPE.MAIN_PHONE)
            return null;
        phoneNumberRankDTO = new PhoneNumberRankDTO();
        phoneNumberRankDTO.setCurrentRank(customer.getCustomerType() != null ? customer.getCustomerType().intValue() : 1);
        phoneNumberRankDTO.setSyncedRank(customer.getCustomerType() != null ? customer.getCustomerType().intValue() : 1);
        phoneNumberRankDTO.setLastSyncTime(new Date(System.currentTimeMillis()).getTime() / 1000);
        if (phoneNumberRankDTO.getCreateTime() == null) {
            phoneNumberRankDTO.setCreateTime(new Date(System.currentTimeMillis()).getTime() / 1000);
        }
        phoneNumberRankDTO.setAccountId(companyId);
        phoneNumberRankDTO.setLastUpdateTime(new Date(System.currentTimeMillis()).getTime() / 1000);
        String standardedPhoneNumber = DataUtil.formatPhoneNumberMy(customerContact.getContact());
        phoneNumberRankDTO.setPhoneNumber(standardedPhoneNumber);
        phoneNumberRankDTO.setPartitionHelper(Integer.parseInt(standardedPhoneNumber.substring(standardedPhoneNumber.length() - 3)));
        return phoneNumberRankDTO;
    }
}
