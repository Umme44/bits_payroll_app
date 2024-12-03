package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfLoanApplication;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfLoanApplicationRepository;
import com.bits.hr.service.dto.EmployeeBankDetailsDTO;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.PfLoanApplicationDTO;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.service.mapper.PfLoanApplicationMapper;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class PfLoanApplicationHrServiceImpl {

    @Autowired
    PfLoanApplicationRepository pfLoanApplicationRepository;

    @Autowired
    PfLoanApplicationMapper pfLoanApplicationMapper;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeMapper employeeMapper;

    public List<PfLoanApplicationDTO> pendingPfLoanApplication() {
        List<PfLoanApplication> pendingPfLoanApplicationList = pfLoanApplicationRepository.getPendingLoanApplication();
        List<PfLoanApplicationDTO> pendingPfLoanApplicationDTOList = pfLoanApplicationMapper.toDto(pendingPfLoanApplicationList);
        return pendingPfLoanApplicationDTOList;
    }

    public EmployeeBankDetailsDTO getBankDetailsByEmployeePin(String pin) {
        Optional<Employee> employee = employeeRepository.findEmployeeByPin(pin);
        EmployeeBankDetailsDTO employeeBankDetailsDTO = new EmployeeBankDetailsDTO();

        EmployeeDTO employeeDTO = employeeMapper.toDto(employee.get());
        employeeBankDetailsDTO.setBankName(employeeDTO.getBankName());
        employeeBankDetailsDTO.setBankAccountNumber(employeeDTO.getBankAccountNo());
        employeeBankDetailsDTO.setBankBranch(employeeDTO.getBankBranchName());

        return employeeBankDetailsDTO;
    }
}
