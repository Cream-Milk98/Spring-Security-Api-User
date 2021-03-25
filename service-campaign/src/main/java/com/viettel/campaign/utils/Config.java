package com.viettel.campaign.utils;

import com.viettel.security.PassTranformer;
import org.apache.logging.log4j.core.config.ConfigurationSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author hanv_itsol
 * @project campaign
 */
public class Config {

    public static final String APP_CONF_FILE_PATH = System.getProperty("user.dir") + File.separator + "etc" + File.separator + "app.conf";
    //    public static final String LOG_CONF_FILE_PATH = System.getProperty("user.dir") + File.separator + "etc" + File.separator + "log.conf";
    public static final String EXCEL_DIR = System.getProperty("user.dir") + File.separator + "etc" + File.separator + "upload";

    public static String amcd_xmlrpc_url;
    public static int num_client_amcd_xmlprc;

    public static final String SALT = "Ipcc#987654321#@!";


//    public static final int AGENT_ANSWER_TIMEOUT = 10;
//    public static final int AGENT_ACCEPT_TIMEOUT = 10;
//    public static final int SMS_ANSWER_TIMEOUT = 24 * 60 * 60;
//    public static final int INTERVAL_SCAN_SMS = 100;
//    public static final int NUM_SMSGW = 1;
//    public static final int NUM_RETRY_SEND_SMSGW = 3;
//    public static final int INTERVAL_RETRY_SEND_SMSGW = 10;
//    public static final String SMS_SEND_OUT_CHANNEL = "198";

//    public static final int INTERVAL_SCAN_FACEBOOK = 100;
//    public static final int FACEBOOK_ANSWER_TIMEOUT = 24 * 60 * 60;

//    public static final String prefixKeyRedis = "";
//    public static final String redisPatternSMS = "";
//    public static final String redisPatternFacebook = "";


    //    public static final String rabbitConnection;
//    public static final String fbGatewayUser;
//    public static final String fbGatewayPass;
//    public static final String virtual_host;
//    public static final String rb_queuename_kpi_log;
//    public static final String facebook_text_queue_name;
//    public static final String app_code;
//    public static final String TICKET_SERVICES_URL;
    public static final int redisTimeout;
    public static final String redisAddress;
    public static final String phoneNumberStart;
    public static final String maxRecordImport;
    public static final String timeEditInteractionResult;
    public static final String regexPhoneNumberMy;
    public static final String prefixPhoneNumberMy;
    public static final Long timeZone;

//    public static String link_services_ticket;

    private static final Properties properties = new Properties();

    static {
//        org.apache.log4j.PropertyConfigurator.configure(Config.LOG_CONF_FILE_PATH);
        ConfigurationSource source = null;
        try {
            properties.load(new FileInputStream(APP_CONF_FILE_PATH));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        PassTranformer.setInputKey("Ipcc#987654321#@!");

//        rabbitConnection = properties.getProperty("rabbit_connection_string");
//        fbGatewayUser = PassTranformer.decrypt(properties.getProperty("rabbit_user", "").trim());
//        fbGatewayPass = PassTranformer.decrypt(properties.getProperty("rabbit_pass", "").trim());
//        virtual_host = properties.getProperty("virtual_host", "/").trim();
//        facebook_text_queue_name = properties.getProperty("facebook_text_queue_name", "mt2facebooktext").trim();
//        rb_queuename_kpi_log = properties.getProperty("rb_queuename_kpi_log", "econtact_kpi_log").trim();
//        app_code = properties.getProperty("app_code", "ECONTACT").trim();
//        TICKET_SERVICES_URL = properties.getProperty("ticket_services_url", "").trim();

        redisAddress = properties.getProperty("redis_address", "").trim();
        phoneNumberStart = properties.getProperty("phone_number_start", "").trim();
        maxRecordImport = properties.getProperty("max_record_import", "").trim();
        timeEditInteractionResult = "".equals(properties.getProperty("time_edit_interaction_result", "").trim()) ? "1" : properties.getProperty("time_edit_interaction_result", "").trim();
        redisTimeout = Integer.valueOf(properties.getProperty("redis_timeout", "3000").trim());

        regexPhoneNumberMy = properties.getProperty("regex_phone_number_my", "").trim();
        prefixPhoneNumberMy = properties.getProperty("prefix_phone_number_my", "").trim();
        timeZone = Long.valueOf(properties.getProperty("timezone", "0").trim());
//        link_services_ticket = properties.getProperty("link_services_ticket");
    }

    private static String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue).trim();
    }

    private static int getInt(String key, int defaultValue) {
        return Integer.valueOf(properties.getProperty(key, "" + defaultValue).trim());
    }
}
