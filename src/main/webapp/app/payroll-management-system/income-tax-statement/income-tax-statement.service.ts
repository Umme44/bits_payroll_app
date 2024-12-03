import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IIncomeTaxStatement } from 'app/shared/model/income-tax-statement-model';
import { IIncomeTaxDropDownMenu } from 'app/shared/model/drop-down-income-tax.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';

type EntityResponseType = HttpResponse<IIncomeTaxStatement>;
type EntityArrayResponseType = HttpResponse<IIncomeTaxDropDownMenu[]>;

@Injectable({ providedIn: 'root' })
export class IncomeTaxStatementService {
  private baseUrlIncomeTaxReport = this.applicationConfigService.getEndpointFor('/api/common/income-tax-report');
  private baseUrlTaxYearList = this.applicationConfigService.getEndpointFor('/api/common/get-income-tax-year-list');
  private baseUrlTaxReportEligibility = this.applicationConfigService.getEndpointFor(
    `/api/common/income-tax-statement/is-employee-eligible`
  );
  private baseUrlTaxAssessmentYear = this.applicationConfigService.getEndpointFor(`/api/common/get-assessment-year-by-aitConfigId`);

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getTaxReportById(id: number): Observable<EntityResponseType> {
    return this.http.get<IIncomeTaxStatement>(`${this.baseUrlIncomeTaxReport}/${id}`, { observe: 'response' });
  }

  getAllAitConfigYears(): Observable<EntityArrayResponseType> {
    return this.http.get<IIncomeTaxDropDownMenu[]>(`${this.baseUrlTaxYearList}`, { observe: 'response' });
  }

  checkValidityOfUserIncomeTaxStatement(): Observable<boolean> {
    return this.http.get<boolean>(this.baseUrlTaxReportEligibility);
  }

  getAssessmentYearByAitConfigId(aitConfigId: number): Observable<HttpResponse<IIncomeTaxDropDownMenu>> {
    return this.http.get<IIncomeTaxDropDownMenu>(`${this.baseUrlTaxAssessmentYear}/${aitConfigId}`, {
      observe: 'response',
    });
  }
}
