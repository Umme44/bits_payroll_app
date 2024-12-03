package com.bits.hr.service.attendanceTimeSheet;

import com.bits.hr.domain.Department;
import com.bits.hr.domain.Designation;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Holidays;
import com.bits.hr.domain.LeaveApplication;
import com.bits.hr.domain.MovementEntry;
import com.bits.hr.domain.Unit;
import com.bits.hr.domain.enumeration.AttendanceStatus;
import com.bits.hr.repository.*;
import com.bits.hr.service.search.FilterDto;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.rmi.NotBoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.print.attribute.standard.Destination;
import org.springframework.stereotype.Service;

@Service
public class MonthlyAttendanceTimeSheetGenerationService {

    private final AttendanceEntryRepository attendanceEntryRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final HolidaysRepository holidaysRepository;
    private final AttendanceTimeSheetMiniService attendanceTimeSheetMiniService;
    private final EmployeeRepository employeeRepository;
    private final MovementEntryRepository movementEntryRepository;

    private final DesignationRepository designationRepository;

    private final DepartmentRepository departmentRepository;

    private final UnitRepository unitRepository;

    public MonthlyAttendanceTimeSheetGenerationService(
        AttendanceEntryRepository attendanceEntryRepository,
        LeaveApplicationRepository leaveApplicationRepository,
        HolidaysRepository holidaysRepository,
        AttendanceTimeSheetMiniService attendanceTimeSheetMiniService,
        EmployeeRepository employeeRepository,
        MovementEntryRepository movementEntryRepository,
        DesignationRepository designationRepository,
        DepartmentRepository departmentRepository,
        UnitRepository unitRepository
    ) {
        this.attendanceEntryRepository = attendanceEntryRepository;
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.holidaysRepository = holidaysRepository;
        this.attendanceTimeSheetMiniService = attendanceTimeSheetMiniService;
        this.employeeRepository = employeeRepository;
        this.movementEntryRepository = movementEntryRepository;
        this.designationRepository = designationRepository;
        this.departmentRepository = departmentRepository;
        this.unitRepository = unitRepository;
    }

    public List<MonthlyAttendanceTimeSheetDTO> getAllEmployeeATS(LocalDate startDate, LocalDate endDate) {
        //  StopWatch stopWatch = new StopWatch();
        //  stopWatch.start("getAllEmployeeATS");
        List<Employee> employeeList = employeeRepository.getEligibleEmployeeForSalaryGeneration_v2(startDate, endDate);
        List<MonthlyAttendanceTimeSheetDTO> monthlyAttendanceTimeSheetDTOList = new ArrayList<>();

        List<LocalDate> dateList = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            dateList.add(date);
        }

        // *** hashtable for holidays
        // data retrieved between date range too for memory optimization purposes
        List<Holidays> HolidaysList = holidaysRepository.findHolidaysStartDateBetweenDates(
            dateList.get(0),
            dateList.get(dateList.size() - 1)
        );
        HashSet<LocalDate> holidaysHashset = new HashSet<>();
        for (Holidays holidays : HolidaysList) {
            // single date , single key , no object duplication
            if (holidays.getStartDate() == holidays.getEndDate()) {
                holidaysHashset.add(holidays.getStartDate());
            }
            // if ranged dates , Key => range 0-n dates
            else {
                for (LocalDate date = holidays.getStartDate(); date.isBefore(holidays.getEndDate().plusDays(1)); date = date.plusDays(1)) {
                    holidaysHashset.add(date);
                }
            }
        }

        //  employeeList.parallelStream().forEach(x-> monthlyAttendanceTimeSheetDTOList.add(getPerEmployeeATS(x,dateList)));
        for (Employee employee : employeeList) {
            monthlyAttendanceTimeSheetDTOList.add(getPerEmployeeATS(employee, dateList, holidaysHashset));
        }

        // stopWatch.stop();
        // log.debug(stopWatch.prettyPrint());
        return monthlyAttendanceTimeSheetDTOList;
    }

    //Mehedi
    public List<MonthlyAttendanceTimeSheetDTO> getAllEmployeeAtsUsingFilter(FilterDto filterDto) throws NotBoundException {
        //  StopWatch stopWatch = new StopWatch();
        //  stopWatch.start("getAllEmployeeATS");

        //        String designation;
        //        String department;
        //        String unit;
        //
        //        if(filterDto.getDestinationId() == 0){
        //            designation = "";
        //        } else {
        //            Optional<Designation> designationObj = designationRepository.findById(filterDto.getDestinationId());
        //            if(designationObj.isPresent()){
        //                designation = designationObj.get().getDesignationName();
        //            } else {
        //                throw new NotBoundException("Designation nor found!");
        //            }
        //        }
        //        if(filterDto.getDepartmentId() == 0){
        //            department = "";
        //        } else {
        //            Optional<Department> departmentObj = departmentRepository.findById(filterDto.getDepartmentId());
        //            if(departmentObj.isPresent()){
        //                department = departmentObj.get().getDepartmentName();
        //            } else {
        //                throw new NotBoundException("Department nor found!");
        //            }
        //        }
        //
        //        if(filterDto.getUnitId() == 0){
        //            unit = "";
        //        } else {
        //            Optional<Unit> unitObj = unitRepository.findById(filterDto.getUnitId());
        //            if(unitObj.isPresent()){
        //                unit = unitObj.get().getUnitName();
        //            } else {
        //                throw new NotBoundException("Unit nor found!");
        //            }
        //        }

        List<Employee> employeeList = employeeRepository.getEligibleEmployeeForMonthlyAttendanceUsingFilter(
            filterDto.getStartDate(),
            filterDto.getEndDate(),
            filterDto.getSearchText(),
            filterDto.getDepartmentId(),
            filterDto.getDestinationId(),
            filterDto.getUnitId()
        );
        List<MonthlyAttendanceTimeSheetDTO> monthlyAttendanceTimeSheetDTOList = new ArrayList<>();

        List<LocalDate> dateList = new ArrayList<>();
        for (LocalDate date = filterDto.getStartDate(); date.isBefore(filterDto.getEndDate().plusDays(1)); date = date.plusDays(1)) {
            dateList.add(date);
        }

        // *** hashtable for holidays
        // data retrieved between date range too for memory optimization purposes
        List<Holidays> HolidaysList = holidaysRepository.findHolidaysStartDateBetweenDates(
            dateList.get(0),
            dateList.get(dateList.size() - 1)
        );
        HashSet<LocalDate> holidaysHashset = new HashSet<>();
        for (Holidays holidays : HolidaysList) {
            // single date , single key , no object duplication
            if (holidays.getStartDate() == holidays.getEndDate()) {
                holidaysHashset.add(holidays.getStartDate());
            }
            // if ranged dates , Key => range 0-n dates
            else {
                for (LocalDate date = holidays.getStartDate(); date.isBefore(holidays.getEndDate().plusDays(1)); date = date.plusDays(1)) {
                    holidaysHashset.add(date);
                }
            }
        }

        //  employeeList.parallelStream().forEach(x-> monthlyAttendanceTimeSheetDTOList.add(getPerEmployeeATS(x,dateList)));
        for (Employee employee : employeeList) {
            monthlyAttendanceTimeSheetDTOList.add(getPerEmployeeATS(employee, dateList, holidaysHashset));
        }

        // stopWatch.stop();
        // log.debug(stopWatch.prettyPrint());
        return monthlyAttendanceTimeSheetDTOList;
    }

    public List<MonthlyAttendanceTimeSheetDTO> getAllEmployeeATS(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());
        return getAllEmployeeATS(startDate, endDate);
    }

    public MonthlyAttendanceTimeSheetDTO getPerEmployeeATS(
        Employee employee,
        List<LocalDate> dateList,
        HashSet<LocalDate> holidaysHashset
    ) {
        MonthlyAttendanceTimeSheetDTO monthlyAttendanceTimeSheetDTO = new MonthlyAttendanceTimeSheetDTO();

        // *** hashtable for attendance
        // data retrieved as hashset between date range too for memory optimization purposes
        HashSet<LocalDate> attendanceEntryHashSet = attendanceEntryRepository.getAttendanceEntryByEmployeeIdAndBetweenTwoDates(
            employee.getId(),
            dateList.get(0),
            dateList.get(dateList.size() - 1)
        );

        // *** hashtable for movement Entry
        List<MovementEntry> movementEntryList = movementEntryRepository.getApprovedMovementEntriesByEmployeePinBetweenTwoDates(
            employee.getPin(),
            dateList.get(0),
            dateList.get(dateList.size() - 1)
        );
        HashSet<LocalDate> movementEntryHashSet = new HashSet<>();
        for (MovementEntry movementEntry : movementEntryList) {
            // single date , single key , no object duplication
            if (movementEntry.getStartDate().equals(movementEntry.getEndDate())) {
                movementEntryHashSet.add(movementEntry.getStartDate());
            }
            // if range , Key => range 0-n , same object
            else {
                for (
                    LocalDate date = movementEntry.getStartDate();
                    date.isBefore(movementEntry.getEndDate().plusDays(1));
                    date = date.plusDays(1)
                ) {
                    movementEntryHashSet.add(date);
                }
            }
        }

        // *** hashtable for LeaveApplication
        // data retrieved between date range too for memory optimization purposes
        // unapproved leave will not be taken
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.getApprovedLeaveApplicationsByEmployeeIdBetweenTwoDates(
            employee.getId(),
            dateList.get(0),
            dateList.get(dateList.size() - 1)
        );
        HashSet<LocalDate> leaveApplicationHashSet = new HashSet<>();
        for (LeaveApplication leaveApplication : leaveApplicationList) {
            // single date , single key , no object duplication
            if (leaveApplication.getStartDate().equals(leaveApplication.getEndDate())) {
                leaveApplicationHashSet.add(leaveApplication.getStartDate());
            }
            // if range , Key => range 0-n , same object
            else {
                for (
                    LocalDate date = leaveApplication.getStartDate();
                    date.isBefore(leaveApplication.getEndDate().plusDays(1));
                    date = date.plusDays(1)
                ) {
                    leaveApplicationHashSet.add(date);
                }
            }
        }

        List<AttendanceTimeSheetMini> attendanceTimeSheetMiniList = attendanceTimeSheetMiniService.getAttendanceTimeSheet(
            dateList,
            holidaysHashset,
            leaveApplicationHashSet,
            attendanceEntryHashSet,
            movementEntryHashSet
        );
        monthlyAttendanceTimeSheetDTO.setEmployeeId(employee.getId());
        monthlyAttendanceTimeSheetDTO.setPin(employee.getPin());
        monthlyAttendanceTimeSheetDTO.setName(employee.getFullName());
        monthlyAttendanceTimeSheetDTO.setAttendanceTimeSheetMiniList(attendanceTimeSheetMiniList);
        return monthlyAttendanceTimeSheetDTO;
    }

    public ExportXLPropertiesDTO getMonthlyAtsDataToExportExcel(List<MonthlyAttendanceTimeSheetDTO> dto) {
        String sheetName = "ATS-Summary";
        List<String> titleList = new ArrayList<>();
        titleList.add("");

        List<String> subTitleList = new ArrayList<>();
        subTitleList.add("Monthly Attendance Time Sheet");
        subTitleList.add(" ");
        subTitleList.add("Legends ->");
        subTitleList.add("P - Present");
        subTitleList.add("A - Absent");
        subTitleList.add("Lt - Late");
        subTitleList.add("L - Leave");
        subTitleList.add("M - Movement");
        subTitleList.add("O - Other");
        subTitleList.add("NFOH - Non Fulfilled Office Hour");
        subTitleList.add("PGH - Present Govt. Holiday");
        subTitleList.add("PWOD - Present Weekly Off Day");
        subTitleList.add("WOD - Weekly Off Day");
        subTitleList.add("GH - Govt. Holiday");
        subTitleList.add("SH - Special Holiday");
        subTitleList.add("MAL - Mentionable Annual Leave");
        subTitleList.add("MCL - Mentionable Casual Leave");
        subTitleList.add("NMCL - Non Mentionable Compensatory Leave");
        subTitleList.add("NMPL - Non Mentionable Paternity Leave");
        subTitleList.add("NMPanL - Non Mentionable Pandemic Leave");
        subTitleList.add("NMML - Non Mentionable Maternity Leave");

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("PIN");
        tableHeaderList.add("Name");
        tableHeaderList.add("Designation");
        tableHeaderList.add("DOJ");
        tableHeaderList.add("Department");

        tableHeaderList.add("Total");
        tableHeaderList.add("Present");
        tableHeaderList.add("Leave");
        tableHeaderList.add("Holiday");
        tableHeaderList.add("Absent");

        // taking first atsMiniList to set date in the header.
        List<AttendanceTimeSheetMini> attendanceTimeSheetMiniList = dto.get(0).getAttendanceTimeSheetMiniList();

        for (int i = 0; i < attendanceTimeSheetMiniList.size(); i++) {
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yy");

            String strDate =
                attendanceTimeSheetMiniList.get(i).getDate().getDayOfMonth() +
                "-" +
                attendanceTimeSheetMiniList.get(i).getDate().getMonth().toString().toLowerCase();
            tableHeaderList.add(strDate);
        }

        List<List<Object>> atsData = new ArrayList<>();

        for (int i = 0; i < dto.size(); i++) {
            List<Object> atsRow = new ArrayList<>();

            Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(dto.get(i).getPin());
            if (!employeeOptional.isPresent()) {
                continue;
            }

            atsRow.add(employeeOptional.get().getPin());
            atsRow.add(employeeOptional.get().getFullName());
            atsRow.add(employeeOptional.get().getDesignation().getDesignationName());
            atsRow.add(employeeOptional.get().getDateOfJoining().toString());
            atsRow.add(employeeOptional.get().getDepartment().getDepartmentName());

            List<AttendanceTimeSheetMini> atsMiniList = dto.get(i).getAttendanceTimeSheetMiniList();

            Long totalDays = (long) atsMiniList.size();
            Long totalPresent = 0L;
            Long totalLeave = 0L;
            Long totalHoliday = 0L;
            Long totalAbsent = 0L;

            for (int c = 0; c < atsMiniList.size(); c++) {
                if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.PRESENT.name()) {
                    totalPresent++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.ABSENT.name()) {
                    totalAbsent++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.LATE.name()) {
                    totalPresent++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.LEAVE.name()) {
                    totalLeave++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.MOVEMENT.name()) {
                    totalPresent++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.OTHER.name()) {
                    totalPresent++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.NON_FULFILLED_OFFICE_HOURS.name()) {
                    totalPresent++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.PRESENT_GOVT_HOLIDAY.name()) {
                    totalPresent++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.PRESENT_WEEKLY_OFFDAY.name()) {
                    totalPresent++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.WEEKLY_OFFDAY.name()) {
                    totalHoliday++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.GOVT_HOLIDAY.name()) {
                    totalHoliday++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.SPECIAL_HOLIDAY.name()) {
                    totalHoliday++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.MENTIONABLE_ANNUAL_LEAVE.name()) {
                    totalLeave++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.MENTIONABLE_CASUAL_LEAVE.name()) {
                    totalLeave++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.NON_MENTIONABLE_COMPENSATORY_LEAVE.name()) {
                    totalLeave++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.NON_MENTIONABLE_PATERNITY_LEAVE.name()) {
                    totalLeave++;
                } else if (atsMiniList.get(c).getAttendanceStatus().name() == AttendanceStatus.NON_MENTIONABLE_PANDEMIC_LEAVE.name()) {
                    totalLeave++;
                } else {
                    totalLeave++;
                }
            }
            atsRow.add(totalDays.intValue());
            atsRow.add(totalPresent.intValue());
            atsRow.add(totalLeave.intValue());
            atsRow.add(totalHoliday.intValue());
            atsRow.add(totalAbsent.intValue());

            for (int j = 0; j < atsMiniList.size(); j++) {
                if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.PRESENT.name()) {
                    atsRow.add("P");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.ABSENT.name()) {
                    atsRow.add("A");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.LATE.name()) {
                    atsRow.add("Lt");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.LEAVE.name()) {
                    atsRow.add("L");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.MOVEMENT.name()) {
                    atsRow.add("M");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.OTHER.name()) {
                    atsRow.add("O");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.NON_FULFILLED_OFFICE_HOURS.name()) {
                    atsRow.add("NFO");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.PRESENT_GOVT_HOLIDAY.name()) {
                    atsRow.add("PGH");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.PRESENT_WEEKLY_OFFDAY.name()) {
                    atsRow.add("PWOD");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.WEEKLY_OFFDAY.name()) {
                    atsRow.add("WOD");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.GOVT_HOLIDAY.name()) {
                    atsRow.add("GH");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.SPECIAL_HOLIDAY.name()) {
                    atsRow.add("SH");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.MENTIONABLE_ANNUAL_LEAVE.name()) {
                    atsRow.add("MAL");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.MENTIONABLE_CASUAL_LEAVE.name()) {
                    atsRow.add("MCL");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.NON_MENTIONABLE_COMPENSATORY_LEAVE.name()) {
                    atsRow.add("NMCL");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.NON_MENTIONABLE_PATERNITY_LEAVE.name()) {
                    atsRow.add("NMPL");
                } else if (atsMiniList.get(j).getAttendanceStatus().name() == AttendanceStatus.NON_MENTIONABLE_PANDEMIC_LEAVE.name()) {
                    atsRow.add("NMpanL");
                } else {
                    atsRow.add("NMML");
                }
            }

            atsData.add(atsRow);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setSubTitleList(subTitleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(atsData);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(38);
        exportXLPropertiesDTO.setHasAutoSummation(false);

        return exportXLPropertiesDTO;
    }
}
