package com.bits.hr.web.rest.commonUser;

import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.EmployeeCommonService;
import com.bits.hr.service.EmployeeStaticFileService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.fileService.EmployeeImageService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files/")
public class EmployeeImage {

    @Autowired
    private EmployeeCommonService employeeCommonService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private EmployeeStaticFileService employeeStaticFileService;

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private EmployeeImageService employeeImageService;

    @GetMapping(value = "/get-employees-image/{pin}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable("pin") String pin) throws IOException {
        return employeeImageService.getEmployeeImage(pin);
        //        Path file = employeeCommonService.getPhoto(pin);
        //        if (file == null) {
        //            Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin);
        //            if (employeeOptional.isPresent()) {
        //                if (employeeOptional.get().getGender() == Gender.MALE) {
        //                    return IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/male-avatar.jpg"));
        //                } else if (employeeOptional.get().getGender() == Gender.FEMALE) {
        //                    return IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/female-avatar.jpg"));
        //                } else {
        //                    return IOUtils.toByteArray(getClass().getResourceAsStream("/static/images/generic-avatar.jpg"));
        //
        //                }
        //            }
        //        }
        //        return Files.readAllBytes(file);
    }
}
