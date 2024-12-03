package com.bits.hr.service.importXL.nominee;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.IdentityType;
import com.bits.hr.domain.enumeration.NomineeType;
import com.bits.hr.domain.enumeration.PfAccountStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.NomineeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfNomineeRepository;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.fileService.NomineeFileService;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.service.importXL.XLImportCommonService;
import com.bits.hr.util.CalculateAgeUtil;
import com.bits.hr.util.PinUtil;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class NomineeImportService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private NomineeRepository nomineeRepository;

    @Autowired
    private PfAccountRepository pfAccountRepository;

    @Autowired
    private PfNomineeRepository pfNomineeRepository;

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private NomineeFileService nomineeFileService;

    private static final int PIN_COL_NO = 1;
    private static final int EMP_NAME_COL_NO = 2;
    private static final int NOMINATION_TYPE_COL_NO = 3;
    private static final int NOMINEE_NAME_COL_NO = 4;
    private static final int NOMINEE_RELATIONSHIP_COL_NO = 5;
    private static final int NOMINEE_PRESENT_ADDRESS_COL_NO = 6;
    private static final int NOMINEE_PERMANENT_ADDRESS_COL_NO = 7;
    private static final int NOMINEE_DOB_COL_NO = 8;
    private static final int NOMINEE_SHARE_PERCENTAGE_COL_NO = 9;
    private static final int NOMINEE_IDENTITY_TYPE_COL_NO = 10;
    private static final int NOMINEE_DOCUMENT_NAME_COL_NO = 11;
    private static final int NOMINEE_IDENTITY_ID_NUMBER_COL_NO = 12;
    private static final int NOMINEE_IMAGE_FILE_NAME = 13;
    private static final int NOMINEE_NID_FILE_NAME = 14;
    private static final int NOMINEE_IS_MINOR_COL_NO = 15;
    private static final int GUARDIAN_NAME_COL_NO = 16;
    private static final int GUARDIAN_FATHER_NAME_COL_NO = 17;
    private static final int GUARDIAN_SPOUSE_NAME_COL_NO = 18;
    private static final int GUARDIAN_DOB_COL_NO = 19;
    private static final int GUARDIAN_PRESENT_ADDRESS_COL_NO = 20;
    private static final int GUARDIAN_PERMANENT_ADDRESS_COL_NO = 21;
    private static final int GUARDIAN_IDENTITY_TYPE_COL_NO = 22;
    private static final int GUARDIAN_DOCUMENT_NAME_COL_NO = 23;
    private static final int GUARDIAN_ID_NUMBER_COL_NO = 24;
    private static final int GUARDIAN_RELATIONSHIP_COL_NO = 25;

    @Transactional
    public boolean importFile(MultipartFile file) {
        int count = 0;
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header = data.remove(0);

            for (List<String> dataItems : data) {
                if (!XLImportCommonService.isXLRowValid(dataItems)) {
                    continue;
                }

                //  0      1       2                  3             4                5                            6                        7               8
                //  S\L   PIN	 Employee Name  Nomination Type  Nominee Name    Relationship with Employee	   Present Address	  Permanent Address	   Date of Birth

                //     9                  10                11             12                  13               14             15
                //   Share Percentage  Identity Type  Document Name  Identity ID Number   Image File Name   NID File Name    isMinor?

                //   16       17              18                19                20              21                  22               23               24                 25
                //  Name   Father Name     Spouse Name    Date of Birth    Present Address    Permanent Address    Identity Type   Document Name    Id Number  Relationship with nominee

                String nomineeType = dataItems.get(NOMINATION_TYPE_COL_NO).trim();
                if (nomineeType.equalsIgnoreCase("GF") || nomineeType.equalsIgnoreCase("GENERAL")) {
                    Optional<Employee> employee = getEmployeeUsingPin(dataItems.get(PIN_COL_NO));
                    if (!employee.isPresent()) {
                        continue;
                    } else {
                        // delete nominee by employee id and nominee type
                        NomineeType nomineeTypeEnum;
                        if (nomineeType.equals("GF")) {
                            nomineeTypeEnum = NomineeType.GRATUITY_FUND;
                        } else {
                            nomineeTypeEnum = NomineeType.GENERAL;
                        }

                        /* delete previous data */
                        List<Nominee> nomineeList = nomineeRepository.getNomineesByEmployeeIdAndNomineeType(
                            employee.get().getId(),
                            nomineeTypeEnum
                        );
                        for (Nominee savedNominee : nomineeList) {
                            if (savedNominee.getImagePath() != null) {
                                fileOperationService.delete(savedNominee.getImagePath());
                            }
                        }
                        nomineeRepository.deleteAllByEmployeeIdAndNomineeType(employee.get().getId(), nomineeTypeEnum);

                        Nominee nominee = new Nominee();
                        nominee.setEmployee(employee.get());
                        nominee.setNomineeName(dataItems.get(NOMINEE_NAME_COL_NO).trim());
                        nominee.setRelationshipWithEmployee(dataItems.get(NOMINEE_RELATIONSHIP_COL_NO));

                        if (nomineeType.equalsIgnoreCase("GF")) {
                            nominee.setNomineeType(NomineeType.GRATUITY_FUND);
                        } else {
                            nominee.setNomineeType(NomineeType.GENERAL);
                        }

                        nominee.setPresentAddress(dataItems.get(NOMINEE_PRESENT_ADDRESS_COL_NO));
                        nominee.setPermanentAddress(dataItems.get(NOMINEE_PERMANENT_ADDRESS_COL_NO));

                        LocalDate dob = doubleToDate(Double.parseDouble(dataItems.get(NOMINEE_DOB_COL_NO)));
                        nominee.setDateOfBirth(dob);

                        // Age calculation and set
                        int ageInYears = CalculateAgeUtil.calculateAgeInYearByDate(dob);
                        nominee.setAge(ageInYears);

                        nominee.setSharePercentage(Double.valueOf(dataItems.get(NOMINEE_SHARE_PERCENTAGE_COL_NO)));

                        IdentityType nomineeIdentityType = getIdentityType(dataItems.get(NOMINEE_IDENTITY_TYPE_COL_NO));
                        nominee.setIdentityType(nomineeIdentityType);
                        nominee.setIdNumber(dataItems.get(NOMINEE_IDENTITY_ID_NUMBER_COL_NO).trim());

                        if (nomineeIdentityType == IdentityType.OTHER) {
                            nominee.setDocumentName(dataItems.get(NOMINEE_DOCUMENT_NAME_COL_NO).trim());
                        }

                        // image file name save in path. In next nominee image migration, replace the path with exact file after saving the images
                        nominee.setImagePath(dataItems.get(NOMINEE_IMAGE_FILE_NAME).trim());
                        count++;
                        boolean isMinor = Boolean.parseBoolean(dataItems.get(NOMINEE_IS_MINOR_COL_NO));
                        if (isMinor) {
                            nominee.setGuardianName(dataItems.get(GUARDIAN_NAME_COL_NO));
                            nominee.setGuardianFatherName(dataItems.get(GUARDIAN_FATHER_NAME_COL_NO));
                            nominee.setGuardianSpouseName(dataItems.get(GUARDIAN_SPOUSE_NAME_COL_NO));

                            LocalDate dobGuardian = doubleToDate(Double.parseDouble(dataItems.get(GUARDIAN_DOB_COL_NO)));
                            nominee.setGuardianDateOfBirth(dobGuardian);

                            // Age calculation and set
                            int guardianAgeInYears = CalculateAgeUtil.calculateAgeInYearByDate(dob);
                            nominee.setAge(guardianAgeInYears);

                            nominee.setGuardianPresentAddress(dataItems.get(GUARDIAN_PRESENT_ADDRESS_COL_NO));
                            nominee.setGuardianPermanentAddress(dataItems.get(GUARDIAN_PERMANENT_ADDRESS_COL_NO));

                            IdentityType guardianIdentityType = getIdentityType(dataItems.get(GUARDIAN_IDENTITY_TYPE_COL_NO));
                            nominee.setGuardianIdentityType(guardianIdentityType);
                            if (guardianIdentityType == IdentityType.OTHER) {
                                nominee.setGuardianDocumentName(dataItems.get(GUARDIAN_DOCUMENT_NAME_COL_NO));
                            }
                            nominee.setGuardianIdNumber(dataItems.get(GUARDIAN_ID_NUMBER_COL_NO));
                            nominee.setGuardianRelationshipWith(dataItems.get(GUARDIAN_RELATIONSHIP_COL_NO));
                        }

                        // duplication check
                        Optional<Nominee> nomineeOptional = nomineeRepository.findDuplicate(
                            nominee.getEmployee(),
                            nominee.getNomineeName(),
                            nominee.getNomineeType(),
                            nominee.getIdNumber()
                        );
                        if (nomineeOptional.isPresent()) {
                            nominee.setId(nomineeOptional.get().getId());
                        }
                        nomineeRepository.save(nominee);
                        //nomineeOptional.orElseGet(() -> nomineeRepository.save(nominee));

                    }
                } else if (nomineeType.equalsIgnoreCase("PF")) {
                    // find pf account using pin, if not exist then create one for RCE or continue
                    PfAccount pfAccount = getPFAccountUsingPin(dataItems.get(PIN_COL_NO).trim());
                    if (pfAccount.getPin() == null) {
                        continue;
                    }

                    /* delete previous data */
                    List<PfNominee> pfNomineeList = pfNomineeRepository.findAllByPfAccountId(pfAccount.getId());
                    for (PfNominee savedNominee : pfNomineeList) {
                        if (savedNominee.getPhoto() != null) {
                            fileOperationService.delete(savedNominee.getPhoto());
                        }
                    }
                    // delete all pf nominee by pfAccount
                    pfNomineeRepository.deleteAllByPfAccountId(pfAccount.getId());

                    PfNominee pfNominee = new PfNominee();
                    pfNominee.setPfAccount(pfAccount);

                    pfNominee.setFullName(dataItems.get(NOMINEE_NAME_COL_NO).trim());
                    pfNominee.setRelationship(dataItems.get(NOMINEE_RELATIONSHIP_COL_NO));

                    pfNominee.setPresentAddress(dataItems.get(NOMINEE_PRESENT_ADDRESS_COL_NO));
                    pfNominee.setPermanentAddress(dataItems.get(NOMINEE_PERMANENT_ADDRESS_COL_NO));

                    LocalDate dob = doubleToDate(Double.parseDouble(dataItems.get(NOMINEE_DOB_COL_NO)));
                    pfNominee.setDateOfBirth(dob);

                    // Age calculation and set
                    int nomineeAgeInYears = CalculateAgeUtil.calculateAgeInYearByDate(dob);
                    pfNominee.setAge(nomineeAgeInYears);

                    pfNominee.setSharePercentage(Double.valueOf(dataItems.get(NOMINEE_SHARE_PERCENTAGE_COL_NO)));

                    IdentityType nomineeIdentityType = getIdentityType(dataItems.get(NOMINEE_IDENTITY_TYPE_COL_NO));
                    pfNominee.setIdentityType(nomineeIdentityType);
                    pfNominee.setIdNumber(dataItems.get(NOMINEE_IDENTITY_ID_NUMBER_COL_NO));

                    if (nomineeIdentityType == IdentityType.OTHER) {
                        pfNominee.setDocumentName(dataItems.get(NOMINEE_DOCUMENT_NAME_COL_NO));
                    }

                    // image file name save in path. In next nominee image migration, replace the path with exact file after saving the images
                    pfNominee.setPhoto(dataItems.get(NOMINEE_IMAGE_FILE_NAME).trim());

                    boolean isMinor = Boolean.parseBoolean(dataItems.get(NOMINEE_IS_MINOR_COL_NO));

                    if (isMinor) {
                        pfNominee.setGuardianName(dataItems.get(GUARDIAN_NAME_COL_NO));
                        pfNominee.setGuardianFatherOrSpouseName(dataItems.get(GUARDIAN_FATHER_NAME_COL_NO));

                        LocalDate dobGuardian = doubleToDate(Double.parseDouble(dataItems.get(GUARDIAN_DOB_COL_NO)));
                        pfNominee.setGuardianDateOfBirth(dobGuardian);

                        // Age calculation and set
                        int guardianAgeInYears = CalculateAgeUtil.calculateAgeInYearByDate(dob);
                        pfNominee.setAge(guardianAgeInYears);

                        pfNominee.setGuardianPresentAddress(dataItems.get(GUARDIAN_PRESENT_ADDRESS_COL_NO));
                        pfNominee.setGuardianPermanentAddress(dataItems.get(GUARDIAN_PERMANENT_ADDRESS_COL_NO));

                        IdentityType guardianIdentityType = getIdentityType(dataItems.get(GUARDIAN_IDENTITY_TYPE_COL_NO));
                        pfNominee.setGuardianIdentityType(guardianIdentityType);
                        if (guardianIdentityType == IdentityType.OTHER) {
                            pfNominee.setGuardianDocumentName(dataItems.get(GUARDIAN_DOCUMENT_NAME_COL_NO));
                        }
                        pfNominee.setGuardianIdNumber(dataItems.get(GUARDIAN_ID_NUMBER_COL_NO));
                        pfNominee.setGuardianRelationshipWithNominee(dataItems.get(GUARDIAN_RELATIONSHIP_COL_NO));
                    }

                    // duplication check
                    Optional<PfNominee> pfNomineeOptional = pfNomineeRepository.findDuplicate(pfAccount, pfNominee.getFullName());
                    if (pfNomineeOptional.isPresent()) {
                        pfNominee.setId(pfNomineeOptional.get().getId());
                    }
                    pfNomineeRepository.save(pfNominee);
                }
            }
            return true;
        } catch (Exception e) {
            log.error(count);
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Employee> getEmployeeUsingPin(String pin) {
        pin = PinUtil.formatPin(pin);
        return employeeRepository.findEmployeeByPin(pin);
    }

    public PfAccount getPFAccountUsingPin(String pin) {
        pin = PinUtil.formatPin(pin);
        List<PfAccount> pfAccountList = pfAccountRepository.getPfAccountsByPin(pin);
        if (pfAccountList.size() > 0) {
            // return 1st one
            return pfAccountList.get(0);
        } else {
            Optional<Employee> employee = getEmployeeUsingPin(pin);
            if (employee.isPresent() && employee.get().getEmployeeCategory() == EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
                Employee employee1 = employee.get();
                // create a pf account
                PfAccount pfAccount = new PfAccount();
                pfAccount
                    .pfCode(employee1.getPin())
                    .pin(pin)
                    .status(PfAccountStatus.ACTIVE)
                    .accHolderName(employee1.getFullName())
                    .departmentName(employee1.getDepartment().getDepartmentName())
                    .designationName(employee1.getDesignation().getDesignationName())
                    .unitName(employee1.getUnit().getUnitName())
                    .dateOfJoining(employee1.getDateOfJoining())
                    .dateOfConfirmation(employee1.getDateOfConfirmation())
                    .membershipStartDate(employee1.getDateOfConfirmation());
                PfAccount savedPfAccount = pfAccountRepository.save(pfAccount);
                return savedPfAccount;
            } else {
                return new PfAccount();
            }
        }
    }

    public IdentityType getIdentityType(String identityType) {
        if (identityType.equalsIgnoreCase("NID") || identityType.equalsIgnoreCase("NATIONAL ID")) {
            return IdentityType.NID;
        } else if (identityType.equalsIgnoreCase("PASSPORT")) {
            return IdentityType.PASSPORT;
        } else {
            return IdentityType.OTHER;
        }
    }

    private LocalDate doubleToDate(Double d) {
        Date javaDate = DateUtil.getJavaDate(d);
        LocalDate date = javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date;
    }

    @Transactional
    public boolean importNomineeImage(MultipartFile[] files) {
        try {
            for (MultipartFile multipartFile : files) {
                if (multipartFile.isEmpty()) continue;

                if (multipartFile.getContentType() == null) {
                    continue;
                } else if (!multipartFile.getContentType().contains("image/")) {
                    continue;
                }

                if (multipartFile.getOriginalFilename() == null || multipartFile.getOriginalFilename().trim().isEmpty()) {
                    continue;
                }

                if (multipartFile.getOriginalFilename().trim().contains("pf")) {
                    List<PfNominee> pfNomineeList = pfNomineeRepository.findByPhoto(multipartFile.getOriginalFilename());
                    if (pfNomineeList.isEmpty()) continue;

                    PfNominee pfNominee = pfNomineeList.get(0);

                    // save file
                    File savedFile = nomineeFileService.save(multipartFile);

                    pfNominee.setPhoto(savedFile.getAbsolutePath());
                    pfNomineeRepository.save(pfNominee);
                } else {
                    List<Nominee> nomineeList = nomineeRepository.findByPhoto(multipartFile.getOriginalFilename());

                    if (nomineeList.isEmpty()) continue;

                    Nominee nominee = nomineeList.get(0);

                    // save file
                    File savedFile = nomineeFileService.save(multipartFile);

                    nominee.setImagePath(savedFile.getAbsolutePath());
                    nomineeRepository.save(nominee);
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
