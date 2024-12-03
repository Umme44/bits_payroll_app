package com.bits.hr.util;

import com.bits.hr.errors.BadRequestAlertException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DateUtil {

    public static LocalDate doubleToDate(Double d) {
        Date javaDate = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(d);
        //System.out.println(new SimpleDateFormat("MM/dd/yyyy").format(javaDate));
        LocalDate date = javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date;
    }

    public static LocalDate xlStringToDate(String string) {
        string = string.trim();
        try {
            return doubleToDate(Double.parseDouble(string));
        } catch (Exception e) {
            log.debug(e.getStackTrace());
            return LocalDate.now();
        }
    }

    public static Instant xlStringToDateTime(String string) {
        string = string.trim();
        try {
            double d = Double.parseDouble(string);
            Date javaDate = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(d);
            Instant date = javaDate.toInstant().atZone(ZoneId.systemDefault()).toInstant();
            return date;
        } catch (Exception e) {
            log.debug(e.getStackTrace());
            return Instant.now();
        }
    }

    public static boolean isBetween(LocalDate date, LocalDate min, LocalDate max) {
        return date.isAfter(min) && date.isBefore(max);
    }

    public static boolean isBetweenOrEqual(LocalDate date, LocalDate min, LocalDate max) {
        if (date.isEqual(min) || date.isEqual(max)) {
            return true;
        } else {
            return date.isAfter(min) && date.isBefore(max);
        }
    }

    public static Instant getInstantByDate(LocalDate date, Instant instant) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault());

        return date
            .atStartOfDay(ZoneId.systemDefault())
            .withHour(localDateTime.getHour())
            .withMinute(localDateTime.getMinute())
            .withSecond(00)
            .withNano(00)
            .toInstant();
    }

    public static boolean isGreaterThenOrEqual(LocalDate a, LocalDate b) {
        // a>=b ??
        return a.isBefore(b) || a.isEqual(b);
    }

    public static boolean isSmallerThenOrEqual(LocalDate a, LocalDate b) {
        // a>=b ??
        return a.isAfter(b) || a.isEqual(b);
    }

    public static String getDayOfMonthSuffix(final int n) {
        if (n <= 1 || n >= 31) {
            throw new IllegalArgumentException("illegal day of month: " + n);
        }
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static String formatDateAsDDDDMMMMYYYY(LocalDate localDate) {
        return localDate.format(
            DateTimeFormatter.ofPattern("dd'" + getDayOfMonthSuffix(localDate.getDayOfMonth()) + "' MMMM uuuu", Locale.ENGLISH)
        );
    }
}
