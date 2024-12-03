import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { calculateDurationOnDays } from '../../../shared/util/date-util';
import { IFlexScheduleApplication } from '../legacy-model/flex-schedule-application.model';
import { createRequestOption } from '../../../core/request/request-util';
import { DATE_FORMAT } from '../../../config/input.constants';

type EntityResponseType = HttpResponse<IFlexScheduleApplication>;
type EntityArrayResponseType = HttpResponse<IFlexScheduleApplication[]>;

@Injectable({ providedIn: 'root' })
export class FlexScheduleApplicationService {
  public resourceUrl = SERVER_API_URL + 'api/attendance-mgt/flex-schedule-applications';

  constructor(protected http: HttpClient) {}

  create(flexScheduleApplication: IFlexScheduleApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(flexScheduleApplication);
    return this.http
      .post<IFlexScheduleApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(flexScheduleApplication: IFlexScheduleApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(flexScheduleApplication);
    return this.http
      .put<IFlexScheduleApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFlexScheduleApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    Object.keys(req).forEach(k => (req[k] === null || req[k] === undefined) && delete req[k]);
    const options = createRequestOption(req);
    return this.http
      .get<IFlexScheduleApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  exportAsFlexScheduleReportDataInXLByDateRange(req?: any): Observable<Blob> {
    Object.keys(req).forEach(k => (req[k] === null || req[k] === undefined) && delete req[k]);
    const options = createRequestOption(req);
    return this.http.get(SERVER_API_URL + 'api/payroll-mgt/flex-schedule-applications-export-report', {
      params: options,
      responseType: 'blob',
    });
  }

  protected convertDateFromClient(flexScheduleApplication: IFlexScheduleApplication): IFlexScheduleApplication {
    const copy: IFlexScheduleApplication = Object.assign({}, flexScheduleApplication, {
      effectiveFrom:
        flexScheduleApplication.effectiveFrom && flexScheduleApplication.effectiveFrom.isValid()
          ? flexScheduleApplication.effectiveFrom.format(DATE_FORMAT)
          : undefined,
      effectiveTo:
        flexScheduleApplication.effectiveTo && flexScheduleApplication.effectiveTo.isValid()
          ? flexScheduleApplication.effectiveTo.format(DATE_FORMAT)
          : undefined,
      sanctionedAt:
        flexScheduleApplication.sanctionedAt && flexScheduleApplication.sanctionedAt.isValid()
          ? flexScheduleApplication.sanctionedAt.toJSON()
          : undefined,
      appliedAt:
        flexScheduleApplication.appliedAt && flexScheduleApplication.appliedAt.isValid()
          ? flexScheduleApplication.appliedAt.format(DATE_FORMAT)
          : undefined,
      createdAt:
        flexScheduleApplication.createdAt && flexScheduleApplication.createdAt.isValid()
          ? flexScheduleApplication.createdAt.toJSON()
          : undefined,
      updatedAt:
        flexScheduleApplication.updatedAt && flexScheduleApplication.updatedAt.isValid()
          ? flexScheduleApplication.updatedAt.toJSON()
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.effectiveFrom = res.body.effectiveFrom ? dayjs(res.body.effectiveFrom) : undefined;
      res.body.effectiveTo = res.body.effectiveTo ? dayjs(res.body.effectiveTo) : undefined;
      res.body.sanctionedAt = res.body.sanctionedAt ? dayjs(res.body.sanctionedAt) : undefined;
      res.body.appliedAt = res.body.appliedAt ? dayjs(res.body.appliedAt) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
      res.body.durationAsDays = calculateDurationOnDays(res.body.effectiveFrom, res.body.effectiveTo, true);
      res.body.inTime = res.body.inTime ? dayjs(res.body.inTime) : undefined;
      res.body.outTime = res.body.outTime ? dayjs(res.body.outTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((flexScheduleApplication: IFlexScheduleApplication) => {
        flexScheduleApplication.effectiveFrom = flexScheduleApplication.effectiveFrom
          ? dayjs(flexScheduleApplication.effectiveFrom)
          : undefined;
        flexScheduleApplication.effectiveTo = flexScheduleApplication.effectiveTo ? dayjs(flexScheduleApplication.effectiveTo) : undefined;
        flexScheduleApplication.sanctionedAt = flexScheduleApplication.sanctionedAt
          ? dayjs(flexScheduleApplication.sanctionedAt)
          : undefined;
        flexScheduleApplication.appliedAt = flexScheduleApplication.appliedAt ? dayjs(flexScheduleApplication.appliedAt) : undefined;
        flexScheduleApplication.createdAt = flexScheduleApplication.createdAt ? dayjs(flexScheduleApplication.createdAt) : undefined;
        flexScheduleApplication.updatedAt = flexScheduleApplication.updatedAt ? dayjs(flexScheduleApplication.updatedAt) : undefined;
        flexScheduleApplication.inTime = flexScheduleApplication.inTime ? dayjs(flexScheduleApplication.inTime) : undefined;
        flexScheduleApplication.outTime = flexScheduleApplication.outTime ? dayjs(flexScheduleApplication.outTime) : undefined;
        flexScheduleApplication.durationAsDays = calculateDurationOnDays(
          flexScheduleApplication.effectiveFrom,
          flexScheduleApplication.effectiveTo,
          true
        );
      });
    }
    return res;
  }
}
