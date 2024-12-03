package com.bits.hr.service.config;

import com.bits.hr.domain.Config;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.repository.ConfigRepository;
import com.bits.hr.service.config.DTO.AttendanceRegularizationMethod;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetConfigValueByKeyService {

    @Autowired
    private ConfigRepository configRepository;

    public boolean isTaxableAllowance01() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.is_taxable_allowance01);
        if (!configOptional.isPresent()) {
            return false;
        } else {
            String value = configOptional.get().getValue().toLowerCase(Locale.ROOT);
            if (value.equals("true")) return true; else return false;
        }
    }

    public boolean isTaxableAllowance02() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.is_taxable_allowance02);
        if (!configOptional.isPresent()) {
            return false;
        } else {
            String value = configOptional.get().getValue().toLowerCase(Locale.ROOT);
            if (value.equals("true")) return true; else return false;
        }
    }

    public boolean isTaxableAllowance03() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.is_taxable_allowance03);
        if (!configOptional.isPresent()) {
            return false;
        } else {
            String value = configOptional.get().getValue().toLowerCase(Locale.ROOT);
            if (value.equals("true")) return true; else return false;
        }
    }

    public boolean isTaxableAllowance04() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.is_taxable_allowance04);
        if (!configOptional.isPresent()) {
            return false;
        } else {
            String value = configOptional.get().getValue().toLowerCase(Locale.ROOT);
            if (value.equals("true")) return true; else return false;
        }
    }

    public boolean isTaxableAllowance05() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.is_taxable_allowance05);
        if (!configOptional.isPresent()) {
            return false;
        } else {
            String value = configOptional.get().getValue().toLowerCase(Locale.ROOT);
            if (value.equals("true")) return true; else return false;
        }
    }

    public boolean isTaxableAllowance06() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.is_taxable_allowance06);
        if (!configOptional.isPresent()) {
            return false;
        } else {
            String value = configOptional.get().getValue().toLowerCase(Locale.ROOT);
            if (value.equals("true")) return true; else return false;
        }
    }

    public String getAllowance01Name() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.name_allowance01);
        if (!configOptional.isPresent()) {
            return "Living Allowance-LFA";
        } else {
            if (configOptional.get().getValue().trim().equals("NOT_APPLICABLE")) {
                return "";
            }
            return configOptional.get().getValue().trim();
        }
    }

    public String getAllowance02Name() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.name_allowance02);
        if (!configOptional.isPresent()) {
            return "Car Allowance";
        } else {
            if (configOptional.get().getValue().trim().equals("NOT_APPLICABLE")) {
                return "";
            }
            return configOptional.get().getValue().trim();
        }
    }

    public String getAllowance03Name() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.name_allowance03);
        if (!configOptional.isPresent()) {
            return "House Rent Reimbursement";
        } else {
            if (configOptional.get().getValue().trim().equals("NOT_APPLICABLE")) {
                return "";
            }
            return configOptional.get().getValue().trim();
        }
    }

    public String getAllowance04Name() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.name_allowance04);
        if (!configOptional.isPresent()) {
            return "Company Secretary";
        } else {
            if (configOptional.get().getValue().trim().equals("NOT_APPLICABLE")) {
                return "";
            }
            return configOptional.get().getValue().trim();
        }
    }

    public String getAllowance05Name() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.name_allowance05);
        if (!configOptional.isPresent()) {
            return "";
        } else {
            if (configOptional.get().getValue().trim().equals("NOT_APPLICABLE")) {
                return "";
            }
            return configOptional.get().getValue().trim();
        }
    }

    public String getAllowance06Name() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.name_allowance06);
        if (!configOptional.isPresent()) {
            return "";
        } else {
            if (configOptional.get().getValue().trim().equals("NOT_APPLICABLE")) {
                return "";
            }
            return configOptional.get().getValue().trim();
        }
    }

    public double getHujurHadia() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.hujur_hadia);
        return configOptional.map(config -> Double.parseDouble(config.getValue().trim())).orElse(0d);
    }

    public int getCasualLeaveLimitPerMonth() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.casual_leave_limit_per_month);
        return configOptional.map(config -> (int) Double.parseDouble(config.getValue().trim())).orElse(2);
    }

    public int getAnnualLeaveLimitPerMonth() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.annual_leave_limit_per_month);
        return configOptional.map(config -> (int) Double.parseDouble(config.getValue().trim())).orElse(31);
    }

    public int getMonthlyAttendanceRegularisationDay() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.monthly_attendance_regularization_day);
        return configOptional.map(config -> (int) Double.parseDouble(config.getValue().trim())).orElse(20);
    }

    public AttendanceRegularizationMethod getAttendanceRegularizationMethod() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.attendance_regularisation_method);
        if (configOptional.isPresent()) {
            String value = configOptional.get().getValue().toUpperCase(Locale.ROOT).trim();
            if (value.equals(AttendanceRegularizationMethod.FULL_MONTH.name().toUpperCase(Locale.ROOT))) {
                return AttendanceRegularizationMethod.FULL_MONTH;
            } else {
                return AttendanceRegularizationMethod.FIXED_DAY;
            }
        } else {
            return AttendanceRegularizationMethod.FIXED_DAY;
        }
    }

    public boolean isMultiplePfAccountSupported() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.is_multiple_pf_account_supported);
        if (!configOptional.isPresent()) {
            return false;
        } else {
            String value = configOptional.get().getValue().toLowerCase(Locale.ROOT);
            return value.equals("true") ? true : false;
        }
    }

    public int getNoticePeriodInDays(EmployeeCategory employeeCategory) {
        if (employeeCategory == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
            Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.notice_period_regular_confirmed_employee);
            return configOptional.map(config -> (int) Double.parseDouble(config.getValue().trim())).orElse(60);
        } else if (employeeCategory == EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
            Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.notice_period_contractual_employee);
            return configOptional.map(config -> (int) Double.parseDouble(config.getValue().trim())).orElse(30);
        } else if (employeeCategory == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) {
            Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.notice_period_regular_probationary_employee);
            return configOptional.map(config -> (int) Double.parseDouble(config.getValue().trim())).orElse(30);
        } else {
            return 0;
        }
    }

    public List<String> getListOfPinForAutomatedApprovalProcess(String processType) {
        List<String> pinList = new ArrayList<>();

        if (processType.equals(DefinedKeys.subordinate_auto_leave_approval)) {
            Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.subordinate_auto_leave_approval);
            if (configOptional.isPresent()) {
                pinList = Arrays.asList(configOptional.get().getValue().split(",[ ]*"));
            }
        }
        if (processType.equals(DefinedKeys.subordinate_auto_attendance_approval)) {
            Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.subordinate_auto_attendance_approval);
            if (configOptional.isPresent()) {
                pinList = Arrays.asList(configOptional.get().getValue().split(",[ ]*"));
            }
        }
        if (processType.equals(DefinedKeys.self_auto_attendance_application)) {
            Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.self_auto_attendance_application);
            if (configOptional.isPresent()) {
                pinList = Arrays.asList(configOptional.get().getValue().split(",[ ]*"));
            }
        }
        pinList.replaceAll(String::trim);

        return pinList;
    }

    public boolean incomeTaxDeductionOnResigningMonth() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.income_tax_deduction_on_resigning_month);
        if (!configOptional.isPresent()) {
            // reflecting brac it services policy
            return false;
        } else {
            String value = configOptional.get().getValue().toLowerCase(Locale.ROOT);
            if (value.equals("true")) return true; else {
                return false;
            }
        }
    }

    public String getDomainName() {
        Optional<Config> configOptional = configRepository.findConfigByKey(DefinedKeys.organisational_email_domain_name);
        if (!configOptional.isPresent()) {
            return "bracits.com";
        } else {
            return configOptional.get().getValue().trim().toLowerCase(Locale.ROOT);
        }
    }
}
