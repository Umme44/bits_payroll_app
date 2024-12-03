package com.bits.hr.service.importXL.batchOperations.employmentActions;

import static com.bits.hr.service.importXL.batchOperations.employmentActions.RowConstantsPromotions.*;

import com.bits.hr.domain.Band;
import com.bits.hr.domain.Designation;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmploymentHistory;
import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.repository.BandRepository;
import com.bits.hr.repository.DesignationRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmploymentHistoryRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EmploymentHistoryService;
import com.bits.hr.service.dto.EmploymentHistoryDTO;
import com.bits.hr.service.employmentHistory.PromotionService;
import com.bits.hr.service.mapper.EmployeeMapper;
import com.bits.hr.service.mapper.EmploymentHistoryMapper;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.PinUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Qualifier("Promotions")
public class BatchPromotionsViaXlsxImpl implements BatchOperationsViaXlsx {

    @Autowired
    GenericXlsxImportService genericXlsxImportService;

    @Autowired
    EmploymentHistoryRepository employmentHistoryRepository;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    EmploymentHistoryService employmentHistoryService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    DesignationRepository designationRepository;

    @Autowired
    BandRepository bandRepository;

    @Autowired
    EmploymentHistoryMapper employmentHistoryMapper;

    @Override
    public boolean batchOperations(MultipartFile file) throws Exception {
        try {
            List<ArrayList<String>> data = genericXlsxImportService.importXlsx(file);
            List<String> header = data.remove(0);

            // file validation
            if (header.get(0).trim().compareToIgnoreCase(FIRST_ROW_FIRST_COL.trim()) != 0) {
                return false;
            }

            for (List<String> dataItems : data) {
                // empty row handling
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                // taking necessary data in local variable
                LocalDate effectiveDate = DateUtil.doubleToDate(Double.parseDouble(dataItems.get(PROMOTION_EFFECTIVE_DATE)));
                String employeePin = PinUtil.formatPin(dataItems.get(EMPLOYEE_PIN));
                Double newGrossSalary = Double.parseDouble(dataItems.get(NEW_GROSS_SALARY));
                String newDesignationName = dataItems.get(NEW_DESIGNATION_NAME);
                String newBandName = PinUtil.formatPin(dataItems.get(NEW_BAND_NAME));

                // first delete duplicate data to rollback old employee data
                // removing duplicates
                Employee employee;
                if (employeeService.findEmployeeByPin(employeePin).isPresent()) {
                    employee = employeeService.findEmployeeByPin(employeePin).get();
                } else {
                    continue; // no employee found with same pin
                }
                List<EmploymentHistory> duplicateEmploymentHistoryList = employmentHistoryRepository.findDuplicates(
                    employee,
                    effectiveDate,
                    EventType.PROMOTION
                );
                for (EmploymentHistory employmentHistory1 : duplicateEmploymentHistoryList) {
                    promotionService.deletePromotion(employmentHistory1.getId());
                }

                EmploymentHistoryDTO employmentHistoryDTO = new EmploymentHistoryDTO();

                // load employee from specified employee in Employment History dto
                // update every data from employee to EmploymentHistoryDTO except current gross , changed band , changed Designation
                // update EmployeeDto object
                // save employee object
                // save employmentHistoryObject

                if (employeeService.findEmployeeByPin(employeePin).isPresent()) {
                    employee = employeeService.findEmployeeByPin(employeePin).get();
                } else {
                    continue; // no employee found with same pin
                }

                Designation designation;
                if (designationRepository.findDesignationByDesignationName(newDesignationName).isPresent()) {
                    designation = designationRepository.findDesignationByDesignationName(newDesignationName).get();
                } else {
                    continue; // no designation found
                }

                Band band;
                if (bandRepository.findBandByBandName(newBandName).isPresent()) {
                    band = bandRepository.findBandByBandName(newBandName).get();
                } else {
                    continue; // no band found
                }

                // until now if all data oK , put it into employment History DTO
                employmentHistoryDTO.setEmployeeId(employee.getId());
                employmentHistoryDTO.setEffectiveDate(effectiveDate);
                employmentHistoryDTO.setCurrentMainGrossSalary(newGrossSalary);
                employmentHistoryDTO.setChangedBandId(band.getId());
                employmentHistoryDTO.setChangedDesignationId(designation.getId());

                // mobile bill + HAF change mod not done here yet ***

                employmentHistoryDTO.setReferenceId(employee.getReferenceId());
                employmentHistoryDTO.setPin(employee.getPin());
                employmentHistoryDTO.setEventType(EventType.PROMOTION);
                employmentHistoryDTO.setIsModifiable(true);
                // if effective date not given , keep current date as effective date
                if (employmentHistoryDTO.getEffectiveDate() == null) {
                    employmentHistoryDTO.setEffectiveDate(LocalDate.now());
                }

                employmentHistoryDTO.setPreviousMainGrossSalary(employee.getMainGrossSalary());
                // employmentHistoryDTO.setCurrentMainGrossSalary();

                employmentHistoryDTO.setPreviousDesignationId(employee.getDesignation().getId());
                //employmentHistoryDTO.setChangedDesignationId(employee.getDesignationId());

                //no change in promotion
                employmentHistoryDTO.setPreviousBandId(employee.getBand().getId());
                //employmentHistoryDTO.setChangedBandId(employee.getBandId());

                // in Promotions current-main-gross salary, band and Designation gets changed

                employee.setMainGrossSalary(employmentHistoryDTO.getCurrentMainGrossSalary());
                employee.setBand(band);
                employee.setDesignation(designation);
                employee.setUpdatedAt(LocalDateTime.now());
                employeeRepository.save(employee);
                //EmploymentHistoryDTO result = employmentHistoryService.save(employmentHistoryDTO);
                save(employmentHistoryDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void save(EmploymentHistoryDTO employmentHistory) {
        employmentHistoryService.save(employmentHistory);
    }
}
