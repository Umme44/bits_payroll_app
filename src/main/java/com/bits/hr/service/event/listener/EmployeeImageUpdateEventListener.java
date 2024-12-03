package com.bits.hr.service.event.listener;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.domain.enumeration.InsuranceRelation;
import com.bits.hr.repository.InsuranceRegistrationRepository;
import com.bits.hr.service.event.EmployeeImageUpdateEvent;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.fileService.EmployeeImageService;
import com.bits.hr.service.fileOperations.fileService.InsuranceFileService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class EmployeeImageUpdateEventListener {

    @Autowired
    private InsuranceRegistrationRepository insuranceRegistrationRepository;

    @Autowired
    private InsuranceFileService insuranceFileService;

    @Autowired
    private EmployeeImageService employeeImageService;

    @Autowired
    private FileOperationService fileOperationService;

    @EventListener
    public void handleEmployeeImageUpdateEvent(EmployeeImageUpdateEvent event) throws IOException {
        Employee employee = event.getEmployee();

        List<InsuranceRegistration> insuranceRegistrations = insuranceRegistrationRepository.findAllInsuranceRegistrationByEmployeeId(
            employee.getId()
        );

        Optional<InsuranceRegistration> insuranceRegistrationOptional = insuranceRegistrations
            .stream()
            .filter(ir -> ir.getInsuranceRelation().equals(InsuranceRelation.SELF))
            .findFirst();

        if (insuranceRegistrationOptional.isPresent()) {
            boolean isExist = fileOperationService.isExist(insuranceRegistrationOptional.get().getPhoto());
            if (isExist) {
                fileOperationService.delete(insuranceRegistrationOptional.get().getPhoto());
            }
            byte[] employeeImage = employeeImageService.getEmployeeImage(employee.getPin());
            File savedFile = insuranceFileService.saveAsByteArray(employeeImage);
            insuranceRegistrationOptional.get().setPhoto(savedFile.getAbsolutePath());
            insuranceRegistrationRepository.save(insuranceRegistrationOptional.get());
        }
    }
}
