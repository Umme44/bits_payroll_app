import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { createRequestOption } from '../../../../core/request/request-util';
import { DATE_FORMAT } from '../../../../config/input.constants';
import { IUserWorkFromHomeApplication } from '../user-work-from-home-application.model';

type EntityResponseType = HttpResponse<IUserWorkFromHomeApplication>;
type EntityArrayResponseType = HttpResponse<IUserWorkFromHomeApplication[]>;

@Injectable({ providedIn: 'root' })
export class UserWorkFromHomeApplicationService {
  public resourceUrl = SERVER_API_URL + 'api/common/work-from-home-applications';

  constructor(protected http: HttpClient) {}

  create(userWorkFromHomeApplication: IUserWorkFromHomeApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userWorkFromHomeApplication);
    return this.http
      .post<IUserWorkFromHomeApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(workFromHomeApplication: IUserWorkFromHomeApplication): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workFromHomeApplication);
    return this.http
      .put<IUserWorkFromHomeApplication>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUserWorkFromHomeApplication>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUserWorkFromHomeApplication[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryFindByEmployeeId(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUserWorkFromHomeApplication[]>(this.resourceUrl + '/employeeId', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(workFromHomeApplication: IUserWorkFromHomeApplication): IUserWorkFromHomeApplication {
    const copy: IUserWorkFromHomeApplication = Object.assign({}, workFromHomeApplication, {
      startDate:
        workFromHomeApplication.startDate && workFromHomeApplication.startDate.isValid()
          ? workFromHomeApplication.startDate.format(DATE_FORMAT)
          : undefined,
      endDate:
        workFromHomeApplication.endDate && workFromHomeApplication.endDate.isValid()
          ? workFromHomeApplication.endDate.format(DATE_FORMAT)
          : undefined,
      appliedAt:
        workFromHomeApplication.appliedAt && workFromHomeApplication.appliedAt.isValid()
          ? workFromHomeApplication.appliedAt.format(DATE_FORMAT)
          : undefined,
      updatedAt:
        workFromHomeApplication.updatedAt && workFromHomeApplication.updatedAt.isValid()
          ? workFromHomeApplication.updatedAt.toJSON()
          : undefined,
      createdAt:
        workFromHomeApplication.createdAt && workFromHomeApplication.createdAt.isValid()
          ? workFromHomeApplication.createdAt.toJSON()
          : undefined,
      sanctionedAt:
        workFromHomeApplication.sanctionedAt && workFromHomeApplication.sanctionedAt.isValid()
          ? workFromHomeApplication.sanctionedAt.toJSON()
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
      res.body.appliedAt = res.body.appliedAt ? dayjs(res.body.appliedAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.sanctionedAt = res.body.sanctionedAt ? dayjs(res.body.sanctionedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((workFromHomeApplication: IUserWorkFromHomeApplication) => {
        workFromHomeApplication.startDate = workFromHomeApplication.startDate ? dayjs(workFromHomeApplication.startDate) : undefined;
        workFromHomeApplication.endDate = workFromHomeApplication.endDate ? dayjs(workFromHomeApplication.endDate) : undefined;
        workFromHomeApplication.appliedAt = workFromHomeApplication.appliedAt ? dayjs(workFromHomeApplication.appliedAt) : undefined;
        workFromHomeApplication.updatedAt = workFromHomeApplication.updatedAt ? dayjs(workFromHomeApplication.updatedAt) : undefined;
        workFromHomeApplication.createdAt = workFromHomeApplication.createdAt ? dayjs(workFromHomeApplication.createdAt) : undefined;
        workFromHomeApplication.sanctionedAt = workFromHomeApplication.sanctionedAt
          ? dayjs(workFromHomeApplication.sanctionedAt)
          : undefined;
      });
    }
    return res;
  }

  isAppliedForWorkFromHome(workFromHomeApplication: IUserWorkFromHomeApplication): Observable<HttpResponse<Boolean>> {
    const copy = this.convertDateFromClient(workFromHomeApplication);
    return this.http.post<Boolean>(`${this.resourceUrl}/isApplied`, copy, { observe: 'response' });
  }
}
