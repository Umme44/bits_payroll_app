import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmployeeSalary } from '../../shared/legacy/legacy-model/employee-salary.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class SalaryGenerationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/salary-generation');
  public importResourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/import');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  generateSalary(year: number, month: number): Observable<HttpResponse<EmployeeSalary[]>> {
    return this.http.get<EmployeeSalary[]>(`${this.resourceUrl}/${year}/${month}`, { observe: 'response' });
  }

  generateSingleSalary(employeeId: number, year: number, month: number): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(`${this.resourceUrl}/${employeeId}/${year}/${month}`, { observe: 'response' });
  }

  uploadLeaveAttendance(file: File, year: number, month: number): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`${this.importResourceUrl}/leave-attendances/${year}/${month}`, formData, { observe: 'response' });
  }

  uploadMobileBills(file: File, year: number, month: number): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`${this.importResourceUrl}/mobile-bills/${year}/${month}`, formData, { observe: 'response' });
  }

  uploadOtherDeductions(file: File, year: number, month: number): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`${this.importResourceUrl}/salary-deductions/${year}/${month}`, formData, { observe: 'response' });
  }
}
