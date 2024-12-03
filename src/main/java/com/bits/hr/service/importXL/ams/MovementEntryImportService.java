package com.bits.hr.service.importXL.ams;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.MovementType;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.AttendanceEntryRepository;
import com.bits.hr.repository.LeaveApplicationRepository;
import com.bits.hr.repository.ManualAttendanceEntryRepository;
import com.bits.hr.repository.MovementEntryRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.PinUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class MovementEntryImportService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private MovementEntryRepository movementEntryRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private AttendanceEntryRepository attendanceEntryRepository;

    @Autowired
    private ManualAttendanceEntryRepository manualAttendanceEntryRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    public boolean importFile(MultipartFile file) {
        try {
            List<MovementEntry> movementEntryList = new ArrayList<>();

            List<ArrayList<String>> data = genericUploadService.upload(file);

            List<String> header = data.remove(0);

            User user = currentEmployeeService.getCurrentUser().get();

            //  0       1       2              3            4          5        6
            //  S/L     PIN     Start Date     End Date     Status     Note     Applied At

            for (List<String> dataItems : data) {
                if (dataItems.isEmpty()) {
                    continue;
                }
                if (dataItems.get(0).equals("0")) {
                    continue;
                }

                String pin = PinUtil.formatPin(dataItems.get(1).trim());
                Optional<Employee> employeeOptional = employeeService.findEmployeeByPin(pin);

                if (!employeeOptional.isPresent()) {
                    continue; // Employee Not Found
                }

                Employee employee = employeeOptional.get();
                LocalDate startDate = DateUtil.xlStringToDate(dataItems.get(2));
                LocalDate endDate = DateUtil.xlStringToDate(dataItems.get(3));

                LocalDate tempEndDate = endDate.plusDays(1);

                //check infinity loop possibilities
                if (startDate.isAfter(endDate)) {
                    continue;
                }

                for (LocalDate date = startDate; date.isBefore(tempEndDate); date = date.plusDays(1)) {
                    MovementEntry movementEntry = new MovementEntry();
                    movementEntry.setEmployee(employee);
                    movementEntry.setType(MovementType.MOVEMENT);
                    movementEntry.setStartDate(date);
                    movementEntry.setEndDate(date);

                    // attendance entry
                    Optional<AttendanceEntry> attendanceEntry = attendanceEntryRepository.findAttendanceEntryByDateAndEmployee(
                        date,
                        employee.getId()
                    );

                    // Manual Attendance Entry
                    List<ManualAttendanceEntry> manualAttendanceEntries = manualAttendanceEntryRepository.findAllManualAttendanceEntriesByPinAndDate(
                        employee.getPin(),
                        date
                    );

                    // Leave application
                    List<LeaveApplication> leaveApplications = leaveApplicationRepository.findEmployeeLeaveApplicationBetweenDatesExceptRejected(
                        employee.getPin(),
                        date,
                        date
                    );

                    if (attendanceEntry.isPresent()) {
                        continue;
                    }
                    if (manualAttendanceEntries.size() > 0) {
                        continue;
                    }
                    if (leaveApplications.size() > 0) {
                        continue;
                    }

                    Instant startTime = startDate.atTime(10, 0, 0).toInstant(ZoneOffset.of("+06:00"));

                    Instant endTime = endDate.atTime(18, 0, 0).toInstant(ZoneOffset.of("+06:00"));

                    movementEntry.setStartTime(startTime);
                    movementEntry.setEndTime(endTime);

                    movementEntry.setStartNote("start-note");
                    movementEntry.setEndNote("end-note");

                    Status status = getStatus(dataItems.get(4));
                    String note = dataItems.get(5).trim();
                    LocalDate createdAt = DateUtil.xlStringToDate(dataItems.get(6));

                    movementEntry.setStatus(status);
                    movementEntry.setNote(note);
                    movementEntry.setCreatedAt(createdAt);
                    movementEntry.setCreatedBy(user);

                    movementEntryList.add(movementEntry);
                }

                for (MovementEntry movementEntry : movementEntryList) {
                    save(movementEntry);
                }
            }
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
        return true;
    }

    boolean save(MovementEntry movementEntry) {
        try {
            List<MovementEntry> existingMovementEntryList = movementEntryRepository.getALLMovementEntriesByEmployeeIdBetweenDates(
                movementEntry.getEmployee().getId(),
                movementEntry.getStartDate(),
                movementEntry.getEndDate()
            );

            // Delete if movement entries exists between dates
            for (MovementEntry existingMovementEntry : existingMovementEntryList) {
                movementEntryRepository.delete(existingMovementEntry);
            }

            movementEntryRepository.save(movementEntry);
            return true;
        } catch (Exception ex) {
            log.error(ex);
            return false;
        }
    }

    Status getStatus(String status) {
        status = status.trim().toUpperCase();

        switch (status) {
            case "APPROVED":
                return Status.APPROVED;
            case "NOT_APPROVED":
            case "NOT APPROVED":
                return Status.NOT_APPROVED;
            default:
                return Status.PENDING;
        }
    }
}
