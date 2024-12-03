package com.bits.hr.service.config;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.UserRepository;
import com.bits.hr.security.AuthoritiesConstants;
import com.bits.hr.security.SecurityUtils;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.mapper.EmployeeMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CurrentEmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private UserRepository userRepository;

    public Optional<Employee> getCurrentEmployee() {
        try {
            String pin = SecurityUtils.getCurrentEmployeePin().trim();
            log.info("============ Current Employee Pin :" + pin);
            return Optional.of(employeeRepository.findEmployeeByPin(pin).get());
        } catch (Exception ex) {
            log.error("============ NO EMPLOYEE FOUND ======= ");
            log.error(ex);
            return Optional.empty();
        }
    }

    public Optional<EmployeeDTO> getCurrentEmployeeDTO() {
        try {
            String pin = SecurityUtils.getCurrentEmployeePin();
            EmployeeDTO employeeDTO = employeeMapper.toDto(employeeRepository.findEmployeeByPin(pin).get());
            return Optional.of(employeeDTO);
        } catch (Exception ex) {
            log.error(ex);
            return Optional.empty();
        }
    }

    public Optional<Long> getCurrentEmployeeId() {
        try {
            String pin = SecurityUtils.getCurrentEmployeePin();
            return Optional.of(employeeRepository.getIdByPin(pin));
        } catch (Exception ex) {
            log.error(ex);
            return Optional.empty();
        }
    }

    public Optional<String> getCurrentEmployeePin() {
        try {
            String pin = SecurityUtils.getCurrentEmployeePin();
            return Optional.of(pin);
        } catch (Exception ex) {
            log.debug(ex);
            return Optional.empty();
        }
    }

    public Optional<String> getCurrentUserEmail() {
        try {
            String email = SecurityUtils.getCurrentUserEmail();
            return Optional.of(email);
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Long> getCurrentUserId() {
        try {
            long id = SecurityUtils.getCurrentUserId();
            return Optional.of(id);
        } catch (Exception ex) {
            log.error(ex);
            return Optional.empty();
        }
    }

    public Optional<User> getCurrentUser() {
        try {
            long id = SecurityUtils.getCurrentUserId();
            return userRepository.findById(id);
        } catch (Exception ex) {
            log.error(ex);
            return Optional.empty();
        }
    }

    public boolean hasManagementAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = authentication
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        if (authentication != null) {
            if (authorities.stream().anyMatch("ROLE_ADMIN"::equals)) {
                return true;
            }
            if (authorities.size() > 3) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean hasPRFAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = authentication
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .filter(authority -> authority.equals(AuthoritiesConstants.PROCUREMENT_MANAGEMENT))
            .collect(Collectors.toList());
        return authorities.size() > 0;
    }
}
