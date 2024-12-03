package com.bits.hr.service.attendanceTimeSheet;

import com.bits.hr.domain.enumeration.AttendanceStatus;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AttendanceTimeSheetMiniService {

    // get list of attendance by pin in a hashtable
    // key will be LocalDate , one employee will have one unique date in attendance Table
    // now mapping will be efficient as hashtable data can be retrieved at O(1) complexity
    public List<AttendanceTimeSheetMini> getAttendanceTimeSheet(
        List<LocalDate> dateList,
        HashSet<LocalDate> holidaysHashset,
        HashSet<LocalDate> leaveApplicationHashSet,
        HashSet<LocalDate> attendanceEntryHashSet,
        HashSet<LocalDate> movementEntryHashSet
    ) {
        //        StopWatch stopWatch = new StopWatch();
        //        stopWatch.start("data preparation stage");
        List<AttendanceTimeSheetMini> attendanceTimeSheetMiniList = new ArrayList<>();

        //        stopWatch.stop();

        //        stopWatch.start("data process stage");
        //        dateList.parallelStream()

        // main loop between start date and end date
        //        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
        for (LocalDate date : dateList) {
            AttendanceTimeSheetMini attendanceTimeSheetMini = new AttendanceTimeSheetMini();
            attendanceTimeSheetMini.setDate(date);

            // priority
            // 1. attendance entry ==> present
            // 2. holiday entry ==> holiday
            // 3. weakly holiday ==> govt. holiday
            // 4. leave entry ==> leave
            // 5. still now nothing found ==> absent
            if (attendanceEntryHashSet.contains(date)) {
                attendanceTimeSheetMini.setAttendanceStatus(AttendanceStatus.PRESENT);
            } else if (movementEntryHashSet.contains(date)) {
                attendanceTimeSheetMini.setAttendanceStatus(AttendanceStatus.MOVEMENT);
            } else if (holidaysHashset.contains(date)) {
                attendanceTimeSheetMini.setAttendanceStatus(AttendanceStatus.GOVT_HOLIDAY);
            } else if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.FRIDAY) {
                attendanceTimeSheetMini.setAttendanceStatus(AttendanceStatus.WEEKLY_OFFDAY);
            } else if (leaveApplicationHashSet.contains(date)) {
                attendanceTimeSheetMini.setAttendanceStatus(AttendanceStatus.LEAVE);
            } else {
                attendanceTimeSheetMini.setAttendanceStatus(AttendanceStatus.ABSENT);
            }
            attendanceTimeSheetMiniList.add(attendanceTimeSheetMini);
        }
        //        stopWatch.stop();

        return attendanceTimeSheetMiniList;
    }
}
