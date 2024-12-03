package com.bits.hr.service.salaryCertificates;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.SalaryCertificate;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.repository.SalaryCertificateRepository;
import com.bits.hr.service.SalaryCertificateService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeSalaryCertificateReportDTO;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.dto.SalaryCertificateDTO;
import com.bits.hr.service.mapper.EmployeeSalaryMapper;
import com.bits.hr.service.mapper.SalaryCertificateMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SalaryCertificateUserService {

    @Autowired
    private SalaryCertificateService salaryCertificateService;

    @Autowired
    private SalaryCertificateRepository salaryCertificateRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private SalaryCertificateMapper salaryCertificateMapper;

    @Autowired
    private EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    private EmployeeSalaryMapper employeeSalaryMapper;

    // apply
    // get all applied
    // edit applied [ only pending ]
    // delete applied [ only if pending ]
    // show details of approved
    // get salaries for drop down

    //    public boolean findSalaryCertificateBySalaryId(Long employeeSalaryId){
    //        Optional<SalaryCertificate> anySalaryCertificate = salaryCertificateRepository.findByEmployeeSalaryId(employeeSalaryId);
    //        if (anySalaryCertificate.isPresent()){
    //            return true;
    //        }
    //        return false;
    //    }

    public Optional<SalaryCertificateDTO> apply(SalaryCertificateDTO salaryCertificateDTO) {
        try {
            // Set Month, Year and employeeId to DTO.
            //            Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryRepository.findById(salaryCertificateDTO.getSalaryId());
            //            salaryCertificateDTO.setMonth(employeeSalaryOptional.get().getMonth());
            //            salaryCertificateDTO.setYear(employeeSalaryOptional.get().getYear());

            Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
            if (!employeeOptional.isPresent()) {
                throw new BadRequestAlertException("Employee Not Found", "SalaryCertificate", "noEmployee");
            }
            salaryCertificateDTO.setEmployeeId(employeeOptional.get().getId());

            Optional<SalaryCertificate> anySalaryCertificate = salaryCertificateRepository.findSalaryCertificateByEmployeeIdYearAndMonth(
                salaryCertificateDTO.getEmployeeId(),
                salaryCertificateDTO.getYear(),
                salaryCertificateDTO.getMonth()
            );
            //checking user has already one application or salary certificate for this month
            if (anySalaryCertificate.isPresent()) {
                log.error("User has previously Approved Salary Certificate or Pending Certificate Application");
                throw new BadRequestAlertException("", "SalaryCertificate", "userDuplicateMonthlySalaryCertificate");
            }
            long userId = currentEmployeeService.getCurrentUserId().get();

            salaryCertificateDTO.setCreatedById(userId);
            salaryCertificateDTO.setStatus(Status.PENDING);
            SalaryCertificateDTO salaryCertificateDTO1 = salaryCertificateService.save(salaryCertificateDTO);
            return Optional.of(salaryCertificateDTO1);
        } catch (Exception ex) {
            log.error(ex);
            return Optional.empty();
        }
    }

    public List<SalaryCertificateDTO> getAllApplied() {
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();

        if (!employeeOptional.isPresent()) {
            throw new BadRequestAlertException("No employee profile is associated with you.", "SalaryCertificate", "noEmployee");
        }

        List<SalaryCertificateDTO> salaryCertificateDTOList = salaryCertificateMapper.toDto(
            salaryCertificateRepository.findAllByEmployeeId(employeeOptional.get().getId())
        );

        for (int i = 0; i < salaryCertificateDTOList.size(); i++) {
            Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryRepository.findEmployeeSalaryByEmployeeIdYearAndMonth(
                salaryCertificateDTOList.get(i).getEmployeeId(),
                salaryCertificateDTOList.get(i).getYear(),
                salaryCertificateDTOList.get(i).getMonth()
            );
            salaryCertificateDTOList.get(i).setSalaryId(employeeSalaryOptional.get().getId());
        }
        return salaryCertificateDTOList;
    }

    public Optional<SalaryCertificateDTO> update(SalaryCertificateDTO salaryCertificateDTO) {
        SalaryCertificateDTO existingSalaryCertificate = salaryCertificateService.findOne(salaryCertificateDTO.getId()).get();

        if (!currentEmployeeService.getCurrentUserId().isPresent()) {
            return Optional.empty();
        }
        Long currentUserId = currentEmployeeService.getCurrentUserId().get();

        Optional<SalaryCertificate> salaryCertificateForThisMonth = salaryCertificateRepository.findSalaryCertificateByEmployeeIdYearAndMonth(
            salaryCertificateDTO.getEmployeeId(),
            salaryCertificateDTO.getYear(),
            salaryCertificateDTO.getMonth()
        );
        if (
            salaryCertificateForThisMonth.isPresent() && !salaryCertificateDTO.getId().equals(salaryCertificateForThisMonth.get().getId())
        ) {
            log.error("User has previously Approved Salary Certificate or Pending Certificate Application");
            throw new BadRequestAlertException("", "SalaryCertificate", "userDuplicateMonthlySalaryCertificate");
        }

        //1. check same or different user has requested for updating his/her salary certificate application;
        //2. the application in pending status or not
        if (
            !existingSalaryCertificate.getCreatedById().equals(currentUserId) ||
            !existingSalaryCertificate.getStatus().equals(Status.PENDING)
        ) {
            return Optional.empty();
        } else {
            salaryCertificateDTO.setCreatedAt(existingSalaryCertificate.getCreatedAt());
            salaryCertificateDTO.setUpdatedAt(LocalDate.now());
            salaryCertificateDTO.setUpdatedById(currentUserId);
            salaryCertificateDTO.setCreatedById(currentUserId);
            return Optional.of(salaryCertificateService.save(salaryCertificateDTO));
        }
    }

    public void delete(Long id) throws Exception {
        if (currentEmployeeService.getCurrentUserId().isPresent()) {
            SalaryCertificateDTO salaryCertificateDTO = this.findSalaryCertificate(id);
            if (!salaryCertificateDTO.getStatus().equals(Status.PENDING)) {
                throw new Exception();
            }
            if (
                salaryCertificateDTO.getCreatedById() != null &&
                currentEmployeeService.getCurrentUserId().get().equals(salaryCertificateDTO.getCreatedById())
            ) {
                salaryCertificateService.delete(id);
            }
        }
    }

    public Optional<EmployeeSalaryDTO> getSalaryForSalaryCertificates(long id) {
        Optional<SalaryCertificateDTO> salaryCertificateDTOOptional = salaryCertificateService.findOne(id);
        if (!salaryCertificateDTOOptional.isPresent()) {
            Optional.empty();
        }
        if (
            salaryCertificateDTOOptional.get().getCreatedById() != null &&
            currentEmployeeService.getCurrentUserId() != null &&
            salaryCertificateDTOOptional.get().getCreatedById() == currentEmployeeService.getCurrentUserId().get()
        ) {
            Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryRepository.findEmployeeSalaryByEmployeeIdYearAndMonth(
                salaryCertificateDTOOptional.get().getEmployeeId(),
                salaryCertificateDTOOptional.get().getYear(),
                salaryCertificateDTOOptional.get().getMonth()
            );
            if (!employeeSalaryOptional.isPresent()) {
                return Optional.empty();
            }
            return Optional.of(employeeSalaryMapper.toDto(employeeSalaryOptional.get()));
        } else return Optional.empty();
    }

    public SalaryCertificateDTO findSalaryCertificate(Long id) {
        List<SalaryCertificateDTO> appliedSalaryCertificate = this.getAllApplied();
        SalaryCertificateDTO salaryCertificateDTO = appliedSalaryCertificate
            .stream()
            .filter(x -> x.getId().equals(id))
            .collect(Collectors.toList())
            .get(0);

        Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryRepository.findEmployeeSalaryByEmployeeIdYearAndMonth(
            salaryCertificateDTO.getEmployeeId(),
            salaryCertificateDTO.getYear(),
            salaryCertificateDTO.getMonth()
        );
        salaryCertificateDTO.setSalaryId(employeeSalaryOptional.get().getId());
        return salaryCertificateDTO;
    }

    public List<EmployeeSalaryDTO> getSalariesForDropDown() {
        List<EmployeeSalaryDTO> employeeSalaryDTOList = new ArrayList<>();
        Optional<Employee> employeeOptional = currentEmployeeService.getCurrentEmployee();
        if (employeeOptional.isPresent()) {
            List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAllByEmployeeId(employeeOptional.get().getId());

            if (employeeSalaryList.size() > 0) {
                employeeSalaryList.sort((s1, s2) -> {
                    if (s1.getYear() - s2.getYear() == 0) {
                        return Month.fromEnum(s2.getMonth()) - Month.fromEnum(s1.getMonth());
                    } else {
                        return s2.getYear() - s1.getYear();
                    }
                });
            }

            List<SalaryCertificate> existingSalaryCertificateList = salaryCertificateRepository.findApprovedSalaryCertificatesByEmployeeId(
                employeeOptional.get().getId()
            );

            for (int i = 0; i < existingSalaryCertificateList.size(); i++) {
                for (int j = 0; j < employeeSalaryList.size(); j++) {
                    boolean isMonthSimilar = employeeSalaryList.get(j).getMonth().equals(existingSalaryCertificateList.get(i).getMonth());
                    boolean isYearSimilar = employeeSalaryList.get(j).getYear().equals(existingSalaryCertificateList.get(i).getYear());

                    if (isYearSimilar && isMonthSimilar) {
                        employeeSalaryList.remove(employeeSalaryList.get(j));
                    }
                }
            }

            //            List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.getEligibleSalariesForSalaryCertificate(pin.get());
            employeeSalaryDTOList = employeeSalaryMapper.toDto(employeeSalaryList);
        }
        return employeeSalaryDTOList;
    }

    public EmployeeSalaryCertificateReportDTO getSalaryCertificateReportByCertificateId(Long id) {
        Long employeeId = currentEmployeeService.getCurrentEmployeeId().get();
        Optional<SalaryCertificate> salaryCertificateOptional = salaryCertificateRepository.findById(id);

        if (!salaryCertificateOptional.isPresent()) {
            throw new BadRequestAlertException("SalaryCertificate not found!", "SalaryCertificate", "notFound");
        }
        if (!salaryCertificateOptional.get().getEmployee().getId().equals(employeeId)) {
            throw new BadRequestAlertException("Employee not found!", "SalaryCertificate", "noEmployee");
        }
        if (salaryCertificateOptional.get().getStatus() != Status.APPROVED) {
            throw new BadRequestAlertException("Report not found!", "SalaryCertificate", "internalServerError");
        }

        Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryRepository.findEmployeeSalaryByEmployeeIdYearAndMonth(
            salaryCertificateOptional.get().getEmployee().getId(),
            salaryCertificateOptional.get().getYear(),
            salaryCertificateOptional.get().getMonth()
        );

        if (!employeeSalaryOptional.isPresent()) {
            throw new BadRequestAlertException("Salary not found!", "SalaryCertificate", "notFound");
        }

        EmployeeSalaryCertificateReportDTO report = new EmployeeSalaryCertificateReportDTO();

        double basic = employeeSalaryOptional.get().getPayableGrossBasicSalary() != null
            ? employeeSalaryOptional.get().getPayableGrossBasicSalary()
            : 0d;
        double houseRent = employeeSalaryOptional.get().getPayableGrossHouseRent() != null
            ? employeeSalaryOptional.get().getPayableGrossHouseRent()
            : 0d;
        double medicalAllowance = employeeSalaryOptional.get().getPayableGrossMedicalAllowance() != null
            ? employeeSalaryOptional.get().getPayableGrossMedicalAllowance()
            : 0d;
        double conveyanceAllowance = employeeSalaryOptional.get().getPayableGrossConveyanceAllowance() != null
            ? employeeSalaryOptional.get().getPayableGrossConveyanceAllowance()
            : 0d;
        double entertainment = employeeSalaryOptional.get().getEntertainment() != null
            ? employeeSalaryOptional.get().getEntertainment()
            : 0d;
        double utility = employeeSalaryOptional.get().getUtility() != null ? employeeSalaryOptional.get().getUtility() : 0d;
        double otherAddition = employeeSalaryOptional.get().getOtherAddition() != null
            ? employeeSalaryOptional.get().getOtherAddition()
            : 0d;
        double grossPay = employeeSalaryOptional.get().getPayableGrossSalary() != null
            ? employeeSalaryOptional.get().getPayableGrossSalary()
            : 0d;

        double incomeTax = employeeSalaryOptional.get().getTaxDeduction() != null ? employeeSalaryOptional.get().getTaxDeduction() : 0d;
        double pfDeduction = employeeSalaryOptional.get().getPfDeduction() != null ? employeeSalaryOptional.get().getPfDeduction() : 0d;
        double mobileBill = employeeSalaryOptional.get().getMobileBillDeduction() != null
            ? employeeSalaryOptional.get().getMobileBillDeduction()
            : 0d;
        double welfareFund = employeeSalaryOptional.get().getWelfareFundDeduction() != null
            ? employeeSalaryOptional.get().getWelfareFundDeduction()
            : 0d;
        double otherDeduction = employeeSalaryOptional.get().getOtherDeduction() != null
            ? employeeSalaryOptional.get().getOtherDeduction()
            : 0d;
        double totalDeduction = employeeSalaryOptional.get().getTotalDeduction() != null
            ? employeeSalaryOptional.get().getTotalDeduction()
            : 0d;
        double netPayable = employeeSalaryOptional.get().getNetPay() != null ? employeeSalaryOptional.get().getNetPay() : 0d;

        report.setEmployeeName(employeeSalaryOptional.get().getEmployee().getFullName());
        report.setEmployeeLastName(employeeSalaryOptional.get().getEmployee().getSurName());
        report.setPin(employeeSalaryOptional.get().getPin());
        report.setJoiningDate(employeeSalaryOptional.get().getJoiningDate());
        report.setConfirmationDate(employeeSalaryOptional.get().getConfirmationDate());
        report.setEmployeeCategory(employeeSalaryOptional.get().getEmployeeCategory());
        report.setDesignation(employeeSalaryOptional.get().getEmployee().getDesignation().getDesignationName());
        report.setUnit(employeeSalaryOptional.get().getUnit());
        report.setDepartment(employeeSalaryOptional.get().getDepartment());
        report.setMonth(employeeSalaryOptional.get().getMonth());
        report.setYear(employeeSalaryOptional.get().getYear());

        report.setPayableGrossBasicSalary(basic);
        report.setPayableGrossHouseRent(houseRent);
        report.setPayableGrossMedicalAllowance(medicalAllowance);
        report.setPayableGrossConveyanceAllowance(conveyanceAllowance);

        report.setEntertainment(entertainment);
        report.setUtility(utility);
        report.setOtherAddition(otherAddition);
        report.setPayableGrossSalary(grossPay);

        report.setPfDeduction(pfDeduction);
        report.setTaxDeduction(incomeTax);
        report.setMobileBillDeduction(mobileBill);
        report.setWelfareFundDeduction(welfareFund);
        report.setOtherDeduction(otherDeduction);
        report.setTotalDeduction(totalDeduction);

        report.setNetPay(netPayable);
        report.setNetPayInWords(NumberToWord.convertNumberToWord(employeeSalaryOptional.get().getNetPay().longValue()));

        return report;
    }
}
