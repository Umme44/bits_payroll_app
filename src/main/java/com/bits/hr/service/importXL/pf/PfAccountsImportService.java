package com.bits.hr.service.importXL.pf;

import static com.bits.hr.service.importXL.pf.rowConstants.PfAccountRowConstants.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.enumeration.PfAccountStatus;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.importXL.GenericUploadServiceImpl;
import com.bits.hr.service.importXL.pf.helperMethods.PfHelperService;
import com.bits.hr.service.importXL.pf.helperMethods.dataConverter.StringToDate;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.PinUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Log4j2
public class PfAccountsImportService implements ImportService {

    @Autowired
    private PfAccountRepository pfAccountRepository;

    @Autowired
    private GenericUploadServiceImpl genericUploadService;

    @Autowired
    private GetConfigValueByKeyService getConfigValueByKeyService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PfHelperService pfHelperService;

    @Override
    public boolean importFile(MultipartFile file) {
        boolean isMultiplePfAccountSupported = getConfigValueByKeyService.isMultiplePfAccountSupported();
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);

            // removing header
            data.remove(0);
            for (ArrayList<String> dt : data) {
                // if empty row skip
                if (dt.get(PF_ACC_SL_NO).equals("0")) {
                    continue;
                }
                if (dt.isEmpty()) {
                    continue;
                }
                if (dt.get(PF_ACC_SL_NO).equals("")) {
                    continue;
                }
                // 0  -> SL/NO
                // 1  -> PF Code
                // 2  -> PIN
                // 3  -> Take Data From System according to pin
                // 4  -> Membership Start Date
                // 5  -> Membership End Date
                // 6  -> Date of Confirmation
                // 7  -> Date of Joining
                // 8  -> Account Status
                // 9  -> Account holder name
                // 10 -> Designation Name
                // 11 -> Department Name
                // 12 -> Unit Name
                // 13 -> OverWrite (If Exist)

                String pfCode = PinUtil.formatPin(dt.get(PF_ACC_PF_CODE));
                String pin = PinUtil.formatPin(dt.get(PF_ACC_PIN)).trim();

                Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin);
                Optional<PfAccount> pfAccountOptional = pfAccountRepository.getPfAccountByPin(pin);

                // If employee does not exist                             => continue
                // If Employee exists & take data from system == true     => Create New PF Account using Employee[pin]
                // If Employee exists & take data from system == false    => Create New PF Account Using the data provided in Excel

                // Save Block
                // If Existing Pf Account is not found                    => Save Newly created Pf Account
                // If Existing Pf Account is found & overwrite == true    => pfAccount.setId(existingPfAccount.getId())
                // If Existing Pf Account is found & overwrite == false   => continue

                if (!employeeOptional.isPresent()) {
                    continue;
                }

                if (dt.get(PF_ACC_TAKE_DATA_FROM_SYS).trim().equalsIgnoreCase("true")) {
                    if (pfAccountOptional.isPresent() & dt.get(PF_ACC_OVERWRITE_ON_EXIST).trim().equalsIgnoreCase("false")) {
                        continue;
                    } else {
                        PfAccount pfAccount = createPfAccount(pfAccountOptional, employeeOptional.get(), dt, true);
                        pfAccountRepository.save(pfAccount);
                    }
                }

                if (dt.get(PF_ACC_TAKE_DATA_FROM_SYS).trim().equalsIgnoreCase("false")) {
                    if (pfAccountOptional.isPresent() & dt.get(PF_ACC_OVERWRITE_ON_EXIST).trim().equalsIgnoreCase("false")) {
                        continue;
                    } else {
                        PfAccount pfAccount = createPfAccount(pfAccountOptional, employeeOptional.get(), dt, false);
                        pfAccountRepository.save(pfAccount);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    // Continue if Employee Pin exists.
    // If Duplicate Pf Account exists -> Then check if column "Overwrite" is true or false.
    // If Overwrite is "false" then continue.
    // If overwrite is "true" then update.
    // If no duplicate is found then create a new PF Account.
    public boolean importPreviousEmployeeFile(MultipartFile file) {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);

            // removing header
            data.remove(0);
            for (ArrayList<String> dataRow : data) {
                // if empty row skip
                if (dataRow.get(0).equals("0")) {
                    continue;
                }
                if (dataRow.isEmpty()) {
                    continue;
                }
                if (dataRow.get(0).equals("")) {
                    continue;
                }

                // S/L  PfCode   Pin    DOJ    DOC     MembershipStart   MembershipEnd    AccHolderName    Dept   Designation    Unit    Overwrite
                //  0     1       2      3      4             5                6                7            8         9          10         11

                // All 12 fields are required
                long totalColumn = 12;

                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).size() < totalColumn) {
                        throw new BadRequestAlertException(
                            "There should not be any blank cell in the excel, all the fields are required.",
                            "PfAccount",
                            "invalidData"
                        );
                    }
                    for (int j = 0; j < data.get(i).size(); j++) {
                        if (data.get(i).get(j).equals("0") || data.get(i).get(j).equals("")) {
                            throw new BadRequestAlertException(
                                "There should not be any blank cell in the excel, all the fields are required.",
                                "PfAccount",
                                "invalidData"
                            );
                        }
                    }
                }

                boolean isEmployeeExist = employeeRepository.isExistByPin(PinUtil.formatPin(dataRow.get(2).trim()));
                Optional<PfAccount> pfAccountOptional = pfAccountRepository.getPfAccountByPin(PinUtil.formatPin(dataRow.get(2).trim()));

                if (isEmployeeExist) {
                    continue;
                }

                if (pfAccountOptional.isPresent()) {
                    if (dataRow.get(11).trim().equalsIgnoreCase("true")) {
                        pfAccountOptional.get().setPfCode(PinUtil.formatPin(dataRow.get(1)));
                        pfAccountOptional.get().setPin(PinUtil.formatPin(dataRow.get(2)));
                        pfAccountOptional.get().setDateOfJoining(DateUtil.xlStringToDate(dataRow.get(3)));
                        pfAccountOptional.get().setDateOfConfirmation(DateUtil.xlStringToDate(dataRow.get(4)));
                        pfAccountOptional.get().setMembershipStartDate(DateUtil.xlStringToDate(dataRow.get(5)));
                        pfAccountOptional.get().setMembershipEndDate(DateUtil.xlStringToDate(dataRow.get(6)));
                        pfAccountOptional.get().setAccHolderName(dataRow.get(7).trim());

                        pfAccountOptional.get().setDepartmentName(setDefaultValueIfDataIsMissing(dataRow.get(8)));
                        pfAccountOptional.get().setDesignationName(setDefaultValueIfDataIsMissing(dataRow.get(9)));
                        pfAccountOptional.get().setUnitName(setDefaultValueIfDataIsMissing(dataRow.get(10)));

                        pfAccountOptional.get().setStatus(PfAccountStatus.CLOSED);

                        pfAccountRepository.save(pfAccountOptional.get());
                    }
                } else {
                    PfAccount pfAccount = new PfAccount();
                    pfAccount.setPfCode(PinUtil.formatPin(dataRow.get(1)));
                    pfAccount.setPin(PinUtil.formatPin(dataRow.get(2)));
                    pfAccount.setDateOfJoining(DateUtil.xlStringToDate(dataRow.get(3)));
                    pfAccount.setDateOfConfirmation(DateUtil.xlStringToDate(dataRow.get(4)));
                    pfAccount.setMembershipStartDate(DateUtil.xlStringToDate(dataRow.get(5).trim()));
                    pfAccount.setMembershipEndDate(DateUtil.xlStringToDate(dataRow.get(6)));
                    pfAccount.setAccHolderName(dataRow.get(7).trim());

                    pfAccount.setDepartmentName(setDefaultValueIfDataIsMissing(dataRow.get(8)));
                    pfAccount.setDesignationName(setDefaultValueIfDataIsMissing(dataRow.get(9)));
                    pfAccount.setUnitName(setDefaultValueIfDataIsMissing(dataRow.get(10)));

                    pfAccount.setStatus(PfAccountStatus.CLOSED);

                    pfAccountRepository.save(pfAccount);
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    private PfAccount createPfAccount(
        Optional<PfAccount> pfAccountOptional,
        Employee employee,
        List<String> dt,
        boolean takeDataFromSystem
    ) {
        if (takeDataFromSystem) {
            // If Pf Account is exists and take data fromm system == true
            if (pfAccountOptional.isPresent()) {
                PfAccount pfAccount = new PfAccount();
                pfAccount.setPin(employee.getPin());
                pfAccount.setPfCode(employee.getPin());
                pfAccount.setMembershipStartDate(employee.getDateOfConfirmation());
                pfAccount.setDateOfJoining(employee.getDateOfJoining());
                pfAccount.setDateOfConfirmation(employee.getDateOfConfirmation());
                pfAccount.setStatus(pfAccountOptional.get().getStatus());
                pfAccount.setDepartmentName(employee.getDepartment().getDepartmentName());
                pfAccount.setDesignationName(employee.getDesignation().getDesignationName());
                pfAccount.setUnitName(employee.getUnit().getUnitName());
                pfAccount.setAccHolderName(employee.getFullName());

                // Set ID of existing PF Account to newly created PF Account.
                pfAccount.setId(pfAccountOptional.get().getId());

                return pfAccount;
            }
            // If Pf Account does not exist and take data fromm system == true
            else {
                PfAccount pfAccount = new PfAccount();
                pfAccount.setPin(employee.getPin());
                pfAccount.setPfCode(employee.getPin());
                pfAccount.setMembershipStartDate(employee.getDateOfConfirmation());
                pfAccount.setDateOfJoining(employee.getDateOfJoining());
                pfAccount.setDateOfConfirmation(employee.getDateOfConfirmation());
                pfAccount.setStatus(PfAccountStatus.ACTIVE);
                pfAccount.setDepartmentName(employee.getDepartment().getDepartmentName());
                pfAccount.setDesignationName(employee.getDesignation().getDesignationName());
                pfAccount.setUnitName(employee.getUnit().getUnitName());
                pfAccount.setAccHolderName(employee.getFullName());

                return pfAccount;
            }
        } else {
            if (pfAccountOptional.isPresent()) {
                PfAccount pfAccount = new PfAccount();
                pfAccount.setPin(PinUtil.formatPin(dt.get(PF_ACC_PIN)));
                pfAccount.setPfCode(PinUtil.formatPin(dt.get(PF_ACC_PIN)));
                pfAccount.setMembershipStartDate(DateUtil.xlStringToDate(dt.get(PF_ACC_MEMBERSHIP_START_DATE)));
                pfAccount.setDateOfJoining(DateUtil.xlStringToDate(dt.get(PF_ACC_DATE_OF_JOINING)));
                pfAccount.setDateOfConfirmation(DateUtil.xlStringToDate(dt.get(PF_ACC_DATE_OF_CONFIRMATION)));
                pfAccount.setStatus(PfAccountStatus.ACTIVE);
                pfAccount.setDepartmentName(dt.get(PF_ACC_DEPARTMENT_NAME).trim());
                pfAccount.setDesignationName(dt.get(PF_ACC_DESIGNATION_NAME).trim());
                pfAccount.setUnitName(dt.get(PF_ACC_UNIT_NAME).trim());
                pfAccount.setAccHolderName(dt.get(PF_ACC_ACCOUNT_HOLDER_NAME).trim());

                pfAccount.setId(pfAccountOptional.get().getId());

                return pfAccount;
            } else {
                PfAccount pfAccount = new PfAccount();
                pfAccount.setPin(PinUtil.formatPin(dt.get(PF_ACC_PIN)));
                pfAccount.setPfCode(PinUtil.formatPin(dt.get(PF_ACC_PIN)));
                pfAccount.setMembershipStartDate(DateUtil.xlStringToDate(dt.get(PF_ACC_MEMBERSHIP_START_DATE)));
                pfAccount.setDateOfJoining(DateUtil.xlStringToDate(dt.get(PF_ACC_DATE_OF_JOINING)));
                pfAccount.setDateOfConfirmation(DateUtil.xlStringToDate(dt.get(PF_ACC_DATE_OF_CONFIRMATION)));
                pfAccount.setStatus(PfAccountStatus.ACTIVE);
                pfAccount.setDepartmentName(dt.get(PF_ACC_DEPARTMENT_NAME).trim());
                pfAccount.setDesignationName(dt.get(PF_ACC_DESIGNATION_NAME).trim());
                pfAccount.setUnitName(dt.get(PF_ACC_UNIT_NAME).trim());
                pfAccount.setAccHolderName(dt.get(PF_ACC_ACCOUNT_HOLDER_NAME).trim());

                return pfAccount;
            }
        }
    }

    public PfAccount refreshExistingAccountAndSave(PfAccount pfAccount) {
        EmployeeDTO employeeDTO = employeeMapper.toDto(employeeRepository.findEmployeeByPin(pfAccount.getPin()).get());
        pfAccount.setStatus(PfAccountStatus.ACTIVE);
        pfAccount.setMembershipStartDate(employeeDTO.getDateOfConfirmation());
        pfAccount.setAccHolderName(employeeDTO.getFullName());
        pfAccount.setDesignationName(employeeDTO.getDesignationName());
        pfAccount.setDepartmentName(employeeDTO.getDepartmentName());
        pfAccount.setUnitName(employeeDTO.getUnitName());
        pfAccount.setDateOfJoining(employeeDTO.getDateOfJoining());
        pfAccount.setDateOfConfirmation(employeeDTO.getDateOfConfirmation());
        return pfAccountRepository.save(pfAccount);
    }

    public PfAccount saveOrOverwriteFromGivenData(ArrayList<String> dt) {
        String pfCode = PinUtil.formatPin(dt.get(PF_ACC_PF_CODE));
        String pin = PinUtil.formatPin(dt.get(PF_ACC_PIN));
        LocalDate membershipStartDate = StringToDate.convert(dt.get(PF_ACC_MEMBERSHIP_START_DATE));
        LocalDate membershipEndDate = StringToDate.convert(dt.get(PF_ACC_MEMBERSHIP_END_DATE));
        LocalDate dateOfJoining = StringToDate.convert(dt.get(PF_ACC_DATE_OF_JOINING));
        LocalDate dateOfConfirmation = StringToDate.convert(dt.get(PF_ACC_DATE_OF_CONFIRMATION));
        PfAccountStatus accountStatus = PfAccountStatus.valueOf(dt.get(PF_ACC_ACCOUNT_STATUS));
        String accountHolderName = dt.get(PF_ACC_ACCOUNT_HOLDER_NAME);
        String designationName = dt.get(PF_ACC_DESIGNATION_NAME);
        String departmentName = dt.get(PF_ACC_DEPARTMENT_NAME);
        String unitName = dt.get(PF_ACC_UNIT_NAME);

        PfAccount pfAccount = new PfAccount();
        Optional<PfAccount> pfAccountOptional = pfAccountRepository.getPfAccountByPinAndPfCode(pin, pfCode);
        if (pfAccountOptional.isPresent()) {
            pfAccount = pfAccountOptional.get();
        }
        pfAccount.setPfCode(pfCode);
        pfAccount.setPin(pin);
        pfAccount.setMembershipStartDate(membershipStartDate);
        pfAccount.setMembershipEndDate(membershipEndDate);
        pfAccount.setStatus(accountStatus);
        pfAccount.setAccHolderName(accountHolderName);
        pfAccount.setDesignationName(designationName);
        pfAccount.setDepartmentName(departmentName);
        pfAccount.setUnitName(unitName);
        pfAccount.setDateOfJoining(dateOfJoining);
        pfAccount.setDateOfConfirmation(dateOfConfirmation);
        return pfAccountRepository.save(pfAccount);
    }

    private String setDefaultValueIfDataIsMissing(String data) {
        if (data.equals("N/A") || data.equals("-") || data.equals("n/a")) {
            return "-";
        } else {
            return data;
        }
    }
}
