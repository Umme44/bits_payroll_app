package com.bits.hr.service.importXL.pf;

import static com.bits.hr.service.importXL.pf.rowConstants.PfCollectionRowConstants.*;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.PfCollection;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.PfCollectionType;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.repository.PfCollectionRepository;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.service.importXL.pf.helperMethods.PfHelperService;
import com.bits.hr.service.importXL.pf.helperMethods.enumHelper.GetPfCollectionTypeEnumFromString;
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
public class PfCollectionsImportService implements ImportService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private PfAccountRepository pfAccountRepository;

    @Autowired
    private PfHelperService pfHelperService;

    @Autowired
    private PfCollectionRepository pfCollectionRepository;

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    @Override
    public boolean importFile(MultipartFile file) {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);

            // removing header
            data.remove(0);
            for (ArrayList<String> dataItems : data) {
                // 0 -> SL/NO
                // 1 -> Overwrite On exist
                // 2 -> PIN
                // 3 -> Pf Code
                // 4 -> Collection Type
                // 5 -> Year
                // 6 -> Month
                // 7 -> Gross
                // 8 -> Basic
                // 9 -> Transaction Date
                // 10-> Employee Contribution
                // 11-> Employee Interest
                // 12-> Employer contribution
                // 13-> Employer Interest
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

                String pfCode = PinUtil.formatPin(dataItems.get(PF_C_PF_CODE));
                String pin = PinUtil.formatPin(dataItems.get(PF_C_PIN));

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

                int month = (int) Double.parseDouble(dataItems.get(PF_C_COLLECTION_MONTH));
                int year = (int) Double.parseDouble(dataItems.get(PF_C_COLLECTION_YEAR));

                double gross = Double.parseDouble(dataItems.get(PF_C_COLLECTION_GROSS).trim());
                double basic = Double.parseDouble(dataItems.get(PF_C_COLLECTION_BASIC).trim());

                PfCollectionType pfCollectionType = GetPfCollectionTypeEnumFromString.get(dataItems.get(PF_C_COLLECTION_TYPE));
                LocalDate transactionDate = LocalDate.of(year, month, 25);
                try {
                    transactionDate = pfHelperService.stringToDate(dataItems.get(PF_C_TRANSACTION_DATE));
                } catch (Exception ex) {
                    log.debug(ex.getMessage());
                }
                double employeeContribution = MathRoundUtil.round(Double.parseDouble(dataItems.get(PF_C_EMPLOYEE_CONTRIBUTION)));
                double employeeInterest = MathRoundUtil.round(Double.parseDouble(dataItems.get(PF_C_EMPLOYEE_INTEREST)));
                double employerContribution = MathRoundUtil.round(Double.parseDouble(dataItems.get(PF_C_EMPLOYER_CONTRIBUTION)));
                double employerInterest = MathRoundUtil.round(Double.parseDouble(dataItems.get(PF_C_EMPLOYER_INTEREST)));

                Optional<PfCollection> pfCollectionOptional = pfCollectionRepository.getPfCollection(pfCode, year, month, pfCollectionType);
                boolean isPfCollectionExist = pfCollectionOptional.isPresent();
                boolean overwriteIfExist = dataItems.get(PF_C_OVERWRITE_ON_EXIST).trim().equalsIgnoreCase("true");
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
                    pfCollection.setEmployeeInterest(employeeInterest);
                    pfCollection.setEmployerContribution(employerContribution);
                    pfCollection.setEmployerInterest(employerInterest);
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
            log.error(e);
            e.printStackTrace();
            return false;
        }
    }

    public boolean importGrossAndBasicToAllPfCollection(MultipartFile file) {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            // removing header
            data.remove(0);

            List<PfCollection> pfCollectionList = new ArrayList<>();

            // PIN    Year    Month    Gross    Basic
            //  0       1       2        3        4

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

                for (String item : dataItems) {
                    if (item.equals("") || item.equals("0")) {
                        return false;
                    }
                }

                String pfCode = PinUtil.formatPin(dataItems.get(0).trim());
                int year = MathRoundUtil.round(Double.parseDouble(dataItems.get(1).trim()));
                int month = MathRoundUtil.round(Double.parseDouble(dataItems.get(2).trim()));

                Optional<PfCollection> pfCollectionOptional = pfCollectionRepository.getMonthlyPfCollection(pfCode, year, month);
                if (!pfCollectionOptional.isPresent()) {
                    continue;
                }

                pfCollectionOptional.get().setGross(Double.parseDouble(dataItems.get(3).trim()));
                pfCollectionOptional.get().setBasic(Double.parseDouble(dataItems.get(4).trim()));

                pfCollectionList.add(pfCollectionOptional.get());
            }

            for (PfCollection pfCollection : pfCollectionList) {
                pfCollectionRepository.save(pfCollection);
            }

            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    public boolean setGrossAndBasicToAllPfCollection() {
        try {
            List<EmployeeSalary> employeeSalaries = employeeSalaryRepository.findAll();

            for (EmployeeSalary es : employeeSalaries) {
                Optional<PfCollection> pfCollectionOptional = pfCollectionRepository.getMonthlyPfCollection(
                    es.getPin(),
                    es.getYear(),
                    Month.fromEnum(es.getMonth())
                );

                if (!pfCollectionOptional.isPresent()) {
                    continue;
                }

                pfCollectionOptional.get().setGross(es.getMainGrossSalary());
                pfCollectionOptional.get().setBasic(es.getMainGrossBasicSalary());

                pfCollectionRepository.save(pfCollectionOptional.get());
            }

            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }
}
