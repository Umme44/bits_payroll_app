package com.bits.hr.service.importXL.pf;

import static com.bits.hr.service.importXL.pf.rowConstants.PfCollectionMonthlyCollectionRowConstants.*;

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
import com.bits.hr.util.MathRoundUtil;
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
public class PfCollectionsMonthlyCollectionsImportService implements ImportService {

    @Autowired
    private GenericUploadService genericUploadService;

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
            log.info("================== importing monthly pf collections ===================");
            List<ArrayList<String>> data = genericUploadService.upload(file);

            // removing header
            data.remove(0);
            for (ArrayList<String> dataItems : data) {
                log.info("================== data:: row :: pf monthly collection ===================");
                // 0 -> SL/NO
                // 1 -> Overwrite On exist
                // 2 -> PIN
                // 3 -> Pf Code
                // 4 -> Year
                // 5 -> Month
                // 6 -> Gross
                // 7 -> Basic
                // 8 -> Employee Contribution
                // 9 -> Employer contribution

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

                String pfCode = PinUtil.formatPin(dataItems.get(PF_CMC_PF_CODE));
                String pin = PinUtil.formatPin(dataItems.get(PF_CMC_PIN));

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

                int month = (int) Double.parseDouble(dataItems.get(PF_CMC_COLLECTION_MONTH));
                int year = (int) Double.parseDouble(dataItems.get(PF_CMC_COLLECTION_YEAR));

                double gross = Double.parseDouble(dataItems.get(PF_CMC_COLLECTION_GROSS));
                double basic = Double.parseDouble(dataItems.get(PF_CMC_COLLECTION_BASIC));

                PfCollectionType pfCollectionType = PfCollectionType.MONTHLY;
                LocalDate transactionDate = LocalDate.of(year, month, 25);

                double employeeContribution = Double.parseDouble(dataItems.get(PF_CMC_EMPLOYEE_CONTRIBUTION));
                double employerContribution = Double.parseDouble(dataItems.get(PF_CMC_EMPLOYER_CONTRIBUTION));

                Optional<PfCollection> pfCollectionOptional = pfCollectionRepository.getPfCollection(pfCode, year, month, pfCollectionType);
                boolean isPfCollectionExist = pfCollectionOptional.isPresent();
                boolean overwriteIfExist = dataItems.get(PF_CMC_OVERWRITE_ON_EXIST).trim().equalsIgnoreCase("true");
                // if pf collection exist and overwrite = false => continue
                // if pf collection exist = true and overWriteIfExist = true ==> do overwrite
                // if pf collection not exist ==> save new

                if (isPfCollectionExist == true && overwriteIfExist == false) {
                    log.debug(
                        "pf collection available for pfcode: " +
                        pfCode +
                        " pin:" +
                        pin +
                        " year: " +
                        year +
                        "Month: " +
                        month +
                        "and asked not to overwrite"
                    );
                    continue;
                }
                if (isPfCollectionExist == true && overwriteIfExist == true) {
                    PfCollection pfCollection = pfCollectionOptional.get();
                    pfCollection.setYear(year);
                    pfCollection.setMonth(month);
                    pfCollection.setPfAccount(pfAccount);
                    pfCollection.setGross(gross);
                    pfCollection.setBasic(basic);
                    pfCollection.setEmployeeContribution(employeeContribution);
                    pfCollection.setEmployerContribution(employerContribution);
                    pfCollection.setCollectionType(pfCollectionType);
                    pfCollection.setTransactionDate(transactionDate);
                    pfCollectionRepository.save(pfCollection);
                    continue;
                }
                if (isPfCollectionExist == false) {
                    PfCollection pfCollection = new PfCollection();
                    pfCollection.setYear(year);
                    pfCollection.setMonth(month);
                    pfCollection.setPfAccount(pfAccount);
                    pfCollection.setGross(gross);
                    pfCollection.setBasic(basic);
                    pfCollection.setEmployeeContribution(employeeContribution);
                    pfCollection.setEmployeeInterest(0.0d);
                    pfCollection.setEmployerContribution(employerContribution);
                    pfCollection.setEmployerInterest(0.0d);
                    pfCollection.setCollectionType(pfCollectionType);
                    pfCollection.setTransactionDate(transactionDate);
                    pfCollectionRepository.save(pfCollection);
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
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

                // S/L  Overwrite   Pin  pfCode   year    month   gross    basic   employeeContribution employerContribution
                //  0       1        2      3       4       5       6        7              8                    9

                boolean isEmployeeExist = employeeRepository.isExistByPin(PinUtil.formatPin(dataItems.get(2).trim()));
                Optional<PfAccount> pfAccountOptional = pfAccountRepository.getPfAccountByPin(PinUtil.formatPin(dataItems.get(2)));

                if (isEmployeeExist) {
                    continue;
                }
                if (!pfAccountOptional.isPresent()) {
                    continue;
                }

                Optional<PfCollection> pfCollectionOptional = pfCollectionRepository.getMonthlyPfCollection(
                    pfAccountOptional.get().getPfCode(),
                    MathRoundUtil.round(Double.parseDouble(dataItems.get(4).trim())),
                    MathRoundUtil.round(Double.parseDouble(dataItems.get(5).trim()))
                );

                if (pfCollectionOptional.isPresent()) {
                    if (dataItems.get(1).trim().equalsIgnoreCase("TRUE")) {
                        pfCollectionOptional.get().setGross(Double.parseDouble(dataItems.get(6).trim()));
                        pfCollectionOptional.get().setBasic(Double.parseDouble(dataItems.get(7).trim()));

                        pfCollectionOptional.get().setEmployeeContribution(Double.parseDouble(dataItems.get(8).trim()));
                        pfCollectionOptional.get().setEmployerContribution(Double.parseDouble(dataItems.get(9).trim()));

                        pfCollectionRepository.save(pfCollectionOptional.get());
                    }
                } else {
                    PfCollection pfCollection = new PfCollection();

                    pfCollection.setPfAccount(pfAccountOptional.get());

                    int year = MathRoundUtil.round(Double.parseDouble(dataItems.get(4).trim()));
                    int month = MathRoundUtil.round(Double.parseDouble(dataItems.get(5).trim()));
                    pfCollection.setYear(year);
                    pfCollection.setMonth(month);

                    pfCollection.setGross(Double.parseDouble(dataItems.get(6).trim()));
                    pfCollection.setBasic(Double.parseDouble(dataItems.get(7).trim()));

                    if (pfCollection.getEmployeeInterest() == null) {
                        pfCollection.setEmployeeInterest(0d);
                    }
                    if (pfCollection.getEmployerInterest() == null) {
                        pfCollection.setEmployerInterest(0d);
                    }

                    pfCollection.setEmployeeContribution(Double.parseDouble(dataItems.get(8).trim()));
                    pfCollection.setEmployerContribution(Double.parseDouble(dataItems.get(9).trim()));

                    LocalDate transactionDate = LocalDate.of(year, month, 25);
                    pfCollection.setTransactionDate(transactionDate);

                    pfCollection.setCollectionType(PfCollectionType.MONTHLY);

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
