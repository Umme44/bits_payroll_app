package com.bits.hr.service.salaryGenerationFractional;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.util.DateUtil;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllowanceService {

    @Autowired
    protected GetConfigValueByKeyService getConfigValueByKeyService;

    public Allowance getAllowance(Employee employee, int month, int year) {
        Allowance allowance = new Allowance();
        LocalDate effectiveDate = LocalDate.of(year, month, 1);

        // allowance 01
        if (
            employee.getAllowance01EffectiveFrom() != null &&
            employee.getAllowance01EffectiveTo() != null &&
            DateUtil.isBetween(effectiveDate, employee.getAllowance01EffectiveFrom(), employee.getAllowance01EffectiveTo())
        ) {
            allowance.setAllowance01(employee.getAllowance01());
        }

        // allowance 02
        if (
            employee.getAllowance02EffectiveFrom() != null &&
            employee.getAllowance02EffectiveTo() != null &&
            DateUtil.isBetween(effectiveDate, employee.getAllowance02EffectiveFrom(), employee.getAllowance02EffectiveTo())
        ) {
            allowance.setAllowance02(employee.getAllowance02());
        }

        // allowance 03
        if (
            employee.getAllowance03EffectiveFrom() != null &&
            employee.getAllowance03EffectiveTo() != null &&
            DateUtil.isBetween(effectiveDate, employee.getAllowance03EffectiveFrom(), employee.getAllowance03EffectiveTo())
        ) {
            allowance.setAllowance03(employee.getAllowance03());
        }

        // allowance 04
        if (
            employee.getAllowance04EffectiveFrom() != null &&
            employee.getAllowance04EffectiveTo() != null &&
            DateUtil.isBetween(effectiveDate, employee.getAllowance04EffectiveFrom(), employee.getAllowance04EffectiveTo())
        ) {
            allowance.setAllowance04(employee.getAllowance04());
        }

        // allowance 05
        if (
            employee.getAllowance05EffectiveFrom() != null &&
            employee.getAllowance05EffectiveTo() != null &&
            DateUtil.isBetween(effectiveDate, employee.getAllowance05EffectiveFrom(), employee.getAllowance05EffectiveTo())
        ) {
            allowance.setAllowance05(employee.getAllowance05());
        }

        // allowance 06
        if (
            employee.getAllowance06EffectiveFrom() != null &&
            employee.getAllowance06EffectiveTo() != null &&
            DateUtil.isBetween(effectiveDate, employee.getAllowance06EffectiveFrom(), employee.getAllowance06EffectiveTo())
        ) {
            allowance.setAllowance06(employee.getAllowance06());
        }

        allowance.setTaxableAllowance01(getConfigValueByKeyService.isTaxableAllowance01());
        allowance.setTaxableAllowance02(getConfigValueByKeyService.isTaxableAllowance02());
        allowance.setTaxableAllowance03(getConfigValueByKeyService.isTaxableAllowance03());
        allowance.setTaxableAllowance04(getConfigValueByKeyService.isTaxableAllowance04());
        allowance.setTaxableAllowance05(getConfigValueByKeyService.isTaxableAllowance05());
        allowance.setTaxableAllowance06(getConfigValueByKeyService.isTaxableAllowance06());

        return allowance;
    }
}
