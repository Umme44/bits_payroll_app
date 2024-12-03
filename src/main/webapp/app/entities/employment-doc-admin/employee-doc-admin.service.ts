import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { IEmployeeNOC } from './model/employee-noc.model';
import { createRequestOption } from '../../core/request/request-util';
import { DATE_FORMAT } from '../../config/input.constants';
import dayjs from 'dayjs/esm';

type EmployeeNocEntityResponseType = HttpResponse<IEmployeeNOC>;
type EmployeeNocEntityArrayResponseType = HttpResponse<IEmployeeNOC[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeDocsService {
  public resourceUrlForEmployeeNoc = SERVER_API_URL + 'api/employee-mgt/employee-no-objection-certificates';

  constructor(protected http: HttpClient) {}

  findEmployeeNocById(id: number): Observable<EmployeeNocEntityResponseType> {
    return this.http
      .get<IEmployeeNOC>(`${this.resourceUrlForEmployeeNoc}/${id}`, { observe: 'response' })
      .pipe(map((res: EmployeeNocEntityResponseType) => this.convertDateFromServer(res)));
  }

  queryEmployeeNoc(req?: any): Observable<EmployeeNocEntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmployeeNOC[]>(this.resourceUrlForEmployeeNoc, { params: options, observe: 'response' })
      .pipe(map((res: EmployeeNocEntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(employeeNOC: IEmployeeNOC): IEmployeeNOC {
    const copy: IEmployeeNOC = Object.assign({}, employeeNOC, {
      leaveStartDate:
        employeeNOC.leaveStartDate && employeeNOC.leaveStartDate.isValid() ? employeeNOC.leaveStartDate.format(DATE_FORMAT) : undefined,
      leaveEndDate:
        employeeNOC.leaveEndDate && employeeNOC.leaveEndDate.isValid() ? employeeNOC.leaveEndDate.format(DATE_FORMAT) : undefined,
      issueDate: employeeNOC.issueDate && employeeNOC.issueDate.isValid() ? employeeNOC.issueDate.format(DATE_FORMAT) : undefined,
      createdAt: employeeNOC.createdAt && employeeNOC.createdAt.isValid() ? employeeNOC.createdAt.format(DATE_FORMAT) : undefined,
      updatedAt: employeeNOC.updatedAt && employeeNOC.updatedAt.isValid() ? employeeNOC.updatedAt.format(DATE_FORMAT) : undefined,
      generatedAt: employeeNOC.generatedAt && employeeNOC.generatedAt.isValid() ? employeeNOC.generatedAt.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EmployeeNocEntityResponseType): EmployeeNocEntityResponseType {
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

  protected convertDateArrayFromServer(res: EmployeeNocEntityArrayResponseType): EmployeeNocEntityArrayResponseType {
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
