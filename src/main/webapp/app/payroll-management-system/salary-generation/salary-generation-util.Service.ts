import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ISalaryGenerationPreValidation } from '../../shared/model/salary-generation-pre-validation.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class SalaryGenerationUtilService {
  public resourceUrlOne = this.applicationConfigService.getEndpointFor('api/payroll-mgt/attendance-summery-check');
  public resourceUrlConstraintCheck = this.applicationConfigService.getEndpointFor('api/payroll-mgt/salary-generator-pre-validation');
  public resourceUrlAttendanceSync = this.applicationConfigService.getEndpointFor('api/attendance-mgt/generate-attendance-summary');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getSalaryGenerationPreValidation(year: number, month: number): Observable<HttpResponse<ISalaryGenerationPreValidation>> {
    return this.http.get<ISalaryGenerationPreValidation>(`${this.resourceUrlConstraintCheck}/${year}/${month}`, { observe: 'response' });
  }

  checkRemainingEmployeesAttendanceSummary(year: number, month: number): Observable<HttpResponse<String[]>> {
    return this.http.get<String[]>(`${this.resourceUrlOne}/${year}/${month}`, { observe: 'response' });
  }

  generateAttendanceSummary(year: number, month: number): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(`${this.resourceUrlAttendanceSync}/${year}/${month}`, { observe: 'response' });
  }
}
