package com.bits.hr.service.importXL.ams;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.TimeSlot;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import lombok.Data;

@Data
public class FlexScheduleImportDTO {

    private Employee employee;
    private LocalDate effectiveDate;
    private LocalDate effectiveEndDate;
    private Instant inTime;
    private Instant outTime;
    private TimeSlot timeSlot;

    public FlexScheduleImportDTO(
        Employee employee,
        LocalDate effectiveDate,
        LocalDate effectiveEndDate,
        Instant inTime,
        Instant outTime,
        TimeSlot timeSlot
    ) {
        this.employee = employee;
        this.effectiveDate = effectiveDate;
        this.effectiveEndDate = effectiveEndDate;
        this.inTime = inTime;
        this.outTime = outTime;
        this.timeSlot = timeSlot;
    }
}
