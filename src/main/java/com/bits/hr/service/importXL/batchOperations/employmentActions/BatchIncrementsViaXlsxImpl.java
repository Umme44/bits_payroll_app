package com.bits.hr.service.importXL.batchOperations.employmentActions;

import static com.bits.hr.service.importXL.batchOperations.employmentActions.RowConstantsIncrements.*;

import com.bits.hr.domain.Band;
import com.bits.hr.domain.Employee;
import com.bits.hr.domain.EmploymentHistory;
import com.bits.hr.domain.enumeration.EventType;
import com.bits.hr.repository.BandRepository;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.EmploymentHistoryRepository;
import com.bits.hr.service.EmployeeService;
import com.bits.hr.service.EmploymentHistoryService;
import com.bits.hr.service.dto.EmploymentHistoryDTO;
import com.bits.hr.service.employmentHistory.IncrementService;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.PinUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Qualifier("Increment")
@Log4j2
@Transactional
public class BatchIncrementsViaXlsxImpl implements BatchOperationsViaXlsx {

    @Autowired
    private GenericXlsxImportService genericXlsxImportService;

    @Autowired
    private EmploymentHistoryRepository employmentHistoryRepository;

    @Autowired
    private IncrementService incrementService;

    @Autowired
    private EmploymentHistoryService employmentHistoryService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BandRepository bandRepository;

    @Override
    public boolean batchOperations(MultipartFile file) throws Exception {
        try {
            // common part
            // -- band
            // -- fail-safe mechanism if no band exist on employee profile
            Band dummyBand = new Band();
            dummyBand.bandName("NO BAND").maxSalary(0d).minSalary(0d).mobileCelling(0d).welfareFund(0d);
            if (!bandRepository.findBandByBandName(dummyBand.getBandName()).isPresent()) {
                bandRepository.save(dummyBand);
            } else {
                dummyBand = bandRepository.findBandByBandName(dummyBand.getBandName()).get();
            }

            List<ArrayList<String>> data = genericXlsxImportService.importXlsx(file);
            List<String> header = data.remove(0);

            // file validation
            if (header.get(0).trim().compareToIgnoreCase(FIRST_ROW_FIRST_COL) != 0) {
                return false;
            }
            for (List<String> dataItems : data) {
                // empty row handling
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                // taking necessary data in local variable
                LocalDate effectiveDate = DateUtil.doubleToDate(Double.parseDouble(dataItems.get(INCREMENT_EFFECTIVE_DATE)));

                String employeePin = PinUtil.formatPin(dataItems.get(EMPLOYEE_PIN).trim());
                double newGrossSalary = Double.parseDouble(dataItems.get(NEW_GROSS_SALARY));

                boolean isBandChanged = !(PinUtil.formatPin(dataItems.get(NEW_BAND_NAME)).trim().equalsIgnoreCase("no_change"));
                String newBandName = "";
                if (isBandChanged) {
                    newBandName = PinUtil.formatPin(dataItems.get(NEW_BAND_NAME)).trim();
                }

                // first delete duplicate data to rollback old employee data
                // removing duplicates
                Optional<Employee> employeeOptional = employeeService.findEmployeeByPin(employeePin);
                Employee employee;
                if (employeeOptional.isPresent()) {
                    employee = employeeOptional.get();
                } else {
                    continue; // no employee found with same pin
                }
                // removing duplicates
                List<EmploymentHistory> duplicateEmploymentHistoryList = employmentHistoryRepository.findDuplicates(
                    employee,
                    effectiveDate,
                    EventType.INCREMENT
                );
                for (EmploymentHistory employmentHistory : duplicateEmploymentHistoryList) {
                    incrementService.deleteAndRevertIncrement(employmentHistory.getId());
                }

                EmploymentHistoryDTO employmentHistoryDTO = new EmploymentHistoryDTO();
                // load employee from specified employee in Employment History dto
                // update every data from employee to EmploymentHistoryDTO except current gross , changed band , changed Designation
                // update EmployeeDto object
                // save employee object
                // save employmentHistoryObject
                // until now if all data ok , put it into employment History DTO
                employmentHistoryDTO.setEmployeeId(employee.getId());
                employmentHistoryDTO.setPin(employee.getPin());
                employmentHistoryDTO.setEffectiveDate(effectiveDate);
                // -- gross
                employmentHistoryDTO.setPreviousMainGrossSalary(employee.getMainGrossSalary());
                employmentHistoryDTO.setCurrentMainGrossSalary(newGrossSalary);

                Band newBand = dummyBand;
                if (employee.getBand() != null) {
                    newBand = employee.getBand();
                    if (isBandChanged) {
                        if (bandRepository.findBandByBandName(newBandName).isPresent()) {
                            newBand = bandRepository.findBandByBandName(newBandName).get();
                        }
                    }
                    employmentHistoryDTO.setPreviousBandId(employee.getBand().getId());
                    employmentHistoryDTO.setChangedBandId(newBand.getId());
                }

                employmentHistoryDTO.setEventType(EventType.INCREMENT);
                employmentHistoryDTO.setIsModifiable(true);

                // if effective date not given , keep current date as effective date

                if (employmentHistoryDTO.getEffectiveDate() == null) {
                    employmentHistoryDTO.setEffectiveDate(LocalDate.now());
                }
                save(employmentHistoryDTO, employee, newBand);
            }
        } catch (Exception e) {
            log.error(e);
            return false;
        }
        return true;
    }

    public void save(EmploymentHistoryDTO employmentHistoryDTO, Employee employee, Band newBand) {
        List<EmploymentHistoryDTO> previousEntryList = employmentHistoryService.getEmploymentHistoryByEmployeePinBetweenTwoDates(
            employee.getPin(),
            employmentHistoryDTO.getEffectiveDate(),
            employmentHistoryDTO.getEffectiveDate(),
            EventType.INCREMENT
        );
        if (previousEntryList.size() > 0) {
            employmentHistoryDTO.setId(previousEntryList.get(0).getId());
        }
        // in Increment current-main-gross salary gets changed
        employee.setMainGrossSalary(employmentHistoryDTO.getCurrentMainGrossSalary());
        employee.setBand(newBand);
        employee.setMobileCelling(newBand.getMobileCelling().longValue());
        employee.setWelfareFundDeduction(newBand.getWelfareFund());
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
        employmentHistoryService.save(employmentHistoryDTO);
    }
}
