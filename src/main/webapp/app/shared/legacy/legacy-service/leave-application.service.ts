import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { IDateRangeDTO } from 'app/shared/model/DateRangeDTO';
import { IAttendanceTimeSheet } from 'app/shared/model/attendance-time-sheet.model';
import { ILeaveApplication } from '../legacy-model/leave-application.model';
import { createRequestOption } from '../../../core/request/request-util';
import { DATE_FORMAT } from '../../../config/input.constants';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

type EntityResponseType = HttpResponse<ILeaveApplication>;
type EntityArrayResponseType = HttpResponse<ILeaveApplication[]>;

@Injectable({ providedIn: 'root' })
export class LeaveApplicationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/leave-applications');
  public resourceUrlUserLeaveApplication = this.applicationConfigService.getEndpointFor('api/common/user-leave-application');
  public resourceUrlForSearch = this.applicationConfigService.getEndpointFor('/api/attendance-mgt/leave-applications-search/');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaveApplication: ILeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .post<ILeaveApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaveApplication: ILeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .put<ILeaveApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  userLeaveApplication(leaveApplication: ILeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .post<ILeaveApplication>(this.resourceUrlUserLeaveApplication, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  userLeaveApplicationUpdate(leaveApplication: ILeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .put<ILeaveApplication>(this.resourceUrlUserLeaveApplication, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  deleteUserLeaveApplication(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrlUserLeaveApplication}/${id}`, { observe: 'response' });
  }

  userLeaveApplicationStatusAndHistory(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaveApplication[]>(this.resourceUrlUserLeaveApplication, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
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

  queryWithEmployeeAndDatesAndLeaveType(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaveApplication[]>(this.resourceUrlForSearch, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  calculateLeaveDuration(leaveApplication: ILeaveApplication): Observable<HttpResponse<number>> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http.post<number>(this.resourceUrl + '/calculate-duration', copy, { observe: 'response' });
  }

  getMonthlyRemainingCasualLeave(leaveApplication: ILeaveApplication): Observable<HttpResponse<number>> {
    const afterDateConverted = this.convertDateFromClient(leaveApplication);
    return this.http.post<number>(this.resourceUrl + '/monthly-remaining-casual-leave', afterDateConverted, { observe: 'response' });
  }

  hasAnyApprovedAndPendingLeaveByEmployeeIdAndDateRange(leaveApplication: ILeaveApplication): Observable<HttpResponse<boolean>> {
    const afterDateConverted = this.convertDateFromClient(leaveApplication);
    return this.http.post<boolean>(this.resourceUrl + '/find-by-employee-id-and-date-range', afterDateConverted, { observe: 'response' });
  }

  getRemainingLeaveBalance(leaveApplication: ILeaveApplication): Observable<HttpResponse<number>> {
    const afterDateConverted = this.convertDateFromClient(leaveApplication);
    return this.http.post<number>(this.resourceUrl + '/leave-balance', afterDateConverted, { observe: 'response' });
  }

  applyLeaveAndApprove(leaveApplication: ILeaveApplication): Observable<HttpResponse<boolean>> {
    const afterDateConverted = this.convertDateFromClient(leaveApplication);
    return this.http.post<boolean>(this.resourceUrl + '/apply-and-approve', afterDateConverted, { observe: 'response' });
  }

  findApplicationsByDateRange(employeeId: number, dateRange: IDateRangeDTO): Observable<HttpResponse<IAttendanceTimeSheet>> {
    const copy = this.convertLeaveApplicationDateRangeFromClient(dateRange);
    return this.http
      .post<IAttendanceTimeSheet>(this.resourceUrl + `/${employeeId}` + '/pending-applications-between-date-range', copy, {
        observe: 'response',
      })
      .pipe(map((res: HttpResponse<IAttendanceTimeSheet>) => this.convertApplicationsDateFromServer(res)));
  }

  protected convertLeaveApplicationDateRangeFromClient(dateRangeDTO: IDateRangeDTO): IDateRangeDTO {
    const copy: IDateRangeDTO = Object.assign({}, dateRangeDTO, {
      startDate:
        dateRangeDTO.startDate && dayjs(dateRangeDTO.startDate).isValid() ? dayjs(dateRangeDTO.startDate).format(DATE_FORMAT) : undefined,
      endDate: dateRangeDTO.endDate && dayjs(dateRangeDTO.endDate).isValid() ? dayjs(dateRangeDTO.endDate).format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertApplicationsDateFromServer(res: HttpResponse<IAttendanceTimeSheet>): HttpResponse<IAttendanceTimeSheet> {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
      res.body.inTime = res.body.inTime ? dayjs(res.body.inTime) : undefined;
      res.body.outTime = res.body.outTime ? dayjs(res.body.outTime) : undefined;
    }
    return res;
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
}
