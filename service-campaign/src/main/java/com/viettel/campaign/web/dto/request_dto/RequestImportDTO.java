package com.viettel.campaign.web.dto.request_dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author anhvd_itsol
 */

@Getter
@Setter
public class RequestImportDTO {
    CommonsMultipartFile file;
}
