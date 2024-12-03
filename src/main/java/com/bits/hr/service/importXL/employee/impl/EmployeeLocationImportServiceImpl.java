package com.bits.hr.service.importXL.employee.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Location;
import com.bits.hr.domain.enumeration.LocationType;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.LocationRepository;
import com.bits.hr.repository.UserRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EventLog.EventLoggingPublisher;
import com.bits.hr.service.LocationService;
import com.bits.hr.service.config.CurrentEmployeeService;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.dto.LocationDTO;
import com.bits.hr.service.importXL.GenericUploadServiceImpl;
import com.bits.hr.service.importXL.XLImportCommonService;
import com.bits.hr.service.mapper.EmployeeMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmployeeLocationImportServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private GenericUploadServiceImpl genericUploadService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private CurrentEmployeeService currentEmployeeService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EventLoggingPublisher eventLoggingPublisher;

    public boolean importFile(MultipartFile file) {
        try {
            // 1. take the data
            List<ArrayList<String>> data = genericUploadService.upload(file);

            // 2. remove xlsx headers
            ArrayList<String> header = data.remove(0);

            // 3. insert data into object

            for (ArrayList<String> dt : data) {
                // if empty row skip
                if (!XLImportCommonService.isXLRowValid(dt)) {
                    continue;
                }

                String pin = dt.get(1).trim().replaceFirst("[.]0*", "");

                Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin);

                if (!employeeOptional.isPresent()) {
                    continue;
                }

                Employee employee = employeeOptional.get();

                LocationDTO location = new LocationDTO();

                for (int i = 3; i < dt.size(); i++) {
                    location.setLocationName(dt.get(i));
                    location.setLocationCode(dt.get(i));

                    location.setParentId(location.getId());

                    location.setLocationType(LocationType.valueOf(header.get(i)));

                    location.setId(null);

                    Optional<Location> locationOptional = locationRepository.findLocation(
                        location.getLocationName(),
                        location.getLocationCode(),
                        location.getParentId()
                    );

                    Long finalLocationId;

                    if (locationOptional.isPresent()) {
                        finalLocationId = locationOptional.get().getId();
                    } else {
                        finalLocationId = locationService.save(location).getId();
                    }

                    location.setId(finalLocationId);

                    EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
                    employeeDTO.setOfficeLocationId(finalLocationId);
                    employeeService.update(employeeDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
