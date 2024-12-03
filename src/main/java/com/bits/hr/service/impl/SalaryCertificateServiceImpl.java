package com.bits.hr.service.impl;

import com.bits.hr.domain.EmployeeSalary;
import com.bits.hr.domain.SalaryCertificate;
import com.bits.hr.domain.enumeration.CertificateStatus;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeSalaryRepository;
import com.bits.hr.repository.SalaryCertificateRepository;
import com.bits.hr.service.SalaryCertificateService;
import com.bits.hr.service.dto.EmployeeSalaryCertificateReportDTO;
import com.bits.hr.service.dto.EmployeeSalaryDTO;
import com.bits.hr.service.dto.SalaryCertificateDTO;
import com.bits.hr.service.mapper.EmployeeSalaryMapper;
import com.bits.hr.service.mapper.SalaryCertificateMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bits.hr.service.salaryCertificates.NumberToWord;
import com.bits.hr.service.search.FilterDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SalaryCertificate}.
 */
@Service
@Transactional
public class SalaryCertificateServiceImpl implements SalaryCertificateService {

    private final Logger log = LoggerFactory.getLogger(SalaryCertificateServiceImpl.class);

    private final SalaryCertificateRepository salaryCertificateRepository;

    private final SalaryCertificateMapper salaryCertificateMapper;

    private final EmployeeSalaryRepository employeeSalaryRepository;

    private final EmployeeSalaryMapper employeeSalaryMapper;

    public SalaryCertificateServiceImpl(
        SalaryCertificateRepository salaryCertificateRepository,
        SalaryCertificateMapper salaryCertificateMapper,
        EmployeeSalaryRepository employeeSalaryRepository,
        EmployeeSalaryMapper employeeSalaryMapper
    ) {
        this.salaryCertificateRepository = salaryCertificateRepository;
        this.salaryCertificateMapper = salaryCertificateMapper;
        this.employeeSalaryRepository = employeeSalaryRepository;
        this.employeeSalaryMapper = employeeSalaryMapper;
    }

    @Override
    public SalaryCertificateDTO save(SalaryCertificateDTO salaryCertificateDTO) {
        log.debug("Request to save SalaryCertificate : {}", salaryCertificateDTO);
        SalaryCertificate salaryCertificate = salaryCertificateMapper.toEntity(salaryCertificateDTO);
        Optional<EmployeeSalary> optionalSalary = employeeSalaryRepository.findEmployeeSalaryByEmployeeIdYearAndMonth(salaryCertificateDTO.getEmployeeId(), salaryCertificateDTO.getYear(), salaryCertificateDTO.getMonth());
        if(optionalSalary.isPresent()){
            salaryCertificate = salaryCertificateRepository.save(salaryCertificate);
            return salaryCertificateMapper.toDto(salaryCertificate);
        }
        else throw new BadRequestAlertException("Salary not found!", "EmployeeSalary", "notFound");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalaryCertificateDTO> findAll(FilterDto filter, Pageable pageable) {
        log.debug("Request to get all SalaryCertificates");
        return salaryCertificateRepository.findAllBySearchText(filter.getSearchText(), pageable).map(salaryCertificateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalaryCertificateDTO> findAllFilterByStatus(Long id, CertificateStatus status, Pageable pageable) {
        log.debug("Request to get all SalaryCertificates");

        Status convertedStatus = null;
        if (status != null && status.equals(CertificateStatus.SENT_FOR_GENERATION)) {
            convertedStatus = Status.PENDING;
        } else if (status != null && status.equals(CertificateStatus.GENERATED)) {
            convertedStatus = Status.APPROVED;
        }

        return salaryCertificateRepository.findAllFilterByStatus(id, convertedStatus, pageable).map(salaryCertificateMapper::toDto);
    }

    @Override
    public Page<SalaryCertificateDTO> findAll(String searchText, CertificateStatus status, Integer selectedYear, Pageable pageable) {
        log.debug("Request to get all SalaryCertificates");

        Status convertedStatus = null;
        if (status != null && status.equals(CertificateStatus.SENT_FOR_GENERATION)) {
            convertedStatus = Status.PENDING;
        } else if (status != null && status.equals(CertificateStatus.GENERATED)) {
            convertedStatus = Status.APPROVED;
        }

        return salaryCertificateRepository
            .findAllUsingFilter(searchText, convertedStatus, selectedYear, pageable)
            .map(salaryCertificateMapper::toDto);
    }

    @Override
    public boolean isReferenceNumberUnique(String referenceNumber) {
        return salaryCertificateRepository.isEmployeeNocReferenceNumberUnique(referenceNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SalaryCertificateDTO> findOne(Long id) {
        log.debug("Request to get SalaryCertificate : {}", id);
        return salaryCertificateRepository.findById(id).map(salaryCertificateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SalaryCertificate : {}", id);
        salaryCertificateRepository.deleteById(id);
    }

    @Override
    public List<EmployeeSalaryDTO> getSalariesForDropDown(Long id) {
        List<EmployeeSalaryDTO> employeeSalaryDTOList = new ArrayList<>();
        List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.findAllByEmployeeId(id);

        if (employeeSalaryList.size() > 1) {
            employeeSalaryList.sort((s1, s2) -> {
                if (s1.getYear() - s2.getYear() == 0) {
                    return Month.fromEnum(s2.getMonth()) - Month.fromEnum(s1.getMonth());
                } else {
                    return s2.getYear() - s1.getYear();
                }
            });
        }

        List<SalaryCertificate> existingSalaryCertificateList = salaryCertificateRepository.findApprovedSalaryCertificatesByEmployeeId(id);

//        for(int i=0; i<existingSalaryCertificateList.size(); i++){
//            for(int j=0; j<employeeSalaryList.size(); j++){
//                boolean isMonthSimilar = employeeSalaryList.get(j).getMonth().equals(existingSalaryCertificateList.get(i).getMonth());
//                boolean isYearSimilar = employeeSalaryList.get(j).getYear().equals(existingSalaryCertificateList.get(i).getYear());
//
//                if(isYearSimilar && isMonthSimilar){
//                    employeeSalaryList.remove(employeeSalaryList.get(j));
//                }
//            }
//        }

        //            List<EmployeeSalary> employeeSalaryList = employeeSalaryRepository.getEligibleSalariesForSalaryCertificate(pin.get());
        employeeSalaryDTOList = employeeSalaryMapper.toDto(employeeSalaryList);
        return employeeSalaryDTOList;
    }

    @Override
    public Optional<EmployeeSalaryDTO> getSalaryForSalaryCertificates(Long id) {
        Optional<SalaryCertificateDTO> salaryCertificateDTOOptional = findOne(id);

        if (!salaryCertificateDTOOptional.isPresent()) {
            Optional.empty();
        }
        Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryRepository.findEmployeeSalaryByEmployeeIdYearAndMonth(
            salaryCertificateDTOOptional.get().getEmployeeId(),
            salaryCertificateDTOOptional.get().getYear(),
            salaryCertificateDTOOptional.get().getMonth()
        );

        if (!employeeSalaryOptional.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(employeeSalaryMapper.toDto(employeeSalaryOptional.get()));
    }

    @Override
    public Optional<SalaryCertificateDTO> getSalaryCertificateById(Long id) {
        Optional<SalaryCertificateDTO> salaryCertificateDTO = findOne(id);

        if (!salaryCertificateDTO.isPresent()) {
            throw new RuntimeException();
        }

        Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryRepository.findEmployeeSalaryByEmployeeIdYearAndMonth(
            salaryCertificateDTO.get().getEmployeeId(),
            salaryCertificateDTO.get().getYear(),
            salaryCertificateDTO.get().getMonth()
        );
        if (!employeeSalaryOptional.isPresent()) {
            throw new RuntimeException();
        }
        salaryCertificateDTO.get().setSalaryId(employeeSalaryOptional.get().getId());

        return salaryCertificateDTO;
    }

    @Override
    public EmployeeSalaryCertificateReportDTO getSalaryCertificateReportByCertificateId(long id) {
        Optional<SalaryCertificate> salaryCertificateOptional = salaryCertificateRepository.findById(id);

        if (!salaryCertificateOptional.isPresent()) {
            throw new BadRequestAlertException("SalaryCertificate not found!", "SalaryCertificate", "notFound");
        }
        if (salaryCertificateOptional.get().getStatus() != Status.APPROVED) {
            throw new BadRequestAlertException("Report Not Found!", "SalaryCertificate", "internalServerError");
        }

        Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryRepository.findEmployeeSalaryByEmployeeIdYearAndMonth(
            salaryCertificateOptional.get().getEmployee().getId(),
            salaryCertificateOptional.get().getYear(),
            salaryCertificateOptional.get().getMonth()
        );

        if (!employeeSalaryOptional.isPresent()) {
            throw new BadRequestAlertException("Salary not found!", "Employee Salary", "notFound");
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
        double livingAllowance = employeeSalaryOptional.get().getAllowance01() != null ? employeeSalaryOptional.get().getAllowance01() : 0d;
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
        double totalPayable = grossPay + livingAllowance;

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
        report.setSalutation(employeeSalaryOptional.get().getEmployee().getGender() == Gender.MALE ? "Mr." : "Ms.");
        report.setPin(employeeSalaryOptional.get().getPin());
        report.setJoiningDate(employeeSalaryOptional.get().getJoiningDate());
        report.setConfirmationDate(employeeSalaryOptional.get().getConfirmationDate());
        report.setEmployeeCategory(employeeSalaryOptional.get().getEmployeeCategory());
        report.setDesignation(employeeSalaryOptional.get().getEmployee().getDesignation().getDesignationName());
        report.setUnit(employeeSalaryOptional.get().getEmployee().getUnit().getUnitName());
        report.setDepartment(employeeSalaryOptional.get().getEmployee().getDepartment().getDepartmentName());
        report.setMonth(employeeSalaryOptional.get().getMonth());
        report.setYear(employeeSalaryOptional.get().getYear());

        report.setPayableGrossBasicSalary(basic);
        report.setPayableGrossHouseRent(houseRent);
        report.setPayableGrossMedicalAllowance(medicalAllowance);
        report.setPayableGrossConveyanceAllowance(conveyanceAllowance);
        report.setLivingAllowance(livingAllowance);

        report.setEntertainment(entertainment);
        report.setUtility(utility);
        report.setOtherAddition(otherAddition);
        report.setPayableGrossSalary(totalPayable);

        report.setPfDeduction(pfDeduction);
        report.setTaxDeduction(incomeTax);
        report.setMobileBillDeduction(mobileBill);
        report.setWelfareFundDeduction(welfareFund);
        report.setOtherDeduction(otherDeduction);
        report.setTotalDeduction(totalDeduction);

        report.setNetPay(netPayable);
        report.setNetPayInWords(NumberToWord.convertNumberToWord((long) totalPayable));

        report.setSignatoryPersonId(salaryCertificateOptional.get().getSignatoryPerson().getId());
        report.setSignatoryPersonName(salaryCertificateOptional.get().getSignatoryPerson().getFullName());
        report.setSignatoryPersonDesignation(salaryCertificateOptional.get().getSignatoryPerson().getDesignation().getDesignationName());
        report.setReferenceNumber(salaryCertificateOptional.get().getReferenceNumber());

        return report;
    }

    @Override
    public boolean isSalaryCertificateExistsForEmployee(SalaryCertificateDTO salaryCertificateDTO){
        return salaryCertificateRepository.findSalaryCertificateByEmployeeIdYearAndMonth(salaryCertificateDTO.getEmployeeId(), salaryCertificateDTO.getYear(), salaryCertificateDTO.getMonth()).isPresent();
    }
}
