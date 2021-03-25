package com.viettel.campaign.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Report DTO class
 */
@Data
public class Report {
    private String title;
    private String name;
    private List<Parameter> parameters;

    public Report(String title, String name) {
        this.title = title;
        this.name = name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Parameter {
        private String title;
        private String name;
        private ParameterType type;

    }

    public enum ParameterType {
        INT, STRING
    }
}
