package com.viettel.campaign.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.joda.time.format.DateTimeFormat;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * @author anhvd_itsol
 */
@Slf4j
public final class DateTimeUtil {
    public static final long DAY_IN_MILLIS = 86400000L;

    public static interface DateTimeProvider {
        long now();
    }

    private static final AtomicReference<DateTimeProvider> PROVIDER = new AtomicReference<DateTimeProvider>();

    public static final DateTimeProvider getProvider() {
        return PROVIDER.get();
    }

    static {
        PROVIDER.set(new DateTimeProvider() {
            @Override
            public long now() {
                return System.currentTimeMillis();
            }
        });
    }

    public static Date currentTime() {
        return new Date(now());
    }

    public static Date currentDate() {
        return date(now());
    }

    public static final Date date(final long millis) {
        final int offset = getTimeZoneOffset(millis);
        long t = millis - ((t = (millis + offset) % DAY_IN_MILLIS) < 0 ? DAY_IN_MILLIS + t : t);
        t = t + (offset - getTimeZoneOffset(t));
        return new Date(t);
    }

    public static int getTimeZoneOffset(long millis) {
        return TimeZone.getDefault().getOffset(millis);
    }

    public static long now() {
        return getProvider().now();
    }

    public static long toMillis(long nanos) {
        return MILLISECONDS.convert(nanos, NANOSECONDS);
    }

    public static long currentTimeMillis() {
        return getProvider().now();
    }

    /**
     * Format
     */
    public static String format(String pattern, long millis) {
        return DateTimeFormat.forPattern(pattern).print(millis);
    }

    public static String format(String pattern, Date date, String defaultValue) {
        if (date == null) return defaultValue;
        return format(pattern, date.getTime());
    }

    /**
     *
     */
    public static boolean isValid(final long millis) {
        return millis > 0L;
    }

    /**
     *
     */
    public static long parse(final String pattern, String date) {
        return DateTimeFormat.forPattern(pattern).parseMillis(date);
    }

    public static Date parseDate(final String pattern, String date) {
        return Strings.isEmpty(date) ? null : date(parse(pattern, date));
    }

    /**
     *
     */

    public static boolean isRun(String cronExpression, Date lastCheck) {
        CronSequenceGenerator generator = new CronSequenceGenerator(cronExpression);
        Date nextExecutionDate = generator.next(lastCheck);

        return new Date().getTime() - nextExecutionDate.getTime() > 0;
    }

    public static Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Date minusTimezoneOffsetHours(Date date, Integer timezoneOffset) {
        if (date != null && timezoneOffset != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            Integer hours = timezoneOffset/60;
            cal.add(Calendar.HOUR, -hours);
            return cal.getTime();
        }
        return null;
    }

    public static boolean inTheSameDay(Date date1, Date date2) {
        if(date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);
            return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);

        }
        return false;
    }

    public static Date lastExecution2Long(String cronExpression){
        Date now = new Date();
        CronSequenceGenerator generator = new CronSequenceGenerator(cronExpression);
        return generator.next(now);
    }

}
