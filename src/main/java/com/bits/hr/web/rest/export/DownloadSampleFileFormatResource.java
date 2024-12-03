package com.bits.hr.web.rest.export;

import com.bits.hr.domain.Location;
import com.bits.hr.errors.BadRequestAlertException;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.dto.AddressBookExternalDTO;
import com.bits.hr.service.mapper.AddressBookExternalMapper;
import com.bits.hr.service.xlExportHandling.genericCsvExport.ExportCSV;
import com.bits.hr.web.rest.util.CopyStreams;
import java.time.LocalDate;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api-open")
@Log4j2
public class DownloadSampleFileFormatResource {

    private static final String RESOURCE_NAME = "DownloadSampleFileFormatResource";

    @Autowired
    private ExportCSV exportCSV;

    //    employee Master Import
    @GetMapping("/employees/employees-master-xlsx-upload-format/")
    public void downloadEmployeeMasterUploadFormatResource(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Employee Master Import " + "file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/Employee Master Import.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressBookExternalMapper addressBookExternalMapper;

    private static final int MAX_PAGE_SIZE = 25;

    @GetMapping("/address-book")
    public Page<AddressBookExternalDTO> addressBook(Pageable pageable) {
        log.debug("External REST request for address book search " + "with parameter {pageable:" + pageable.toString() + "}");

        pageSizeValidation(pageable);

        return employeeRepository
            .getAllActiveEmployeesTillDate("", LocalDate.now(), pageable)
            .map(employee -> {
                AddressBookExternalDTO addressBookDTO = addressBookExternalMapper.toDto(employee);

                if (employee.getReportingTo() != null) {
                    addressBookDTO.setReportingToName(employee.getReportingTo().getFullName());
                    addressBookDTO.setReportingToPin(employee.getReportingTo().getPin());
                    Location location = employee.getOfficeLocation();
                    HashMap<String, String> officeLocation = new HashMap<>();
                    while (location != null) {
                        officeLocation.put(location.getLocationType().toString().toLowerCase(), location.getLocationName());
                        location = location.getParent();
                    }
                    addressBookDTO.setOfficeLocations(officeLocation);
                }

                return addressBookDTO;
            });
    }

    private static void pageSizeValidation(Pageable pageable) {
        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new BadRequestAlertException("Max page should not more than " + MAX_PAGE_SIZE + ".", RESOURCE_NAME, "");
        }
    }

    //    employee Common Import		>>		GET		api-open/employees/employees-legacy-xlsx-upload-format/
    @GetMapping("/employees/employees-legacy-xlsx-upload-format/")
    public void downloadEmployeeCommonUploadFormatResource(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Employee Legacy " + "Import file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/01_EmployeeData.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    @GetMapping("/employees/employees-tin-number-upload-format/")
    public void downloadEmployeeTinNumberUploadFormatResource(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Employee TIN Number  " + "Import file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/08_Employee_TIN_Number_Import_Format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    // Holidays
    @GetMapping("/holidays/holidays-xlsx-upload-format/")
    public void downloadHolidaysUploadFormatResource(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Holidays " + "Import file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/05_Holiday_Import_Format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    // Leave Balance
    @GetMapping("/leave-balance/leave-balance-xlsx-upload-format/")
    public void downloadLeaveBalanceUploadFormatResource(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Leave Balance " + "Import file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/06_Leave_Balance_Import_Format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    @GetMapping("/leave-application/leave-application-xlsx-upload-format/")
    public void downloadLeaveApplicationUploadFormatResource(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Leave Application " + "Import file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/07_Leave_Application_Import_Format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // mobile bill
    @GetMapping("/salary-generation/mobile-bill-xlsx-upload-format/")
    public void downloadMobileBillImportFormatResource(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Mobile Bill " + "Import file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/03_MobileBill.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    // attendance summary
    @GetMapping("/salary-generation/attendance-summary-xlsx-upload-format/")
    public void downloadAttendanceSummeryImportFormatResource(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Attendance Summary " + "Import file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/02_LeaveAttandance.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    // pf-loan
    @GetMapping("/salary-generation/pf-loan-repayment-xlsx-upload-format/")
    public void downloadPfLoanRepaymentImportFormatResource(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "PF loan  repayment " + "Import file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/04_PFLoanRepayment.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    // Deductions
    @GetMapping("/salary-generation/salary-deductions-xlsx-upload-format/")
    public void downloadSalaryDeductionsFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "salary-deductions" + " Import file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/salary-deductions.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // employee salary
    @GetMapping("/employees/employees-salary-xlsx-upload-format/")
    public void downloadEmployeeSalaryUploadFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Salary Import " + "file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/salaryImportFile.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    // batch promotion
    @GetMapping("/batch-promotion-xlsx-upload-format/")
    public void downloadBatchPromotionFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "batch_promotion" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/batch_promotion.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    // batch work from home
    @GetMapping("/batch-work-from-home-application-xlsx-upload-format/")
    public void downloadBatchWorkFromHomeApplicationFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "batch_work_from_home" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/batch_work_from_home.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    // batch increment
    @GetMapping("/batch-increment-xlsx-upload-format/")
    public void downloadBatchIncrementFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "batch_increment" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/batch_increment.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    // batch transfer
    @GetMapping("/batch-transfer-xlsx-upload-format/")
    public void downloadBatchTransferFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "batch_transfer" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/batch_transfer.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    // batch arrear
    @GetMapping("/batch-arrear-xlsx-upload-format/")
    public void downloadBatchArrearFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "batch_arrear" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/batch_arrear.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    // batch arrear
    @GetMapping("/tax-challan-xlsx-upload-format/")
    public void downloadTaxChallanImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "tax_challan_import_format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/tax_challan.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    // http://localhost:8080/api-open/employees/bank-details-import-format/
    @GetMapping("/employees/bank-details-import-format/")
    public void downloadBankDetailsFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "bank_details" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(new ClassPathResource("static/XlsxImportFormat/bank.xlsx").getInputStream(), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    // http://localhost:8080/api-open/employees/allowance-import-format/
    @GetMapping("/employees/allowance-import-format/")
    public void downloadAllowanceFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "allowance" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(new ClassPathResource("static/XlsxImportFormat/allowance.xlsx").getInputStream(), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    @GetMapping("/employees/employee-location-import-format/")
    public void downloadLocationFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "employee_locations" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/employee_locations.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    @GetMapping("/employees/ddu-import-format")
    public void downloadDDUFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "DDU Format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(new ClassPathResource("static/XlsxImportFormat/Batch-ddu.xlsx").getInputStream(), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    @GetMapping("/employees/garbage-attendance-format")
    public void downloadGarbageAttendanceFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Garbage Attendance Import Format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/garbage-attendance.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    @GetMapping("/employees/flex-schedule-format")
    public void downloadFlexScheduleFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Flex schedule Import Format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/flex-schedule.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    @GetMapping("/employees/festival-bonus-import-format")
    public void downloadFestivalBonusImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Festival Bonus Import Format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/festival_bonus_Import_format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    @GetMapping("/attendance-entry/attendance-entry-xlsx-upload-format")
    public void downloadAttendanceImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "Attendance entry " + "Import file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(new ClassPathResource("static/XlsxImportFormat/attendance.xlsx").getInputStream(), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/individual-arrear-salary-import-format")
    public void downloadIndividualArrearSalaryImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "individual_arrear_salaries_import_format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/individual-arrear-salary-excel-import-format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/import-format/nominee")
    public void downloadNomineeImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "nominee_import_format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/nominee-import-sample.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/import-format/insurance-claim")
    public void downloadInsuranceClaimImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "insurance-claim-import-format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/insurance_claim_import_format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/import-format/previous-insurance-registration")
    public void downloadPreviousInsuranceRegistrationImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "previous-insurance-registration-import-format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/previous_insurance_registration_import_format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/import-format/previous-insurance-claim")
    public void downloadPreviousInsuranceClaimImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "previous-insurance-claim-import-format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/previous_insurance_claim_import_format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/import-format/approved-insurance-registrations")
    public void downloadApprovedInsuranceRegistrationsImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "approved-Insurance-registrations-import-format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/approved-Insurance-registrations-import-format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/import-format/living-allowance")
    public void downloadLivingAllowanceImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "living_allowance_import_format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/living_allowance_Import_format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/import-format/rrf")
    public void downloadRRFImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "rrf_import_format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/rrf-import-sample.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/import-format/employee-pin")
    public void downloadEmployeePinImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "employee_pin_import_format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/employee-pin-import-file-format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/import-format/movement-entry")
    public void downloadMovementEntryFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "movement_entry_import_format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/movement_entry_xlsx_format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/employees/employee-documents-batch-import-format")
    public void downloadEmployeeDocumentsImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "employee_documents_import_format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/employee_documents_import_format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/employees/employee-documents-batch-zip-import-format")
    public void downloadEmployeeDocumentsZipImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "employee_documents_zip_import_format" + ".zip");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/employee_documents_zip_import_format.zip").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/import-format/billable-augmented-data-format")
    public void downloadBillableAugmentedDataImportFormat(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "xlsx_billable-augmented_data_upload_format" + ".xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/xlsx_billable-augmented_update_file_import_sample.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
