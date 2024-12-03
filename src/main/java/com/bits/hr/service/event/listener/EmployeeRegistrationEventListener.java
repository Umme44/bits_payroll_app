package com.bits.hr.service.event.listener;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.User;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.UserRepository;
import com.bits.hr.service.event.EmployeeRegistrationEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("ldap")
public class EmployeeRegistrationEventListener {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeRegistrationEventListener.class);

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeRegistrationEventListener(UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    @EventListener
    public void mapUserEmployee(EmployeeRegistrationEvent event) {
        Optional<User> userOptional = userRepository.findOneByEmailIgnoreCase(event.getOfficialEmail());
        if (!userOptional.isPresent()) return;
        User user = userOptional.get();
        detachPreviousUserFormOldEmployeeProfile(user, event.getPin());
        Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(event.getPin());
        if (!employeeOptional.isPresent()) return;
        Employee employee = employeeOptional.get();
        employee.setUser(user);
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }

    public void detachPreviousUserFormOldEmployeeProfile(User user, String pin) {
        List<Employee> employeeList = employeeRepository.findAllByUserIdExceptPin(user.getId(), pin);
        for (Employee employee : employeeList) {
            employee.setUser(null);
            employee.setUpdatedAt(LocalDateTime.now());
            employeeRepository.save(employee);
        }
    }
}
