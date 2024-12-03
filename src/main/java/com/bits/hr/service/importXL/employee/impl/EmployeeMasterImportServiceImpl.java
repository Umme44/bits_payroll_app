package com.bits.hr.service.importXL.employee.impl;

import static com.bits.hr.service.importXL.employee.rowConstants.EmployeeMasterRowConstants.*;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.*;
import com.bits.hr.repository.*;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.event.EmployeeRegistrationEvent;
import com.bits.hr.service.importXL.GenericUploadServiceImpl;
import com.bits.hr.service.importXL.XLImportCommonService;
import com.bits.hr.service.importXL.employee.EmployeeMasterImportService;
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
public class EmployeeMasterImportServiceImpl implements EmployeeMasterImportService {

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
                0   referenceId
                1   pin
                2   fullName
                3   surName
                4   nationalIdNo
                5   dateOfBirth
                6   placeOfBirth
                7   fatherName
                8   motherName
                9   bloodGroup
                10  presentAddress
                11  permanentAddress
                12  personalContactNo
                13  personalEmail
                14  religion
                15  maritalStatus
                16  dateOfMarriage
                17  spouseName
                18  officialEmail
                19  officialContactNo
                20  officePhoneExtension
                21  whatsappId
                22  skypeId
                23  emergencyContactPersonName
                24  emergencyContactPersonRelationshipWithEmployee
                25  emergencyContactPersonContactNumber
                26  mainGrossSalary
                27  unit
                28  employeeCategory
                29  location
                30  officeTimeDuration
                31  checkInTime
                32  checkOutTime
                33  dateOfJoining
                34  dateOfConfirmation
                35  isProbationaryPeriodExtended
                36  probationPeriodExtendedTo
                37  payType
                38  disbursementMethod
                39  bankName
                40  bankAccountNo
                41  mobileCelling
                42  bkashNumber
                43  cardType
                44  cardNumber
                45  tinNumber
                46  passportNo
                47  passportPlaceOfIssue
                48  passportIssuedDate
                49  passportExpiryDate
                50  Gender
                51  WelfareFundDeduction
                52  Designation
                53  Department
                54  ReportingTo
                55  Nationality
                56  BankBranch
                57  Band
                58  User
            */

                // if empty row skip
                if (!XLImportCommonService.isXLRowValid(dt)) {
                    continue;
                }

                Employee employee = new Employee();

                String referenceId = PinUtil.formatPin(dt.get(REFERENCE_ID));
                employee.setReferenceId(referenceId);

                String pin = PinUtil.formatPin(dt.get(PIN));

                // if available , do not overwrite
                if (employeeRepository.findEmployeeByPin(pin).isPresent()) {
                    continue;
                }
                employee.setPin(pin);

                String fullName = dt.get(FULL_NAME).equals("0") ? "" : dt.get(FULL_NAME);
                employee.setFullName(fullName);

                String surName = dt.get(SUR_NAME).equals("0") ? "" : dt.get(SUR_NAME);
                employee.setSurName(surName);

                String nationalIdNumber = dt.get(NATIONAL_ID_NO).equals("0") ? "" : formatIDNumber(dt.get(NATIONAL_ID_NO));
                employee.setNationalIdNo(nationalIdNumber);

                LocalDate dob = null;
                if (!dt.get(DATE_OF_BIRTH).equals("0")) {
                    dob = doubleToDate(Double.parseDouble(dt.get(DATE_OF_BIRTH)));
                }
                employee.setDateOfBirth(dob);

                String placeOfBirth = dt.get(PLACE_OF_BIRTH).equals("0") ? "" : dt.get(PLACE_OF_BIRTH);
                employee.setPlaceOfBirth(placeOfBirth);

                String fatherName = dt.get(FATHER_NAME).equals("0") ? "" : dt.get(FATHER_NAME);
                employee.setFatherName(fatherName);

                String motherName = dt.get(MOTHER_NAME).equals("0") ? "" : dt.get(MOTHER_NAME);
                employee.setMotherName(motherName);

                employee.setBloodGroup(getBloodGroupEnum(dt.get(BLOOD_GROUP)));

                String presentAdress = dt.get(PRESENT_ADDRESS).equals("0") ? "" : dt.get(PRESENT_ADDRESS);
                employee.setPresentAddress(presentAdress);

                String permanentAddress = dt.get(PERMANENT_ADDRESS).equals("0") ? "" : dt.get(PERMANENT_ADDRESS);
                employee.setPermanentAddress(permanentAddress); //11

                String personalContactNo = dt.get(PERSONAL_CONTACT_NO).equals("0") ? "" : formatCellNumber(dt.get(PERSONAL_CONTACT_NO));
                employee.setPersonalContactNo(personalContactNo);

                String personalEmail = dt.get(PERSONAL_EMAIL).equals("0") ? "" : dt.get(PERSONAL_EMAIL);
                employee.setPersonalEmail(personalEmail);

                employee.setReligion(getReligionEnum(dt.get(RELIGION))); // ISLAM, HINDU, BUDDHA, CHRISTIAN, OTHER
                employee.setMaritalStatus(getMaritualStatusEnum(dt.get(MARITAL_STATUS)));

                LocalDate dom = null;
                if (!dt.get(DATE_OF_MARRIAGE).equals("0")) {
                    dom = doubleToDate(Double.parseDouble(dt.get(DATE_OF_MARRIAGE)));
                }
                employee.setDateOfMarriage(dom);

                String spouseName = dt.get(SPOUSE_NAME).equals("0") ? "" : dt.get(SPOUSE_NAME);
                employee.setSpouseName(spouseName);

                String officialEmail = dt.get(OFFICIAL_EMAIL).equals("0") ? "" : dt.get(OFFICIAL_EMAIL);
                employee.setOfficialEmail(officialEmail);

                String officialContactNumber = dt.get(OFFICIAL_CONTACT_NO).equals("0") ? "" : formatCellNumber(dt.get(OFFICIAL_CONTACT_NO));
                employee.setOfficialContactNo(officialContactNumber);

                String officePhoneExtension = dt.get(OFFICE_PHONE_EXTENSION).equals("0") ? "" : dt.get(OFFICE_PHONE_EXTENSION);
                employee.setOfficePhoneExtension(officePhoneExtension); // 20

                String whatssupId = dt.get(WHATSAPP_ID).equals("0") ? "" : formatCellNumber(dt.get(WHATSAPP_ID));
                employee.setWhatsappId(whatssupId);
                String skypeId = dt.get(SKYPE_ID).equals("0") ? "" : dt.get(SKYPE_ID);
                skypeId = skypeId.contains("E") ? formatCellNumber(skypeId) : skypeId;

                employee.setSkypeId(skypeId);

                String emergencyContactPersonName = dt.get(EMERGENCY_CONTACT_PERSON_NAME).equals("0")
                    ? ""
                    : dt.get(EMERGENCY_CONTACT_PERSON_NAME);
                employee.setEmergencyContactPersonName(emergencyContactPersonName);

                String EmergencyContactPersonRelationshipWithEmployee = dt.get(EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE) == "0"
                    ? ""
                    : dt.get(EMERGENCY_CONTACT_PERSON_RELATIONSHIP_WITH_EMPLOYEE);
                employee.setEmergencyContactPersonRelationshipWithEmployee(EmergencyContactPersonRelationshipWithEmployee);

                String EmergencyContactPersonContactNumber = dt.get(EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER).equals("0")
                    ? ""
                    : formatCellNumber(dt.get(EMERGENCY_CONTACT_PERSON_CONTACT_NUMBER));
                employee.setEmergencyContactPersonContactNumber(EmergencyContactPersonContactNumber);

                employee.setMainGrossSalary(Double.parseDouble(dt.get(MAIN_GROSS_SALARY)));
                employee.setUnit(getOrCreateUnit(dt.get(UNIT)));
                employee.setEmployeeCategory(getEmployeeCategoryEnum(dt.get(EMPLOYEE_CATEGORY)));

                String officeLocation = dt.get(LOCATION).equals("0") ? "" : dt.get(LOCATION);
                employee.setLocation(officeLocation);

                LocalDate doj = null;
                if (!dt.get(DATE_OF_JOINING).equals("0")) {
                    doj = doubleToDate(Double.parseDouble(dt.get(DATE_OF_JOINING)));
                }
                employee.setDateOfJoining(doj);

                LocalDate doc = null;
                if (!dt.get(DATE_OF_CONFIRMATION).equals("0")) {
                    doc = doubleToDate(Double.parseDouble(dt.get(DATE_OF_CONFIRMATION)));
                }
                employee.setDateOfConfirmation(doc);

                boolean isProbationaryPeriodExtended;
                isProbationaryPeriodExtended = dt.get(35).trim().equals("yes");

                employee.setIsProbationaryPeriodExtended(isProbationaryPeriodExtended);

                LocalDate probationPeriodExtendedTo = null;
                if (dt.get(PROBATION_PERIOD_EXTENDED_TO) != "0") {
                    probationPeriodExtendedTo = doubleToDate(Double.parseDouble(dt.get(PROBATION_PERIOD_EXTENDED_TO)));
                }
                employee.setProbationPeriodExtendedTo(probationPeriodExtendedTo);

                employee.setPayType(getPayTypeEnum(dt.get(PAY_TYPE)));
                employee.setDisbursementMethod(getDibrushmentEnum(dt.get(DISBURSEMENT_METHOD))); //  BANK, CASH, MOBILE_BANKING

                String bankName = dt.get(BANK_NAME).equals("0") ? "" : dt.get(BANK_NAME);
                employee.setBankName(bankName);

                String bankAccountNumber = dt.get(BANK_ACCOUNT_NO).equals("0") ? "" : dt.get(BANK_ACCOUNT_NO);
                employee.setBankAccountNo(bankAccountNumber);

                employee.setMobileCelling((long) Double.parseDouble(dt.get(MOBILE_CELLING)));

                String bkashNumber = dt.get(BKASH_NUMBER).equals("0") ? "" : dt.get(BKASH_NUMBER);
                employee.setBkashNumber(bkashNumber);

                employee.setCardType(getCardEnum(dt.get(CARD_TYPE))); //DEBIT_CARD, CREDIT_CARD, PREPAID_CARD

                String cardNumber = dt.get(CARD_NUMBER).equals("0") ? "" : dt.get(CARD_NUMBER);
                employee.setCardNumber(cardNumber);

                String tinNumber = dt.get(TIN_NUMBER).equals("0") ? "" : dt.get(TIN_NUMBER);
                employee.setTinNumber(tinNumber);

                String passportNumber = dt.get(PASSPORT_NO).equals("0") ? "" : formatIDNumber(dt.get(PASSPORT_NO));
                employee.setPassportNo(passportNumber);

                String passportPlaceOfIssue = dt.get(PASSPORT_PLACE_OF_ISSUE).equals("0") ? "" : dt.get(PASSPORT_PLACE_OF_ISSUE);
                employee.setPassportPlaceOfIssue(passportPlaceOfIssue);

                LocalDate passportIssuedDate = null;
                if (!dt.get(PASSPORT_ISSUED_DATE).equals("0")) {
                    passportIssuedDate = doubleToDate(Double.parseDouble(dt.get(PASSPORT_ISSUED_DATE)));
                }
                employee.setPassportIssuedDate(passportIssuedDate);

                LocalDate passportExpiryDate = null;
                if (!dt.get(PASSPORT_ISSUED_DATE).equals("0")) {
                    passportExpiryDate = doubleToDate(Double.parseDouble(dt.get(PASSPORT_EXPIRY_DATE)));
                }
                employee.setPassportExpiryDate(passportExpiryDate);

                employee.setGender(getGenderEnum(dt.get(GENDER)));

                employee.setWelfareFundDeduction(Double.parseDouble(dt.get(WELFARE_FUND_DEDUCTION)));

                employee.setDesignation(getOrCreateDesignation(dt.get(DESIGNATION)));

                employee.setDepartment(getOrCreateDepartment(dt.get(DEPARTMENT)));

                //employee.setReportingTo(getReportingTo(PinUtil.formatPin(dt.get(REPORTING_TO))));

                employee.setNationality(getOrCreateNationality(dt.get(NATIONALITY)));

                employee.setBankBranch(getOrCreateBankBranch(dt.get(BANK_BRANCH)));

                employee.setBand(getOrCreateBand(dt.get(BAND)));

                reportingTo.put(employee.getPin(), PinUtil.formatPin(dt.get(REPORTING_TO)));
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
                eventLoggingPublisher.publishEvent(user, e, RequestMethod.POST, "Employee Master Import");
                publishEvent(e.getOfficialEmail(), e.getPin());
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

            if (!phoneNumber.substring(0, 4).equals("+880") && !phoneNumber.startsWith("0")) {
                phoneNumber = "+880" + phoneNumber;
            }
            if (phoneNumber.startsWith("0")) {
                phoneNumber = "+88" + phoneNumber;
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
