package com.bits.hr.web.rest.export;

import com.bits.hr.web.rest.util.CopyStreams;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api-open")
public class PfImportSampleFileFormatResource {

    @GetMapping("/pf-import/pf-account-sample-file-xlsx/")
    public void downloadPfAccountSampleFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "pf_account_" + "file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/pf_account_file_Format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/pf-import/pf-collection-sample-file-xlsx/")
    public void downloadPfCollectionSampleFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "pf_collection_" + "file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/pf_collection.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/pf-import/pf-collection-interest-sample-file-xlsx/")
    public void downloadPfCollectionInterestSampleFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "pf_collection_interest_" + "file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/pf_collection_interest.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/pf-import/pf-collection-monthly-collection-sample-file-xlsx/")
    public void downloadPfCollectionMonthlyCollectionInterestSampleFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "pf_collection_monthly_collection_" + "file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/pf_collection_monthly_collection.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/pf-import/pf-collection-opening-balance-sample-file-xlsx/")
    public void downloadPfCollectionOpeningBalanceInterestSampleFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "pf_collection_opening_balance_" + "file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/pf_collection_opening_balance.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/pf-import/pf-loan-sample-file-xlsx/")
    public void downloadPfLoanSampleFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "pf_loan_" + "file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(new ClassPathResource("static/XlsxImportFormat/pf_loan.xlsx").getInputStream(), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Previous employee Data import
    @GetMapping("/pf-import/previous-employee-pf-account-sample-file-xlsx/")
    public void downloadPreviousEmployeePfAccountSampleFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "previous_employee_pf_account_" + "file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/previous_employee_pf_account_file_Format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/pf-import/previous-employee-pf-collection-interest-sample-file-xlsx/")
    public void downloadPreviousEmployeePfCollectionInterestSampleFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader(
                "Content-Disposition",
                "attachment; filename=" + "previous_employee_pf_collection_interest_" + "file_Format.xlsx"
            );
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/previous_employee_pf_collection_interest_file_Format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/pf-import/previous-employee-pf-monthly-collection-sample-file-xlsx/")
    public void downloadPreviousEmployeePfMonthlyCollectionSampleFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader(
                "Content-Disposition",
                "attachment; filename=" + "previous_employee_pf_monthly_collection_" + "file_Format.xlsx"
            );
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/previous_employee_pf_monthly_collection_file_Format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/pf-import/previous-employee-pf-opening-balance-sample-file-xlsx/")
    public void downloadPreviousEmployeePfOpeningBalanceSampleFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader(
                "Content-Disposition",
                "attachment; filename=" + "previous_employee_pf_opening_balance_" + "file_Format.xlsx"
            );
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/previous_employee_pf_opening_balance_file_Format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/pf-import/import-gross-basic-to-pf-collection-sample-file-xlsx/")
    public void downloadGrossAndBasicImportFormatSampleFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + "pf_collection_add_gross_and_basic_" + "file_Format.xlsx");
            response.getOutputStream();
            CopyStreams.copy(
                new ClassPathResource("static/XlsxImportFormat/pf_collection_add_gross_and_basic_import_format.xlsx").getInputStream(),
                response.getOutputStream()
            );
            response.getOutputStream().flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
