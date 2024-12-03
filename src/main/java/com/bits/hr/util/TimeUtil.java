package com.bits.hr.util;

import java.time.Instant;
import java.time.ZoneId;

public class TimeUtil {

    public static String getHourAndMinute(Instant instant) {
        int hour = instant.atZone(ZoneId.of("Asia/Dhaka")).getHour();
        int minute = instant.atZone(ZoneId.of("Asia/Dhaka")).getMinute();
        return hour + ":" + minute;
    }
}
