package com.viettel.campaign.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {

    private static final Logger logger = LoggerFactory.getLogger(DataUtil.class);

    public static boolean isNullOrZero(Long value) {
        return (value == null || value.equals(0L));
    }

    public static boolean isNullOrEmpty(String value) {
        return (value == null || value.isEmpty());
    }

    public static boolean isNullOrZero(BigDecimal value) {
        return (value == null || value.compareTo(BigDecimal.ZERO) == 0);
    }

    public static Long safeToLong(Object obj1, Long defaultValue) {
        Long result = defaultValue;
        if (obj1 != null) {
            try {
                result = Long.parseLong(obj1.toString());
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return result;
    }

    public static Long safeToLong(Object obj1) {
        Long result = null;
        if (obj1 != null) {
            try {
                result = Long.parseLong(obj1.toString());
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return result;
    }

    public static BigDecimal safeToBigDecimal(Object obj1) {
        BigDecimal result = new BigDecimal(0);
        if (obj1 != null) {
            try {
                result = BigDecimal.valueOf(Long.parseLong(obj1.toString()));
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return result;
    }

    public static Double safeToDouble(Object obj1) {
        Double result = null;
        if (obj1 != null) {
            try {
                result = Double.parseDouble(obj1.toString());
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return result;
    }

    public static Short safeToShort(Object obj1) {
        Short result = 0;
        if (obj1 != null) {
            try {
                result = Short.parseShort(obj1.toString());
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return result;
    }

    public static int safeToInt(Object obj1) {
        int result = 0;
        if (obj1 == null) {
            return 0;
        }
        try {
            result = Integer.parseInt(obj1.toString());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return result;
    }

    public static String safeToString(Object obj1) {
        if (obj1 == null) {
            return "";
        }

        return obj1.toString();
    }

    public static Date safeToDate(Object obj1) {
        Date result = null;
        if (obj1 == null) {
            return null;
        }
        try {
            result = (Date) obj1;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    public static boolean safeEqual(Long obj1, Long obj2) {
        if (obj1.equals(obj2)) {
            return true;
        }
        return obj2 != null && obj1.compareTo(obj2) == 0;
    }

    public static String convertDateToStringDDMMYYYYHHMISS(Date datetime) {
        String s = null;
        if (datetime != null) {
            Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            s = formatter.format(datetime);
        }
        return s;
    }

    public static Date convertStringToDateDDMMYYYYHHMISS(String str) {
        Date dateTime = null;
        try {
            if (str != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                dateTime = formatter.parse(str);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return dateTime;
    }

    public static String safeToClob(Clob clobValue) {
        String result = null;
        if (clobValue != null) {
            StringBuilder sb = new StringBuilder();
            try {
                Reader reader = clobValue.getCharacterStream();
                BufferedReader br = new BufferedReader(reader);
                String line;
                while (null != (line = br.readLine())) {
                    sb.append(line);
                }
                br.close();
            } catch (SQLException | IOException e) {
                logger.error(e.getMessage(), e);
            }
            result = sb.toString();
        }

        return result;
    }

    public static Date toDatefromStringFormat(String value, String format) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return date;
    }

    public static boolean isNumber(String strId) {
        return strId.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isLong(String strId) {
        return strId.matches("\\d*");
    }

    public static String standardPhone(String mobilePhone) {
        if (mobilePhone != null) {
            if (mobilePhone.startsWith("0")) {
                mobilePhone = mobilePhone.substring(1, mobilePhone.length());
            } else if (mobilePhone.startsWith("84")) {
                mobilePhone = mobilePhone.substring(2, mobilePhone.length());
            }
        }
        return mobilePhone;
    }

    public static String tohhmmss(Long conferenctime) {
        String fmt = "0";
        try {
            if (conferenctime == 0) {
                return "0";
            }
            // conferencetime: tinh theo s
            long mmt = conferenctime / 60;
            long ss = conferenctime % 60;
            long mm = mmt % 60;
            long hh = mmt / 60;
            fmt = replace(hh) + ":" + replace(mm) + ":" + replace(ss);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return fmt;
    }

    public static String replace(long m) {
        String mt = String.valueOf(m);
        mt = (m >= 10) ? mt : '0' + mt;
        return mt;
    }

    public static String getSafeFileName(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != '/' && c != '\\' && c != 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String standardPhoneNew(String input) {
        if (input == null) {
            return null;
        }
        while (input.startsWith("0")) {
            input = input.substring(1, input.length());
        }
        return input;
    }

    public static boolean isNullOrZero(Short value) {
        return (value == null || value.equals(Short.parseShort("0")));
    }

    public static boolean isNullOrZero(Integer value) {
        return (value == null || value.equals(Integer.parseInt("0")));
    }

    public static String formatPhoneNumberMy(String phoneNumber) {
        if (phoneNumber != null) {
            String[] regexList = Config.regexPhoneNumberMy.split(" ; ");
            String[] prefixList = Config.prefixPhoneNumberMy.split(";");
            for (int i = 0; i < regexList.length; i++) {
                Pattern p = Pattern.compile(regexList[i]);
                Matcher matcher = p.matcher(phoneNumber);
                if (matcher.find()) {
                    return prefixList.length > 0 ? prefixList[i] : "" + matcher.group(1);
                }
            }
        }
        return phoneNumber;
    }

    public static String removeNonBMPCharacters(String input) {
        if (input != null) {
            StringBuilder strBuilder = new StringBuilder();
            input.codePoints().forEach((i) -> {
                if (!Character.isSupplementaryCodePoint(i) && i < 100) {
                    strBuilder.append(Character.toChars(i));
                }
            });
            return strBuilder.toString();
        } else {
            return input;
        }
    }


    public static InetAddress getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
