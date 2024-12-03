package com.bits.hr.service;

import com.bits.hr.service.dto.EmployeeCommonDTO;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.UserEditAccountDTO;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeCommonService {
    Page<EmployeeCommonDTO> findAll(Pageable pageable);

    List<EmployeeMinimalDTO> findAllMinimal();

    /**
     * find all employee except pending and approved for separation
     * @return List of all employee minimal
     */
    List<EmployeeMinimalDTO> findAllMinimalEmploymentActive();

    Optional<EmployeeCommonDTO> findOne(Long id);

    UserEditAccountDTO update(UserEditAccountDTO userEditAccountDTO);

    String uploadPhoto(MultipartFile file, String pin);

    Path getPhoto(String pin);

    UserEditAccountDTO findEmployeeByPin(String pin);

    Set<String> getSuggestions();
}
