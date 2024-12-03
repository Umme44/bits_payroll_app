package com.bits.hr.service.config;

import static com.bits.hr.service.config.GetPRFApprovalConfigService.*;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.Relation;
import com.bits.hr.repository.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationStartConfigurationService {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private InsuranceConfigurationRepository insuranceConfigurationRepository;

    private static final String GENERAL_SHIFT_TITLE = "General Shift";

    private final Logger log = LoggerFactory.getLogger(ApplicationStartConfigurationService.class);

    public void registerDomainName() {
        if (!configRepository.findConfigByKey(DefinedKeys.organisational_email_domain_name).isPresent()) {
            Config config = new Config();
            config.setKey(DefinedKeys.organisational_email_domain_name);
            config.setValue("bracits.com");
            configRepository.save(config);
        }
    }

    public void createRRFApprovalProcess() {
        if (!configRepository.findConfigByKey(DefinedKeys.rrf_approval_flow).isPresent()) {
            String recommendedByLM = "LM";
            String recommendedByHoD = "HoD";
            String recommendedByCTO = "1703";
            String VettedByHoHR = "1603";
            String ceoPin = "1600";
            String approvalProcess = recommendedByLM + "," + recommendedByHoD + "," + recommendedByCTO + "," + VettedByHoHR + ',' + ceoPin;

            Config config = new Config();
            config.setKey(DefinedKeys.rrf_approval_flow);
            config.setValue(approvalProcess);
            configRepository.save(config);
        }
    }

    public void createPRFApprovalProcess() {
        if (!configRepository.findConfigByKey(DefinedKeys.PRF_APPROVAL_FLOW).isPresent()) {
            //Technical-1703,Operation-1603,Finance-1376,CEO-1600
            String approvalProcess = String.format(
                "%s-1703,%s-1603,%s-1376,%s-1600",
                PRF_APPROVAL_02_NAME,
                PRF_APPROVAL_03_NAME,
                PRF_APPROVAL_04_NAME,
                PRF_APPROVAL_05_NAME
            );

            Config config = new Config();
            config.setKey(DefinedKeys.PRF_APPROVAL_FLOW);
            config.setValue(approvalProcess);
            configRepository.save(config);
        }
    }

    public void createMaxAllowedDaysForChangeAttendanceStatus() {
        if (!configRepository.findConfigByKey(DefinedKeys.max_allowed_previous_days_for_change_attendance_status).isPresent()) {
            String maxDays = "366";

            Config config = new Config();
            config.setKey(DefinedKeys.max_allowed_previous_days_for_change_attendance_status);
            config.setValue(maxDays);
            configRepository.save(config);
        }
    }

    public void createPRFConfiguration() {
        if (!configRepository.findConfigByKey(DefinedKeys.PRF_MAX_TOTAL_APPROXIMATE_BDT_AMOUNT).isPresent()) {
            String maxAmount = "1500000";
            Config config = new Config();
            config.setKey(DefinedKeys.PRF_MAX_TOTAL_APPROXIMATE_BDT_AMOUNT);
            config.setValue(maxAmount);
            configRepository.save(config);
        }
        if (!configRepository.findConfigByKey(DefinedKeys.PRF_TEAM_CONTACT_NO).isPresent()) {
            String contactNo = "01787680629";
            Config config = new Config();
            config.setKey(DefinedKeys.PRF_TEAM_CONTACT_NO);
            config.setValue(contactNo);
            configRepository.save(config);
        }
        if (!configRepository.findConfigByKey(DefinedKeys.PRF_OFFICER_EMPLOYEE_PIN).isPresent()) {
            String employeePIN = "1565";
            Config config = new Config();
            config.setKey(DefinedKeys.PRF_OFFICER_EMPLOYEE_PIN);
            config.setValue(employeePIN);
            configRepository.save(config);
        }
    }

    public void createMaxDurationInDaysForAttendanceDataLoad() {
        if (!configRepository.findConfigByKey(DefinedKeys.max_duration_in_days_for_attendance_data_load).isPresent()) {
            String maxDays = "366";

            Config config = new Config();
            config.setKey(DefinedKeys.max_duration_in_days_for_attendance_data_load);
            config.setValue(maxDays);
            configRepository.save(config);
        }
    }

    public void createIsLeaveApplicationEnabledForUserEnd() {
        if (!configRepository.findConfigByKey(DefinedKeys.is_leave_application_enabled_for_user_end).isPresent()) {
            Config config = new Config();
            config.setKey(DefinedKeys.is_leave_application_enabled_for_user_end);
            config.setValue("TRUE");
            configRepository.save(config);
        }
    }

    public void createIsRRFEnabledForUserEnd() {
        if (!configRepository.findConfigByKey(DefinedKeys.is_rrf_enabled_for_user_end).isPresent()) {
            Config config = new Config();
            config.setKey(DefinedKeys.is_rrf_enabled_for_user_end);
            config.setValue("FALSE");
            configRepository.save(config);
        }
    }

    public void createIsTaxStatementVisibleForUserEnd() {
        if (!configRepository.findConfigByKey(DefinedKeys.is_income_tax_visibility_enabled_for_user_end).isPresent()) {
            Config config = new Config();
            config.setKey(DefinedKeys.is_income_tax_visibility_enabled_for_user_end);
            config.setValue("FALSE");
            configRepository.save(config);
        }
    }

    public void createTimeSlot() {
        if (timeSlotRepository.findAll().size() == 0) {
            log.debug("Creating Time Slots for Flex Schedule...");

            createTimeSlot("Shift 1", 8, 30, 16, 30);
            createTimeSlot("Shift 2", 9, 0, 17, 00);
            createTimeSlot("Shift 3", 9, 30, 17, 30);
            TimeSlot timeSlotShiftGeneral = createTimeSlot(GENERAL_SHIFT_TITLE, 10, 0, 18, 0);
            assignTimeSlot(timeSlotShiftGeneral);
            createTimeSlot("Shift 5", 10, 30, 18, 30);
        }
    }

    private TimeSlot createTimeSlot(String title, int inTimeHour, int inTimeMinutes, int outTimeHour, int outTimeMinutes) {
        TimeSlot timeSlot = new TimeSlot();

        timeSlot
            .title(title)
            .inTime(
                Instant
                    .now()
                    .atZone(ZoneOffset.systemDefault())
                    .withHour(inTimeHour)
                    .withMinute(inTimeMinutes)
                    .withSecond(00)
                    .withNano(00)
                    .toInstant()
            )
            .outTime(
                Instant
                    .now()
                    .atZone(ZoneOffset.systemDefault())
                    .withHour(outTimeHour)
                    .withMinute(outTimeMinutes)
                    .withSecond(00)
                    .withNano(00)
                    .toInstant()
            );

        return timeSlotRepository.save(timeSlot);
    }

    private void assignTimeSlot(TimeSlot timeSlot) {
        log.debug("Assigning General Time Slots to Employees ...");
        List<Employee> employeeList = employeeRepository.findAll();
        for (Employee employee : employeeList) {
            employee.currentInTime(timeSlot.getInTime()).currentOutTime(timeSlot.getOutTime());
            employee.setUpdatedAt(LocalDateTime.now());
            employeeRepository.save(employee);
        }
    }

    public void setDefaultShift() {
        Optional<TimeSlot> defaultTimeSlot = timeSlotRepository.findDefaultTimeSlot();
        if (!defaultTimeSlot.isPresent()) {
            List<TimeSlot> timeSlots = timeSlotRepository.findAll();
            boolean generalShiftFound = false;
            for (TimeSlot timeSlot : timeSlots) {
                if (timeSlot.getTitle().equals(GENERAL_SHIFT_TITLE)) {
                    generalShiftFound = true;
                    timeSlot.setIsDefaultShift(true);
                    timeSlotRepository.save(timeSlot);
                }
            }
            if (!generalShiftFound) {
                timeSlots.get(0).setIsDefaultShift(true);
                timeSlotRepository.save(timeSlots.get(0));
            }
        }
    }

    /*public void createInsuranceRelation() {
        if (insuranceRelationRepository.findAll().size() == 0) {
            insuranceRelationRepository.save(createInsuranceRelation("Child 1", Relation.CHILD));
            insuranceRelationRepository.save(createInsuranceRelation("Child 2", Relation.CHILD));
            insuranceRelationRepository.save(createInsuranceRelation("Child 3", Relation.CHILD));
            insuranceRelationRepository.save(createInsuranceRelation("Spouse", Relation.SPOUSE));
            insuranceRelationRepository.save(createInsuranceRelation("Self", Relation.SELF));
        }
    }*/

    /*private InsuranceRelation createInsuranceRelation(String relationName, Relation relation) {
        InsuranceRelation insuranceRelation = new InsuranceRelation();
        insuranceRelation.setRelationName(relationName);
        insuranceRelation.setRelation(relation);
        return insuranceRelation;
    }*/

    public void createInsuranceConfiguration() {
        if (insuranceConfigurationRepository.findAll().size() == 0) {
            InsuranceConfiguration insuranceConfiguration = new InsuranceConfiguration();
            insuranceConfiguration.setMaxAllowedChildAge(25d);
            insuranceConfiguration.setMaxTotalClaimLimitPerYear(200000d);
            insuranceConfiguration.setInsuranceClaimLink("");
            insuranceConfigurationRepository.save(insuranceConfiguration);
        }
    }
}
