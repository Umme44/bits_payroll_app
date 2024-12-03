package com.bits.hr.service.attendanceTimeSheet;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.AttendanceDeviceOrigin;
import com.bits.hr.domain.enumeration.AttendanceStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.dto.TimeRangeDTO;
import com.bits.hr.service.search.FilterDto;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AttendanceTimeSheetExportXLService {

    @Autowired
    private AttendanceTimeSheetServiceV2 attendanceTimeSheetService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public ExportXLPropertiesDTO generateMonthlyAtsReport(
        String searchText,
        Long departmentId,
        Long designationId,
        Long unitId,
        LocalDate startDate,
        LocalDate endDate
    ) {
        try {
            FilterDto filterDto = new FilterDto();
            filterDto.setSearchText(searchText);
            filterDto.setDepartmentId(departmentId);
            filterDto.setDestinationId(designationId);
            filterDto.setUnitId(unitId);
            filterDto.setStartDate(startDate);
            filterDto.setEndDate(endDate);

            List<Employee> employeeList = employeeRepository.getEligibleEmployeeForMonthlyAttendanceUsingFilter(
                startDate,
                endDate,
                searchText,
                departmentId,
                designationId,
                unitId
            );

            List<MonthlyAttendanceTimeSheetDetailedReportDTO> detailedReportDTOS = new ArrayList<>();

            for (Employee employee : employeeList) {
                List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = attendanceTimeSheetService.getAttendanceTimeSheet(
                    employee.getId(),
                    startDate,
                    endDate,
                    AtsDataProcessLevel.MINIMAL
                );

                MonthlyAttendanceTimeSheetDetailedReportDTO detailedReportDTO = new MonthlyAttendanceTimeSheetDetailedReportDTO();
                detailedReportDTO.setPin(employee.getPin());
                detailedReportDTO.setName(employee.getFullName());
                detailedReportDTO.setDepartmentName(employee.getDepartment().getDepartmentName());
                detailedReportDTO.setDesignationName(employee.getDesignation().getDesignationName());
                detailedReportDTO.setUnitName(employee.getUnit().getUnitName());
                detailedReportDTO.setAttendanceTimeSheetDTOList(attendanceTimeSheetDTOList);

                detailedReportDTOS.add(detailedReportDTO);
            }

            // Sheet Name
            String sheetName = "Monthly Attendance Time Sheet";

            // Title List
            List<String> titleList = new ArrayList<>();

            // Sub Title List
            List<String> subTitleList = new ArrayList<>();
            subTitleList.add("Monthly Attendance Time Sheet");
            subTitleList.add("From: " + startDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
            subTitleList.add("To: " + endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));

            // Table Pre Header List
            List<Object> preHeaderList = new ArrayList<>();
            preHeaderList.add("");
            preHeaderList.add("");
            preHeaderList.add("");
            preHeaderList.add("");
            if (detailedReportDTOS.size() > 0) {
                for (AttendanceTimeSheetDTO dto : detailedReportDTOS.get(0).getAttendanceTimeSheetDTOList()) {
                    if (
                        dto.getAttendanceStatus() == AttendanceStatus.GOVT_HOLIDAY ||
                        dto.getAttendanceStatus() == AttendanceStatus.WEEKLY_OFFDAY
                    ) {
                        preHeaderList.add(dto.getDate());
                    } else {
                        preHeaderList.add(dto.getDate());
                        preHeaderList.add(dto.getDate());
                        preHeaderList.add(dto.getDate());
                        preHeaderList.add(dto.getDate());
                    }
                }
            }

            // Table Header List
            List<String> headerList = new ArrayList<>();
            headerList.add("PIN");
            headerList.add("Name");
            headerList.add("Department");
            headerList.add("Designation");

            if (detailedReportDTOS.size() > 0) {
                for (AttendanceTimeSheetDTO dto : detailedReportDTOS.get(0).getAttendanceTimeSheetDTOList()) {
                    if (
                        dto.getAttendanceStatus() == AttendanceStatus.GOVT_HOLIDAY ||
                        dto.getAttendanceStatus() == AttendanceStatus.WEEKLY_OFFDAY
                    ) {
                        headerList.add("Attendance Status");
                    } else {
                        headerList.add("Attendance Status");
                        headerList.add("In Time");
                        headerList.add("Out Time");
                        headerList.add("Total Working Hour");
                    }
                }
            }

            // Table Data List of List
            List<List<Object>> listOfData = new ArrayList<>();

            for (MonthlyAttendanceTimeSheetDetailedReportDTO reportDTO : detailedReportDTOS) {
                List<Object> data = new ArrayList<>();

                data.add(reportDTO.getPin());
                data.add(reportDTO.getName());
                data.add(reportDTO.getDepartmentName());
                data.add(reportDTO.getDesignationName());

                for (AttendanceTimeSheetDTO dto : reportDTO.getAttendanceTimeSheetDTOList()) {
                    if (
                        dto.getAttendanceStatus() == AttendanceStatus.GOVT_HOLIDAY ||
                        dto.getAttendanceStatus() == AttendanceStatus.WEEKLY_OFFDAY
                    ) {
                        data.add(dto.getAttendanceStatus().toString());
                    } else {
                        data.add(dto.getAttendanceStatus().toString());

                        String inTime = dto.getInTime() == null
                            ? "-"
                            : dto.getInTime().atZone(ZoneId.systemDefault()).toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
                        String outTime = dto.getOutTime() == null
                            ? "-"
                            : dto.getOutTime().atZone(ZoneId.systemDefault()).toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
                        String totalWorkingHour = dto.getTotalWorkingHour() == null ? "-" : dto.getTotalWorkingHour().toString();

                        data.add(inTime);
                        data.add(outTime);
                        data.add(totalWorkingHour);
                    }
                }

                listOfData.add(data);
            }

            ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();
            exportXLPropertiesDTO.setSheetName(sheetName);
            exportXLPropertiesDTO.setTitleList(titleList);
            exportXLPropertiesDTO.setSubTitleList(subTitleList);
            exportXLPropertiesDTO.setTablePreHeaderList(preHeaderList);
            exportXLPropertiesDTO.setTableHeaderList(headerList);
            exportXLPropertiesDTO.setTableDataListOfList(listOfData);
            exportXLPropertiesDTO.setHasAutoSummation(false);
            exportXLPropertiesDTO.setAutoSizeColumnUpTo(300);

            return exportXLPropertiesDTO;
        } catch (Exception e) {
            log.error("Error in generateMonthlyAtsReport: ", e);
            throw new BadRequestAlertException(
                "Error in generateMonthlyAtsReport",
                "AttendanceTimeSheetExportXLService",
                "generateMonthlyAtsReport"
            );
        }
    }

    public ExportXLPropertiesDTO getAtsDataXLReport(TimeRangeDTO timeRangeDTO, Employee employee) {
        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = attendanceTimeSheetService.getAttendanceTimeSheet(
            employee.getId(),
            timeRangeDTO.getStartDate(),
            timeRangeDTO.getEndDate(),
            AtsDataProcessLevel.MINIMAL
        );

        Collections.reverse(attendanceTimeSheetDTOList);

        return getAtsReportV2(employee, timeRangeDTO.getStartDate(), timeRangeDTO.getEndDate(), attendanceTimeSheetDTOList);
    }

    public ExportXLPropertiesDTO getAtsDataXLReport (TimeRangeDTO timeRangeDTO, List<Long> idList){
        Long employeeCount = employeeRepository.countByIdIn(idList);

        if (employeeCount != idList.size()) {
            throw new BadRequestAlertException("Employees Missing", "Employee", "noEmployee");
        }

        List<AttendanceTimeSheetEmployeeDTO> attendanceTimeSheetEmployeeDTOList = new ArrayList<>();
        idList.forEach(id -> {
            List<AttendanceTimeSheetDTO> employeeAttendances = attendanceTimeSheetService.getAttendanceTimeSheet(
                id,
                timeRangeDTO.getStartDate(),
                timeRangeDTO.getEndDate(),
                AtsDataProcessLevel.MINIMAL
            );
            Employee employee = employeeRepository.findById(id).get();
            AttendanceTimeSheetEmployeeDTO attendanceTimeSheetEmployeeDTO = new AttendanceTimeSheetEmployeeDTO();
            attendanceTimeSheetEmployeeDTO.setAttendanceTimeSheetDTOList(employeeAttendances);
            attendanceTimeSheetEmployeeDTO.setEmployee(employee);
            attendanceTimeSheetEmployeeDTOList.add(attendanceTimeSheetEmployeeDTO);
        });
        Collections.reverse(attendanceTimeSheetEmployeeDTOList);

        return getAtsReportForEmployeeList(
            attendanceTimeSheetEmployeeDTOList,
            timeRangeDTO.getStartDate(),
            timeRangeDTO.getEndDate()
        );
    }



    //    public ExportXLPropertiesDTO getAtsDataForAdmin(TimeRangeDTO timeRangeDTO, Employee employee) {
    //
    //        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList =
    //            attendanceTimeSheetService.getAttendanceTimeSheet(
    //                employee.getPin(),
    //                timeRangeDTO.getStartDate(),
    //                timeRangeDTO.getEndDate()
    //            );
    //
    //        Collections.reverse(attendanceTimeSheetDTOList);
    //
    //        return getAtsReport(
    //            employee,
    //            timeRangeDTO.getStartDate(),
    //            timeRangeDTO.getEndDate(),
    //            attendanceTimeSheetDTOList
    //        );
    //    }
    //
    //    public ExportXLPropertiesDTO getMyTeamAtsData(TimeRangeDTO timeRangeDTO, Employee teamMember) {
    //
    //        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList =
    //            attendanceTimeSheetService.getAttendanceTimeSheet(
    //                teamMember.getPin(),
    //                timeRangeDTO.getStartDate(),
    //                timeRangeDTO.getEndDate()
    //            );
    //
    //        Collections.reverse(attendanceTimeSheetDTOList);
    //
    //        return getAtsReport(
    //            teamMember,
    //            timeRangeDTO.getStartDate(),
    //            timeRangeDTO.getEndDate(),
    //            attendanceTimeSheetDTOList
    //        );
    //    }

    public ExportXLPropertiesDTO getAtsReport(
        Employee employee,
        LocalDate startDate,
        LocalDate endDate,
        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList
    ) {
        String sheetName = "Attendance Time Sheet";

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();
        subTitleList.add("PIN :" + employee.getPin());
        subTitleList.add("Name :" + employee.getFullName());
        subTitleList.add("Department :" + employee.getDepartment().getDepartmentName());
        subTitleList.add("Designation :" + employee.getDesignation().getDesignationName());
        subTitleList.add("Unit :" + employee.getUnit().getUnitName());
        subTitleList.add("ATS data Showing From " + startDate.toString() + " to " + endDate.toString());
        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("S/L");
        tableHeaderList.add("Date");
        tableHeaderList.add("Day");
        tableHeaderList.add("Status");
        tableHeaderList.add("In Time");
        tableHeaderList.add("Punch In Device");
        tableHeaderList.add("Out Time");
        tableHeaderList.add("Punch Out Device");
        tableHeaderList.add("Is Auto Punch Out");
        tableHeaderList.add("Duration");
        tableHeaderList.add("Late Duration");
        tableHeaderList.add("Details");

        List<List<Object>> dataList = new ArrayList<>();

        for (int i = 0; i < attendanceTimeSheetDTOList.size(); i++) {
            List<Object> dataRow = new ArrayList<>();
            dataRow.add(i + 1);
            dataRow.add(attendanceTimeSheetDTOList.get(i).getDate().toString());
            dataRow.add(getDay(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getAttendanceStatus(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getInTime(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getPunchInDevice(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getOutTime(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getPunchOutDevice(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getIsAutoPunchOut(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getDuration(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getLateDuration(attendanceTimeSheetDTOList.get(i)));
            dataRow.add("Details");

            dataList.add(dataRow);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();
        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(10);
        return exportXLPropertiesDTO;
    }

    public ExportXLPropertiesDTO getAtsReportV2(
        Employee employee,
        LocalDate startDate,
        LocalDate endDate,
        List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList
    ) {

        String sheetName = "Attendance Time Sheet";

        List<String> titleList = new ArrayList<>();

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("SL");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Name");
        tableHeaderList.add("Date");
        tableHeaderList.add("Attendance Status");
        tableHeaderList.add("In Time");
        tableHeaderList.add("Punch In Device Type (Office/Remote)");
        tableHeaderList.add("Out Time");
        tableHeaderList.add("Punch Out Device Type (Office/Remote)");
        tableHeaderList.add("Is Auto Punch Out (Yes/No)");
        tableHeaderList.add("Total Working Hour");
        tableHeaderList.add("Late Duration");

        List<List<Object>> dataList = new ArrayList<>();

        for (int i = 0; i < attendanceTimeSheetDTOList.size(); i++) {
            List<Object> dataRow = new ArrayList<>();
            dataRow.add(i + 1);
            dataRow.add(employee.getPin());
            dataRow.add(employee.getFullName());
            dataRow.add(attendanceTimeSheetDTOList.get(i).getDate().toString());
            dataRow.add(getAttendanceStatus(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getInTime(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getPunchInDevice(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getOutTime(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getPunchOutDevice(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getIsAutoPunchOut(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getDuration(attendanceTimeSheetDTOList.get(i)));
            dataRow.add(getLateDuration(attendanceTimeSheetDTOList.get(i)));

            dataList.add(dataRow);
        }
        ExportXLPropertiesDTO exportXLPropertiesDTO = populateExcelProperties(sheetName, titleList, tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);

        return exportXLPropertiesDTO;
    }

    public ExportXLPropertiesDTO getAtsReportForEmployeeList(
        List<AttendanceTimeSheetEmployeeDTO> attendanceTimeSheetEmployeeDTOList,
        LocalDate startDate,
        LocalDate endDate
    ) {

        String sheetName = "Attendance Time Sheet";

        List<String> titleList = new ArrayList<>();

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("SL");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Name");
        tableHeaderList.add("Date");
        tableHeaderList.add("Attendance Status");
        tableHeaderList.add("In Time");
        tableHeaderList.add("Punch In Device Type (Office/Remote)");
        tableHeaderList.add("Out Time");
        tableHeaderList.add("Punch Out Device Type (Office/Remote)");
        tableHeaderList.add("Is Auto Punch Out (Yes/No)");
        tableHeaderList.add("Total Working Hour");
        tableHeaderList.add("Late Duration");

        List<List<Object>> dataList = new ArrayList<>();
        int sl = 1;
        for(int x=0; x< attendanceTimeSheetEmployeeDTOList.size();x++) {
            List<AttendanceTimeSheetDTO> attendanceTimeSheetDTOList = attendanceTimeSheetEmployeeDTOList.get(x).getAttendanceTimeSheetDTOList();
            for (int i = 0; i < attendanceTimeSheetDTOList.size(); i++) {
                List<Object> dataRow = new ArrayList<>();
                dataRow.add(sl++);
                dataRow.add(attendanceTimeSheetEmployeeDTOList.get(x).getEmployee().getPin());
                dataRow.add(attendanceTimeSheetEmployeeDTOList.get(x).getEmployee().getFullName());
                dataRow.add(attendanceTimeSheetDTOList.get(i).getDate().toString());
                dataRow.add(getAttendanceStatus(attendanceTimeSheetDTOList.get(i)));
                dataRow.add(getInTime(attendanceTimeSheetDTOList.get(i)));
                dataRow.add(getPunchInDevice(attendanceTimeSheetDTOList.get(i)));
                dataRow.add(getOutTime(attendanceTimeSheetDTOList.get(i)));
                dataRow.add(getPunchOutDevice(attendanceTimeSheetDTOList.get(i)));
                dataRow.add(getIsAutoPunchOut(attendanceTimeSheetDTOList.get(i)));
                dataRow.add(getDuration(attendanceTimeSheetDTOList.get(i)));
                dataRow.add(getLateDuration(attendanceTimeSheetDTOList.get(i)));

                dataList.add(dataRow);
            }
        }
        ExportXLPropertiesDTO exportXLPropertiesDTO = populateExcelProperties(sheetName, titleList, tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        return exportXLPropertiesDTO;
    }

    private ExportXLPropertiesDTO populateExcelProperties(String sheetName, List<String> titleList, List<String> tableHeaderList){
        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();
        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(10);
        return exportXLPropertiesDTO;
    }

    String getAttendanceStatus(AttendanceTimeSheetDTO dto) {
        try {
            return AttendanceStatus.toString(dto.getAttendanceStatus());
        } catch (Exception e) {
            return "";
        }
    }

    String getLateDuration(AttendanceTimeSheetDTO dto) {
        try {
            if (dto.getAttendanceStatus() == AttendanceStatus.LATE) {
                int lateDurationHour = (int) Math.floor(dto.getLateDuration());
                int lateDurationMinute = (int) ((dto.getLateDuration() - Math.floor(dto.getLateDuration())) * 100);

                String lateDurationHourString = lateDurationHour > 12
                    ? String.valueOf(lateDurationHour % 12)
                    : String.valueOf(lateDurationHour);
                String lateDurationMinuteString = lateDurationMinute > 9 ? lateDurationMinute + "" : "0" + lateDurationMinute;

                String lateDuration =
                    lateDurationHourString + ":" + lateDurationMinuteString + ":00 " + (lateDurationHour >= 12 ? "PM" : "AM");
                return lateDuration;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    String getDuration(AttendanceTimeSheetDTO dto) {
        try {
            int workingHour = (int) Math.floor(dto.getTotalWorkingHour());
            int workingMinute = (int) Math.round(((dto.getTotalWorkingHour() - Math.floor(dto.getTotalWorkingHour())) * 100));

            String workingHourString = workingHour > 12 ? String.valueOf(workingHour % 12) : String.valueOf(workingHour);
            String workingMinuteString = workingMinute > 9 ? workingMinute + "" : "0" + workingMinute;

            String totalWorkingHour = workingHourString + ":" + workingMinuteString + ":00 " + (workingHour >= 12 ? "PM" : "AM");
            return totalWorkingHour;
        } catch (Exception e) {
            return "";
        }
    }

    String getDay(AttendanceTimeSheetDTO dto) {
        try {
            String day = dto.getDate().getDayOfWeek().toString();
            return day.substring(0, 1).toUpperCase() + day.substring(1).toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    String getInTime(AttendanceTimeSheetDTO dto) {
        try {
            Instant inTime = dto.getInTime();
            int second = inTime.atZone(ZoneId.systemDefault()).getSecond();
            Instant inTimeWithoutSecond = inTime.minusSeconds(second);

            String inTimeString = DateTimeFormatter
                .ofLocalizedTime(FormatStyle.MEDIUM)
                .withZone(ZoneId.systemDefault())
                .format(inTimeWithoutSecond);
            return inTimeString;
        } catch (Exception e) {
            return null;
        }
    }

    String getOutTime(AttendanceTimeSheetDTO dto) {
        try {
            Instant outTime = dto.getOutTime();
            int second = outTime.atZone(ZoneId.systemDefault()).getSecond();
            Instant outTimeWithoutSecond = outTime.minusSeconds(second);

            String inTimeString = DateTimeFormatter
                .ofLocalizedTime(FormatStyle.MEDIUM)
                .withZone(ZoneId.systemDefault())
                .format(outTimeWithoutSecond);
            return inTimeString;
        } catch (Exception e) {
            return null;
        }
    }

    String getPunchInDevice(AttendanceTimeSheetDTO dto) {
        try {
            if(dto.getPunchInDeviceOrigin().equals(AttendanceDeviceOrigin.DEVICE)){
                return "Office";
            }
            else if(dto.getPunchInDeviceOrigin().equals(AttendanceDeviceOrigin.WEB)){
                return "Remote";
            }
            else {
//                return capitalize(dto.getPunchInDeviceOrigin().toString());
                return "";
            }

        } catch (Exception e) {
            return "";
        }
    }

    String getPunchOutDevice(AttendanceTimeSheetDTO dto) {
        try {
            if(dto.getPunchOutDeviceOrigin().equals(AttendanceDeviceOrigin.DEVICE)){
                return "Office";
            }
            else if(dto.getPunchOutDeviceOrigin().equals(AttendanceDeviceOrigin.WEB)){
                return "Remote";
            }
            else {
//                return capitalize(dto.getPunchOutDeviceOrigin().toString());
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    String getIsAutoPunchOut(AttendanceTimeSheetDTO dto) {
        try {
//            return capitalize(dto.isIsAutoPunchOut().toString().toUpperCase());
            if(dto.isIsAutoPunchOut()){
                return "Yes";
            }
            else return "No";
        } catch (Exception e) {
            return "";
        }
    }

    String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
