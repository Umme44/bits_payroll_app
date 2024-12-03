import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployeeSalary, NewEmployeeSalary } from '../employee-salary.model';
import {Fraction, IFraction} from "../../../shared/model/fraction.model";

export type PartialUpdateEmployeeSalary = Partial<IEmployeeSalary> & Pick<IEmployeeSalary, 'id'>;

type RestOf<T extends IEmployeeSalary | NewEmployeeSalary> = Omit<
  T,
  | 'salaryGenerationDate'
  | 'createdAt'
  | 'updatedAt'
  | 'joiningDate'
  | 'confirmationDate'
  | 'attendanceRegularisationStartDate'
  | 'attendanceRegularisationEndDate'
> & {
  salaryGenerationDate?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
  joiningDate?: string | null;
  confirmationDate?: string | null;
  attendanceRegularisationStartDate?: string | null;
  attendanceRegularisationEndDate?: string | null;
};

export type RestEmployeeSalary = RestOf<IEmployeeSalary>;

export type NewRestEmployeeSalary = RestOf<NewEmployeeSalary>;

export type PartialUpdateRestEmployeeSalary = RestOf<PartialUpdateEmployeeSalary>;

export type EntityResponseType = HttpResponse<IEmployeeSalary>;
export type EntityArrayResponseType = HttpResponse<IEmployeeSalary[]>;

export type FractionResponseType = HttpResponse<Fraction[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeSalaryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/employee-salaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employeeSalary: NewEmployeeSalary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeSalary);
    return this.http
      .post<RestEmployeeSalary>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(employeeSalary: IEmployeeSalary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeSalary);
    return this.http
      .put<RestEmployeeSalary>(`${this.resourceUrl}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(employeeSalary: PartialUpdateEmployeeSalary): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employeeSalary);
    return this.http
      .patch<RestEmployeeSalary>(`${this.resourceUrl}/${this.getEmployeeSalaryIdentifier(employeeSalary)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmployeeSalary>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  findBreakDownForId(id: number): Observable<FractionResponseType> {
    return this.http
      .get<IFraction[]>(`${this.resourceUrl}/breakdown/${id}`, { observe: 'response' })
      .pipe(map((res: FractionResponseType) => this.convertFractionArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmployeeSalary[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  queryWithParams(year: number, month: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmployeeSalary[]>(`${this.resourceUrl}/${year}/${month}`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  queryWithEmployeeId(employeeId: number, year: number, month: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<RestEmployeeSalary[]>(`${this.resourceUrl}/${employeeId}/${year}/${month}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  finalize(year: number, month: number): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(`${SERVER_API_URL}api/payroll-mgt/salary-finalize/${year}/${month}`, { observe: 'response' });
  }

  holdEmployeeSalary(employeeSalaryId: number): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(`${this.resourceUrl}/hold/${employeeSalaryId}`, { observe: 'response' });
  }

  unHoldEmployeeSalary(employeeSalaryId: number): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(`${this.resourceUrl}/unHold/${employeeSalaryId}`, { observe: 'response' });
  }

  makeSalaryVisibleToEmployee(id: number, year: number, month: string): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.resourceUrl + `/make-employee-salary-visible/${id}/${year}/${month}`, { observe: 'response' });
  }

  makeSalaryHiddenFromEmployee(id: number, year: number, month: string): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.resourceUrl + `/make-employee-salary-hidden/${id}/${year}/${month}`, { observe: 'response' });
  }

  getEmployeeSalaryIdentifier(employeeSalary: Pick<IEmployeeSalary, 'id'>): number {
    return employeeSalary.id;
  }

  compareEmployeeSalary(o1: Pick<IEmployeeSalary, 'id'> | null, o2: Pick<IEmployeeSalary, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeeSalaryIdentifier(o1) === this.getEmployeeSalaryIdentifier(o2) : o1 === o2;
  }

  addEmployeeSalaryToCollectionIfMissing<Type extends Pick<IEmployeeSalary, 'id'>>(
    employeeSalaryCollection: Type[],
    ...employeeSalariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employeeSalaries: Type[] = employeeSalariesToCheck.filter(isPresent);
    if (employeeSalaries.length > 0) {
      const employeeSalaryCollectionIdentifiers = employeeSalaryCollection.map(
        employeeSalaryItem => this.getEmployeeSalaryIdentifier(employeeSalaryItem)!
      );
      const employeeSalariesToAdd = employeeSalaries.filter(employeeSalaryItem => {
        const employeeSalaryIdentifier = this.getEmployeeSalaryIdentifier(employeeSalaryItem);
        if (employeeSalaryCollectionIdentifiers.includes(employeeSalaryIdentifier)) {
          return false;
        }
        employeeSalaryCollectionIdentifiers.push(employeeSalaryIdentifier);
        return true;
      });
      return [...employeeSalariesToAdd, ...employeeSalaryCollection];
    }
    return employeeSalaryCollection;
  }

  protected convertDateFromClient<T extends IEmployeeSalary | NewEmployeeSalary | PartialUpdateEmployeeSalary>(
    employeeSalary: T
  ): RestOf<T> {
    return {
      ...employeeSalary,
      salaryGenerationDate: employeeSalary.salaryGenerationDate?.format(DATE_FORMAT) ?? null,
      createdAt: employeeSalary.createdAt?.toJSON() ?? null,
      updatedAt: employeeSalary.updatedAt?.toJSON() ?? null,
      joiningDate: employeeSalary.joiningDate?.format(DATE_FORMAT) ?? null,
      confirmationDate: employeeSalary.confirmationDate?.format(DATE_FORMAT) ?? null,
      attendanceRegularisationStartDate: employeeSalary.attendanceRegularisationStartDate?.format(DATE_FORMAT) ?? null,
      attendanceRegularisationEndDate: employeeSalary.attendanceRegularisationEndDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restEmployeeSalary: RestEmployeeSalary): IEmployeeSalary {
    return {
      ...restEmployeeSalary,
      salaryGenerationDate: restEmployeeSalary.salaryGenerationDate ? dayjs(restEmployeeSalary.salaryGenerationDate) : undefined,
      createdAt: restEmployeeSalary.createdAt ? dayjs(restEmployeeSalary.createdAt) : undefined,
      updatedAt: restEmployeeSalary.updatedAt ? dayjs(restEmployeeSalary.updatedAt) : undefined,
      joiningDate: restEmployeeSalary.joiningDate ? dayjs(restEmployeeSalary.joiningDate) : undefined,
      confirmationDate: restEmployeeSalary.confirmationDate ? dayjs(restEmployeeSalary.confirmationDate) : undefined,
      attendanceRegularisationStartDate: restEmployeeSalary.attendanceRegularisationStartDate
        ? dayjs(restEmployeeSalary.attendanceRegularisationStartDate)
        : undefined,
      attendanceRegularisationEndDate: restEmployeeSalary.attendanceRegularisationEndDate
        ? dayjs(restEmployeeSalary.attendanceRegularisationEndDate)
        : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmployeeSalary>): HttpResponse<IEmployeeSalary> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmployeeSalary[]>): HttpResponse<IEmployeeSalary[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  protected convertFractionArrayFromServer(res: FractionResponseType): FractionResponseType {
    if (res.body) {
      res.body.forEach((fraction: IFraction) => {
        fraction.startDate = fraction.startDate ? dayjs(fraction.startDate) : undefined;
        fraction.endDate = fraction.endDate ? dayjs(fraction.endDate) : undefined;
      });
    }
    return res;
  }
}
