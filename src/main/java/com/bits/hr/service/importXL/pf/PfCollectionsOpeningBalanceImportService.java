package com.bits.hr.service.importXL.pf;

import static com.bits.hr.service.importXL.pf.rowConstants.PfCollectionOpeningBalanceRowConstants.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfCollection;
import com.bits.hr.domain.enumeration.PfCollectionType;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.service.importXL.pf.helperMethods.PfHelperService;
import com.bits.hr.util.PinUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Log4j2
public class PfCollectionsOpeningBalanceImportService implements ImportService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private GetConfigValueByKeyService getConfigValueByKeyService;

    @Autowired
    private PfAccountRepository pfAccountRepository;

    @Autowired
    private PfHelperService pfHelperService;

    @Autowired
    private PfCollectionRepository pfCollectionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public boolean importFile(MultipartFile file) {
        try {
            log.info("================== importing opening pf collections ===================");
            List<ArrayList<String>> data = genericUploadService.upload(file);

            // removing header
            data.remove(0);
            for (ArrayList<String> dataItems : data) {
                log.info("================== data:: row :: pf opening collection ===================");
                // 0 -> SL/NO
                // 1 -> Overwrite On exist
                // 2 -> PIN
                // 3 -> Pf Code
                // 4 -> Collection Type
                // 5 -> Year
                // 6 -> Month
                // 7 -> Transaction Date
                // 8 -> Employee Contribution
                // 9 -> Employee Interest
                // 10 -> Employer contribution
                // 11 -> Employer Interest
                // if empty row skip

                if (dataItems.isEmpty()) {
                    continue;
                }
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                if (dataItems.get(0).equals("")) {
                    continue;
                }

                String pfCode = PinUtil.formatPin(dataItems.get(PF_COB_PF_CODE));
                String pin = PinUtil.formatPin(dataItems.get(PF_COB_PIN));

                PfAccount pfAccount = new PfAccount();
                Optional<PfAccount> pfAccountOptional = pfAccountRepository.getPfAccountByPinAndPfCode(pin, pfCode);
                boolean isPfAccExist = pfAccountOptional.isPresent();
                if (isPfAccExist) {
                    pfAccount = pfAccountOptional.get();
                }
                // if pf account not exist AND isMultiplePfAccountSupported == false
                // ==> create new pf account
                // if pf account not exist AND isMultiplePfAccountSupported == true
                // ==> continue and log no pf account found
                if (isPfAccExist == false) {
                    // create new pf account
                    if (pfHelperService.crateNewPfAccountFromSysDataAndSave(pfCode, pin).isPresent()) {
                        pfAccount = pfHelperService.crateNewPfAccountFromSysDataAndSave(pfCode, pin).get();
                    } else {
                        log.error(
                            "Employee does not exist for the following pin: " +
                            pin +
                            ", pfCode: " +
                            pfCode +
                            ", you might need to create PF Accounts separately."
                        );
                        continue;
                    }
                }

                PfCollectionType pfCollectionType = PfCollectionType.OPENING_BALANCE;
                LocalDate transactionDate = LocalDate.now();
                double employeeContribution = Double.parseDouble(dataItems.get(PF_COB_EMPLOYEE_CONTRIBUTION));
                double employeeInterest = Double.parseDouble(dataItems.get(PF_COB_EMPLOYEE_INTEREST));
                double employerContribution = Double.parseDouble(dataItems.get(PF_COB_EMPLOYER_CONTRIBUTION));
                double employerInterest = Double.parseDouble(dataItems.get(PF_COB_EMPLOYER_INTEREST));

                Optional<PfCollection> pfCollectionOptional = pfCollectionRepository.getOpeningPfBalance(pfCode, pin);
                boolean isPfCollectionExist = pfCollectionOptional.isPresent();
                boolean overwriteIfExist = dataItems.get(PF_COB_OVERWRITE_ON_EXIST).trim().equalsIgnoreCase("true");
                // if pf collection exist and overwrite = false => continue
                // if pf collection exist = true and overWriteIfExist = true ==> do overwrite
                // if pf collection not exist ==> save new

                if (isPfCollectionExist == true && !overwriteIfExist) {
                    log.debug("pf opening balance available for pfcode: " + pfCode + " pin:" + pin + "and asked not to overwrite");
                    continue;
                }
                if (isPfCollectionExist == true && overwriteIfExist) {
                    PfCollection pfCollection = pfCollectionOptional.get();
                    pfCollection.setPfAccount(pfAccount);
                    pfCollection.setEmployeeContribution(employeeContribution);
                    pfCollection.setEmployeeInterest(employeeInterest);
                    pfCollection.setEmployerContribution(employerContribution);
                    pfCollection.setEmployerInterest(employerInterest);
                    pfCollection.setCollectionType(pfCollectionType);
                    pfCollection.setTransactionDate(transactionDate);
                    pfCollectionRepository.save(pfCollection);
                    continue;
                }
                if (!isPfCollectionExist) {
                    PfCollection pfCollection = new PfCollection();
                    pfCollection.setPfAccount(pfAccount);
                    pfCollection.setEmployeeContribution(employeeContribution);
                    pfCollection.setEmployeeInterest(employeeInterest);
                    pfCollection.setEmployerContribution(employerContribution);
                    pfCollection.setEmployerInterest(employerInterest);
                    pfCollection.setCollectionType(pfCollectionType);
                    pfCollection.setTransactionDate(transactionDate);
                    pfCollectionRepository.save(pfCollection);
                    continue;
                }
            }
            return true;
        } catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    // 1. If Pin exists on the database, then continue.
    // 2. If Pf account does not exist, then continue.
    // 3. If Pf account does exist, then check if there are any duplicate Pf Collection (pf code, month, year).
    // 4. If duplicate found, then check if "Override on existing" == true ??.
    // 4. If true then Replace updated data, otherwise continue.
    public boolean importPreviousEmployeeFile(MultipartFile file) {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);

            // removing header
            data.remove(0);

            for (ArrayList<String> dataItems : data) {
                if (dataItems.isEmpty()) {
                    continue;
                }
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                if (dataItems.get(0).equals("")) {
                    continue;
                }

                // S/L  Overwrite   Pin  pfCode   EmployeeContribution    employeeInterest  EmployerContribution    employerInterest
                //  0       1        2      3             4                       5                  6                      7

                boolean isEmployeeExist = employeeRepository.isExistByPin(PinUtil.formatPin(dataItems.get(2).trim()));
                Optional<PfAccount> pfAccountOptional = pfAccountRepository.getPfAccountByPin(PinUtil.formatPin(dataItems.get(2)));

                if (isEmployeeExist) {
                    continue;
                }
                if (!pfAccountOptional.isPresent()) {
                    continue;
                }

                Optional<PfCollection> pfCollectionOptional = pfCollectionRepository.getOpeningPfBalance(
                    pfAccountOptional.get().getPfCode(),
                    pfAccountOptional.get().getPin()
                );

                if (pfCollectionOptional.isPresent()) {
                    if (dataItems.get(1).trim().equalsIgnoreCase("TRUE")) {
                        pfCollectionOptional.get().setEmployeeContribution(Double.parseDouble(dataItems.get(4).trim()));
                        pfCollectionOptional.get().setEmployeeInterest(Double.parseDouble(dataItems.get(5).trim()));

                        pfCollectionOptional.get().setEmployerContribution(Double.parseDouble(dataItems.get(6).trim()));
                        pfCollectionOptional.get().setEmployerInterest(Double.parseDouble(dataItems.get(7).trim()));

                        pfCollectionRepository.save(pfCollectionOptional.get());
                    }
                } else {
                    PfCollection pfCollection = new PfCollection();

                    pfCollection.setPfAccount(pfAccountOptional.get());

                    pfCollection.setCollectionType(PfCollectionType.OPENING_BALANCE);

                    pfCollection.setEmployeeContribution(Double.parseDouble(dataItems.get(4).trim()));
                    pfCollection.setEmployeeInterest(Double.parseDouble(dataItems.get(5).trim()));

                    pfCollection.setEmployerContribution(Double.parseDouble(dataItems.get(6).trim()));
                    pfCollection.setEmployerInterest(Double.parseDouble(dataItems.get(7).trim()));

                    pfCollectionRepository.save(pfCollection);
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }
}
