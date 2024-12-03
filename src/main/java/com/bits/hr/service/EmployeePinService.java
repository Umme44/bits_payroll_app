package com.bits.hr.service;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.EmployeePinStatus;
import com.bits.hr.service.dto.EmployeePinDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.EmployeePin}.
 */
public interface EmployeePinService {
    EmployeePinDTO create(EmployeePinDTO employeePinDTO, User user);
    EmployeePinDTO update(EmployeePinDTO employeePinDTO, User user);
    Page<EmployeePinDTO> findAll(String searchText, EmployeeCategory selectedCategory, EmployeePinStatus pinStatus, Pageable pageable);
    Optional<EmployeePinDTO> findOne(Long id);
    Optional<EmployeePinDTO> findOneByPin(String pin);
    void delete(Long id);
    EmployeePinDTO declineEmployeeOnboard(Long id, User user);
    boolean isPinUnique(String pin, Long employeePinId);
    void updateEmployeePinStatus(Employee employee);
    String getThePreviousPin(String pin);

    boolean isEmployeeExistByRRF(String rrfNumber);
}
