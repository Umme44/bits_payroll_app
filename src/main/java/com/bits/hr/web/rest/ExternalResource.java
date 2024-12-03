package com.bits.hr.web.rest;

import static java.time.temporal.ChronoUnit.DAYS;

import com.bits.hr.domain.IntegratedAttendance;
import com.bits.hr.domain.Location;
import com.bits.hr.domain.enumeration.EmploymentStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.ExternalAddressBookService;
import com.bits.hr.service.HolidaysService;
import com.bits.hr.service.LeaveApplicationService;
import com.bits.hr.service.attendanceTimeSheet.AtsDataProcessLevel;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetDTO;
import com.bits.hr.service.attendanceTimeSheet.AttendanceTimeSheetServiceV2;
import com.bits.hr.service.dto.AddressBookExternalDTO;
import com.bits.hr.service.dto.HolidaysDTO;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.mapper.AddressBookExternalMapper;
import com.bits.hr.service.mapper.AddressBookMapper;
import com.bits.hr.web.rest.search.LeaveApplicationSearchResource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.bits.hr.domain.LeaveApplication}.
 */
@RestController
@RequestMapping("/api/external")
public class ExternalResource {

    private static final String RESOURCE_NAME = "External";

    private static final int MAX_PAGE_SIZE = 25;

    private final Logger log = LoggerFactory.getLogger(LeaveApplicationSearchResource.class);

    //    @Autowired
    //    private final AttendanceIntegrationScheduler attendanceIntegrationScheduler;

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @Autowired
    private AttendanceTimeSheetServiceV2 attendanceTimeSheetService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    AddressBookMapper addressBookMapper;

    @Autowired
    private AddressBookExternalMapper addressBookExternalMapper;

    @Autowired
    private HolidaysService holidaysService;

    @Autowired
    private ExternalAddressBookService externalAddressBookService;

    @GetMapping("/leave-applications")
    public Page<LeaveApplicationDTO> getLeaveApplicationsByEmployeeBetweenTwoDates(
        @RequestParam(required = false, defaultValue = "") String employeePin,
        @RequestParam(value = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(value = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        Pageable pageable
    ) {
        log.debug("External REST request to get LeaveApplications");

        if (startDate.isAfter(endDate)) {
            throw new BadRequestAlertException(
                "Oops! It looks like the start date is set later than the end date." +
                " Please double-check your dates to make sure they're in the correct order.",
                RESOURCE_NAME,
                ""
            );
        }

        pageSizeValidation(pageable);

        return leaveApplicationService.findLeaveApplicationsByEmployeeBetweenDatesLeaveType(
            employeePin,
            startDate,
            endDate,
            null,
            pageable
        );
    }

    @GetMapping("/attendance-time-sheet")
    public List<AttendanceTimeSheetDTO> getEmployeesAttendanceTimeSheetBetweenDates(
        @RequestParam String employeePin,
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate
    ) {
        log.debug(
            "External REST request to get all AttendanceTimeSheet between two dates " +
            "with parameter {employeePin:" +
            employeePin +
            ", startDate:" +
            startDate +
            " endDate:" +
            endDate +
            "}"
        );

        if (startDate.isAfter(endDate)) {
            throw new BadRequestAlertException(
                "Oops! It looks like the start date is set later than the end date." +
                " Please double-check your dates to make sure they're in the correct order.",
                RESOURCE_NAME,
                ""
            );
        }

        if (DAYS.between(startDate, endDate) > 30) {
            throw new BadRequestAlertException("Duration must not exceed more than 30 days.", RESOURCE_NAME, "");
        }

        Optional<Long> employeeId = employeeRepository.findIdByPin(employeePin);
        if (!employeeId.isPresent()) throw new NoEmployeeProfileException();

        return attendanceTimeSheetService.getAttendanceTimeSheet(
            employeeId.get(),
            startDate,
            endDate,
            AtsDataProcessLevel.FULL_FEATURED_ADMIN
        );
    }

    @GetMapping("/address-book")
    public Page<AddressBookExternalDTO> addressBook(
        Pageable pageable,
        @RequestParam(name = "search", required = false, defaultValue = "") String search,
        @RequestParam(name = "employmentStatus", required = false) EmploymentStatus status) {

        log.debug("External REST request for address book search " +
            "with parameter {pageable:"+pageable.toString()+"}");

        pageSizeValidation(pageable);

        try{
            if(status == EmploymentStatus.ACTIVE){
                return externalAddressBookService.allActiveEmployeeProfiles(search, pageable);
            }
            else return externalAddressBookService.getAllEmployeesTillDate(search, status, pageable);
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new BadRequestAlertException("Error occurred! Please try again later", RESOURCE_NAME, "");
        }
    }

    @PostMapping("/sync-attendance")
    public boolean syncAttendanceServer(@RequestBody List<IntegratedAttendance> data) {
        //        attendanceIntegrationScheduler.updateAttendanceSyncCache(data);
        return true;
    }

    private static void pageSizeValidation(Pageable pageable) {
        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new BadRequestAlertException("Max page should not more than " + MAX_PAGE_SIZE + ".", RESOURCE_NAME, "");
        }
    }

    @GetMapping("/holidays")
    public ResponseEntity<List<HolidaysDTO>> getAllHolidaysOfCurrentYear() {
        log.debug("REST request to get all Holidays");
        int currentYear = LocalDate.now().getYear();
        return ResponseEntity.ok().body(holidaysService.findAllOfAYear(currentYear));
    }
}
