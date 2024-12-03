package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmployeeStaticFile;
import com.bits.hr.domain.InsuranceRegistration;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.InsuranceRegistrationRepository;
import com.bits.hr.service.FileService;
import com.bits.hr.service.ImportEmployeeImageUploadService;
import com.bits.hr.service.dto.EmployeeImageUploadDTO;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import com.bits.hr.service.dto.EmployeeStaticFileDTO;
import com.bits.hr.service.dto.LeaveApplicationDTO;
import com.bits.hr.service.event.EmployeeImageUpdateEvent;
import com.bits.hr.service.event.EventType;
import com.bits.hr.service.event.LeaveApplicationEvent;
import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.mapper.EmployeeMinimalMapper;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImportEmployeeImageUploadServiceImpl implements ImportEmployeeImageUploadService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMinimalMapper employeeMinimalMapper;

    @Autowired
    private FileOperationService fileOperationService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private InsuranceRegistrationRepository insuranceRegistrationRepository;

    private String folderPath;

    @Autowired
    private FileService fileService;

    @Override
    public Boolean imageBatchSave(MultipartFile[] files) {
        try {
            boolean isSavedFile = false;
            for (MultipartFile multipartFile : files) {
                if (!Objects.requireNonNull(multipartFile.getContentType()).contains("image/")) {
                    continue;
                }

                String fileNameWithOutExtension = Objects
                    .requireNonNull(multipartFile.getOriginalFilename())
                    .substring(0, multipartFile.getOriginalFilename().indexOf("."));
                Optional<Employee> employeeOptional = employeeRepository.findByPin(fileNameWithOutExtension);
                Employee employee;

                if (employeeOptional.isPresent()) {
                    employee = employeeOptional.get();
                } else {
                    continue;
                }
                String fileName = fileService.save(multipartFile, employee.getPin());
                employee.setPicture(fileName);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);

                isSavedFile = true;

                updateImageInInsuranceRegistration(employee);
            }

            return isSavedFile;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public void delete(String pin) {
        Optional<Employee> employee = employeeRepository.findEmployeeByPin(pin);
        if (employee.isPresent()) {
            String fileName = employee.get().getPicture();
            fileOperationService.delete(fileName);
            employee.get().setPicture(null);
            employeeRepository.save(employee.get());
        }
    }

    @Override
    public Page<EmployeeImageUploadDTO> findAllImages(Pageable pageable, String searchText) throws IOException {
        Page<Employee> employees = employeeRepository.findAllEmployeesByPinOrFullName(searchText, pageable);
        List<EmployeeImageUploadDTO> employeeImageUploadDTOList = new ArrayList<>();

        for (Employee employeeMinimal : employees) {
            EmployeeImageUploadDTO employeeImageUploadDTO = new EmployeeImageUploadDTO();
            Path absolutePath;
            employeeImageUploadDTO.setPin(employeeMinimal.getPin());
            employeeImageUploadDTO.setDateOfJoining(employeeMinimal.getDateOfJoining());
            employeeImageUploadDTO.setDesignationName(employeeMinimal.getDesignation().getDesignationName());
            employeeImageUploadDTO.setFullName(employeeMinimal.getFullName());
            employeeImageUploadDTO.setEmployeeId(employeeMinimal.getId());

            //fetching employee's image name & process file absolute path with image name
            if (employeeMinimal.getPicture() != null) {
                absolutePath = fileService.load(employeeMinimal.getPicture());
            } else {
                continue;
            }

            // process image byteStream for frontend with absolute path
            employeeImageUploadDTO.setGetByteStreamFromFilePath(fileOperationService.loadAsByte(absolutePath.toString()));
            employeeImageUploadDTOList.add(employeeImageUploadDTO);
        }

        return new PageImpl<>(employeeImageUploadDTOList, pageable, employees.getTotalElements());
    }

    @Override
    public Page<EmployeeImageUploadDTO> findAllImages(Pageable pageable) throws IOException {
        Page<Employee> employees = employeeRepository.findAll(pageable);
        List<EmployeeImageUploadDTO> employeeImageUploadDTOList = new ArrayList<>();

        for (Employee employeeMinimal : employees) {
            EmployeeImageUploadDTO employeeImageUploadDTO = new EmployeeImageUploadDTO();
            Path absolutePath;
            employeeImageUploadDTO.setPin(employeeMinimal.getPin());
            employeeImageUploadDTO.setDateOfJoining(employeeMinimal.getDateOfJoining());
            employeeImageUploadDTO.setDesignationName(employeeMinimal.getDesignation().getDesignationName());
            employeeImageUploadDTO.setFullName(employeeMinimal.getFullName());
            employeeImageUploadDTO.setEmployeeId(employeeMinimal.getId());

            //fetching employee's image name & process file absolute path with image name
            if (employeeMinimal.getPicture() != null) {
                absolutePath = fileService.load(employeeMinimal.getPicture());
            } else {
                continue;
            }

            // process image byteStream for frontend with absolute path
            employeeImageUploadDTO.setGetByteStreamFromFilePath(fileOperationService.loadAsByte(absolutePath.toString()));
            employeeImageUploadDTOList.add(employeeImageUploadDTO);
        }

        return new PageImpl<>(employeeImageUploadDTOList, pageable, employees.getTotalElements());
    }

    @Override
    public EmployeeImageUploadDTO updateImage(long id, MultipartFile file) {
        Optional<Employee> employee = employeeRepository.findById(id);
        EmployeeImageUploadDTO employeeImageUploadDTO = new EmployeeImageUploadDTO();

        if (employee.isPresent()) {
            if (file.getContentType().contains("image/")) {
                String fileName = fileService.save(file, employee.get().getPin());
                employee.get().setPicture(fileName);
                employeeRepository.save(employee.get());

                updateImageInInsuranceRegistration(employee.get());

                employeeImageUploadDTO.setEmployeeId(employee.get().getId());
                employeeImageUploadDTO.setFullName(employee.get().getFullName());
                employeeImageUploadDTO.setDesignationName(employee.get().getDesignation().getDesignationName());
                employeeImageUploadDTO.setDateOfJoining(employee.get().getDateOfJoining());
                employeeImageUploadDTO.setPin(employee.get().getPin());
            }
        }

        return employeeImageUploadDTO;
    }

    private void updateImageInInsuranceRegistration(Employee employee) {
        List<InsuranceRegistration> insuranceRegistrationList = insuranceRegistrationRepository.findSelfRegistrationByEmployeeId(
            employee.getId()
        );

        if (insuranceRegistrationList.size() > 0) {
            publishEvent(employee, EventType.CREATED);
        }
    }

    private void publishEvent(Employee employee, EventType event) {
        EmployeeImageUpdateEvent employeeImageUpdateEvent = new EmployeeImageUpdateEvent(this, employee, event);
        applicationEventPublisher.publishEvent(employeeImageUpdateEvent);
    }
}
