import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { IEmploymentHistory } from '../../shared/legacy/legacy-model/employment-history.model';
import { createRequestOption } from '../../core/request/request-util';
import { ApplicationConfigService } from '../../core/config/application-config.service';

type EntityResponseType = HttpResponse<IEmploymentHistory>;
type EntityArrayResponseType = HttpResponse<IEmploymentHistory[]>;

@Injectable({ providedIn: 'root' })
export class EmploymentHistoriesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('/api/employee-mgt/employment-histories-by-user');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getEmploymentHistoryByEmployeeID(employeeId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmploymentHistory[]>(`${this.resourceUrl}/${employeeId}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((employmentHistory: IEmploymentHistory) => {
        employmentHistory.effectiveDate = employmentHistory.effectiveDate ? dayjs(employmentHistory.effectiveDate) : undefined;
      });
    }
    return res;
  }
}
