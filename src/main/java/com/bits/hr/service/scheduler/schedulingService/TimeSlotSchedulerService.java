package com.bits.hr.service.scheduler.schedulingService;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.FlexSchedule;
import com.bits.hr.domain.TimeSlot;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.repository.FlexScheduleRepository;
import com.bits.hr.repository.TimeSlotRepository;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TimeSlotSchedulerService {

    @Autowired
    TimeSlotRepository timeSlotRepository;

    public void checkEmptyTimeSlotCode() {
        List<TimeSlot> timeSlotList = timeSlotRepository.findAll();
        try {
            for (TimeSlot timeSlot : timeSlotList) {
                if (timeSlot.getCode() == null) {
                    String code = "TS-" + timeSlot.getId();
                    timeSlot.setCode(code);
                    timeSlotRepository.save(timeSlot);
                }
            }
        } catch (Exception ex) {
            log.error(ex);
        }
    }
}
