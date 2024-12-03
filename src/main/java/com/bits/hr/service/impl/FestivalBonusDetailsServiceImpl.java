package com.bits.hr.service.impl;

import com.bits.hr.domain.*;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.FestivalBonusDetailsRepository;
import com.bits.hr.service.FestivalBonusDetailsService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.dto.FestivalBonusDetailsDTO;
import com.bits.hr.service.mapper.EmployeeSalaryMapper;
import com.bits.hr.service.mapper.FestivalBonusDetailsMapper;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FestivalBonusDetails}.
 */
@Log4j2
@Service
@Transactional
public class FestivalBonusDetailsServiceImpl implements FestivalBonusDetailsService {

    private final FestivalBonusDetailsRepository festivalBonusDetailsRepository;

    private final FestivalBonusDetailsMapper festivalBonusDetailsMapper;

    @Autowired
    private EmployeeSalaryMapper employeeSalaryMapper;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public FestivalBonusDetailsServiceImpl(
        FestivalBonusDetailsRepository festivalBonusDetailsRepository,
        FestivalBonusDetailsMapper festivalBonusDetailsMapper
    ) {
        this.festivalBonusDetailsRepository = festivalBonusDetailsRepository;
        this.festivalBonusDetailsMapper = festivalBonusDetailsMapper;
    }

    @Override
    public FestivalBonusDetailsDTO save(FestivalBonusDetailsDTO festivalBonusDetailsDTO) {
        log.debug("Request to save FestivalBonusDetails : {}", festivalBonusDetailsDTO);

        FestivalBonusDetails festivalBonusDetails = festivalBonusDetailsMapper.toEntity(festivalBonusDetailsDTO);
        festivalBonusDetails = festivalBonusDetailsRepository.save(festivalBonusDetails);
        return festivalBonusDetailsMapper.toDto(festivalBonusDetails);
    }

    @Override
    public FestivalBonusDetailsDTO update(FestivalBonusDetailsDTO festivalBonusDetailsDTO) {
        log.debug("Request to save FestivalBonusDetails : {}", festivalBonusDetailsDTO);
        Optional<FestivalBonusDetails> fbDetails = festivalBonusDetailsRepository.findById(festivalBonusDetailsDTO.getId());

        if (!fbDetails.isPresent()) {
            throw new BadRequestAlertException("Festival bonus details not found", "FestivalBonusDetails", "notFound");
        }

        fbDetails.get().setGross(festivalBonusDetailsDTO.getGross());
        fbDetails.get().setBasic(festivalBonusDetailsDTO.getBasic());
        fbDetails.get().setBonusAmount(festivalBonusDetailsDTO.getBonusAmount());

        FestivalBonusDetails festivalBonusDetails = festivalBonusDetailsRepository.save(fbDetails.get());
        return festivalBonusDetailsMapper.toDto(festivalBonusDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FestivalBonusDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FestivalBonusDetails");
        return festivalBonusDetailsRepository.findAll(pageable).map(festivalBonusDetailsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FestivalBonusDetailsDTO> findOne(Long id) {
        log.debug("Request to get FestivalBonusDetails : {}", id);
        return festivalBonusDetailsRepository.findById(id).map(festivalBonusDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FestivalBonusDetails : {}", id);
        festivalBonusDetailsRepository.deleteById(id);
    }

    @Override
    public EmployeeSalaryDTO prepareFestivalBonusPayslip(long employeeId, int year, int festivalNo) {
        try {
            Employee employee = employeeRepository.findById(employeeId).get();
            List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.getNonProRataFbBonusByEmployeeIdBetweenTimeRange(
                employeeId,
                LocalDate.of(year, java.time.Month.JANUARY, 1),
                LocalDate.of(year, java.time.Month.DECEMBER, 31)
            );

            EmployeeSalary employeeSalary = new EmployeeSalary();
            employeeSalary.setEmployee(employee);
            employeeSalary.setPayableGrossBasicSalary(0.0);
            employeeSalary.setPayableGrossHouseRent(0.0);
            employeeSalary.setPayableGrossConveyanceAllowance(0.0);
            employeeSalary.setEntertainment(0.0);
            employeeSalary.setUtility(0.0);
            employeeSalary.setPayableGrossSalary(0.0);
            employeeSalary.setAllowance01(0.0);
            employeeSalary.setAllowance02(0.0);
            employeeSalary.setOtherAddition(0.0);
            employeeSalary.setSalaryAdjustment(0.0);
            employeeSalary.setArrearSalary(0.0);
            employeeSalary.setTotalDeduction(0.0);
            employeeSalary.setMobileBillDeduction(0.0);
            employeeSalary.setWelfareFundDeduction(0.0);
            employeeSalary.setTaxDeduction(0.0);
            employeeSalary.setPfDeduction(0.0);
            employeeSalary.setPfArrear(0.0);
            employeeSalary.setTotalDeduction(0.0);

            if (festivalNo == 1) {
                double bonusAmount = festivalBonusDetailsList.get(0).getBonusAmount();
                EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(employeeSalary);
                employeeSalaryDTO.setFestivalBonus(bonusAmount);
                employeeSalaryDTO.setNetPay(bonusAmount);
                return employeeSalaryDTO;
            } else if (festivalNo == 2) {
                double bonusAmount = festivalBonusDetailsList.get(1).getBonusAmount();
                EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(employeeSalary);
                employeeSalaryDTO.setFestivalBonus(bonusAmount);
                employeeSalaryDTO.setNetPay(bonusAmount);
                return employeeSalaryDTO;
            } else {
                EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(dummyFestivalPayslip());
                employeeSalaryDTO.setFestivalBonus(0.0);
                return employeeSalaryDTO;
            }
        } catch (Exception e) {
            EmployeeSalaryDTO employeeSalaryDTO = employeeSalaryMapper.toDto(dummyFestivalPayslip());
            employeeSalaryDTO.setFestivalBonus(0.0);
            return employeeSalaryDTO;
        }
    }

    private EmployeeSalary dummyFestivalPayslip() {
        /*if festival data is not available, then generate dummy report will 0 values*/
        EmployeeSalary employeeSalary = new EmployeeSalary();
        employeeSalary.setPayableGrossBasicSalary(0.0);
        employeeSalary.setPayableGrossHouseRent(0.0);
        employeeSalary.setPayableGrossConveyanceAllowance(0.0);
        employeeSalary.setEntertainment(0.0);
        employeeSalary.setUtility(0.0);
        employeeSalary.setPayableGrossSalary(0.0);
        employeeSalary.setAllowance01(0.0);
        employeeSalary.setAllowance02(0.0);
        employeeSalary.setOtherAddition(0.0);
        employeeSalary.setSalaryAdjustment(0.0);
        employeeSalary.setArrearSalary(0.0);
        employeeSalary.setTotalDeduction(0.0);
        employeeSalary.setMobileBillDeduction(0.0);
        employeeSalary.setWelfareFundDeduction(0.0);
        employeeSalary.setTaxDeduction(0.0);
        employeeSalary.setPfDeduction(0.0);
        employeeSalary.setPfArrear(0.0);
        employeeSalary.setTotalDeduction(0.0);
        employeeSalary.setNetPay(0.0);
        if (currentEmployeeService.getCurrentEmployee().isPresent()) {
            Employee employee = currentEmployeeService.getCurrentEmployee().get();
            employeeSalary.setEmployee(employee);
        } else {
            Employee employee = new Employee();
            employee.setPin("");
            employee.setFullName("");
            employee.setUnit(new Unit());
            employee.setDepartment(new Department());
            employee.setDesignation(new Designation());
            employee.setDateOfJoining(LocalDate.of(1900, Month.JANUARY, 1)); //dummy date
            employeeSalary.setEmployee(employee);
        }
        return employeeSalary;
    }

    @Override
    public Set<Integer> getDisbursedFestivalYearsByEmployeeId(long employeeId, LocalDate disbursementDate) {
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findDisbursedByEmployeeId(
            employeeId,
            disbursementDate
        );
        HashSet<Integer> years = new HashSet<>();
        festivalBonusDetailsList.forEach(details -> {
            int year = details.getFestival().getBonusDisbursementDate().getYear();
            years.add(year);
        });
        return years;
    }

    @Override
    public List<FestivalBonusDetailsDTO> getYearWiseFestivalBonusDetailsList(long employeeId, int year) {
        List<FestivalBonusDetails> festivalBonusDetailsList = festivalBonusDetailsRepository.findAllByEmployeeIdBetweenDates(
            employeeId,
            LocalDate.of(year, java.time.Month.JANUARY, 1),
            LocalDate.of(year, java.time.Month.DECEMBER, 31)
        );

        List<FestivalBonusDetailsDTO> festivalBonusDetailsDTOS = festivalBonusDetailsMapper.toDto(festivalBonusDetailsList);
        /** festival bonus serial no for front-end selection **/
        int count;
        for (int i = 0; i < festivalBonusDetailsDTOS.size(); i++) {
            count = i + 1;
            festivalBonusDetailsDTOS.get(i).setYearlyFestivalOrderNo(count);
            /* remove other info */
            festivalBonusDetailsDTOS.get(i).setFestivalId(0l);
            festivalBonusDetailsDTOS.get(i).setFestivalName("");
        }
        return festivalBonusDetailsDTOS;
    }

    @Override
    public boolean holdOrUnHoldFestivalBonusDetail(long fbDetailId, boolean isHold) {
        try {
            Optional<FestivalBonusDetails> festivalBonusDetailsOptional = festivalBonusDetailsRepository.findById(fbDetailId);
            if (!festivalBonusDetailsOptional.isPresent()) {
                throw new BadRequestAlertException("Invalid Festival Bonus Details ID", "FestivalBonusDetails", "idnull");
            }
            if (isHold) {
                festivalBonusDetailsOptional.get().setIsHold(true);
                festivalBonusDetailsOptional.get().setRemarks("Hold FnF");
            } else {
                festivalBonusDetailsOptional.get().setIsHold(false);
                festivalBonusDetailsOptional.get().setRemarks(" - ");
            }

            festivalBonusDetailsRepository.save(festivalBonusDetailsOptional.get());
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }
}
