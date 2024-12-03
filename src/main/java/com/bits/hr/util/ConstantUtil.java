package com.bits.hr.util;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.FlexSchedule;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.FlexScheduleService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConstantUtil {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    FlexScheduleService flexScheduleService;

    // return start time , should be rep[laced when flex time feature implemented
    public LocalDateTime getStartTimeByPin(String pin, LocalDate date) {
        // ignore year month day , only time is concern here
        //        if (employeeRepository.findEmployeeByPin(pin).isPresent() && employeeRepository.findEmployeeByPin(pin).get().getCheckInTime() != null) {
        //            //return LocalDateTime.ofInstant(employeeRepository.findEmployeeByPin(pin).get().getCheckInTime(),ZoneOffset.systemDefault());
        //            // todo :: fixed data
        //        }

        try {
            long employeeId = employeeRepository.getIdByPin(pin);
            FlexSchedule flexSchedule = flexScheduleService.getEffectiveFlexSchedule(employeeId, date);
            Instant inTime = flexSchedule.getInTime();
            return LocalDateTime.ofInstant(inTime, ZoneId.systemDefault());
        } catch (Exception exception) {
            return LocalDateTime.of(2020, 1, 1, 10, 0, 0);
        }
        /*Optional<Employee> employee = employeeRepository.findEmployeeByPin(pin);
        if(employee.isPresent() && employee.get().getCurrentInTime()!=null){
            Instant inTime = employee.get().getCurrentInTime();
            return LocalDateTime.ofInstant(inTime, ZoneId.systemDefault());
        }
        return LocalDateTime.of(2020, 1, 1, 10, 0, 0);*/
    }

    // return start time , should be rep[laced when flex time feature implemented
    public double getOfficeTimeDurationByPin(String pin) {
        // ignore year month day , only time is concern here
        //        if (employeeRepository.findEmployeeByPin(pin).isPresent() && employeeRepository.findEmployeeByPin(pin).get().getOfficeTimeDuration() != null) {
        //        // double officeTime = employeeRepository.findEmployeeByPin(pin).get().getOfficeTimeDuration();
        //        // return officeTime > 24 ? 8d : officeTime;
        //            return 8.00d;
        //        }

        try {
            long employeeId = employeeRepository.getIdByPin(pin);
            FlexSchedule flexSchedule = flexScheduleService.getEffectiveFlexSchedule(employeeId, LocalDate.now());
            Instant inTime = flexSchedule.getInTime();
            Instant outTime = flexSchedule.getOutTime();
            return ChronoUnit.HOURS.between(inTime, outTime);
        } catch (Exception exception) {
            return 8.00d;
        }
        /*Optional<Employee> employee = employeeRepository.findEmployeeByPin(pin);
        if(employee.isPresent() && employee.get().getCurrentInTime()!=null && employee.get().getCurrentOutTime()!=null){
            return ChronoUnit.HOURS.between(employee.get().getCurrentInTime(), employee.get().getCurrentOutTime());
        }
        else {
            return 8.00d;
        }*/
    }
}
