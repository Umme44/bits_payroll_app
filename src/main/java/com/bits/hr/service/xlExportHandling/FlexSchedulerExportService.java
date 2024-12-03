package com.bits.hr.service.xlExportHandling;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.FlexScheduleApplicationRepository;
import com.bits.hr.repository.FlexScheduleRepository;
import com.bits.hr.repository.TimeSlotRepository;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import com.bits.hr.util.DateUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FlexSchedulerExportService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private FlexScheduleRepository flexScheduleRepository;

    @Autowired
    private FlexScheduleApplicationRepository flexScheduleApplicationRepository;

    public ExportXLPropertiesDTO exportFlexSchedule() {
        List<CustomTimeSlotDTO> flexScheduleAndTimeSlotList = timeSlotRepository.getFlexScheduleAndTimeSlot();

        String sheetName = "Flex Schedule";

        List<String> titleList = new ArrayList<>();

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("SL");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Full Name");
        tableHeaderList.add("Effective Start Date");
        tableHeaderList.add("Effective End Date");
        tableHeaderList.add("In Time");
        tableHeaderList.add("Out Time");
        tableHeaderList.add("TimeSlot Title");
        tableHeaderList.add("Weekends");

        List<List<Object>> dataList = new ArrayList<>();

        for (int i = 0; i < flexScheduleAndTimeSlotList.size(); i++) {
            List<Object> data = new ArrayList<>();
            data.add(i + 1);

            Optional<Employee> employee = employeeRepository.findById(flexScheduleAndTimeSlotList.get(i).getEmployeeId());

            data.add(employee.get().getPin());
            data.add(employee.get().getFullName());
            data.add(flexScheduleAndTimeSlotList.get(i).getEffectiveDate());
            data.add("-");
            data.add(flexScheduleAndTimeSlotList.get(i).getInTime());
            data.add(flexScheduleAndTimeSlotList.get(i).getOutTime());
            data.add(flexScheduleAndTimeSlotList.get(i).getTimeSlotTitle());
            data.add("-");

            dataList.add(data);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(12);

        return exportXLPropertiesDTO;
    }

    public ExportXLPropertiesDTO exportMissingFlexSchedule() {
        List<FlexSchedule> flexScheduleList = flexScheduleRepository.findAll();

        List<CustomTimeSlotDTO> flexScheduleAndTimeSlotList = timeSlotRepository.getFlexScheduleAndTimeSlot();

        List<FlexSchedule> missingFlexScheduleList = new ArrayList<>();

        for (FlexSchedule flexSchedule : flexScheduleList) {
            Long id = flexSchedule.getId();
            Optional<CustomTimeSlotDTO> optionalFlexScheduleAndTimeSlotDTO = flexScheduleAndTimeSlotList
                .stream()
                .filter(x -> x.getFlexScheduleId().equals(id))
                .findAny();
            if (!optionalFlexScheduleAndTimeSlotDTO.isPresent()) {
                missingFlexScheduleList.add(flexSchedule);
            }
        }

        String sheetName = "Missing Flex Schedule";

        List<String> titleList = new ArrayList<>();

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("SL");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Full Name");
        tableHeaderList.add("Effective Start Date");
        tableHeaderList.add("Effective End Date");
        tableHeaderList.add("In Time");
        tableHeaderList.add("Out Time");
        tableHeaderList.add("TimeSlot Title");
        tableHeaderList.add("WeekEnds");

        List<List<Object>> dataList = new ArrayList<>();

        for (int i = 0; i < missingFlexScheduleList.size(); i++) {
            List<Object> data = new ArrayList<>();
            data.add(i + 1);

            Employee employee = missingFlexScheduleList.get(i).getEmployee();

            data.add(employee.getPin());
            data.add(employee.getFullName());
            data.add(missingFlexScheduleList.get(i).getEffectiveDate());
            data.add("-");
            data.add(missingFlexScheduleList.get(i).getInTime());
            data.add(missingFlexScheduleList.get(i).getOutTime());
            data.add("-");
            data.add("-");

            dataList.add(data);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(12);

        return exportXLPropertiesDTO;
    }

    public ExportXLPropertiesDTO exportFlexScheduleApplications() {
        List<FlexScheduleApplication> flexScheduleAndTimeSlotList = flexScheduleApplicationRepository.findAll();

        String sheetName = "Flex Schedule Application";

        List<String> titleList = new ArrayList<>();

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("SL");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Full Name");
        tableHeaderList.add("Effective Start Date");
        tableHeaderList.add("Effective End Date");
        tableHeaderList.add("In Time");
        tableHeaderList.add("Out Time");
        tableHeaderList.add("TimeSlot Title");
        tableHeaderList.add("Applied At");
        tableHeaderList.add("Status");

        List<List<Object>> dataList = new ArrayList<>();

        for (int i = 0; i < flexScheduleAndTimeSlotList.size(); i++) {
            List<Object> data = new ArrayList<>();
            data.add(i + 1);

            Optional<Employee> employee = employeeRepository.findById(flexScheduleAndTimeSlotList.get(i).getRequester().getId());

            data.add(employee.get().getPin());
            data.add(employee.get().getFullName());
            data.add(flexScheduleAndTimeSlotList.get(i).getEffectiveFrom());
            data.add(flexScheduleAndTimeSlotList.get(i).getEffectiveTo());
            data.add(flexScheduleAndTimeSlotList.get(i).getTimeSlot().getInTime());
            data.add(flexScheduleAndTimeSlotList.get(i).getTimeSlot().getOutTime());
            data.add(flexScheduleAndTimeSlotList.get(i).getTimeSlot().getTitle());
            if (flexScheduleAndTimeSlotList.get(i).getAppliedAt() != null) {
                data.add(flexScheduleAndTimeSlotList.get(i).getAppliedAt());
            } else {
                data.add("-");
            }
            data.add(flexScheduleAndTimeSlotList.get(i).getStatus().toString());

            dataList.add(data);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(12);

        return exportXLPropertiesDTO;
    }

    public ExportXLPropertiesDTO exportFlexScheduleApplicationsReport(
        Long employeeId,
        List<Long> timeSlotIdList,
        LocalDate startDate,
        LocalDate endDate,
        Status status
    ) {
        if (timeSlotIdList.isEmpty()) {
            timeSlotIdList = timeSlotRepository.findAll().stream().map(TimeSlot::getId).collect(Collectors.toList());
        }
        List<FlexScheduleApplication> flexScheduleAndTimeSlotList = flexScheduleApplicationRepository.findFlexApplicationBetweenDatesReport(
            employeeId,
            timeSlotIdList,
            startDate,
            endDate,
            status
        );

        String sheetName = "Flex Schedule Application";

        List<String> titleList = new ArrayList<>();

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("SL");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Full Name");
        tableHeaderList.add("Effective Start Date");
        tableHeaderList.add("Effective End Date");
        tableHeaderList.add("In Time");
        tableHeaderList.add("Out Time");
        tableHeaderList.add("TimeSlot Title");
        tableHeaderList.add("Applied At");
        tableHeaderList.add("Status");

        List<List<Object>> dataList = new ArrayList<>();

        for (int i = 0; i < flexScheduleAndTimeSlotList.size(); i++) {
            List<Object> data = new ArrayList<>();
            data.add(i + 1);

            Optional<Employee> employee = employeeRepository.findById(flexScheduleAndTimeSlotList.get(i).getRequester().getId());

            data.add(employee.get().getPin());
            data.add(employee.get().getFullName());
            data.add(flexScheduleAndTimeSlotList.get(i).getEffectiveFrom());
            data.add(flexScheduleAndTimeSlotList.get(i).getEffectiveTo());
            data.add(flexScheduleAndTimeSlotList.get(i).getTimeSlot().getInTime());
            data.add(flexScheduleAndTimeSlotList.get(i).getTimeSlot().getOutTime());
            data.add(flexScheduleAndTimeSlotList.get(i).getTimeSlot().getTitle());
            if (flexScheduleAndTimeSlotList.get(i).getAppliedAt() != null) {
                data.add(flexScheduleAndTimeSlotList.get(i).getAppliedAt());
            } else {
                data.add("-");
            }
            data.add(flexScheduleAndTimeSlotList.get(i).getStatus().toString());

            dataList.add(data);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(12);

        return exportXLPropertiesDTO;
    }
}
