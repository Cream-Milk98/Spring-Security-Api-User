package com.viettel.campaign.service;

import com.viettel.campaign.model.ccms_full.Campaign;
import com.viettel.campaign.model.ccms_full.TimeRangeDialMode;
import com.viettel.campaign.model.ccms_full.TimeZoneDialMode;
import com.viettel.campaign.web.dto.CampaignDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.request_dto.CampaignRequestDTO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface CampaignService {

    ResultDTO search(CampaignRequestDTO requestDto);

    ResultDTO findByCampaignCode(CampaignRequestDTO requestDTO);

    List<Campaign> findAllCondition(Long companySiteId);

    List<Campaign> findCampaignByCompanySiteIdAndStartTimeIsLessThanEqualAndStatusIn(Long siteId, Date startTime, List<Long> status);

    List<Campaign> findCampaignByCompanySiteIdAndEndTimeIsLessThanEqualAndStatusIn(Long siteId, Date startTime, List<Long> status);

    Campaign updateProcess(Campaign c);

    ResultDTO findByCampaignId(Long campaignId, Integer offSet);

    ResultDTO addNewCampaign(CampaignDTO campaignDTO) throws ParseException;

    ResultDTO findCampaignById(Long campaignId);

    ResultDTO changeCampaignStatus(CampaignDTO dto);

    ResultDTO checkAllowStatusToPrepare(Long campaignId);

    XSSFWorkbook exportCampaigns(CampaignRequestDTO dto);

    List<TimeRangeDialMode> getCampaignTimeRangeMode (Long campaignId, Long companySiteId, Integer timeZoneOffset);

    List<TimeZoneDialMode> getCampaignTimeZoneMode (Long campaignId, Long companySiteId);

    ResultDTO renewCampaign(CampaignDTO campaignDTO);

    ResultDTO updateCampaign(CampaignDTO dto) throws ParseException;

    //<editor-fold: hungtt>
    ResultDTO findCustomerListReallocation(CampaignRequestDTO dto);

    ResultDTO reallocationCustomer(CampaignRequestDTO dto);

    ResultDTO getListFieldsNotShow(CampaignRequestDTO dto);

    ResultDTO getListFieldsToShow(CampaignRequestDTO dto);

    ResultDTO getCampaignCustomerList(CampaignRequestDTO dto);

    ResultDTO getCustomerList(CampaignRequestDTO dto);

    ResultDTO getCustomerChoosenList(CampaignRequestDTO dto);

    ResultDTO addCustomerListToCampaign(CampaignRequestDTO dto);

    ResultDTO deleteCustomerListFromCampaign(CampaignRequestDTO dto);

    ResultDTO saveFieldCustomer(CampaignRequestDTO dto);

    ResultDTO getCampaignCustomerInformation(CampaignRequestDTO dto);

    ResultDTO getCustomerListInformation(CampaignRequestDTO dto);

    ResultDTO getCountIndividualOnList(CampaignRequestDTO dto);

    ResultDTO saveCustomerCampaign(CampaignRequestDTO dto);

    ResultDTO getConnectStatus(Long companySiteId);
    //</editor-fold>

}
