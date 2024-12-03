package com.bits.hr.service.workFromHomeApplication;

import com.bits.hr.domain.Employee;
import com.bits.hr.service.dto.WorkFromHomeApplicationDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkFromHomeApplicationLMService {
    Page<WorkFromHomeApplicationDTO> getAllSubOrdinateApplications(String searchText, Employee employee, Pageable pageable);

    Page<WorkFromHomeApplicationDTO> getAllPendingSubOrdinateApplications(long employeeId, String searchText, Pageable pageable);

    boolean enableSelectedLM(List<Long> selectedIds, long currentEmployeeId);

    boolean disableSelectedLM(List<Long> selectedIds, long currentEmployeeId);
}
