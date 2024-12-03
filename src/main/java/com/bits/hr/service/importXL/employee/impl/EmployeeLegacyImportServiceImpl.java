package com.bits.hr.service.importXL.employee.impl;

import static com.bits.hr.service.importXL.employee.rowConstants.EmployeeLegacyImportRowConstant.*;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.*;
import com.bits.hr.repository.*;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.event.EmployeeRegistrationEvent;
import com.bits.hr.service.importXL.GenericUploadServiceImpl;
import com.bits.hr.service.importXL.XLImportCommonService;
import com.bits.hr.service.importXL.employee.EmployeeLegacyImportService;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.util.NameUtil;
import com.bits.hr.util.PinUtil;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmployeeLegacyImportServiceImpl implements EmployeeLegacyImportService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private GenericUploadServiceImpl genericUploadService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private NationalityRepository nationalityRepository;

    @Autowired
    private BankBranchRepository bankBranchRepository;

    @Autowired
    private BandRepository bandRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    @Override
    public boolean importEmployeesLegacyXl(MultipartFile file) {
        try {
            ArrayList<Employee> employeeArrayList = new ArrayList<>();
            // 1. take the data
            List<ArrayList<String>> data = genericUploadService.upload(file);

            Map<String, String> reportingTo = new HashMap<String, String>();
            // 2. remove xlsx headers
            data.remove(0);

            // 3. insert data into object

            for (ArrayList<String> dt : data) {
                /*
                A   0   sl
                B   1   pin
                C   2   fullName
                D   3   band
                E   4   designation

                F   5   unit
                G   6   dept
                H   7   LM pin

                8   9   10  11  12  13  14  15  16
                I   J   K   L   M   N   O   P   Q   == SKIP

                R   17   Location
                S   18   floor
                T   19  space

                U   20  official email
                V   21  personal email
                W   22  cell
                X   23  whatssup
                Y   24  emergency contract
                Z   25  skype
                AA  26  job started ( in work life )
                AB  27  bits join ( doj )
                AC  28  DoC/Contract End
                AD  29  DoB
                AE  30  Blood Group

                AF   31  Gender
                AG   32  Status ( Martial)
                AH   33  Religion
                AI   34  NID
                AJ   35  Job Status XX

                AK   36  Contract Status

                AL   37  biTS Duration  XX
                AM   38  biTS Years     XX
                AN   39  Total duration XX
                AO   40  Total Years    XX
            */

                // if empty row skip
                if (!XLImportCommonService.isXLRowValid(dt)) {
                    continue;
                }

                Employee employee = new Employee();

                String pin = PinUtil.formatPin(dt.get(LRC_PIN));

                // if available , overwrite
                // if avilable and resigned / hold status then skip
                if (employeeRepository.findEmployeeByPin(pin).isPresent()) {
                    employee = employeeRepository.findEmployeeByPin(pin).get();
                    if (
                        employee.getEmploymentStatus() == EmploymentStatus.RESIGNED ||
                        employee.getEmploymentStatus() == EmploymentStatus.HOLD
                    ) {
                        continue;
                    }
                }
                employee.setPin(pin.trim());

                String fullName = dt.get(LRC_FULL_NAME).equals("0") ? "" : NameUtil.makeItProperCase(dt.get(LRC_FULL_NAME).trim());
                employee.setFullName(fullName);

                String surName = fullName.substring(fullName.lastIndexOf(" ") + 1);
                employee.setSurName(surName);

                employee.setBand(getOrCreateBand(dt.get(LRC_BAND)));

                employee.setDesignation(getOrCreateDesignation(dt.get(LRC_DESIGNATION)));

                employee.setUnit(getOrCreateUnit(dt.get(LRC_UNIT)));

                employee.setDepartment(getOrCreateDepartment(dt.get(LRC_DEPT)));

                reportingTo.put(employee.getPin(), PinUtil.formatPin(dt.get(LRC_LM_PIN)));

                //                R   17   Location
                //                S   18   floor
                //                T   19  space

                String location = dt.get(LRC_LOCATION).equals("0") ? "" : dt.get(LRC_LOCATION).trim();
                String floor = dt.get(LRC_FLOOR).equals("0") ? "" : dt.get(LRC_FLOOR).trim();
                String space = dt.get(LRC_SPACE).equals("0") ? "" : dt.get(LRC_SPACE).trim();

                String officeLocation = location + "-" + floor + "-" + space;

                employee.setLocation(officeLocation);

                //                U   20  official email

                String officialEmail = dt.get(LRC_OFFICIAL_EMAIL).equals("0") ? "" : dt.get(LRC_OFFICIAL_EMAIL);
                employee.setOfficialEmail(officialEmail);

                //                V   21  personal email

                String personalEmail = dt.get(LRC_PERSONAL_EMAIL).equals("0") ? "" : dt.get(LRC_PERSONAL_EMAIL).trim();
                employee.setPersonalEmail(personalEmail);

                //                W   22  cell
                String officialContactNumber = dt.get(LRC_CELL).equals("0") ? "" : formatCellNumber(dt.get(LRC_CELL));
                employee.setOfficialContactNo(officialContactNumber);

                //                X   23  whatssup
                String whatssupId = dt.get(LRC_WHATSSUP).equals("0") ? "" : formatCellNumber(dt.get(LRC_WHATSSUP));
                employee.setWhatsappId(whatssupId);

                employee.setPersonalContactNo(whatssupId);

                //                Y   24  emergency contract
                String EmergencyContactPersonContactNumber = dt.get(LRC_EMERGENCY_CONTRACT).equals("0")
                    ? ""
                    : formatCellNumber(dt.get(LRC_EMERGENCY_CONTRACT));
                employee.setEmergencyContactPersonContactNumber(EmergencyContactPersonContactNumber);

                //                Z   25  skype
                String skypeId = dt.get(LRC_SKYPE).equals("0") ? "" : dt.get(LRC_SKYPE);
                skypeId = skypeId.contains("E") ? formatCellNumber(skypeId) : skypeId;

                employee.setSkypeId(skypeId);
                //                AA  26  job started ( in work life ) XX
                //                AB  27  bits join ( doj )
                LocalDate doj = null;
                if (!dt.get(LRC_Doj).equals("0")) {
                    doj = doubleToDate(Double.parseDouble(dt.get(LRC_Doj)));
                }
                employee.setDateOfJoining(doj);

                //                AD  29  DoB

                LocalDate dob = null;
                if (!dt.get(LRC_DoB).equals("0")) {
                    dob = doubleToDate(Double.parseDouble(dt.get(LRC_DoB)));
                }
                employee.setDateOfBirth(dob);

                //                AE  30  Blood Group
                employee.setBloodGroup(getBloodGroupEnum(dt.get(LRC_BLOOD_GROUP)));

                //                AF   31  Gender
                employee.setGender(getGenderEnum(dt.get(LRC_GENDER)));

                //                AG   32  Status ( Martial)
                employee.setMaritalStatus(getMaritualStatusEnum(dt.get(LRC_MARITAL_STATUS)));

                //                AH   33  Religion
                employee.setReligion(getReligionEnum(dt.get(LRC_RELIGION))); // ISLAM, HINDU, BUDDHA, CHRISTIAN, OTHER
                //                AI   34  NID
                String nationalIdNumber = dt.get(LRC_NID).equals("0") ? "" : formatIDNumber(dt.get(LRC_NID));
                employee.setNationalIdNo(nationalIdNumber);

                //                AJ   35  Job Status XX
                //                AK   36  Contract Status
                employee.setEmployeeCategory(getEmployeeCategoryEnum(dt.get(LRC_EMPLOYEE_CATEGORY)));

                //                AC  28  DoC/Contract End
                LocalDate doc_or_ce = null;
                if (!dt.get(LRC_DoC_OR_CONTRACT_END).equals("0")) {
                    doc_or_ce = doubleToDate(Double.parseDouble(dt.get(LRC_DoC_OR_CONTRACT_END)));
                }
                if (
                    employee.getEmployeeCategory() == EmployeeCategory.CONTRACTUAL_EMPLOYEE ||
                    employee.getEmployeeCategory() == EmployeeCategory.INTERN
                ) {
                    employee.setContractPeriodEndDate(doc_or_ce);
                }
                if (
                    employee.getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE ||
                    employee.getEmployeeCategory() == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE
                ) {
                    employee.setDateOfConfirmation(doc_or_ce);
                    if (employee.getEmployeeCategory() == EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) {
                        employee.setProbationPeriodEndDate(doc_or_ce.minusDays(1));
                    }
                }

                //                employee.setOfficeTimeDuration(8d);
                //                Instant checkInTime = Instant.parse("2021-01-01T04:00:00.00Z");
                //                Instant checkOutTime = Instant.parse("2021-01-01T12:00:00.00Z");
                //                employee.setCheckInTime(checkInTime);
                //                employee.setCheckOutTime(checkOutTime);

                double grossSalary = 0;
                try {
                    grossSalary = Double.parseDouble(dt.get(LRC_GROSS_SALARY));
                    employee.setMainGrossSalary(grossSalary);
                } catch (Exception e) {
                    employee.setMainGrossSalary(grossSalary);
                }

                long mobileCelling = 0;
                try {
                    mobileCelling = (long) Double.parseDouble(dt.get(LRC_MOBILE_CELLING));
                    employee.setMobileCelling(mobileCelling);
                } catch (Exception e) {
                    employee.setMobileCelling(mobileCelling);
                }

                double welfareFund = 0;
                try {
                    welfareFund = Double.parseDouble(dt.get(LRC_WELFARE_FUND));
                    employee.setWelfareFundDeduction(welfareFund);
                } catch (Exception e) {
                    employee.setWelfareFundDeduction(welfareFund);
                }

                employee.setPayType(PayType.MONTHLY);
                employee.setDisbursementMethod(DisbursementMethod.BANK); //  BANK, CASH, MOBILE_BANKING

                //                String bankName = dt.get(BANK_NAME).equals("0") ? "" : dt.get(BANK_NAME);
                //                employee.setBankName(bankName);
                //
                //                String bankAccountNumber = dt.get(BANK_ACCOUNT_NO).equals("0") ? "" : dt.get(BANK_ACCOUNT_NO);
                //                employee.setBankAccountNo(bankAccountNumber);

                //employeeRepository.save(employee);
                employeeArrayList.add(employee);
            }

            User user = currentEmployeeService.getCurrentUser().get();

            for (Employee e : employeeArrayList) {
                e.setEmploymentStatus(EmploymentStatus.ACTIVE);
                e.setCreatedAt(Instant.now());
                e.setCreatedBy(user.getEmail());
                e.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(e);
                employeeService.createJoiningEntry(employeeMapper.toDto(e));
                publishEvent(e.getOfficialEmail(), e.getPin());
                eventLoggingPublisher.publishEvent(user, e, RequestMethod.POST, "Employee Legacy Import");
            }

            for (Map.Entry<String, String> entry : reportingTo.entrySet()) {
                String employee = entry.getKey();
                String reportsTo = entry.getValue();
                if (
                    employeeRepository.findEmployeeByPin(employee).isPresent() &&
                    employeeRepository.findEmployeeByPin(reportsTo).isPresent()
                ) {
                    Employee emp = employeeRepository.findEmployeeByPin(employee).get();
                    Employee rpt = employeeRepository.findEmployeeByPin(reportsTo).get();
                    emp.setReportingTo(rpt);
                    emp.setUpdatedAt(LocalDateTime.now());
                    employeeRepository.save(emp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    Gender getGenderEnum(String s) {
        s = s.toLowerCase(Locale.ROOT);
        if (s.trim().equals("male")) return Gender.MALE; else if (s.trim().equals("female")) return Gender.FEMALE; else return Gender.OTHER;
    }

    Unit getOrCreateUnit(String str) {
        return unitRepository
            .findUnitByUnitNameIgnoreCase(str.trim())
            .orElseGet(() -> {
                Unit u = new Unit();
                u.setUnitName(str);
                return unitRepository.save(u);
            });
    }

    Department getOrCreateDepartment(String str) {
        return departmentRepository
            .findDepartmentByDepartmentNameIgnoreCase(str)
            .orElseGet(() -> {
                Department d = new Department();
                d.setDepartmentName(str);
                return departmentRepository.save(d);
            });
    }

    Employee getReportingTo(String s) {
        return employeeRepository
            .findEmployeeByPin(s.trim())
            .orElseGet(() -> {
                return null;
            });
    }

    // getOrCreateBankBranch
    BankBranch getOrCreateBankBranch(String s) {
        return bankBranchRepository
            .findBankBranchByBranchName(s.trim())
            .orElseGet(() -> {
                BankBranch d = new BankBranch();
                d.setBranchName(s);
                return bankBranchRepository.save(d);
            });
    }

    // getOrCreateBand
    Band getOrCreateBand(String s) {
        final String band = PinUtil.formatPin(s).trim();
        return bandRepository
            .findBandByBandName(band)
            .orElseGet(() -> {
                Band d = new Band();
                d.setBandName(band);
                d.setMaxSalary(0.0);
                d.setMinSalary(0.0);
                d.setMobileCelling(0.0);
                d.setWelfareFund(0.0);

                return bandRepository.save(d);
            });
    }

    // getOrCreateNationality
    Nationality getOrCreateNationality(String s) {
        return nationalityRepository
            .findNationalityByNationalityName(s.trim())
            .orElseGet(() -> {
                Nationality d = new Nationality();
                d.setNationalityName(s);
                return nationalityRepository.save(d);
            });
    }

    Designation getOrCreateDesignation(String s) {
        return designationRepository
            .findDesignationByDesignationName(s.trim())
            .orElseGet(() -> {
                Designation d = new Designation();
                d.setDesignationName(s.trim());
                return designationRepository.save(d);
            });
    }

    EmployeeCategory getEmployeeCategoryEnum(String s) {
        s = s.trim();

        switch (s) {
            case "REGULAR_CONFIRMED_EMPLOYEE":
            case "Regular Confirmed Employee":
            case "Confirmed":
            case "confirmed":
            case "Confirm":
                return EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE;
            case "REGULAR_PROVISIONAL_EMPLOYEE":
            case "Regular Probational Employee":
            case "Probational":
            case "probational":
            case "Probation":
                return EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE;
            case "CONTRACTUAL_EMPLOYEE":
            case "Contractual Employee":
            case "Contractual":
            case "contractual":
            case "by Contract":
            case "by contract":
                return EmployeeCategory.CONTRACTUAL_EMPLOYEE;
            default:
                return EmployeeCategory.INTERN;
        }
    }

    BloodGroup getBloodGroupEnum(String s) {
        s = s.trim();
        switch (s) {
            case "A_POSITIVE":
            case "A+ve":
            case "A+":
                return BloodGroup.A_POSITIVE;
            case "B_POSITIVE":
            case "B+ve":
            case "B+":
                return BloodGroup.B_POSITIVE;
            case "O_POSITIVE":
            case "O+ve":
            case "O+":
                return BloodGroup.O_POSITIVE;
            case "AB_POSITIVE":
            case "AB+ve":
            case "AB+":
                return BloodGroup.AB_POSITIVE;
            case "A_NEGATIVE":
            case "A-ve":
            case "A-":
                return BloodGroup.A_NEGATIVE;
            case "B_NEGATIVE":
            case "B-ve":
            case "B-":
                return BloodGroup.B_NEGATIVE;
            case "O_NEGATIVE":
            case "O-ve":
            case "O-":
                return BloodGroup.O_NEGATIVE;
            case "AB_NEGATIVE":
            case "AB-ve":
            case "AB-":
                return BloodGroup.AB_NEGATIVE;
            default:
                return null;
        }
    }

    Religion getReligionEnum(String s) {
        s = s.trim(); //// ISLAM, HINDU, BUDDHA, CHRISTIAN, OTHER
        s = s.toLowerCase(Locale.ROOT);
        switch (s) {
            case "islam":
            case "muslim":
                return Religion.ISLAM;
            case "hindu":
            case "hinduism":
                return Religion.HINDU;
            case "buddha":
            case "buddhism":
                return Religion.BUDDHA;
            case "christian":
            case "christianism":
                return Religion.CHRISTIAN;
            default:
                return Religion.OTHER;
        }
    }

    MaritalStatus getMaritualStatusEnum(String s) {
        s = s.trim().toLowerCase(Locale.ROOT);
        switch (s) {
            case "married":
                return MaritalStatus.MARRIED;
            case "divorced":
                return MaritalStatus.DIVORCED;
            case "widowed":
                return MaritalStatus.WIDOWED;
            default:
                return MaritalStatus.SINGLE;
        }
    }

    PayType getPayTypeEnum(String s) {
        s = s.trim();
        switch (s) {
            case "HOURLY":
                return PayType.HOURLY;
            case "UNPAID":
                return PayType.UNPAID;
            default:
                return PayType.MONTHLY;
        }
    }

    DisbursementMethod getDibrushmentEnum(String s) {
        s = s.trim();
        switch (s) {
            case "CASH":
            case "Cash":
            case "cash":
                return DisbursementMethod.CASH;
            case "MOBILE_BANKING":
            case "Mobile Banking":
            case "mobile Banking":
                return DisbursementMethod.MOBILE_BANKING;
            default:
                return DisbursementMethod.BANK;
        }
    }

    CardType getCardEnum(String s) {
        s = s.trim();
        switch (s) {
            case "CREDIT_CARD":
            case "Credit Card":
            case "Credit":
            case "credit":
                return CardType.CREDIT_CARD;
            case "DEBIT_CARD":
            case "Debit Card":
            case "Debit":
            case "debit":
                return CardType.DEBIT_CARD;
            case "PREPAID_CARD":
            case "Prepaid Card":
            case "Prepaid":
            case "prepaid":
                return CardType.PREPAID_CARD;
            default:
                return null;
        }
    }

    LocalDate doubleToDate(Double d) {
        Date javaDate = DateUtil.getJavaDate(d);
        //System.out.println(new SimpleDateFormat("MM/dd/yyyy").format(javaDate));
        LocalDate date = javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date;
    }

    Instant doubleToInstant(Double d) {
        Date javaDate = DateUtil.getJavaDate(d);
        //System.out.println(new SimpleDateFormat("MM/dd/yyyy").format(javaDate));
        Instant date = javaDate.toInstant();
        return date;
    }

    private void publishEvent(String officialEmail, String pin) {
        eventPublisher.publishEvent(new EmployeeRegistrationEvent(this, officialEmail, pin));
    }

    private String formatCellNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replace("-", "");

        if (phoneNumber.length() >= 10) {
            if (phoneNumber.contains("E")) {
                phoneNumber = BigDecimal.valueOf(Double.parseDouble(phoneNumber)).toPlainString();
            }

            if (!phoneNumber.substring(0, 4).equals("+880")) {
                phoneNumber = "+880" + phoneNumber;
            }
        }

        return phoneNumber;
    }

    /*
     * passport and nid number correction utility function
     * */
    private String formatIDNumber(String id) {
        id = id.replace("-", "");

        if (id.contains("E")) {
            id = BigDecimal.valueOf(Double.parseDouble(id)).toPlainString();
        }

        return id;
    }
}
