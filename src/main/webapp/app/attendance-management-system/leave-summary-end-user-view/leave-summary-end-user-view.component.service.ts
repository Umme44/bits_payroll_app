import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { ILeaveBalanceEndUserView } from '../../shared/legacy/legacy-model/leave-balance-end-user-view.model';
import { createRequestOption } from '../../core/request/request-util';

type EntityResponseType = HttpResponse<ILeaveBalanceEndUserView>;
type EntityArrayResponseType = HttpResponse<ILeaveBalanceEndUserView[]>;

@Injectable({ providedIn: 'root' })
export class LeaveSummaryEndUserViewService {
  public resourceUrlCommon = this.applicationConfigService.getEndpointFor('api/attendance-user/leave-balances');
  public resourceUrlHR = this.applicationConfigService.getEndpointFor('api/attendance-mgt/leave-balances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeaveBalanceEndUserView>(`${this.resourceUrlCommon}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaveBalanceEndUserView[]>(this.resourceUrlCommon, { params: options, observe: 'response' });
  }

  loadByYear(year: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaveBalanceEndUserView[]>(`${this.resourceUrlCommon}/my-leave-summary/${year}/`, {
      params: options,
      observe: 'response',
    });
  }

  loadByYearAndEmployeeId(year: number, employeeId: number): Observable<EntityArrayResponseType> {
    return this.http.get<ILeaveBalanceEndUserView[]>(`${this.resourceUrlHR}/${year}/${employeeId}`, {
      observe: 'response',
    });
  }

  loadByYearAndSubordinateId(year: number, subordinateId: number): Observable<EntityArrayResponseType> {
    return this.http.get<ILeaveBalanceEndUserView[]>(`${this.resourceUrlCommon}/get-subordinate-leave-summary/${subordinateId}/${year}/`, {
      observe: 'response',
    });
  }
}
