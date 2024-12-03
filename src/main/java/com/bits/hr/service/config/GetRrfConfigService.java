package com.bits.hr.service.config;

import com.bits.hr.domain.Config;
import com.bits.hr.domain.Department;
import com.bits.hr.domain.Employee;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.ConfigRepository;
import com.bits.hr.repository.DepartmentRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.DepartmentService;
import com.bits.hr.service.config.DTO.RRFApprovalDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class GetRrfConfigService {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;


    public RRFApprovalDTO getRRFApprovalFlow(Employee requester, Long requestedDepartmentId) {
        try {
            RRFApprovalDTO rrfApprovalDTO = new RRFApprovalDTO();
            rrfApprovalDTO.setRequester(requester);

            // Loading Approval flow Config ........................................................

            Config config = configRepository.findConfigByKey(DefinedKeys.rrf_approval_flow).get();
            //  0 -> LM1
            //  1 -> Head Of Department
            //  2 -> CTO(If Sdlc) NA
            //  3 -> HoHr(1981)
            //  4 -> CEO (1900)
            String[] approvalFlow = config.getValue().split(",");

            Optional<Employee> cto = Optional.empty();
            if (!approvalFlow[2].trim().equals("NA")) {
                cto = getEmployeeByPin(approvalFlow[2]);
            }

            Optional<Employee> hoHR = getEmployeeByPin(approvalFlow[3]);
            Optional<Employee> recommendedByCEO = getEmployeeByPin(approvalFlow[4]);

            // getting HOD .............................................

            Department requestedDepartment = departmentRepository.findById(requestedDepartmentId).get();

//            Optional<Employee> hoDOptional = departmentService.getHeadOfDepartment(requestedDepartment.getId());
            Optional<Employee> hoDOptional = getHodForRequester(requester, recommendedByCEO.get());

            if (!hoDOptional.isPresent()) {
                throw new RuntimeException("Department head not found for department id: " + requestedDepartment.getId());
            }
            Employee hoD = hoDOptional.get(); // every department must have head of department otherwise RRF can't be raised for that particular department

            Employee lineManager = requester.getReportingTo(); // lm can be null for ceo

            // loading constant flow ...............................................................
            rrfApprovalDTO.setRecommendedByHoD(hoD);
            rrfApprovalDTO.setRecommendedByHoHR(hoHR.get());
            rrfApprovalDTO.setRecommendedByCEO(recommendedByCEO.get());

            // CTO will be on approval authority only in SDLC process
            if (cto.isPresent() && isInSuperOrdinateTrail(requester, cto.get())) {
                rrfApprovalDTO.setRecommendedByCTO(cto.get());
            } else {
                rrfApprovalDTO.setRecommendedByCTO(null);
            }


            // if requester = HoD
            // then LM1 = null, HoD = requester and approved by default
            // if requester LM1 = HoD,
            // then LM1 = null, no auto approval from HoD
            if (
                requester.getId().equals(hoD.getId()) || // if requester = HoD
                    requester.getReportingTo().getId().equals(hoD.getId()) || // if requester LM1 = HoD
                    requester.getId().equals(recommendedByCEO.get().getId()) || // if requester = CEO
                    (cto.isPresent() && requester.getId().equals(cto.get().getId()))
            ) {
                rrfApprovalDTO.setRecommendedByLM(null);
                // todo: approve by HoD when Requester equals HoD in upper service logic
            } else {
                rrfApprovalDTO.setRecommendedByLM(lineManager);
            }
            return rrfApprovalDTO;
        } catch (
            Exception ex) {
            log.error(ex);
            return new RRFApprovalDTO();
        }
    }


    private Optional<Employee> getEmployeeByPin(String pin) {
        try {
            return employeeRepository.findEmployeeByPin(pin.trim());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    private boolean isInSuperOrdinateTrail(Employee requester, Employee superordinateEmployee) {
        List<Employee> superOrdinateTrail = new ArrayList<>();
        superOrdinateTrail.add(requester);
        Employee tmpEmp = requester;
        while (true) {
            if (tmpEmp.getReportingTo() != null &&
                tmpEmp.getId() != tmpEmp.getReportingTo().getId()) {

                Employee employee = employeeRepository.findById(tmpEmp.getReportingTo().getId()).get();
                tmpEmp = employee;
                if (employee.getId().equals(superordinateEmployee.getId())) {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }

    private Optional<Employee> getHodForRequester(Employee employee, Employee ceo){
        int count = 0;
        while (count < 20 ){
            if (employee.getReportingTo().equals(ceo)){
                return Optional.of(employee);
            }
            else if(employee.getReportingTo().equals(null)) {
                return Optional.of(ceo);
            }
            else {
                employee = employee.getReportingTo();
                count++;
            }
        }
        return Optional.empty();
    }

}
