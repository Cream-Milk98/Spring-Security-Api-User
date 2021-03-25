/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.campaign.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author phamky
 */
public class TimeZoneUtils {

    private static Long getTimeZone(TimeZone tz) {
        return TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
    }

    private static Long getTimeZoneToMinutes(TimeZone tz) {
        return TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset());
    }

    public static String toQueryTimeZoneZero(Date date){
        String dateStr = toDateStringWithTimeZoneZero(date);
        return "to_date( '"+dateStr+"', 'DD/MM/YYYY HH24:MI:SS')";
    }

    public static String changeTimeZoneString(String dateStr, TimeZone tz){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = sdf.parse(dateStr);
            sdf.setTimeZone(tz);
            return sdf.format(date);

        } catch (ParseException ex) {
        }
        return "";
    }

    public static String changeTimeZoneStringPattern(String dateStr, TimeZone tz, String pattern){
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = sdf1.parse(dateStr);
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setTimeZone(tz);
            return sdf.format(date);

        } catch (ParseException ex) {
        }
        return "";
    }

    public static String toDateStringWithTimeZoneZero(Date date){
        TimeZone tz = getZone(0l);
        return toDateStringWithTimeZone(date, tz);
    }

    public static String toDateStringWithTimeZonePattern(Date date, long tz, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(getZone(tz));
        return sdf.format(date);
    }

//    public static String toHourStringWithTimeZonePattern(Long hour, long tz){
//        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//        sdf.setTimeZone(getZone(tz));
//        return sdf.format(date);
//    }


    public static String toDateStringWithTimeZonePattern(Date date, TimeZone tz, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(tz);
        return sdf.format(date);
    }


    public static String toDateStringWithTimeZone(Date date, TimeZone tz){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(tz);
        return date == null ? null : sdf.format(date);
    }

    public static TimeZone getZone(Long timeZone){
        if(timeZone == null){
            //timezone is null, return default
            return TimeZone.getDefault();
        }
        String[] ids = TimeZone.getAvailableIDs();
        for (String id : ids) {
            TimeZone tz = TimeZone.getTimeZone(id);
            if(getTimeZone(tz).equals(timeZone)){
                //zone need change
                return tz;
            }
        }
        return TimeZone.getDefault();
    }

    public static TimeZone getZoneMinutes(Long timeZone){
        if(timeZone == null){
            //timezone is null, return default
            return TimeZone.getDefault();
        }
        String[] ids = TimeZone.getAvailableIDs();
        for (String id : ids) {
            TimeZone tz = TimeZone.getTimeZone(id);
            if(getTimeZoneToMinutes(tz).equals(timeZone)){
                //zone need change
                return tz;
            }
        }
        return TimeZone.getDefault();
    }

    public static Date changeTimeZone(Date date, Long timeZone){
        if(timeZone == null){
            //timezone is null, do not change timezone
            return date;
        }
        String[] ids = TimeZone.getAvailableIDs();
        for (String id : ids) {
            TimeZone tz = TimeZone.getTimeZone(id);
            if(getTimeZone(tz).equals(timeZone)){
                //zone need change

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                df.setTimeZone(tz);
                try {
                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date newDate = df2.parse(df.format(date));
                    System.out.println(df2.format(newDate));
                    return newDate;
                } catch (ParseException ex) {
                }
            }
        }
        return date;
    }

    public static String toDateStringWithTimeZoneWithoutTime(Date date, TimeZone tz){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(tz);
        return date == null ? null : sdf.format(date);
    }

   public static Date convertStringToDate(String date, String pattern, TimeZone tz) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(tz);
        return dateFormat.parse(date);
    }

    public static Date convertStringToDateTime(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.parse(date);
    }

    public static String convertDateToStringTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return date == null ? null : sdf.format(date);
    }

    public static String convertDateToStringDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return date == null ? null : sdf.format(date);
    }

}
