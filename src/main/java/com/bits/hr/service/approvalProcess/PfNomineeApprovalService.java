package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.PfNominee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfNomineeRepository;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.ApprovalDTO;
import com.bits.hr.service.dto.PfNomineeDTO;
import com.bits.hr.service.mapper.PfNomineeMapper;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PfNomineeApprovalService {

    @Autowired
    private PfNomineeRepository pfNomineeRepository;

    @Autowired
    private PfNomineeMapper pfNomineeMapper;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<PfNomineeDTO> getPendingPfNomineeList() {
        List<PfNominee> pfNomineeList = pfNomineeRepository.findIsNotApproved();
        List<PfNomineeDTO> pfNomineeDTOList = pfNomineeMapper.toDto(pfNomineeList);
        return pfNomineeDTOList;
    }

    public List<PfNomineeDTO> getApprovedPfNomineeList() {
        List<PfNominee> pfNomineeList = pfNomineeRepository.findIsApproved();
        List<PfNomineeDTO> pfNomineeDTOList = pfNomineeMapper.toDto(pfNomineeList);
        return pfNomineeDTOList;
    }

    @Transactional
    public Boolean approveSelected(ApprovalDTO approvalDTO) {
        try {
            approvalDTO
                .getListOfIds()
                .stream()
                .forEach(pfNomineeId -> {
                    PfNominee pfNominee = pfNomineeRepository.findById(pfNomineeId).get();
                    pfNominee.setApprovedBy(currentEmployeeService.getCurrentEmployee().get());
                    pfNominee.setIsApproved(true);
                    pfNomineeRepository.save(pfNominee);
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
                    PfNominee pfNominee = pfNomineeRepository.findById(pfNomineeId).get();
                    pfNominee.setApprovedBy(currentEmployeeService.getCurrentEmployee().get());
                    pfNominee.setIsApproved(false);
                    pfNomineeRepository.save(pfNominee);
                });
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
