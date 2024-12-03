import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IEmployeeNOC } from '../../../shared/legacy/legacy-model/employee-noc.model';
import { createRequestOption } from '../../../core/request/request-util';

import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from '../../../config/input.constants';

type EntityResponseType = HttpResponse<IEmployeeNOC>;
type EntityArrayResponseType = HttpResponse<IEmployeeNOC[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeNOCService {
  public resourceUrl = SERVER_API_URL + 'api/common/employee-no-objection-certificates';

  constructor(protected http: HttpClient) {}

  create(employeeNOC: IEmployeeNOC): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeNOC);
    return this.http
      .post<IEmployeeNOC>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(employeeNOC: IEmployeeNOC): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeNOC);
    return this.http
      .put<IEmployeeNOC>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmployeeNOC>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmployeeNOC[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  hasAnyApprovedLeaveApplicationWithinDateRange(startingDate: dayjs.Dayjs, endingDate: dayjs.Dayjs): Observable<HttpResponse<boolean>> {
    const startDate = startingDate.format(DATE_FORMAT);
    const endDate = endingDate.format(DATE_FORMAT);
    const options = createRequestOption({ startDate, endDate });
    return this.http.get<boolean>(this.resourceUrl + '/has-any-approved-leave-application', { params: options, observe: 'response' });
  }

  calculateLeaveDays(startingDate: dayjs.Dayjs, endingDate: dayjs.Dayjs): Observable<HttpResponse<number>> {
    const startDate = startingDate.format(DATE_FORMAT);
    const endDate = endingDate.format(DATE_FORMAT);
    const options = createRequestOption({ startDate, endDate });
    return this.http.get<number>(this.resourceUrl + `/calculate-leave-days`, { params: options, observe: 'response' });
  }

  protected convertDateFromClient(employeeNOC: IEmployeeNOC): IEmployeeNOC {
    const copy: IEmployeeNOC = Object.assign({}, employeeNOC, {
      leaveStartDate:
        employeeNOC.leaveStartDate && employeeNOC.leaveStartDate.isValid() ? employeeNOC.leaveStartDate.format(DATE_FORMAT) : undefined,
      leaveEndDate:
        employeeNOC.leaveEndDate && employeeNOC.leaveEndDate.isValid() ? employeeNOC.leaveEndDate.format(DATE_FORMAT) : undefined,
      issueDate: employeeNOC.issueDate && employeeNOC.issueDate.isValid() ? employeeNOC.issueDate.format(DATE_FORMAT) : undefined,
      createdAt: employeeNOC.createdAt && employeeNOC.createdAt.isValid() ? employeeNOC.createdAt.toJSON() : undefined,
      updatedAt: employeeNOC.updatedAt && employeeNOC.updatedAt.isValid() ? employeeNOC.updatedAt.toJSON() : undefined,
      generatedAt: employeeNOC.generatedAt && employeeNOC.generatedAt.isValid() ? employeeNOC.generatedAt.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.leaveStartDate = res.body.leaveStartDate ? dayjs(res.body.leaveStartDate) : undefined;
      res.body.leaveEndDate = res.body.leaveEndDate ? dayjs(res.body.leaveEndDate) : undefined;
      res.body.issueDate = res.body.issueDate ? dayjs(res.body.issueDate) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
      res.body.generatedAt = res.body.generatedAt ? dayjs(res.body.generatedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((employeeNOC: IEmployeeNOC) => {
        employeeNOC.leaveStartDate = employeeNOC.leaveStartDate ? dayjs(employeeNOC.leaveStartDate) : undefined;
        employeeNOC.leaveEndDate = employeeNOC.leaveEndDate ? dayjs(employeeNOC.leaveEndDate) : undefined;
        employeeNOC.issueDate = employeeNOC.issueDate ? dayjs(employeeNOC.issueDate) : undefined;
        employeeNOC.createdAt = employeeNOC.createdAt ? dayjs(employeeNOC.createdAt) : undefined;
        employeeNOC.updatedAt = employeeNOC.updatedAt ? dayjs(employeeNOC.updatedAt) : undefined;
        employeeNOC.generatedAt = employeeNOC.generatedAt ? dayjs(employeeNOC.generatedAt) : undefined;
      });
    }
    return res;
  }
}
