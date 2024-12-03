package com.bits.hr.service.fileOperations.fileService;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.enumeration.Gender;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EmployeeCommonService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EmployeeImageService {

    @Autowired
    private EmployeeCommonService employeeCommonService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public byte[] getEmployeeImage(String pin) throws IOException {
        try {
            Path imgPath = employeeCommonService.getPhoto(pin);
            if (imgPath == null) {
                return getPlaceHolderImage(pin);
            }
            byte[] img = Files.readAllBytes(imgPath);
            return img;
        } catch (Exception e) {
            log.error(e);
            return getPlaceHolderImage(pin);
        }
    }

    private byte[] getPlaceHolderImage(String pin) throws IOException {
        Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin);
        if (employeeOptional.isPresent()) {
            if (employeeOptional.get().getGender() == Gender.MALE) {
                return IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/male-avatar.jpg"));
            } else if (employeeOptional.get().getGender() == Gender.FEMALE) {
                return IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/female-avatar.jpg"));
            } else {
                return IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/generic-avatar.jpg"));
            }
        }
        return null;
    }
}
