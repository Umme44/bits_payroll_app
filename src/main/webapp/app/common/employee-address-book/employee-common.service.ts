import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { createRequestOption } from '../../core/request/request-util';
import dayjs from 'dayjs/esm';
import { IEmployee } from '../../entities/employee-custom/employee-custom.model';

type EntityResponseType = HttpResponse<IEmployee>;
type EntityArrayResponseType = HttpResponse<IEmployee[]>;

@Injectable({
  providedIn: 'root',
})
export class EmployeeCommonService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/common/employees');
  public resourceUrlForMyTeamMembers = this.applicationConfigService.getEndpointFor('api/common/my-team-member/list');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getAllMinimal(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IEmployee[]>(this.resourceUrl + '/minimal', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getMyTeamMembers(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IEmployee[]>(this.resourceUrlForMyTeamMembers, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getAllMinimalActive(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IEmployee[]>(this.resourceUrl + '/minimal-active', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getSuggestions(): Observable<HttpResponse<string[]>> {
    return this.http.get<string[]>(this.resourceUrl + '/address-book-suggestions', { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmployee[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getCurrentEmployee(): Observable<EntityResponseType> {
    return this.http
      .get<IEmployee>(`${this.resourceUrl}/current`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getCurrentEmployeeMinimal(): Observable<EntityResponseType> {
    return this.http
      .get<IEmployee>(`${this.resourceUrl}/current-employee/minimal-info`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getCurrentDP(): Observable<Blob> {
    const url = `${this.resourceUrl}/current/photo`;
    return this.http.get(url, { responseType: 'blob' });
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
    }
    return res;
  }

  public convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
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
      });
    }
    return res;
  }

  normalizeEmployeeName(fullName: any): string {
    //example: normalize Abul Kasem Mohammad Shohel Ahmed Mostafa => A. K. M. S. A. Mostafa
    const employeeName = fullName.toString();
    //check name is greater than 35 characters long
    if (employeeName.length > 35) {
      let abbreviateForm = '';
      const names = employeeName.split(' ');
      const excludingLastPart = names.slice(0, names.length - 1);
      excludingLastPart.forEach((x: string) => {
        abbreviateForm = abbreviateForm + x.charAt(0) + '. ';
      });
      return abbreviateForm + names[names.length - 1];
    }
    return fullName;
  }

  employeeInfoForLeaveApply(): Observable<HttpResponse<IEmployee>> {
    return this.http.get<IEmployee>(this.resourceUrl + '/current-employee/info/leave-apply', { observe: 'response' });
  }
}
