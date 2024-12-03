import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';
import { IEmployeeDashboardAnalytics } from 'app/shared/model/employee-dashboard-analytics.model';
import { IEmployee } from '../employee-custom.model';
import { DATE_FORMAT } from '../../../config/input.constants';

type EntityResponseType = HttpResponse<IEmployee>;
type EntityArrayResponseType = HttpResponse<IEmployee[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeCustomService {
  public resourceUrl = SERVER_API_URL + 'api/employee-mgt/employees';
  public resourceUrlCommon = SERVER_API_URL + 'api/common/employees';

  constructor(protected http: HttpClient) {}

  create(employee: IEmployee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employee);
    return this.http
      .post<IEmployee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(employee: IEmployee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employee);
    return this.http
      .put<IEmployee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmployee>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findMinimal(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmployee>(`${this.resourceUrl}/minimal/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getCurrentEmployee(): Observable<EntityResponseType> {
    return this.http
      .get<IEmployee>(`${this.resourceUrlCommon}/current`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findCommon(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmployee>(`${this.resourceUrlCommon}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    return this.http
      .get<IEmployee[]>(this.resourceUrl, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getAllMinimal(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IEmployee[]>(this.resourceUrlCommon + '/minimal', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
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

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEmployeeDashboardAnalytics(): Observable<HttpResponse<IEmployeeDashboardAnalytics>> {
    return this.http.get<IEmployeeDashboardAnalytics>(this.resourceUrl + '/get-employee-dashboard-analytics', { observe: 'response' });
  }

  syncImages(force: boolean): Observable<HttpResponse<{}>> {
    let options = new HttpParams();
    options = options.append('force', String(force));
    return this.http.get(`${this.resourceUrl}/sync-images`, { params: options, observe: 'response' });
  }

  uploadXlsxFile(file: File): Observable<HttpResponse<boolean>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<boolean>(`${this.resourceUrl}/xlsx-upload`, formData, { observe: 'response' });
  }

  protected convertDateFromClient(employee: IEmployee): IEmployee {
    const copy: IEmployee = Object.assign({}, employee, {
      dateOfBirth: employee.dateOfBirth && employee.dateOfBirth.isValid() ? employee.dateOfBirth.format(DATE_FORMAT) : undefined,
      dateOfMarriage:
        employee.dateOfMarriage && employee.dateOfMarriage.isValid() ? employee.dateOfMarriage.format(DATE_FORMAT) : undefined,
      dateOfJoining: employee.dateOfJoining && employee.dateOfJoining.isValid() ? employee.dateOfJoining.format(DATE_FORMAT) : undefined,
      dateOfConfirmation:
        employee.dateOfConfirmation && employee.dateOfConfirmation.isValid() ? employee.dateOfConfirmation.format(DATE_FORMAT) : undefined,
      probationPeriodExtendedTo:
        employee.probationPeriodExtendedTo && employee.probationPeriodExtendedTo.isValid()
          ? employee.probationPeriodExtendedTo.format(DATE_FORMAT)
          : undefined,
      passportIssuedDate:
        employee.passportIssuedDate && employee.passportIssuedDate.isValid() ? employee.passportIssuedDate.format(DATE_FORMAT) : undefined,
      passportExpiryDate:
        employee.passportExpiryDate && employee.passportExpiryDate.isValid() ? employee.passportExpiryDate.format(DATE_FORMAT) : undefined,
      probationPeriodEndDate:
        employee.probationPeriodEndDate && employee.probationPeriodEndDate.isValid()
          ? employee.probationPeriodEndDate.format(DATE_FORMAT)
          : undefined,
      contractPeriodExtendedTo:
        employee.contractPeriodExtendedTo && employee.contractPeriodExtendedTo.isValid()
          ? employee.contractPeriodExtendedTo.format(DATE_FORMAT)
          : undefined,
      contractPeriodEndDate:
        employee.contractPeriodEndDate && employee.contractPeriodEndDate.isValid()
          ? employee.contractPeriodEndDate.format(DATE_FORMAT)
          : undefined,
      allowance01EffectiveFrom:
        employee.allowance01EffectiveFrom && employee.allowance01EffectiveFrom.isValid()
          ? employee.allowance01EffectiveFrom.format(DATE_FORMAT)
          : undefined,
      allowance01EffectiveTo:
        employee.allowance01EffectiveTo && employee.allowance01EffectiveTo.isValid()
          ? employee.allowance01EffectiveTo.format(DATE_FORMAT)
          : undefined,
      allowance02EffectiveFrom:
        employee.allowance02EffectiveFrom && employee.allowance02EffectiveFrom.isValid()
          ? employee.allowance02EffectiveFrom.format(DATE_FORMAT)
          : undefined,
      allowance02EffectiveTo:
        employee.allowance02EffectiveTo && employee.allowance02EffectiveTo.isValid()
          ? employee.allowance02EffectiveTo.format(DATE_FORMAT)
          : undefined,
      allowance03EffectiveFrom:
        employee.allowance03EffectiveFrom && employee.allowance03EffectiveFrom.isValid()
          ? employee.allowance03EffectiveFrom.format(DATE_FORMAT)
          : undefined,
      allowance03EffectiveTo:
        employee.allowance03EffectiveTo && employee.allowance03EffectiveTo.isValid()
          ? employee.allowance03EffectiveTo.format(DATE_FORMAT)
          : undefined,
      allowance04EffectiveFrom:
        employee.allowance04EffectiveFrom && employee.allowance04EffectiveFrom.isValid()
          ? employee.allowance04EffectiveFrom.format(DATE_FORMAT)
          : undefined,
      allowance04EffectiveTo:
        employee.allowance04EffectiveTo && employee.allowance04EffectiveTo.isValid()
          ? employee.allowance04EffectiveTo.format(DATE_FORMAT)
          : undefined,
      allowance05EffectiveFrom:
        employee.allowance05EffectiveFrom && employee.allowance05EffectiveFrom.isValid()
          ? employee.allowance05EffectiveFrom.format(DATE_FORMAT)
          : undefined,
      allowance05EffectiveTo:
        employee.allowance05EffectiveTo && employee.allowance05EffectiveTo.isValid()
          ? employee.allowance05EffectiveTo.format(DATE_FORMAT)
          : undefined,
      allowance06EffectiveFrom:
        employee.allowance06EffectiveFrom && employee.allowance06EffectiveFrom.isValid()
          ? employee.allowance06EffectiveFrom.format(DATE_FORMAT)
          : undefined,
      allowance06EffectiveTo:
        employee.allowance06EffectiveTo && employee.allowance06EffectiveTo.isValid()
          ? employee.allowance06EffectiveTo.format(DATE_FORMAT)
          : undefined,
      createdAt: employee.createdAt && employee.createdAt.isValid() ? employee.createdAt.toJSON() : undefined,
      updatedAt: employee.updatedAt && employee.updatedAt.isValid() ? employee.updatedAt.format(DATE_FORMAT) : undefined,
      currentInTime: employee.currentInTime && employee.currentInTime.isValid() ? employee.currentInTime.toJSON() : undefined,
      currentOutTime: employee.currentOutTime && employee.currentOutTime.isValid() ? employee.currentOutTime.toJSON() : undefined,
      onlineAttendanceSanctionedAt:
        employee.onlineAttendanceSanctionedAt && employee.onlineAttendanceSanctionedAt.isValid()
          ? employee.onlineAttendanceSanctionedAt.toJSON()
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? dayjs(res.body.dateOfBirth) : undefined;
      res.body.dateOfMarriage = res.body.dateOfMarriage ? dayjs(res.body.dateOfMarriage) : undefined;
      res.body.dateOfJoining = res.body.dateOfJoining ? dayjs(res.body.dateOfJoining) : undefined;
      res.body.dateOfConfirmation = res.body.dateOfConfirmation ? dayjs(res.body.dateOfConfirmation) : undefined;
      res.body.probationPeriodExtendedTo = res.body.probationPeriodExtendedTo ? dayjs(res.body.probationPeriodExtendedTo) : undefined;
      res.body.passportIssuedDate = res.body.passportIssuedDate ? dayjs(res.body.passportIssuedDate) : undefined;
      res.body.passportExpiryDate = res.body.passportExpiryDate ? dayjs(res.body.passportExpiryDate) : undefined;
      res.body.probationPeriodEndDate = res.body.probationPeriodEndDate ? dayjs(res.body.probationPeriodEndDate) : undefined;
      res.body.contractPeriodExtendedTo = res.body.contractPeriodExtendedTo ? dayjs(res.body.contractPeriodExtendedTo) : undefined;
      res.body.contractPeriodEndDate = res.body.contractPeriodEndDate ? dayjs(res.body.contractPeriodEndDate) : undefined;
      res.body.allowance01EffectiveFrom = res.body.allowance01EffectiveFrom ? dayjs(res.body.allowance01EffectiveFrom) : undefined;
      res.body.allowance01EffectiveTo = res.body.allowance01EffectiveTo ? dayjs(res.body.allowance01EffectiveTo) : undefined;
      res.body.allowance02EffectiveFrom = res.body.allowance02EffectiveFrom ? dayjs(res.body.allowance02EffectiveFrom) : undefined;
      res.body.allowance02EffectiveTo = res.body.allowance02EffectiveTo ? dayjs(res.body.allowance02EffectiveTo) : undefined;
      res.body.allowance03EffectiveFrom = res.body.allowance03EffectiveFrom ? dayjs(res.body.allowance03EffectiveFrom) : undefined;
      res.body.allowance03EffectiveTo = res.body.allowance03EffectiveTo ? dayjs(res.body.allowance03EffectiveTo) : undefined;
      res.body.allowance04EffectiveFrom = res.body.allowance04EffectiveFrom ? dayjs(res.body.allowance04EffectiveFrom) : undefined;
      res.body.allowance04EffectiveTo = res.body.allowance04EffectiveTo ? dayjs(res.body.allowance04EffectiveTo) : undefined;
      res.body.allowance05EffectiveFrom = res.body.allowance05EffectiveFrom ? dayjs(res.body.allowance05EffectiveFrom) : undefined;
      res.body.allowance05EffectiveTo = res.body.allowance05EffectiveTo ? dayjs(res.body.allowance05EffectiveTo) : undefined;
      res.body.allowance06EffectiveFrom = res.body.allowance06EffectiveFrom ? dayjs(res.body.allowance06EffectiveFrom) : undefined;
      res.body.allowance06EffectiveTo = res.body.allowance06EffectiveTo ? dayjs(res.body.allowance06EffectiveTo) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
      res.body.updatedAt = res.body.updatedAt ? dayjs(res.body.updatedAt) : undefined;
      res.body.currentInTime = res.body.currentInTime ? dayjs(res.body.currentInTime) : undefined;
      res.body.currentOutTime = res.body.currentOutTime ? dayjs(res.body.currentOutTime) : undefined;
      res.body.onlineAttendanceSanctionedAt = res.body.onlineAttendanceSanctionedAt
        ? dayjs(res.body.onlineAttendanceSanctionedAt)
        : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((employee: IEmployee) => {
        employee.dateOfBirth = employee.dateOfBirth ? dayjs(employee.dateOfBirth) : undefined;
        employee.dateOfMarriage = employee.dateOfMarriage ? dayjs(employee.dateOfMarriage) : undefined;
        employee.dateOfJoining = employee.dateOfJoining ? dayjs(employee.dateOfJoining) : undefined;
        employee.dateOfConfirmation = employee.dateOfConfirmation ? dayjs(employee.dateOfConfirmation) : undefined;
        employee.probationPeriodExtendedTo = employee.probationPeriodExtendedTo ? dayjs(employee.probationPeriodExtendedTo) : undefined;
        employee.passportIssuedDate = employee.passportIssuedDate ? dayjs(employee.passportIssuedDate) : undefined;
        employee.passportExpiryDate = employee.passportExpiryDate ? dayjs(employee.passportExpiryDate) : undefined;
        employee.probationPeriodEndDate = employee.probationPeriodEndDate ? dayjs(employee.probationPeriodEndDate) : undefined;
        employee.contractPeriodExtendedTo = employee.contractPeriodExtendedTo ? dayjs(employee.contractPeriodExtendedTo) : undefined;
        employee.contractPeriodEndDate = employee.contractPeriodEndDate ? dayjs(employee.contractPeriodEndDate) : undefined;
        employee.allowance01EffectiveFrom = employee.allowance01EffectiveFrom ? dayjs(employee.allowance01EffectiveFrom) : undefined;
        employee.allowance01EffectiveTo = employee.allowance01EffectiveTo ? dayjs(employee.allowance01EffectiveTo) : undefined;
        employee.allowance02EffectiveFrom = employee.allowance02EffectiveFrom ? dayjs(employee.allowance02EffectiveFrom) : undefined;
        employee.allowance02EffectiveTo = employee.allowance02EffectiveTo ? dayjs(employee.allowance02EffectiveTo) : undefined;
        employee.allowance03EffectiveFrom = employee.allowance03EffectiveFrom ? dayjs(employee.allowance03EffectiveFrom) : undefined;
        employee.allowance03EffectiveTo = employee.allowance03EffectiveTo ? dayjs(employee.allowance03EffectiveTo) : undefined;
        employee.allowance04EffectiveFrom = employee.allowance04EffectiveFrom ? dayjs(employee.allowance04EffectiveFrom) : undefined;
        employee.allowance04EffectiveTo = employee.allowance04EffectiveTo ? dayjs(employee.allowance04EffectiveTo) : undefined;
        employee.allowance05EffectiveFrom = employee.allowance05EffectiveFrom ? dayjs(employee.allowance05EffectiveFrom) : undefined;
        employee.allowance05EffectiveTo = employee.allowance05EffectiveTo ? dayjs(employee.allowance05EffectiveTo) : undefined;
        employee.allowance06EffectiveFrom = employee.allowance06EffectiveFrom ? dayjs(employee.allowance06EffectiveFrom) : undefined;
        employee.allowance06EffectiveTo = employee.allowance06EffectiveTo ? dayjs(employee.allowance06EffectiveTo) : undefined;
        employee.createdAt = employee.createdAt ? dayjs(employee.createdAt) : undefined;
        employee.updatedAt = employee.updatedAt ? dayjs(employee.updatedAt) : undefined;
        employee.currentInTime = employee.currentInTime ? dayjs(employee.currentInTime) : undefined;
        employee.currentOutTime = employee.currentOutTime ? dayjs(employee.currentOutTime) : undefined;
        employee.onlineAttendanceSanctionedAt = employee.onlineAttendanceSanctionedAt
          ? dayjs(employee.onlineAttendanceSanctionedAt)
          : undefined;
      });
    }
    return res;
  }

  getEmployees(): Observable<HttpResponse<IEmployee[]>> {
    return this.http.get<IEmployee[]>(this.resourceUrlCommon + '/all-employees', { observe: 'response' });
  }
}
