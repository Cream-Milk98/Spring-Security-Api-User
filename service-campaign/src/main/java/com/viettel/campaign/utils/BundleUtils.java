package com.viettel.campaign.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author hungtt
 */
public class BundleUtils {
    protected static final Logger logger = LoggerFactory.getLogger(BundleUtils.class);
    private static ResourceBundle rsConfig = null;
    private static final String GLOBAL_CONFIG = "config/globalConfig";

    public static String getLangString(String key, Locale... locale) {
        Locale vi = new Locale("vi");
        Locale mlocale = vi;

        try {
            if (locale != null) {
                if (locale.length == 0) {
                    rsConfig = ResourceBundle.getBundle(Constants.LANGUAGE.LANGUAGE, mlocale);
                } else {
                    rsConfig = ResourceBundle.getBundle(Constants.LANGUAGE.LANGUAGE, locale[0]);
                }
            } else {
                rsConfig = ResourceBundle.getBundle(Constants.LANGUAGE.LANGUAGE, mlocale);
            }
            return new String(rsConfig.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            return key;
        } catch (MissingResourceException mre) {
            logger.error(mre.getMessage(), mre);
            return null;
        }
    }

    public static String getGlobalConfig(String configKey) {
        String result = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(GLOBAL_CONFIG);
            result = bundle.getString(configKey);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }
}
