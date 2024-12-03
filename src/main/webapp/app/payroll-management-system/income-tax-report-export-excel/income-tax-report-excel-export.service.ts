import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IIncomeTaxStatement } from '../../shared/model/income-tax-statement-model';
import { IIncomeTaxDropDownMenu } from '../../shared/model/drop-down-income-tax.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';

type EntityArrayResponseType = HttpResponse<IIncomeTaxStatement[]>;

@Injectable({ providedIn: 'root' })
export class IncomeTaxReportExcelExportService {
  private baseUrl = this.applicationConfigService.getEndpointFor('/api/payroll-mgt/get-income-tax-report');
  private baseUrlForListOfYears = this.applicationConfigService.getEndpointFor('/api/payroll-mgt/get-income-tax-year-list');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  exportTaxReports(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${id}`, { responseType: 'blob' });
  }

  getAllYear(): Observable<HttpResponse<IIncomeTaxDropDownMenu[]>> {
    return this.http.get<IIncomeTaxDropDownMenu[]>(`${this.baseUrlForListOfYears}`, { observe: 'response' });
  }
}
