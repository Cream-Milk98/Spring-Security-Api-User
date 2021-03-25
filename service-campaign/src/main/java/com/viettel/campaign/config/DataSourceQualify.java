package com.viettel.campaign.config;

/**
 * @author anhvd_itsol
 */
public class DataSourceQualify {
    public static final String CCMS_FULL = "ccmsFullTransactionManager";
    public static final String ACD_FULL = "acdFullTransactionManager";
    public static final String CHAINED = "chainedTransactionManager";
    public static final String CCMS_FULL_CHAINED = "ccmsFullChainedTransactionManager";

    public static final String JPA_UNIT_NAME_CCMS_FULL ="PERSITENCE_UNIT_NAME_1";
    public static final String JPA_UNIT_NAME_ACD_FULL ="PERSITENCE_UNIT_NAME_2";

    public static final String NAMED_JDBC_PARAMETER_TEMPLATE_CCMS_FULL ="NAMED_JDBC_PARAMETER_TEMPLATE_CCMS_FULL";
    public static final String NAMED_JDBC_PARAMETER_TEMPLATE_ACD_FULL ="NAMED_JDBC_PARAMETER_TEMPLATE_ACD_FULL";
}
