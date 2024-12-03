package com.bits.hr.service.attendanceSync;

import com.bits.hr.domain.Employee;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.AttendanceEntryService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.AttendanceEntryDTO;
import com.bits.hr.service.dto.EmployeeDTO;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OnlinePunchService {

    @Autowired
    CurrentEmployeeService currentEmployeeService;

    @Autowired
    AttendanceEntryService attendanceEntryService;

    @Autowired
    EmployeeRepository employeeRepository;

    public boolean punch() {
        try {
            AttendanceEntryDTO attendanceEntryDTO = new AttendanceEntryDTO();
            Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
            if (!employeeOptional.isPresent()) {
                log.info("no employee associated to perform online attendance. ");
                return false;
            }
            if (
                employeeOptional.get().getIsAllowedToGiveOnlineAttendance() != null &&
                employeeOptional.get().getIsAllowedToGiveOnlineAttendance() == false
            ) {
                log.info("Not eligible to perform online attendance. ");
                return false;
            }
            long id = employeeOptional.get().getId();
            // get today's attendance entry by employee id
            // if not available then check in else check out
            if (attendanceEntryService.findByDateAndEmployeeId(LocalDate.now(), id).isPresent() == true) {
                // out logic , UPDATE OUT TIME
                String outNote = attendanceEntryDTO.getOutNote();

                attendanceEntryDTO = attendanceEntryService.findByDateAndEmployeeId(LocalDate.now(), id).get();
                if (attendanceEntryDTO.getOutTime() == null) {
                    attendanceEntryDTO.setOutTime(Instant.now());
                }
                attendanceEntryDTO.setOutNote(outNote);
                attendanceEntryService.update(attendanceEntryDTO);
                return true;
            } else {
                //in logic
                attendanceEntryDTO.setDate(LocalDate.now()); // current date
                attendanceEntryDTO.setInTime(Instant.now()); // in time = request hitting time in api
                attendanceEntryDTO.setOutTime(null); // if in then out should stay null
                attendanceEntryDTO.setEmployeeId(id);
                attendanceEntryService.create(attendanceEntryDTO);
                return true;
            }
        } catch (Exception ex) {
            log.info(ex);
            return false;
        }
    }

    public AttendanceEntryDTO currentDateStatus() {
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) throw new NoEmployeeProfileException();
        long id = employeeOptional.get().getId();
        if (attendanceEntryService.findByDateAndEmployeeId(LocalDate.now(), id).isPresent() == true) {
            return attendanceEntryService.findByDateAndEmployeeId(LocalDate.now(), id).get();
        } else {
            return null;
        }
    }

    public String createAttendanceEntry(AttendanceEntryDTO attendanceEntryDTO) {
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) throw new NoEmployeeProfileException();
        long id = employeeOptional.get().getId();

        // get today's attendance entry by employee id
        // if not available then check in else check out
        if (attendanceEntryService.findByDateAndEmployeeId(LocalDate.now(), id).isPresent() == true) {
            // out logic , UPDATE OUT TIME
            String outNote = attendanceEntryDTO.getOutNote();

            attendanceEntryDTO = attendanceEntryService.findByDateAndEmployeeId(LocalDate.now(), id).get();
            attendanceEntryDTO.setOutTime(Instant.now());
            attendanceEntryDTO.setOutNote(outNote);
            attendanceEntryService.update(attendanceEntryDTO);
            return "Successfully Checked Out";
        } else {
            //in logic
            attendanceEntryDTO.setDate(LocalDate.now()); // current date
            attendanceEntryDTO.setInTime(Instant.now()); // in time = request hitting time in api
            attendanceEntryDTO.setOutTime(null); // if in then out should stay null
            attendanceEntryDTO.setEmployeeId(id);
            attendanceEntryService.create(attendanceEntryDTO);
            return "Successfully Checked In";
        }
    }

    public boolean isEligible() {
        Optional<EmployeeDTO> employeeDTOOptional = currentEmployeeService.getCurrentEmployeeDTO();
        if (!employeeDTOOptional.isPresent()) {
            return false;
        } else if (
            employeeDTOOptional.get().isIsAllowedToGiveOnlineAttendance() == null ||
            employeeDTOOptional.get().isIsAllowedToGiveOnlineAttendance() == false
        ) {
            return false;
        } else return true;
    }

    public void dataTuneForEligibility() {
        List<Employee> employeeList = employeeRepository.getEmployeeWhereAttendanceEligibilityNull();
        for (Employee employee : employeeList) {
            employee.setIsAllowedToGiveOnlineAttendance(true);
            employee.setUpdatedAt(LocalDateTime.now());
            employeeRepository.save(employee);
        }
    }
}
