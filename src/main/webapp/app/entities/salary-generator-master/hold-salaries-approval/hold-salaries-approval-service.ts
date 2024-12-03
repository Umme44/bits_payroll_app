import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { IEmployeeSalary } from '../../../shared/legacy/legacy-model/employee-salary.model';
import { createRequestOption } from '../../../core/request/request-util';

@Injectable({ providedIn: 'root' })
export class HoldSalariesApprovalService {
  private resourceURL = this.applicationConfigService.getEndpointFor('/api/payroll-mgt/salary-hold');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  public getHoldSalaries(req?: any): Observable<HttpResponse<IEmployeeSalary[]>> {
    const options = createRequestOption(req);
    return this.http.get<IEmployeeSalary[]>(this.resourceURL + '/salaries', { params: options, observe: 'response' });
  }
}
