import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ISalaryCertificate } from '../../../shared/legacy/legacy-model/salary-certificate.model';
import { createRequestOption } from '../../../core/request/request-util';
import { DATE_FORMAT } from '../../../config/input.constants';
import dayjs from 'dayjs/esm';
import { IEmployeeSalary } from '../../../entities/employee-salary/employee-salary.model';

type EntityResponseType = HttpResponse<ISalaryCertificate>;
type EntityArrayResponseType = HttpResponse<ISalaryCertificate[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeSalaryCertificateService {
  public userResourceUrl = SERVER_API_URL + 'api/common/salary-certificates';
  public resourceUrl = SERVER_API_URL + 'api/payroll-mgt/salary-certificates';

  constructor(protected http: HttpClient) {}

  getListOfSalaries(): Observable<HttpResponse<IEmployeeSalary[]>> {
    return this.http.get<IEmployeeSalary[]>(this.userResourceUrl + '/list-of-salaries', { observe: 'response' });
  }
  getEmployeeListOfSalaries(): Observable<HttpResponse<IEmployeeSalary[]>> {
    return this.http.get<IEmployeeSalary[]>(this.resourceUrl + '/list-of-salaries', { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISalaryCertificate[]>(this.userResourceUrl + '/all', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  create(salaryCertificate: ISalaryCertificate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salaryCertificate);
    return this.http
      .post<ISalaryCertificate>(this.userResourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISalaryCertificate>(`${this.userResourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
  update(salaryCertificate: ISalaryCertificate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(salaryCertificate);
    return this.http
      .put<ISalaryCertificate>(this.userResourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.userResourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeeSalaryByCertificateId(id: any): Observable<HttpResponse<IEmployeeSalary>> {
    return this.http.get<IEmployeeSalary>(`${this.userResourceUrl}/${id}/employee-salary`, { observe: 'response' });
  }

  protected convertDateFromClient(salaryCertificate: ISalaryCertificate): ISalaryCertificate {
    const copy: ISalaryCertificate = Object.assign({}, salaryCertificate, {
      createdAt:
        salaryCertificate.createdAt && salaryCertificate.createdAt.isValid() ? salaryCertificate.createdAt.format(DATE_FORMAT) : undefined,
      updatedAt:
        salaryCertificate.updatedAt && salaryCertificate.updatedAt.isValid() ? salaryCertificate.updatedAt.format(DATE_FORMAT) : undefined,
      sanctionAt:
        salaryCertificate.sanctionAt && salaryCertificate.sanctionAt.isValid()
          ? salaryCertificate.sanctionAt.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
      res.body.sanctionAt = res.body.sanctionAt ? dayjs(res.body.sanctionAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((salaryCertificate: ISalaryCertificate) => {
        salaryCertificate.createdAt = salaryCertificate.createdAt ? dayjs(salaryCertificate.createdAt) : undefined;
        salaryCertificate.updatedAt = salaryCertificate.updatedAt ? dayjs(salaryCertificate.updatedAt) : undefined;
        salaryCertificate.sanctionAt = salaryCertificate.sanctionAt ? dayjs(salaryCertificate.sanctionAt) : undefined;
      });
    }
    return res;
  }
}
