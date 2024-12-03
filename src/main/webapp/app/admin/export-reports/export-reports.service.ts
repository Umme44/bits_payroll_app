import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { createRequestOption } from '../../core/request/request-util';

@Injectable({ providedIn: 'root' })
export class ExportReportsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/salary-generator-masters');
  public resourceUrlForTin = this.applicationConfigService.getEndpointFor('/api/employee-mgt/export-tin-number-csv');
  public fileResourceUrl = this.applicationConfigService.getEndpointFor('/api/employee-mgt/export-employees');
  public exportSalaryBetweenDateRange = this.applicationConfigService.getEndpointFor(
    '/api/employee-mgt/employee-salary-between-date-range'
  );
  public leaveBalanceFileResourceUrl = this.applicationConfigService.getEndpointFor('/api/employee-mgt/export-leave-balance');
  public resourceUrlToExportInsuranceRegistration = this.applicationConfigService.getEndpointFor(
    '/api/employee-mgt/export-insurance-registrations'
  );
  public resourceUrlToExportApprovedInsuranceRegistration = this.applicationConfigService.getEndpointFor(
    '/api/employee-mgt/export-all-approved-insurance-registrations'
  );
  public resourceUrlToExportInsuranceClaim = this.applicationConfigService.getEndpointFor('/api/employee-mgt/export-insurance-claim');
  public resourceUrlToExportEmployeePINs = this.applicationConfigService.getEndpointFor(
    '/api/employee-mgt/employee-pin/export-employee-pin-xlsx'
  );
  public resourceUrlToExportEmployeeReferencePINs = this.applicationConfigService.getEndpointFor(
    '/api/employee-mgt/employee-pin/export-employee-reference-pin-xlsx'
  );
  public resourceUrlToExportHolidays = this.applicationConfigService.getEndpointFor('/api/employee-mgt/export/Holidays/');
  public resourceUrlForYearlyTaxReturnSubmission = this.applicationConfigService.getEndpointFor(
    '/api/payroll-mgt/export/employee-salary/yearly-tax-return-submission'
  );
  public resourceUrlForMonthlyAtsReport = this.applicationConfigService.getEndpointFor(
    '/api/attendance-mgt/export-monthly-attendance-time-sheet-report'
  );
  public resourceUrlForEmployeeDocDetailedExport = this.applicationConfigService.getEndpointFor(
    '/api/payroll-mgt/employee-doc/export-employee-docs-detailed-report'
  );

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  public exportEmployeeXlsx(): Observable<Blob> {
    return this.http.get(this.fileResourceUrl, { responseType: 'blob' });
  }

  public exportLeaveBalanceXlsx(year: number): Observable<Blob> {
    return this.http.get(`${this.leaveBalanceFileResourceUrl}/${year}`, { responseType: 'blob' });
  }

  public exportEmployeeTinXlsx(): Observable<Blob> {
    return this.http.get(this.resourceUrlForTin, { responseType: 'blob' });
  }

  public exportInsuranceRegistrations(): Observable<Blob> {
    return this.http.get(this.resourceUrlToExportInsuranceRegistration, { responseType: 'blob' });
  }

  public exportApprovedInsuranceRegistrations(): Observable<Blob> {
    return this.http.get(this.resourceUrlToExportApprovedInsuranceRegistration, { responseType: 'blob' });
  }

  public exportInsuranceClaim(): Observable<Blob> {
    return this.http.get(this.resourceUrlToExportInsuranceClaim, { responseType: 'blob' });
  }

  public exportEmployeePins(): Observable<Blob> {
    return this.http.get(this.resourceUrlToExportEmployeePINs, { responseType: 'blob' });
  }

  public exportEmployeeReferencePins(): Observable<Blob> {
    return this.http.get(this.resourceUrlToExportEmployeeReferencePINs, { responseType: 'blob' });
  }

  public exportHolidays(): Observable<Blob> {
    return this.http.get(this.resourceUrlToExportHolidays, { responseType: 'blob' });
  }

  public exportYearlyTaxReturnSubmissionReport(req?: any): Observable<Blob> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceUrlForYearlyTaxReturnSubmission, { params: options, responseType: 'blob' });
  }

  public exportSalaryDateRange(req?: any): Observable<Blob> {
    const options = createRequestOption(req);
    return this.http.get(this.exportSalaryBetweenDateRange, { params: options, responseType: 'blob' });
  }

  public exportMonthlyAtsReport(req?: any): Observable<Blob> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceUrlForMonthlyAtsReport, { params: options, responseType: 'blob' });
  }

  public exportEmployeeDocDetailedReport(req?: any): Observable<Blob> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceUrlForEmployeeDocDetailedExport, { params: options, responseType: 'blob' });
  }
}
