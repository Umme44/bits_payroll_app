package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.IndividualArrearSalary;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.IndividualArrearSalaryRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.IndividualArrearSalaryService;
import com.bits.hr.service.dto.IndividualArrearPayslipDTO;
import com.bits.hr.service.dto.IndividualArrearSalaryDTO;
import com.bits.hr.service.dto.IndividualArrearSalaryGroupDataDTO;
import com.bits.hr.service.mapper.IndividualArrearSalaryMapper;
import com.bits.hr.service.selecteable.SelectableDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link IndividualArrearSalary}.
 */
@Service
@Transactional
public class IndividualArrearSalaryServiceImpl implements IndividualArrearSalaryService {

    private final Logger log = LoggerFactory.getLogger(IndividualArrearSalaryServiceImpl.class);

    private final IndividualArrearSalaryRepository individualArrearSalaryRepository;

    private final IndividualArrearSalaryMapper individualArrearSalaryMapper;

    private final EmployeeService employeeService;

    public IndividualArrearSalaryServiceImpl(
        IndividualArrearSalaryRepository individualArrearSalaryRepository,
        IndividualArrearSalaryMapper individualArrearSalaryMapper,
        EmployeeService employeeService
    ) {
        this.individualArrearSalaryRepository = individualArrearSalaryRepository;
        this.individualArrearSalaryMapper = individualArrearSalaryMapper;
        this.employeeService = employeeService;
    }

    @Override
    public IndividualArrearSalaryDTO save(IndividualArrearSalaryDTO individualArrearSalaryDTO) {
        log.debug("Request to save IndividualArrearSalary : {}", individualArrearSalaryDTO);
        IndividualArrearSalary individualArrearSalary = individualArrearSalaryMapper.toEntity(individualArrearSalaryDTO);
        individualArrearSalary = individualArrearSalaryRepository.save(individualArrearSalary);
        return individualArrearSalaryMapper.toDto(individualArrearSalary);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IndividualArrearSalaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IndividualArrearSalaries");
        return individualArrearSalaryRepository.findAll(pageable).map(individualArrearSalaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IndividualArrearSalaryDTO> findOne(Long id) {
        log.debug("Request to get IndividualArrearSalary : {}", id);
        return individualArrearSalaryRepository.findById(id).map(individualArrearSalaryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IndividualArrearSalary : {}", id);
        individualArrearSalaryRepository.deleteById(id);
    }

    public List<IndividualArrearSalaryGroupDataDTO> getGroupTitles() {
        List<Object[]> resultList = individualArrearSalaryRepository.getListGroupByTitle();
        List<String> titles = new ArrayList<>();

        List<IndividualArrearSalaryGroupDataDTO> dtoList = new ArrayList<>();
        for (Object[] objList : resultList) {
            if (objList == null) {
                continue;
            }
            IndividualArrearSalaryGroupDataDTO dto = new IndividualArrearSalaryGroupDataDTO();

            dto.setTitle(objList[0].toString());
            dto.setEffectiveDate(LocalDate.parse(objList[1].toString()));
            dto.setEffectiveFrom(LocalDate.parse(objList[2].toString()));

            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public Page<IndividualArrearSalaryDTO> getAllByTitle(Pageable pageable, String title) {
        return individualArrearSalaryRepository.getAllByTitle(pageable, title).map(individualArrearSalaryMapper::toDto);
    }

    @Override
    public List<Integer> getListOfDisbursedArrearSalaryYears(Employee employee) {
        return individualArrearSalaryRepository.getListOfDisbursedSalaryYears(employee.getId());
    }

    @Override
    public List<SelectableDTO> getListOfArrearSalaryTitleByEmployeeIdAndYear(Employee employee, Integer year) {
        List<String> titles = individualArrearSalaryRepository.getListOfArrearSalaryTitleByEmployeeIdAndYear(employee.getId(), year);

        List<SelectableDTO> selectableDTOList = new ArrayList<>();

        int i = 0;
        for (String title : titles) {
            selectableDTOList.add(new SelectableDTO(i, title));
            i++;
        }

        return selectableDTOList;
    }

    @Override
    public IndividualArrearPayslipDTO getByEmployeeAndTitle(Employee employee, String title) {
        Optional<IndividualArrearSalary> resultList = individualArrearSalaryRepository.getByEmployeeIdAndTitle(employee.getId(), title);

        if (!resultList.isPresent()) {
            throw new BadRequestAlertException("No arrear salary with provided title found.", "IndividualArrearSalary", "");
        }

        IndividualArrearSalaryDTO arrearSalaryDTO = individualArrearSalaryMapper.toDto(resultList.get());

        IndividualArrearPayslipDTO payslipDTO = new IndividualArrearPayslipDTO();
        payslipDTO.setId(arrearSalaryDTO.getId());
        payslipDTO.setEffectiveDate(arrearSalaryDTO.getEffectiveDate());
        payslipDTO.setTitle(arrearSalaryDTO.getTitle());
        payslipDTO.setTitleEffectiveFrom(arrearSalaryDTO.getTitleEffectiveFrom());
        payslipDTO.setArrearRemarks(arrearSalaryDTO.getArrearRemarks());

        payslipDTO.setPin(arrearSalaryDTO.getPin());
        payslipDTO.setFullName(arrearSalaryDTO.getFullName());
        payslipDTO.setDesignationName(arrearSalaryDTO.getDesignationName());
        payslipDTO.setDepartmentName(arrearSalaryDTO.getDepartmentName());
        payslipDTO.setUnitName(arrearSalaryDTO.getUnitName());
        payslipDTO.setJoiningDate(employee.getDateOfJoining());
        payslipDTO.setBankName(employee.getBankName());
        payslipDTO.setBankAccountNo(employee.getBankAccountNo());

        payslipDTO.setBasic(arrearSalaryDTO.getArrearSalary() * 0.6);
        payslipDTO.setHouseRent(arrearSalaryDTO.getArrearSalary() * 0.3);
        payslipDTO.setMedical(arrearSalaryDTO.getArrearSalary() * 0.06);
        payslipDTO.setConveyance(arrearSalaryDTO.getArrearSalary() * 0.04);

        payslipDTO.setGrossPay(arrearSalaryDTO.getArrearSalary());
        payslipDTO.setFestivalBonus(Optional.of(arrearSalaryDTO.getFestivalBonus()).orElse(0.0));
        payslipDTO.setLivingAllowance(0.0);
        payslipDTO.setOtherAddition(0.0);
        payslipDTO.setSalaryAdjustment(0.0);

        payslipDTO.setTaxDeduction(arrearSalaryDTO.getTaxDeduction());
        payslipDTO.setArrearPfDeduction(arrearSalaryDTO.getArrearPfDeduction());

        payslipDTO.setTotalAddition(arrearSalaryDTO.getArrearSalary() + payslipDTO.getFestivalBonus());

        payslipDTO.setTotalDeduction(arrearSalaryDTO.getTaxDeduction() + arrearSalaryDTO.getArrearPfDeduction());

        payslipDTO.setNetPayable(payslipDTO.getTotalAddition() - payslipDTO.getTotalDeduction());

        return payslipDTO;
    }
}
