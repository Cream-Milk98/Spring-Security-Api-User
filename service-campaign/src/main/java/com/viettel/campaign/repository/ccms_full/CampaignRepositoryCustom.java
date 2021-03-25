package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.web.dto.CampaignDTO;
import com.viettel.campaign.web.dto.CampaignInformationDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.request_dto.CampaignRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CampaignRepositoryCustom {

    ResultDTO search(CampaignRequestDTO requestDto);

    Page<CampaignDTO> findByCampaignCode(CampaignRequestDTO requestDTO);

    String getMaxCampaignIndex();

    ResultDTO checkAllowStatusToPrepare(Long campaignId);

    //<editor-fold: hungtt>
    ResultDTO findCustomerListReallocation(CampaignRequestDTO dto);

    ResultDTO getListFieldsNotShow(CampaignRequestDTO dto);

    ResultDTO getListFieldsToShow(CampaignRequestDTO dto);

    ResultDTO getCampaignCustomerList(CampaignRequestDTO dto);

    ResultDTO getCustomerList(CampaignRequestDTO dto);

    ResultDTO getCustomerChoosenList(CampaignRequestDTO dto);

    CampaignInformationDTO getCampaignCustomerInformation(CampaignRequestDTO dto);

    List<CampaignInformationDTO> getCustomerListInformation(CampaignRequestDTO dto);

    List<CampaignInformationDTO> getCountIndividualOnList(CampaignRequestDTO dto);

    //</editor-fold>
}
