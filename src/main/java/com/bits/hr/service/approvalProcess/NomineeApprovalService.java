package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Nominee;
import com.bits.hr.domain.enumeration.NomineeType;
import com.bits.hr.domain.enumeration.Status;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.NomineeRepository;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.NomineeDTO;
import com.bits.hr.service.mapper.NomineeMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NomineeApprovalService {

    @Autowired
    private NomineeRepository nomineeRepository;

    @Autowired
    private NomineeMapper nomineeMapper;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<NomineeDTO> getAllByNomineeTypeAndStatus(NomineeType nomineeType, Status status) {
        List<Nominee> nomineeList = nomineeRepository.getAllByNomineeTypeAndStatus(nomineeType, status);
        return nomineeMapper.toDto(nomineeList);
    }

    public Page<NomineeDTO> getAllApprovedOrPendingNominees(String searchText, NomineeType nomineeType, Status status, Pageable pageable) {
        Page<Nominee> oneNomineeDTOList = null;
        List<NomineeDTO> finalNomineeDTOList = new ArrayList<>();
        if (!Objects.equals(searchText, "")) {
            List<Employee> employees = employeeRepository.searchAllByFullNameAndPin(searchText);
            if (employees.size() > 0) {
                for (Employee employee : employees) {
                    oneNomineeDTOList = nomineeRepository.getAllApprovedOrPendingNominees(employee.getId(), nomineeType, status, pageable);
                    List<NomineeDTO> nomineeDTOS = nomineeMapper.toDto(oneNomineeDTOList.getContent());
                    finalNomineeDTOList.addAll(nomineeDTOS);
                }
            } else {
                return new PageImpl<>(finalNomineeDTOList, pageable, 0);
            }
        } else {
            oneNomineeDTOList = nomineeRepository.getAllNomineesByNomineeTypeAndStatus(nomineeType, status, pageable);
            List<NomineeDTO> nomineeDTOS = nomineeMapper.toDto(oneNomineeDTOList.getContent());
            finalNomineeDTOList.addAll(nomineeDTOS);
        }

        return new PageImpl<>(finalNomineeDTOList, pageable, oneNomineeDTOList.getTotalElements());
    }

    public Boolean approveSelected(ApprovalDTO approvalDTO) {
        Optional<Employee> employee = currentEmployeeService.getCurrentEmployee();

        try {
            approvalDTO
                .getListOfIds()
                .stream()
                .forEach(pfNomineeId -> {
                    Nominee nominee = nomineeRepository.findById(pfNomineeId).get();
                    nominee.setApprovedBy(currentEmployeeService.getCurrentEmployee().get());
                    nominee.setIsLocked(true);
                    if (employee.isPresent()) {
                        nominee.setApprovedBy(employee.get());
                    }
                    nominee.setStatus(Status.APPROVED);
                    nomineeRepository.save(nominee);
                });
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public Boolean rejectSelected(ApprovalDTO approvalDTO) {
        try {
            approvalDTO
                .getListOfIds()
                .stream()
                .forEach(pfNomineeId -> {
                    Nominee nominee = nomineeRepository.findById(pfNomineeId).get();
                    nominee.setApprovedBy(null);
                    nominee.setIsLocked(false);
                    nominee.setStatus(Status.PENDING);
                    nomineeRepository.save(nominee);
                });
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
