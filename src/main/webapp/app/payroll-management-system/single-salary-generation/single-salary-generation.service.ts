import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { EmployeeSalary } from '../../shared/legacy/legacy-model/employee-salary.model';

@Injectable({ providedIn: 'root' })
export class SalaryGenerationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/salary-generation');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  generateSalary(year: number, month: number): Observable<HttpResponse<EmployeeSalary[]>> {
    return this.http.get<EmployeeSalary[]>(`${this.resourceUrl}/${year}/${month}`, { observe: 'response' });
  }
}
