package com.bits.hr.service.LeaveManagement.userLeaveApplication;

import com.bits.hr.domain.Config;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.LeaveType;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.repository.ConfigRepository;
import com.bits.hr.service.EmployeeResignationService;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.LeaveApplicationService;
import com.bits.hr.service.LeaveManagement.LeaveDaysCount.LeaveDaysCalculationService;
import com.bits.hr.service.attendanceTimeSheet.AttendanceSharedService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.config.DefinedKeys;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.util.DateUtil;
import java.time.LocalDate;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserLeaveApplicationService {

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private EmployeeResignationService employeeResignationService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private AttendanceSharedService attendanceSharedService;

    @Autowired
    private LeaveDaysCalculationService leaveDaysCalculationService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    private static final String ENTITY_NAME = "leaveApplication";

    public LeaveApplicationDTO create(LeaveApplicationDTO leaveApplicationDTO) {
        // check is leave application enabled
        Optional<Config> config = configRepository.findConfigByKey(DefinedKeys.is_leave_application_enabled_for_user_end);
        if (!config.isPresent()) {
            throw new RuntimeException("missing key = is_leave_application_enabled_for_user_end");
        }
        boolean enabled = Boolean.parseBoolean(config.get().getValue());
        if (!enabled) {
            throw new BadRequestAlertException(
                "Leave application is temporary disabled. Please try again later.",
                "LeaveApplicationDTO",
                "leaveApplicationIsDisabled"
            );
        }

        boolean hasConflict = leaveDaysCalculationService.hasAnyLAConflict(
            currentEmployeeService.getCurrentEmployeePin().get(),
            leaveApplicationDTO.getId(),
            leaveApplicationDTO.getStartDate(),
            leaveApplicationDTO.getEndDate()
        );
        if (hasConflict) {
            throw new BadRequestAlertException(
                "Application is conflicting with previous leave application!",
                "LeaveApplicationDTO",
                "leaveApplicationConflict"
            );
        }

        // refresh leave duration days
        int leaveDays = leaveDaysCalculationService.leaveDaysCalculation(
            leaveApplicationDTO.getStartDate(),
            leaveApplicationDTO.getEndDate()
        );
        leaveApplicationDTO.setDurationInDay(leaveDays);

        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) throw new NoEmployeeProfileException();

        //2-days per month casual leave validation
        if (leaveApplicationDTO.getLeaveType().equals(LeaveType.MENTIONABLE_CASUAL_LEAVE)) {
            Optional<EmployeeDTO> employeeDto = employeeService.findOne(employeeOptional.get().getId());

            if (!employeeDto.isPresent()) {
                throw new BadRequestAlertException("Employee not found", "LeaveApplicationDTO", "idNotExist");
            }

            Employee employee = employeeMapper.toEntity(employeeDto.get());

            if (leaveApplicationDTO.getId() == null) {
                leaveApplicationDTO.setId(-1L);
            }

            //leaveDaysCalculationService.canApplyForCasualLeave(employee, leaveApplicationDTO.getId(), leaveApplicationDTO.getStartDate(), leaveApplicationDTO.getEndDate());

            boolean canApplyCasualLeave = leaveDaysCalculationService.canApplyForCasualLeave(
                employee,
                leaveApplicationDTO.getId(),
                leaveApplicationDTO.getStartDate(),
                leaveApplicationDTO.getEndDate()
            );

            if (!canApplyCasualLeave) {
                throw new BadRequestAlertException(
                    "Oops! Can't take more then two casual leaves per month.",
                    "LeaveApplicationDTO",
                    "idNotExist"
                );
            }
        }

        // year validation
        if (leaveApplicationDTO.getStartDate() != null && leaveApplicationDTO.getEndDate() != null) {
            if (leaveApplicationDTO.getStartDate().getYear() != leaveApplicationDTO.getEndDate().getYear()) {
                throw new BadRequestAlertException("Start Year and End Year must be same", "LeaveApplicationDTO", "yearMustBeSame");
            }
        }

        // date validation
        if (leaveApplicationDTO.getStartDate().isBefore(attendanceSharedService.getMaxAllowedPreviousDate())) {
            String msg = attendanceSharedService.getMaxAllowedPreviousDateCrossingValidationMessage();
            throw new BadRequestAlertException(msg, ENTITY_NAME, "maxAllowedDateCrossed");
        }

        // validation
        LocalDate startDate = leaveApplicationDTO.getStartDate();
        LocalDate endDate = leaveApplicationDTO.getEndDate();
        if (employeeOptional.get().getDateOfJoining() == null) {
            throw new BadRequestAlertException(
                "Your Date of Joining is missing, contact with HR for update.",
                "LeaveApplicationDTO",
                "dojMissing"
            );
        }
        if (startDate.isBefore(employeeOptional.get().getDateOfJoining())) {
            throw new BadRequestAlertException(
                "You can not apply leave before your date of joining!",
                "LeaveApplicationDTO",
                "leaveApplicationBeforeDOJ"
            );
        }

        Optional<LocalDate> lwd = employeeResignationService.getLastWorkingDay(employeeOptional.get().getId());
        if (lwd.isPresent() && DateUtil.isBetween(lwd.get(), startDate, endDate)) {
            throw new BadRequestAlertException(
                "After last working day, leave application is not valid",
                "LeaveApplicationDTO",
                "leaveAfterLastWorkingDay"
            );
        }

        // Application date = current date
        leaveApplicationDTO.setApplicationDate(LocalDate.now());
        leaveApplicationDTO.setIsHRApproved(false);
        leaveApplicationDTO.setIsLineManagerApproved(false);
        leaveApplicationDTO.setIsRejected(false);
        leaveApplicationDTO.setRejectionComment("");
        leaveApplicationDTO.setEmployeeId(employeeOptional.get().getId());

        if (leaveApplicationService.getApplicableLeaveBalance(leaveApplicationDTO) < leaveApplicationDTO.getDurationInDay()) {
            throw new BadRequestAlertException("Insufficient Leave balance", ENTITY_NAME, "insufficientBalance");
        }
        return leaveApplicationService.save(leaveApplicationDTO);
    }

    public LeaveApplicationDTO update(LeaveApplicationDTO leaveApplicationDTO) {
        // check is leave application enabled
        Optional<Config> config = configRepository.findConfigByKey(DefinedKeys.is_leave_application_enabled_for_user_end);
        if (!config.isPresent()) {
            throw new RuntimeException("missing key = is_leave_application_enabled_for_user_end");
        }
        boolean enabled = Boolean.parseBoolean(config.get().getValue());
        if (!enabled) {
            throw new BadRequestAlertException(
                "Leave application is temporary disabled. Please try again later.",
                "LeaveApplicationDTO",
                "leaveApplicationIsDisabled"
            );
        }

        // checking conflict with other leave application
        boolean isConflict = leaveDaysCalculationService.hasAnyLAConflict(
            currentEmployeeService.getCurrentEmployeePin().get(),
            leaveApplicationDTO.getId(),
            leaveApplicationDTO.getStartDate(),
            leaveApplicationDTO.getEndDate()
        );
        if (isConflict) {
            throw new BadRequestAlertException(
                "Application is conflicting with previous leave application!",
                "LeaveApplicationDTO",
                "leaveApplicationConflict"
            );
        }

        // refresh leave duration days
        int leaveDays = leaveDaysCalculationService.leaveDaysCalculation(
            leaveApplicationDTO.getStartDate(),
            leaveApplicationDTO.getEndDate()
        );
        leaveApplicationDTO.setDurationInDay(leaveDays);

        // year validation
        if (leaveApplicationDTO.getStartDate() != null && leaveApplicationDTO.getEndDate() != null) {
            if (leaveApplicationDTO.getStartDate().getYear() != leaveApplicationDTO.getEndDate().getYear()) {
                throw new BadRequestAlertException("Start Year and End Year must be same", "LeaveApplicationDTO", "yearMustBeSame");
            }
        }

        // date validation
        if (leaveApplicationDTO.getStartDate().isBefore(attendanceSharedService.getMaxAllowedPreviousDate())) {
            String msg = attendanceSharedService.getMaxAllowedPreviousDateCrossingValidationMessage();
            throw new BadRequestAlertException(msg, ENTITY_NAME, "maxAllowedDateCrossed");
        }

        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) throw new NoEmployeeProfileException();

        // doj and lwd validation
        LocalDate startDate = leaveApplicationDTO.getStartDate();
        LocalDate endDate = leaveApplicationDTO.getEndDate();
        if (employeeOptional.get().getDateOfJoining() == null) {
            throw new BadRequestAlertException(
                "Your Date of Joining is missing, contact with HR for update.",
                "LeaveApplicationDTO",
                "dojMissing"
            );
        }
        if (startDate.isBefore(employeeOptional.get().getDateOfJoining())) {
            throw new BadRequestAlertException(
                "You can not apply leave before your date of joining!",
                "LeaveApplicationDTO",
                "leaveApplicationBeforeDOJ"
            );
        }

        Optional<LocalDate> lwd = employeeResignationService.getLastWorkingDay(employeeOptional.get().getId());
        if (lwd.isPresent() && DateUtil.isBetween(lwd.get(), startDate, endDate)) {
            throw new BadRequestAlertException(
                "After last working day, leave application is not valid",
                "LeaveApplicationDTO",
                "leaveAfterLastWorkingDay"
            );
        }

        // previous value
        LeaveApplicationDTO previousLeaveApplication = leaveApplicationService.findOne(leaveApplicationDTO.getId()).get();

        if (previousLeaveApplication.getEmployeeId() == null) {
            throw new BadRequestAlertException("EmployeeId is not found", ENTITY_NAME, "accessForbidden");
        }

        // pre-authorize (employee id)

        if (!previousLeaveApplication.getEmployeeId().equals(employeeOptional.get().getId())) {
            throw new BadRequestAlertException("Access Forbidden", "UserLeaveApplication", "accessForbidden");
        }

        // check status
        if (
            previousLeaveApplication.isIsHRApproved() ||
            previousLeaveApplication.isIsLineManagerApproved() ||
            previousLeaveApplication.isIsRejected()
        ) {
            log.error("Only pending requests are allowed to edit id = {}", previousLeaveApplication.getId());
            throw new BadRequestAlertException("Only pending requests are allowed to edit.", "UserLeaveApplication", "statusIsNotPending");
        }

        int previousAppliedDays = previousLeaveApplication.getDurationInDay();

        leaveApplicationDTO.setApplicationDate(LocalDate.now());
        leaveApplicationDTO.setIsHRApproved(false);
        leaveApplicationDTO.setIsLineManagerApproved(false);
        leaveApplicationDTO.setIsRejected(false);
        leaveApplicationDTO.setRejectionComment("");
        leaveApplicationDTO.setEmployeeId(employeeOptional.get().getId());

        if (
            (leaveApplicationService.getApplicableLeaveBalance(leaveApplicationDTO) + previousAppliedDays) <
            leaveApplicationDTO.getDurationInDay()
        ) {
            throw new BadRequestAlertException("Insufficient Leave balance", ENTITY_NAME, "insufficientBalance");
        }

        return leaveApplicationService.save(leaveApplicationDTO);
    }
}
