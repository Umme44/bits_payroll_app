package com.bits.hr.service.finalSettlement;

import static com.bits.hr.service.salaryGenerationFractional.SalaryConstants.BASIC_PERCENT;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.finalSettlement.dto.PfGfStatement;
import com.bits.hr.service.finalSettlement.dto.PfStatement;
import com.bits.hr.service.finalSettlement.dto.pf.PfDetails;
import com.bits.hr.service.finalSettlement.dto.salary.TimeDuration;
import com.bits.hr.service.finalSettlement.helperMethods.PfStatementGenerationService;
import com.bits.hr.service.finalSettlement.helperMethods.ResignationProcessingService;
import com.bits.hr.service.finalSettlement.util.PfStatementToPfDetails;
import com.bits.hr.service.finalSettlement.util.ServiceTenure;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PfGfSettlementService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PfStatementGenerationService pfStatementGenerationService;

    @Autowired
    private ResignationProcessingService lastWorkingDayService;

    public Optional<PfGfStatement> getPfGfStatement(long employeeId) {
        PfGfStatement pfGfStatement = new PfGfStatement();

        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

        if (!employeeOptional.isPresent()) {
            log.debug(" No employee found with associated employee id");
            return Optional.ofNullable(null);
        }
        Employee employee = employeeOptional.get();

        if (employee.getEmployeeCategory() != EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
            return Optional.ofNullable(null);
        }

        pfGfStatement.setName(employee.getFullName());
        pfGfStatement.setPin(employee.getPin());
        pfGfStatement.setDateOfJoining(employee.getDateOfJoining());
        pfGfStatement.setDateOfConfirmation(employee.getDateOfConfirmation());
        pfGfStatement.setGfRulesEffectiveFrom(employee.getDateOfJoining());
        pfGfStatement.setPfRulesEffectiveFrom(employee.getDateOfConfirmation());
        // get last working day from employee Resignation
        Optional<LocalDate> lastWorkingDayOptional = lastWorkingDayService.getLastWorkingDay(employeeId);
        if (!lastWorkingDayOptional.isPresent()) {
            log.debug(" employee resignation not found for pin ::" + employee.getPin() + " Name ::" + employee.getFullName());
            return Optional.ofNullable(null);
        }

        pfGfStatement.setLastWorkingDay(lastWorkingDayOptional.get());

        // calculate tenure
        TimeDuration gfTenure = ServiceTenure.calculateTenure(employee.getDateOfJoining(), lastWorkingDayOptional.get());
        TimeDuration pfTenure = ServiceTenure.calculateTenure(employee.getDateOfConfirmation(), lastWorkingDayOptional.get());
        pfGfStatement.setGfEntitlementTenure(
            gfTenure.getYear() + " Years, " + gfTenure.getMonth() + " Months, " + gfTenure.getDay() + " Days"
        );
        pfGfStatement.setPfEntitlementTenure(
            pfTenure.getYear() + " Years, " + pfTenure.getMonth() + " Months, " + pfTenure.getDay() + " Days"
        );
        pfGfStatement.setPfEntitlementTenureInDays(
            ChronoUnit.DAYS.between(employee.getDateOfJoining(), lastWorkingDayOptional.get().plusDays(1))
        );
        pfGfStatement.setGfEntitlementTenureInDays(
            ChronoUnit.DAYS.between(employee.getDateOfConfirmation(), lastWorkingDayOptional.get().plusDays(1))
        );
        PfStatement pfStatement = pfStatementGenerationService.generate(employeeId).get();
        PfDetails pfDetails = PfStatementToPfDetails.generatePfDetailsFromPfStatement(pfStatement, pfTenure);
        pfGfStatement.setPfDetails(pfDetails);

        int serviceYear = ServiceTenure.calculateServiceYear(employee.getDateOfJoining(), lastWorkingDayOptional.get());
        pfGfStatement.setServiceLengthInYear(serviceYear);
        double lastBasic = employee.getMainGrossSalary() * BASIC_PERCENT;
        pfGfStatement.setLastBasic(lastBasic);
        double totalGfPayable = (double) serviceYear * 1.5d * lastBasic;
        pfGfStatement.setTotalGfPayable(totalGfPayable);
        pfGfStatement.setTotalPayablePfAndGf(pfGfStatement.getTotalGfPayable() + pfGfStatement.getPfDetails().getTotalPfPayable());
        return Optional.of(pfGfStatement);
    }
}
