package com.bits.hr.service;

import com.bits.hr.domain.EmployeePinConfiguration;
import com.bits.hr.domain.User;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.service.dto.EmployeePinConfigurationDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bits.hr.domain.EmployeePinConfiguration}.
 */
public interface EmployeePinConfigurationService {
    EmployeePinConfigurationDTO create(EmployeePinConfigurationDTO employeePinConfigurationDTO, User user);

    EmployeePinConfigurationDTO update(EmployeePinConfigurationDTO employeePinConfigurationDTO, User user);

    Page<EmployeePinConfigurationDTO> findAll(Pageable pageable);

    Optional<EmployeePinConfigurationDTO> findOne(Long id);

    List<EmployeePinConfigurationDTO> findByEmployeeCategory(EmployeeCategory employeeCategory);

    void delete(Long id);

    boolean isPinSequenceUnique(String startingPin, String endingPin, Long pinConfigurationId);

    boolean isPinSequenceFullFilled(EmployeeCategory category);

    //    Optional<String> getLastCreatedPinFromTheSequence(EmployeeCategory category);

    String getTheLastPinFromTheSequence(EmployeePinConfiguration config);

    String getTheLastCreatedPinFromTheSequence(EmployeePinConfiguration config);

    boolean isTheSequenceFullFilled(EmployeePinConfiguration config);

    void updatePinConfigurationInformation(EmployeePinConfiguration employeePinConfiguration);
}
