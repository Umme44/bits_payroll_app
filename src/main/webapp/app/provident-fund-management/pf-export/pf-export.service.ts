import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { createRequestOption } from '../../core/request/request-util';
import { DATE_FORMAT } from '../../config/input.constants';
import { Dayjs } from 'dayjs';
import { ApplicationConfigService } from '../../core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class PfExportService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/pf-mgt');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  monthlyPfCollectionExport(): Observable<Blob> {
    return this.http.get(this.resourceUrl + `/export-monthly-pf-collection-csv`, { responseType: 'blob' });
  }

  pfCollectionInterestsExport(): Observable<Blob> {
    return this.http.get(this.resourceUrl + `/export-pf-collection-interest-csv`, { responseType: 'blob' });
  }

  pfOpeningBalanceExport(): Observable<Blob> {
    return this.http.get(this.resourceUrl + `/export-pf-opening-balance-csv`, { responseType: 'blob' });
  }

  annualPfAmountReport(year: number): Observable<Blob> {
    return this.http.get(this.resourceUrl + `/export-annual-pf-report/${year}`, { responseType: 'blob' });
  }

  dateRangePfAmountReport(req?: any): Observable<Blob> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceUrl + `/pf-collections-between-date-range/`, { params: options, responseType: 'blob' });
  }

  loadAllYears(): Observable<HttpResponse<number[]>> {
    return this.http.get<number[]>(this.resourceUrl + `/overall-pf-amount-report/get-all-years`, { observe: 'response' });
  }

  detailedPfContributionReport(): Observable<Blob> {
    return this.http.get(this.resourceUrl + `/export-detailed-pf-contribution-report-csv`, { responseType: 'blob' });
  }

  detailedPfInterestReport(): Observable<Blob> {
    return this.http.get(this.resourceUrl + `/export-detailed-pf-interest-report-csv`, { responseType: 'blob' });
  }
  pfStatementReport(selectedDate: Dayjs): Observable<Blob> {
    const options = createRequestOption({ selectedDate: selectedDate.format(DATE_FORMAT) });
    return this.http.get(this.resourceUrl + `/export-pf-statement-report`, { params: options, responseType: 'blob' });
  }
}
