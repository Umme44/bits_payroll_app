import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IIncomeTaxStatement } from 'app/shared/model/income-tax-statement-model';
import { IIncomeTaxDropDownMenu } from 'app/shared/model/drop-down-income-tax.model';
import { IEmployeeMinimal } from '../../shared/model/employee-minimal.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';

type EntityResponseType = HttpResponse<IIncomeTaxStatement>;
type EntityArrayResponseType = HttpResponse<IIncomeTaxDropDownMenu[]>;

@Injectable({ providedIn: 'root' })
export class IncomeTaxStatementAdminService {
  private baseUrl = this.applicationConfigService.getEndpointFor('/api/payroll-mgt/income-tax-report');
  private baseUrlForYearList = this.applicationConfigService.getEndpointFor('/api/payroll-mgt/get-income-tax-year-list-admin');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getTaxReport(employeeId: number, aitConfifId: number): Observable<EntityResponseType> {
    return this.http.get<IIncomeTaxStatement>(`${this.baseUrl}/${employeeId}/${aitConfifId}`, { observe: 'response' });
  }

  getAllYear(): Observable<EntityArrayResponseType> {
    return this.http.get<IIncomeTaxDropDownMenu[]>(`${this.baseUrlForYearList}`, { observe: 'response' });
  }

  getAllEligibleEmployeeForTaxStatement(id: number): Observable<HttpResponse<IEmployeeMinimal[]>> {
    return this.http.get<IEmployeeMinimal[]>(`${this.baseUrl}/get-all-eligible-employees-within-year/${id}`, { observe: 'response' });
  }
}
