import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { ILeaveApplication } from '../../shared/legacy/legacy-model/leave-application.model';
import { DATE_FORMAT } from '../../config/input.constants';
import { ILeaveBalanceEndUserView } from '../../shared/legacy/legacy-model/leave-balance-end-user-view.model';
import { createRequestOption } from '../../core/request/request-util';
import { ApplicationConfigService } from '../../core/config/application-config.service';

type EntityResponseType = HttpResponse<ILeaveApplication>;
type EntityArrayResponseType = HttpResponse<ILeaveApplication[]>;

@Injectable({ providedIn: 'root' })
export class UserLeaveApplicationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/leave-applications');
  public resourceUrlUserLeaveApplication = this.applicationConfigService.getEndpointFor('api/common/user-leave-application');
  public urlLeaveDateCalculate = this.applicationConfigService.getEndpointFor('/api/common/leave-date-calculation');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaveApplication: ILeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .post<ILeaveApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  calculateUserLeaveDuration(leaveApplication: ILeaveApplication): Observable<HttpResponse<number>> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http.post<number>(this.urlLeaveDateCalculate + '/calculate-for-current-employee', copy, { observe: 'response' });
  }

  calculateUserCasualLeaveRemaining(startDate: string, endDate: string, leaveApplicationId?: number): Observable<HttpResponse<boolean>> {
    if (!leaveApplicationId) leaveApplicationId = -1;

    return this.http.get<boolean>(
      `${this.urlLeaveDateCalculate}/calculate-casual-leave-remaining/${startDate}/${endDate}/${leaveApplicationId}`,
      {
        observe: 'response',
      }
    );
  }

  update(leaveApplication: ILeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .put<ILeaveApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaveApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaveApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findUserLeaveApplication(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaveApplication>(`${this.resourceUrlUserLeaveApplication}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  protected convertDateFromClient(leaveApplication: ILeaveApplication): ILeaveApplication {
    const copy: ILeaveApplication = Object.assign({}, leaveApplication, {
      applicationDate:
        leaveApplication.applicationDate && leaveApplication.applicationDate.isValid()
          ? leaveApplication.applicationDate.format(DATE_FORMAT)
          : undefined,
      startDate:
        leaveApplication.startDate && leaveApplication.startDate.isValid() ? leaveApplication.startDate.format(DATE_FORMAT) : undefined,
      endDate: leaveApplication.endDate && leaveApplication.endDate.isValid() ? leaveApplication.endDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.applicationDate = res.body.applicationDate ? dayjs(res.body.applicationDate) : undefined;
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((leaveApplication: ILeaveApplication) => {
        leaveApplication.applicationDate = leaveApplication.applicationDate ? dayjs(leaveApplication.applicationDate) : undefined;
        leaveApplication.startDate = leaveApplication.startDate ? dayjs(leaveApplication.startDate) : undefined;
        leaveApplication.endDate = leaveApplication.endDate ? dayjs(leaveApplication.endDate) : undefined;
      });
    }
    return res;
  }

  loadByYear(year: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaveBalanceEndUserView[]>(`${this.resourceUrl}/my-leave-summary/${year}/`, {
      params: options,
      observe: 'response',
    });
  }
}
