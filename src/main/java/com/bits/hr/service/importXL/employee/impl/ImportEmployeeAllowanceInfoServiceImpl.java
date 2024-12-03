package com.bits.hr.service.importXL.employee.impl;

import com.bits.hr.domain.Config;
import com.bits.hr.domain.Employee;
import com.bits.hr.repository.ConfigRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.config.DefinedKeys;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.service.importXL.XLImportCommonService;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.PinUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ImportEmployeeAllowanceInfoServiceImpl {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ConfigRepository configRepository;

    public boolean importFile(MultipartFile file) {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            //  0               1       2                           3                   4                       5
            //  UPD IF EXST   YES	    Allowance Effective From	December 31, 1998	Allowance Effective To 	December 31, 2077
            List<String> header1 = data.remove(0);

            // take effective from and effective to from header 1
            LocalDate effectiveFrom = LocalDate.of(1998, 12, 31);
            LocalDate effectiveTo = LocalDate.of(2077, 12, 31);
            try {
                if (header1.get(1).toUpperCase(Locale.ROOT).equals("YES")) {
                    effectiveFrom = DateUtil.doubleToDate(Double.parseDouble(header1.get(3)));
                    effectiveTo = DateUtil.doubleToDate(Double.parseDouble(header1.get(5)));
                }
            } catch (Exception e) {
                //                logger.debug(e.toString());
            }

            //0     1               2   3   4   5   6   7
            //YES	is Taxable ?	NO	NO	NO	NO	NO	NO
            List<String> header2 = data.remove(0);
            // update conf from header 2 if said YES
            if (header2.get(0).toLowerCase(Locale.ROOT).equals("yes")) processAndSaveTaxConfig(header2);

            //  0           1                       2               3               4                5               6               7
            //	<<blank>>   Allowance Generic name	Allowance 01 	Allowance 02	Allowance 03	Allowance 04 	Allowance 05	Allowance 06
            List<String> header3 = data.remove(0);
            // nothing to do with header 3

            //0     1       2                       3               4                           5                    6               7
            //YES	PIN	    Living Allowance-LFA	Car Allowance	House Rent Reimbursement 	Company Secretary	 NOT_APPLICABLE	 NOT_APPLICABLE
            List<String> header4 = data.remove(0);
            // update configuration accordingly from header 4
            processAndSaveAllowanceNames(header4);

            // 0    1       2   3   4   5   6   7
            // YES	1748	5	10	15	20	0	0
            for (List<String> dataItems : data) {
                Employee employee = new Employee();
                // skip empty
                if (!XLImportCommonService.isXLRowValid(dataItems)) {
                    continue;
                }
                // skip if asked to not update existing
                if (dataItems.get(0).toLowerCase(Locale.ROOT).equals("no")) {
                    continue;
                }
                // skip if employee not available according to pin
                if (employeeRepository.findEmployeeByPin(PinUtil.formatPin(dataItems.get(1))).isPresent()) {
                    employee = employeeRepository.findEmployeeByPin(PinUtil.formatPin(dataItems.get(1))).get();
                } else {
                    continue;
                }
                double allowance01 = Double.parseDouble(dataItems.get(2));
                double allowance02 = Double.parseDouble(dataItems.get(3));
                double allowance03 = Double.parseDouble(dataItems.get(4));
                double allowance04 = Double.parseDouble(dataItems.get(5));
                double allowance05 = Double.parseDouble(dataItems.get(6));
                double allowance06 = Double.parseDouble(dataItems.get(7));

                employee.setAllowance01(allowance01);
                employee.setAllowance01EffectiveFrom(effectiveFrom);
                employee.setAllowance01EffectiveTo(effectiveTo);

                employee.setAllowance02(allowance02);
                employee.setAllowance02EffectiveFrom(effectiveFrom);
                employee.setAllowance02EffectiveTo(effectiveTo);

                employee.setAllowance03(allowance03);
                employee.setAllowance03EffectiveFrom(effectiveFrom);
                employee.setAllowance03EffectiveTo(effectiveTo);

                employee.setAllowance04(allowance04);
                employee.setAllowance04EffectiveFrom(effectiveFrom);
                employee.setAllowance04EffectiveTo(effectiveTo);

                employee.setAllowance05(allowance05);
                employee.setAllowance05EffectiveFrom(effectiveFrom);
                employee.setAllowance05EffectiveTo(effectiveTo);

                employee.setAllowance06(allowance06);
                employee.setAllowance06EffectiveFrom(effectiveFrom);
                employee.setAllowance06EffectiveTo(effectiveTo);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void processAndSaveTaxConfig(List<String> header2) {
        //0     1               2   3   4   5   6   7
        //YES	is Taxable ?	NO	NO	NO	NO	NO	NO
        boolean isTaxableAllowance01 = header2.get(2).toLowerCase(Locale.ROOT).equals("yes");
        boolean isTaxableAllowance02 = header2.get(3).toLowerCase(Locale.ROOT).equals("yes");
        boolean isTaxableAllowance03 = header2.get(4).toLowerCase(Locale.ROOT).equals("yes");
        boolean isTaxableAllowance04 = header2.get(5).toLowerCase(Locale.ROOT).equals("yes");
        boolean isTaxableAllowance05 = header2.get(6).toLowerCase(Locale.ROOT).equals("yes");
        boolean isTaxableAllowance06 = header2.get(7).toLowerCase(Locale.ROOT).equals("yes");

        Optional<Config> allowance01TaxConfig = configRepository.findConfigByKey(DefinedKeys.is_taxable_allowance01);
        Optional<Config> allowance02TaxConfig = configRepository.findConfigByKey(DefinedKeys.is_taxable_allowance02);
        Optional<Config> allowance03TaxConfig = configRepository.findConfigByKey(DefinedKeys.is_taxable_allowance03);
        Optional<Config> allowance04TaxConfig = configRepository.findConfigByKey(DefinedKeys.is_taxable_allowance04);
        Optional<Config> allowance05TaxConfig = configRepository.findConfigByKey(DefinedKeys.is_taxable_allowance05);
        Optional<Config> allowance06TaxConfig = configRepository.findConfigByKey(DefinedKeys.is_taxable_allowance06);

        saveOrUpdateConfig(allowance01TaxConfig, DefinedKeys.is_taxable_allowance01, Boolean.toString(isTaxableAllowance01));
        saveOrUpdateConfig(allowance02TaxConfig, DefinedKeys.is_taxable_allowance02, Boolean.toString(isTaxableAllowance02));
        saveOrUpdateConfig(allowance03TaxConfig, DefinedKeys.is_taxable_allowance03, Boolean.toString(isTaxableAllowance03));
        saveOrUpdateConfig(allowance04TaxConfig, DefinedKeys.is_taxable_allowance04, Boolean.toString(isTaxableAllowance04));
        saveOrUpdateConfig(allowance05TaxConfig, DefinedKeys.is_taxable_allowance05, Boolean.toString(isTaxableAllowance05));
        saveOrUpdateConfig(allowance06TaxConfig, DefinedKeys.is_taxable_allowance06, Boolean.toString(isTaxableAllowance06));
    }

    private void processAndSaveAllowanceNames(List<String> header4) {
        //0     1       2                       3               4                           5                    6               7
        //YES	PIN	    Living Allowance-LFA	Car Allowance	House Rent Reimbursement 	Company Secretary	 NOT_APPLICABLE	 NOT_APPLICABLE

        String allowance01Name = header4.get(2).trim();
        String allowance02Name = header4.get(3).trim();
        String allowance03Name = header4.get(4).trim();
        String allowance04Name = header4.get(5).trim();
        String allowance05Name = header4.get(6).trim();
        String allowance06Name = header4.get(7).trim();

        Optional<Config> allowance01NameConfig = configRepository.findConfigByKey(DefinedKeys.name_allowance01);
        Optional<Config> allowance02NameConfig = configRepository.findConfigByKey(DefinedKeys.name_allowance02);
        Optional<Config> allowance03NameConfig = configRepository.findConfigByKey(DefinedKeys.name_allowance03);
        Optional<Config> allowance04NameConfig = configRepository.findConfigByKey(DefinedKeys.name_allowance04);
        Optional<Config> allowance05NameConfig = configRepository.findConfigByKey(DefinedKeys.name_allowance05);
        Optional<Config> allowance06NameConfig = configRepository.findConfigByKey(DefinedKeys.name_allowance06);

        saveOrUpdateConfig(allowance01NameConfig, DefinedKeys.name_allowance01, allowance01Name);
        saveOrUpdateConfig(allowance02NameConfig, DefinedKeys.name_allowance02, allowance02Name);
        saveOrUpdateConfig(allowance03NameConfig, DefinedKeys.name_allowance03, allowance03Name);
        saveOrUpdateConfig(allowance04NameConfig, DefinedKeys.name_allowance04, allowance04Name);
        saveOrUpdateConfig(allowance05NameConfig, DefinedKeys.name_allowance05, allowance05Name);
        saveOrUpdateConfig(allowance06NameConfig, DefinedKeys.name_allowance06, allowance06Name);
    }

    private void saveOrUpdateConfig(Optional<Config> config, String key, String value) {
        // update
        if (config.isPresent()) {
            config.get().setValue(value);
            configRepository.save(config.get());
        }
        // save
        else {
            Config c1 = new Config();
            c1.setKey(key);
            c1.setValue(value);
            configRepository.save(c1);
        }
    }
}
