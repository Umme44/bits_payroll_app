package com.bits.hr.service.finalSettlement;

import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.dto.EmployeeCommonDTO;
import com.bits.hr.service.mapper.EmployeeCommonMapper;
import com.bits.hr.service.search.FilterDto;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EligibleEmployeeForFinalSettlement {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeCommonMapper employeeCommonMapper;

    public Page<EmployeeCommonDTO> getEligibleEmployeesForFinalSettlement(FilterDto filterDto, Pageable pageable) {
        Page<EmployeeCommonDTO> employeeCommonDTOPage = employeeRepository
            .getPendingFinalSettlement(
                filterDto.getSearchText(),
                "88" + filterDto.getSearchText(),
                "+88" + filterDto.getSearchText(),
                "%" + filterDto.getSearchText() + "%",
                filterDto.getDestinationId(),
                filterDto.getDepartmentId(),
                filterDto.getUnitId(),
                filterDto.getBloodGroup(),
                filterDto.getGender(),
                pageable
            )
            .map(employeeCommonMapper::toDto);

        return employeeCommonDTOPage;
    }

    public Page<EmployeeCommonDTO> getPendingEmployeesForFinalSettlement(FilterDto filterDto, Pageable pageable) {
        Page<EmployeeCommonDTO> employeeCommonDTOPage = employeeRepository
            .getPendingFinalSettlement(
                filterDto.getSearchText(),
                "88" + filterDto.getSearchText(),
                "+88" + filterDto.getSearchText(),
                "%" + filterDto.getSearchText() + "%",
                filterDto.getDestinationId(),
                filterDto.getDepartmentId(),
                filterDto.getUnitId(),
                filterDto.getBloodGroup(),
                filterDto.getGender(),
                pageable
            )
            .map(employeeCommonMapper::toDto);

        return employeeCommonDTOPage;
    }

    public Page<EmployeeCommonDTO> getSettledEmployeesForFinalSettlement(FilterDto filterDto, Pageable pageable) {
        Page<EmployeeCommonDTO> employeeCommonDTOPage = employeeRepository
            .getSettledFinalSettlement(
                filterDto.getSearchText(),
                "88" + filterDto.getSearchText(),
                "+88" + filterDto.getSearchText(),
                "%" + filterDto.getSearchText() + "%",
                filterDto.getDestinationId(),
                filterDto.getDepartmentId(),
                filterDto.getUnitId(),
                filterDto.getBloodGroup(),
                filterDto.getGender(),
                pageable
            )
            .map(employeeCommonMapper::toDto);

        return employeeCommonDTOPage;
    }
}
