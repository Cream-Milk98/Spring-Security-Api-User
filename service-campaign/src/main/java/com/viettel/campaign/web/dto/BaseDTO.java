package com.viettel.campaign.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseDTO implements Serializable {

    protected String keySet;
    protected Integer pageSize;
    protected Integer page;
    protected String sort;
    protected String langKey;
    protected Long timezone;
}
