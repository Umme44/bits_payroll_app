import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DetailedPfStatement } from 'app/shared/model/pf/detailed-pf-statement.model';
import { createRequestOption } from '../../core/request/request-util';
import { ApplicationConfigService } from '../../core/config/application-config.service';

import dayjs from 'dayjs/esm';
import { map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class PfAmountDetailedReportService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/pf-mgt');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getDetailedPfStatement(employeePin: string, reqObj?: any): Observable<HttpResponse<DetailedPfStatement>> {
    const options = createRequestOption(reqObj);
    return this.http.get<DetailedPfStatement>(this.resourceUrl + `/detailed-pf-amount-report/${employeePin}`, {
      params: options,
      observe: 'response',
    });
  }

  getListOfYears(pfCode: string): Observable<HttpResponse<number[]>> {
    return this.http.get<number[]>(this.resourceUrl + `/${pfCode}/get-list-of-years-from-pf-collection`, { observe: 'response' });
  }
  protected convertUserPfStatementDateFromServer(res: HttpResponse<DetailedPfStatement>): HttpResponse<DetailedPfStatement> {
    if (res.body) {
      res.body.dateOfJoining = res.body.dateOfJoining ? dayjs(res.body.dateOfJoining) : undefined;
      res.body.dateOfConfirmation = res.body.dateOfConfirmation ? dayjs(res.body.dateOfConfirmation) : undefined;
    }
    return res;
  }
}
