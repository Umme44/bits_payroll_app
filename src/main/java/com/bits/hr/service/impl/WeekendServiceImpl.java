package com.bits.hr.service.impl;

import com.bits.hr.service.WeekendService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class WeekendServiceImpl implements WeekendService {

    @Override
    public boolean checkWeekendByLocalDate(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.FRIDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY;
    }
}
