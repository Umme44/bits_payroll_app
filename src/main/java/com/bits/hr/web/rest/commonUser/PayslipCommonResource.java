package com.bits.hr.web.rest.commonUser;

import com.bits.hr.domain.Employee;
import com.bits.hr.errors.NoEmployeeProfileException;
import com.bits.hr.service.EmployeeSalaryService;
import com.bits.hr.service.FestivalBonusDetailsService;
import com.bits.hr.service.IndividualArrearSalaryService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.dto.EmployeeSalaryPayslipDto;
import com.bits.hr.service.dto.FestivalBonusDetailsDTO;
import com.bits.hr.service.dto.IndividualArrearPayslipDTO;
import com.bits.hr.service.selecteable.SelectableDTO;
import java.time.LocalDate;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/common")
public class PayslipCommonResource {

    private final EmployeeSalaryService employeeSalaryService;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private FestivalBonusDetailsService festivalBonusDetailsService;

    @Autowired
    private IndividualArrearSalaryService individualArrearSalaryService;

    public PayslipCommonResource(EmployeeSalaryService employeeSalaryService) {
        this.employeeSalaryService = employeeSalaryService;
    }

    @GetMapping("/payslip/{year}/{month}")
    public ResponseEntity<EmployeeSalaryPayslipDto> getPayslipForMonthYear(
        @PathVariable("year") int year,
        @PathVariable("month") int month
    ) {
        Optional<Employee> employeeIdOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeIdOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }

        EmployeeSalaryPayslipDto result = employeeSalaryService.getSalaryPayslipByEmployeeAndYearAndMonth(
            employeeIdOptional.get(),
            year,
            month
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/my-salary-year-list")
    public ResponseEntity<HashSet<Integer>> getMyDisbursedSalaryYearList() {
        log.debug("REST Request to get generated salary years ");

        Optional<Employee> employeeIdOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeIdOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        return ResponseEntity.ok(employeeSalaryService.getSelectableYearsByEmployee(employeeIdOptional.get()));
    }

    @GetMapping("/my-salary-month-list/{year}")
    public ResponseEntity<List<SelectableDTO>> getMyDisbursedSalaryYearWiseMonthList(@PathVariable int year) {
        log.debug("REST Request to get generated months of Year: " + year);
        Optional<Employee> employeeIdOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeIdOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }
        return ResponseEntity.ok(employeeSalaryService.getVisibleSelectableListByEmployeeAndYear(employeeIdOptional.get(), year));
    }

    @GetMapping("/my-festival-bonus-year-list")
    public ResponseEntity<Set<Integer>> getMyFestivalYearList() {
        log.debug("REST Request to get years of Festival Bonus List");
        long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
        return ResponseEntity.ok(festivalBonusDetailsService.getDisbursedFestivalYearsByEmployeeId(employeeId, LocalDate.now()));
    }

    @GetMapping("/year-wise-festival-bonus-list/{year}")
    public ResponseEntity<List<FestivalBonusDetailsDTO>> getYearWiseFestivalDetailsList(@PathVariable int year) {
        log.debug("REST Request to get Festival Bonus List");
        long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
        return ResponseEntity.ok(festivalBonusDetailsService.getYearWiseFestivalBonusDetailsList(employeeId, year));
    }

    @GetMapping("/festival-bonus-payslip/{year}/{festivalNo}")
    public ResponseEntity<EmployeeSalaryDTO> getFestivalPayslip(
        @PathVariable("year") int year,
        @PathVariable("festivalNo") int festivalNo
    ) {
        long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
        EmployeeSalaryDTO employeeSalaryDTO = festivalBonusDetailsService.prepareFestivalBonusPayslip(employeeId, year, festivalNo);
        return ResponseEntity.ok(employeeSalaryDTO);
    }

    @GetMapping("/arrear-salary-years")
    public ResponseEntity<List<Integer>> getMyDisbursedArrearYearList() {
        log.debug("REST Request to get years of Arrear List");

        Optional<Employee> employeeIdOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeIdOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }

        return ResponseEntity.ok(individualArrearSalaryService.getListOfDisbursedArrearSalaryYears(employeeIdOptional.get()));
    }

    @GetMapping("/arrear-salary-title/{year}")
    public ResponseEntity<List<SelectableDTO>> getMyDisbursedArrearTitleListByYear(@PathVariable("year") int year) {
        log.debug("REST Request to get title of Arrear List");

        Optional<Employee> employeeIdOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeIdOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }

        return ResponseEntity.ok(
            individualArrearSalaryService.getListOfArrearSalaryTitleByEmployeeIdAndYear(employeeIdOptional.get(), year)
        );
    }

    @GetMapping("/arrear-salary-payslip")
    public ResponseEntity<IndividualArrearPayslipDTO> getMyDisbursedArrearDetails(@RequestParam("title") String title) {
        log.debug("REST Request to get details of Arrear");

        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (!employeeOptional.isPresent()) {
            throw new NoEmployeeProfileException();
        }

        return ResponseEntity.ok(individualArrearSalaryService.getByEmployeeAndTitle(employeeOptional.get(), title));
    }
}
