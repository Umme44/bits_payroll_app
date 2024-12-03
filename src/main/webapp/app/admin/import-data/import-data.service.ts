import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { IFileImportDetails } from '../../shared/model/file-import-details.model';

@Injectable({ providedIn: 'root' })
export class ImportDataService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/salary-generation');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  uploadEmployeeSalaryXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`api/payroll-mgt/employee-salary-temp-data/import`, formData, { observe: 'response' });
  }

  uploadEmployeeMasterXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`api/employee-mgt/import-employees-master-xlsx/`, formData, { observe: 'response' });
  }

  uploadEmployeeLegacyXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`api/employee-mgt/import-employees-common-xlsx/`, formData, { observe: 'response' });
  }

  uploadEmployeeAllowanceXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`api/employee-mgt/import-allowance`, formData, { observe: 'response' });
  }

  uploadEmployeeLocationXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`api/employee-mgt/import-employee-locations`, formData, { observe: 'response' });
  }

  uploadEmployeeBankDetailsXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`api/employee-mgt/import-bank-details`, formData, { observe: 'response' });
  }

  uploadHolidaysXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`api/attendance-mgt/holidays/import-holidays-xlsx/`, formData, { observe: 'response' });
  }

  uploadLeaveBalanceXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`api/attendance-mgt/leave-balance/import-leave-balance-xlsx/`, formData, { observe: 'response' });
  }

  uploadLeaveApplicationXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`api/employee-mgt/import-leave-application`, formData, { observe: 'response' });
  }

  uploadAttendanceXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/attendance-mgt/import-attendance-entry/`, formData, { observe: 'response' });
  }

  uploadNomineeXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/employee-mgt/import-nominee-entry/`, formData, { observe: 'response' });
  }

  uploadPreviousInsuranceRegistrationsXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/employee-mgt/insurance/import-previous-insurance-registrations-xlsx/`, formData, {
      observe: 'response',
    });
  }

  uploadApprovedInsuranceRegistrationsXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/employee-mgt/insurance/import-approved-insurance-registrations-xlsx/`, formData, {
      observe: 'response',
    });
  }

  uploadRrfXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/employee-mgt/rrf/import-rrf-xlsx`, formData, { observe: 'response' });
  }

  uploadEmployeePinXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/employee-mgt/employee-pin/import-employee-pin-xlsx`, formData, { observe: 'response' });
  }

  uploadEmployeeReferencePinXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/employee-mgt/employee-pin/import-employee-reference-pin-xlsx`, formData, { observe: 'response' });
  }

  uploadInsuranceClaimXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/employee-mgt/insurance/import-insurance-claim-xlsx/`, formData, { observe: 'response' });
  }

  uploadPreviousInsuranceClaimXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/employee-mgt/insurance/import-previous-insurance-claim-xlsx/`, formData, { observe: 'response' });
  }

  uploadLivingAllowanceXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(SERVER_API_URL + `/api/employee-mgt/lfa/import-lfa-xlsx/`, formData, { observe: 'response' });
  }

  uploadMovementEntryXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(SERVER_API_URL + `/api/attendance-mgt/import-movement-entry-xlsx`, formData, { observe: 'response' });
  }

  uploadTinXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(SERVER_API_URL + `/api/employee-mgt/import-employees-tin-number-xlsx/`, formData, {
      observe: 'response',
    });
  }

  uploadFlexScheduleXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(SERVER_API_URL + `/api/attendance-mgt/import-flex-schedule-xlsx/`, formData, {
      observe: 'response',
    });
  }

  uploadIndividualArrearXlsxFile(file: File): Observable<HttpResponse<IFileImportDetails>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<IFileImportDetails>(`/api/payroll-mgt/individual-arrear-import/`, formData, { observe: 'response' });
  }

  uploadNomineeImage(files: File[]): Observable<boolean> {
    const formData: FormData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append('file', files[i]);
    }
    return this.http.post<boolean>(SERVER_API_URL + '/api/employee-mgt/import-nominee-image', formData);
  }

  uploadInsuranceImage(files: File[]): Observable<boolean> {
    const formData: FormData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append('file', files[i]);
    }
    return this.http.post<boolean>(SERVER_API_URL + '/api/employee-mgt/insurance/import-insurance-images/', formData);
  }

  sendSampleEmail(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(SERVER_API_URL + '/api/employee-mgt/send-sample-email', { observe: 'response' });
  }

  requestZeroHourScheduler(): Observable<HttpResponse<void>> {
    return this.http.get<void>(SERVER_API_URL + '/api/employee-mgt/test-zero-hour-scheduler', { observe: 'response' });
  }

  requestMorningScheduler(): Observable<HttpResponse<void>> {
    return this.http.get<void>(SERVER_API_URL + '/api/employee-mgt/test-morning-scheduler', { observe: 'response' });
  }

  requestToDeleteInsuranceData(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(SERVER_API_URL + '/api/employee-mgt/delete-insurance-data', { observe: 'response' });
  }

  requestToDeletePfNomineeData(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(SERVER_API_URL + '/api/employee-mgt/delete-pf-nominee-data', { observe: 'response' });
  }

  requestToDeleteGfNomineeData(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(SERVER_API_URL + '/api/employee-mgt/delete-gf-nominee-data', { observe: 'response' });
  }

  requestToDeleteGeneralNomineeData(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(SERVER_API_URL + '/api/employee-mgt/delete-general-nominee-data', { observe: 'response' });
  }

  runScheduledInsuranceService(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(SERVER_API_URL + '/api/employee-mgt/insurance-registrations/insurance-scheduled-service', {
      observe: 'response',
    });
  }

  resetInsuranceBalance(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(SERVER_API_URL + '/api/employee-mgt/insurance-registrations/reset-insurance-balance', {
      observe: 'response',
    });
  }

  runEmployeePinScheduler(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(SERVER_API_URL + '/api/employee-mgt/employee-pins/run-employee-pin-scheduler', { observe: 'response' });
  }

  changeRRFStatusFromClosedToOpen(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(
      SERVER_API_URL + '/api/employee-mgt/recruitment-requisition-forms/change-rrf-status-from-closed-to-open',
      { observe: 'response' }
    );
  }

  trimEmployeePin(type: string): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(SERVER_API_URL + '/api/employee-mgt/employees/fix-employee-pin?type=' + type, { observe: 'response' });
  }

  fixMultipleJoiningHistory(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(SERVER_API_URL + '/api/employee-mgt/employees/fix-multiple-joining-history', { observe: 'response' });
  }
}
