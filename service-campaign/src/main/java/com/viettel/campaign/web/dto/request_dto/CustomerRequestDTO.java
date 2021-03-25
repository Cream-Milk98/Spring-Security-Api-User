package com.viettel.campaign.web.dto.request_dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerRequestDTO {
    Long customerListId;
    List<Long> ids;
    Long companySiteId;
}
