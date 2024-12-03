import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILeaveApplication, NewLeaveApplication } from '../leave-application.model';
import {IAttendanceTimeSheet} from "../../../shared/model/attendance-time-sheet.model";
import {IDateRangeDTO} from "../../../shared/model/DateRangeDTO";

export type PartialUpdateLeaveApplication = Partial<ILeaveApplication> & Pick<ILeaveApplication, 'id'>;

type RestOf<T extends ILeaveApplication | NewLeaveApplication> = Omit<T, 'applicationDate' | 'startDate' | 'endDate' | 'sanctionedAt'> & {
  applicationDate?: string | null;
  startDate?: string | null;
  endDate?: string | null;
  sanctionedAt?: string | null;
};

export type RestLeaveApplication = RestOf<ILeaveApplication>;

export type NewRestLeaveApplication = RestOf<NewLeaveApplication>;

export type PartialUpdateRestLeaveApplication = RestOf<PartialUpdateLeaveApplication>;

export type EntityResponseType = HttpResponse<ILeaveApplication>;
export type EntityArrayResponseType = HttpResponse<ILeaveApplication[]>;

@Injectable({ providedIn: 'root' })
export class LeaveApplicationService {
  public resourceUrlForSearch = SERVER_API_URL + '/api/attendance-mgt/leave-applications-search/';
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/leave-applications');
 /* protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/leave-applications');*/

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaveApplication: NewLeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .post<RestLeaveApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(leaveApplication: ILeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .put<RestLeaveApplication>(`${this.resourceUrl}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(leaveApplication: PartialUpdateLeaveApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http
      .patch<RestLeaveApplication>(`${this.resourceUrl}/${this.getLeaveApplicationIdentifier(leaveApplication)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLeaveApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  findApplicationsByDateRange(employeeId: number, dateRange: IDateRangeDTO): Observable<HttpResponse<IAttendanceTimeSheet>> {
    const copy = this.convertLeaveApplicationDateRangeFromClient(dateRange);
    return this.http
      .post<IAttendanceTimeSheet>(this.resourceUrl + `/${employeeId}` + '/pending-applications-between-date-range', copy, {
        observe: 'response',
      })
      .pipe(map((res: HttpResponse<IAttendanceTimeSheet>) => this.convertApplicationsDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLeaveApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  calculateLeaveDuration(leaveApplication: ILeaveApplication): Observable<HttpResponse<number>> {
    const copy = this.convertDateFromClient(leaveApplication);
    return this.http.post<number>(this.resourceUrl + '/calculate-duration', copy, { observe: 'response' });
  }

  getLeaveApplicationIdentifier(leaveApplication: Pick<ILeaveApplication, 'id'>): number {
    return leaveApplication.id;
  }

  compareLeaveApplication(o1: Pick<ILeaveApplication, 'id'> | null, o2: Pick<ILeaveApplication, 'id'> | null): boolean {
    return o1 && o2 ? this.getLeaveApplicationIdentifier(o1) === this.getLeaveApplicationIdentifier(o2) : o1 === o2;
  }

  addLeaveApplicationToCollectionIfMissing<Type extends Pick<ILeaveApplication, 'id'>>(
    leaveApplicationCollection: Type[],
    ...leaveApplicationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const leaveApplications: Type[] = leaveApplicationsToCheck.filter(isPresent);
    if (leaveApplications.length > 0) {
      const leaveApplicationCollectionIdentifiers = leaveApplicationCollection.map(
        leaveApplicationItem => this.getLeaveApplicationIdentifier(leaveApplicationItem)!
      );
      const leaveApplicationsToAdd = leaveApplications.filter(leaveApplicationItem => {
        const leaveApplicationIdentifier = this.getLeaveApplicationIdentifier(leaveApplicationItem);
        if (leaveApplicationCollectionIdentifiers.includes(leaveApplicationIdentifier)) {
          return false;
        }
        leaveApplicationCollectionIdentifiers.push(leaveApplicationIdentifier);
        return true;
      });
      return [...leaveApplicationsToAdd, ...leaveApplicationCollection];
    }
    return leaveApplicationCollection;
  }

  protected convertDateFromClient<T extends ILeaveApplication | NewLeaveApplication | PartialUpdateLeaveApplication>(
    leaveApplication: T
  ): RestOf<T> {
    return {
      ...leaveApplication,
      applicationDate: leaveApplication.applicationDate?.format(DATE_FORMAT) ?? null,
      startDate: leaveApplication.startDate?.format(DATE_FORMAT) ?? null,
      endDate: leaveApplication.endDate?.format(DATE_FORMAT) ?? null,
      sanctionedAt: leaveApplication.sanctionedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restLeaveApplication: RestLeaveApplication): ILeaveApplication {
    return {
      ...restLeaveApplication,
      applicationDate: restLeaveApplication.applicationDate ? dayjs(restLeaveApplication.applicationDate) : undefined,
      startDate: restLeaveApplication.startDate ? dayjs(restLeaveApplication.startDate) : undefined,
      endDate: restLeaveApplication.endDate ? dayjs(restLeaveApplication.endDate) : undefined,
      sanctionedAt: restLeaveApplication.sanctionedAt ? dayjs(restLeaveApplication.sanctionedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLeaveApplication>): HttpResponse<ILeaveApplication> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLeaveApplication[]>): HttpResponse<ILeaveApplication[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  queryWithEmployeeAndDatesAndLeaveType(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req,);
    return this.http
      .get<ILeaveApplication[]>(this.resourceUrlForSearch, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
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

  protected convertLeaveApplicationDateRangeFromClient(dateRangeDTO: IDateRangeDTO): IDateRangeDTO {
    const copy: IDateRangeDTO = Object.assign({}, dateRangeDTO, {
      startDate:
        dateRangeDTO.startDate && dayjs(dateRangeDTO.startDate).isValid() ? dayjs(dateRangeDTO.startDate).format(DATE_FORMAT) : undefined,
      endDate:
        dateRangeDTO.endDate && dayjs(dateRangeDTO.endDate).isValid() ? dayjs(dateRangeDTO.endDate).format(DATE_FORMAT) : undefined,
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
}
