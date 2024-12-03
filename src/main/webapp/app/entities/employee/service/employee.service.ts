import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEmployee, NewEmployee } from '../employee.model';

export type PartialUpdateEmployee = Partial<IEmployee> & Pick<IEmployee, 'id'>;

type RestOf<T extends IEmployee | NewEmployee> = Omit<
  T,
  | 'dateOfBirth'
  | 'dateOfMarriage'
  | 'dateOfJoining'
  | 'dateOfConfirmation'
  | 'probationPeriodExtendedTo'
  | 'passportIssuedDate'
  | 'passportExpiryDate'
  | 'probationPeriodEndDate'
  | 'contractPeriodExtendedTo'
  | 'contractPeriodEndDate'
  | 'allowance01EffectiveFrom'
  | 'allowance01EffectiveTo'
  | 'allowance02EffectiveFrom'
  | 'allowance02EffectiveTo'
  | 'allowance03EffectiveFrom'
  | 'allowance03EffectiveTo'
  | 'allowance04EffectiveFrom'
  | 'allowance04EffectiveTo'
  | 'allowance05EffectiveFrom'
  | 'allowance05EffectiveTo'
  | 'allowance06EffectiveFrom'
  | 'allowance06EffectiveTo'
  | 'createdAt'
  | 'updatedAt'
  | 'currentInTime'
  | 'currentOutTime'
  | 'onlineAttendanceSanctionedAt'
  | 'lastWorkingDay'
> & {
  dateOfBirth?: string | null;
  dateOfMarriage?: string | null;
  dateOfJoining?: string | null;
  dateOfConfirmation?: string | null;
  probationPeriodExtendedTo?: string | null;
  passportIssuedDate?: string | null;
  passportExpiryDate?: string | null;
  probationPeriodEndDate?: string | null;
  contractPeriodExtendedTo?: string | null;
  contractPeriodEndDate?: string | null;
  allowance01EffectiveFrom?: string | null;
  allowance01EffectiveTo?: string | null;
  allowance02EffectiveFrom?: string | null;
  allowance02EffectiveTo?: string | null;
  allowance03EffectiveFrom?: string | null;
  allowance03EffectiveTo?: string | null;
  allowance04EffectiveFrom?: string | null;
  allowance04EffectiveTo?: string | null;
  allowance05EffectiveFrom?: string | null;
  allowance05EffectiveTo?: string | null;
  allowance06EffectiveFrom?: string | null;
  allowance06EffectiveTo?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
  currentInTime?: string | null;
  currentOutTime?: string | null;
  onlineAttendanceSanctionedAt?: string | null;
  lastWorkingDay?: string | null;
};

export type RestEmployee = RestOf<IEmployee>;

export type NewRestEmployee = RestOf<NewEmployee>;

export type PartialUpdateRestEmployee = RestOf<PartialUpdateEmployee>;

export type EntityResponseType = HttpResponse<IEmployee>;
export type EntityArrayResponseType = HttpResponse<IEmployee[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/employees');
  public resourceUrlCommon = SERVER_API_URL + 'api/common/employees';

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employee: NewEmployee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employee);
    return this.http
      .post<RestEmployee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(employee: IEmployee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employee);
    return this.http
      .put<RestEmployee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(employee: PartialUpdateEmployee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employee);
    return this.http
      .patch<RestEmployee>(`${this.resourceUrl}/${this.getEmployeeIdentifier(employee)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEmployee>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmployee[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getAllMinimal(): Observable<EntityArrayResponseType> {
    return this.http
      .get<RestEmployee[]>(this.resourceUrlCommon + '/minimal', { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeeIdentifier(employee: Pick<IEmployee, 'id'>): number {
    return employee.id;
  }

  compareEmployee(o1: Pick<IEmployee, 'id'> | null, o2: Pick<IEmployee, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmployeeIdentifier(o1) === this.getEmployeeIdentifier(o2) : o1 === o2;
  }

  addEmployeeToCollectionIfMissing<Type extends Pick<IEmployee, 'id'>>(
    employeeCollection: Type[],
    ...employeesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const employees: Type[] = employeesToCheck.filter(isPresent);
    if (employees.length > 0) {
      const employeeCollectionIdentifiers = employeeCollection.map(employeeItem => this.getEmployeeIdentifier(employeeItem)!);
      const employeesToAdd = employees.filter(employeeItem => {
        const employeeIdentifier = this.getEmployeeIdentifier(employeeItem);
        if (employeeCollectionIdentifiers.includes(employeeIdentifier)) {
          return false;
        }
        employeeCollectionIdentifiers.push(employeeIdentifier);
        return true;
      });
      return [...employeesToAdd, ...employeeCollection];
    }
    return employeeCollection;
  }

  protected convertDateFromClient<T extends IEmployee | NewEmployee | PartialUpdateEmployee>(employee: T): RestOf<T> {
    return {
      ...employee,
      dateOfBirth: employee.dateOfBirth?.format(DATE_FORMAT) ?? null,
      dateOfMarriage: employee.dateOfMarriage?.format(DATE_FORMAT) ?? null,
      dateOfJoining: employee.dateOfJoining?.format(DATE_FORMAT) ?? null,
      dateOfConfirmation: employee.dateOfConfirmation?.format(DATE_FORMAT) ?? null,
      probationPeriodExtendedTo: employee.probationPeriodExtendedTo?.format(DATE_FORMAT) ?? null,
      passportIssuedDate: employee.passportIssuedDate?.format(DATE_FORMAT) ?? null,
      passportExpiryDate: employee.passportExpiryDate?.format(DATE_FORMAT) ?? null,
      probationPeriodEndDate: employee.probationPeriodEndDate?.format(DATE_FORMAT) ?? null,
      contractPeriodExtendedTo: employee.contractPeriodExtendedTo?.format(DATE_FORMAT) ?? null,
      contractPeriodEndDate: employee.contractPeriodEndDate?.format(DATE_FORMAT) ?? null,
      allowance01EffectiveFrom: employee.allowance01EffectiveFrom?.format(DATE_FORMAT) ?? null,
      allowance01EffectiveTo: employee.allowance01EffectiveTo?.format(DATE_FORMAT) ?? null,
      allowance02EffectiveFrom: employee.allowance02EffectiveFrom?.format(DATE_FORMAT) ?? null,
      allowance02EffectiveTo: employee.allowance02EffectiveTo?.format(DATE_FORMAT) ?? null,
      allowance03EffectiveFrom: employee.allowance03EffectiveFrom?.format(DATE_FORMAT) ?? null,
      allowance03EffectiveTo: employee.allowance03EffectiveTo?.format(DATE_FORMAT) ?? null,
      allowance04EffectiveFrom: employee.allowance04EffectiveFrom?.format(DATE_FORMAT) ?? null,
      allowance04EffectiveTo: employee.allowance04EffectiveTo?.format(DATE_FORMAT) ?? null,
      allowance05EffectiveFrom: employee.allowance05EffectiveFrom?.format(DATE_FORMAT) ?? null,
      allowance05EffectiveTo: employee.allowance05EffectiveTo?.format(DATE_FORMAT) ?? null,
      allowance06EffectiveFrom: employee.allowance06EffectiveFrom?.format(DATE_FORMAT) ?? null,
      allowance06EffectiveTo: employee.allowance06EffectiveTo?.format(DATE_FORMAT) ?? null,
      createdAt: employee.createdAt?.toJSON() ?? null,
      updatedAt: employee.updatedAt && employee.updatedAt.isValid() ? employee.updatedAt.toJSON() : undefined,
      currentInTime: employee.currentInTime?.toJSON() ?? null,
      currentOutTime: employee.currentOutTime?.toJSON() ?? null,
      onlineAttendanceSanctionedAt: employee.onlineAttendanceSanctionedAt?.toJSON() ?? null,
      lastWorkingDay: employee.lastWorkingDay?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restEmployee: RestEmployee): IEmployee {
    return {
      ...restEmployee,
      dateOfBirth: restEmployee.dateOfBirth ? dayjs(restEmployee.dateOfBirth) : undefined,
      dateOfMarriage: restEmployee.dateOfMarriage ? dayjs(restEmployee.dateOfMarriage) : undefined,
      dateOfJoining: restEmployee.dateOfJoining ? dayjs(restEmployee.dateOfJoining) : undefined,
      dateOfConfirmation: restEmployee.dateOfConfirmation ? dayjs(restEmployee.dateOfConfirmation) : undefined,
      probationPeriodExtendedTo: restEmployee.probationPeriodExtendedTo ? dayjs(restEmployee.probationPeriodExtendedTo) : undefined,
      passportIssuedDate: restEmployee.passportIssuedDate ? dayjs(restEmployee.passportIssuedDate) : undefined,
      passportExpiryDate: restEmployee.passportExpiryDate ? dayjs(restEmployee.passportExpiryDate) : undefined,
      probationPeriodEndDate: restEmployee.probationPeriodEndDate ? dayjs(restEmployee.probationPeriodEndDate) : undefined,
      contractPeriodExtendedTo: restEmployee.contractPeriodExtendedTo ? dayjs(restEmployee.contractPeriodExtendedTo) : undefined,
      contractPeriodEndDate: restEmployee.contractPeriodEndDate ? dayjs(restEmployee.contractPeriodEndDate) : undefined,
      allowance01EffectiveFrom: restEmployee.allowance01EffectiveFrom ? dayjs(restEmployee.allowance01EffectiveFrom) : undefined,
      allowance01EffectiveTo: restEmployee.allowance01EffectiveTo ? dayjs(restEmployee.allowance01EffectiveTo) : undefined,
      allowance02EffectiveFrom: restEmployee.allowance02EffectiveFrom ? dayjs(restEmployee.allowance02EffectiveFrom) : undefined,
      allowance02EffectiveTo: restEmployee.allowance02EffectiveTo ? dayjs(restEmployee.allowance02EffectiveTo) : undefined,
      allowance03EffectiveFrom: restEmployee.allowance03EffectiveFrom ? dayjs(restEmployee.allowance03EffectiveFrom) : undefined,
      allowance03EffectiveTo: restEmployee.allowance03EffectiveTo ? dayjs(restEmployee.allowance03EffectiveTo) : undefined,
      allowance04EffectiveFrom: restEmployee.allowance04EffectiveFrom ? dayjs(restEmployee.allowance04EffectiveFrom) : undefined,
      allowance04EffectiveTo: restEmployee.allowance04EffectiveTo ? dayjs(restEmployee.allowance04EffectiveTo) : undefined,
      allowance05EffectiveFrom: restEmployee.allowance05EffectiveFrom ? dayjs(restEmployee.allowance05EffectiveFrom) : undefined,
      allowance05EffectiveTo: restEmployee.allowance05EffectiveTo ? dayjs(restEmployee.allowance05EffectiveTo) : undefined,
      allowance06EffectiveFrom: restEmployee.allowance06EffectiveFrom ? dayjs(restEmployee.allowance06EffectiveFrom) : undefined,
      allowance06EffectiveTo: restEmployee.allowance06EffectiveTo ? dayjs(restEmployee.allowance06EffectiveTo) : undefined,
      createdAt: restEmployee.createdAt ? dayjs(restEmployee.createdAt) : undefined,
      updatedAt: restEmployee.updatedAt ? dayjs(restEmployee.updatedAt) : undefined,
      currentInTime: restEmployee.currentInTime ? dayjs(restEmployee.currentInTime) : undefined,
      currentOutTime: restEmployee.currentOutTime ? dayjs(restEmployee.currentOutTime) : undefined,
      onlineAttendanceSanctionedAt: restEmployee.onlineAttendanceSanctionedAt
        ? dayjs(restEmployee.onlineAttendanceSanctionedAt)
        : undefined,
      lastWorkingDay: restEmployee.lastWorkingDay ? dayjs(restEmployee.lastWorkingDay) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEmployee>): HttpResponse<IEmployee> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEmployee[]>): HttpResponse<IEmployee[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  getAllMinimalOfNgSelect(): Observable<EntityArrayResponseType> {
    return this.http.get<IEmployee[]>(this.resourceUrlCommon + '/minimal', { observe: 'response' }).pipe(
      map((res: EntityArrayResponseType) => {
        if (res.body) {
          res.body.forEach((employee: IEmployee) => {
            employee.fullName = `${employee.pin}-${employee.fullName}-${employee.designationName}`;
          });
        }
        return res;
      })
    );
  }
}
