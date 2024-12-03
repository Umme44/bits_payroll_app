import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class BatchOperationsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('/api/employee-mgt');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  garbageAttenXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/attendance-mgt/import-garbage-attendance-xlsx`, formData, { observe: 'response' });
  }

  flexScheduleXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/attendance-mgt/import-flex-schedule-xlsx`, formData, { observe: 'response' });
  }

  batchDDUXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/employee-mgt/import-employees-ddu-xlsx/`, formData, { observe: 'response' });
  }

  batchPromotionsXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/employee-mgt/batch-promotion/`, formData, { observe: 'response' });
  }

  batchIncrementXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/employee-mgt/batch-increment/`, formData, { observe: 'response' });
  }

  batchTransferXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/employee-mgt/batch-transfer/`, formData, { observe: 'response' });
  }
  batchArrearXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/payroll-mgt/batch-arrear`, formData, { observe: 'response' });
  }

  taxChallanXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/payroll-mgt/import-tax-challan-xlsx/`, formData, { observe: 'response' });
  }

  festivalBonusXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`/api/payroll-mgt/import-festival-bonus`, formData, { observe: 'response' });
  }

  importEmployeeDocumentsZip(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`${this.resourceUrl}/employee-document/batch/files`, formData, { observe: 'response' });
  }

  importEmployeeDocumentsXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`${this.resourceUrl}/employee-document/batch/excel`, formData, { observe: 'response' });
  }

  importEmployeeDocuments(zip: File, excel: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('zip', zip);
    formData.append('excel', excel);
    return this.http.post<boolean>(`${this.resourceUrl}/employee-document/batch`, formData, { observe: 'response' });
  }

  workFromHomeApplicationXlsxFile(file: any): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`${this.resourceUrl}/work-from-home-applications/import-work-from-home-applications`, formData, {
      observe: 'response',
    });
    return this.http.post<boolean>(`${this.resourceUrl}/work-from-home-applications/import-work-from-home-applications`, formData, {
      observe: 'response',
    });
  }

  billableAugmentedDataXlsxFile(file: any): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`${this.resourceUrl}/import-billable-augmented-data-xlsx`, formData, {
      observe: 'response',
    });
  }
}
