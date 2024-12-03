import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Filter } from './filter.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { createRequestOption } from '../../core/request/request-util';
import { DATE_FORMAT } from '../../config/input.constants';
import dayjs from 'dayjs/esm';
import { IEmployee } from '../../entities/employee-custom/employee-custom.model';

type EntityResponseType = HttpResponse<IEmployee>;
type EntityArrayResponseType = HttpResponse<IEmployee[]>;

@Injectable({ providedIn: 'root' })
export class EmployeeSearchService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('/api');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  query(filter: Filter, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .post<IEmployee[]>(`${this.resourceUrl}/employee-mgt/employeeSearch`, filter, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryForAddressBook(filter: Filter, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .post<IEmployee[]>(`${this.resourceUrl}/common/address-book/employee-search`, filter, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryForBloodGroupInfos(filter: Filter, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.post<IEmployee[]>(`${this.resourceUrl}/common/blood-group-information/search`, filter, {
      params: options,
      observe: 'response',
    });
  }

  queryFinalSettlement(filter: Filter, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .post<IEmployee[]>(`${this.resourceUrl}/payroll-mgt/employee-search-final-settlement`, filter, {
        params: options,
        observe: 'response',
      })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
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
      });
    }
    return res;
  }
}
